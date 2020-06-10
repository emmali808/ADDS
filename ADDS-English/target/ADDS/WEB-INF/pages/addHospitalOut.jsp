<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="cn.medicine.pojo.User" %>
<%@ page import="cn.medicine.pojo.Patient" %>
<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<%
  String path=request.getContextPath();
  String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    int count=1;

%>
<!DOCTYPE html>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>住院信息</title>
  <link rel="stylesheet" href="<%=path%>/css/patient.css"/>
</head>
<body>
<div id="top">
    <jsp:include page="doctorTop.jsp" flush="true"/>
</div>
<form action="<%=basePath%>record/addHospitalOut" method="post" >
    <div class="content-wrapper">
        <section class="form-wrapper">
            <div class="section-title" style="flex-basis: 200px">
                <span>新增出院信息</span>
            </div>
            <div class="form-content">
                <table class="hospital-out" style="border-collapse:separate; border-spacing:10px;margin: 0 auto;">
                    <tr>
                        <td>病人名字：</td>
                        <td><input class="sys-input" id="patient_name" type="text" name="patient_name"><br></td>
                    </tr>
                    <tr>
                        <td>病人身份证号码：</td>
                        <td><input class="sys-input" id="patient_ID" type="text" name="patient_ID"><br></td>
                    </tr>

                    <tr>
                        <td>入院时间：</td>
                        <td><input class="sys-input" id="time_in" type="text" name="time_in"><br></td>
                    </tr>
                    <tr>
                        <td>出院时间：</td>
                        <td><input class="sys-input" id="time_out" type="text" name="time_out"><br></td>

                    </tr>

                    <tr>
                        <td>诊疗经过：</td>
                        <td><input class="sys-input" id="treatment_procedure" type="text" name="treatment_procedure"><br></td>
                    </tr>

                    <tr>
                        <td>入院诊断：</td>
                        <td><input class="sys-input" id="admission_diagnosis" type="text" name="admission_diagnosis"><br></td>
                    </tr>
                    <tr>
                        <td>出院诊断：</td>
                        <td><input class="sys-input" id="discharge_diagnosis" type="text" name="discharge_diagnosis"><br></td>
                    </tr>
                    <tr>
                        <td>出院医嘱：</td>
                        <td><input class="sys-input" id="medical_advice" type="text" name="medical_advice"><br></td>
                    </tr>
                    <tr>
                        <td>医生身份证号码：</td>
                        <td><input class="sys-input" id="doctor_ID" type="text" name="doctor_ID"><br></td>
                    </tr>
                    <tr>
                        <td>医生名字：</td>
                        <td><input class="sys-input" id="doctor_name" type="text" name="doctor_name"><br></td>
                    </tr>
                </table>
                <div style="text-align: center">
                    <input style="width: 100px" class="sys-input" type="submit" value="提交">
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