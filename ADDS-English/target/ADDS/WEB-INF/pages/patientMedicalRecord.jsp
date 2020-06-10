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
  <title>门诊记录</title>
</head>
<body>
<div id="top">
  <jsp:include page="patientTop.jsp" flush="true"/>
</div>
<div class="content-wrapper">
  <section>
    <div class="section-patient-content">
      <table style="border-collapse:separate; border-spacing:10px;">
        <tr>
          <td>诊断号</td>
          <td>${clinicRecord.clinic_record_Id }</td>
        </tr>
        <tr>
          <td style="white-space:nowrap;">诊断时间</td>
          <td>${clinicRecord.time }</td>
        </tr>
        <tr>
          <td>医生名字</td>
          <td>${clinicRecord.doctor_name }</td>

        </tr>
        <tr>
          <td>诊断内容</td>
          <td>${clinicRecord.content }</td>

        </tr>
        <tr>
          <td>检查内容</td>
          <td>${clinicRecord.examination }</td>

        </tr>
        <tr>
          <td>检验内容</td>
          <td>${clinicRecord.inspection }</td>

        </tr>
        <tr>
          <td>药方</td>
          <td>${clinicRecord.prescription }</td>

        </tr>
        <tr>
          <td>类型</td>
          <td>${clinicRecord.type }</td>
        </tr>



      </table>

    </div>
  </section>
</div>
<footer class="footer">
  <div class="copyright">@Copyright XMU Lab</div>
</footer>
</body>
</html>