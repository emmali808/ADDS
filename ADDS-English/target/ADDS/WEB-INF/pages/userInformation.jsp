<%@ page import="cn.medicine.pojo.User" %>
<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
  String path=request.getContextPath();
  String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    User user=(User)request.getSession().getAttribute("user");
    String gender="";
    if(user.getGender().matches("m")){
        gender="男";
    }else{
        gender="女";
    }
%>
<!DOCTYPE html>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>用户信息</title>
</head>
<body>
<div id="top">
      <c:choose>
          <c:when test="${usertype==0}">
              <jsp:include page="adminTop.jsp" flush="true"/>
          </c:when>
          <c:when test="${usertype==1}">
              <jsp:include page="doctorTop.jsp" flush="true"/>
          </c:when>
          <c:when test="${usertype==2}">
              <jsp:include page="patientTop.jsp" flush="true"/>
          </c:when>
          <c:when test="${usertype==3}">
              <jsp:include page="expertTop.jsp" flush="true"/>
          </c:when>         
          <c:otherwise>
              <jsp:include page="notLoginTop.jsp" flush="true"/>
          </c:otherwise>
      </c:choose>
  </div>
<div class="content-wrapper">
  <section class="form-wrapper">
    <div class="section-title">
      <span>我的信息</span>
    </div>
    <div class="form-content">
        <table style="margin: 0 auto" >
          <tr>
             <td>用户名：</td>
             <td> &nbsp;</td>
             <td>${user.login_name }</td>
          </tr>
          <tr>
            <td>姓名：</td>
            <td> &nbsp;</td>
            <td>${user.username }</td>
          </tr>
          <tr>
            <td>身份证号：</td>
            <td>&nbsp;</td>
            <td>${user.identityID }</td>
          </tr>
          <tr>
            <td>年龄：</td>
            <td> &nbsp;</td>
            <td>${user.age }</td>
          </tr>
          <tr>
            <td>性别：</td>
            <td>&nbsp; </td>
            <td><%=gender%></td>
          </tr>
          <tr>
            <td>电话：</td>
            <td> </td>
            <td>${user.phone }</td>
          </tr>

          <tr>
            <td>邮件：</td>
            <td> </td>
            <td>${user.email }</td>
          </tr>          
        </table>
        <div style="text-align: center">
            <a style="width: 100px;margin: 10px 0;" class="sys-input" href="<%=basePath%>user/toUpdate" >&nbsp;修改个人信息</a>
        </div>
    </div>
   </section>
 </div>
<div id="footer">
  <jsp:include page="footer.jsp" flush="true"/>
</div>
</body>
</html>