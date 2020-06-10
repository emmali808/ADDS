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
<title>主页面</title>
    <link rel="stylesheet" href="<%=path%>/css/reset.css"/>
    <link rel="stylesheet" href="<%=path%>/css/style.css"/>
    <script src="https://d3js.org/d3.v4.min.js"></script>
    <script src="../js/d3-ellipse-force.js"></script>
    <script src="../js/graph.js"></script>

</head>
<body style="background-image: url(<%=path%>/images/background2.jpg) ;background-size: cover;">
<div id="top">
    <jsp:include page="patientTop.jsp" flush="true"/>
</div>
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

<div class="svg-container">
    <svg width="1100" height="560"></svg>
</div>

<footer class="footer">
    <div class="copyright">@Copyright XMU Lab</div>
</footer>

<script>
    var svg = d3.select("svg"),
        width = +svg.attr("width"),
        height = +svg.attr("height");

    var color = d3.scaleOrdinal(d3.schemeCategory20);

    var nd;
    for (var i=0; i<graph.nodes.length; i++) {
        nd = graph.nodes[i];
        nd.rx = nd.id.length * 4.5;
        nd.ry = 12;
    }

    var simulation = d3.forceSimulation()
        .force("link", d3.forceLink().id(function(d) { return d.id; }))
        .force("collide", d3.ellipseForce(6, 0.5, 5))
        .force("center", d3.forceCenter(width / 2, height / 2));

    var link = svg.append("g")
        .attr("class", "link")
        .selectAll("line")
        .data(graph.links)
        .enter().append("line")
        .attr("stroke-width", function(d) { return Math.sqrt(d.value); });

    var node = svg.append("g")
        .attr("class", "node")
        .selectAll("ellipse")
        .data(graph.nodes)
        .enter().append("ellipse")
        .attr("rx", function(d) { return d.rx; })
        .attr("ry", function(d) { return d.ry; })
        .attr("fill", function(d) { return color(d.group); })
        .call(d3.drag()
            .on("start", dragstarted)
            .on("drag", dragged)
            .on("end", dragended));

    var text = svg.append("g")
        .attr("class", "labels")
        .selectAll("text")
        .data(graph.nodes)
        .enter().append("text")
        .attr("dy", 2)
        .attr("text-anchor", "middle")
        .text(function(d) {return d.id})
        .attr("fill", "white");


    simulation
        .nodes(graph.nodes)
        .on("tick", ticked);

    simulation.force("link")
        .links(graph.links);

    function ticked() {
        link
            .attr("x1", function(d) { return d.source.x; })
            .attr("y1", function(d) { return d.source.y; })
            .attr("x2", function(d) { return d.target.x; })
            .attr("y2", function(d) { return d.target.y; });

        node
            .attr("cx", function(d) { return d.x; })
            .attr("cy", function(d) { return d.y; });
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

</script>

</body>
</html>

<style>
    .svg-container {
        margin-top: -10px;
        text-align: center;
    }
    .link line {
        stroke: #999;
        stroke-opacity: 0.6;
    }

    .labels text {
        pointer-events: none;
        font: 10px sans-serif;
    }
</style>