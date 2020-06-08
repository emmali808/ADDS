<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
  String path=request.getContextPath();
  String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>诊疗记录</title>
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
      <table style="border-collapse:separate; border-spacing:10px;width: 100%;">
       <c:forEach items="${clinicRecordList }" var="clinicRecord">
        <tr>
          <td>诊断号: </td>
          <td>${clinicRecord.clinic_record_Id }</td>
        </tr>
        <tr>
          <td>诊断时间: </td>
          <td>${clinicRecord.time }</td>
        </tr>
        <tr>
          <td>医生名字: </td>
          <td>${clinicRecord.doctor_name }</td>

        </tr>
        <tr>
          <td>诊断内容: </td>
          <td>${clinicRecord.content }</td>

        </tr>
        <tr>
          <td>现病史: </td>
          <td>${clinicRecord.present_illness }</td>

        </tr>
        <tr>
          <td>既往史: </td>
          <td>${clinicRecord.medical_history }</td>

        </tr>
        <tr>
          <td>检查内容: </td>
          <td>${clinicRecord.examination }</td>

        </tr>
        <tr>
          <td>检验内容: </td>
          <td>${clinicRecord.inspection }</td>

        </tr>
        <tr>
          <td>药方: </td>
          <td>${clinicRecord.prescription }</td>

        </tr>
        <tr>
          <td>类型: </td>
          <td>${clinicRecord.type }</td>
        </tr>       
       </c:forEach>
        
      </table>
      <div>
        <c:choose>
          <c:when test="${usertype==1 }">
              <div class="pagination-wrapper" >
                  <span class="total-text">共${totalpage}页</span>
                  <a class="pagination"  href="<%=basePath%>record/getClinicRecordByDoc?curpage=${curpage-1}" >
                      <span class="front-page"></span>
                  </a>
                  <span class="pagination current-page">${curpage}</span>
                  <a class= "pagination "href="<%=basePath%>record/getClinicRecordByDoc?curpage=${curpage+1}" >
                      <span class="next-page"></span>
                  </a>
              </div>
          </c:when>
          <c:when test="${usertype==2 }">
              <div class="pagination-wrapper" >
                  <span class="total-text">共${totalpage}页</span>
                  <a class="pagination" href="<%=basePath%>record/clinicRecord?curpage=${curpage-1}" >
                      <span class="front-page"></span>
                  </a>
                  <span class="pagination current-page">${curpage}</span>
                  <a class= "pagination" href="<%=basePath%>record/clinicRecord?curpage=${curpage+1}" >
                      <span class="next-page"></span>
                  </a>
              </div>
          </c:when>
        </c:choose>
        
      </div>

    </div>

  </section>
</div>
<div id="footer">
  <jsp:include page="footer.jsp" flush="true"/>
</div>
</body>
</html>