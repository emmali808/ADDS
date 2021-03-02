<template>
  <el-aside width="240px" class="scrollable-div">
    <el-scrollbar ref="MedicalCaseSearchMenuScrollBar">
      <el-menu
        router
        :default-active="this.$route.path"
        :default-openeds="['2']">
        <el-menu-item index="/medicalCaseDeepSearch/uploadMedicalRecords">Upload Medical Records</el-menu-item>
        <el-menu-item index="/medicalCaseDeepSearch/caseSearch">Medical Case Search</el-menu-item>
        <el-menu-item index="/medicalCaseDeepSearch/submitQuestions">Submit Questions</el-menu-item>
      </el-menu>
    </el-scrollbar>
  </el-aside>
</template>

<script>
    export default {
        name: "MedicalCaseSearchMenu",
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
