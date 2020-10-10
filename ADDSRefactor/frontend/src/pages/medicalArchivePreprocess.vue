<template>
  <div>
    <el-container>
      <NavigationBar/>
      <el-container class="content-div">

        <el-header>
          <el-button type="primary" @click="openUploadDialog">Upload Medical Archive</el-button>
        </el-header>

        <el-main>
          <el-table
              :data="tableData" height="500" style="width: 100%;">
            <el-table-column prop="title" label="Title" align="center"></el-table-column>
            <el-table-column prop="category" label="Category" align="center"></el-table-column>
            <el-table-column label="Status" align="center">
              <template slot-scope="scope">
                <el-tag
                    :type="scope.row.finished ? 'success' : 'danger'"
                    disable-transitions>{{scope.row.finished ? 'Processed' : 'Processing'}}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="Options" align="center">
              <template slot-scope="scope">
                <el-button type="primary" size="small" :disabled="!scope.row.finished">Download</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-main>

        <el-dialog
            title="Upload Medical Archive"
            :visible.sync="uploadFormVisible"
            width="640px"
            append-to-body
            :close-on-click-modal="false"
            :close-on-press-escape="false"
            :show-close="false">
          <el-form ref="uploadForm" :model="uploadForm" :rules="rules" label-position="left" label-width="130px">
            <el-form-item label="Name" prop="name">
              <el-input v-model="uploadForm.name"></el-input>
            </el-form-item>
            <el-form-item label="Description" prop="desc">
              <el-input type="textarea" :rows="5" v-model="uploadForm.desc"></el-input>
            </el-form-item>
          </el-form>
          <div slot="footer">
            <el-button type="primary" @click="upload">Submit</el-button>
            <el-button @click="cancelUpload">Cancel</el-button>
          </div>
        </el-dialog>

      </el-container>
    </el-container>
  </div>
</template>

<script>
    import NavigationBar from "../components/NavigationBar";
    export default {
        name: "medicalArchivePreprocess",
        components: {
            NavigationBar
        },
        methods:{
          openUploadDialog() {
            this.uploadFormVisible = true;
          },
          upload() {
            this.uploadFormVisible = false;
          },
          cancelUpload() {
            this.uploadFormVisible = false;
          }
        },
        data: function() {
            return {
                uploadFormVisible: false,
                uploadForm: {
                    name: '',
                    // category: '',
                    desc: '',
                },
                rules: {
                    name: [
                        {required: true, message: 'Please input name', trigger: 'blur'}
                        // {min: 1, max: 10, message: 'Name length is between 1 and 10', trigger: 'blur'}
                    ],
                    desc: [
                        {required: true, message: 'Please input description', trigger: 'blur'}
                    ]
                },
                tableData:[
                    {
                        title:"Orthopaedic dataset1",
                        category:"Orthopaedic",
                        finished:true
                    },
                    {
                        title:"Orthopaedic dataset2",
                        category:"Orthopaedic",
                        finished:true
                    },
                    {
                        title:"Orthopaedic dataset3",
                        category:"Orthopaedic",
                        finished:false
                    },
                    {
                        title:"Hepatobiliary surgery dataset1",
                        category:"Hepatobiliary surgery",
                        finished:true
                    },
                    {
                        title:"Hepatobiliary surgery dataset2",
                        category:"Hepatobiliary surgery",
                        finished:false
                    },
                    {
                        title:"Hepatobiliary surgery dataset3",
                        category:"Hepatobiliary surgery",
                        finished:false
                    },
                    {
                        title:"Hepatobiliary surgery dataset4",
                        category:"Hepatobiliary surgery",
                        finished:false
                    },
                    {
                        title:"Hepatobiliary surgery dataset5",
                        category:"Hepatobiliary surgery",
                        finished:false
                    },
                    {
                        title:"Hepatobiliary surgery dataset6",
                        category:"Hepatobiliary surgery",
                        finished:false
                    }

                ]
            }
        }
    }
</script>

<style scoped>
  .content-div {
    position: relative;
    top: 110px;
  }

  #paginator {
    text-align: center;
  }
</style>
