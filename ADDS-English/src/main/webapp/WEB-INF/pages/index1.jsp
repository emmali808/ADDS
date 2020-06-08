<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<%
    String path=request.getContextPath();
    String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title>疾病-临床诊疗知识库</title>
    <link rel="stylesheet" href="<%=path%>/css/reset.css"/>
    <link rel="stylesheet" href="<%=path%>/css/style.css"/>
    <script src="https://d3js.org/d3.v4.min.js"></script>
    <script src="../../js/d3-ellipse-force.js"></script>
    <%--<script src="./js/graph.js"></script>--%>
    <script src="https://apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js"></script>
    <script src="http://echarts.baidu.com/build/dist/echarts-all.js"></script>
</head>
<body  style="background-image: url(<%=path%>/images/background2.jpg) ;background-size: cover;">
<!-- <div class="bg" ;width:100%;heigt:100%;position:absolute"> -->

<!-- </div> -->
<nav class="nav">
    <div class="header clearfix" style="display:none">
        <div class="logo">
            <img src="<%=path%>/images/logo2.jpg">
        </div>
        <div class="user-info">
        </div>
    </div>
    <div class="nav-wrapper clearfix">
        <div style="width: 1080px;margin: 0 auto;display:flex;justify-content:space-between">
            <div class="n-list" >
                <ul>
                    <%--<li>全部</li>--%>
                    <li>Disease</li>
                    <li>Symptom</li>
                </ul>
            </div>
            <div class="n-search" style="width:40%">
                <input type="text" style="width: 100%">
                <i class="iconfont">&#xe600</i>
            </div>
            <div class="n-list">
                <ul>
                    <li> <a href="<%=basePath%>user/userLogin">Login</a></li>
                    <li style="width:auto"> <a href="<%=basePath%>user/toAddUser">Register</a></li>
                </ul>
            </div>
        </div>
    </div>
</nav>
<div class="nav-padding"></div>
<%--<div class="content-wrapper">--%>
<%--<section>--%>
<%--<div class="section-title">--%>
<%--<span>科室导航</span>--%>
<%--</div>--%>
<%--<div class="section-content">--%>
<%--<div class="container">--%>
<%--<a class="item"><span><i class="item-icon"></i>产科</span></a>--%>
<%--<a class="item"><span><i class="item-icon"></i>风湿科</span></a>--%>
<%--<a class="item"><span><i class="item-icon"></i>骨科</span></a>--%>
<%--<a class="item"><span><i class="item-icon"></i>老年病科</span></a>--%>
<%--<a class="item"><span><i class="item-icon"></i>感染内科</span></a>--%>
<%--<a class="item"><span><i class="item-icon"></i>口腔科</span></a>--%>
<%--<a class="item"><span><i class="item-icon"></i>神经外科</span></a>--%>
<%--</div>--%>
<%--<div class="container">--%>
<%--<a class="item"><span><i class="item-icon"></i>普通外科</span></a>--%>
<%--<a class="item"><span><i class="item-icon"></i>肾内科</span></a>--%>
<%--<a class="item"><span><i class="item-icon"></i>血液科</span></a>--%>
<%--<a class="item"><span><i class="item-icon"></i>代谢科</span></a>--%>
<%--<a class="item"><span><i class="item-icon"></i>神经内科</span></a>--%>
<%--<a class="item"><span><i class="item-icon"></i>心血管内科</span></a>--%>
<%--<a class="item"><span><i class="item-icon"></i>胸外科</span></a>--%>
<%--</div>--%>
<%--<div class="container">--%>
<%--<a class="item"><span><i class="item-icon"></i>妇科</span></a>--%>
<%--<a class="item"><span><i class="item-icon"></i>呼吸科</span></a>--%>
<%--<a class="item"><span><i class="item-icon"></i>泌尿外科</span></a>--%>
<%--<a class="item"><span><i class="item-icon"></i>烧伤科</span></a>--%>
<%--<a class="item"><span><i class="item-icon"></i>内分泌科</span></a>--%>
<%--<a class="item"><span><i class="item-icon"></i>肿瘤科</span></a>--%>
<%--</div>--%>
<%--<div class="container">--%>
<%--<a class="item"><span><i class="item-icon"></i>消化科</span></a>--%>
<%--<a class="item"><span><i class="item-icon"></i>眼科</span></a>--%>
<%--<a class="item"><span><i class="item-icon"></i>儿科</span></a>--%>
<%--<a class="item"><span><i class="item-icon"></i>肝胆外科</span></a>--%>
<%--<a class="item"><span><i class="item-icon"></i>精神科</span></a>--%>
<%--<a class="item"><span><i class="item-icon"></i>耳鼻喉</span></a>--%>
<%--</div>--%>
<%--</div>--%>
<%--</section>--%>
<%--<section>--%>
<%--<div class="section-title">--%>
<%--<span>疾病代码与分类</span>--%>
<%--</div>--%>
<%--<div class="section-content">--%>
<%--<div class="container">--%>
<%--<a class="item"><span><i class="item-icon"></i>产后出血</span></a>--%>
<%--<a class="item"><span><i class="item-icon"></i>Rh血型不合</span></a>--%>
<%--<a class="item"><span><i class="item-icon"></i>产科休克</span></a>--%>
<%--<a class="item"><span><i class="item-icon"></i>产后虚脱</span></a>--%>
<%--</div>--%>
<%--<div class="container">--%>
<%--<a class="item"><span><i class="item-icon"></i>先天性心脏病</span></a>--%>
<%--<a class="item"><span><i class="item-icon"></i>小儿白血病</span></a>--%>
<%--<a class="item"><span><i class="item-icon"></i>小儿便秘</span></a>--%>
<%--<a class="item"><span><i class="item-icon"></i>心肌炎</span></a>--%>
<%--</div>--%>
<%--</div>--%>
<%--</section>--%>
<%--<section>--%>
<%--<div class="section-title">--%>
<%--<span>推荐疾病</span>--%>
<%--</div>--%>
<%--<div class="section-content">--%>
<%--<div class="container">--%>
<%--<a class="item"><span><i class="item-icon"></i>阿尔茨海默病</span></a>--%>
<%--<a class="item"><span><i class="item-icon"></i>闭塞性动脉硬化</span></a>--%>
<%--<a class="item"><span><i class="item-icon"></i>老年便秘</span></a>--%>
<%--<a class="item"><span><i class="item-icon"></i>老年低温症</span></a>--%>
<%--</div>--%>
<%--<div class="container">--%>
<%--<a class="item"><span><i class="item-icon"></i>久病卧床</span></a>--%>
<%--<a class="item"><span><i class="item-icon"></i>静脉血栓</span></a>--%>
<%--<a class="item"><span><i class="item-icon"></i>败血症</span></a>--%>
<%--<a class="item"><span><i class="item-icon"></i>病毒性肝炎</span></a>--%>
<%--</div>--%>
<%--</div>--%>
<%--</section>--%>
<%--</div>--%>
<%--<footer class="footer">--%>
<%--<div class="copyright">@Copyright XMU Lab1</div>--%>
<%--</footer>--%>
<div class="graph-search">
    <div class="graph-title">Searching in Knowledge Graph</div>
    <div class="search-container">
        <span style="font-size: 18px">Entities：</span>
        <select class="selectEntity" onblur="this.size=0" onchange="this.size=0"></select>
        <button id="searchButton">Search</button>
    </div>
    <div id="graph-main" style="height: 430px">

    </div>
    <div id="modal-overlay">
        <div class="modal-data">loading...</div>
    </div>
</div>


<script>

    renderChart();
    var select = $('.selectEntity')[0];
    var umlCategories = ['equality','inverse_is_a','is_a','related'];
    $.ajax({
        url:"<%=basePath%>graph/getAll",
        success:function(response){
            response.forEach(function(value){
                var option = $("<option value='"+value.id+"'"+">"+value.name+"</option>");
                $(select).append(option);
            });
        }
    });
    getGraphAndRender(113);
    $("#searchButton").on('click', function() {
        var index = $('select option:selected').val();
        $('#modal-overlay').show();
        getGraphAndRender(index);
    });

    function getGraphAndRender(id){
        $.ajax({
            url:"<%=basePath%>graph/getUmlEntityDeeply?id="+id+"&deep=2",
            success:function(response){
                var points = response.points;
                var lines = response.lines;
                points.forEach(function (point,index) {
                    //point.category = 1;
                    //point.category = point.id;
                    point.category = index;
                });
                lines.forEach(function (line,index) {
                    line.source = searchPointName(points,line.start);
                    line.target = searchPointName(points,line.end);
                    line.weight = 1;
                    line.name = line.type;
                });
                renderChart(points,lines);
                $('#modal-overlay').hide();
            }
        });
    }
    function searchPointName(points,id) {
        for(var i=0; i<points.length; i++){
            if(points[i].id == id){
                return points[i].name;
            }
        }
        return -1;
    }
    function renderChart(points, lines) {
        var myChart = echarts.init(document.getElementById('graph-main'));
        var option = {
            title : {
                text: 'Result   ',
                //subtext: '数据来自人立方',
                x:'right',
                y:'bottom'
            },
            tooltip : {
                trigger: 'item',
                formatter: '{a} : {b}'
            },
            toolbox: {
                show : true,
                feature : {
                    restore : {show: true},
                    magicType: {show: true, type: ['force', 'chord']},
                    saveAsImage : {show: true}
                }
            },
            series : [
                {
                    type:'force',
                    name : "Relation",
                    ribbonType: false,
                    categories : [
                        {
                            name: 'equality'
                        },
                        {
                            name: 'inverse_is_a'
                        },
                        {
                            name: 'is_a'
                        },
                        {
                            name: 'related'
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
                            },
                            linkStyle: {
                                type: 'curve'
                            }
                        },
                        emphasis: {
                            label: {
                                show: false
                                // textStyle: null      // 默认使用全局文本样式，详见TEXTSTYLE
                            },
                            nodeStyle : {
                                //r: 30
                            },
                            linkStyle : {}
                        }
                    },
                    useWorker: false,
                    minRadius : 15,
                    maxRadius : 25,
                    gravity: 1.1,
                    scaling: 1.1,
                    nodes: points,
                    links: lines
                }
            ]
        };
        myChart.setOption(option);
    }


</script>
</body>
<style>
    .graph-search{
        position: relative;
        width: 90%;
        margin: 20px auto 0 auto;
        padding: 20px 0 0 0;
        border: 1px solid #409EFF;
        text-align: center;
        overflow: hidden;
        background-color: rgba(255,255,255,0.8);
    }
    .graph-title{
        font-size: 28px;
    }
    .search-container{
        margin-top: 30px;
        float: right;
    }
    .selectEntity {
        font-size: 15px;
        background-color: #fff;
        border-radius: 4px;
        border: 1px solid #dcdfe6;
        box-sizing: border-box;
        color: #606266;
        display: inline-block;
        height: 40px;
        line-height: 40px;
        outline: none;
        padding: 0 15px;
        margin-right: 10px;
    }
    #searchButton{
        color: #fff;
        background-color: #409eff;
        border-color: #409eff;
        display: inline-block;
        line-height: 1;
        white-space: nowrap;
        cursor: pointer;
        text-align: center;
        box-sizing: border-box;
        outline: none;
        font-weight: 500;
        padding: 12px 20px;
        font-size: 14px;
        border-radius: 4px;
    }
    #graph-main{
        margin: 100px auto 0 auto;
    }
    #modal-overlay {
        position: absolute;   /* 使用绝对定位或固定定位  */
        left: 0;
        top: 0;
        width:100%;
        height:100%;
        text-align: center;
        z-index: 1000;
        background-color: rgba(255,255,255,0.8);
        opacity: 0.8;   /* 背景半透明 */
    }
    .modal-data{
        width: 300px;
        margin: 300px auto;
        background-color: #fff;
        padding:15px;
        text-align:center;
    }
</style>
</html>