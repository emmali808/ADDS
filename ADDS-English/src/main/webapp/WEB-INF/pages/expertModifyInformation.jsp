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
  <title>修改用户信息</title>
  <link rel="stylesheet" href="<%=path%>/css/reset.css"/>
  <link rel="stylesheet" href="<%=path%>/css/style.css"/>
</head>
<body>
<div id="top">
  <jsp:include page="expertTop.jsp" flush="true"/>
</div>
<form action="<%=basePath%>expert/update" method="post" >
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
          <td>用户名</td>
          <td> &nbsp;</td>
          <td><input class="sys-input" id="username"   type="text" name="username" placeholder="${user.username }"><br></td>
        </tr>
        <tr>
          <td>身份证号</td>
          <td>&nbsp;</td>
          <td>${user.identityID }</td>
        </tr>
        <tr>
          <td>年龄</td>
          <td> &nbsp;</td>
          <td><input class="sys-input" id="age"  type="text" name="age" placeholder="${user.age }"></td>
        </tr>
        <tr>
          <td>性别</td>
          <td>&nbsp; </td>
          <td>
          <select class="sys-input"  name="gender">
            <c:choose>
              <c:when test="${user.gender.equals('m')}">
                <option value="m" selected="selected">男</option>
                <option value="f">女</option>
              </c:when>
              <c:otherwise>
                <option value="m">男</option>
                <option value="f" selected="selected">女</option>
              </c:otherwise>
            </c:choose>            
          </select>
          </td>
        </tr>
        <tr>
          <td>电话</td>
          <td> </td>
          <td><input class="sys-input"  id="phone"  type="text" name="phone" placeholder="${user.phone }"></td>
        </tr>
        <tr>
          <td>邮件</td>
          <td> </td>
          <td><input class="sys-input"  id="email" type="text" name="email" placeholder="${user.email }"></td>
        </tr>
          <tr>
            <td>出生年月</td>
            <td> </td>
            <td><input  class="sys-input"  id="birthday"type="text" name="birthday" placeholder="${expert.birthday }"></td>
          </tr>
          <tr>
            <td>婚姻情况</td>
            <td> </td>
            <td>
            <c:choose>
              <c:when test="${expert.marriage.equals('0')}">
                <select name="marriage">
                  <option value="0" selected="selected">未婚</option>
                  <option value="1">已婚</option>
                  <option value="2">离异</option>
                  <option value="3">丧偶</option>
                </select>
              </c:when>
               <c:when test="${expert.marriage.equals('1')}">
                <select name="marriage">
                  <option value="0">未婚</option>
                  <option value="1" selected="selected">已婚</option>
                  <option value="2">离异</option>
                  <option value="3">丧偶</option>
                </select>
              </c:when>
               <c:when test="${expert.marriage.equals('2')}">
                <select name="marriage">
                  <option value="0">未婚</option>
                  <option value="1">已婚</option>
                  <option value="2" selected="selected">离异</option>
                  <option value="3">丧偶</option>
                </select>
              </c:when>
               <c:when test="${expert.marriage.equals('3')}">
                <select name="marriage">
                  <option value="0">未婚</option>
                  <option value="1">已婚</option>
                  <option value="2">离异</option>
                  <option value="3" selected="selected">丧偶</option>
                </select>
              </c:when>            
            </c:choose>               
            </td>

          </tr>
          <tr>
            <td>民族</td>
            <td> </td>
            <td><input  class="sys-input"  id="nation" type="text" name="nation" placeholder="${expert.nation }"></td>
          </tr>
          <tr>
            <td>出生地</td>
            <td> </td>
            <td><input  class="sys-input"  id="birth_place" type="text" name="birth_place" placeholder="${expert.birth_place}"></td>
          </tr>

          <tr>
            <td>工作单位</td>
            <td> </td>
            <td><input  class="sys-input"  id="work_place" type="text" name="work_place" placeholder="${expert.work_place}"></td>
          </tr>

          <tr>
            <td>职称</td>
            <td> </td>
            <td><input  class="sys-input"  id="title" type="text" name="title" placeholder="${expert.title }"></td>
          </tr>
          <tr>
            <td>擅长方向</td>
            <td> </td>
            <td><input  class="sys-input"  id="skill" type="text" name="skill" placeholder="${expert.skill }"></td>
          </tr>

          <tr>
            <td>学历</td>
            <td> </td>
            <td><input  class="sys-input"  id="education" type="text" name="education" placeholder="${expert.education }"></td>
          </tr>
          <tr>
            <td>其他介绍</td>
            <td> </td>
            <td><input  class="sys-input"  id="introduction" type="text" name="introduction" placeholder="${expert.introduction }"></td>
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