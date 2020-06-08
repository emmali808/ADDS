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
  <title>head</title>
</head>
<body>
<nav class="nav">
  <div>
      <jsp:include page="top.jsp" flush="true"/>
  </div>
  <div class="nav-wrapper clearfix">
    <div style="width: 1080px;margin: 0 auto">
      <div class="n-list">
        <ul>
          <%--<li><a href="<%=basePath%>user/index">首页</a></li>--%>
          <%--<li><a  href="<%=basePath%>user/hospitalOverview">医院概况</a></li>--%>
         <%--&lt;%&ndash; <li><a  href="<%=basePath%>record/clinicRecord4Doctor">出诊记录</a></li>--%>
          <%--<li style="width: 120px"><a  href="<%=basePath%>record/hospitalRecord4Doctor">病人住院记录</a></li>&ndash;%&gt;--%>
          <%--<li><a  href="<%=basePath%>doctor/allPatients4doctor">我的患者</a></li>--%>
          <%--<li style="color: black"><a style="color: black" href="<%=basePath%>doctor/QA">问答平台</a></li>--%>
          <%--&lt;%&ndash;<li><a  href="<%=basePath%>doctor/QA">问答平台  </a></li>&ndash;%&gt;--%>
          <%--<li><a  href="<%=basePath%>doctor/account">知识图谱</a></li>--%>
          <%--<li style="color: black"><a style="color: black" href="<%=basePath%>doctor/documentList">文档处理</a></li>--%>
          <%--<li><a href="<%=basePath%>doctor/account">个人信息</a></li>--%>

            <li style="font-size: 13px;"><a href="<%=basePath%>user/index">Home</a></li>
            <%--<li><a  href="<%=basePath%>user/hospitalOverview">医院概况</a></li>--%>
            <%--<li style="font-size: 12px;line-height: 20px"><a  href="<%=basePath%>user/hospitalOverview">Hospital overview</a></li>--%>
            <%-- <li><a  href="<%=basePath%>record/clinicRecord4Doctor">出诊记录</a></li>
             <li style="width: 120px"><a  href="<%=basePath%>record/hospitalRecord4Doctor">病人住院记录</a></li>--%>
            <li style="font-size: 13px;"><a href="<%=basePath%>doctor/allPatients4doctor">Patients</a></li>
            <li style="font-size: 13px;width:120px;"><a href="<%=basePath%>doctor/QA">Expert Q&A</a></li>
            <%--<li><a  href="<%=basePath%>doctor/QA">问答平台  </a></li>--%>
            <%--<li style="font-size: 13px;line-height: 20px;width:120px;"><a href="<%=basePath%>doctor/account">Knowledge<br>graph</a></li>--%>
            <li style="color: black;font-size: 13px;line-height: 20px;width:120px;"><a href="<%=basePath%>doctor/documentList">Medical archives processing</a></li>
            <%--<li style="font-size: 12px;line-height: 20px;margin-left: 20px"><a href="<%=basePath%>doctor/account">Personal information</a></li>--%>
        </ul>
      </div>
      <div class="n-search">
        <input type="text">
        <i class="iconfont">&#xe600</i>
      </div>
    </div>
  </div>
</nav>
<div class="nav-padding"></div>
</body>
</html>
