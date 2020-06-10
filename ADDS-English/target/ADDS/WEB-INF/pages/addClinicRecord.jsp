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
  <title>门诊信息</title>
    <link rel="stylesheet" href="<%=path%>/css/doctor.css"/>
</head>
<body>
<div id="top">
    <jsp:include page="doctorTop.jsp" flush="true"/>
</div>
<form action="<%=basePath%>record/addClinicRecord" method="post" >
    <div class="content-wrapper">
        <section class="form-wrapper">
            <div class="section-title" style="flex-basis: 200px">
                <span>新增门诊信息</span>
            </div>

            <div class="form-content">
                <table class="doc-add-clinic" style="margin:0 auto;border-collapse:separate; border-spacing:10px;">
                    <tr>
                        <td>病人名字：</td>
                        <td><input class="sys-input"id="patient_name" type="text" name="patient_name"><br></td>
                    </tr>
                    <tr>
                        <td>病人身份证号码：</td>
                        <td><input class="sys-input"id="patient_ID" type="text" name="patient_ID"><br></td>
                    </tr>
                    <tr>
                        <td>门诊时间：</td>
                        <td><input class="sys-input"id="time" type="text" name="time"><br></td>

                    </tr>
                   <tr>
                        <td>诊断内容：</td>
                        <td><input class="sys-input"id="content" type="text" name="content"><br></td>
                    </tr>
                    <tr>
                        <td>现病史：</td>
                        <td><input class="sys-input"id="present_illness" type="text" name="present_illness"><br></td>
                    </tr>
                    <tr>
                        <td>既往史：</td>
                        <td><input class="sys-input"id="medical_history" type="text" name="medical_history"><br></td>
                    </tr>
                    <tr>
                        <td>检查内容：</td>
                        <td><input class="sys-input"id="examination" type="text" name="examination"> <br></td>
                    </tr>
                    <tr>
                        <td>检验内容：</td>
                        <td><input class="sys-input"id="inspection" type="text" name="inspection"><br></td>
                    </tr>

                    <tr>
                        <td>辅助检查：</td>
                        <td><input class="sys-input"id="assistant_examination" type="text" name="assistant_examination"><br></td>
                    </tr>
                    <tr>
                        <td>处方：</td>
                        <td><input class="sys-input"id="prescription" type="text" name="prescription"><br></td>
                    </tr>
                    <tr>
                        <td>类别：</td>
                        <td>
                            <select name="type" style="width: 100%;">
                                <option value="0">初次门诊</option>
                                <option value="1">复诊</option>
                            </select>
                        </td>
                    </tr>
                </table>
                <div style="text-align: center">
                    <input style="width: 100px" type="submit" class="sys-input" value="提交">
                </div>
            </div>
        </section>
    </div>
</form>
 <div id="footer">
  <jsp:include page="footer.jsp" flush="true"/>
</div>
</body>
</html>