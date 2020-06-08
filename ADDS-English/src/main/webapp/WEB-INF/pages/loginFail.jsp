<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<%
  String path=request.getContextPath();
  String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
  <link href="<%=path%>/css/bootstrap.min.css" rel="stylesheet" media="screen">
    <link href="<%=path%>/css/style.css" rel="stylesheet" media="screen">
  <script type="text/javascript" src="<%=path%>/js/jquery.min.js"></script>
  <title>Login failed</title>
</head>
<body style="background-color:#8bd09f">
<div class="col-sm-12 " style="display:flex;justify-content:center;margin:40px 0 40px 0;">
	<img src="<%=path%>/images/logo.png " height="150" width="150" style="border-radius:75px;" alt="logo">
</div>
<div class="col-sm-12">
	<div class="col-sm-4"></div>
	<div class="col-sm-4" style="line-height:12px;padding:36px;border-radius:0px;box-shadow: 0px 0px 20px #655c5c;background-color:white">
        <p>The account or password is incorrect, or the account is not activated !</p>
        <p style="margin-top: 20px"><a href="<%=basePath%>user/userLogin">Re-login</a></p>
	</div>
	<div class="col-sm-4"></div>
</div>
</body>
</html>
