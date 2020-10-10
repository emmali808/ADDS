<template>
  <el-main>
    <div class="top-bar">
      <div class="option-bar">
        <div class="selector-bar">
          <span>Deep Model: </span>
          <el-cascader
            ref="selectedDeepModelOptions"
            v-model="selectedDeepModel"
            :options="deepModelsOptions"
            :props="{ expandTrigger: 'hover'}"
            @change="selectModel"
            placeholder="Select a model">
          </el-cascader>
        </div>
      </div>
    </div>
    <div class="content-div scrollable-div">
      <el-scrollbar>
        <div class="content-parent">
          <div class="content">
            <el-card class="">
              <div slot="header">
                <span><b>Model:</b>&ensp;{{modelInfoForm.modelName}}</span>
              </div>
              <div>
                <div v-if="!modelInfoFormVisible">
                  <span>Select a model. </span>
                </div>
                <div v-else>
                  <el-form :model="modelInfoForm" label-position="left" label-width="110px">
                    <el-form-item v-if="modelInfoForm.articleTitle !== ''" label="Article" prop="articleTitle">
                      <span>{{modelInfoForm.articleTitle}}</span>&emsp;&emsp;
                      <el-button type="primary" size="mini" @click="downloadArticle">Download</el-button>
                    </el-form-item>
                    <el-form-item v-if="modelInfoForm.introduction !== ''" label="Introduction" prop="introduction">
                      <span>{{modelInfoForm.introduction}}</span>
                    </el-form-item>
                    <el-form-item v-if="modelInfoForm.imgUrl !== ''" label="Architecture" prop="architecture">
                      <el-image :src="modelInfoForm.imgUrl" :fit="'scale-down'" style="width: 90%">
                        <div slot="placeholder">Loading... </div>
                      </el-image>
                    </el-form-item>
                    <el-form-item v-if="modelInfoForm.codeUrl !== ''" label="Code" prop="code">
                      <el-button type="primary" size="mini" @click="downloadCode">Download</el-button>
                    </el-form-item>
                  </el-form>
                </div>
              </div>
            </el-card>
          </div>
        </div>
      </el-scrollbar>
    </div>
  </el-main>
</template>

<script>
    import axios from "axios";

    export default {
        name: "DeepModels",
        data() {
            return {
                deepModelsOptions: [],
                selectedDeepModel: [],
                modelInfoFormVisible: false,
                modelInfoForm: {
                    modelName: '',
                    articleTitle: '',
                    articleUrl: '',
                    introduction: '',
                    imgUrl: '',
                    codeUrl: ''
                }
            };
        },
        methods: {
            loadDeepModels() {
                this.$axios({
                    method: 'get',
                    url: '/modelCategory',
                }).then(res => {
                    // console.log(res.data);
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
                // console.log(value);
                // console.log(value[value.length-1]);
                this.getModelInfo(value[value.length-1]);
                this.modelInfoFormVisible = true;
            },
            getModelInfo(modelId) {
                this.$axios({
                    method: 'get',
                    url: '/modelCategory/model/' + modelId,
                }).then(res => {
                    // console.log(res.data);
                    this.modelInfoForm.modelName = res.data.name;
                    this.modelInfoForm.articleTitle = res.data.articleTitle;
                    if (this.modelInfoForm.articleTitle === '' || this.modelInfoForm.articleTitle === null) {
                        this.modelInfoForm.articleTitle = '';
                    }
                    this.modelInfoForm.introduction = res.data.intro;
                    if (this.modelInfoForm.introduction === '' || this.modelInfoForm.introduction === null) {
                        this.modelInfoForm.introduction = '';
                    }
                    let imgUrl = res.data.architectureUrl;
                    if (imgUrl === '' || imgUrl === null) {
                        this.modelInfoForm.imgUrl = '';
                    } else {
                        this.modelInfoForm.imgUrl = "/api/model-img/deep-model-img/" + imgUrl;
                        // this.modelInfoForm.imgUrl = "/model-img/deep-model-img/" + imgUrl;
                    }
                    this.modelInfoForm.codeUrl = res.data.codeUrl;
                    if (this.modelInfoForm.codeUrl === '' || this.modelInfoForm.codeUrl === null) {
                        this.modelInfoForm.codeUrl = '';
                    }
                    this.modelInfoForm.articleUrl = res.data.articleUrl;
                    if (this.modelInfoForm.articleUrl === '' || this.modelInfoForm.articleUrl === null) {
                        this.modelInfoForm.articleUrl = '';
                    }
                }).catch(error => {
                    console.log(error);
                });
            },
            downloadArticle() {
                if (this.modelInfoForm.articleUrl !== '' && this.modelInfoForm.articleUrl !== null) {
                    let a = document.createElement('a');
                    a.href = "/api/model-article/deep-model-article/" + this.modelInfoForm.articleUrl;
                    // a.href = "/model-article/deep-model-article/" + this.modelInfoForm.articleUrl;
                    let filename = this.modelInfoForm.articleUrl;
                    let fileType = filename.substring(filename.lastIndexOf("."), filename.length);
                    // console.log(fileType);
                    a.download = this.modelInfoForm.modelName + fileType;
                    a.click();
                } else {
                    this.$notify.info({
                        title: 'Notification',
                        message: 'No Article. '
                    });
                }
            },
            downloadCode() {
                if (this.modelInfoForm.codeUrl !== '' && this.modelInfoForm.codeUrl !== null) {
                    let a = document.createElement('a');
                    a.href = "/api/model-code/deep-model-code/" + this.modelInfoForm.codeUrl;
                    // a.href = "/model-code/deep-model-code/" + this.modelInfoForm.codeUrl;
                    let filename = this.modelInfoForm.codeUrl;
                    let fileType = filename.substring(filename.lastIndexOf("."), filename.length);
                    // console.log(fileType);
                    a.download = this.modelInfoForm.modelName + fileType;
                    a.click();
                } else {
                    this.$notify.info({
                        title: 'Notification',
                        message: 'No Code. '
                    });
                }
            }
        },
        mounted() {
            let models = [];
            let metrics = [];
            this.$axios({
                method: 'get',
                url: '/modelCategory/modelName',
            }).then(res => {
                // console.log(res.data);
                for (let model in res.data) {
                    if (res.data.hasOwnProperty(model)) {
                        models[res.data[model].id] = res.data[model].name;
                    }
                }
                this.$axios({
                    method: 'get',
                    url: '/modelMetric',
                }).then(res => {
                    // console.log(res.data);
                    for (let metric in res.data) {
                        if (res.data.hasOwnProperty(metric)) {
                            metrics[res.data[metric].id] = res.data[metric].metricName;
                        }
                    }
                    this.$store.dispatch('saveSysData', {
                        models: models,
                        metrics: metrics
                    });
                }).catch(error => {
                    console.log(error);
                });
            }).catch(error => {
                console.log(error);
            });
        },
        created() {
            this.loadDeepModels();
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

  .top-bar {
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

  .el-cascader {
    width: 400px;
  }

  .content-div {
    position: fixed;
    top: 180px;
    left: 280px;
    right: 40px;
    bottom: 20px;
  }

  .content {
    padding-left: 30px;
    padding-right: 30px;
  }
</style>
