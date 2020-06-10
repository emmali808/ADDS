<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
  String path=request.getContextPath();
  String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>历史问题</title>
<link rel="stylesheet" href="<%=path%>/css/reset.css"/>
    <link rel="stylesheet" href="<%=path%>/css/style.css"/>
    <script type="text/javascript" src="<%=path%>/js/jquery.min.js"></script>
</head>
<body>
  <div id="top" style="position: relative;z-index: 10">
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
  
  <div id="questionlistdiv" class="content-wrapper" style="position: relative;z-index: 1">
      <c:if test="${count==0}">
          <div>未回答过问题</div>
      </c:if>
      <c:forEach items="${qarList}" var="questionAndResult">
       <section class="container">
         <div class="box section-qa-content">
             <div class="question" style="margin: 5px 0px;" data-number="${questionAndResult.qid}">${questionAndResult.content}</div>
             <div id="result">
                 我选择了
                 <c:choose>
                    <c:when test="${questionAndResult.resultType==1 }">Yes</c:when>
                    <c:when test="${questionAndResult.resultType==2 }">No</c:when>
                  </c:choose>                
                </div>
         </div>
       </section>
    </c:forEach>
      <div class="pagination-wrapper" >
          <span class="total-text">共${totalpage}页</span>
          <a class="pagination" href="<%=basePath%>expert/historyAnswerQuestion?curpage=${curpage-1}" >
              <span class="front-page"></span>
          </a>
          <span class="pagination current-page">${curpage}</span>
          <a class= "pagination" href="<%=basePath%>expert/historyAnswerQuestion?curpage=${curpage+1}" >
              <span class="next-page"></span>
          </a>
      </div>
  </div>
 <div id="footer">
  <jsp:include page="footer.jsp" flush="true"/>
</div>
</body>
</html>