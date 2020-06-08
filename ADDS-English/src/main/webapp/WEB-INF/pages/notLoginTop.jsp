<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
  String path=request.getContextPath();
  String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>管理员头部页面</title>
<link rel="stylesheet" href="<%=path%>/css/reset.css"/>
  <link rel="stylesheet" href="<%=path%>/css/style.css"/>
  <script type="text/javascript" src="<%=path%>/js/jquery.min.js"></script>
</head>
<body>
  <nav class="nav">
    <div class="header clearfix">
        <div class="logo">
            <img src="<%=path%>/images/logo2.jpg">
        </div>
        <div class="user-info">
            <a href="<%=basePath%>user/userLogin">login</a>
            <a href="<%=basePath%>user/toAddUser">register</a>
        </div>
    </div>
    <div class="nav-wrapper clearfix">
        <div style="width: 1080px;margin: 0 auto">
            <div class="n-list">
                <ul>
                    <li>全部</li>
                    <li>疾病</li>
                    <li>症状</li>
                </ul>
            </div>
            <div class="n-search">
                <input type="text">
                <i class="iconfont">&#xe600</i>
            </div>
        </div>
    </div>
</nav>
</body>
</html>