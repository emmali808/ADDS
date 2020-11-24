<template>
  <el-container id="container" direction="vertical">
    <el-form ref="uploadForm" :model="uploadForm" label-position="left" label-width="100px">
      <el-form-item label="File Upload:" prop="uploadFile">
        <el-upload
          ref="upload"
          action=""
          accept=".zip"
          :multiple="false"
          :file-list="fileList"
          :show-file-list="true"
          :http-request="uploadMedicalArchive"
          :auto-upload="false">
          <el-button slot="trigger" size="small" type="primary">Browse</el-button>
        </el-upload>
      </el-form-item>
      <el-form-item label="Description:" prop="desc">
        <el-input type="textarea" :rows="6" v-model="uploadForm.desc"></el-input>
      </el-form-item>
    </el-form>
    <div id="footer">
      <el-button size="small" type="primary" @click="upload">Submit</el-button>
    </div>
  </el-container>
</template>

<script>
    export default {
      name: 'UploadMedicalRecords',
      data() {
        return {
          uploadForm: {
            desc: '',
          },
          fileList: [],
          // headers: {Token: sessionStorage.getItem('addsCurrentUserToken')}
        }
      },
      methods: {
        upload() {
          this.$refs.upload.submit();
        },
        uploadMedicalArchive(content) {
          var date = new Date();
          var dateString =
              date.getUTCFullYear() +
              ("0" + (date.getUTCMonth()+1)).slice(-2) +
              ("0" + date.getUTCDate()).slice(-2) +
              ("0" + date.getUTCHours()).slice(-2) +
              ("0" + date.getUTCMinutes()).slice(-2) +
              ("0" + date.getUTCSeconds()).slice(-2);

          let params = new FormData();
          params.append('title', 'Patient ' + this.$store.state.user.id + ' Medical Archive ' + dateString);
          params.append('category', 'N/A');
          params.append('desc', this.uploadForm.desc);
          params.append('file', content.file);

          this.$axios({
            method: 'post',
            url: '/archive/user/' + this.$store.state.user.id,
            data: params
          }).then(res => {
            this.$refs['uploadForm'].resetFields();
            this.$message({
              type: 'success',
              message: 'Successully uploaded medical archive!',
              showClose: true
            });
            this.$axios({
              method: 'post',
              url: '/diagnosis/' + res.data['medicalArchiveId'],
            });
          }).catch(error => {
            this.$message({
              type: 'error',
              message: 'Failed to upload! Check console.',
              showClose: true
            });
            console.log(error);
          });
        },
      }
    }
</script>

<style scoped>
  #container {
    position: fixed;
    top: 100px;
    left: 300px;
    width: 600px;
  }

  #footer {
    text-align: center;
  }
</style>
