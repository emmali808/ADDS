<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="cn.medicine.pojo.User" %>
<%@ page import="cn.medicine.pojo.Patient" %>
<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<%
  String path=request.getContextPath();
  String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";


%>
<!DOCTYPE html>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>新增体征信息</title>
  <link rel="stylesheet" href="<%=path%>/css/reset.css"/>
  <link rel="stylesheet" href="<%=path%>/css/style.css"/>


</head>
<body>
<div id="top">
    <jsp:include page="patientTop.jsp" flush="true"/>
</div>
<form style="min-height: 100%" action="<%=basePath%>record/addBasicBodyInfo" method="post" >
<div class="content-wrapper">
  <section class="form-wrapper">
    <div class="section-title" style="flex-basis:200px">
      <span>新增体征信息</span>
    </div>
      <div class="form-content" style="text-align: center">
          <table style="border-collapse:separate; border-spacing:10px;margin: 0 auto;">
              <tr>
                  <td>时间: </td>
                  <td><input class="sys-input" id="time" type="text" name="time"><br></td>
              </tr>
                  <tr>
                      <td>脉搏: </td>
                      <td><input class="sys-input" id="pulse" type="text" name="pulse"><br></td>
                  </tr>
                  <tr>
                      <td>口表温度: </td>
                      <td><input class="sys-input" id="oral" type="text" name="oral"><br></td>
                  </tr>
                  <tr>
                      <td>肛门温度: </td>
                      <td><input class="sys-input" id="rectal" type="text" name="rectal"><br></td>
                  </tr>
                  <tr>
                      <td>腋窝温度: </td>
                      <td><input class="sys-input" id="axillary" type="text" name="axillary"><br></td>
                  </tr>
                  <tr>
                      <td>心率: </td>
                      <td><input class="sys-input" id="heart_rate" type="text" name="heart_rate"><br></td>
                  </tr>
                  <tr>
                      <td>血压: </td>
                      <td>
                          <input class="sys-input" style="margin-bottom: 10px;" id="blood_pressure_high" type="text" name="blood_pressure_high">
                          <input class="sys-input" id="blood_pressure_low" type="text" name="blood_pressure_low">
                      </td>
                  </tr>
                  <tr>
                      <td>血糖: </td>
                      <td><input class="sys-input" id="blood_glucose" type="text" name="blood_glucose"><br></td>
                  </tr>
                  <tr>
                      <td>体重: </td>
                      <td><input class="sys-input" id="weight" type="text" name="weight"><br></td>
                  </tr>
                  <tr>
                      <td>身高: </td>
                      <td><input class="sys-input" id="height" type="text" name="height"><br></td>
                  </tr>
          </table>
          <div>
              <input type="submit" class="common-submit form-submit" value="提交">
          </div>
      </div>
    </section>
  </div>
</form>
<footer class="footer">
  <div class="copyright">@Copyright XMU Lab</div>
</footer>
</body>
</html>