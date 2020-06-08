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
            <li><a  href="<%=basePath%>user/allUser">所有用户</a></li>
            <li><a  href="<%=basePath%>user/allpatients">患者</a></li>
            <li><a  href="<%=basePath%>user/alldoctors">&nbsp;医生  </a></li>
            <li><a  href="<%=basePath%>user/allexperts">&nbsp;专家  </a></li>
            <li><a  href="<%=basePath%>question/allQuestion">&nbsp;所有问题  </a></li>
            <li><a  href="<%=basePath%>user/account">&nbsp;个人信息</a></li>
            <li><a  href="<%=basePath%>hospitalDepartment/manage">&nbsp;科室管理</a></li>
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