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
  <title>top</title>
  <link rel="stylesheet" href="<%=path%>/css/reset.css"/>
  <link rel="stylesheet" href="<%=path%>/css/style.css"/>
  <script type="text/javascript" src="<%=path%>/js/jquery.min.js"></script>
</head>
<body>

<%--  <div class="header clearfix" style="display:none">
    <div class="logo">
      <img src="<%=path%>/images/logo2.png">
    </div> --%>

    <div class="user-info">
      <span>Hello，${user.username}</span>
      <a href="javascript:void(0)" onclick=" this.href='<%=basePath%>user/goToHome'">Logout</a>
    </div>   
 
</body>
</html>