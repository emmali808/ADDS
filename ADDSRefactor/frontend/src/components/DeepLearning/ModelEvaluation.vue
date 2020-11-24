<template>
  <el-main>
    <div class="top-bar">
      <div class="option-bar">
        <div class="header-bar">
          <span>Model Evaluation Tasks</span>
        </div>
        <div class="operation-btn-bar">
          <el-button type="primary" @click="addTaskFormVisible = true">Add a task</el-button>
        </div>
      </div>
    </div>
    <el-dialog title="New Task" :visible.sync="addTaskFormVisible" width="640px"
               append-to-body :close-on-click-modal="false" :close-on-press-escape="false" :show-close="false">
      <el-form ref="addModelEvaluationTask" :model="addModelEvaluationTaskForm" :rules="rules" label-position="left" label-width="140px">
        <el-form-item label="Name" prop="name">
          <el-input v-model="addModelEvaluationTaskForm.name" placeholder="Input task name"></el-input>
        </el-form-item>
        <el-form-item label="Dataset" prop="dataset">
          <el-select v-model="addModelEvaluationTaskForm.datasetId" placeholder="Choose a dataset">
            <el-option v-for="item in datasetList" :key="item.value" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Query Length" prop="queryLength">
          <el-input v-model="addModelEvaluationTaskForm.queryLength" placeholder="Input query length"></el-input>
        </el-form-item>
        <el-form-item label="Document Length" prop="documentLength">
          <el-input v-model="addModelEvaluationTaskForm.documentLength" placeholder="Input document length"></el-input>
        </el-form-item>
        <el-form-item label="Deep Models" prop="deepModels">
<!--          <el-select v-model="addModelEvaluationTask.deepModels" placeholder="Choose Deep Models">-->
<!--            <el-option v-for="item in deepModelList" :key="item.value" :label="item.label" :value="item.value"></el-option>-->
<!--          </el-select>-->
          <el-cascader
            ref="selectedDeepModelOptions"
            v-model="selectedDeepModel"
            :options="deepModelsOptions"
            :props="{ expandTrigger: 'hover'}"
            @change="selectModel"
            placeholder="Select a model">
          </el-cascader>
        </el-form-item>
        <el-form-item label="Metric" prop="metric">
          <el-select v-model="addModelEvaluationTaskForm.metricId" placeholder="Choose a metric">
            <el-option v-for="item in metricList" :key="item.value" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
<!--        <el-form-item label="Remark" prop="remark">-->
<!--&lt;!&ndash;          Remark 或者 Desc&ndash;&gt;-->
<!--          <el-input v-model="addModelEvaluationTask.remark" placeholder="Add remark"></el-input>-->
<!--        </el-form-item>-->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="addModelEvaluationTask">Submit</el-button>
        <el-button @click="cancelAddModelEvaluationTask">Cancel</el-button>
      </div>
    </el-dialog>
    <div class="content-div scrollable-div">
      <el-scrollbar>
        <div class="content-parent">
          <div class="content">
            <div class="item-list">
              <el-table ref="modelEvaluationTaskTable" :data="modelEvaluationTaskTableData" :max-height="modelEvaluationTaskTableHeight">
                <template slot="empty">
                  <span>{{modelEvaluationTaskTableEmptyText}}</span>
                </template>
                <el-table-column prop="name" label="Name" min-width="15%" align="center" :show-overflow-tooltip="true"></el-table-column>
                <el-table-column prop="desc" label="Description" min-width="45%" align="center">
                  <template slot-scope="scope">
                    <el-tooltip effect="dark" placement="top">
                      <div slot="content" v-html="lineFeeds(scope.row.desc)"></div>
                      <div class="ellipsis-desc">{{scope.row.desc}}</div>
                    </el-tooltip>
                  </template>
                </el-table-column>
                <el-table-column prop="status" label="Status" min-width="18%" align="center">
                  <template slot-scope="scope">
                    <el-tag :type="scope.row.status?'success':'info'" @click="showMsg(scope.row.status)">{{scope.row.status?'Finished':'Running'}}</el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="operations" label="Operations" min-width="22%" align="center">
                  <template slot-scope="scope">
                    <el-button v-if="scope.row.status" type="primary" plain size="small" @click="showResult(scope.row.id)">Result</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </div>
        </div>
      </el-scrollbar>
    </div>
    <el-dialog title="Result" :visible.sync="resultDialogFormVisible" width="840px"
               append-to-body :close-on-click-modal="false" :close-on-press-escape="false" :show-close="false">
<!--      <el-table :data="resultTableBody" width="100%" border>-->
<!--        &lt;!&ndash; Dynamic table &ndash;&gt;-->
<!--        <el-table-column v-for="(item,index) in resultTableHeader" :key="index" :label="item.label" :property="item.property" align="center">-->
<!--          <template slot-scope="scope">-->
<!--            {{scope.row[scope.column.property]}}-->
<!--          </template>-->
<!--        </el-table-column>-->
<!--      </el-table>-->
      <div style="margin: 10px 5px;">
        <span><b>Result: </b></span><br/>
        <span>{{resultDesc}}</span>
      </div>
      <el-table :data="resultTableData" size="small" border>
        <el-table-column label="model" prop="model" align="center"></el-table-column>
        <el-table-column label="NDCG@1" prop="ndcg1" align="center"></el-table-column>
        <el-table-column label="NDCG@3" prop="ndcg3" align="center"></el-table-column>
        <el-table-column label="NDCG@5" prop="ndcg5" align="center"></el-table-column>
        <el-table-column label="NDCG@10" prop="ndcg10" align="center"></el-table-column>
        <el-table-column label="map" prop="map" align="center"></el-table-column>
        <el-table-column label="Recall@3" prop="recall3" align="center"></el-table-column>
        <el-table-column label="Recall@5" prop="recall5" align="center"></el-table-column>
        <el-table-column label="Recall@10" prop="recall10" align="center"></el-table-column>
        <el-table-column label="Precision@1" prop="precision1" align="center"></el-table-column>
        <el-table-column label="Precision@3" prop="precision3" align="center"></el-table-column>
        <el-table-column label="Precision@5" prop="precision5" align="center"></el-table-column>
        <el-table-column label="Precision@10" prop="precision10" align="center"></el-table-column>
      </el-table>
<!--      <ve-histogram :data="resultData" :settings="resultSettings" style="width: 480px; height: 280px; margin: 0 auto 30px;"></ve-histogram>-->
      <div slot="footer" class="dialog-footer">
        <el-button @click="closeResultDialog">Close</el-button>
      </div>
    </el-dialog>
  </el-main>
</template>

<script>
    export default {
        name: "ModelEvaluation",
        data() {
            return {
                modelEvaluationTaskTableHeight: 200,
                modelEvaluationTaskTableData: [],
                modelEvaluationTaskTableEmptyText: 'Loading...',
                addTaskFormVisible: false,
                datasetList: [],
                datasetName: [],
                metricList: [],
                deepModelsOptions: [],
                selectedDeepModel: [],
                addModelEvaluationTaskForm: {
                    name: '',
                    datasetId: '',
                    queryLength: '',
                    documentLength: '',
                    modelId: '',
                    metricId: ''
                },
                rules: {
                    name: [
                        {required: true, message: 'Please input name', trigger: 'blur'}
                    ],
                    dataset: [
                        {required: true, message: '', trigger: 'blur'}
                    ],
                    queryLength: [
                      // 要求正整数
                        {required: true, message: '', trigger: 'blur'}
                    ],
                    documentLength: [
                      // 要求正整数
                        {required: true, message: '', trigger: 'blur'}
                    ],
                    deepModels: [
                        {required: true, message: '', trigger: 'blur'}
                    ],
                    metric: [
                        {required: true, message: '', trigger: 'blur'}
                    ]
                },
                resultDialogFormVisible: false,
                resultDesc: '',
                resultTableData: []
            };
        },
        methods: {
            loadModelEvaluationTask() {
                this.$axios({
                    method: 'get',
                    url: '/task/' + this.$store.state.user.id + '/modelEvaluation'
                }).then(res => {
                    // console.log(res.data);
                    this.modelEvaluationTaskTableData.length = 0;
                    for (let task in res.data) {
                        if (res.data.hasOwnProperty(task)) {
                            let desc =
                                'Model: ' + this.$store.state.sysData.models[res.data[task].modelId] + ',\n' +
                                'Dataset: ' + this.datasetName[res.data[task].datasetId] + ',\n' +
                                'Metric: ' + this.$store.state.sysData.metrics[res.data[task].metricId] + ',\n' +
                                'Query Length: ' + res.data[task].queryLength + ',\n' +
                                'Document Length: ' + res.data[task].documentLength;
                            this.modelEvaluationTaskTableData.push({
                                id: res.data[task].id,
                                name: res.data[task].taskName,
                                desc: desc,
                                status: res.data[task].status === 1
                            });
                        }
                    }
                    if (this.modelEvaluationTaskTableData.length === 0) {
                        this.modelEvaluationTaskTableEmptyText = 'No Model Evaluation Task';
                    }
                }).catch(error => {
                    console.log(error);
                });
            },
            loadDataset() {
                this.$axios({
                    method: 'get',
                    url: '/doctor/' + this.$store.state.user.id + '/availableDataSets'
                }).then(res => {
                    // console.log(res.data);
                    this.datasetList.length = 0;
                    for (let dataset in res.data) {
                        if (res.data.hasOwnProperty(dataset)) {
                            let datasetId = res.data[dataset].id;
                            let datasetName = res.data[dataset].dataset_name;
                            this.datasetName[datasetId] = datasetName;
                            this.datasetList.push({
                                value: datasetId,
                                label: datasetName
                            });
                        }
                    }
                    this.loadModelEvaluationTask();
                }).catch(error => {
                    // this.$message({
                    //     type: 'error',
                    //     message: '[ERROR: ModelEvaluation.vue -> loadDataset()] Check Console plz! ',
                    //     showClose: true
                    // });
                    console.log(error);
                });
            },
            loadMetric() {
                let metrics = this.$store.state.sysData.metrics;
                if (metrics === null || metrics.length === 0) {
                    metrics = JSON.parse(sessionStorage.getItem("addsSysData")).metrics;
                }
                for (let metric in metrics) {
                    if (metrics.hasOwnProperty(metric)) {
                        // console.log(metrics[metric]);
                        if (metrics[metric] != null) {
                            this.metricList.push({
                                value: metric,
                                label: metrics[metric]
                            });
                        }
                    }
                }
            },
            loadDeepModels() {
                this.$axios({
                    method: 'get',
                    url: '/modelCategory',
                }).then(res => {
                    for (let category in res.data) {
                        if (res.data.hasOwnProperty(category)) {
                            let childModels = [];
                            let models = res.data[category].models;
                            for (let model in models) {
                                if (models.hasOwnProperty(model)) {
                                    childModels.push({
                                        value: models[model].id,
                                        label: models[model].name
                                    });
                                }
                            }
                            this.deepModelsOptions.push({
                                value: res.data[category].id,
                                label: res.data[category].name,
                                children: childModels
                            });
                        }
                    }
                }).catch(error => {
                    console.log(error);
                });
            },
            selectModel(value) {
                this.addModelEvaluationTaskForm.modelId = value[value.length-1];
            },
            cancelAddModelEvaluationTask() {
                this.closeAddModelEvaluationTaskForm();
            },
            closeAddModelEvaluationTaskForm() {
                this.addTaskFormVisible = false;
                this.selectedDeepModel = [];
                this.addModelEvaluationTaskForm.metricId = '';
                this.$refs['addModelEvaluationTask'].resetFields();
            },
            addModelEvaluationTask() {
                // console.log("Name: " + this.addModelEvaluationTaskForm.name);
                // console.log("Dataset ID: " + this.addModelEvaluationTaskForm.datasetId);
                // console.log("Query Length: " + this.addModelEvaluationTaskForm.queryLength);
                // console.log("Document Length: " + this.addModelEvaluationTaskForm.documentLength);
                // console.log("Model ID: " + this.addModelEvaluationTaskForm.modelId);
                // console.log("Metric ID: " + this.addModelEvaluationTaskForm.metricId);

                this.$axios({
                    method: 'post',
                    url: '/task/' + this.$store.state.user.id + '/modelEvaluation',
                    data: {
                        taskName: this.addModelEvaluationTaskForm.name,
                        datasetId: this.addModelEvaluationTaskForm.datasetId,
                        queryLength: this.addModelEvaluationTaskForm.queryLength,
                        documentLength: this.addModelEvaluationTaskForm.documentLength,
                        modelId: this.addModelEvaluationTaskForm.modelId,
                        metricId: this.addModelEvaluationTaskForm.metricId
                    }
                }).then(res => {
                    // console.log(res.data);
                    // this.closeAddModelEvaluationTaskForm();
                    this.loadModelEvaluationTask();
                    this.beginTask(res.data);
                }).catch(error => {
                    this.$message({
                        type: 'error',
                        message: '[ERROR: ModelEvaluation.vue -> addModelEvaluationTask()] Check Console plz! ',
                        showClose: true
                    });
                    console.log(error);
                });
            },
            beginTask(taskId) {
                this.$axios({
                    method: 'post',
                    url: '/doctor/' + this.$store.state.user.id + '/DLTask',
                    data: {
                        id: taskId,
                        taskName: this.addModelEvaluationTaskForm.name,
                        datasetId: this.addModelEvaluationTaskForm.datasetId,
                        queryLength: this.addModelEvaluationTaskForm.queryLength,
                        documentLength: this.addModelEvaluationTaskForm.documentLength,
                        modelId: this.addModelEvaluationTaskForm.modelId,
                        metricId: this.addModelEvaluationTaskForm.metricId
                    }
                }).then(res => {
                    // console.log(res);
                    this.closeAddModelEvaluationTaskForm();
                }).catch(error => {
                    // this.$message({
                    //     type: 'error',
                    //     message: '[ERROR: ModelEvaluation.vue -> beginTask()] Check Console plz! ',
                    //     showClose: true
                    // });
                    console.log(error);
                });
            },
            showResult(taskId) {
                this.resultTableData.length = 0;
                this.resultDialogFormVisible = true;
                this.loadTaskResult(taskId);
            },
            closeResultDialog() {
                this.resultDialogFormVisible = false;
            },
            loadTaskResult(taskId) {
                this.$axios({
                    method: 'get',
                    url: '/task/modelEvaluationResult/' + taskId
                }).then(res => {
                    // console.log(res.data);
                    // this.resultTableData.length = 0;
                    this.resultTableData.push({
                        model: res.data.modelName,
                        ndcg1: res.data.ndcg1,
                        ndcg3: res.data.ndcg3,
                        ndcg5: res.data.ndcg5,
                        ndcg10: res.data.ndcg10,
                        map: res.data.map,
                        recall3: res.data.recall3,
                        recall5: res.data.recall5,
                        recall10: res.data.recall10,
                        precision1: res.data.precision1,
                        precision3: res.data.precision3,
                        precision5: res.data.precision5,
                        precision10: res.data.precision10
                    });
                    this.resultDesc =
                        'model: ' + res.data.modelName + ', ' +
                        'n@1: ' + res.data.ndcg1 + ', ' +
                        'n@3: ' + res.data.ndcg3 + ', ' +
                        'n@5: ' + res.data.ndcg5 + ', ' +
                        'n@10: ' + res.data.ndcg10 + ', ' +
                        'map: ' + res.data.map + ', ' +
                        'r@3: ' + res.data.recall3 + ', ' +
                        'r@5: ' + res.data.recall5 + ', ' +
                        'r@10: ' + res.data.recall10 + ', ' +
                        'p@1: ' + res.data.precision1 + ', ' +
                        'p@3: ' + res.data.precision3 + ', ' +
                        'p@5: ' + res.data.precision5 + ', ' +
                        'p@10: ' + res.data.precision10;
                }).catch(error => {
                    this.$message({
                        type: 'error',
                        message: '[ERROR: ModelEvaluation.vue -> loadTaskResult()] Check Console plz! ',
                        showClose: true
                    });
                    console.log(error);
                });
            },
            lineFeeds(str) {
                return str.replace(/\n/g, '<br/>');
            },
            showMsg(status) {
                if (status) {
                    this.$notify({
                        title: 'Success',
                        message: 'This task is finished! Check RESULT! ',
                        type: 'success'
                    });
                } else {
                    this.$notify.info({
                        title: 'Notification',
                        message: 'This task is still running! '
                    });
                }
            }
        },
        created() {
            this.loadDeepModels();
            this.loadMetric();
            this.loadDataset();
        },
        mounted() {
            this.$nextTick(function () {
                this.modelEvaluationTaskTableHeight = window.innerHeight - this.$refs.modelEvaluationTaskTable.$el.offsetTop - 190;
                let self = this;
                window.onresize = function() {
                    self.modelEvaluationTaskTableHeight = window.innerHeight - self.$refs.modelEvaluationTaskTable.$el.offsetTop - 190;
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
    padding: 0 20px;
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

  .ellipsis-desc {
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
  }

  .el-tag {
    width: 75px;
    text-align: center;
  }

  .el-select {
    width: 100%;
  }

  .el-cascader {
    width: 100%;
  }
</style>
