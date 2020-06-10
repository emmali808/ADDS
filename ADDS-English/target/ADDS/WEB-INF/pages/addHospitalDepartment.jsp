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
  <link rel="stylesheet" href="<%=path%>/css/reset.css"/>
  <link rel="stylesheet" href="<%=path%>/css/style.css"/>
  <script type="text/javascript" src="<%=path%>/js/jquery.min.js"></script>
</head>
<body>
<div id="top">
  <nav class="nav">
    <div>
      <jsp:include page="top.jsp" flush="true"/>
    </div>
        
    <div class="nav-wrapper clearfix">
      <div style="width: 1080px;margin: 0 auto">
        <div class="n-list">
          <ul>
            <li><a href="<%=basePath%>user/index">首页</a></li>
            <li><a  href="<%=basePath%>user/allUser">所有用户</a></li>
            <li><a  href="<%=basePath%>patient/clinicRecord">患者</a></li>
            <li><a  href="<%=basePath%>patient/ask">&nbsp;医生  </a></li>
            <li><a  href="<%=basePath%>patient/ask">&nbsp;专家  </a></li>
            <li><a  href="<%=basePath%>patient/ask">&nbsp;问答  </a></li>
            <li><a  href="<%=basePath%>patient/account">&nbsp;个人信息</a></li>
            <li><a  href="<%=basePath%>hospitalDepartment/manage">&nbsp;科室管理</a></li>
          </ul>
        </div>
        <div class="n-search">
          <input type="text">
          <i class="iconfont">&#xe600</i>
        </div>
      </div>
    </div>
  </nav>
</div>
<div class="content-wrapper">

    <div>新增科室</div>
    <form id="addHospitalDepartmentForm" action="<%=basePath%>hospitalDepartment/add" method="post" style="width:500px;margin:2% auto;">
       <div id="hospitalDepartmentInfo" style="height:450px;width:500px;">
         <div style="padding-top:30px;margin:0 auto;width:400px">
           <table>
             <tr>
               <td style="text-align: right">科室名<label style="color:red">*</label></td>
               <td><input type="text" name="hdName" id="hdName" data-options="width:210,prompt:'必填'"></td>
             </tr>
             <tr>
               <td style="text-align: right">科室描述</td>
               <td><input type="text" name="description" id="description"></td>
             </tr>
             <tr>
               <td style="text-align: right">备注</td>
               <td><input type="text" name="remark" id="remark"></td>
             </tr>
             <tr>
               <td><input type="submit" value="确定" /></td>
                <td><input type="reset" value="重置" /></td>
             </tr>
           </table>          
         </div>
       </div>
    </form>
</div>

<div>
  <jsp:include page="footer.jsp" flush="true"/>
</div>
</body>
</html>