<template>
  <el-main>
    <div class="top-bar">
      <div class="option-bar">
        <div class="header-bar">
          <span>Knowledge Exploration Tasks</span>
        </div>
        <div class="operation-btn-bar">
          <el-button type="primary" @click="addTaskFormVisible = true">Add a task</el-button>
        </div>
      </div>
    </div>
    <el-dialog title="New Task" :visible.sync="addTaskFormVisible" width="640px"
               append-to-body :close-on-click-modal="false" :close-on-press-escape="false" :show-close="false">
      <el-form ref="addKnowledgeExplorationTaskForm" :model="addKnowledgeExplorationTaskForm" :rules="rules" label-position="left" label-width="140px">
        <el-form-item label="Name" prop="name">
          <el-input v-model="addKnowledgeExplorationTaskForm.name" placeholder="Input task name"></el-input>
        </el-form-item>
        <el-form-item label="Dataset" prop="dataset">
          <el-select v-model="addKnowledgeExplorationTaskForm.datasetId" placeholder="Choose a dataset">
            <el-option v-for="item in datasetList" :key="item.value" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Metric" prop="metric">
          <el-select v-model="addKnowledgeExplorationTaskForm.metricId" placeholder="Choose a metric">
            <el-option v-for="item in metricList" :key="item.value" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Query Length" prop="queryLength">
          <el-input v-model="addKnowledgeExplorationTaskForm.queryLength" placeholder="Input query length"></el-input>
        </el-form-item>
        <el-form-item label="Document Length" prop="documentLength">
          <el-input v-model="addKnowledgeExplorationTaskForm.documentLength" placeholder="Input document length"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="addKnowledgeExplorationTask">Submit</el-button>
        <el-button @click="cancelAddKnowledgeExplorationTask">Cancel</el-button>
      </div>
    </el-dialog>
    <div class="content-div scrollable-div">
      <el-scrollbar>
        <div class="content-parent">
          <div class="content">
            <div class="item-list">
              <el-table ref="knowledgeExplorationTaskTable" :data="knowledgeExplorationTaskTableData" :max-height="knowledgeExplorationTaskTableHeight">
                <template slot="empty">
                  <span>{{knowledgeExplorationTaskTableEmptyText}}</span>
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
                    <el-button v-if="scope.row.status" type="primary" plain size="small" @click="showResult(scope.row)">Result</el-button>
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
      <div style="margin: 10px 5px;">
        <span><b>The best result: </b></span><br/>
        <span>{{bestResultDesc}}</span>
      </div>
      <el-table :data="resultTableData" size="small" border :stripe="true" height="200">
        <el-table-column label="model" min-width="100px" prop="model" align="center"></el-table-column>
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
      <div class="download-btn" style="text-align: right; margin: 10px;">
        <span>Download Results</span>&emsp;
        <el-button type="primary" plain size="small" @click="downloadResult">Download</el-button>
      </div>
      <ve-histogram :data="resultData" :settings="resultSettings" :extend="resultExtend" style="width: 640px; height: 280px; margin: 20px auto 20px;"></ve-histogram>
      <div slot="footer" class="dialog-footer">
        <el-button @click="closeResultDialog">Close</el-button>
      </div>
    </el-dialog>
  </el-main>
</template>

<script>
    export default {
        name: "KnowledgeExploration",
        data() {
            return {
                knowledgeExplorationTaskTableHeight: 200,
                knowledgeExplorationTaskTableData: [],
                knowledgeExplorationTaskTableEmptyText: 'Loading...',
                addTaskFormVisible: false,
                addKnowledgeExplorationTaskForm: {
                    name: '',
                    datasetId: '',
                    queryLength: '',
                    documentLength: '',
                    metricId: ''
                },
                rules: {
                    name: [
                        {required: true, message: '', trigger: 'blur'}
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
                    metric: [
                        {required: true, message: '', trigger: 'blur'}
                    ]
                },
                datasetList: [],
                datasetName: [],
                metricList: [],
                deepModelCount: 0,
                resultDialogFormVisible: false,
                currentResultTaskId: 0,
                currentResultTaskName: '',
                bestResultDesc: '',
                resultTableData: [],
                resultData: {
                    columns: ['model', 'metric'],
                    rows: []
                },
                resultSettings: {
                    metrics: ['metric'],
                    dimension: ['model'],
                    labelMap: {
                        'metric': ''
                    }
                },
                resultExtend: {
                    series: {
                        // 柱形图柱子宽度
                        // barWidth: 10
                    },
                    xAxis: {
                        // x 轴字体斜显示
                        axisLabel: {
                            // margin: 15,
                            // interval: 0,
                            rotate: 90,
                            // formatter: name => {
                            //   return echarts.format.truncateText(name)
                            // }
                        },
                        triggerEvent: true
                    }
                }
            };
        },
        methods: {
            loadKnowledgeExplorationTask() {
                this.$axios({
                    method: 'get',
                    url: '/task/' + this.$store.state.user.id + '/knowledgeExploration'
                }).then(res => {
                    // console.log(res.data);
                    this.knowledgeExplorationTaskTableData.length = 0;
                    for (let task in res.data) {
                        if (res.data.hasOwnProperty(task)) {
                            let desc =
                                'Dataset: ' + this.datasetName[res.data[task].datasetId] + ',\n' +
                                'Metric: ' + this.$store.state.sysData.metrics[res.data[task].metricId] + ',\n' +
                                'Query Length: ' + res.data[task].queryLength + ',\n' +
                                'Document Length: ' + res.data[task].documentLength;
                            this.knowledgeExplorationTaskTableData.push({
                                id: res.data[task].id,
                                name: res.data[task].taskName,
                                desc: desc,
                                status: res.data[task].status === this.deepModelCount,
                                metric: res.data[task].metricId
                            });
                        }
                    }
                    if (this.knowledgeExplorationTaskTableData.length === 0) {
                        this.knowledgeExplorationTaskTableEmptyText = 'No Knowledge Exploration Task';
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
                    this.loadKnowledgeExplorationTask();
                }).catch(error => {
                    this.$message({
                        type: 'error',
                        message: '[ERROR: KnowledgeExploration.vue -> loadDataset()] Check Console plz! ',
                        showClose: true
                    });
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
                        if (metrics[metric] != null) {
                            this.metricList.push({
                                value: metric,
                                label: metrics[metric]
                            });
                        }
                    }
                }
            },
            cancelAddKnowledgeExplorationTask() {
                this.closeAddKnowledgeExplorationTaskForm();
            },
            closeAddKnowledgeExplorationTaskForm() {
                this.addTaskFormVisible = false;
                this.addKnowledgeExplorationTaskForm.metricId = '';
                this.$refs['addKnowledgeExplorationTaskForm'].resetFields();
            },
            addKnowledgeExplorationTask() {
                this.$axios({
                    method: 'post',
                    url: '/task/' + this.$store.state.user.id + '/knowledgeExploration',
                    data: {
                        taskName: this.addKnowledgeExplorationTaskForm.name,
                        datasetId: this.addKnowledgeExplorationTaskForm.datasetId,
                        queryLength: this.addKnowledgeExplorationTaskForm.queryLength,
                        documentLength: this.addKnowledgeExplorationTaskForm.documentLength,
                        metricId: this.addKnowledgeExplorationTaskForm.metricId
                    }
                }).then(res => {
                    // console.log(res.data);
                    // this.closeAddKnowledgeExplorationTaskForm();
                    this.loadKnowledgeExplorationTask();
                    this.beginTask(res.data);
                }).catch(error => {
                    this.$message({
                        type: 'error',
                        message: '[ERROR: KnowledgeExploration.vue -> addKnowledgeExplorationTask()] Check Console plz! ',
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
                    console.log(res);
                    this.closeAddKnowledgeExplorationTaskForm();
                }).catch(error => {
                    this.$message({
                        type: 'error',
                        message: '[ERROR: KnowledgeExploration.vue -> beginTask()] Check Console plz! ',
                        showClose: true
                    });
                    console.log(error);
                });
            },
            showResult(task) {
                this.resultTableData.length = 0;
                this.resultData.rows.length = 0;
                this.resultDialogFormVisible = true;
                this.currentResultTaskId = task.id;
                this.currentResultTaskName = task.name;
                this.loadTaskResult(task);
            },
            closeResultDialog() {
                this.resultDialogFormVisible = false;
            },
            loadTaskResult(task) {
                this.$axios({
                    method: 'get',
                    url: '/task/knowledgeExplorationResult/' + task.id
                }).then(res => {
                    // console.log(res.data);
                    let bestResultMetric = -999;
                    let bestResultIndex = -1;
                    let metricMap = {
                        '1': 'NDCG@1',
                        '2': 'NDCG@3',
                        '3': 'NDCG@5',
                        '4': 'NDCG@10',
                        '5': 'map',
                        '6': 'Recall@3',
                        '7': 'Recall@5',
                        '8': 'Recall@10',
                        '9': 'Precision@1',
                        '10': 'Precision@3',
                        '11': 'Precision@5',
                        '12': 'Precision@10'
                    };
                    let metricMapJson = {
                        '1': 'ndcg1',
                        '2': 'ndcg3',
                        '3': 'ndcg5',
                        '4': 'ndcg10',
                        '5': 'map',
                        '6': 'recall3',
                        '7': 'recall5',
                        '8': 'recall10',
                        '9': 'precision1',
                        '10': 'precision3',
                        '11': 'precision5',
                        '12': 'precision10'
                    };
                    this.resultSettings.labelMap['metric'] = metricMap[task.metric];
                    // console.log("Chart Metric: " + this.resultSettings.labelMap['metric']);
                    for (let result in res.data) {
                        if (res.data.hasOwnProperty(result)) {
                            this.resultTableData.push({
                                model: res.data[result].modelName,
                                ndcg1: res.data[result].ndcg1,
                                ndcg3: res.data[result].ndcg3,
                                ndcg5: res.data[result].ndcg5,
                                ndcg10: res.data[result].ndcg10,
                                map: res.data[result].map,
                                recall3: res.data[result].recall3,
                                recall5: res.data[result].recall5,
                                recall10: res.data[result].recall10,
                                precision1: res.data[result].precision1,
                                precision3: res.data[result].precision3,
                                precision5: res.data[result].precision5,
                                precision10: res.data[result].precision10
                            });
                            this.resultData.rows.push({
                                'model': res.data[result].modelName,
                                'metric': res.data[result][metricMapJson[task.metric]]
                            });
                            // console.log("Metric: " + res.data[result][metricMapJson[task.metric]]);
                            if (res.data[result][metricMapJson[task.metric]] > bestResultMetric) {
                                bestResultIndex = result;
                            }
                        }
                    }
                    this.bestResultDesc =
                        'model: ' + res.data[bestResultIndex].modelName + ', ' +
                        'n@1: ' + res.data[bestResultIndex].ndcg1 + ', ' +
                        'n@3: ' + res.data[bestResultIndex].ndcg3 + ', ' +
                        'n@5: ' + res.data[bestResultIndex].ndcg5 + ', ' +
                        'n@10: ' + res.data[bestResultIndex].ndcg10 + ', ' +
                        'map: ' + res.data[bestResultIndex].map + ', ' +
                        'r@3: ' + res.data[bestResultIndex].recall3 + ', ' +
                        'r@5: ' + res.data[bestResultIndex].recall5 + ', ' +
                        'r@10: ' + res.data[bestResultIndex].recall10 + ', ' +
                        'p@1: ' + res.data[bestResultIndex].precision1 + ', ' +
                        'p@3: ' + res.data[bestResultIndex].precision3 + ', ' +
                        'p@5: ' + res.data[bestResultIndex].precision5 + ', ' +
                        'p@10: ' + res.data[bestResultIndex].precision10;
                }).catch(error => {
                    console.log(error);
                });
            },
            downloadResult() {
                this.$axios({
                    method: 'get',
                    url: '/task/knowledgeExploration/downloadResult/' + this.currentResultTaskId,
                    responseType: 'blob'
                }).then(res => {
                    // console.log(res.data);
                    let blob = new Blob([res.data], {type: "application/octet-stream"});
                    let objectUrl = URL.createObjectURL(blob);
                    let a = document.createElement('a');
                    a.href = objectUrl;
                    a.download = this.currentResultTaskName + "_Result.xls";
                    a.click();
                }).catch(error => {
                    this.$message({
                        type: 'error',
                        message: '[ERROR: KnowledgeExploration.vue -> downloadResult()] Check Console plz! ',
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
            this.loadMetric();
            this.$axios({
                method: 'get',
                url: '/modelCategory',
            }).then(res => {
                let modelCount = 0;
                for (let category in res.data) {
                    if (res.data.hasOwnProperty(category)) {
                        modelCount += res.data[category].models.length;
                    }
                }
                this.deepModelCount = modelCount;
                this.loadDataset();
            }).catch(error => {
                console.log(error);
            });
        },
        mounted() {
          this.$nextTick(function () {
            this.knowledgeExplorationTaskTableHeight = window.innerHeight - this.$refs.knowledgeExplorationTaskTable.$el.offsetTop - 190;
            let self = this;
            window.onresize = function() {
              self.knowledgeExplorationTaskTableHeight = window.innerHeight - self.$refs.knowledgeExplorationTaskTable.$el.offsetTop - 190;
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
</style>
