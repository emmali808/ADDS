<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
  String path=request.getContextPath();
  String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE>
<html style="background-color: #EDF1F5;">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>问题列表</title>
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

  <%--<div style="font-size: 18px;margin-left: 100px">问答平台 > 回答问题 </div>--%>
  <div id="questionlistdiv" class="wrapper" style="position: relative;padding-bottom: 40px">
    <c:forEach items="${questionList}" var="question">
       <section class="container">
            <%--<form class="qa-input" action="" method="post">--%>
                <div class="number">${question.remark}</div>
                <div class="tran-in"></div>
                <div class="qa-input">
                    <div class="question" >${question.content}</div>
                    <div class="answer" data-number="${question.qid}">
                        <span class="answer-item"><input name="answer-item" type="radio" value="1"/>Yes</span>
                        <span class="answer-item"><input name="answer-item" type="radio" value="2"/>No</span>
                    </div>
                    <div class="submit">
                        <button type="submit" class="btn question-submit" style="margin-right: 10px" >Submit</button>
                        <button class="btn">Cancel</button>
                    </div>
                </div>
            <%--</form>--%>
          </section>
    </c:forEach>
      <c:if test="${user.type eq 1}">

          <div class="pagination-wrapper" >
              <span class="total-text">共${totalpage}页</span>
              <a class="pagination" href="<%=basePath%>question/search?curpage=${curpage-1}&hospitalDepartment=${hospitalDepartment}" >
                  <span class="front-page"></span>
              </a>
              <span class="pagination current-page">${curpage}</span>
              <a class= "pagination" href="<%=basePath%>question/search?curpage=${curpage+1}&hospitalDepartment=${hospitalDepartment}" >
                  <span class="next-page"></span>
              </a>
          </div>
      </c:if>
      <c:if test="${user.type eq 1}">

          <div class="pagination-wrapper" >
              <span class="total-text">共${totalpage}页</span>
              <a class="pagination" href="<%=basePath%>question/search?curpage=${curpage-1}&hospitalDepartment=${hospitalDepartment}" >
                  <span class="front-page"></span>
              </a>
              <span class="pagination current-page">${curpage}</span>
              <a class= "pagination" href="<%=basePath%>question/search?curpage=${curpage+1}&hospitalDepartment=${hospitalDepartment}" >
                  <span class="next-page"></span>
              </a>
          </div>
      </c:if>ytchen


  </div>
  
 <div id="footer">
  <jsp:include page="footer.jsp" flush="true"/>
</div>
</body>
<script>
    $('.question-submit').on('click',function(){
        var $answer = $(this).parent('.submit').prev('.answer');
        console.log($(this));
        var qid=$answer.data('number');
        var answerid= $answer.find('input[name="answer-item"]:checked').val();
        
        $.ajax({
     		url:"<%=basePath%>question/addQuestionResult",
     		type:"POST",
     		data:{questionid:qid,questionanswer:answerid},
     		dataType:"json",
     		success:function(data){
     			if(data.msg==-1){
     				
     			}else if(data.msg==1){
                   console.log(data);
     				$answer.parent().parent('.container').remove();
     				alert('提示:成功回答问题！');
     			}
     		},
     		error:function(){
     			alert('提示:回答问题失败！');
     		}
     	});
        
    });
</script>
</html>