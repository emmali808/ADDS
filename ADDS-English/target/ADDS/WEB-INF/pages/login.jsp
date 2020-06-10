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
  <title>用户登录</title>
</head>
<body style="background-color:#8bd09f">
<div class="col-sm-12 " style="display:flex;justify-content:center;margin:40px 0 40px 0;">
	<img src="<%=path%>/images/logo.png " height="150" width="150" style="border-radius:75px;" alt="logo">
</div>
<div class="col-sm-12">
	<div class="col-sm-4"></div>
	<div class="col-sm-4" style="line-height:12px;padding:36px;border-radius:0px;box-shadow: 0px 0px 20px #655c5c;background-color:white">
    <form action="<%=basePath%>user/login" method="post" >
    <div>
        <label for="name">Username：</label><input id="name" class="login-username-input form-control" type="text" name="login_name" placeholder="Username"><br>
    </div>
    <div>
        <label for="password">Password：</label><input id="password" class="login-psw-input form-control" type="password" name="password" placeholder="Password"><br>
    </div>
    <div>
        <input class="btn btn-default btn-block green-btn" style="width:30%;float:right" type="submit" value="login">
    </div>
    </form>
	</div>
	<div class="col-sm-4"></div>
</div>
</body>
</html>