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
  <title>主页面</title>
    <link rel="stylesheet" href="<%=path%>/css/admin.css"/>
  <script type="text/javascript" src="<%=path%>/js/jquery.min.js"></script>
  <script type="text/javascript">
    $(document).ready(function(){
    	$("#addHospitalDepartment").click(function(){
            $("#hdlist").css('display','none');
            $('#add_wrapper').css('display','block');
            $('#hd_add').css('display','block');
            $('#tip').css('visibility','hidden');
        });
    	
    	$("#searchHospitalDepartment").click(function(){
    		$.ajax({
    			url:"<%=basePath%>hospitalDepartment/listAll3",
    			type:'get',
    			data:{},
    			success:function(result){
                    $('#add_wrapper').css('display','block');
                    $("#hdlist").text("");
                    $('#hd_add').css('display','none');
                    $("#hdlist").css('display','block');
                   if(result!=null){
                	   for(var i=0;i<result.length;i++){
                		   $("#hdlist").append(result[i].name+"---"+result[i].description+"----"+result[i].remark+"<br>");
                	   }
                   }
    				
    			},
    			error:function(){
    				
    			}
    			
    		});
    	});
    });
    
    function cancelAddHospitalDepartment(){
        $('#add_wrapper').css('display','none');
    	document.getElementById('hd_add').style.display='none';
    }
    function addHospitalDepartment1(){
     	var hdname=encodeURI($("#hdName").val());
     	var hddescription=encodeURI($("#description").val());
     	var hdremark=encodeURI($("#remark").val());
        if(!hdname || hdname==='') {
            $('#tip').css('visibility','visible');
            return ;
        }
     	 var hd={
     			name:hdname,
     			description:hddescription,
     			remark:hdremark
     	 }; 
     	 var data=encodeURI(JSON.stringify(hd));

     	$.ajax({
     		url:"<%=basePath%>hospitalDepartment/add",
     		type:"POST",
     		/* data:JSON.stringify(data), */
     		data:"orderJson="+data,
     		dataType:"json",
     		success:function(data){
     			if(data.msg==-1){
     				
     			}else if(data.msg!=0){
     				alert('提示:新增科室成功！');
     				document.getElementById('hd_add').style.display='none';
     			}
     		},
     		error:function(){
     			alert('提示新增科室失败！');
     		}
     	});
    }
    
  </script>
</head>
<body>
<div id="top">
  <jsp:include page="adminTop.jsp" flush="true"/>
</div>
<div class="content-wrapper">
    <div class="admin-row">
        <button class="sys-input" style="width: 120px" id="addHospitalDepartment" type="button">增加科室</button>
        <input  class="sys-input"  style="width: 120px" id="searchHospitalDepartment" type="button" value="查询科室" />
    </div>
    <div id="add_wrapper" class="box admin-content" style="display: none">
        <div id="hd_add" style="display:none">
            <form id="addHospitalDepartmentForm" method="post">
                <div id="hospitalDepartmentInfo" >
                    <table style="width: 100%;border-collapse: separate;border-spacing: 5px">
                        <tr>
                            <td style="text-align: right"><label style="color:red">*</label>科室名: </td>
                            <td><input class="sys-input" type="text" name="hdName" id="hdName" data-options="width:210,prompt:'必填'"></td>
                        </tr>
                        <tr>
                            <td style="text-align: right">科室描述: </td>
                            <td><input class="sys-input" type="text" name="description" id="description"></td>
                        </tr>
                        <tr>
                            <td style="text-align: right">备注: </td>
                            <td><input class="sys-input" type="text" name="remark" id="remark"></td>
                        </tr>
                        <tr id="tip" style="visibility: hidden">
                            <td></td>
                            <td style="color: red">请填写科室名</td>
                        </tr>
                    </table>
                    <div class="admin-row" style="width: 300px;margin: 0 auto;margin-top: 20px">
                        <button type="button" class="sys-input"  style="width: 80px" id="savebtn" onclick="addHospitalDepartment1()" >确定</button>
                        <button type="button" class="sys-input" style="width: 80px" onclick="cancelAddHospitalDepartment()">取消</button>
                    </div>
                </div>
            </form>
        </div>
        <div id="hdlist" style="display: none"></div>
    </div>   
</div>
<div>
  <jsp:include page="footer.jsp" flush="true"/>
</div>
</body>
</html>