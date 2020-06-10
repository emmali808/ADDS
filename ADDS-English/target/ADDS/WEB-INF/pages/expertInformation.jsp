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
    <jsp:include page="expertTop.jsp" flush="true"/>
</div>
<div class="content-wrapper">
  <section class="form-wrapper">
    <div class="section-title">
      <span>我的信息</span>
    </div>
    <div class="form-content">
        <table >
          <tr>
            <td>用户名：</td>
            <td> &nbsp;</td>
            <td>${user.username }</td>
          </tr>
          <tr>
            <td>身份证号</td>
            <td>&nbsp;</td>
            <td>${user.identityID }</td>
          </tr>
          <tr>
            <td>年龄</td>
            <td> &nbsp;</td>
            <td>${user.age }</td>
          </tr>
          <tr>
            <td>性别</td>
            <td>&nbsp; </td>
            <td><%=gender%></td>
          </tr>
          <tr>
            <td>电话</td>
            <td> </td>
            <td>${user.phone }</td>
          </tr>

          <tr>
            <td>邮件</td>
            <td> </td>
            <td>${user.email }</td>
          </tr>
              <tr>
                <td>出生年月</td>
                <td> </td>
                <td>${expert.birthday }</td>
              </tr>

              <tr>
                <td>婚姻情况</td>
                <td> </td>
                <td>
                <c:choose>
                  <c:when test="${expert.marriage.equals('0') }">未婚</c:when>
                  <c:when test="${expert.marriage.equals('1') }">已婚</c:when>
                  <c:when test="${expert.marriage.equals('2') }">离异</c:when>
                  <c:when test="${expert.marriage.equals('3') }">丧偶</c:when>
                </c:choose>
               </td>
              </tr>
              <tr>
                <td>民族</td>
                <td> </td>
                <td>${expert.nation }</td>
              </tr>
              <tr>
                <td>出生地</td>
                <td> </td>
                <td>${expert.birth_place }</td>
              </tr>

              <tr>
                <td>工作单位</td>
                <td> </td>
                <td>${expert.work_place }</td>
              </tr>

              <tr>
                <td>职称</td>
                <td> </td>
                <td>${expert.title }</td>
              </tr>
            <tr>
                <td>擅长方向</td>
                <td> </td>
                <td>${expert.skill }</td>
              </tr>

              <tr>
                <td>学历</td>
                <td> </td>
                <td>${expert.education }</td>
              </tr>
                <tr>
                    <td>其他介绍</td>
                    <td> </td>
                    <td>${expert.introduction }</td>
                </tr>
        </table>
        <div>
            <a  href="<%=basePath%>expert/toUpdate" >&nbsp;修改个人信息</a>
        </div>
    </div>
    </section>
  </div>

 <div id="footer">
  <jsp:include page="footer.jsp" flush="true"/>
</div>
</body>
</html>