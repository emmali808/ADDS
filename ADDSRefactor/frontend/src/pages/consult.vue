<template>
  <div>
    <el-container>
      <NavigationBar/>
      <el-container id="consult-div">
        <el-container id="chat-div">
          <el-main>
            <div class="option-block">
              <el-select v-model="characterChosen" placeholder="Choose a people to consult" @change="chooseCharacter">
                <el-option-group v-for="group in characterList" :key="group.label" :label="group.label">
                  <el-option v-for="item in group.options" :key="item.value" :label="item.label" :value="item.value">
                  </el-option>
                </el-option-group>
              </el-select>
            </div>
            <div id="chat-history-box">
              <div id="chat-history" ref="chat">
                {{chats}}
              </div>
            </div>
          </el-main>
          <el-footer>
            <div id="input">
              <el-input placeholder="input here" v-model="input" @keyup.enter.native="send">
                <el-button slot="append" @click="send">SEND</el-button>
              </el-input>
            </div>
          </el-footer>
        </el-container>
        <el-container id="info-div">
          <el-main id="intro">
            <div>
              <el-avatar>
                <img v-bind:src="require('./../assets/' + characterImgPath)"/>
              </el-avatar>
            </div>
            <div id="name-box">
              <div>{{characterName}}</div>
            </div>
            <div id="name-box1">
              <div style="font-size: 12px;">{{characterIntro}}</div>
            </div>
          </el-main>
        </el-container>
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
          input: '',
          chats: '',
          characterList: [{
            options: [
                {value:1, label: "AI Robot"},
                {value:2, label: "Yadi Li"}
              ]
          }],
          characterChosen: '',
          characterImgPath: 'doctor.png',
          characterName: 'Yadi Li',
          characterIntro: 'Yadi Li is currently an attending doctor in the department of dermatology, Beijing Tongren hospital. She received her M.D. in dermatology from Capital Medical University in 2012. Her sub-specialties are skin cosmetic medicine and dermatology surgery.'
        }
      },
      methods: {
        send() {
          this.chats += this.input + '\n'
          this.input = ''
        },
        chooseCharacter(){
          if(this.characterChosen == 1) {
            this.characterImgPath = 'robot.jpg'
            this.characterName = 'AI robot'
            this.characterIntro = 'AI robot automatically replies your questions.'
          }
          else {
            this.characterImgPath = 'doctor.png'
            this.characterName = 'Yadi Li'
            this.characterIntro = 'Yadi Li is currently an attending doctor in the department of dermatology, Beijing Tongren hospital. She received her M.D. in dermatology from Capital Medical University in 2012. Her sub-specialties are skin cosmetic medicine and dermatology surgery.'
          }
        }
      }
    }
</script>

<style scoped>
  #consult-div {
    position: fixed;
    top: 25%;
    left: 25%;
    right: 25%;
    bottom: 10%;
    border: 1px rgb(180, 180, 180) solid;
    border-radius: 5px;
  }

  #chat-div {
    width: 70%;
    height: 100%;
    margin-right: 0;
  }

  #chat-history-box {
    height: 99%;
    background-color: #fff;
    border: 1px rgb(180, 180, 180) solid;
    border-radius: 5px;
  }

  #chat-history {
    margin-left: 20px;
    margin-right: 20px;
    white-space: pre-line;
  }
  
  #input {
    width: 100%;
    height: 80%;
    margin-bottom: 0px;
  }

  .el-input {
    width: 100%;
  }

  #info-div {
    width: 32%;
    height: 100%;
    padding-top: 50px;
    text-align: center;
    border-left: 1px rgb(180, 180, 180) solid;
    border-radius: 5px;
    background-color: rgb(225, 243, 255);
  }

  #name-box {
    margin-top: 20px;
  }
  #name-box1 {
    margin-top: 10px;
    text-align: left;
    margin-left: 5%;
  }

  .intro {
    padding-top: 50px;
  }
  .option-block {
    display: inline-block;
    position: fixed;
    top: 15%;
    /*margin-right: 40px;*/
  }

  .option-block .el-select {
    width: 400px;
  }
</style>
