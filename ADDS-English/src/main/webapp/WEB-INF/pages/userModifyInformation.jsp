<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="<%=path%>/css/bootstrap.min.css" rel="stylesheet" media="screen">
    <title>修改用户信息</title>
    <link rel="stylesheet" href="<%=path%>/css/style.css"/>
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

<div class="content-wrapper">
    <section class="form-wrapper">
        <div class="section-title" style="flex-basis: 200px">
            <span>我的信息</span>
        </div>
        <div class="form-content">
            <form action="<%=basePath%>user/update" method="post">
                <table style="margin: 0 auto;border-collapse:separate;border-spacing: 10px;">
                    <tr>
                        <td>姓名：</td>
                        <td> &nbsp;</td>
                        <td><input class="sys-input" id="username" type="text" name="username"
                                   placeholder="${user.username }"><br></td>
                    </tr>
                    <tr>
                        <td>身份证号</td>
                        <td>&nbsp;</td>
                        <td><input class="sys-input" id="identityID" type="text" name="identityID"
                                   placeholder="${user.identityID }"></td>
                    </tr>
                    <tr>
                        <td>年龄</td>
                        <td> &nbsp;</td>
                        <td><input class="form-control" id="age" type="text" name="age" placeholder="${user.age }"></td>
                    </tr>
                    <tr>
                        <td>性别</td>
                        <td>&nbsp; </td>
                        <td><select style="width: 100%" name="gender">
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
                        <td></td>
                        <td><input class="sys-input" id="phone" type="text" name="phone" placeholder="${user.phone }">
                        </td>
                    </tr>
                    <tr>
                        <td>邮件</td>
                        <td></td>
                        <td><input class="sys-input" id="email" type="text" name="email" placeholder="${user.email }">
                        </td>
                    </tr>
                </table>
                <div style="text-align: center">
                    <input style="width: 100px;margin:20px 0px" class="sys-input" type="submit" value="提交">
                </div>
            </form>
        </div>
    </section>

</div>

<div id="footer">
    <jsp:include page="footer.jsp" flush="true"/>
</div>
</body>
</html>