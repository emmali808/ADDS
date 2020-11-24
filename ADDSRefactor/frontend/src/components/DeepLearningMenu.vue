<template>
  <el-aside width="240px" class="scrollable-div">
    <el-scrollbar ref="deepLearningMenuScrollBar">
      <el-menu
        router
        @select=""
        @open=""
        @close=""
        :default-active="this.$route.path"
        :default-openeds="['2']">
        <el-menu-item index="/deepLearning/deepModels">Deep Models</el-menu-item>
        <el-submenu index="2">
          <template slot="title">Data Preparation</template>
          <el-menu-item-group>
            <el-menu-item index="/deepLearning/uploadDataset">Dataset</el-menu-item>
            <el-menu-item index="/deepLearning/uploadKnowledgeGraph">Knowledge Graph</el-menu-item>
          </el-menu-item-group>
        </el-submenu>
        <el-menu-item index="/deepLearning/modelEvaluation">Model Evaluation</el-menu-item>
        <el-menu-item index="/deepLearning/autoSelection">Auto Selection</el-menu-item>
        <el-menu-item index="/deepLearning/knowledgeExploration">Knowledge Exploration</el-menu-item>
      </el-menu>
    </el-scrollbar>
  </el-aside>
</template>

<script>
    export default {
        name: "DeepLearningMenu",
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
