<%@ page import="cn.medicine.pojo.User" %>
<%@ page import="cn.medicine.pojo.Patient" %>
<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<%
  String path=request.getContextPath();
  String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    User user=(User)request.getSession().getAttribute("user");
    Patient patient=(Patient)request.getSession().getAttribute("patient");
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
    <jsp:include page="doctorTop.jsp" flush="true"/>
</div>
<div class="content-wrapper">
    <section class="form-wrapper">
        <div class="section-title" style="flex-basis:200px">
            <span>我的信息</span>
        </div>

        <div  class="form-content" style="text-align: center">
            <table style="border-collapse:separate;margin-left: 100px;">
          <tr>
             <td>用户名：</td>
             <td>${user.login_name }</td>
          </tr>
          <tr>
            <td>姓名：</td>
            <td>${user.username }</td>
          </tr>
          <tr>
            <td>身份证号：</td>
            <td>${user.identityID }</td>
          </tr>
          <tr>
            <td>年龄：</td>
            <td>${user.age }</td>
          </tr>
          <tr>
            <td>性别：</td>
            <td><%=gender%></td>
          </tr>
          <tr>
            <td>电话：</td>
            <td>${user.phone }</td>
          </tr>

          <tr>
            <td>邮件：</td>
            <td>${user.email }</td>
          </tr>
              <tr>
                <td>出生年月：</td>
                <td>${doctor.birthday }</td>
              </tr>

              <tr>
                <td>婚姻情况：</td>
                <td>${doctor.marriage }</td>
              </tr>
              <tr>
                <td>民族：</td>
                <td>${doctor.nation }</td>
              </tr>
              <tr>
                <td>出生地：</td>
                <td>${doctor.birth_place }</td>
              </tr>

              <tr>
                <td>工作单位：</td>
                <td>${doctor.work_place }</td>
              </tr>
               <tr>
                <td>入职时间：</td>
                <td>${doctor.entry_time }</td>
              </tr>
               <tr>
                <td>所属科室：</td>
                <td>${doctor.department }</td>
              </tr>

              <tr>
                <td>职务：</td>
                <td>${doctor.duty }</td>
              </tr>
              <tr>
                <td>职称：</td>
                <td>${doctor.title }</td>
              </tr>
            <tr>
                <td>擅长方向：</td>
                <td>${doctor.skill }</td>
              </tr>

              <tr>
                <td>门诊时间：</td>
                <td>${doctor.outpatient_time }</td>
              </tr>
                <tr>
                    <td>其他介绍：</td>
                    <td>${doctor.introduction }</td>
                </tr>
        </table>
        <div style="width: 200px;margin: 0 auto;">
            <a class="common-submit form-submit" href="<%=basePath%>doctor/toUpdate" >&nbsp;修改个人信息</a>
        </div>
    </div>
   </section>
 </div>
<div id="footer">
  <jsp:include page="footer.jsp" flush="true"/>
</div>
</body>
</html>