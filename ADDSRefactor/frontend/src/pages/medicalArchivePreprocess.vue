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
              :data="tableData" height="500" style="width: 100%;">
            <template slot="empty">
              <span>Loading ...</span>
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
                <el-button type="primary" size="small" :disabled="!scope.row.finished">Download</el-button>
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
            <el-form-item label="Name" prop="name">
              <el-input v-model="uploadForm.name"></el-input>
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
              url: '/archive/doctor/' + this.$store.state.user.id,
            }).then(res => {
              this.tableData.length = 0;
              for (let row in res.data) {
                if (res.data.hasOwnProperty(row)) {
                  this.tableData.push({
                    title: res.data[row].title,
                    category: res.data[row].category,
                    finished: res.data[row].status
                  });
                }
              }
              console.log(res.data)
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
            params.append("name", this.uploadForm.name);
            params.append("category", this.uploadForm.category);
            params.append("desc", this.uploadForm.desc);
            params.append("file", content.file);

            this.$axios({
              method: 'post',
              url: '/archive/doctor/' + this.$store.state.user.id,
              data: params
            }).then(res => {
              this.$refs['uploadForm'].resetFields();
              this.uploadFormVisible = false;
              this.loadArchiveList();
            }).catch(error => {
              this.$message({
                type: 'error',
                message: 'Failed to upload! Check console.',
                showClose: true
              });
              console.log(error);
            });
          }
        },
        data: function() {
          return {
            uploadFormVisible: false,
            fileList: [],
            uploadForm: {
              name: '',
              category: 'Orthopaedic',
              desc: '',
            },
            rules: {
              name: [
                {required: true, message: 'Please input name', trigger: 'blur'}
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
            tableData:[]
          }
        },
        created() {
            this.loadArchiveList();
        },
    }
</script>

<style scoped>
  .content-div {
    position: relative;
    top: 110px;
  }

  #paginator {
    text-align: center;
  }
</style>
