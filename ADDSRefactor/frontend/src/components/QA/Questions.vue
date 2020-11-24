<template>
  <el-main>
    <div class="option-bar-div">
      <div class="option-bar">
        <div class="selector-bar">
          <span>Select: </span>
          <el-select placeholder="Question Status" v-model="value1" @change="selectQuestionStatus">
<!--            clearable @clear="clearQuestionStatus" @clear="clearQuestionType"-->
            <el-option v-for="item in options1" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
          <el-select placeholder="Question Type" v-model="value2" @change="selectQuestionType">
            <el-option v-for="item in options2" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </div>
        <div class="search-bar">
          <el-input placeholder="Search" v-model="searchContent" clearable>
            <el-button slot="append" icon="el-icon-search" @click="searchQuestions"></el-button>
          </el-input>
        </div>
        <div class="btn-bar">
          <el-button type="primary" @click="dialogFormVisible = true">New Question</el-button>
          <el-button type="primary" @click="loadMyQuestions">My Questions</el-button>
        </div>
      </div>
    </div>

    <el-dialog title="New Question" :visible.sync="dialogFormVisible" append-to-body width="560px">
      <el-form ref="addQuestionForm" :model="newQuestionForm" :rules="rules" label-position="left" label-width="120px">
        <el-form-item label="Department" prop="hospitalDepartmentId">
          <el-select v-model="newQuestionForm.hospitalDepartmentId" placeholder="Choose a department">
            <el-option v-for="item in newQuestionForm.departmentOptions" :key="item.value" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Description" prop="questionDesc">
          <el-input type="textarea" :rows="5" v-model="newQuestionForm.questionDesc"></el-input>
        </el-form-item>
        <el-form-item label="Type" prop="questionType">
          <el-radio-group v-model="newQuestionForm.questionType">
            <el-radio label="1">Simple choice</el-radio>
            <el-radio label="2">Detailed answer</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="addNewQuestion">Submit</el-button>
        <el-button @click="dialogFormVisible = false">Cancel</el-button>
      </div>
    </el-dialog>

    <div class="content-div scrollable-div">
      <el-scrollbar>
        <div ref="questionListDiv" class="question-list-div" v-infinite-scroll="load" infinite-scroll-disabled="stopLoading">
          <el-card class="question-div" v-for="item in questionList" :key="item.id">
<!--            v-bind:class="{'answered-question-div': item.isAnswered}"-->
            <div slot="header">
              <span>Question ID: {{item.id}}</span>
              <el-button type="text" @click="showDetails(item)">Details</el-button>
            </div>
            <div class="question-content">{{item.desc}}</div>
          </el-card>
          <div class="loading-info">
            <p v-if="stopLoading">No more questions. </p>
            <div v-loading="!stopLoading" element-loading-text="Loading... " element-loading-spinner="el-icon-loading"></div>
          </div>
          <el-backtop :target="scrollTarget" :bottom="75" :right="75" :visibility-height="500">
            <div class="back-top">TOP</div>
          </el-backtop>
        </div>
      </el-scrollbar>
    </div>
  </el-main>
</template>

<script>
    export default {
        name: "Questions",
        data() {
            return {
                options1: [
                    {
                        value: '1',
                        label: 'Answered'
                    }, {
                        value: '2',
                        label: 'Not Answered'
                    }
                ],
                value1: '1',
                options2: [
                    {
                        value: '1',
                        label: 'Simple choice'
                    }, {
                        value: '2',
                        label: 'Detailed answer'
                    }
                ],
                value2: '1',
                searchContent: '',
                dialogFormVisible: false,
                newQuestionForm: {
                    departmentOptions: [],
                    hospitalDepartmentId: '',
                    questionDesc: '',
                    questionType: ''
                },
                rules: {
                    hospitalDepartmentId: [
                        {required: true, message: 'Please choose a department', trigger: 'blur'}
                    ],
                    questionDesc: [
                        {required: true, message: 'Please input question description', trigger: 'blur'}
                    ],
                    questionType: [
                        {required: true, message: 'Please choose question type', trigger: 'blur'}
                    ]
                },

                questionList: [],
                page: 1,
                limit: 10,
                loadedQuestionCount: 999,
                scroll: 0,

                questionStatus: 1,
                questionType: 1,

                scrollTarget: '.scrollable-div .el-scrollbar__wrap'
            };
        },
        computed: {
            stopLoading() {
                return this.loadedQuestionCount < this.limit;
            }
        },
        methods: {
            selectQuestionStatus(value) {
                if (value === '1') {
                    this.questionStatus = 1;
                    this.selectQuestions();
                } else if (value === '2') {
                    this.questionStatus = 2;
                    this.selectQuestions();
                }
            },
            selectQuestionType(value) {
                if (value === '1') {
                    this.questionType = 1;
                    this.selectQuestions();
                } else if (value === '2') {
                    this.questionType = 2;
                    this.selectQuestions();
                }
            },
            // clearQuestionStatus() {
            //     this.questionStatus = 0;
            //     this.selectQuestions();
            // },
            // clearQuestionType() {
            //     this.questionType = 0;
            //     this.selectQuestions();
            // },
            initPage() {
                this.questionList.length = 0;
                this.page = 1;
                this.loadedQuestionCount = 999;
            },
            selectQuestions() {
                // console.log(
                //     "Question Status: " + this.questionStatus + "\n" +
                //     "Question Type: " + this.questionType
                // );

                this.initPage();
                this.loadQuestions();
            },
            loadQuestions() {
                this.$axios({
                    method: 'post',
                    url: '/doctor/' + this.$store.state.user.id + '/question',
                    data: {
                        answeredOrNot: this.questionStatus,
                        questionType: this.questionType,
                        start: this.page,
                        limit: this.limit
                    }
                }).then(res => {
                    console.log(res.data);
                    this.loadedQuestionCount = res.data.length;
                    for (let question in res.data) {
                        if (res.data.hasOwnProperty(question)) {
                            this.questionList.push({
                                id: res.data[question].qid,
                                desc: res.data[question].content,
                                type: res.data[question].type,
                                status: this.questionStatus
                                // departmentId: res.data[i].hospitalDepartmentId
                            });
                        }
                    }
                    this.questionList.sort((a, b) => (a.id > b.id) ? -1 : 1)
                }).catch(error => {
                    console.log(error);
                    alert("ERROR! Check Console plz! ");
                    this.loadedQuestionCount = 0;
                });

                this.page++;
            },
            load() {
                setTimeout(() => {
                    this.loadQuestions();
                }, 500);
            },
            searchQuestions() {
                this.$axios({
                    method: 'post',
                    url: '/user/similarityQuestion',
                    data: {
                        question: this.searchContent
                    }
                }).then(res => {
                    console.log(res.data);
                    this.loadedQuestionCount = 0;
                    this.initPage();
                    for (let question in res.data) {
                        if (res.data.hasOwnProperty(question)) {
                            let answered = res.data[question].answered === 1;
                            this.questionList.push({
                                id: res.data[question].qid,
                                desc: res.data[question].content,
                                isAnswered: answered
                                // departmentId: res.data[i].hospitalDepartmentId
                            });
                        }
                    }
                }).catch(error => {
                    console.log(error);
                    alert("ERROR! Check Console plz! ");
                });
            },
            loadDepartments() {
                this.$axios({
                    method: 'get',
                    url: '/department',
                }).then(res => {
                    // console.log(res.data);
                    for (let department in res.data) {
                        if (res.data.hasOwnProperty(department)) {
                            this.newQuestionForm.departmentOptions.push({
                                value: res.data[department].id,
                                label: res.data[department].name
                            });
                        }
                    }
                }).catch(error => {
                    console.log(error);
                    alert("ERROR  in function \"loadDepartments( )\"! Check Console plz! ");
                });
            },
            addNewQuestion() {
                // console.log(
                //     'Department ID: ' + this.newQuestionForm.hospitalDepartmentId + '\n' +
                //     'Question Desc: ' + this.newQuestionForm.questionDesc + '\n' +
                //     'Question Type: ' + this.newQuestionForm.questionType
                // );

                this.$axios({
                    method: 'put',
                    url: '/patient',
                    data: {
                        content: this.newQuestionForm.questionDesc,
                        hospitalDepartmentId: this.newQuestionForm.hospitalDepartmentId,
                        type: this.newQuestionForm.questionType,
                        // userid: this.$store.state.user.id,
                        userid: 2,
                        remark: ''
                    }
                }).then(res => {
                    // console.log(res.data);
                    this.$message({
                        type: 'success',
                        message: 'Add Question successfully! '
                    });
                    this.dialogFormVisible = false;
                    this.selectQuestions();
                    this.$refs['addQuestionForm'].resetFields();
                }).catch(error => {
                    console.log(error);
                    alert("ERROR! Check Console plz! ");
                });
            },
            loadMyQuestions() {
                this.$axios({
                    method: 'get',
                    url: '/patient/' + this.$store.state.user.id + '/question',
                }).then(res => {
                    // console.log(res.data);

                    this.loadedQuestionCount = 0;
                    this.initPage();
                    for (let question in res.data) {
                        if (res.data.hasOwnProperty(question)) {
                            this.questionList.push({
                                id: res.data[question].qid,
                                desc: res.data[question].content,
                                type: res.data[question].type
                            });
                        }
                    }
                    this.loadedQuestionCount = 0;
                    this.questionList.sort((a, b) => (a.id > b.id) ? -1 : 1)
                }).catch(error => {
                    console.log(error);
                    alert("ERROR  in function \"loadMyQuestions( )\" [Questions]! Check Console plz! ");
                });
            },
            scrollTo() {
                let currentQuestion = JSON.parse(sessionStorage.getItem("addsCurrentQuestionStatusIsChanged"));
                if (currentQuestion.isChanged) {
                    this.selectQuestions();
                } else {
                    this.$refs.questionListDiv.parentElement.parentElement.scrollTop = this.scroll;
                }
            },
            showDetails(question) {
                sessionStorage.setItem("addsCurrentQuestion", JSON.stringify(question));
                this.$router.push('/QA/questionDetail/' + question.id);

                // if (question.type === 1) {
                //     this.$router.push('/QA/simpleChoice/' + question.id);
                // } else if (question.type === 2) {
                //     sessionStorage.setItem("addsCurrentQuestion", question.desc);
                //     this.$router.push('/QA/detailedAnswer/' + question.id);
                // }
            }
        },
        created() {
            this.loadMyQuestions();
            this.loadDepartments();
            sessionStorage.setItem("addsCurrentQuestionStatusIsChanged", JSON.stringify({
                isChanged: false
            }));
        },
        beforeRouteEnter(to, from, next) {
            next(vm => {
                vm.scrollTo();
            })
        },
        beforeRouteLeave(to, from, next) {
            this.scroll = this.$refs.questionListDiv.parentElement.parentElement.scrollTop;
            next();
        },
        watch: {
            '$route': {
              handler(route) {
                this.loadMyQuestions();
              }
            }
        }
    }
</script>

<style scoped>
  .el-main {
    position: fixed;
    top: 80px;
    left: 30px;
    right: 30px;
    bottom: 0;
    margin: 20px;
    padding: 10px;
  }

  .option-bar-div {
    text-align: center;
  }

  .option-bar-div .option-bar {
    display: inline-block;
  }

  .selector-bar {
    float: left;
    margin: 0 20px;
  }

  .selector-bar span {
    margin-right: 20px;
  }

  .search-bar {
    float: left;
    margin: 0 20px;
    width: 280px;
  }

  .btn-bar {
    float: left;
    margin: 0 20px;
  }

  .content-div {
    position: fixed;
    top: 180px;
    left: 30px;
    right: 30px;
    bottom: 0;
  }

  .question-list-div {
    overflow-y: hidden;
  }

  .el-card:hover {
    box-shadow: 6px 6px 8px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);
  }

  .question-div {
    width: 75%;
    margin: 0 auto 30px auto;
    padding: 20px 40px;
    border: 1px solid rgb(180, 216, 255);
    border-radius: 5px;
    overflow: hidden;
  }

  .question-div .el-button {
    float: right;
    padding: 0;
  }

  .loading-info {
    height: 40px;
    text-align: center;
  }

  .back-top {
    height: 50px;
    width: 50px;
    line-height: 50px;
    background-color: rgb(238, 241, 246);
    box-shadow: 0 0 6px rgba(0, 0, 0, .12);
    font-weight: bold;
    color: #1989fa;
    text-align: center;
    border-radius: 3px;
    position: fixed;
    bottom: 75px;
    right: 75px;
    cursor: pointer;
  }
</style>
