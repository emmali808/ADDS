<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
  String path=request.getContextPath();
  String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>用户信息</title>
  <link rel="stylesheet" href="<%=path%>/css/reset.css"/>
  <link rel="stylesheet" href="<%=path%>/css/style.css"/>
  <link rel="stylesheet" href="<%=path%>/css/admin.css"/>
</head>
<body>
<div id="top">
  <jsp:include page="adminTop.jsp" flush="true"/>
</div>

<div class="content-wrapper">
  <section class="form-wrapper">
    <div class="section-title">
      <span>所有用户</span>
    </div>
    <div class="form-content">
        <table class="real-table" style="width: 100%" >
            <tr class="t-title">
                <td>用户名</td>
                <td>年龄</td>
                <td>性别</td>
                <td>电话</td>
                <td>邮箱</td>
                <td>身份证号</td>
                <td>类型</td>
            </tr>
        <c:forEach items="${userList}" var="user1">
          <tr>
            <td>${user1.username }</td>
            <td>${user1.age }</td>
            <td>${user1.gender }</td>
            <td>${user1.phone }</td>
            <td>${user1.email }</td>
            <td>${user1.identityID }</td>
            <td>
            <c:choose>
             <c:when test="${user1.type==0 }">
                                                           管理员
             </c:when>
             <c:when test="${user1.type==1 }">
                                                           医生
             </c:when>
             <c:when test="${user1.type==2 }">
                                                           病人
             </c:when>
             <c:when test="${user1.type==3}">
                                                           专家
             </c:when>
            </c:choose>
            </td>
          </tr>
        </c:forEach>
      </table>
        <div class="pagination-wrapper" >
            <span class="total-text">共${totalpage}页</span>
            <a class="pagination" href="<%=basePath%>user/allUser?curpage=${curpage-1}" >
                <span class="front-page"></span>
            </a>
            <span class="pagination current-page">${curpage}</span>
            <a class= "pagination" href="<%=basePath%>user/allUser?curpage=${curpage+1}" >
                <span class="next-page"></span>
            </a>
        </div>
    </div>
  </section>

</div>
<div id="footer">
  <jsp:include page="footer.jsp" flush="true"/>
</div>
</body>
</html>