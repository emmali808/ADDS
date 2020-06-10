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
  <title>住院记录</title>
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
      <table style="border-collapse:separate; border-spacing:10px;">
        <c:forEach items="${hospitalInList}" var="hospitalIn">
        <tr>
          <td style="white-space:nowrap;">入院时间</td>
          <td>${hospitalIn.time }</td>
        </tr>
        <tr>
          <td>医生名字</td>
          <td>${hospitalIn.doctor_name }</td>

        </tr>
        <tr>
          <td>入院原因</td>
          <td>${hospitalIn.causes }</td>

        </tr>
        <tr>
          <td>病程</td>
          <td>${hospitalIn.course }</td>

        </tr>
        <tr>
          <td>症状表现</td>
          <td>${hospitalIn.symptoms }</td>

        </tr>
        <tr>
          <td>查体</td>
          <td>${hospitalIn.physical_examination }</td>

        </tr>
        <tr>
          <td>既往史</td>
          <td>${hospitalIn.medical_history }</td>
        </tr>
          <tr>
            <td>辅助检查</td>
            <td>${hospitalIn.assistant_examination }</td>
          </tr>
          <tr>
            <td>初步诊断</td>
            <td>${hospitalIn.preliminary_diagnosis }</td>
          </tr>
          <tr>
            <td>诊断依据</td>
            <td>${hospitalIn.diagnostic_basis }</td>
          </tr>
          <tr>
            <td>鉴别诊断</td>
            <td>${hospitalIn.differential_diagnosis }</td>
          </tr>

          <tr>
            <td>诊疗计划</td>
            <td>${hospitalIn.assessment_plan }</td>
          </tr>
         </c:forEach>
      </table>
     <div>
        <c:choose>
          <c:when test="${usertype==1 }">
              <div class="pagination-wrapper" >
                  <span class="total-text">共${totalpage}页</span>
                  <a class="pagination" href="<%=basePath%>record/getHospitalRecordInByDoc?curpage=${curpage-1}" >
                      <span class="front-page"></span>
                  </a>
                  <span class="pagination current-page">${curpage}</span>
                  <a class= "pagination "href="<%=basePath%>record/getHospitalRecordInByDoc?curpage=${curpage+1}" >
                      <span class="next-page"></span>
                  </a>
              </div>
          </c:when>
          <c:when test="${usertype==2 }">
              <div class="pagination-wrapper" >
                  <span class="total-text">共${totalpage}页</span>
                  <a class="pagination" href="<%=basePath%>record/hospitalRecordIn?curpage=${curpage-1}" >
                      <span class="front-page"></span>
                  </a>
                  <span class="pagination current-page">${curpage}</span>
                  <a class= "pagination " href="<%=basePath%>record/hospitalRecordIn?curpage=${curpage+1}" >
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