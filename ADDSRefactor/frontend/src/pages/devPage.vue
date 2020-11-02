<template>
  <div>
    <el-container>
      <NavigationBar/>
      <el-container id="container" direction="vertical">
        <el-button  size="small" type="primary" @click="magic" id="magic">magic button</el-button>
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
        }
      },
      methods: {
        magic() {
          this.$axios({
                method: 'post',
                url: '/kg/createGraph',
          }).then(res => {
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
    top: 0%;
    left: 0%;
    right: 0%;
    bottom: 0%;
  }

  #magic {
    width: 100px;
    margin: auto;
  }

</style>
