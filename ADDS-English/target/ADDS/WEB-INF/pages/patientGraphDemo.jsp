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
  <title>诊疗记录</title>
  <link rel="stylesheet" href="<%=path%>/css/reset.css"/>
  <link rel="stylesheet" href="<%=path%>/css/style.css"/>
</head>
<body>
<div id="top">
  <jsp:include page="patientTop.jsp" flush="true"/>
</div>
<div class="content-wrapper">
  <section>
    <div class="section-patient-content">

      <input type="text" style="display:none" value="${strall}"/>
      <div id="graph" style="display: none" data-nodes="${str1}"  ></div>
         ${str1}${str}

        </tr>
      </table>
      <form action="<%=basePath%>record/uploadClinic" method="post"
            enctype="multipart/form-data">
        <input type="file" name="file" /> <input
              type="submit" value="上传最新病例" />
      </form>
    </div>

  </section>
</div>
<footer class="footer">
  <div class="copyright">@Copyright XMU Lab</div>
</footer>
</body>
<script>
  $(function () {
    var nodes = $('#graph').data('nodes');
    nodes = nodes.replace(/=/g,':');
    console.log(nodes);
    nodes = JSON.parse(nodes);
    console.log(nodes);
  })
</script>
</html>