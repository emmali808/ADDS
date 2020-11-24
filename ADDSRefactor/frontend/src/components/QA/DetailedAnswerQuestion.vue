<template>
  <el-main>
    <el-page-header @back="backToQuestionList" title="Go Back"></el-page-header>
    <div class="question-detail-div">
      <div class="question-detail">
        <div class="question-content-div">
          <el-form :model="questionContentForm" label-position="left" label-width="90px">
            <el-form-item label="Question ID" prop="questionId">
              <span>{{questionContentForm.questionId}}</span>
            </el-form-item>
            <el-form-item label="Description" prop="description">
              <span>{{questionContentForm.desc}}</span>
            </el-form-item>
            <el-form-item v-if="this.$store.state.user.type === 1 || this.$store.state.user.type === 3">
              <el-button type="primary" @click="dialogFormVisible = true">Answer</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div class="chart-content-div">
          <el-card class="question-div">
            <div slot="header">
              <span>Answers Statistics: </span>
            </div>
<!--            <div class="chart-div">-->
<!--              <ve-pie :data="AnswerChartData"></ve-pie>-->
<!--            </div>-->
          </el-card>
        </div>
      </div>
    </div>
    <el-dialog title="Answer this question" :visible.sync="dialogFormVisible" append-to-body width="640px">
      <el-form ref="answerQuestionForm" :model="answerQuestionForm" :rules="rules" label-position="left" label-width="128px">
        <el-form-item label="Description" prop="description">
          <span>{{questionContentForm.desc}}</span>
        </el-form-item>
        <el-form-item label="Your answer" prop="answer">
          <el-input type="textarea" :rows="7" v-model="answerQuestionForm.answer"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="">Submit</el-button>
        <el-button @click="dialogFormVisible = false">Cancel</el-button>
      </div>
    </el-dialog>
  </el-main>
</template>

<script>
    export default {
        name: "DetailedAnswerQuestion",
        data() {
            return {
                questionContentForm: {
                    questionId: 0,
                    desc: '',
                    answers: []
                },
                // questionId: 0,
                // questionDesc: '',
                // AnswerChartData: {
                //     columns: ['Answer', 'Count'],
                //     rows: [
                //         {'Answer': 'Yes', 'Count': 0},
                //         {'Answer': 'No', 'Count': 0}
                //     ]
                // },
                isAnswered: false,
                dialogFormVisible: false,
                answerQuestionForm: {
                    answer: ''
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
        methods: {
            backToQuestionList() {
                this.$router.back();
            }
        },
        beforeRouteEnter(to, from, next) {
            let questionDesc = sessionStorage.getItem("addsCurrentQuestion");
            // console.log(questionDesc);
            if (questionDesc === null || questionDesc === '') {
                next('/QA');
            } else {
                next();
            }
        },
        beforeRouteLeave(to, from, next) {
            // sessionStorage.setItem("addsCurrentQuestion", "");
            sessionStorage.removeItem("addsCurrentQuestion");
            next();
        },
        created() {
            let questionDesc = sessionStorage.getItem("addsCurrentQuestion");
            this.questionContentForm.questionId = this.$route.params.id;
            this.questionContentForm.desc = questionDesc;
            this.$axios({
                method: 'get',
                url: '/QA/' + this.questionContentForm.questionId + '/detailAnswer'
            }).then(res => {
                console.log(res.data);
            }).catch(error => {
                console.log(error);
                alert("ERROR! Check Console plz! ");
            });
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

  .question-detail-div {
    width: 85%;
    padding: 20px 50px;
    margin: 40px auto;
    border: 1px solid rgb(230, 230, 230);
    border-radius: 5px;
    box-shadow: 2px 2px 8px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);
  }

  .question-detail {
    width: 100%;
    text-align: center;
  }

  .question-content-div, .chart-content-div {
    width: 48%;
    display: inline-block;
    vertical-align: top;
    text-align: left;
  }
</style>
