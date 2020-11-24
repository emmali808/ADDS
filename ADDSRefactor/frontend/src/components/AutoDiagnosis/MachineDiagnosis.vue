<template>
    <el-main>
        <div class="patientCase">
            <h3>Select Your Case:</h3>
            <div class="option-block">
                <el-select v-model="kgChosen" placeholder="Choose a knowledge graph" @change="loadKG1">
                    <el-option v-for="kg in kgList" :key="kg.value" :label="kg.label" :value="kg.value">
                    </el-option>
                </el-select>
            </div>
            <div class="kg-div">
                <div class="svg-div" ref="svgDiv1">
                    <svg width="100%" height="100%"></svg>
                </div>
            </div>
        </div>
        <div class="similarCases" ref="similarCases">
            <h3>Similar Cases:</h3>
            <div class="kg-div">
                <div class="svg-div" ref="svgDiv2">
                    <svg width="100%" height="100%"></svg>
                </div>
            </div>
            <div class="diagnosis" style="margin-right: auto;margin-left: auto;margin-top: 10px;width: 750px;text-align: left;font-size: 16px">
                <span style="font-weight: bold">Diagnosis：</span>
                {{diagnosis}}
            </div>
            <div class="page-nav" >
                <el-pagination
                    @current-change="handleCurrentChange"
                    :page-size="1"
                    layout="prev, pager, next"
                    :total="numOfGraphs">
                </el-pagination>
            </div>
        </div>
    </el-main>

</template>

<script type="text/ecmascript-6">
    import * as d3 from 'd3';

    export default {
        name: 'machineDiagnosis',
        data() {
            return {
                kgList: [],
                kgChosen: '',
                kgData1: {
                    nodes: [],
                    links: []
                },
                kgData2: {
                    nodes: [],
                    links: []
                },
                kgSvgComponents1: {
                    svg: null,
                    simulation: null,
                    colors: [],
                    linkGroup: [],
                    linkTextGroup: [],
                    nodeGroup: [],
                    nodeTextGroup: []
                },
                kgSvgComponents2: {
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
                    id: -1,
                },
                transform: '',
                similarGraphAdmissionIds: [],
                numOfGraphs: 10,
                diagnosis: ''
            }
        },
        created() {
            this.loadKGList();
        },
        mounted() {
            this.initGraph();
        },
        watch: {
            '$route': {
              handler(route) {
                this.loadKGList();
                this.removeCurrentKgData1();
                this.removeCurrentKgData2();
                this.paintGraph1(this.kgData1, this.kgSvgComponents1);

                this.$refs.similarCases.style.display = "none";
              }
            }
        },
        methods: {
            loadKGList() {
                this.$axios({
                    method: 'get',
                    url: '/kg/user/' + this.$store.state.user.id,
                }).then(res => {
                    this.kgList.length = 0;
                    for (let kg in res.data) {
                        if (res.data.hasOwnProperty(kg)) {
                            this.kgList.push({
                                value: res.data[kg].id,
                                label: res.data[kg].name
                            });
                        }
                    }
                }).catch(error => {
                    console.log(error);
                });
            },
            loadKG1() {
                this.removeCurrentKgData1();
                this.removeCurrentKgData2();
                // get medical archive generated graph
                this.$axios({
                    method: 'get',
                    url: '/kg/graph/' + this.kgChosen,
                }).then(res => {
                    let nodes = res.data.nodes;
                    let links = res.data.links;
                    this.kgData1.nodes = nodes;
                    this.kgData1.links = links;
                    this.paintGraph1(this.kgData1, this.kgSvgComponents1);
                }).catch(error => {
                    console.log(error);
                });
                // get similar cases admission node id
                this.$axios({
                    method: 'get',
                    url: '/diagnosis/' + this.kgChosen
                }).then(res => {
                    this.similarGraphAdmissionIds = res.data;
                    this.numOfGraphs = res.data.length;
                    // display one of the similar cases
                    if (res.data.length != 0) {
                        this.$refs.similarCases.style.display = "block";
                        this.loadKG2(1);
                    } else {
                        this.$refs.similarCases.style.display = "none";
                    }
                }).catch(error => {
                    console.log(error);
                });
            },
            loadKG2(graphId) {
                this.$axios({
                    method: 'get',
                    url: '/kg/node/' + this.similarGraphAdmissionIds[graphId - 1],
                }).then(res => {
                    let nodes = res.data.nodes;
                    let links = res.data.links;
                    this.kgData2.nodes = nodes;
                    this.kgData2.links = links;
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
                    this.paintGraph2(this.kgData2, this.kgSvgComponents2);
                    var admission_id = ''
                    nodes.forEach(node => {
                        if(node.type == 'admission') {
                           admission_id = node.admission_id.substring(2, node.admission_id.length)
                        }
                    });
                    this.$axios({
                        method: 'get',
                        url: '/diagnosis/admission/' + admission_id,
                    }).then(res => {
                        if (res.data !='') {
                            this.diagnosis = res.data
                        } else {
                            this.diagnosis = 'Doctor\'s diagnosis missing'
                        }
                    }).catch(error => {
                        console.log(error)
                    })
                }).catch(error => {
                    console.log(error);
                });
            },
            initGraph() {
                let width = this.$refs.svgDiv1.offsetWidth;
                let height = this.$refs.svgDiv1.offsetHeight;
                this.kgSvgComponents1.simulation = d3.forceSimulation()
                    .force("link", d3.forceLink().id(d => {return d.id;}))
                    .force("charge", d3.forceManyBody().strength(100))
                    .force("center", d3.forceCenter(width / 2, height / 2))
                    .force("collide", d3.forceCollide(80).strength(0.2).iterations(5));
                this.kgSvgComponents2.simulation = d3.forceSimulation()
                    .force("link", d3.forceLink().id(d => {return d.id;}))
                    .force("charge", d3.forceManyBody().strength(100))
                    .force("center", d3.forceCenter(width / 2, height / 2))
                    .force("collide", d3.forceCollide(80).strength(0.2).iterations(5));
                this.kgSvgComponents1.colors = [
                    '#6ca46c', '#4e88af',
                    '#ca635f', '#d2907c',
                    '#d6744d', '#cec255',
                    '#b4c6e7', '#cdb4e6'
                ];
                this.kgSvgComponents2.colors = this.kgSvgComponents1.colors;
                this.kgSvgComponents1.svg = d3.selectAll("svg").filter(function (d, i) { return i === 0;});
                this.kgSvgComponents2.svg = d3.selectAll("svg").filter(function (d, i) { return i === 1;});
                this.kgSvgComponents1.linkGroup = this.kgSvgComponents1.svg.append("g").attr("class", "links");
                this.kgSvgComponents1.nodeGroup = this.kgSvgComponents1.svg.append("g").attr("class", "nodes");
                this.kgSvgComponents1.nodeTextGroup = this.kgSvgComponents1.svg.append("g").attr("class", "texts");
                this.kgSvgComponents2.linkGroup = this.kgSvgComponents2.svg.append("g").attr("class", "links");
                this.kgSvgComponents2.nodeGroup = this.kgSvgComponents2.svg.append("g").attr("class", "nodes");
                this.kgSvgComponents2.nodeTextGroup = this.kgSvgComponents2.svg.append("g").attr("class", "texts");
            },
            paintGraph1(kgData, kgSvgComponents) {
                let svg = kgSvgComponents.svg;
                let simulation = kgSvgComponents.simulation;
                let colors = kgSvgComponents.colors;

                svg.selectAll("*").remove();
                let centerX = this.$refs.svgDiv1.offsetWidth / 2;
                let centerY = this.$refs.svgDiv1.offsetHeight / 2;

                let link = svg.append("g").attr("class", "links")
                    .selectAll("line").data(kgData.links).enter().append("line")
                    .attr("stroke-width", 1)
                    .style("stroke", "rgba(50, 50, 50, 0.8)");

                let node = svg.append("g").attr("class", "nodes")
                    .selectAll("circle").data(kgData.nodes).enter().append("circle")
                    .attr("cx", centerX)
                    .attr("cy", centerY)
                    .attr("r", d => {return 50;})
                    .attr("fill", d => {
                        if (d.type == "patient") {
                            return colors[0]
                        } else if (d.type == "admission") {
                            return colors[1]
                        } else if (d.type == "disease") {
                            return colors[2]
                        } else if (d.type == "drug") {
                            return colors[5]
                        }
                    })
                    .style("stroke", "rgba(50, 50, 50, 0.8)")
                    .style("stroke-width", "2px")
                    .style("cursor", "pointer")
                    .call(d3.drag().on("start", dragstarted).on("drag", dragged).on("end", dragended));

                let text = svg.append("g").attr("class", "texts")
                    .selectAll("text").data(kgData.nodes).enter().append("text")
                    .text(d => {
                      if (d.type == "patient") {
                        return "patient " + d.patient_id
                      } else if (d.type == "admission") {
                        return "admission " + d.admission_id
                      } else if (d.type == "disease") {
                        return d.alias
                      } else if (d.type == "drug") {
                        return d.drug_alias
                      }
                    })
                    .attr("x", centerX)
                    .attr("y", centerY)
                    .attr("text-anchor", "middle")
                    .attr("fill", "rgba(50, 50, 50, 0.8)")
                    .style("cursor", "pointer")
                    .call(d3.drag().on("start", dragstarted).on("drag", dragged).on("end", dragended))
                    .call(this.wrap);
                
                

                var tspan = svg.selectAll("tspan");

                simulation.nodes(kgData.nodes).on("tick", ticked);
                simulation.force("link").links(kgData.links);
                simulation.alpha(0.1).restart();
                svg.call(d3.zoom().on("zoom", () => {
                    svg.selectAll("g").attr("transform", d3.event.transform);
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
                    tspan
                        .attr("x", function(d) { return d.x; })
                        .attr("y", function(d) { return d.y; });
                }
                function dragstarted(d) {
                    if (!d3.event.active) simulation.alphaTarget(0.2).restart();
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
            paintGraph2(kgData, kgSvgComponents) {
                let svg = kgSvgComponents.svg;
                let simulation = kgSvgComponents.simulation;
                let colors = kgSvgComponents.colors;

                svg.selectAll("*").remove();
                let centerX = this.$refs.svgDiv1.offsetWidth / 2;
                let centerY = this.$refs.svgDiv1.offsetHeight / 2;

                let link = svg.append("g").attr("class", "links")
                    .selectAll("line").data(kgData.links).enter().append("line")
                    .attr("stroke-width", 1)
                    .style("stroke", "rgba(50, 50, 50, 0.8)");

                let tooltip = d3.select("body").select(".tooltip")
                if (tooltip._groups[0][0] == null) {
                    tooltip = d3.select("body").append("div")
                    .attr("class", "tooltip")
                    .style("display", "none")
                    .style("text-align", "center")
                    .style("border-radius", "4px")
                    .style("padding", "10px 20px")
                    .style("width", "fit-content")
                    .style("position", "relative")                    
                    .style("white-space", "pre");
                }

                function setTooltipContent(d) {
                  let content = '';
                  if (d.type == "patient") {
                        content += "patient_id: " + d.patient_id + "\n"
                        content += "gender: " + d.gender + "\n"
                        content += "religion: " + d.religion + "\n"
                        content += "ethnicity: " + d.ethnicity + "\n"
                        content += "birth_time: " + d.birth_time + "\n"                   
                      } else if (d.type == "admission") {
                        content += "admission_id: " + d.admission_id + "\n"
                        content += "admit_time: " + d.admit_time + "\n"
                        content += "duration: " + d.duration + "\n"
                        content += "flag: " + d.flag + "\n"
                        content += "admit_age: " + d.admit_age + "\n"
                      } else if (d.type == "disease") {
                        content += "disease_id: " + d.disease_id + "\n"
                        content += "alias: " + d.alias + "\n"
                      } else if (d.type == "drug") {
                        content += "drug_id: " + d.drug_id + "\n"
                        content += "drug_alias: " + d.drug_alias + "\n"
                        content += "dose_val_rx: " + d.dose_val_rx + "\n"
                        content += "dose_unit_rx: " + d.dose_unit_rx + "\n"
                      }
                  return content;
                }

                function updateTooltip(d) {
                    tooltip.html(setTooltipContent(d))
                        .style("left", (d3.event.pageX) + "px")
                        .style("top", (d3.event.pageY - tooltip.node().getBoundingClientRect().height * 0.5) + "px");
                }

                let node = svg.append("g").attr("class", "nodes")
                    .selectAll("circle").data(kgData.nodes).enter().append("circle")
                    .attr("cx", centerX)
                    .attr("cy", centerY)
                    .attr("r", d => {return 50;})
                    .attr("fill", d => {
                        if (d.type == "patient") {
                            return colors[0]
                        } else if (d.type == "admission") {
                            return colors[1]
                        } else if (d.type == "disease") {
                            return colors[2]
                        } else if (d.type == "drug") {
                            return colors[5]
                        }
                    })
                    .style("stroke", "rgba(50, 50, 50, 0.8)")
                    .style("stroke-width", "2px")
                    .style("cursor", "pointer")
                    .call(d3.drag().on("start", dragstarted).on("drag", dragged).on("end", dragended))
                    .on('mouseover', function (d, i) {
                        tooltip.style("display", "block")
                        .style("color", "rgb(20, 20, 20)")
                        .style("background-color", "rgba(240, 240, 240, 0.95)")
                        .style("border", "1px solid rgba(240, 240, 240, 0.95)")
                        .style("box-shadow", "2px 2px 2px rgba(80, 80, 80, 0.6)");;
                        updateTooltip(d);
                    })
                    .on('mousemove', function (d, i) {
                        updateTooltip(d);
                    })
                    .on('mouseout', function (d, i) {
                        tooltip.style("display", "none");
                    });

                node.on("click", d => {
                    this.expand(d.id);
                });

                let text = svg.append("g").attr("class", "texts")
                    .selectAll("text").data(kgData.nodes).enter().append("text")
                    .text(d => {
                      if (d.type == "patient") {
                        return "patient " + d.patient_id
                      } else if (d.type == "admission") {
                        return "admission " + d.admission_id
                      } else if (d.type == "disease") {
                        return d.alias
                      } else if (d.type == "drug") {
                        return d.drug_alias
                      }
                    })
                    .attr("x", centerX)
                    .attr("y", centerY)
                    .attr("text-anchor", "middle")
                    .attr("fill", "rgba(50, 50, 50, 0.8)")
                    .style("cursor", "pointer")
                    .call(d3.drag().on("start", dragstarted).on("drag", dragged).on("end", dragended))
                    .on('mouseover', function (d, i) {
                        tooltip.style("display", "block")
                        .style("color", "rgb(20, 20, 20)")
                        .style("background-color", "rgba(240, 240, 240, 0.95)")
                        .style("border", "1px solid rgba(240, 240, 240, 0.95)")
                        .style("box-shadow", "2px 2px 2px rgba(80, 80, 80, 0.6)");;
                        updateTooltip(d);
                    })
                    .on('mousemove', function (d, i) {
                        updateTooltip(d);
                    })
                    .on('mouseout', function (d, i) {
                        tooltip.style("display", "none");
                    })
                    .call(this.wrap);
                
                text.on("click", d => {
                    this.expand(d.id);
                });

                var tspan = svg.selectAll("tspan");

                svg.selectAll("g").attr("transform", this.transform);

                simulation.nodes(kgData.nodes).on("tick", ticked);
                simulation.force("link").links(kgData.links);
                simulation.alpha(0.1).restart();
                svg.call(d3.zoom().on("zoom", () => {
                    svg.selectAll("g").attr("transform", d3.event.transform);
                    this.transform = d3.event.transform;
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
                    tspan
                        .attr("x", function(d) { return d.x; })
                        .attr("y", function(d) { return d.y; });
                }
                function dragstarted(d) {
                    if (!d3.event.active) simulation.alphaTarget(0.2).restart();
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
            wrap(text) {
                text.each(function() {
                    var text = d3.select(this);
                    var words = text.text().split(/\s+|\*|-/).reverse();
                    var word;
                    var x = text.attr("x");
                    var y = text.attr("y");
                    var dy = 5;
                    var lines = [];
                    var line = [];
                    var lineNumber = 0;
                    var lineHeight = 18;

                    while (word = words.pop()) {
                        line.push(word);
                        if (line.join(" ").length > 9) {
                            if (line.length == 1) {
                                lines.push(line.join(" "));
                                line.pop();
                            } else {
                                line.pop();
                                lines.push(line.join(" "));
                                line = [word];
                            }
                        }
                    }
                    if (line.length != 0) {
                        lines.push(line.join(" "));
                    }

                    text.text(null)
                    dy = dy + lineHeight * (1 - lines.length) * 0.5 // calculate actual offset of y
                    for (let index = 0; index < lines.length; index++) {
                        const element = lines[index];
                        text.append("tspan")
                            .attr("x", x)
                            .attr("y", y)
                            .attr("dy", lineNumber++ * lineHeight + dy)
                            .text(element);
                    }
                })
            },
            expand(nodeId) {
                this.kgEntityInfo.id = nodeId;
                let childNode = this.kgNodeBufferedMap[nodeId].childNode;
                if (childNode != null) {
                  if (childNode.length === 0) {
                    this.getRelNodes(nodeId);
                  } else {
                    if (this.kgNodeUnfoldFlag[this.kgEntityInfo.id]) {
                        this.unfoldRelNodes()
                    }
                  }
                }
            },
            getRelNodes(nodeId) {
                if (this.kgNodeBufferedMap[nodeId].childNode.length === 0) {
                    this.$axios({
                        method: 'get',
                        url: '/kg/graph/relNodes/' + nodeId,
                    }).then(res => {
                        let nodes = res.data.nodes;
                        let links = res.data.links;
                        this.kgNodeBufferedMap[nodeId].childNode = nodes;
                        this.kgNodeBufferedMap[nodeId].relations = links;
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
                        if (this.kgNodeUnfoldFlag[this.kgEntityInfo.id]) {
                            this.unfoldRelNodes()
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
                            this.kgData2.nodes.push(n);
                            this.kgNodeBufferedMap[n.id].loadedInGraph = true;
                        }
                    }
                }
                let links = this.kgData2.links.concat(this.kgNodeBufferedMap[this.kgEntityInfo.id].relations);
                let linkSet = new Set(links);
                this.kgData2.links = Array.from(linkSet);
                this.paintGraph2(this.kgData2, this.kgSvgComponents2);
                this.kgNodeUnfoldFlag[this.kgEntityInfo.id] = false;
            },
            removeCurrentKgData1() {
                this.kgData1.nodes.length = 0;
                this.kgData1.links.length = 0;
            },
            removeCurrentKgData2() {
                this.kgNodeBufferedMap.length = 0;
                this.kgNodeUnfoldFlag.length = 0;
                this.kgData2.nodes.length = 0;
                this.kgData2.links.length = 0;
                this.kgEntityInfo.id = -1;
            },
            handleCurrentChange(val) {
                this.loadKG2(val);
            }
        }

    }

</script>

<style scoped>
    .el-main {
        position: fixed;
        top: 100px;
        left: 300px;
        right: 30px;
        bottom: 0;
        padding: 0;
    }

    .option-block {
        display: inline-block;
    }

    .option-block .el-select {
        width: 300px;
    }

    .diagnosis{
        margin: 10px;
    }

    .page-nav{
        text-align: center;
        margin-top: 20px;
        padding-bottom: 100px;
    }

    .kg-div {
        position: relative;
        height: 500px;
        margin: 20px 0;
        margin-right: 50px;
        border: 2px solid #272b30;
        border-radius: 8px;
    }

    .svg-div {
        width: 100%;
        height: 100%;
    }

    svg {
        border-radius: 5px;
    }

    .similarCases {
        display: none;
    }
</style>
