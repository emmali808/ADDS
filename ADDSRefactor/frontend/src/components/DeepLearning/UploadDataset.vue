<template>
  <el-main>
    <div class="top-bar">
      <div class="option-bar">
        <div class="header-bar">
          <span>My Dataset</span>
        </div>
        <div class="operation-btn-bar">
          <el-button type="primary" @click="newDatasetDialog">Add Dataset</el-button>
        </div>
      </div>
    </div>
    <el-dialog title="Add Dataset" :visible.sync="addDatasetFormVisible" width="640px"
               append-to-body :close-on-click-modal="false" :close-on-press-escape="false" :show-close="false">
      <el-steps :active="stepsActive" finish-status="success" align-center>
        <el-step title="Step 1" description="Create dataset"></el-step>
        <el-step title="Step 2" description="Upload files"></el-step>
      </el-steps>
      <el-form ref="addDatasetForm" :model="addDatasetForm" :rules="rules" label-position="left" label-width="130px">
        <el-form-item label="Name" prop="name">
          <el-input v-model="addDatasetForm.name"></el-input>
        </el-form-item>
        <el-form-item label="Description" prop="desc">
          <el-input type="textarea" :rows="5" v-model="addDatasetForm.desc"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="addDataset">Submit</el-button>
        <el-button @click="cancelAddDataset">Cancel</el-button>
      </div>
    </el-dialog>

    <el-dialog title="Upload Dataset" :visible.sync="uploadDatasetFormVisible" width="640px"
               append-to-body :close-on-click-modal="false" :close-on-press-escape="false" :show-close="false">
      <el-steps :active="stepsActive" finish-status="success" align-center>
        <el-step title="Step 1" description="Create dataset"></el-step>
        <el-step title="Step 2" description="Upload files"></el-step>
      </el-steps>
      <el-form ref="uploadDatasetForm" :model="uploadDatasetForm" label-position="left" label-width="70px">
        <el-form-item v-if="!trainSetExist" label="TrainSet" prop="trainSet">
          <el-upload
            ref="uploadTrainSet"
            action=""
            :multiple="false"
            :file-list="trainSetFileList"
            :show-file-list="true"
            :http-request="uploadDatasetFile"
            :auto-upload="false"
          >
            <el-button slot="trigger" type="primary" size="mini" class="browse-btn">Browse</el-button>
            <el-button type="success" size="mini" class="browse-btn" @click="submitUploadTrainSetFile">Upload</el-button>
          </el-upload>
        </el-form-item>

        <el-form-item v-if="!testSetExist" label="TestSet" prop="trainSet">
          <el-upload
            ref="uploadTestSet"
            action=""
            :multiple="false"
            :file-list="testSetFileList"
            :show-file-list="true"
            :http-request="uploadDatasetFile"
            :auto-upload="false"
          >
            <el-button slot="trigger" type="primary" size="mini" class="browse-btn">Browse</el-button>
            <el-button type="success" size="mini" class="browse-btn" @click="submitUploadTestSetFile">Upload</el-button>
          </el-upload>
        </el-form-item>

        <el-form-item v-if="!devSetExist" label="DevSet" prop="trainSet">
          <el-upload
            ref="uploadDevSet"
            action=""
            :multiple="false"
            :file-list="devSetFileList"
            :show-file-list="true"
            :http-request="uploadDatasetFile"
            :auto-upload="false"
          >
            <el-button slot="trigger" type="primary" size="mini" class="browse-btn">Browse</el-button>
            <el-button type="success" size="mini" class="browse-btn" @click="submitUploadDevSetFile">Upload</el-button>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
<!--        <el-button type="primary" @click="submitUpload">Submit</el-button>-->
        <el-button @click="cancelUploadDataset">Finish</el-button>
      </div>
    </el-dialog>

    <div class="content-div scrollable-div">
      <el-scrollbar>
        <div class="content-parent">
          <div class="content">
            <div class="item-list">
              <el-table ref="datasetTable" :data="datasetTableData" :max-height="datasetTableHeight">
                <template slot="empty">
                  <span>{{datasetTableEmptyText}}</span>
                </template>
                <el-table-column prop="name" label="Name" min-width="20%" align="center" :show-overflow-tooltip="true"></el-table-column>
                <el-table-column prop="desc" label="Description" min-width="30%" align="center" :show-overflow-tooltip="true"></el-table-column>
                <el-table-column prop="status" label="Status" min-width="15%" align="center">
                  <template slot-scope="scope">
                    <el-popover placement="top" :title="scope.row.name" trigger="hover">
                      <div>
                        <el-button :type="scope.row.trainSetName===''?'danger':'success'" plain size="mini" class="dataset-btn">TrainSet</el-button>
                        <el-button :type="scope.row.testSetName===''?'danger':'success'" plain size="mini" class="dataset-btn">TestSet</el-button>
                        <el-button :type="scope.row.devSetName===''?'danger':'success'" plain size="mini" class="dataset-btn">DevSet</el-button>
                      </div>
                      <el-tag slot="reference" :type="scope.row.status?'success':'danger'" @click="showMsg(scope.row.status)">{{scope.row.status?"Available":"Not Available"}}</el-tag>
                    </el-popover>
                  </template>
                </el-table-column>
                <el-table-column prop="operations" label="Operations" min-width="35%" align="center">
                  <template slot-scope="scope">
                    <div v-if="scope.row.status">
                      <el-button type="primary" plain size="small" @click="goToModelEvaluation(scope.row.id)">Model Evaluation</el-button>
                      <el-button type="primary" plain size="small" @click="goToAutoSelection(scope.row.id)">Auto Selection</el-button>
                    </div>
                    <div v-else>
                      <el-button type="primary" plain size="small" @click="openUploadDatasetForm(scope.row.id, scope.row.trainSetName, scope.row.testSetName, scope.row.devSetName)">Upload</el-button>
                    </div>
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
        name: "UploadDataset",
        data() {
            return {
                datasetTableHeight: 200,
                datasetTableData: [],
                datasetTableEmptyText: 'Loading...',
                stepsActive: 0,
                addDatasetFormVisible: false,
                addDatasetForm: {
                    name: '',
                    desc: '',
                },
                rules: {
                    name: [
                        {required: true, message: 'Please input name', trigger: 'blur'}
                        // {min: 1, max: 10, message: 'Name length is between 1 and 10', trigger: 'blur'}
                    ],
                    desc: [
                        {required: true, message: 'Please input description', trigger: 'blur'}
                    ]
                },
                uploadDatasetFormVisible: false,
                uploadDatasetForm: {
                    datasetId: 0,
                    type: ''
                },
                // uploadDatasetFormRules: {
                //   fileUpload: [
                //     {required: true, message: '', trigger: 'blur'}
                //   ]
                // },
                trainSetExist: false,
                testSetExist: false,
                devSetExist: false,
                trainSetFileList: [],
                testSetFileList: [],
                devSetFileList: []
            };
        },
        methods: {
            loadDataset() {
                this.$axios({
                    method: 'get',
                    url: '/doctor/' + this.$store.state.user.id + '/dataSets',
                }).then(res => {
                    // console.log(res.data);
                    this.datasetTableData.length = 0;
                    for (let dataset in res.data) {
                        if (res.data.hasOwnProperty(dataset)) {
                            let status = true;
                            let trainSetName = '';
                            let testSetName = '';
                            let devSetName = '';
                            if (res.data[dataset].train_name === "" || res.data[dataset].train_name === null) {
                                trainSetName = '';
                                status = false;
                            } else {
                                trainSetName = res.data[dataset].train_name;
                            }
                            if (res.data[dataset].test_name === "" || res.data[dataset].test_name === null) {
                                testSetName = '';
                                status = false;
                            } else {
                                testSetName = res.data[dataset].test_name;
                            }
                            if (res.data[dataset].dev_name === "" || res.data[dataset].dev_name === null) {
                                devSetName = '';
                                status = false;
                            } else {
                                devSetName = res.data[dataset].dev_name;
                            }
                            this.datasetTableData.push({
                                id: res.data[dataset].id,
                                name: res.data[dataset].dataset_name,
                                desc: res.data[dataset].dataset_desc,
                                status: status,
                                trainSetName: trainSetName,
                                testSetName: testSetName,
                                devSetName: devSetName
                            });
                        }
                    }
                    if (this.datasetTableData.length === 0) {
                        this.datasetTableEmptyText = 'No Dataset';
                    }
                }).catch(error => {
                    console.log(error);
                });
            },
            newDatasetDialog() {
                this.stepsActive = 0;
                this.addDatasetFormVisible = true;
            },
            addDataset() {
                this.$axios({
                    method: 'post',
                    url: '/doctor/' + this.$store.state.user.id + '/dataSet',
                    data: {
                        dataset_name: this.addDatasetForm.name,
                        dataset_desc: this.addDatasetForm.desc
                    }
                }).then(res => {
                    // console.log(res.data);
                    this.closeAddDatasetForm();
                    this.loadDataset();
                    this.$notify({
                        title: 'Success',
                        message: 'Successfully create dataset! ',
                        type: 'success'
                    });
                    this.openUploadDatasetForm(res.data, '', '', '');
                }).catch(error => {
                    console.log(error);
                });
            },
            cancelAddDataset() {
                this.closeAddDatasetForm();
            },
            closeAddDatasetForm() {
                this.addDatasetFormVisible = false;
                this.$refs['addDatasetForm'].resetFields();
            },
            submitUploadTrainSetFile() {
                // if (this.trainSetFileList.length === 0) {
                //     this.$notify.info({
                //         title: 'Notification',
                //         message: 'Please choose a file! '
                //     });
                // }
                // console.log(this.trainSetFileList);
                this.uploadDatasetForm.type = 'train';
                this.$refs.uploadTrainSet.submit();
            },
            submitUploadTestSetFile() {
                this.uploadDatasetForm.type = 'test';
                this.$refs.uploadTestSet.submit();
            },
            submitUploadDevSetFile() {
                this.uploadDatasetForm.type = 'dev';
                this.$refs.uploadDevSet.submit();
            },
            uploadDatasetFile(content) {
                let params = new FormData();
                params.append("dId", this.uploadDatasetForm.datasetId);
                params.append("type", this.uploadDatasetForm.type);
                // params.append("name", this.uploadDatasetForm.name);
                params.append("file", content.file);
                // console.log(content.file);

                this.$axios({
                    method: 'post',
                    url: '/doctor/' + this.$store.state.user.id + '/dataSets',
                    data: params
                }).then(res => {
                    // console.log(res.data);
                    this.$notify({
                        title: 'Success',
                        message: 'Successfully upload file! ',
                        type: 'success'
                    });
                }).catch(error => {
                    console.log(error);
                    this.$notify.error({
                        title: 'Error',
                        message: 'Wrong file format! '
                    });
                });
            },
            cancelUploadDataset() {
                this.closeUploadDatasetForm();
                this.loadDataset();
            },
            openUploadDatasetForm(datasetId, trainSet, testSet, devSet) {
                this.stepsActive = 1;
                this.uploadDatasetForm.datasetId = datasetId;
                this.trainSetExist = trainSet !== '';
                this.testSetExist = testSet !== '';
                this.devSetExist = devSet !== '';
                this.uploadDatasetFormVisible = true;
            },
            closeUploadDatasetForm() {
                this.uploadDatasetFormVisible = false;
                this.$refs['uploadDatasetForm'].resetFields();
                this.trainSetFileList = [];
                this.testSetFileList = [];
                this.devSetFileList = [];
            },
            showMsg(status) {
                if (status) {
                    this.$notify({
                        title: 'Success',
                        message: 'Dataset is available! Begin a TASK or RESET dataset! ',
                        type: 'success'
                        // offset: 200
                    });
                } else {
                    this.$notify.warning({
                      title: 'Warning',
                      message: 'Dataset is not available! Please UPLOAD first! '
                    });
                }
            },
            goToModelEvaluation(datasetId) {
                // this.$message({
                //     type: 'success',
                //     message: 'Dataset id: ' + datasetId + ', go to Model Evaluation page'
                // });
                this.$router.push('/deepLearning/modelEvaluation');
            },
            goToAutoSelection(datasetId) {
                // this.$message({
                //     type: 'success',
                //     message: 'Dataset id: ' + datasetId + ', go to Auto Selection page'
                // });
                this.$router.push('/deepLearning/autoSelection');
            }
        },
        created() {
            this.loadDataset();
        },
        mounted() {
            this.$nextTick(function () {
                this.datasetTableHeight = window.innerHeight - this.$refs.datasetTable.$el.offsetTop - 190;
                let self = this;
                window.onresize = function() {
                    self.datasetTableHeight = window.innerHeight - self.$refs.datasetTable.$el.offsetTop - 190;
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

  /*.el-upload {*/
  /*  display: inline-block;*/
  /*}*/

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

  .dataset-btn {
    margin: 0 auto;
  }

  .browse-btn {
    width: 70px;
    height: 30px;
    margin: 5px;
    vertical-align: top;
  }

  .el-tag {
    width: 95px;
    text-align: center;
  }

  .el-steps {
    margin-bottom: 40px;
  }
</style>
