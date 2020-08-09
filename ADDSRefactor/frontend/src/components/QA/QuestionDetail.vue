<template>
  <el-main>
    <div class="fixed-header">
      <el-page-header @back="backToQuestionList" title="Go Back" :content="this.pageHeader"></el-page-header>
      <div class="question-desc-div">
        <div class="question-desc">
          <span>Description:&ensp;<b>{{this.question.desc}}</b></span>
        </div>
        <div class="operation-btn" v-if="this.question.status === 2">
          <el-button class="answerBtn" type="primary" plain @click="dialogFormVisible = true" :disabled="disabledBtn">Answer This Question</el-button>
        </div>
      </div>
      <el-divider/>
    </div>
    <el-dialog title="Answer this question" :visible.sync="dialogFormVisible" append-to-body width="640px">
      <el-form ref="answerQuestionForm" :model="answerQuestionForm" :rules="rules" label-position="left" label-width="128px">
        <el-form-item label="Description" prop="description">
          <span>{{this.question.desc}}</span>
        </el-form-item>
        <el-form-item label="Your answer" prop="answer" v-if="this.question.type === 1">
          <el-radio-group v-model="answerQuestionForm.answer">
            <el-radio label="yes">Yes</el-radio>
            <el-radio label="no">No</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="Your answer" prop="answer" v-if="this.question.type === 2">
          <el-input type="textarea" :rows="7" v-model="answerQuestionForm.answer"></el-input>
        </el-form-item>
        <el-form-item label="Remark" prop="remark">
          <el-input v-model="answerQuestionForm.remark"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="answerQuestion">Submit</el-button>
        <el-button @click="cancelAnswer">Cancel</el-button>
      </div>
    </el-dialog>
    <div class="answer-content-div">
      <div class="simple-choice" v-if="this.question.type === 1">
        <div class="content-div scrollable-div">
          <el-card class="simple-choice-answer-div">
            <div slot="header">
              <span><b>Answers Statistics: </b></span>
            </div>
            <div class="chart-div">
              <ve-pie :data="simpleChoiceAnswers"></ve-pie>
              <div v-if="noAnswerForNow">
                <span>No answer for now. </span>
              </div>
            </div>
          </el-card>
        </div>
      </div>
      <div class="detailed-answer" v-if="this.question.type === 2">
        <div class="content-div scrollable-div">
          <el-scrollbar>
            <div class="answer-list">
              <el-card class="detailed-answer-div" v-for="item in detailedAnswers" :key="item.id">
                <div slot="header">
                  <span>User ID: {{item.userId}}</span>
                </div>
                <div class="answer-content">{{item.answerContent}}</div>
              </el-card>
              <el-card class="detailed-answer-div" v-if="noAnswerForNow">
                <div class="answer-content">No answer for now. </div>
              </el-card>
            </div>
          </el-scrollbar>
        </div>
      </div>
    </div>
  </el-main>
</template>

<script>
    export default {
        name: "QuestionDetail",
        data() {
            return {
                pageHeader: '',
                question: {
                    id: 0,
                    department: '',
                    desc: '',
                    type: 1,
                    status: 1,
                },
                simpleChoiceAnswers: {
                    columns: ['Answer', 'Count'],
                    rows: [
                        {'Answer': 'Yes', 'Count': 0},
                        {'Answer': 'No', 'Count': 0}
                    ]
                },
                detailedAnswers: [],
                noAnswerForNowFlag: false,
                dialogFormVisible: false,
                disabledBtn: false,
                answerQuestionForm: {
                    answer: '',
                    remark: ''
                },
                rules: {
                    answer: [{
                        required: true,
                        message: 'Please input your answer',
                        trigger: 'blur'
                    }]
                }
            };
        },
        computed: {
            noAnswerForNow() {
                return this.noAnswerForNowFlag;
            }
        },
        methods: {
            backToQuestionList() {
                this.$router.back();
            },
            getSimpleChoiceAnswers() {
                this.$axios({
                    method: 'get',
                    url: '/QA/' + this.question.id + '/simpleAnswer'
                }).then(res => {
                    console.log(res.data);
                    this.question.desc = res.data.content;
                    this.simpleChoiceAnswers.rows[0].Count = res.data.yesCount;
                    this.simpleChoiceAnswers.rows[1].Count = res.data.noCount;
                    if (res.data.yesCount === 0 && res.data.noCount === 0) {
                        this.noAnswerForNowFlag = true;
                    }
                }).catch(error => {
                    console.log(error);
                    alert("ERROR  in function \"getSimpleChoiceAnswers( )\"! Check Console plz! ");
                });
            },
            getDetailedAnswers() {
                this.$axios({
                    method: 'get',
                    url: '/QA/' + this.question.id + '/detailAnswer'
                }).then(res => {
                    console.log(res.data);
                    this.detailedAnswers.length = 0;
                    if (res.data.length === 0) {
                        this.noAnswerForNowFlag = true;
                    } else {
                        for (let answer in res.data) {
                            if (res.data.hasOwnProperty(answer)) {
                                this.detailedAnswers.push({
                                    id: res.data[answer].qrid,
                                    userId: res.data[answer].userid,
                                    answerContent: res.data[answer].detailedAnswer
                                });
                            }
                        }
                    }
                }).catch(error => {
                    console.log(error);
                    alert("ERROR  in function \"getDetailedAnswers( )\"! Check Console plz! ");
                });
            },
            answerQuestion() {
                // console.log('Answer: ' + this.answerQuestionForm.answer);

                this.$axios({
                    method: 'put',
                    url: '/doctor/' + this.$store.state.user.id + '/question/' + this.question.id + '/answer',
                    data: {
                        type: this.question.type,
                        answer: this.answerQuestionForm.answer,
                        remark: this.answerQuestionForm.remark
                    }
                }).then(res => {
                    // console.log(res.data);

                    this.$message({
                        type: 'success',
                        message: 'Answered Question successfully! '
                    });
                    this.refreshPage();
                }).catch(error => {
                    console.log(error);
                    alert("ERROR  in function \"answerQuestion( )\"! Check Console plz! ");
                });
            },
            cancelAnswer() {
                this.dialogFormVisible = false;
                this.$refs['answerQuestionForm'].resetFields();
            },
            refreshPage() {
                this.question.status = 2;
                this.disabledBtn = true;
                this.dialogFormVisible = false;
                this.noAnswerForNowFlag = false;
                this.$refs['answerQuestionForm'].resetFields();
                if (this.question.type === 1) {
                    this.getSimpleChoiceAnswers();
                } else if (this.question.type === 2) {
                    this.getDetailedAnswers();
                }
                sessionStorage.setItem("addsCurrentQuestionStatusIsChanged", JSON.stringify({
                    isChanged: true
                }));
            }
        },
        created() {
            this.question.id = this.$route.params.id;
            this.pageHeader = 'Question ID ' + this.question.id;
            let currentQuestion = JSON.parse(sessionStorage.getItem("addsCurrentQuestion"));
            this.question.department = '';
            this.question.desc = currentQuestion.desc;
            this.question.type = currentQuestion.type;
            this.question.status = currentQuestion.status;

            if (this.question.type === 1) {
                this.getSimpleChoiceAnswers();
            } else if (this.question.type === 2) {
                this.getDetailedAnswers();
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

  .fixed-header {
    position: fixed;
    left: 60px;
    right: 60px;
  }

  .question-desc-div {
    width: 90%;
    padding: 30px 50px;
    margin: 0 auto;
  }

  .question-desc {
    width: 80%;
    display: inline-block;
    vertical-align: middle;
  }

  .question-desc span {
    font-size: 24px;
    overflow-wrap: break-word;
  }

  .operation-btn {
    width: 18%;
    display: inline-block;
    vertical-align: middle;
    text-align: right;
  }

  .el-divider {
    margin: 0;
  }

  .content-div {
    position: fixed;
    top: 248px;
    left: 60px;
    right: 60px;
    bottom: 40px;
  }

  .simple-choice-answer-div {
    width: 48%;
    margin: 20px auto;
  }

  .detailed-answer-div {
    width: 75%;
    margin: 0 auto 30px auto;
  }
</style>
