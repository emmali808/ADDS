<template>
  <div id="log-in">
<!--    <div id="info-div">-->
<!--      <p id="info-head">ADDS</p>-->
<!--      <p id="info-content">Welcome! </p>-->
<!--    </div>-->
    <div id="log-in-div">
      <el-form ref="loginForm" :model="loginForm">
        <p id="form-head">Welcome</p>
        <el-form-item prop="username">
          <el-input v-model="loginForm.username" name="username" placeholder="Username" auto-complete="on"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="loginForm.password" name="password" placeholder="Password" auto-complete="on" show-password></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="login">Log in</el-button>
          <el-button type="primary" @click="signUp">Sign up</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
    export default {
        name: "login",
        data() {
            return {
                loginForm: {
                    username: '',
                    password: ''
                }
            };
        },
        methods: {
            login() {
                if (this.loginForm.username === '' || this.loginForm.password === '') {
                    alert("Username or password cannot be blank! ");
                } else {
                    this.$axios({
                        method: 'post',
                        url: '/user/login',
                        data: {
                            login_name: this.loginForm.username,
                            password: this.loginForm.password
                        },
                        headers: {
                            'Content-Type': 'application/json'
                        }
                    }).then(res => {
                        // console.log(res.data);
                        // console.log("Token: " + res.headers["token"]);
                        // Save user's info & token
                        this.$store.commit('saveUserInfo', res.data.info);
                        this.$store.commit('saveToken', res.headers["token"]);
                        this.$notify({
                            title: 'Success',
                            message: 'Welcome! ',
                            type: 'success'
                        });
                        this.$router.push('/deepLearning');
                    }).catch(error => {
                        console.log(error);
                        // alert("Username or password is wrong! ");
                        // alert("ERROR  in function \"login( )\" [login]! Check Console plz! ");
                        this.$notify.error({
                            title: 'Error',
                            message: 'Username or password is incorrect. '
                        });
                    });
                }
            },
            signUp() {
                this.$router.push('/signUp');
            },
            clearForm() {
                this.$refs['loginForm'].resetFields();
            }
        },
        beforeRouteEnter(to, from, next) {
            next(vm => {
                vm.clearForm();
            })
        }
    }
</script>

<style scoped>
  #log-in {
    text-align: center;

    /*background-color: yellowgreen;*/
  }

  #info-div {
    width: 300px;
    height: 280px;
    /*width: 30%;*/
    /*margin: 100px auto;*/
    text-align: center;
    display: inline-block;
    vertical-align: middle;

    /*background-color: rgba(64, 158, 255, 0.2);*/

    /*border: 1px solid #dddddd;*/
    /*border-radius: 5px;*/
  }

  #info-head {
    font-size: 32px;
    font-weight: bolder;
    margin-top: 85px;
  }

  #info-content {
    font-size: 26px;
    /*font-weight: bolder;*/
    /*margin-top: 40px;*/
  }

  #log-in-div {
    width: 450px;
    /*width: 50%;*/
    margin: 120px auto;
    text-align: center;
    border: 1px solid #cccccc;
    border-radius: 5px;
    display: inline-block;
    vertical-align: middle;

    /*background-color: pink;*/
  }

  #form-head {
    font-size: 24px;
    font-weight: bolder;
    color: #409EFF;
    /*font-family: "Helvetica Neue";*/
  }

  .el-form {
    padding: 30px 20px;
  }

  .el-form-item {
    margin-top: 40px;
    margin-bottom: 40px;
  }

  .el-input {
    width: 300px;
  }
</style>
