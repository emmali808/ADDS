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
          </el-form>
        </div>
        <div class="chart-content-div">
          <el-card class="question-div">
            <div slot="header">
              <span>Answers Statistics: </span>
            </div>
            <div class="chart-div">
              <ve-pie :data="AnswerChartData"></ve-pie>
            </div>
          </el-card>
        </div>
      </div>
    </div>
  </el-main>
</template>

<script>
    export default {
        name: "SimpleChoiceQuestion",
        data() {
            return {
                questionContentForm: {
                    questionId: 0,
                    desc: ''
                },
                AnswerChartData: {
                    columns: ['Answer', 'Count'],
                    rows: [
                        {'Answer': 'Yes', 'Count': 0},
                        {'Answer': 'No', 'Count': 0}
                    ]
                },
                isAnswered: false
            };
        },
        methods: {
            backToQuestionList() {
                this.$router.back();
            }
        },
        created() {
            this.questionContentForm.questionId = this.$route.params.id;
            this.$axios({
                method: 'get',
                url: '/QA/' + this.questionContentForm.questionId + '/simpleAnswer'
            }).then(res => {
                console.log(res.data);
                this.questionContentForm.desc = res.data.content;
                let yesCount = res.data.yesCount;
                let noCount = res.data.noCount;
                this.AnswerChartData.rows[0].Count = yesCount;
                this.AnswerChartData.rows[1].Count = noCount;
                if (yesCount > 0 || noCount > 0) {
                    this.isAnswered = true;
                }
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

  .question-content-div .el-form {
    width: 90%;
  }
</style>
