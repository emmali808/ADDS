<template>
  <div>
    <el-container>
      <NavigationBar/>
      <el-container class="content-div">
        
      </el-container>
    </el-container>
  </div>
</template>

<script>
    import NavigationBar from "../components/NavigationBar";
    export default {
        name: "medicalArchivePreprocess",
        components: {
            NavigationBar
        }
    }
</script>

<style scoped>
  .content-div {
    position: fixed;
    top: 0;
    left: 30px;
    right: 30px;
    bottom: 0;
  }
</style>
