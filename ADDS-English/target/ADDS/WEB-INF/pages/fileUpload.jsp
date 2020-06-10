<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户上传图片页面</title>
<base href="<%=basePath%>">
</head>
<body>
  <center>
		<form action="file/onefile2"
			method="post" enctype="multipart/form-data">
			<input type="file" name="file" /> 
			<input type="submit" value="上 传" />
		</form>
		<h5>上传结果：</h5>
		<img alt="暂无图片" src="${fileUrl}" />
	</center>
</body>
</html>