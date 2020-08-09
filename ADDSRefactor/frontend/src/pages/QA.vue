<template>
  <div>
    <el-container>
      <NavigationBar/>
      <el-container class="parent-div">
        <keep-alive>
          <router-view v-if="$route.meta.keepAlive"/>
        </keep-alive>
        <router-view v-if="!$route.meta.keepAlive"/>
      </el-container>
    </el-container>
  </div>
</template>

<script>
    import NavigationBar from "../components/NavigationBar";
    export default {
        name: "QA",
        components: {
            NavigationBar
        }
    }
</script>

<style scoped>
  .parent-div {
    position: fixed;
    top: 0;
    left: 30px;
    right: 30px;
    bottom: 0;
  }
</style>
