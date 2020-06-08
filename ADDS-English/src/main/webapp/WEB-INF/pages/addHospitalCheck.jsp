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
<form action="<%=basePath%>record/addHospitalCheck" method="post" >
    <div class="content-wrapper">
        <section class="form-wrapper">
            <div class="section-title" style="flex-basis: 210px">
                <span>新增查房信息</span>
            </div>
            <div class="form-content">
                <table class="hospital-out" style="border-collapse:separate; border-spacing:10px;margin: 0 auto">
                    <tr>
                        <td>检查时间：</td>
                        <td><input class="sys-input" id="time" type="text" name="time"><br></td>
                    </tr>
                    <tr>
                        <td>病人名字：</td>
                        <td><input class="sys-input" id="patient_name" type="text" name="patient_name"><br></td>
                    </tr>
                   <tr>
                        <td>病人身份证号码：</td>
                        <td><input class="sys-input" id="patient_ID" type="text" name="patient_ID"><br></td>
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
                        <td>辅助检查：</td>
                        <td><input class="sys-input" id="assistant_examination" type="text" name="assistant_examination"><br></td>
                    </tr>
                    <tr>
                        <td>诊断：</td>
                        <td><input class="sys-input" id="diagnosis" type="text" name="diagnosis"><br></td>
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
                    <input style="width: 100px" type="submit" class="sys-input" value="提交">
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