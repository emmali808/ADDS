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
  <jsp:include page="patientTop.jsp" flush="true"/>
</div>
<form action="<%=basePath%>patient/update" method="post" >
<div class="content-wrapper">
  <section class="form-wrapper">
    <div class="section-patient-title" style="flex-basis:200px">
      <span>我的信息</span>
    </div>

    <div class="form-content"  style="text-align: center" >
      <table style="border-collapse:separate; margin: 0 auto;width: 40%" >
        <tr>
          <td>姓名: </td>
          <td><input class="sys-input" id="username" type="text" name="username" placeholder="${user.username }"><br></td>
        </tr>
        <tr>
          <td>身份证号: </td>
          <td>${user.identityID }</td>
        </tr>
        <tr>
          <td>年龄: </td>
          <td><input class="sys-input" id="age" type="text" name="age" placeholder="${user.age }"></td>
        </tr>
        <tr>
          <td>性别: </td>
          <td>
            <select class="info-select" name="gender">
            <option value="m">男</option>
            <option value="f">女</option>
          </select>
          </td>
        </tr>
        <tr>
          <td>电话: </td>
          <td><input class="sys-input" id="phone" type="text" name="phone" placeholder="${user.phone }"></td>
        </tr>
        <tr>
          <td>邮件: </td>
          <td><input class="sys-input" id="email" type="text" name="email" placeholder="${user.email }"></td>
        </tr>
          <tr>
            <td>出生年月: </td>
            <td><input class="sys-input" id="birthday" type="text" name="birthday" placeholder="${patient.birthday }"></td>
          </tr>
          <tr>
            <td>婚姻情况: </td>
            <td>
              <select  class="info-select" name="marriage">
                <option value="0">未婚</option>
                <option value="1">已婚</option>
                <option value="2">离异</option>
                <option value="3">丧偶</option>
              </select>
            </td>

          </tr>
          <tr>
            <td>民族: </td>
            <td><input class="sys-input" id="nation" type="text" name="nation" placeholder="${patient.nation }"></td>
          </tr>
          <tr>
            <td>出生地: </td>
            <td><input class="sys-input" id="birth_place" type="text" name="birth_place" placeholder="${patient.birth_place}"></td>
          </tr>

          <tr>
            <td>工作单位: </td>
            <td><input class="sys-input" id="work_place" type="text" name="work_place" placeholder="${patient.work_place}"></td>
          </tr>
          <tr>
            <td>联系人: </td>
            <td><input class="sys-input" id="contact_person" type="text" name="contact_person" placeholder="${patient.contact_person }"></td>
          </tr>
          <tr>
            <td>联系人电话: </td>
            <td><input class="sys-input" id="contact_phone" type="text" name="contact_phone" placeholder="${patient.contact_phone }"></td>
          </tr>
          <tr>
            <td>与联系人关系</td>
            <td><input class="sys-input" id="contact_relationship" type="text" name="contact_relationship" placeholder="${patient.contact_relationship}"></td>
          </tr>
          <tr>
            <td>联系人地址</td>
            <td><input class="sys-input" id="contact_address" type="text" name="contact_address" placeholder="${patient.contact_address }"></td>
          </tr>
          <tr>
            <td>医保类别</td>
            <td><input class="sys-input" id="category" type="text" name="category" placeholder="${patient.category }"></td>
          </tr>

          <tr>
            <td>医保卡号</td>
            <td><input class="sys-input" id="medicare_card_id" type="text" name="medicare_card_id" placeholder="${patient.medicare_card_id }"></td>
          </tr>
      </table>
      <div style="margin-top: 20px;">
        <input   class="common-submit form-submit" type="submit" value="提交">
      </div>
    </div>
  </section>
</div>
</form>
<footer class="footer">
  <div class="copyright">@Copyright XMU Lab</div>
</footer>
</body>
</html>