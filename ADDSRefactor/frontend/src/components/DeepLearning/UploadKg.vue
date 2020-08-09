<template>
  <el-main>
    <div class="top-bar">
      <div class="option-bar">
        <div class="header-bar">
          <span>My Knowledge Graph</span>
        </div>
        <div class="operation-btn-bar">
          <el-button type="primary" @click="dialogFormVisible = true">Add KG</el-button>
        </div>
      </div>
    </div>
    <el-dialog title="Upload Knowledge Graph" :visible.sync="dialogFormVisible" width="640px"
               append-to-body :close-on-click-modal="false" :close-on-press-escape="false" :show-close="false">
      <el-form ref="uploadKgForm" :model="uploadKgForm" :rules="rules" label-position="left" label-width="130px">
        <el-form-item label="Name" prop="name">
          <el-input v-model="uploadKgForm.name"></el-input>
        </el-form-item>
        <el-form-item label="Description" prop="desc">
          <el-input type="textarea" :rows="5" v-model="uploadKgForm.desc"></el-input>
        </el-form-item>
        <el-form-item label="KG Format" prop="kgFormat">
          <span><b>entity,relation,entity</b>&emsp;(Require CSV file type)</span>&emsp;
          <el-tooltip effect="dark" placement="top">
            <div slot="content">
              header,relation,tail<br/>
              m,knows,n<br/>
              x,knows,y
            </div>
            <el-button type="info" size="mini">Sample</el-button>
          </el-tooltip>
        </el-form-item>
        <el-form-item label="File Upload" prop="fileUpload">
          <el-upload
            ref="upload"
            action=""
            :multiple="false"
            :file-list="fileList"
            :show-file-list="true"
            :http-request="uploadKg"
            :auto-upload="false"
          >
            <el-button slot="trigger" type="primary" class="browse-btn">Browse</el-button>
<!--            <div slot="tip" class="el-upload__tip">只能上传 csv 文件，且数据格式同 sample.txt</div>-->
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitUpload">Submit</el-button>
        <el-button @click="cancelAddKG">Cancel</el-button>
      </div>
    </el-dialog>
    <div class="content-div scrollable-div">
      <el-scrollbar>
        <div class="content-parent">
          <div class="content">
            <div class="item-list">
              <el-table ref="kgTable" :data="kgTableData" :max-height="kgTableHeight">
                <template slot="empty">
                  <span>{{kgTableEmptyText}}</span>
                </template>
                <el-table-column prop="name" label="Name" min-width="20%" align="center" :show-overflow-tooltip="true"></el-table-column>
                <el-table-column prop="desc" label="Description" min-width="30%" align="center" :show-overflow-tooltip="true"></el-table-column>
                <el-table-column prop="operations" label="Operations" min-width="50%" align="center">
                  <template slot-scope="scope">
                    <el-button type="primary" plain size="small" @click="goToKnowledgeGraph(scope.row.id)">Show this KG</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </div>
        </div>
      </el-scrollbar>
    </div>
  </el-main>
</template>

<script>
    export default {
        name: "UploadKg",
        data() {
            return {
                kgTableHeight: 200,
                kgTableData: [],
                kgTableEmptyText: 'Loading...',
                dialogFormVisible: false,
                uploadKgForm: {
                    name: '',
                    desc: ''
                },
                fileList: [],
                rules: {
                    name: [
                        {required: true, message: 'Please input name', trigger: 'blur'}
                    ],
                    desc: [
                        {required: true, message: 'Please input description', trigger: 'blur'}
                    ]
                }
            };
        },
        methods: {
            loadKg() {
                this.$axios({
                    method: 'get',
                    url: '/kg/doctor/' + this.$store.state.user.id
                }).then(res => {
                    // console.log(res.data);
                    this.kgTableData.length = 0;
                    for (let kg in res.data) {
                        if (res.data.hasOwnProperty(kg)) {
                            this.kgTableData.push({
                                id: res.data[kg].id,
                                name: res.data[kg].name,
                                desc: res.data[kg].desc
                            });
                        }
                    }
                    if (this.kgTableData.length === 0) {
                        this.kgTableEmptyText = 'No Knowledge Graph';
                    }
                }).catch(error => {
                    console.log(error);
                });
            },
            submitUpload() {
                this.$refs.upload.submit();
            },
            uploadKg(content) {
                let params = new FormData();
                params.append("name", this.uploadKgForm.name);
                params.append("desc", this.uploadKgForm.desc);
                params.append("file", content.file);

                this.$axios({
                    method: 'post',
                    url: '/kg/upload/' + this.$store.state.user.id,
                    data: params
                }).then(res => {
                    // console.log(res.data);
                    this.closeDialogForm();
                    this.loadKg();
                }).catch(error => {
                    this.$message({
                        type: 'error',
                        message: '[ERROR: UploadKg -> loadKg()] Check Console plz! ',
                        showClose: true
                    });
                    console.log(error);
                });
            },
            cancelAddKG() {
                this.closeDialogForm();
            },
            closeDialogForm() {
                this.dialogFormVisible = false;
                this.$refs['uploadKgForm'].resetFields();
                this.fileList = [];
            },
            goToKnowledgeGraph(kgId) {
                this.$notify.info({
                    title: 'Notification',
                    message: 'Go to Knowledge Graph. '
                });
                console.log(kgId);
            }
        },
        created() {
            this.loadKg();
        },
        mounted() {
            this.$nextTick(function () {
                this.kgTableHeight = window.innerHeight - this.$refs.kgTable.$el.offsetTop - 190;
                let self = this;
                window.onresize = function() {
                    self.kgTableHeight = window.innerHeight - self.$refs.kgTable.$el.offsetTop - 190;
                }
            });
        }
    }
</script>

<style scoped>
  .el-main {
    position: fixed;
    top: 80px;
    left: 260px;
    right: 30px;
    bottom: 0;
    margin: 20px;
    padding: 10px;
  }

  .option-bar {
    display: inline-block;
    position: fixed;
    left: 280px;
    right: 40px;
  }

  .header-bar {
    float: left;
    margin: 0 20px;
  }

  .header-bar span {
    font-size: 20px;
    font-weight: bold;
    float: left;
  }

  .operation-btn-bar {
    float: right;
    margin: 0 20px;
    overflow: hidden;
  }

  .el-form {
    padding: 0 10px;
  }

  .content-div {
    position: fixed;
    top: 130px;
    left: 280px;
    right: 40px;
    bottom: 20px;
    z-index: -100;
  }

  .content {
    margin: 10px;
    padding: 30px;
  }

  .browse-btn {
    width: 90px;
    margin: 5px;
  }
</style>
