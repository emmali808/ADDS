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

  <title>我的数据集</title>
</head>
<body>
<div id="top">
    <jsp:include page="doctorTop.jsp" flush="true"/>
</div>
<div class="content-wrapper">
  <section class="form-wrapper">
    <div class="section-title">
      <%--<span>我的数据集</span>--%>
      <span>My data</span>
    </div>
    <div class="form-content" id="app">
        <!--表格数据-->
        <div style="margin-top: 20px"></div>
        <div>
            <a href="<%=basePath%>doctor/uploadDocumentDoctor">
                <%--<el-button type="primary" icon="el-icon-circle-plus-outline">上传数据</el-button>--%>
                <el-button type="primary" icon="el-icon-circle-plus-outline">Upload medical archives</el-button>
            </a>
        </div>
        <div style="margin-top: 20px;">
            <el-table :data="tableData.slice((currentPage-1)*pageSize,currentPage*pageSize)"
                      max-height="410" height="410" fit border style="width: 100%;">
                <%--<el-table-column prop="title" label="标题" sortable></el-table-column>--%>
                <%--<el-table-column prop="category" label="类别" sortable></el-table-column>--%>
                <el-table-column prop="title" label="Title" sortable></el-table-column>
                <el-table-column prop="category" label="Category" sortable></el-table-column>
                <el-table-column prop="status" label="Status" align="center" width="150px" sortable>
                    <template slot-scope="scope">
                        <el-tag
                                :type="scope.row.statusType"
                                disable-transitions>{{scope.row.statusText}}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="Options" align="center" width="150px" fixed="right">
                    <template slot-scope="scope">
                        <el-button type="primary" size="small" :disabled="scope.row.downloadStatus">Download</el-button>
                    </template>
                </el-table-column>
            </el-table>

            <div style="margin-top: 20px;margin-left: auto;margin-right: auto;text-align: center;width:100%">
                <el-pagination
                        :total="totalSize"
                        @current-change="currentChange"
                        :page-size="pageSize"
                        layout="prev, pager, next">
                </el-pagination>
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
            //换页触发的函数
            currentChange:function(currentPage){
                this.currentPage = currentPage;
            },
        },
        data: function() {
            return {
                visible: false,
                totalSize:9,
                pageSize:6,
                currentPage:1,
                tableData:[
                    {
                        title:"Orthopaedic dataset1",
                        category:"Orthopaedic",
                        state:"已处理",
                        statusType:"success",
                        statusText:"Processed",
                        downloadStatus:false
                    },
                    {
                        title:"Orthopaedic dataset2",
                        category:"Orthopaedic",
                        state:"已处理",
                        statusType:"success",
                        statusText:"Processed",
                        downloadStatus:false
                    },
                    {
                        title:"Orthopaedic dataset3",
                        category:"Orthopaedic",
                        state:"未处理",
                        statusType:"danger",
                        statusText:"Processing",
                        downloadStatus:true
                    },
                    {
                        title:"Hepatobiliary surgery dataset1",
                        category:"Hepatobiliary surgery",
                        state:"已处理",
                        statusType:"success",
                        statusText:"Processed",
                        downloadStatus:false
                    },
                    {
                        title:"Hepatobiliary surgery dataset2",
                        category:"Hepatobiliary surgery",
                        state:"未处理",
                        statusType:"danger",
                        statusText:"Processing",
                        downloadStatus:true
                    },
                    {
                        title:"Hepatobiliary surgery dataset3",
                        category:"Hepatobiliary surgery",
                        state:"未处理",
                        statusType:"danger",
                        statusText:"Processing",
                        downloadStatus:true
                    },
                    {
                        title:"Hepatobiliary surgery dataset4",
                        category:"Hepatobiliary surgery",
                        state:"未处理",
                        statusType:"danger",
                        statusText:"Processing",
                        downloadStatus:true
                    },
                    {
                        title:"Hepatobiliary surgery dataset5",
                        category:"Hepatobiliary surgery",
                        state:"未处理",
                        statusType:"danger",
                        statusText:"Processing",
                        downloadStatus:true
                    },
                    {
                        title:"Hepatobiliary surgery dataset6",
                        category:"Hepatobiliary surgery",
                        state:"未处理",
                        statusType:"danger",
                        statusText:"Processing",
                        downloadStatus:true
                    }

                ]
            }
        }
    })
</script>

</html>
