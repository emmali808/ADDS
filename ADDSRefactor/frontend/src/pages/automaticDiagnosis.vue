<template>
  <div>
    <el-container>
      <NavigationBar/>
      <el-container id="container" direction="vertical">
        <div id="browseSection">File Upload: </div>
        <div id="descriptionSection">Description: </div>
        <textarea id="textArea" rows="10" cols="80"></textarea>

        <el-upload
          ref="upload"
          action="http://127.0.0.1:8080/diagnosis/upload"
          accept=".zip"
          :file-list="fileList"
          :limit="1"
          :auto-upload="false">
          
          <el-button slot="trigger" size="small" type="primary" id="browseBtn">Browse</el-button>
          <el-button size="small" type="primary" @click="uploadFile" id="submitBtn">Submit</el-button>
        </el-upload>
        <el-button  size="small" type="primary" @click="create" id="create">create graph</el-button>

      </el-container>
    </el-container>
  </div>
</template>

<script>
    import NavigationBar from "../components/NavigationBar";
    export default {
      name: "autoDiagnosis",
      components: {
          NavigationBar
      },
      data() {
        return {
          fileList: [],
          // headers: {Token: sessionStorage.getItem("addsCurrentUserToken")}
        }
      },
      methods: {
        uploadFile: function(params){
          this.$refs.upload.submit();
        },
        create() {
          this.$axios({
              method: 'get',
              url: 'http://127.0.0.1:8080/kg/createGraph'
          }).catch(error => {
              console.log(error);
          });
        },
      }
    }
</script>

<style scoped>
  #container {
    position: fixed;
    top: 20%;
    left: 25%;
    right: 25%;
    bottom: 10%;
    border: 1px rgb(180, 180, 180) solid;
    border-radius: 5px;
  }

  #browseSection {
    margin-left: 40px;
    margin-top: 30px;
  }

  #descriptionSection {
    margin-left: 40px;
    margin-top: 40px;
  }

  #textArea {
    width: 600px;
    height: 150px;
    margin-left: 40px;
    margin-top: 5px;
  }

  #browseBtn {
    position: relative;
    left: 140px;
    top: -245px;
  }

  #submitBtn {
    position: relative;
    left: 250px;
    top: 40px;
  }

  #create {
    margin-top: 50px;
    width: 100px;
    margin-left: auto;
    margin-right: auto;
  }

</style>
