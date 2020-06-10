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
    <title>QA</title>
    <script type="text/javascript" src="<%=path%>/js/jquery.min.js"></script>
</head>
<body>
  <div id="top">
    <jsp:include page="expertTop.jsp" flush="true"/>
  </div>
<div class="content-wrapper">     
   <section>
    <div class="section-title">
      <span>回答问题</span>
    </div>
    <div class="box section-qa-content">
     <form action="<%=basePath%>question/search" method="get">
         <div class="section-qa-row">
             <label>选择科室：</label>
             <select id="hospitalDepartment" name="hospitalDepartment">
                 <option value="0">所有</option>
                 <c:forEach items="${hospitalDepartmentList}" var="hd">
                     <option value="${hd.hdid}">${hd.name}</option>
                 </c:forEach>
             </select>
         </div>
         <div class="section-qa-row" style="justify-content: center">
             <button class="sys-btn" id="search" type="submit">查找</button>
         </div>
     </form>     
    </div>
  </section>   
</div>
<div id="footer">
  <jsp:include page="footer.jsp" flush="true"/>
</div>
</body>
</html>