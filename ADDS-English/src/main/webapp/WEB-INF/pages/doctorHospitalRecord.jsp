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
  <link rel="stylesheet" href="<%=path%>/css/reset.css"/>
  <link rel="stylesheet" href="<%=path%>/css/style.css"/>


</head>
<body>
<div id="top">
  <jsp:include page="doctorTop.jsp" flush="true"/>
</div>
<div class="content-wrapper">
  <section>
    <div class="section-title">
      <span>
                            查询记录
      </span>
    </div>
    <div class="section-content">
       <a href="<%=basePath%>record/getHospitalRecordInByDoc">查询住院记录</a>|<a href="<%=basePath%>record/getHospitalCheckRecordByDoc">查询查房记录</a>|<a href="<%=basePath%>record/getHospitalOutRecordByDoc">查询出院记录</a>
    </div>

  </section>
  <section>
    <div class="section-title">
      <span>
                                        添加住院记录
      </span>
    </div>
     <div class="section-content">
       <a href="<%=basePath%>record/toAddHospitalIn">添加住院记录</a>
     </div>

  </section>
  <section>
    <div class="section-title">
      <span>
                                  添加查房记录
      </span>
    </div>
    <div class="section-content">
      <a href="<%=basePath%>record/toAddHospitalCheck">添加查房记录</a>
    </div>
  </section>
  <section>
    <div class="section-title">
      <span>
                                   添加出院记录
      </span>
    </div>
    <div class="section-content">
       <a href="<%=basePath%>record/toAddHospitalOut">添加出院记录</a>
    </div>

  </section>
</div>
<div id="top">
  <jsp:include page="footer.jsp" flush="true"/>
</div>
</body>
</html>