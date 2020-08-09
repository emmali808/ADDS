<template>
  <div>
    <el-container>
      <NavigationBar/>
      <el-container>
        <el-main>
          <div class="option-bar">
            <div class="option-block">
              <el-select v-model="kgChosen" placeholder="Choose a knowledge graph" @change="loadKG">
                <el-option-group v-for="group in kgGroupList" :key="group.label" :label="group.label">
                  <el-option v-for="item in group.options" :key="item.value" :label="item.label" :value="item.value">
                  </el-option>
                </el-option-group>
              </el-select>
            </div>
<!--            <div class="option-block">-->
<!--              <el-button @click="initGraph">Reset</el-button>-->
<!--            </div>-->
<!--            <div class="option-block">-->
<!--              <el-button type="primary" @click="downloadKg">Download</el-button>-->
<!--            </div>-->
          </div>

<!--          <div class="btn-div">-->
<!--            <el-select v-model="kgChosen" placeholder="Choose a knowledge graph">-->
<!--              <el-option-group v-for="group in kgGroupList" :key="group.label" :label="group.label">-->
<!--                <el-option v-for="item in group.options" :key="item.value" :label="item.label" :value="item.value">-->
<!--                </el-option>-->
<!--              </el-option-group>-->
<!--            </el-select>-->
<!--            <el-button @click="initGraph">Reset</el-button>-->
<!--            <el-button type="primary" @click="downloadKg">Download</el-button>-->
<!--          </div>-->
          <div class="kg-div">
            <div class="svg-div" ref="svgDiv">
              <svg width="100%" height="100%">
              </svg>
            </div>
          </div>
          <div class="info-div">
            <el-card>
              <div slot="header" class="">
                <span>{{kgEntityInfo.entityType}}</span>
                <el-button v-if="kgNodeUnfoldFlag[kgEntityInfo.id]" type="primary" size="mini" plain style="float: right;" @click="unfoldRelNodes">Unfold</el-button>
                <el-button v-else type="info" size="mini" plain style="float: right;" @click="resetGraph">Ellipsis</el-button>
              </div>
              <div>
                <span><i>_NODE_INFO_</i></span><br/>
                <span><i>name: {{kgEntityInfo.name}}</i></span><br/>
                <span v-if="kgEntityInfo.childNode.length !== 0"><i>child: {{kgEntityInfo.childNode}}</i></span>
                <span v-else><i>child: {{kgEntityInfo.noChildNodeInfo}}</i></span>
              </div>
            </el-card>
          </div>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
    import NavigationBar from "../components/NavigationBar";

    import * as d3 from 'd3';
    // import d3tooltip from 'd3-tooltip';

    import GraphData from "../assets/GraphData";

    export default {
        name: "knowledgeGraph",
        components: {
            NavigationBar
        },
        data() {
            return {
                kgGroupList: [{
                    label: 'System Knowledge Graph',
                    options: []
                }, {
                    label: 'My Knowledge Graph',
                    options: []
                }],
                kgChosen: '',
                kgSvgComponents: {
                    svg: null,
                    simulation: null,
                    colors: [],

                    linkGroup: [],
                    linkTextGroup: [],
                    nodeGroup: [],
                    nodeTextGroup: []
                },
                kgNodeBufferedMap: [],
                kgNodeUnfoldFlag: [],
                kgEntityInfo: {
                    entityType: 'Entity Info',
                    id: -1,
                    name: '',
                    childNode: [],
                    noChildNodeInfo: '',
                    status: true
                },
                kgData: {
                    nodes: [],
                    links: []
                },


                kgNodeMap: [],
                kgNodeChildMap: [],
                bufferedChildEntities: {
                    nodes: [],
                    links: []
                }
            };
        },
        methods: {
            loadKGList() {
                this.$axios({
                    method: 'get',
                    url: '/kg/doctor/' + this.$store.state.user.id,
                }).then(res => {
                    this.kgGroupList[1].options.length = 0;
                    for (let kg in res.data) {
                        if (res.data.hasOwnProperty(kg)) {
                            this.kgGroupList[1].options.push({
                                value: res.data[kg].id,
                                label: res.data[kg].name
                            });
                        }
                    }
                }).catch(error => {
                    console.log(error);
                });
            },
            loadKG() {
              this.removeCurrentKgData();
              // this.kgNodeMap.length = 0;
              // this.kgNodeChildMap.length = 0;
              this.$axios({
                  method: 'get',
                  url: '/kg/graph/' + this.kgChosen,
              }).then(res => {
                  let nodes = res.data.nodes;
                  let links = res.data.links;
                  this.kgData.nodes = nodes;
                  this.kgData.links = links;
                  for (let node in nodes) {
                      if (nodes.hasOwnProperty(node)) {
                          this.kgNodeBufferedMap[nodes[node].id] = {
                              id: nodes[node].id,
                              name: nodes[node].name,
                              childNode: [],
                              relations: [],
                              loadedInGraph: true
                          };
                      }
                  }
                  for (let link in links) {
                      if (links.hasOwnProperty(link)) {
                          this.kgNodeBufferedMap[links[link].source].childNode.push({
                              id: this.kgNodeBufferedMap[links[link].target].id,
                              name: this.kgNodeBufferedMap[links[link].target].name
                          });
                          this.kgNodeBufferedMap[links[link].source].relations.push(links[link]);
                          this.kgNodeUnfoldFlag[links[link].source] = false;
                          this.kgNodeUnfoldFlag[links[link].target] = true;
                      }
                  }
                  this.paintGraph();
              }).catch(error => {
                  console.log(error);
              });
            },
            removeCurrentKgData() {
                this.kgNodeBufferedMap.length = 0;
                this.kgNodeUnfoldFlag.length = 0;
                this.kgEntityInfo.entityType = 'Entity Info';
                this.kgEntityInfo.id = -1;
                this.kgEntityInfo.name = '';
                this.kgEntityInfo.childNode.length = 0;
                this.kgEntityInfo.noChildNodeInfo = '';
                this.kgData.nodes.length = 0;
                this.kgData.links.length = 0;
            },
            displayNodeInfo(nodeId) {
                this.kgEntityInfo.entityType = 'Node';
                this.kgEntityInfo.id = nodeId;
                this.kgEntityInfo.name = this.kgNodeBufferedMap[nodeId].name;
                let childNode = this.kgNodeBufferedMap[nodeId].childNode;
                if (childNode != null) {
                  if (childNode.length === 0) {
                    this.kgEntityInfo.noChildNodeInfo = 'Loading...';
                    this.getRelNodes(nodeId);
                  } else {
                    this.kgEntityInfo.childNode = childNode;
                  }
                } else {
                  this.kgEntityInfo.noChildNodeInfo = 'No relation node';
                }
            },
            getRelNodes(nodeId) {
                if (this.kgNodeBufferedMap[nodeId].childNode.length === 0) {
                    this.$axios({
                        method: 'get',
                        url: '/kg/graph/relNodes/' + nodeId,
                    }).then(res => {
                        // console.log(res.data);
                        let nodes = res.data.nodes;
                        let links = res.data.links;
                        this.kgNodeBufferedMap[nodeId].childNode = nodes;
                        this.kgNodeBufferedMap[nodeId].relations = links;
                        this.kgEntityInfo.childNode = nodes;
                        for (let node in nodes) {
                            if (nodes.hasOwnProperty(node)) {
                                if (this.kgNodeBufferedMap[nodes[node].id] == null) {
                                    this.kgNodeBufferedMap[nodes[node].id] = {
                                        id: nodes[node].id,
                                        name: nodes[node].name,
                                        childNode: [],
                                        relations: [],
                                        loadedInGraph: false
                                    };
                                    this.kgNodeUnfoldFlag[nodes[node].id] = true;
                                }
                            }
                        }
                    }).catch(error => {
                        console.log(error);
                    });
                }
            },
            unfoldRelNodes() {
                let newNodes = this.kgNodeBufferedMap[this.kgEntityInfo.id].childNode;
                for (let node in newNodes) {
                    if (newNodes.hasOwnProperty(node)) {
                        let n = newNodes[node];
                        if (!this.kgNodeBufferedMap[n.id].loadedInGraph) {
                            this.kgData.nodes.push(n);
                            this.kgNodeBufferedMap[n.id].loadedInGraph = true;
                        }
                    }
                }
                // 这里可能会出现重复的link，使用linkSet去重是否有效？待测试
                let links = this.kgData.links.concat(this.kgNodeBufferedMap[this.kgEntityInfo.id].relations);
                let linkSet = new Set(links);
                this.kgData.links = Array.from(linkSet);
                // paintGraph方法重新设计
                this.paintGraph();
                this.kgNodeUnfoldFlag[this.kgEntityInfo.id] = false;
            },
            ellipsisRelNodes() {
            },
            resetGraph() {
                this.loadKG();
            },
            initGraph() {
                // console.log("Init Graph SVG");
                let width = this.$refs.svgDiv.offsetWidth;
                let height = this.$refs.svgDiv.offsetHeight;
                this.kgSvgComponents.simulation = d3.forceSimulation()
                    .force("link", d3.forceLink().id(d => {return d.id;}))
                    .force("charge", d3.forceManyBody().strength(100))
                    .force("center", d3.forceCenter(width / 2, height / 2))
                    .force("collide", d3.forceCollide(80).strength(0.2).iterations(5));
                this.kgSvgComponents.colors = [
                    '#6ca46c', '#4e88af',
                    '#ca635f', '#d2907c',
                    '#d6744d', '#ded295',
                    '#b4c6e7', '#cdb4e6'
                ];
                this.kgSvgComponents.svg = d3.select("svg");
                this.kgSvgComponents.linkGroup = this.kgSvgComponents.svg.append("g").attr("class", "links");
                this.kgSvgComponents.nodeGroup = this.kgSvgComponents.svg.append("g").attr("class", "nodes");
                this.kgSvgComponents.nodeTextGroup = this.kgSvgComponents.svg.append("g").attr("class", "texts");
            },
            paintGraph() {
                let svg = this.kgSvgComponents.svg;
                let simulation = this.kgSvgComponents.simulation;
                let colors = this.kgSvgComponents.colors;
                let transform = null;

                svg.selectAll("*").remove();
                let centerX = this.$refs.svgDiv.offsetWidth / 2;
                let centerY = this.$refs.svgDiv.offsetHeight / 2;

                let link = svg.append("g").attr("class", "links")
                    .selectAll("line").data(this.kgData.links).enter().append("line")
                    .attr("stroke-width", 1)
                    .style("stroke", "rgba(240, 240, 240, 0.8)");

                let node = svg.append("g").attr("class", "nodes")
                    .selectAll("circle").data(this.kgData.nodes).enter().append("circle")
                    .attr("cx", centerX)
                    .attr("cy", centerY)
                    .attr("r", d => {return 15;})
                    .attr("fill", d => {return colors[d.id % 8];})
                    .style("stroke", "#fff")
                    .style("stroke-width", "2px")
                    .style("cursor", "pointer")
                    .call(d3.drag().on("start", dragstarted).on("drag", dragged).on("end", dragended));

                node.on("click", d => {
                    this.displayNodeInfo(d.id);
                });

                let text = svg.append("g").attr("class", "texts")
                    .selectAll("text").data(this.kgData.nodes).enter().append("text")
                    .text(d => {return d.name;})
                    .attr("x", centerX)
                    .attr("y", centerY)
                    .attr("dy", 4)
                    .attr("text-anchor", "middle")
                    .attr("fill", "white")
                    .style("cursor", "pointer")
                    .call(d3.drag().on("start", dragstarted).on("drag", dragged).on("end", dragended));

                text.on("click", d => {
                    this.displayNodeInfo(d.id);
                });
                svg.selectAll("g").attr("transform", transform);

                simulation.nodes(this.kgData.nodes).on("tick", ticked);
                simulation.force("link").links(this.kgData.links);
                simulation.alpha(0.1).restart();
                svg.call(d3.zoom().on("zoom", () => {
                    svg.selectAll("g").attr("transform", d3.event.transform);
                    transform = d3.event.transform;
                }));
                svg.on("dblclick.zoom", null);
                function ticked() {
                    link
                        .attr("x1", function(d) {return d.source.x;})
                        .attr("y1", function(d) {return d.source.y;})
                        .attr("x2", function(d) {return d.target.x;})
                        .attr("y2", function(d) {return d.target.y;});
                    node
                        .attr("cx", function(d) {return d.x;})
                        .attr("cy", function(d) {return d.y;});
                    text
                        .attr("x", function(d) { return d.x; })
                        .attr("y", function(d) { return d.y; });
                }
                function dragstarted(d) {
                    if (!d3.event.active) simulation.alphaTarget(0.3).restart();
                    d.fx = d.x;
                    d.fy = d.y;
                }
                function dragged(d) {
                    d.fx = d3.event.x;
                    d.fy = d3.event.y;
                }
                function dragended(d) {
                    if (!d3.event.active) simulation.alphaTarget(0);
                    d.fx = null;
                    d.fy = null;
                }
            },
            downloadKg() {
            },
            paintKG() {
              // 获取 svg 元素所在块元素（svgDiv）的长度和高度，用来确定画布中心点
              let width = this.$refs.svgDiv.offsetWidth;
              let height = this.$refs.svgDiv.offsetHeight;

              // let names = ['Films', 'Characters', 'Planets', 'Starships', 'Vehicles', 'Species'];
              // 设置不同组的节点颜色
              let colors = [
                '#6ca46c', '#4e88af',
                '#ca635f', '#d2907c',
                '#d6744d', '#ded295',
                '#b4c6e7', '#cdb4e6'];
              // 获取图谱数据
              // let graph = GraphData.graph();
              let graph = this.kgData;

              // 设置节点半径
              for (let i = 0; i < graph.nodes.length; i++) {
                let nd = graph.nodes[i];
                nd.r = 15;
                // nd.r = nd.id.length * 3; //(test)
              }

              // 创建一个力学模拟器（force 力学图）
              let simulation = d3.forceSimulation()
                  .force("link", d3.forceLink().id(function (d) {
                    return d.id;
                  }))
                  // 设置万有引力，设置引力强度
                  .force("charge", d3.forceManyBody().strength(100))
                  // 设置居中力，中心点为画布中心点
                  .force("center", d3.forceCenter(width / 2, height / 2))
                  // 设置碰撞作用力，指定一个 radius 区域来防止节点重叠；设置碰撞力强度，范围是[0,1]，默认为0.7；设置迭代次数，默认为1（迭代次数越多最终的布局效果越好，但是计算复杂度更高）
                  .force("collide", d3.forceCollide(80).strength(0.2).iterations(5))
                // .force("collide", d3.forceCollide(100).strength(0.2).iterations(5)) //(test)
                // // 设置 alpha 系数，在计时器的每一帧中，仿真的 alpha 系数会不断削减，当 alpha 到达一个系数时，仿真将会停止，也就是 alpha 的目标系数 alphaTarget，该值区间为[0,1]，默认为0，控制力学模拟衰减率区间为[0-1]，设为0则不停止，默认0.0228，直到0.001
                // .alphaDecay(0.0228)
                // // 设置监听事件，例如监听 tick 滴答事件
                // .on("tick", ()=>this.ticked())
              ;

              // 获取 svg 元素
              let svg = d3.select("svg");
              svg.selectAll("*").remove();

              // 设置鼠标滚轮缩放
              svg.call(d3.zoom().on("zoom", function () {
                svg.selectAll("g").attr("transform", d3.event.transform);
              }));
              // 禁止双击缩放
              svg.on("dblclick.zoom", null);

              // 添加 line
              let link = svg.append("g").attr("class", "links")
                .selectAll("line").data(graph.links).enter().append("line")
                .attr("stroke-width", 1)
                .style("stroke", "rgba(240, 240, 240, 0.8)");

              // 添加 circle
              let node = svg.append("g").attr("class", "nodes")
                .selectAll("circle").data(graph.nodes).enter().append("circle")
                .attr("r", function (d) {
                  return d.r;
                })
                .attr("fill", function (d) {
                  return colors[d.id % 8];
                  // return 'rgb(140, 197, 255)';
                })
                .style("stroke", "#fff")
                .style("stroke-width", "2px")
                .style("cursor", "pointer")
                .call(d3.drag()
                  .on("start", dragstarted)
                  .on("drag", dragged)
                  .on("end", dragended));

              // node.append("title").text(d=>{return d.name});
              node.on("click", d => {
                // console.log(d.name);
                this.kgEntityInfo.entityType = 'Node';
                this.kgEntityInfo.name = d.name;
              });

              // 添加 text
              let text = svg.append("g").attr("class", "texts")
                .selectAll("text").data(graph.nodes).enter().append("text")
                .text(d=>{return d.name})
                .attr("dy", 4)
                // 设置文本对齐方式为居中（start | middle | end）
                .attr("text-anchor", "middle")
                .attr("fill", "white")
                // .style("display", "none") //(test)
                // .style("display", "inline-block")
                // .style("max-width", "15px")
                // .style("white-space", "nowrap")
                // .style("overflow", "hidden")
                // .style("text-overflow", "ellipsis")
                // .style("vertical-align", "middle")
                .style("cursor", "pointer")
                .call(d3.drag()
                  .on("start", dragstarted)
                  .on("drag", dragged)
                  .on("end", dragended));

              simulation.nodes(graph.nodes).on("tick", ticked);
              simulation.force("link").links(graph.links);

              //ticked()函数确定link线的起始点x、y坐标 node确定中心点 文本通过translate平移变化
              function ticked() {
                link
                  .attr("x1", function(d) {return d.source.x;})
                  .attr("y1", function(d) {return d.source.y;})
                  .attr("x2", function(d) {return d.target.x;})
                  .attr("y2", function(d) {return d.target.y;});
                node
                  .attr("cx", function(d) {return d.x;})
                  .attr("cy", function(d) {return d.y;});
                text
                  .attr("x", function(d) { return d.x; })
                  .attr("y", function(d) { return d.y; });
              }

              function dragstarted(d) {
                if (!d3.event.active) simulation.alphaTarget(0.3).restart();
                d.fx = d.x;
                d.fy = d.y;
                //dragging = true;
              }

              //拖动进行中
              function dragged(d) {
                d.fx = d3.event.x;
                d.fy = d3.event.y;
              }

              //拖动结束
              function dragended(d) {
                if (!d3.event.active) simulation.alphaTarget(0);
                d.fx = null;
                d.fy = null;
                //dragging = false;
              }
            }
        },
        created() {
            this.loadKGList();
        },
        mounted() {
            this.initGraph();
        }
    }
</script>

<style scoped>
  .el-main {
    position: fixed;
    top: 80px;
    left: 30px;
    right: 30px;
    bottom: 0;
    margin: 20px;
    padding: 10px;
  }

  .option-bar {
    /*text-align: center;*/
  }

  .option-block {
    display: inline-block;
    /*margin-right: 40px;*/
  }

  .option-block .el-select {
    width: 400px;
  }







  .btn-div {
    float: right;
  }

  .kg-div {
    position: fixed;
    top: 160px;
    left: 30px;
    right: 360px;
    bottom: 0;
    margin: 20px;
    border: 3px solid #272b30;
    border-radius: 8px;
    padding: 10px;
  }

  .info-div {
    position: fixed;
    top: 160px;
    right: 30px;
    /*bottom: 0;*/
    width: 300px;
    margin: 20px;
    padding-top: 10px;
  }

  .svg-div {
    width: 100%;
    height: 100%;
    /*background-color: cadetblue;*/
  }

  svg {
    background-color: #272b30;
  }
</style>
