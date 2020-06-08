<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%
    String path=request.getContextPath();
    String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
   <link rel="stylesheet" href="https://unpkg.com/element-ui/lib/theme-chalk/index.css">
   <title>上传病例</title>
    <link rel="stylesheet" href="<%=path%>/css/reset.css"/>
    <link rel="stylesheet" href="<%=path%>/css/style.css"/>
</head>
<body>
<div id="top">
    <jsp:include page="patientTop.jsp" flush="true"/>
</div>
<div class="content-wrapper">
  <section class="form-wrapper">
    <div class="section-title">
      <%--<span>自动诊断</span>--%>
      <span>Automatic diagnosis</span>
    </div>
    <div class="form-content" id="app">
        <!--表格数据-->
        <div style="margin-top: 20px">
            <el-tabs v-model="activeName2" type="card" stretch @tab-click="handleClick">
                <%--<el-tab-pane label="上传病例" name="first"></el-tab-pane>--%>
                <%--<el-tab-pane label="机器诊断" name="second"></el-tab-pane>--%>
                <%--<el-tab-pane label="提交问题" name="third"></el-tab-pane>--%>
                <el-tab-pane label="Upload medical records" name="first"></el-tab-pane>
                <el-tab-pane label="Machine diagnosis" name="second"></el-tab-pane>
                <el-tab-pane label="Submit questions" name="third"></el-tab-pane>
            </el-tabs>
        </div>
        <div style="padding: 20px">
            <div style="display: none">
                <p id="graph">${graph}</p>
            </div>
            <div id="graphWrapper" style="display: flex;justify-content: center;flex-direction: column">
            </div>
            <div style="margin-right: auto;margin-left: auto;width: 750px;text-align: left;font-size: 16px">
                <span style="font-weight: bold">Diagnosis：</span>
                <%--患者有流鼻涕现象，可能患上流感。后脑勺有几个不突起的小红点。经测体温为37.8度，心电图结果为窦性心律，T波改变。--%>
                <%--建议服用乙酰吉他霉素颗粒，一日二次，一次一袋。小儿清热宁颗粒，一日三次，每次半袋。--%>
                The patient has a runny nose and may have influenza. There are several small red dots that do not protrude in the back of the head. The measured body temperature was 37.8 degrees, and the electrocardiogram results in sinus rhythm and T wave changes. It is recommended to take acetyl guitarmycin granules twice a day, one bag at a time. Pediatric heat-clearing granules, three times a day, half a bag each time.
            </div>
        </div>
        <div style="margin-right: auto;margin-left: auto;width: 500px;text-align: center;margin-top: 10px">
            <%--<span style="font-size: 16px">相似结果：</span>--%>
            <span style="font-size: 16px">Results：</span>
            <ul class="result-pagination">
                <li><a href="#">«</a></li>
                <li><a href="#">1</a></li>
                <li><a class="active" href="#">2</a></li>
                <li><a href="#">3</a></li>
                <li><a href="#">4</a></li>
                <li><a href="#">»</a></li>
            </ul>
        </div>

    </div>
   </section>
 </div>
<div id="footer">
  <jsp:include page="footer.jsp" flush="true"/>
</div>
</body>

<!-- import Vue before Element -->
<script src="https://unpkg.com/vue/dist/vue.js"></script>
<!-- import JavaScript -->
<script src="https://unpkg.com/element-ui/lib/index.js"></script>
<script src="http://echarts.baidu.com/build/dist/echarts.js"></script>
<script type="text/javascript">
    new Vue({
        el: '#app',
        methods:{
            handleClick(tab,event){
                if(tab.name=="first"){
                    window.location.href="<%=basePath%>patient/uploadCase";
                }
                if(tab.name=="third"){
                    window.location.href="<%=basePath%>patient/uploadQuestion";
                }
            }
        },
        data: function() {
            return {
                visible: false,
                activeName2: 'second'
            }
        },
        created(){
            var graph = $('#graph').text();
            graph = JSON.parse(graph);
            console.log(graph);
            require.config({
                paths: {
                    echarts: 'http://echarts.baidu.com/build/dist'
                }
            });
            require(
                [
                    'echarts',
                    'echarts/chart/force'
                ],
                function (ec) {
                    for(var index = 0;index < graph.length;index ++) {
                        var targetNode = document.createElement('div');
                        $(targetNode).addClass('graph');
                        $('#graphWrapper').append(targetNode);
                        var chart = ec.init(targetNode);
                        graph[index].nodes.forEach(function(node) {
                            node.draggable = true;
                        });
                        var option = {
                            tooltip : {
                                trigger: 'item',
                                formatter: '{b}'
                            },
                            legend: {
                                x: 'left',
                                data:['DisorderOrSyndrome','Finding','symptom','LabTest','medication','value']
                            },
                            series : [
                                {
                                    type:'force',
                                    name : "",
                                    ribbonType: false,
                                    categories : [
                                        {
                                            name: 'DisorderOrSyndrome'
                                        },
                                        {
                                            name: 'Finding'
                                        },
                                        {
                                            name:'symptom'
                                        },
                                        {
                                            name: 'LabTest'
                                        },
                                        {
                                            name: 'medication'
                                        },
                                        {
                                            name: 'value'
                                        }
                                    ],
                                    itemStyle: {
                                        normal: {
                                            label: {
                                                show: true,
                                                textStyle: {
                                                    color: '#333'
                                                }
                                            },
                                            nodeStyle : {
                                                brushType : 'both',
                                                borderColor : 'rgba(255,215,0,0.4)',
                                                borderWidth : 1
                                            }
                                        },
                                        emphasis: {
                                            label: {
                                                show: false
                                            },
                                            nodeStyle : {
                                                //r: 30
                                            },
                                            linkStyle : {}
                                        }
                                    },
                                    minRadius : 15,
                                    maxRadius : 25,
                                    gravity: 1.1,
                                    scaling: 1.2,
                                    draggable: false,
                                    linkSymbol: 'arrow',
                                    steps: 10,
                                    coolDown: 0.9,
                                    //preventOverlap: true,
                                    nodes:graph[index].nodes,
                                    links : graph[index].links
                                }
                            ]
                        };
                        chart.setOption(option);
                        var date = document.createElement('h2');
                        date.innerText = "Date："+graph[index].time;
                        $(date).addClass('graph-date');
                        $(targetNode).after(date);
                    }
                }
            );
        }
    });

</script>

<style>
    .result-pagination {
        display: inline-block;
        padding: 0;
        margin: 0;
        width: 400px;
    }

    ul.result-pagination li a {
        color: black;
        float: left;
        padding: 8px 16px;
        text-decoration: none;
    }

    ul.result-pagination li a.active {
        background-color: #4CAF50;
        color: white;
    }

    ul.result-pagination li a:hover:not(.active) {background-color: #ddd;}
</style>
</html>

