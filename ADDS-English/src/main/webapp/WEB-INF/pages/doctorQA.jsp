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
</head>
<body>
  <div id="top">
    <jsp:include page="doctorTop.jsp" flush="true"/>
  </div>
<div class="content-wrapper">
  <section>
    <div class="section-title">
      <span>Submit questions</span>
    </div>
      <div class="box section-qa-content">
          <div class="section-qa-row">
              <label>Description：</label>
              <textarea class="sys-input" type="text" id="question" name="question"></textarea>
          </div>
          <div class="section-qa-row">
              <label class="form-label">Department：</label>
              <select name="hospitalDepartment" id="hospitalDepartment">
                  <c:forEach items="${hospitalDepartmentList}" var="hd">
                      <option value="${hd.hdid}">${hd.name}</option>
                  </c:forEach>
              </select>
          </div>
          <div class="section-qa-row" style="justify-content: center">
              <button id="addQuestion" class="sys-btn" >submit</button>
          </div>
      </div>
  </section>    
   <section>
    <div class="section-title">
      <span>Answer questions</span>
    </div>
    <div class="box section-qa-content">
        <form action="<%=basePath%>question/search" method="get">
         <div class="section-qa-row">
           <label class="form-label">Department：</label>
           <select  name="hospitalDepartment">
               <option value="0">所有</option>
               <c:forEach items="${hospitalDepartmentList}" var="hd">
               <option value="${hd.hdid}">${hd.name}</option>
               </c:forEach>
           </select>
       </div>
         <div class="section-qa-row" style="justify-content: center">
             <button id="search" class="sys-btn" type="submit">search</button>
         </div>
     </form>     
    </div>
  </section>
    <section>
        <div class="section-title">
            <span>View questions</span>
        </div>
        <div class="box section-qa-content">
            <form style="text-align: center" action="<%=basePath%>question/searchSelfQuestion" method="get">
                <button style="margin-top: 15px" class="sys-btn" id="searchSelfQuestion" type="submit">search</button>
            </form>
        </div>
    </section>
  <%--<section>--%>
    <%--<div class="section-title">--%>
      <%--<span>在线交流</span>--%>
    <%--</div>--%>
      <%--<div class="box section-qa-content" style="text-align: center">--%>
          <%--<a style="margin-top: 25px" class="sys-btn"  href="<%=basePath %>doctor/talk"  >进入在线交流</a>--%>
      <%--</div>--%>
  <%--</section> --%>
</div>
<div id="questionlistdiv" class="wrapper">
  
</div>

<div id="footer">
  <jsp:include page="footer.jsp" flush="true"/>
</div>
</body>
<script>
    $('#addQuestion').on('click',function(){
        var qcontent=$("#question").val();
        var hospitalDepartment=$("#hospitalDepartment").val();
        $.ajax({
            url:"<%=basePath%>question/add",
            type:"POST",
            data:{questioncontent:qcontent,hospitalDepartmentid:hospitalDepartment},
            dataType:"json",
            success:function(data){
                if(data.msg==1){
                    alert("问题添加成功!");
                }else{
                    alert("问题添加失败!");
                }
            },
            error:function(){
                alert('提示:问题添加失败（error）！');
            }
        });
    });
</script>
</html>