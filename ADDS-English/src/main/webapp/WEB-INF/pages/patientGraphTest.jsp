<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<%
  String path=request.getContextPath();
  String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>个性化图谱</title>
  <link rel="stylesheet" href="<%=path%>/css/reset.css"/>
  <link rel="stylesheet" href="<%=path%>/css/style.css"/>


</head>
<body>
<div id="top">
  <jsp:include page="patientTop.jsp" flush="true"/>
</div>
<div class="content-wrapper">
  <div style="display: none">
    <p id="graph">${graph}</p>
  </div>
  <div id="graphWrapper" style="display: flex;justify-content: center;flex-direction: column">
  </div>

</div>
<div style="margin-right: auto;margin-left: auto;width: 500px;text-align: center;margin-top: 20px">
  <span style="font-size: 16px">相似结果：</span>
  <ul class="pagination">
    <li><a href="#">«</a></li>
    <li><a href="#">1</a></li>
    <li><a class="active" href="#">2</a></li>
    <li><a href="#">3</a></li>
    <li><a href="#">4</a></li>
    <li><a href="#">»</a></li>
  </ul>
</div>
<footer class="footer">
  <div class="copyright">@Copyright XMU Lab</div>
</footer>

<script src="http://echarts.baidu.com/build/dist/echarts.js"></script>
<script type="text/javascript">
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
              date.innerText = "日期:"+graph[index].time;
              $(date).addClass('graph-date');
              $(targetNode).after(date);
            }
          }
  );
</script>

</body>
<style>

  ul.pagination li {display: inline;}

  .pagination {
    display: inline-block;
    padding: 0;
    margin: 0;
    width: 400px;
  }

  ul.pagination li a {
    color: black;
    float: left;
    padding: 8px 16px;
    text-decoration: none;
  }

  ul.pagination li a.active {
    background-color: #4CAF50;
    color: white;
  }

  ul.pagination li a:hover:not(.active) {background-color: #ddd;}
</style>
</html>