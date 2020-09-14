<template>
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
    <!-- <el-button  size="small" type="primary" @click="create" id="create">create graph</el-button> -->

  </el-container>
</template>

<script>
    export default {
      name: "UploadMedicalRecords",
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
        // create() {
        //   this.$axios({
        //       method: 'get',
        //       url: 'http://127.0.0.1:8080/kg/createGraph'
        //   }).catch(error => {
        //       console.log(error);
        //   });
        // },
      }
    }
</script>

<style scoped>
  #container {
    position: fixed;
    top: 80px;
    left: 260px;
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
    left: 230px;
    top: 40px;
  }

</style>
