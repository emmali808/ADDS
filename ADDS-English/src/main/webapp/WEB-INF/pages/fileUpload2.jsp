<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
		<form action="file/threeFile" method="post"
			enctype="multipart/form-data">
			<input type="file" name="file" /><br /> <input type="file"
				name="file" /><br /> <input type="file" name="file" /><br /> <input
				type="submit" value="上 传" />
		</form>
		<h5>上传结果：</h5>

		<c:forEach items="${fileList}" var="imagename">
				<img alt="暂无图片" src="${imagename}" />	<br/>
		</c:forEach>
	</center>
</body>
</html>