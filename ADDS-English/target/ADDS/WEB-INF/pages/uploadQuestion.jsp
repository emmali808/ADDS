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
   <title>提出问题</title>
</head>
<body>
<div id="top">
    <jsp:include page="patientTop.jsp" flush="true"/>
</div>
<div class="content-wrapper">
  <section class="form-wrapper">
    <div class="section-title">
      <span>Automatic diagnosis</span>
    </div>
    <div class="form-content" id="app">
        <!--表格数据-->
        <div style="margin-top: 20px">
            <el-tabs v-model="activeName2" type="card" stretch @tab-click="handleClick">
                <%--<el-tab-pane label="上传病例" name="first"></el-tab-pane>--%>
                <%--<el-tab-pane label="机器诊断" name="second"></el-tab-pane>--%>
                <%--<el-tab-pane label="提交问题" name="third"></el-tab-pane>--%>
                    <el-tab-pane label="Upload medical records" name="first"></el-tab-pane>
                    <el-tab-pane label="Machine diagnosis" name="second"></el-tab-pane>
                    <el-tab-pane label="Submit questions" name="third"></el-tab-pane>
            </el-tabs>
        </div>
        <div style="padding: 30px">
            <div style="margin-top: 20px">
                <label for="t" style="vertical-align: top"><strong>Question description：</strong></label>
                <textarea id="t" rows="4" cols="50" style="vertical-align: top"></textarea>
            </div>
            <%--<p style="font-size: 16px;margin-top: 8px">注：问题应该是可以用"是"或"否"回答的问题，若想进一步咨询，可以进入--%>
                <%--<span style="color: #409EFF"><a href="<%=basePath%>/patient/talk" style="text-decoration-line: underline">在线交流</a></span>--%>
            <%--</p>--%>
            <p style="font-size: 16px;margin-top: 15px"><strong>Note：</strong>Please submit a simple yes or no question. You also can contact the doctors using <span style="color: #409EFF"><a href="<%=basePath%>/patient/talk" style="text-decoration-line: underline">Online system</a></span></p>
            <div style="width: 100%;margin-left:250px;margin-top: 50px">
                <el-button type="primary" icon="el-icon-circle-check-outline">Submit</el-button>
            </div>
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
                if(tab.name=="first"){
                    window.location.href="<%=basePath%>patient/uploadCase";
                }
                if(tab.name=="second"){
                    window.location.href="<%=basePath%>patient/uploadGraphPatient";
                }
            }
        },
        data: function() {
            return {
                visible: false,
                activeName2: 'third'
            }
        }
    })
</script>

</html>
