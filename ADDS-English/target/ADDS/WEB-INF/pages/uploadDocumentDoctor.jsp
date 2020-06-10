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
   <title>上传数据</title>
</head>
<body>
<div id="top">
    <jsp:include page="doctorTop.jsp" flush="true"/>
</div>
<div class="content-wrapper">
  <section class="form-wrapper">
    <div class="section-title">
      <%--<span>上传数据集</span>--%>
      <span>Upload medical archives</span>
    </div>
    <div class="form-content" id="app">
        <!--表格数据-->
        <div style="margin-top: 20px"></div>
        <div style="padding: 30px">
            <div>
                <%--<label for="name">数据集名称：</label>--%>
                <label for="name">Data name：</label>
                <input id="name" type="text"><br>
            </div>
            <div style="margin-top: 20px">
                <label for="type">Data category：</label>
                <select id="type" style="padding: 5px">
                    <%--<option value ="骨科">骨科</option>--%>
                    <%--<option value ="肝胆外科">肝胆外科</option>--%>
                    <option value ="骨科">Orthopaedic</option>
                    <option value ="肝胆外科">Hepatobiliary surgery</option>
                </select><br>
            </div>
            <div style="margin-top: 20px">
                <%--<label for="upload">Upload files：&nbsp;&nbsp;&nbsp;&nbsp;</label>--%>
                <%--<input id="upload" type="file"><br>--%>
                    <label>Upload files (*.zip)：&nbsp;&nbsp;&nbsp;&nbsp;</label>
                    <el-upload
                        style="display: inline-block"
                        class="upload-demo"
                        ref="uploadZip"
                        :auto-upload="false"
                        action="<%=basePath%>doctor/uploadZip">
                    <el-button size="small" type="primary">Browse</el-button>
                    <%--<div slot="tip" class="el-upload__tip">jpg/png files with a size less than 500kb</div>--%>
                    </el-upload>
            </div>
            <div style="margin-top: 20px">
                <%--<label for="t" style="vertical-align: top">数据集描述：</label>--%>
                <label for="t" style="vertical-align: top">Data description：</label>
                <textarea id="t" rows="4" cols="40" style="vertical-align: top"></textarea>
            </div>
            <div style="width: 100%;margin-left: 110px;margin-top: 50px">
                <el-button icon="el-icon-circle-check-outline" @click="submitUpload">Submit</el-button>
                <a href="<%=basePath%>doctor/documentList">
                    <el-button icon="el-icon-d-arrow-right">Back</el-button>
                </a>
            </div>


        </div>
        <div id="modal-overlay">
            <div class="modal-data">loading...</div>
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
<script src="https://apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js"></script>
<script>
    new Vue({
        el: '#app',
        created(){
            $('#modal-overlay').hide();
        },
        methods:{
            submitUpload:function () {
                $('#modal-overlay').show();
                this.$refs.uploadZip.submit();
                var loadingEle = document.getElementsByClassName('modal-data')[0];
                setTimeout(function () {
                    loadingEle.innerHTML = "上传中 ...";
                },0);
                setTimeout(function () {
                    loadingEle.innerHTML = "OCR 分析中 ...";
                    console.log(1);
                },1000);
                setTimeout(function () {
                    $('#modal-overlay').hide();
                    window.location.href = "<%=basePath%>doctor/documentList";
                },4000);
            }
        },
        data: function() {
            return {
                visible: false

            }
        }
    })
</script>
<style>
    #modal-overlay {
        position: absolute;   /* 使用绝对定位或固定定位  */
        left: 0;
        top: 0;
        width:100%;
        height:100%;
        text-align: center;
        z-index: 1000;
        background-color: rgba(255,255,255,0.9);
        opacity: 0.9;   /* 背景半透明 */
    }
    .modal-data{
        width: 300px;
        margin: 180px auto;
        background-color: #fff;
        padding: 15px;
        text-align: center;
    }
</style>
</html>
