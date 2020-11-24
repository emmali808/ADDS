<template>
  <el-aside width="240px" class="scrollable-div">
    <el-scrollbar ref="AutoDiagnosisMenuScrollBar">
      <el-menu
        router
        :default-active="this.$route.path"
        :default-openeds="['2']">
        <el-menu-item index="/autoDiagnosis/uploadMedicalRecords">Upload Medical Records</el-menu-item>
        <el-menu-item index="/autoDiagnosis/machineDiagnosis">Machine Diagnosis</el-menu-item>
        <el-menu-item index="/autoDiagnosis/submitQuestions">Submit Questions</el-menu-item>
      </el-menu>
    </el-scrollbar>
  </el-aside>
</template>

<script>
    export default {
        name: "AutoDiagnosisMenu",
        data() {
            return {
                category: []
            };
        },
        methods: {
        },
        created() {
        }
    }
</script>

<style scoped>
  .el-aside {
    position: fixed;
    top: 80px;
    bottom: 0;
    background-color: rgb(238, 241, 246);
    z-index: 999;
  }

  .el-scrollbar {
    height: 100%;
  }
</style>
