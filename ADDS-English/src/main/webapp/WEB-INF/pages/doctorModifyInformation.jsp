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
  <title>修改用户信息</title>
  <link rel="stylesheet" href="<%=path%>/css/reset.css"/>
  <link rel="stylesheet" href="<%=path%>/css/style.css"/>


</head>
<body>
<div id="top">
  <jsp:include page="doctorTop.jsp" flush="true"/>
</div>
<form action="<%=basePath%>doctor/update" method="post" >
  <div class="content-wrapper">
    <section class="form-wrapper">
      <div class="section-patient-title" style="flex-basis:200px">
        <span>我的信息</span>
      </div>

      <div class="form-content"  style="text-align: center" >
        <table style="border-collapse:separate; margin: 0 auto;width: 40%" >
        <%--    <tr>
             <td>ID</td>
             <td> </td>
             <td>${user.uid }</td>
           </tr> --%>
        <tr>
          <td>姓名</td>
          <td> &nbsp;</td>
          <td><input id="username" type="text" name="username" placeholder="${user.username }"><br></td>
        </tr>
        <tr>
          <td>身份证号</td>
          <td>&nbsp;</td>
          <td>${user.identityID }</td>
        </tr>
        <tr>
          <td>年龄</td>
          <td> &nbsp;</td>
          <td><input id="age" type="text" name="age" placeholder="${user.age }"></td>
        </tr>
        <tr>
          <td>性别</td>
          <td>&nbsp; </td>
          <td><select name="gender">
            <option value="m">男</option>
            <option value="f">女</option>
          </select>
          </td>
        </tr>
        <tr>
          <td>电话</td>
          <td> </td>
          <td><input id="phone" type="text" name="phone" placeholder="${user.phone }"></td>
        </tr>



        <tr>
          <td>邮件</td>
          <td> </td>
          <td><input id="email" type="text" name="email" placeholder="${user.email }"></td>
        </tr>
          <tr>
            <td>出生年月</td>
            <td> </td>
            <td><input id="birthday" type="text" name="birthday" placeholder="${doctor.birthday }"></td>
          </tr>

          <tr>
            <td>婚姻情况</td>
            <td> </td>
            <td>
              <select name="marriage">
                <option value="0">未婚</option>
                <option value="1">已婚</option>
                <option value="2">离异</option>
                <option value="3">丧偶</option>
              </select>
            </td>

          </tr>
          <tr>
            <td>民族</td>
            <td> </td>
            <td><input id="nation" type="text" name="nation" placeholder="${doctor.nation }"></td>
          </tr>
          <tr>
            <td>出生地</td>
            <td> </td>
            <td><input id="birth_place" type="text" name="birth_place" placeholder="${doctor.birth_place}"></td>
          </tr>

          <tr>
            <td>工作单位</td>
            <td> </td>
            <td><input id="work_place" type="text" name="work_place" placeholder="${doctor.work_place}"></td>
          </tr>
         <tr>
            <td>入职时间</td>
            <td> </td>
            <td><input id="entry_time" type="text" name="entry_time" placeholder="${doctor.entry_time }"></td>
          </tr>
          <tr>
            <td>所属科室</td>
            <td> </td>
            <td><input id="department" type="text" name="department" placeholder="${doctor.department }"></td>
          </tr>

          <tr>
            <td>职务</td>
            <td> </td>
            <td><input id="duty" type="text" name="duty" placeholder="${doctor.duty}"></td>
          </tr>
          <tr>
            <td>职称</td>
            <td> </td>
            <td><input id="title" type="text" name="title" placeholder="${doctor.title }"></td>
          </tr>
          <tr>
            <td>擅长方向</td>
            <td> </td>
            <td><input id="skill" type="text" name="skill" placeholder="${doctor.skill }"></td>
          </tr>

          <tr>
            <td>门诊时间</td>
            <td> </td>
            <td><input id="outpatient_time" type="text" name="outpatient_time" placeholder="${doctor.outpatient_time }"></td>
          </tr>
          <tr>
            <td>其他介绍</td>
            <td> </td>
            <td><input id="introduction" type="text" name="introduction" placeholder="${doctor.introduction }"></td>
          </tr>
      </table>
        <div style="margin-top: 20px;">
          <input   class="common-submit form-submit" type="submit" value="提交">
        </div>
    </div>
  </section>

</div>
</form>
<div id="footer">
  <jsp:include page="footer.jsp" flush="true"/>
</div>
</body>
</html>