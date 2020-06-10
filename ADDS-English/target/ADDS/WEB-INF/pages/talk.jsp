<%@ page language="java" contentType="text/html; charset=utf-8" import="cn.medicine.pojo.User,java.util.*"
         pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
  String path=request.getContextPath();
  String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
  String basePath2 = request.getServerName() + ":"+ request.getServerPort() + path + "/";
  long to;
%>
<!DOCTYPE html>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>在线交流</title>
  <link rel="stylesheet" href="<%=path%>/css/talk.css">
  <script type="text/javascript" src="<%=path%>/js/jquery.min.js"></script>
</head>
		<script type="text/javascript">
		  $(document).ready(function(){			  			 													  
			  $("#selectUser").click(function(){
				  var selectid=$("#toUser").val();
				  var selecttext=$("#toUser").text();
				  to=selectid;
				  alert("选中 "+selecttext+" 用户");
			  });
		  });
		  var path='<%=basePath2%>';
		  
		  var uid=${user.id};
			var from=uid;
			var fromName="${user.username}";
		    to=-1;//默认广播
					
			var websocket;
			if ('WebSocket' in window) {
				websocket = new WebSocket("ws://" + path + "/ws?uid="+uid);
			} else if ('MozWebSocket' in window) {
				websocket = new MozWebSocket("ws://" + path + "/ws"+uid);
			} else {
				websocket = new SockJS("http://" + path + "/ws/sockjs"+uid);
			}
			websocket.onopen = function(event) {
				console.log("WebSocket:已连接");
				console.log(event);
			};
			websocket.onmessage = function(event) {
				var data=JSON.parse(event.data);
				console.log("WebSocket:收到一条消息",data);
				var textCss=data.from==-1?"sfmsg_text":"fmsg_text";
				if(data.from==-1&&data.text=='newuser'){
					updateOnlineUsers();
				}else{
					$("#content").append("<div class='talk-content-row doctor-row'> <i class='talk-person doctor'></i><span class='talk-icon talk-icon-lm' ></span><div class='talk-icon-cm'><p>"+data.text+"</p></div></div>");
					
					to=data.from;
					
					scrollToBottom();
				};							
			};
			websocket.onerror = function(event) {
				console.log("WebSocket:发生错误 ");
				console.log(event);
			};
			websocket.onclose = function(event) {
				console.log("WebSocket:已关闭");
				console.log(event);
			}
				
				
				Date.prototype.Format = function (fmt) { //author: meizz 
				    var o = {
				        "M+": this.getMonth() + 1, //月份 
				        "d+": this.getDate(), //日 
				        "h+": this.getHours(), //小时 
				        "m+": this.getMinutes(), //分 
				        "s+": this.getSeconds(), //秒 
				        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
				        "S": this.getMilliseconds() //毫秒 
				    };
				    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
				    for (var k in o)
				    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
				    return fmt;
				}
		  function sendMsg(){
				var v=$("#msg").val();
				if(v==""){
					return;
				}else{					
					var data={};
					data["from"]=from;
					data["fromName"]=fromName;
					data["to"]=to;
					data["text"]=v;
					websocket.send(JSON.stringify(data));
					$("#content").append("<div class='talk-content-row patient-row'><div class='talk-icon-cm-patient'><p>"+data.text+"</p></div><span class='talk-icon talk-icon-lm-patient' ></span> <i class='talk-person patient'></i></div>");
					scrollToBottom();
					$("#msg").val("");
				}
			}
			
		function scrollToBottom(){
				var div = document.getElementById('content');
				div.scrollTop = div.scrollHeight;
			}
		  function send(event){
				var code;
				 if(window.event){
					 code = window.event.keyCode; // IE
				 }else{
					 code = e.which; // Firefox
				 }
				if(code==13){ 
					sendMsg();            
				}
			}
			
			function clearAll(){
				$("#content").empty();
			}
			function updateOnlineUsers(){
				$.ajax({
		     		url:"<%=basePath%>user/updateOnlineUsers",
		     		type:"GET",
		     		data:{},
		     		dataType:"json",
		     		success:function(data){
		     			 console.log(data.msg);
		                if(data.msg==1){
		                	var str="";
		                	var onlineusers=data.onlineusers;
		                	for(var i=0;i<onlineusers.length;i++){
		                		str+="<option value='"+onlineusers[i].id+"'>"+onlineusers[i].username+"</option>";
		                	}
		                	
		                    $("#toUser").html(str);
		                    console.log("更新在线用户");
		                }
		     		},
		     		error:function(){

		     		}
		     	});
			}
		</script>
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
  <section class="container">
    <div class="box section-qa-content" >
		<div class="section-qa-row">
			<label>Choose a person to communicate：</label>
			<select name="toUser" id="toUser">
				<c:forEach items="${onlineusers}" var="onlineuser">
					<option value="${onlineuser.id}">${onlineuser.username}</option>
				</c:forEach>
			</select>
			<button style="margin-left: 10px;width: 100px;" class="sys-input" id="selectUser">Submit</button>
		</div>
    </div>
  </section>
  <div class="talk-container">
	  <div class="talk-bd-left">
		  <div id="content" class="talk-content"></div>
		  <div>
			  <input style="width: 100%;box-sizing: border-box" type="text" placeholder="Please enter the message here" id="msg" class="sys-input " onkeydown="send(event)">
		  </div>
		  <div class="talk-submit">
			  <input type="button" value="send" class="send btn-talk btn-send" onclick="sendMsg()" >
			  <input type="button" value="clear" class="clear btn-talk btn-close" onclick="clearAll()">
		  </div>
	  </div>
	  <div class="talk-bg-right">
		  <div class="doctor-image doctor"></div>
		  <h1>Yadi Li</h1>
		  <%--<p>中山大学硕士研究生导师,广州市红十字会医院心血管内科主任</p>--%>
		  <%--<p>擅长内科疾病的诊断与治疗，对心血管内科疑难杂症有独到见解及创新有效方法，对高血压、冠心病心肌梗死、心绞痛、心源性休克、急性与慢性心力衰竭，心肌炎和心肌病，风湿性心脏病、肺栓塞、心律失常、周围血管病等常见心血管病和心血管疑难杂症的诊疗、危重病人的抢救均具有丰富的临床经验</p>--%>
          <p>&nbsp;&nbsp;&nbsp;&nbsp;Yadi Li is currently an attending doctor in the department of dermatology, Beijing Tongren hospital.
              She received her M.D. in dermatology from Capital Medical University in 2012. Her sub-specialties are skin cosmetic medicine and dermatology surgery.</p>
	  </div>
  </div>
</div>
<div id="top">
  <jsp:include page="footer.jsp" flush="true"/>
</div>
</body>
</html>