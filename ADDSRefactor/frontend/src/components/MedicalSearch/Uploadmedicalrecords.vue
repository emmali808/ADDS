<template>
  <el-container id="container" direction="vertical">

      <el-form v-if="this.$store.state.user.type != 3" ref="downloadForm3" :model="downloadForm3" label-position="left" label-width="110px">
          <el-form-item label="File Format:">
              <el-button
                      type="primary"
                      size="small"
                      @click="downloadSampleImageFile">
                  Download
              </el-button>
          </el-form-item>
      </el-form>

    <el-form v-if="this.$store.state.user.type != 3" ref="uploadForm1" :model="uploadForm1" label-position="left" label-width="110px">
      <el-form-item label="File Upload:" prop="uploadFile">
        <el-upload
          ref="upload"
          action=""
          accept=".zip"
          :multiple="false"
          :file-list="fileList1"
          :show-file-list="true"
          :http-request="uploadMedicalArchive"
          :auto-upload="false">
          <el-button slot="trigger" size="small" type="primary">Browse</el-button>
        </el-upload>
      </el-form-item>
      <el-form-item label="Description:" prop="desc">
        <el-input type="textarea" :rows="6" v-model="uploadForm1.desc"></el-input>
      </el-form-item>
    </el-form>
    <div v-if="this.$store.state.user.type != 3" id="footer1">
      <el-button size="small" type="primary" @click="upload">Submit</el-button>
    </div>


          <el-form v-if="this.$store.state.user.type == 3" ref="downloadForm2" :model="downloadForm2" label-position="left" label-width="170px">
              <el-form-item label="Sample File Format:">
          <el-button
                  type="primary"
                  size="small"
                  @click="downloadSampleCsvFile">
              Download
          </el-button>
              </el-form-item>
          </el-form>

      <el-form v-if="this.$store.state.user.type == 3" ref="uploadForm2" :model="uploadForm2" label-position="left" label-width="170px">
          <el-form-item label="Medical Cases Upload:" prop="uploadFile">
              <el-upload
                      ref="uploadCsv"
                      action=""
                      accept=".csv"
                      :multiple="false"
                      :file-list="fileList2"
                      :show-file-list="true"
                      :http-request="uploadHistoryAdmissions"
                      :auto-upload="false">
                  <el-button slot="trigger" size="small" type="primary">Browse</el-button>
              </el-upload>
          </el-form-item>


          <el-form-item label="Description:" prop="desc">
              <el-input type="textarea" :rows="6" v-model="uploadForm2.desc"></el-input>
          </el-form-item>

      </el-form>
      <div v-if="this.$store.state.user.type == 3" id="footer2">
          <el-button size="small" type="primary" @click="uploadCsv">Submit</el-button>
      </div>

  </el-container>
</template>



<script>
    export default {
      name: 'UploadMedicalRecords',
      data() {
        return {
          uploadForm1: {
            desc: 'Upload medical case By' + ' User: ' + this.$store.state.user.login_name,
          },
            uploadForm2: {
                desc: 'Upload medical cases By' + ' Manager: ' + this.$store.state.user.login_name,
            },

            downloadForm1:{

            },
            downloadForm2:{

            },

            downloadForm3:{

            },
          fileList1: [],
            fileList2: [],
          // headers: {Token: sessionStorage.getItem('addsCurrentUserToken')}
        }
      },
      methods: {
        upload() {
          this.$refs.upload.submit();
        },
         uploadCsv() {
              this.$refs.uploadCsv.submit();
         },
          downloadSampleCsvFile(){
              let link = document.createElement('a');
              link.style.display = 'none';
              link.href = window.location.origin + this.$axios.defaults.baseURL + '/case/download/SampleCsvFile';
              document.body.appendChild(link);
              link.click();
              link.remove();
          },
          downloadSampleImageFile(){
              let link = document.createElement('a');
              link.style.display = 'none';
              link.href = window.location.origin + this.$axios.defaults.baseURL + '/case/download/SampleImageFile';
              document.body.appendChild(link);
              link.click();
              link.remove();
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
          params.append('desc', this.uploadForm1.desc);
          params.append('file', content.file);

          this.$axios({
            method: 'post',
            url: '/case/user/' + this.$store.state.user.id,
            data: params
          }).then(res => {
            this.$refs['uploadForm1'].resetFields();
            this.$message({
              type: 'success',
              message: 'Successully uploaded medical case!',
              showClose: true
            });
            this.$axios({
              method: 'post',
              url: '/caseSearch/' + res.data['medicalCaseId'],
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

        uploadHistoryAdmissions(content) {
          var date = new Date();
          var dateString =
                  date.getUTCFullYear() +
                  ("0" + (date.getUTCMonth()+1)).slice(-2) +
                  ("0" + date.getUTCDate()).slice(-2) +
                  ("0" + date.getUTCHours()).slice(-2) +
                  ("0" + date.getUTCMinutes()).slice(-2) +
                  ("0" + date.getUTCSeconds()).slice(-2);

          let params = new FormData();
          params.append('title', this.$store.state.user.id + ' Upload History Admissions ' + dateString);
          params.append('category', 'N/A');
          params.append('desc', this.uploadForm2.desc);
          params.append('file', content.file);

          this.$axios({
            method: 'post',
            url: '/case/history_admissions/'+ this.$store.state.user.id,
            data: params
          }).then(res => {
            this.$refs['uploadForm2'].resetFields();
            this.$message({
              type: 'success',
              message: 'Successully uploaded history admissions!',
              showClose: true
            });
          }).catch(error => {
            this.$message({
              type: 'error',
              message: 'Failed to upload! Check console.',
              showClose: true
            });
            console.log(error);
          });
        }
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

  #footer1 {
      text-align: center;
  }

  #footer2 {
      text-align: center;
  }
</style>
