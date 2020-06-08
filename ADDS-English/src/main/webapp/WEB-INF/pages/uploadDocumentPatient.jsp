<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%
    String path=request.getContextPath();
    String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
   <link rel="stylesheet" href="https://unpkg.com/element-ui/lib/theme-chalk/index.css">
   <title>上传病例</title>
</head>
<body>
<div id="top">
    <jsp:include page="patientTop.jsp" flush="true"/>
</div>
<div class="content-wrapper">
  <section class="form-wrapper">
    <div class="section-title">
      <span>Automatic diagnosis</span>
      <%--<span>自动诊断</span>--%>
    </div>
    <div class="form-content" id="app">
        <!--表格数据-->
        <div style="margin-top: 20px">
            <el-tabs v-model="activeName2" type="card" stretch @tab-click="handleClick">
                <el-tab-pane label="Upload medical records" name="first"></el-tab-pane>
                <el-tab-pane label="Machine diagnosis" name="second"></el-tab-pane>
                <el-tab-pane label="Submit questions" name="third"></el-tab-pane>
                <%--<el-tab-pane label="上传病例" name="first"></el-tab-pane>--%>
                <%--<el-tab-pane label="机器诊断" name="second"></el-tab-pane>--%>
                <%--<el-tab-pane label="提交问题" name="third"></el-tab-pane>--%>
            </el-tabs>
        </div>
        <div style="padding: 30px">
            <form action="<%=basePath%>patient/uploadCaseHistory" method="post" enctype="multipart/form-data">
                <div style="margin-top: 20px">
                    <label>File Upload：</label>
                    <%--<input id="upload" type="file" name="file"><br>--%>
                    <el-button type="primary">Browse</el-button>
                </div>
                <div style="margin-top: 20px">
                    <label for="t" style="vertical-align: top">description：</label>
                    <textarea id="t" rows="4" cols="40" style="vertical-align: top"></textarea>
                </div>
                <div style="width: 100%;margin-left:250px;margin-top: 50px">
                    <el-button type="primary" native-type="submit" icon="el-icon-circle-check-outline">Submit</el-button>
                </div>
            </form>
        </div>

    </div>
   </section>
 </div>
<div id="footer">
  <jsp:include page="footer.jsp" flush="true"/>
</div>
</body>

<!-- import Vue before Element -->
<script src="https://unpkg.com/vue/dist/vue.js"></script>
<!-- import JavaScript -->
<script src="https://unpkg.com/element-ui/lib/index.js"></script>
<script>
    new Vue({
        el: '#app',
        methods:{
            handleClick(tab,event){
                if(tab.name=="second"){
                    window.location.href="<%=basePath%>patient/uploadGraphPatient";
                }
                if(tab.name=="third"){
                    window.location.href="<%=basePath%>patient/uploadQuestion";
                }
            }
        },
        data: function() {
            return {
                visible: false,
                activeName2: 'first'
            }
        }
    })
</script>

</html>
