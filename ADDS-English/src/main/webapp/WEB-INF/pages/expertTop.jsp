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
  <link rel="stylesheet" href="<%=path%>/css/reset.css"/>
  <link rel="stylesheet" href="<%=path%>/css/style.css"/>
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
          <li><a href="<%=basePath%>user/index">首页</a></li>
          <li><a  href="<%=basePath%>expert/answerQuestion">&nbsp;回答问题  </a></li>
          <li><a  href="<%=basePath%>expert/historyAnswerQuestion">&nbsp;历史答题  </a></li>
          <li><a  href="<%=basePath%>expert/talk">&nbsp;在线交流 </a></li>
          <li><a  href="<%=basePath%>expert/account">&nbsp;个人信息</a></li>
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
