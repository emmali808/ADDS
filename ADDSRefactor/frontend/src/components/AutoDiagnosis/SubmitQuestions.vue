<template>
    <el-container id="container" direction="vertical">
        <div class="note">
            <strong>Note:</strong><div>Please submit a simple yes or no question. You also can contact the doctors using <a href="#/consult">Online system</a>.</div>
        </div>
        <div class="form">
            <el-form ref="submitQuestionForm" :model="submitQuestionForm" label-position="left" label-width="100px">
                <el-form-item label="Description:" prop="questionDesc">
                    <el-input type="textarea" :rows="6" v-model="submitQuestionForm.questionDesc"></el-input>
                </el-form-item>
            </el-form>
        </div>
        <div class="button">
            <el-button size="small" type="primary" @click="submitQuestion">Submit</el-button>
        </div>
    </el-container>
</template>

<script>
    export default {
        name: "SubmitQuestions",
        data() {
            return {
                submitQuestionForm: {
                    questionDesc: '',
                },
            };
        },
        methods: {
            submitQuestion() {
                this.$axios({
                    method: 'put',
                    url: '/patient',
                    data: {
                        content: this.submitQuestionForm.questionDesc,
                        hospitalDepartmentId: 2,
                        type: 1,
                        userid: this.$store.state.user.id,
                        remark: ''
                    }
                }).then(res => {
                    this.$message({
                        type: 'success',
                        message: 'Add Question successfully! '
                    });
                    this.$refs['submitQuestionForm'].resetFields();
                    window.location.href = '#/QA/questions';
                }).catch(error => {
                    console.log(error);
                    alert("ERROR! Check Console plz! ");
                });
            },
        },
        created() {
        },
        mounted() {
        }
    }
</script>

<style scoped>
    #container {
        position: fixed;
        top: 100px;
        left: 300px;
        width: 100%;
    }

    .note {
        margin-top: 10px;
        margin-bottom: 40px;
    }

    .note strong {
        margin-right: 10px;
        float: left;
    }

    .form {
        width: 600px;
    }

    .button{
        text-align: center;
        width: 600px;
    }
</style>
