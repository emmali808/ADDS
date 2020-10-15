<template>
  <div>
    <el-container>
      <NavigationBar/>
      <el-container class="content-div">

        <el-header>
          <el-button type="primary" @click="openUploadDialog">Upload Medical Archive</el-button>
        </el-header>

        <el-main>
          <el-table
            ref="table" :data="tableData" :max-height="tableHeight" style="width: 100%;">
            <template slot="empty">
              <span>{{ promptText }}</span>
            </template>
            <el-table-column prop="title" label="Title" align="center"></el-table-column>
            <el-table-column prop="category" label="Category" align="center"></el-table-column>
            <el-table-column label="Status" align="center">
              <template slot-scope="scope">
                <el-tag
                    :type="scope.row.finished ? 'success' : 'danger'"
                    disable-transitions>{{scope.row.finished ? 'Processed' : 'Processing'}}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="Options" align="center">
              <template slot-scope="scope">
                <el-button
                    type="primary"
                    size="small"
                    :disabled="!scope.row.finished"
                    @click="downloadArchive(scope.row.id)">
                  Download
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-main>

        <el-dialog
            title="Upload Medical Archive"
            :visible.sync="uploadFormVisible"
            width="640px"
            append-to-body
            :close-on-click-modal="false"
            :close-on-press-escape="false"
            :show-close="false">
          <el-form ref="uploadForm" :model="uploadForm" :rules="rules" label-position="left" label-width="130px">
            <el-form-item label="Title" prop="title">
              <el-input v-model="uploadForm.title"></el-input>
            </el-form-item>
            <el-form-item label="Category" prop="category">
              <el-select v-model="uploadForm.category" placeholder="Please select category">
                <el-option label="Orthopaedic" value="Orthopaedic"></el-option>
                <el-option label="Hepatobiliary surgery" value="Hepatobiliary surgery"></el-option>
                <el-option label="Cardiovascular Surgery" value="Cardiovascular Surgery"></el-option>
                <el-option label="Endocrinology" value="Endocrinology"></el-option>
                <el-option label="Cardiology" value="Cardiology"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="Upload File" prop="uploadFile">
              <el-upload
                ref="upload"
                action=""
                accept=".zip"
                :multiple="false"
                :file-list="fileList"
                :show-file-list="true"
                :http-request="uploadMedicalArchive"
                :auto-upload="false">
                <el-button slot="trigger" type="primary">Browse</el-button>
              </el-upload>
            </el-form-item>
            <el-form-item label="Description" prop="desc">
              <el-input type="textarea" :rows="5" v-model="uploadForm.desc"></el-input>
            </el-form-item>
          </el-form>
          <div slot="footer">
            <el-button type="primary" @click="upload">Submit</el-button>
            <el-button @click="cancelUpload">Cancel</el-button>
          </div>
        </el-dialog>

      </el-container>
    </el-container>
  </div>
</template>

<script>
    import NavigationBar from "../components/NavigationBar";
    export default {
        name: "medicalArchivePreprocess",
        components: {
            NavigationBar
        },
        methods:{
          loadArchiveList() {
            this.$axios({
              method: 'get',
              url: '/archive/user/' + this.$store.state.user.id,
            }).then(res => {
              this.tableData.length = 0;
              for (let row in res.data) {
                if (res.data.hasOwnProperty(row)) {
                  this.tableData.push({
                    title: res.data[row].title,
                    category: res.data[row].category,
                    finished: res.data[row].status,
                    id: res.data[row].id
                  });
                }
              }
              if (res.data.length == 0) {
                this.promptText = 'No Medical Archive'
              }
            }).catch(error => {
              console.log(error);
            });
          },
          openUploadDialog() {
            this.uploadFormVisible = true;
          },
          upload() {
            this.$refs.upload.submit();
          },
          cancelUpload() {
            this.$refs['uploadForm'].resetFields();
            this.uploadFormVisible = false;
          },
          uploadMedicalArchive(content) {
            let params = new FormData();
            params.append("title", this.uploadForm.title);
            params.append("category", this.uploadForm.category);
            params.append("desc", this.uploadForm.desc);
            params.append("file", content.file);

            this.$axios({
              method: 'post',
              url: '/archive/user/' + this.$store.state.user.id,
              data: params
            }).then(res => {
              this.$refs['uploadForm'].resetFields();
              this.uploadFormVisible = false;
              this.$message({
                type: 'success',
                message: 'Successully uploaded medical archive!',
                showClose: true
              });
              this.loadArchiveList();
              this.$axios({
                method: 'post',
                url: '/preprocess/' + res.data['medicalArchiveId'],
              });
            }).catch(error => {
              this.$message({
                type: 'error',
                message: 'Failed to upload! Check console.',
                showClose: true
              });
              console.log(error);
            });
          },
          downloadArchive(row) {
            let link = document.createElement('a');
            link.style.display = 'none';
            link.href = window.location.origin + this.$axios.defaults.baseURL + '/archive/download/' + row;
            document.body.appendChild(link);
            link.click();
            link.remove();
          }
        },
        data: function() {
          return {
            uploadFormVisible: false,
            fileList: [],
            uploadForm: {
              title: '',
              category: 'Orthopaedic',
              desc: '',
            },
            rules: {
              title: [
                {required: true, message: 'Please input title', trigger: 'blur'}
              ],
              category: [
                {required: true, message: 'Please select category', trigger: 'blur'}
              ],
              uploadFile: [
                {required: true, message: 'Please select file', trigger: 'change'}
              ],
              desc: [
                {required: true, message: 'Please input description', trigger: 'blur'}
              ]
            },
            tableData:[],
            promptText: 'Loading ...',
            tableHeight: 200,
          }
        },
        created() {
          this.loadArchiveList();
        },
        mounted() {
          this.$nextTick(function () {
              this.tableHeight = window.innerHeight - this.$refs.table.$el.offsetTop - 190;
              let self = this;
              window.onresize = function() {
                  self.tableHeight = window.innerHeight - self.$refs.table.$el.offsetTop - 190;
              }
          });
        }
    }
</script>

<style scoped>
  .content-div {
    position: relative;
    top: 110px;
  }
</style>
