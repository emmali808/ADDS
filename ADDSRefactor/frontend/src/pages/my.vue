<template>
  <div>
    <el-container>
      <NavigationBar/>
      <el-container>
        <el-main>
          <div id="my-info-div">
            <el-form ref="myInfoForm" :model="myInfoForm" :rules="rules" label-position="left" label-width="90px">
              <div id="my-info-div-1">
                <div id="my-info-div-1-1">
                  <el-form-item label="Name" prop="name">
                    <el-input v-model="myInfoForm.name"></el-input>
                  </el-form-item>
                  <el-form-item label="Gender" prop="gender">
                    <el-input v-model="myInfoForm.gender"></el-input>
                  </el-form-item>
                  <el-form-item label="Age" prop="age">
                    <el-input v-model="myInfoForm.age"></el-input>
                  </el-form-item>
                </div>
                <div id="my-info-div-1-2">
                  <el-form-item label="Picture" prop="picture">
                    <el-input v-model="myInfoForm.picture"></el-input>
                  </el-form-item>
                </div>
              </div>
              <div id="my-info-div-2">
                <div id="my-info-div-2-1">
                  <el-form-item label="Nation" prop="nation">
                    <el-input v-model="myInfoForm.nation"></el-input>
                  </el-form-item>
                  <el-form-item label="Birthday" prop="birthday">
                    <el-input v-model="myInfoForm.birthday"></el-input>
                  </el-form-item>
                  <el-form-item label="Phone" prop="phone">
                    <el-input v-model="myInfoForm.phone"></el-input>
                  </el-form-item>
                  <el-form-item label="Birthplace" prop="birthplace">
                    <el-input v-model="myInfoForm.birthplace"></el-input>
                  </el-form-item>
                </div>
                <div id="my-info-div-2-2">
                  <el-form-item label="IdentityID" prop="identityID">
                    <el-input v-model="myInfoForm.identityID"></el-input>
                  </el-form-item>
                  <el-form-item label="Marriage" prop="marriage">
                    <el-input v-model="myInfoForm.marriage"></el-input>
                  </el-form-item>
                  <el-form-item label="Email" prop="email">
                    <el-input v-model="myInfoForm.email"></el-input>
                  </el-form-item>
                  <el-form-item label="Workplace" prop="workplace">
                    <el-input v-model="myInfoForm.workplace"></el-input>
                  </el-form-item>
                </div>
              </div>


<!--              <el-form-item label="Name" prop="name">-->
<!--                <el-input v-model="myInfoForm.name"></el-input>-->
<!--              </el-form-item>-->
<!--              <el-form-item label="Gender" prop="gender">-->
<!--                <el-input v-model="myInfoForm.gender"></el-input>-->
<!--              </el-form-item>-->
<!--              <el-form-item label="Age" prop="age">-->
<!--                <el-input v-model="myInfoForm.age"></el-input>-->
<!--              </el-form-item>-->
<!--              <el-form-item label="Picture" prop="picture">-->
<!--                <el-input v-model="myInfoForm.picture"></el-input>-->
<!--              </el-form-item>-->
<!--              <el-form-item label="Nation" prop="nation">-->
<!--                <el-input v-model="myInfoForm.nation"></el-input>-->
<!--              </el-form-item>-->
<!--              <el-form-item label="IdentityID" prop="identityID">-->
<!--                <el-input v-model="myInfoForm.identityID"></el-input>-->
<!--              </el-form-item>-->
<!--              <el-form-item label="Birthday" prop="birthday">-->
<!--                <el-input v-model="myInfoForm.birthday"></el-input>-->
<!--              </el-form-item>-->
<!--              <el-form-item label="Marriage" prop="marriage">-->
<!--                <el-input v-model="myInfoForm.marriage"></el-input>-->
<!--              </el-form-item>-->
<!--              <el-form-item label="Phone" prop="phone">-->
<!--                <el-input v-model="myInfoForm.phone"></el-input>-->
<!--              </el-form-item>-->
<!--              <el-form-item label="Email" prop="email">-->
<!--                <el-input v-model="myInfoForm.email"></el-input>-->
<!--              </el-form-item>-->
<!--              <el-form-item label="Birth Place" prop="birthplace">-->
<!--                <el-input v-model="myInfoForm.birthplace"></el-input>-->
<!--              </el-form-item>-->
<!--              <el-form-item label="Work Place" prop="workplace">-->
<!--                <el-input v-model="myInfoForm.workplace"></el-input>-->
<!--              </el-form-item>-->



<!--              <el-form-item label="Login Name" prop="loginName">-->
<!--                <el-input v-model="myInfoForm.loginName"></el-input>-->
<!--              </el-form-item>-->
<!--              <el-form-item label="Password" prop="password">-->
<!--                <el-input v-model="myInfoForm.password"></el-input>-->
<!--              </el-form-item>-->
<!--              <el-form-item label="Type" prop="type">-->
<!--                <el-input v-model="myInfoForm.type"></el-input>-->
<!--              </el-form-item>-->
<!--              <el-form-item label="State" prop="state">-->
<!--                <el-input v-model="myInfoForm.state"></el-input>-->
<!--              </el-form-item>-->



              <el-form-item style="text-align: center;margin: 40px auto;">
                <el-button type="primary" @click="modifyMyInfo">Modify</el-button>
                <el-button @click="resetMyInfo">Reset</el-button>
              </el-form-item>
            </el-form>
          </div>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
    import NavigationBar from "../components/NavigationBar";
    export default {
        name: "my",
        components: {
            NavigationBar
        },
        data() {
            return {
                myInfoForm: {
                    name: this.$store.state.user.username,
                    age: this.$store.state.user.age,
                    gender: this.$store.state.user.gender,
                    phone: this.$store.state.user.phone,
                    email: this.$store.state.user.email,
                    identityID: this.$store.state.user.identityId,
                    birthday: this.$store.state.user.birthday,
                    nation: this.$store.state.user.nation,
                    marriage: this.$store.state.user.marriage,
                    birthplace: this.$store.state.user.birth_place,
                    workplace: this.$store.state.user.work_place,

                    picture: '(null)',

                    loginName: '',
                    password: '',
                    type: '',
                    state: ''
                },
                rules: {
                    name: [
                        {required: true, message: 'Please input name', trigger: 'blur'}
                        // {min: 1, max: 10, message: 'Name length is between 1 and 10', trigger: 'blur'}
                    ],
                    gender: [
                        {required: true, message: 'Please input gender', trigger: 'blur'}
                    ],
                    age: [
                        {required: true, message: 'Please input age', trigger: 'blur'}
                    ],
                    nation: [
                        {required: true, message: 'Please input nation', trigger: 'blur'}
                    ],
                    identityID: [
                        {required: true, message: 'Please input identityID', trigger: 'blur'}
                    ],
                    birthday: [
                        {required: true, message: 'Please input birthday', trigger: 'blur'}
                    ],
                    marriage: [
                        {required: true, message: 'Please input marriage', trigger: 'blur'}
                    ],
                    phone: [
                        {required: true, message: 'Please input phone', trigger: 'blur'}
                    ],
                    email: [
                        {required: true, message: 'Please input email', trigger: 'blur'}
                    ],
                    birthplace: [
                        {required: true, message: 'Please input birthplace', trigger: 'blur'}
                    ],
                    workplace: [
                        {required: true, message: 'Please input workplace', trigger: 'blur'}
                    ]
                }
            };
        },
        methods: {
            modifyMyInfo() {
                this.$message.error('修改个人信息的功能暂未开放哦！');
            },
            resetMyInfo() {
                this.$refs['myInfoForm'].resetFields();
                this.$message({
                    type: 'success',
                    message: 'Reset successfully! '
                });
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
    /*background-color: salmon;*/

    /*padding-left: 300px;*/
    /*padding-top: 80px;*/
  }

  #my-info-div {
    /*width: 1000px;*/
    margin: 40px 80px;
    /*margin: 40px auto;*/
  }

  #my-info-div-1, #my-info-div-2 {
    width: 900px;
    margin: 0 auto;
    overflow: hidden;
  }

  #my-info-div-1-1, #my-info-div-2-1 {
    float: left;
  }

  #my-info-div-1-2, #my-info-div-2-2 {
    float: right;
  }

  .el-input {
    width: 300px;
  }
</style>
