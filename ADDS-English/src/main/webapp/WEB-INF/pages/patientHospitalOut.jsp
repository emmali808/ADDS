<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
  <title>查房记录</title>
  <link rel="stylesheet" href="<%=path%>/css/patient.css"/>
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
    <div class="form-content">
      <table class="hospital-out" style="border-collapse:separate; border-spacing:10px;">
        <c:forEach items="${hospitalOutList}" var="hospitalOut">
        <tr>
          <td >入院时间：</td>
          <td>${hospitalOut.time_in }</td>
        </tr>
        <tr>
          <td>出院时间：</td>

          <td>${hospitalOut.time_out }</td>

        </tr>
        <tr>
          <td>病人身份证号码：</td>
          <td>${hospitalOut.patient_ID }</td>

        </tr>
        <tr>
          <td>病人名字：</td>
          <td>${hospitalOut.patient_name }</td>

        </tr>
        <tr>
          <td>医生身份证号码：</td>
          <td>${hospitalOut.doctor_ID }</td>

        </tr>
        <tr>
          <td>医生名字：</td>
          <td>${hospitalOut.doctor_name }</td>

        </tr>
        <tr>
          <td>记录医生身份证号码：</td>
          <td>${hospitalOut.record_doctor_ID }</td>

        </tr>
        <tr>
          <td>记录医生名字：</td>
          <td>${hospitalOut.record_doctor_name }</td>
        </tr>
          <tr>
            <td>诊疗经过：</td>
            <td>${hospitalOut.treatment_procedure }</td>
          </tr>
          <tr>
            <td>入院诊断：</td>
            <td>${hospitalOut.admission_diagnosis }</td>
          </tr>
          <tr>
            <td>出院诊断：</td>
            <td>${hospitalOut.discharge_diagnosis }</td>
          </tr>
          <tr>
            <td>出院医嘱：</td>
            <td>${hospitalOut.medical_advice }</td>
          </tr>
         </c:forEach>
      </table>
     <div>
        <c:choose>
          <c:when test="${usertype==1 }">
              <div class="pagination-wrapper" >
                  <span class="total-text">共${totalpage}页</span>
                  <a class="pagination"  href="<%=basePath%>record/getHospitalOutRecordByDoc?curpage=${curpage-1}" >
                      <span class="front-page"></span>
                  </a>
                  <span class="pagination current-page">${curpage}</span>
                  <a class= "pagination " href="<%=basePath%>record/getHospitalCheckRecordByDoc?curpage=${curpage+1}" >
                      <span class="next-page"></span>
                  </a>
              </div>
          </c:when>
         
        </c:choose>
        
      </div>
    </div>    
  </section>
</div>
<div id="top">
  <jsp:include page="footer.jsp" flush="true"/>
</div>
</body>
</html>