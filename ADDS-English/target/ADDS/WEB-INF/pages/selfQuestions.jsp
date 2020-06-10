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
<title>提出的问题</title>
    <link rel="stylesheet" href="<%=path%>/css/doctor.css"/>
    <script type="text/javascript" src="<%=path%>/js/jquery.min.js"></script>
    <script type="text/javascript" src="<%=path%>/js/echarts.min.js"></script>
</head>
<body>
  <div id="top" style="position:relative;z-index: 10;">
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
  <%--<div style="font-size: 18px;margin-left: 100px">Expert Q&A > View analysis </div>--%>
  <div id="questionlistdiv" class="content-wrapper" style="position: relative;z-index:1;">
      <c:if test="${count==0}">
          <div style="text-align: center;margin: 20px 0">未提问过问题</div>
      </c:if>
      <c:forEach items="${questionList}" var="question">
       <section class="container">
         <div class="qa-input">
             <div class="question ques-row" data-number="${question.qid}">
                    <div>
                        ${question.content}
                    </div>
                    <div class="ques-col">
                        <button type="submit" class="sys-input" >View analysis</button>
                    </div>
             </div>
             <div class="ques-result">

             </div>
         </div>
       </section>
    </c:forEach>
      <div class="pagination-wrapper" style="bottom: 0px" >
          <span class="total-text">共${totalpage}页</span>
          <a class="pagination" href="<%=basePath%>question/searchSelfQuestion?curpage=${curpage-1}" >
              <span class="front-page"></span>
          </a>
          <span class="pagination current-page">${curpage}</span>
          <a class= "pagination" href="<%=basePath%>question/searchSelfQuestion?curpage=${curpage+1}" >
              <span class="next-page"></span>
          </a>
      </div>
  </div>
  <div id="footer">
  <jsp:include page="footer.jsp" flush="true"/>
</div>
</body>
<script>
    $('.ques-col').on('click','button',function(){
        var $question = $(this).parent('.ques-col').parent('.question');
        /*console.log($(this));*/
       /*  alert($answer.data('number')); */
       /*  alert( $answer.find('input[name="answer-item"]:checked').val()); */
        var qid=$question.data('number');
        var option = null;
        var chart = null;
        $.ajax({
     		url:"<%=basePath%>question/searchAnswer",
     		type:"POST",
     		data:{questionid:qid},
     		dataType:"json",
     		success:function(data){
                if(data.msg==1){
                    console.log("success");
                    var node = $question.siblings('.ques-result');
                    node.not('.show-result').addClass('show-result');
                    console.log(node);
                    setTimeout(function() {
                        initChart(node.get(0),data);
                    },500)
//                    var str="<label>参与总人数"+data.allnumber+"</label><br>"+"<label>选择Yes的人数"+data.yesnumber+"</label><br>"+
//                            "<label>选择No的人数"+data.nonumber+"</label><br>";
//                   $question.siblings(".result").html(str);
                }else{
                    alert("获取信息失败!");
                }
     		},
     		error:function(){
                alert("获取信息失败error!");
     		}
     	});

        
    });
    function initChart(node,data) {
        console.log('charts');
        console.log(node);
        var chart = echarts.init(node);
        var option = {
            title: {
                text: ''
            },
            tooltip: {
                trigger: 'item',
                formatter:  "{a} <br/>{b} : {c} ({d}%)"
            },
            legend: {
                orient: 'vertical',
                left: 'left',
                data: ['Number of people who choose YES','Number of people who choose No']
            },
            series: [
                {
                    name: '问题选择',
                    type: 'pie',
                    radius: '55%',
                    center: ['50%','60%'],
                    data: [
                        {value: data.yesnumber, name:'Number of people who choose YES'},
                        {value:data.nonumber, name:'Number of people who choose No'}
                        // {value: 0.64, name:'Number of people who choose YES'},
                        // {value:0.36, name:'Number of people who choose No'}
                    ]
                }
            ],
            itemStyle: {
                emphasis: {
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
            }
        };
        chart.setOption(option);
    }
</script>
</html>