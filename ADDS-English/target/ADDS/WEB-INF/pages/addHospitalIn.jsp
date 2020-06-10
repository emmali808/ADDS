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
  <title>住院信息</title>
  <link rel="stylesheet" href="<%=path%>/css/patient.css"/>
</head>
<body>
<div id="top">
    <jsp:include page="doctorTop.jsp" flush="true"/>
</div>
<form action="<%=basePath%>record/addHospitalIn" method="post" >
    <div class="content-wrapper">
        <section class="form-wrapper">
            <div class="section-title" style="flex-basis: 200px">
                <span>新增住院信息：</span>
            </div>
            <div class="form-content">
                <table class="hospital-out" style="margin: 0 auto;border-collapse:separate; border-spacing:10px;">
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
                        <td><input class="sys-input" id="time" type="text" name="time"><br></td>

                    </tr>

                    <tr>
                        <td>入院原因：</td>
                        <td><input class="sys-input" id="causes" type="text" name="causes"><br></td>
                    </tr>
                    <tr>
                        <td>病程：</td>
                        <td><input class="sys-input" id="course" type="text" name="course"><br></td>
                    </tr>
                    <tr>
                        <td>症状表现：</td>
                        <td><input class="sys-input" id="symptoms" type="text" name="symptoms"><br></td>
                    </tr>
                    <tr>
                        <td>查体：</td>
                        <td><input class="sys-input" id="physical_examination" type="text" name="physical_examination"> <br></td>
                    </tr>
                    <tr>
                        <td>既往史：</td>
                        <td><input class="sys-input" id="medical_history" type="text" name="medical_history"><br></td>
                    </tr>

                    <tr>
                        <td>辅助检查：</td>
                        <td><input class="sys-input" id="assistant_examination" type="text" name="assistant_examination"><br></td>
                    </tr>
                    <tr>
                        <td>初步诊断：</td>
                        <td><input class="sys-input" id="preliminary_diagnosis" type="text" name="preliminary_diagnosis"><br></td>
                    </tr>
                    <tr>
                        <td>诊断依据：</td>
                        <td><input class="sys-input" id="diagnostic_basis" type="text" name="diagnostic_basis"><br></td>
                    </tr>
                    <tr>
                        <td>鉴别诊断：</td>
                        <td><input class="sys-input" id="differential_diagnosis" type="text" name="differential_diagnosis"><br></td>
                    </tr>
                    <tr>
                        <td>诊疗计划：</td>
                        <td><input class="sys-input" id="assessment_plan" type="text" name="assessment_plan"><br></td>
                    </tr>
                </table>
                <div style="text-align: center">
                    <input type="submit" style="width: 100px" class="sys-input" value="提交">
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