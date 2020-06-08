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


</head>
<body>
<div id="top">
  <jsp:include page="expertTop.jsp" flush="true"/>
</div>
<div class="content-wrapper">
  <section>
    <div class="section-title">
      <span>科室导航</span>
    </div>
    <div class="section-content">
      <div class="container">
        <a class="item"><span><i class="item-icon"></i>产科</span></a>
        <a class="item"><span><i class="item-icon"></i>风湿科</span></a>
        <a class="item"><span><i class="item-icon"></i>骨科</span></a>
        <a class="item"><span><i class="item-icon"></i>老年病科</span></a>
        <a class="item"><span><i class="item-icon"></i>感染内科</span></a>
        <a class="item"><span><i class="item-icon"></i>口腔科</span></a>
        <a class="item"><span><i class="item-icon"></i>神经外科</span></a>
      </div>
      <div class="container">
        <a class="item"><span><i class="item-icon"></i>普通外科</span></a>
        <a class="item"><span><i class="item-icon"></i>肾内科</span></a>
        <a class="item"><span><i class="item-icon"></i>血液科</span></a>
        <a class="item"><span><i class="item-icon"></i>代谢科</span></a>
        <a class="item"><span><i class="item-icon"></i>神经内科</span></a>
        <a class="item"><span><i class="item-icon"></i>心血管内科</span></a>
        <a class="item"><span><i class="item-icon"></i>胸外科</span></a>
      </div>
      <div class="container">
        <a class="item"><span><i class="item-icon"></i>妇科</span></a>
        <a class="item"><span><i class="item-icon"></i>呼吸科</span></a>
        <a class="item"><span><i class="item-icon"></i>泌尿外科</span></a>
        <a class="item"><span><i class="item-icon"></i>烧伤科</span></a>
        <a class="item"><span><i class="item-icon"></i>内分泌科</span></a>
        <a class="item"><span><i class="item-icon"></i>肿瘤科</span></a>
      </div>
      <div class="container">
        <a class="item"><span><i class="item-icon"></i>消化科</span></a>
        <a class="item"><span><i class="item-icon"></i>眼科</span></a>
        <a class="item"><span><i class="item-icon"></i>儿科</span></a>
        <a class="item"><span><i class="item-icon"></i>肝胆外科</span></a>
        <a class="item"><span><i class="item-icon"></i>精神科</span></a>
        <a class="item"><span><i class="item-icon"></i>耳鼻喉</span></a>
      </div>
    </div>
  </section>
  <section>
    <div class="section-title">
      <span>疾病代码与分类</span>
    </div>
    <div class="section-content">
      <div class="container">
        <a class="item"><span><i class="item-icon"></i>产后出血</span></a>
        <a class="item"><span><i class="item-icon"></i>Rh血型不合</span></a>
        <a class="item"><span><i class="item-icon"></i>产科休克</span></a>
        <a class="item"><span><i class="item-icon"></i>产后虚脱</span></a>
      </div>
      <div class="container">
        <a class="item"><span><i class="item-icon"></i>先天性心脏病</span></a>
        <a class="item"><span><i class="item-icon"></i>小儿白血病</span></a>
        <a class="item"><span><i class="item-icon"></i>小儿便秘</span></a>
        <a class="item"><span><i class="item-icon"></i>心肌炎</span></a>
      </div>
    </div>
  </section>
  <section>
    <div class="section-title">
      <span>推荐疾病</span>
    </div>
    <div class="section-content">
      <div class="container">
        <a class="item"><span><i class="item-icon"></i>阿尔茨海默病</span></a>
        <a class="item"><span><i class="item-icon"></i>闭塞性动脉硬化</span></a>
        <a class="item"><span><i class="item-icon"></i>老年便秘</span></a>
        <a class="item"><span><i class="item-icon"></i>老年低温症</span></a>
      </div>
      <div class="container">
        <a class="item"><span><i class="item-icon"></i>久病卧床</span></a>
        <a class="item"><span><i class="item-icon"></i>静脉血栓</span></a>
        <a class="item"><span><i class="item-icon"></i>败血症</span></a>
        <a class="item"><span><i class="item-icon"></i>病毒性肝炎</span></a>
      </div>
    </div>
  </section>
</div>
<div id="footer">
  <jsp:include page="footer.jsp" flush="true"/>
</div>
</body>
</html>