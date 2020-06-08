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
  <title>寻医问药</title>
</head>

<body>
<div id="top">
  <jsp:include page="patientTop.jsp" flush="true"/>
</div>
<div class="content-wrapper">
  <section>
    <div class="section-title">
      <span>Submit</span>
    </div>
    <div class="box section-qa-content">
      <div class="section-qa-row">
        <label class="form-label">Description: </label>
        <textarea class="sys-input" type="text" id="question" name="question"></textarea>
      </div>
      <div class="section-qa-row">
        <label class="form-label">Department type: </label>
        <select name="hospitalDepartment" id="hospitalDepartment">
          <c:forEach items="${hospitalDepartmentList}" var="hd">
            <option value="${hd.hdid}">${hd.name}</option>
          </c:forEach>
        </select>
      </div>
      <div class="section-qa-row" style="justify-content: center">
        <button id="addQuestion" class="sys-btn">submit</button>
      </div>
    </div>
  </section>
  <section>
    <div class="section-title">
      <span>Question</span>
    </div>
      <div class="box section-qa-content">
        <form style="width:100px;margin:0 auto ;margin-top: 25px;" action="<%=basePath%>question/searchSelfQuestion" method="get">
          <button id="searchSelfQuestion" class="sys-btn" type="submit">Search question</button>
        </form>
      </div>
  </section>
  <section>
    <div class="section-title">
      <span>Online system</span>
    </div>
    <div class="box section-qa-content" style="text-align: center">
      <a style="margin-top: 25px" class="sys-btn" href="<%=basePath %>patient/talk" >Go to Online system</a>
    </div>
  </section>
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