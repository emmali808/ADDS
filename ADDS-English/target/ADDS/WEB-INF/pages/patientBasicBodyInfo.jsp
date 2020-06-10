<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<%
  String path=request.getContextPath();
  String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
 /*  int count=1; */
%>
<!DOCTYPE html>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>体征信息</title>
</head>
<body>
<div id="top">
    <jsp:include page="patientTop.jsp" flush="true"/>
</div>
<div class="content-wrapper">
    <section class="form-wrapper">
    <div class="section-title" style="flex-basis: 200px">
      <span>Historical physical condition</span>
    </div>
      <div class="form-content">
          <table style="border-collapse:separate; border-spacing:10px;width: 400px" class="myTable">
              <c:forEach items="${basicBodyInfoList}" var="basicBodyInfo">
              <tr>
                  <td>Time：</td>
                  <td>${basicBodyInfo.time }</td>
              </tr>
                  <tr>
                      <td>Pulse：</td>
                      <td>${basicBodyInfo.pulse }</td>
                  </tr>
                  <tr>
                      <td>Mouth temperature：</td>
                      <td>${basicBodyInfo.oral }</td>
                  </tr>
                  <tr>
                      <td>Anal temperature：</td>
                      <td>${basicBodyInfo.rectal }</td>
                  </tr>
                  <tr>
                      <td>Axillary temperature：</td>
                      <td>${basicBodyInfo.axillary}</td>
                  </tr>
                  <tr>
                      <td>Heart rate：</td>
                      <td>${basicBodyInfo.heart_rate }</td>
                  </tr>
                  <tr>
                      <td>Blood pressure：</td>
                      <td>${basicBodyInfo.blood_pressure_high }/${basicBodyInfo.blood_pressure_low }</td>
                  </tr>
                  <tr>
                      <td>Blood glucose：</td>
                      <td>${basicBodyInfo.blood_glucose }</td>
                  </tr>
                  <tr>
                      <td>Weight：</td>
                      <td>${basicBodyInfo.weight }</td>
                  </tr>
                  <tr>
                      <td>Height：</td>
                      <td>${basicBodyInfo.height }</td>
                  </tr>
                  <tr>
                      <td>&nbsp;</td>
                  </tr>
              </c:forEach>
          </table>
         <div>
             <div class="pagination-wrapper" >
                 <span class="total-text">${totalpage}Pages</span>
                 <a class="pagination" href="<%=basePath%>record/basicBody?curpage=${curpage-1}" >
                     <span class="front-page"></span>
                 </a>
                 <span class="pagination current-page">${curpage}</span>
                 <a class= "pagination " href="<%=basePath%>record/basicBody?curpage=${curpage+1}" >
                     <span class="next-page"></span>
                 </a>
             </div>
         </div>
      </div>
    </section>
    <section class="form-wrapper" style="display: none">
        <div class="section-title" style="flex-basis: 200px">
        <span>添加体征信息</span>
      </div>
        <div class="form-content" style="text-align: center">
            <a style="margin-top:20px;" class="sys-btn" href="<%=basePath%>record/toAddBasicBody">点击添加体征信息</a>
        </div>
    </section>
  </div>
<div id="footer">
  <jsp:include page="footer.jsp" flush="true"/>
</div>
</body>

</html>
