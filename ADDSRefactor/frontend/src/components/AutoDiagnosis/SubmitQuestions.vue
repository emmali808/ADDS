<template>
  <el-main>
    <div class="option-bar">
      <div class="selector-bar">
        <p style="font-size: 16px;margin-top: 15px"><strong>Note：</strong>Please submit a simple yes or no question. You also can contact the doctors using <span style="color: #409EFF"><a href="#/consult" style="text-decoration-line: underline">Online system</a></span></p>
      </div>
    </div>
    <div class="textfield" >
      <span>Question Description: </span>
      <textarea id="t" rows="10" cols="80" style="vertical-align: top"></textarea>
    </div>
    <div class="subbutton">
      <el-button type="primary" native-type="submit" icon="el-icon-circle-check-outline">Submit</el-button>
    </div>
  </el-main>
</template>

<script>
    export default {
        name: "AutoSelection",
        data() {
            return {
                autoSelectionTaskTableHeight: 200,
                autoSelectionTaskTableData: [],
                autoSelectionTaskTableEmptyText: 'Loading...',
                addTaskFormVisible: false,
                addAutoSelectionTaskForm: {
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
            loadAutoSelectionTask() {
                this.$axios({
                    method: 'get',
                    url: '/task/' + this.$store.state.user.id + '/autoSelection'
                }).then(res => {
                    // console.log(res.data);
                    this.autoSelectionTaskTableData.length = 0;
                    for (let task in res.data) {
                        if (res.data.hasOwnProperty(task)) {
                            let desc =
                                'Dataset: ' + this.datasetName[res.data[task].datasetId] + ',\n' +
                                'Metric: ' + this.$store.state.sysData.metrics[res.data[task].metricId] + ',\n' +
                                'Query Length: ' + res.data[task].queryLength + ',\n' +
                                'Document Length: ' + res.data[task].documentLength;
                            this.autoSelectionTaskTableData.push({
                                id: res.data[task].id,
                                name: res.data[task].taskName,
                                desc: desc,
                                status: res.data[task].status === 1,
                                metric: res.data[task].metricId
                            });
                        }
                    }
                    if (this.autoSelectionTaskTableData.length === 0) {
                        this.autoSelectionTaskTableEmptyText = 'No Auto Selection Task';
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
                    this.loadAutoSelectionTask();
                }).catch(error => {
                    this.$message({
                        type: 'error',
                        message: '[ERROR: AutoSelection.vue -> loadDataset()] Check Console plz! ',
                        showClose: true
                    });
                    console.log(error);
                });
            },
            loadMetric() {
                let metrics = this.$store.state.sysData.metrics;
                for (let metric in metrics) {
                    if (metrics.hasOwnProperty(metric)) {
                        this.metricList.push({
                            value: metric,
                            label: metrics[metric]
                        });
                    }
                }
            },
            cancelAddAutoSelectionTask() {
                this.closeAddAutoSelectionTaskForm();
            },
            closeAddAutoSelectionTaskForm() {
                this.addTaskFormVisible = false;
                this.$refs['addAutoSelectionTaskForm'].resetFields();
            },
            addAutoSelectionTask() {
                this.$axios({
                    method: 'post',
                    url: '/task/' + this.$store.state.user.id + '/autoSelection',
                    data: {
                        taskName: this.addAutoSelectionTaskForm.name,
                        datasetId: this.addAutoSelectionTaskForm.datasetId,
                        queryLength: this.addAutoSelectionTaskForm.queryLength,
                        documentLength: this.addAutoSelectionTaskForm.documentLength,
                        metricId: this.addAutoSelectionTaskForm.metricId
                    }
                }).then(res => {
                    // console.log(res.data);
                    this.closeAddAutoSelectionTaskForm();
                    this.loadAutoSelectionTask();
                }).catch(error => {
                    this.$message({
                        type: 'error',
                        message: '[ERROR: AutoSelection.vue -> addAutoSelectionTask()] Check Console plz! ',
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
                    url: '/task/autoSelectionResult/' + task.id
                }).then(res => {
                    // console.log(res.data);
                    let bestResultMetric = -999;
                    let bestResultIndex = -1;
                    let metricMap = {
                        '1': 'map',
                        '2': 'p@3',
                        '3': 'p@5',
                        '4': 'r@3',
                        '5': 'r@5',
                        '6': 'n@3',
                        '7': 'n@5'
                    };
                    let metricMapJson = {
                        '1': 'map',
                        '2': 'precision3',
                        '3': 'precision5',
                        '4': 'recall3',
                        '5': 'recall5',
                        '6': 'ndcg3',
                        '7': 'ndcg5'
                    };
                    this.resultSettings.labelMap['metric'] = metricMap[task.metric];
                    // console.log("Chart Metric: " + this.resultSettings.labelMap['metric']);
                    for (let result in res.data) {
                        if (res.data.hasOwnProperty(result)) {
                            this.resultTableData.push({
                                model: res.data[result].modelName,
                                map: res.data[result].map,
                                precision3: res.data[result].precision3,
                                precision5: res.data[result].precision5,
                                recall3: res.data[result].recall3,
                                recall5: res.data[result].recall5,
                                ndcg3: res.data[result].ndcg3,
                                ndcg5: res.data[result].ndcg5
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
                        'map: ' + res.data[bestResultIndex].map + ', ' +
                        'p@3: ' + res.data[bestResultIndex].precision3 + ', ' +
                        'p@5: ' + res.data[bestResultIndex].precision5 + ', ' +
                        'r@3: ' + res.data[bestResultIndex].recall3 + ', ' +
                        'r@5: ' + res.data[bestResultIndex].recall5 + ', ' +
                        'n@3: ' + res.data[bestResultIndex].ndcg3 + ', ' +
                        'n@5: ' + res.data[bestResultIndex].ndcg5;
                }).catch(error => {
                    this.$message({
                        type: 'error',
                        message: '[ERROR: AutoSelection.vue -> loadTaskResult()] Check Console plz! ',
                        showClose: true
                    });
                    console.log(error);
                });
            },
            downloadResult() {
                this.$axios({
                    method: 'get',
                    url: '/task/autoSelection/downloadResult/' + this.currentResultTaskId,
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
                        message: '[ERROR: AutoSelection.vue -> downloadResult()] Check Console plz! ',
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
            this.loadDataset();
        },
        mounted() {
            this.$nextTick(function () {
                this.autoSelectionTaskTableHeight = window.innerHeight - this.$refs.autoSelectionTaskTable.$el.offsetTop - 190;
                let self = this;
                window.onresize = function() {
                    self.autoSelectionTaskTableHeight = window.innerHeight - self.$refs.autoSelectionTaskTable.$el.offsetTop - 190;
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

  .selector-bar {
    float: left;
    margin: 0 40px;
  }

  .selector-bar span {
    margin-right: 20px;
  }

  .textfield{
    position: fixed;
    left: 280px;
    top: 180px;
    right: 40px;
    float: left;
    margin: 0 40px;
  }

  .textfield span {
    margin-right: 20px;
  }

  .subbutton{
    position: fixed;
    top: 400px;
    left: 300px;
    right: 40px;
    float: left;
    margin: 0 380px;

  }
</style>
