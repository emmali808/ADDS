<template>
    <el-container id="container" direction="vertical">


        <el-form v-if="this.$store.state.user.type == 3" ref="downloadForm2" :model="downloadForm2" label-position="left" label-width="170px">
            <el-form-item label="Sample File Format:">
                <el-button
                        type="primary"
                        size="small"
                        @click="downloadSampleCsvFileForCluster">
                    Download
                </el-button>
            </el-form-item>
        </el-form>

        <el-form v-if="this.$store.state.user.type == 3" ref="uploadForm2" :model="uploadForm2" label-position="left" label-width="170px">
            <el-form-item label="Medical Cases Upload:" prop="uploadFile">
                <el-upload
                        ref="uploadCsv"
                        action=""
                        accept=".csv"
                        :multiple="false"
                        :file-list="fileList2"
                        :show-file-list="true"
                        :http-request="uploadDataset"
                        :auto-upload="false">
                    <el-button slot="trigger" size="small" type="primary">Browse</el-button>
                </el-upload>
            </el-form-item>

            <el-form-item label="Diagnosis Number:">
                <el-input
                        style="width:80px;margin-right:9px"
                        type="text"
                        v-model="uploadForm2.cluster_num"
                        oninput="value=value.replace(/^\.+|[^\d.]/g,'')"
                        clearable>
                </el-input>

                <el-button size="small" type="primary" @click="uploadCsv">Submit</el-button>
<!--                yongzhiqian huida-->
<!--                <el-button size="small"  type="primary"  @click="myEcharts">Show</el-button>-->

<!--           <el-input type="textarea" :rows="6" v-model="uploadForm2.desc"></el-input>-->
            </el-form-item>

        </el-form>
        <div class="Echarts">
            <div id="main" style="width: 600px;height:400px;"></div>

            <el-form v-if="this.$store.state.user.type == 3" ref="downloadForm1" :model="downloadForm1" label-position="left" label-width="170px">
                <el-form-item label="Report Download:">
                    <el-button
                            type="primary"
                            size="small"
                            @click="downloadClusterReport">
                       text
                    </el-button>

                    <el-button
                            type="primary"
                            size="small"
                            @click="downloadResultImage">
                        image
                    </el-button>

                </el-form-item>
            </el-form>

        </div>


<!--        <el-form v-if="this.$store.state.user.type == 3" ref="downloadForm2" :model="downloadForm2" label-position="left" label-width="170px">-->
<!--            <el-form-item label="Report Download:">-->
<!--                <el-button-->
<!--                        type="primary"-->
<!--                        size="small"-->
<!--                        @click="downloadSampleCsvFile">-->
<!--                    Download-->
<!--                </el-button>-->
<!--            </el-form-item>-->
<!--        </el-form>-->

    </el-container>

</template>



<script>
    export default {
        name: 'UploadMedicalRecords',
        data() {
            return {
                uploadForm1: {
                    desc: 'Upload medical case By' + ' User: ' + this.$store.state.user.login_name,
                },
                uploadForm2: {
                    desc: 'Upload medical cases By' + ' Manager: ' + this.$store.state.user.login_name,
                },
                cluster_num: {

                },
                downloadForm1:{

                },
                downloadForm2:{

                },

                downloadForm3:{

                },
                fileList1: [],
                fileList2: [],
                // headers: {Token: sessionStorage.getItem('addsCurrentUserToken')}
            }
        },
        methods: {

            //点击保存下载图片
            downloadResultImage() {
                let aLink = document.createElement('a');
                let blob = this.base64ToBlob();
                let evt = document.createEvent('HTMLEvents');
                evt.initEvent('click', true, true);
                aLink.download = "cluster result image"; //下载图片的名称
                aLink.href = URL.createObjectURL(blob);
                aLink.click();
            },
            exportImg() { //echart返回一个 base64 的 URL
                let myChart = this.$echarts.init(
                    document.getElementById("main")
                );
                return myChart.getDataURL({
                    type: 'png',
                    pixelRatio: 1,
                    backgroundColor: '#fff'
                })
            },
            base64ToBlob() { //将base64转换blob
                let img = this.exportImg();
                let parts = img.split(';base64,');
                let contentType = parts[0].split(':')[1];
                let raw = window.atob(parts[1]);
                let rawLength = raw.length;
                let uInt8Array = new Uint8Array(rawLength);
                for (let i = 0; i < rawLength; ++i) {
                    uInt8Array[i] = raw.charCodeAt(i);
                }
                return new Blob([uInt8Array], { type: contentType });
            },

            myEcharts() {
                var myChart = this.$echarts.init(document.getElementById('main'));
            },

            myEcharts(data){
                console.log(data)

                // 基于准备好的dom，初始化echarts实例
                var myChart = this.$echarts.init(document.getElementById('main'));

                var COLOR_ALL = ['#c23531', '#2f4554', '#61a0a8', '#d48265', '#91c7ae', '#749f83', '#ca8622', '#bda29a', '#6e7074', '#546570', '#c4ccd3'];
                //data's color array should be modified
                for(var i = 0; i < data.length;i++){
                    var color_index = parseInt(data[i].itemStyle.color['color']);
                   data[i].itemStyle.color = COLOR_ALL[color_index];
                    // console.log(data[i])
                }

                // 指定图表的配置项和数据
                var option = {
                    xAxis: {
                        type: 'value'
                    },
                    yAxis: {
                        type: 'value'
                    },
                    series: [{
                        symbolSize: 13,
                        data: data,
                        type: 'scatter',
                    }],
                    // toolbox: {
                    //     show: true,
                    //     feature: {
                    //         saveAsImage: {
                    //             show:true,
                    //             excludeComponents :['toolbox'],
                    //             pixelRatio: 2
                    //         }
                    //     }
                    // }
                    tooltip: {
                        trigger: 'axis',
                        formatter: function (params) {
                            // console.log(params[0].data.diagnosis)
                            return 'id: ' + params[0].data.name
                                // + '<br>' + 'diagnosis: ' + params[0].data.diagnosis;
                        }
                    },
                };

                var height = document.getElementById('hight');
                var weight = document.getElementById('weight');

                // 使用刚指定的配置项和数据显示图表。
                myChart.setOption(option);

                var ecConfig = echarts.config;  //关键中的关键

                function eConsole(param){

                    if(param.value.length > 1) {
                        height.innerText = param.value[0]+'cm';
                        weight.innerText = param.value[1]+'cm';
                    } else {
                        height.innerText = param.name+'cm';
                        weight.innerText = param.value+'cm';
                    }
                }

                //在这里做一个点击事件的监听，绑定的是eConsole方法
                myChart.on(ecConfig.EVENT.CLICK, eConsole);   //max关键

            },

            upload() {
                this.$refs.upload.submit();
            },
            uploadCsv() {
                this.$refs.uploadCsv.submit();
                // this.myEcharts();
            },

            downloadClusterReport(){
                let link = document.createElement('a');
                link.style.display = 'none';
                link.href = window.location.origin + this.$axios.defaults.baseURL + '/case/download/ClusterReport';
                document.body.appendChild(link);
                link.click();
                link.remove();
            },
            downloadSampleCsvFileForCluster(){
                let link = document.createElement('a');
                link.style.display = 'none';
                link.href = window.location.origin + this.$axios.defaults.baseURL + '/case/download/SampleCsvFileForCluster';
                document.body.appendChild(link);
                link.click();
                link.remove();
            },
            downloadSampleImageFile(){
                let link = document.createElement('a');
                link.style.display = 'none';
                link.href = window.location.origin + this.$axios.defaults.baseURL + '/case/download/SampleImageFile';
                document.body.appendChild(link);
                link.click();
                link.remove();
            },
            uploadMedicalArchive(content) {
                var date = new Date();
                var dateString =
                    date.getUTCFullYear() +
                    ("0" + (date.getUTCMonth()+1)).slice(-2) +
                    ("0" + date.getUTCDate()).slice(-2) +
                    ("0" + date.getUTCHours()).slice(-2) +
                    ("0" + date.getUTCMinutes()).slice(-2) +
                    ("0" + date.getUTCSeconds()).slice(-2);

                let params = new FormData();
                params.append('title', 'Patient ' + this.$store.state.user.id + ' Medical Archive ' + dateString);
                params.append('category', 'N/A');
                params.append('desc', this.uploadForm1.desc);
                params.append('file', content.file);

                this.$axios({
                    method: 'post',
                    url: '/case/user/' + this.$store.state.user.id,
                    data: params
                }).then(res => {
                    this.$refs['uploadForm1'].resetFields();
                    this.$message({
                        type: 'success',
                        message: 'Successully uploaded medical case!',
                        showClose: true
                    });
                    this.$axios({
                        method: 'post',
                        url: '/caseSearch/' + res.data['medicalCaseId'],
                    });
                }).catch(error => {
                    this.$message({
                        type: 'error',
                        message: 'Failed to upload! Check console.',
                        showClose: true
                    });
                    console.log(error);
                });
            }

            ,
            uploadDataset(content) {
                var date = new Date();
                var dateString =
                    date.getUTCFullYear() +
                    ("0" + (date.getUTCMonth()+1)).slice(-2) +
                    ("0" + date.getUTCDate()).slice(-2) +
                    ("0" + date.getUTCHours()).slice(-2) +
                    ("0" + date.getUTCMinutes()).slice(-2) +
                    ("0" + date.getUTCSeconds()).slice(-2);

                let params = new FormData();
                params.append('title', this.$store.state.user.id + ' Upload History Admissions ' + dateString);
                params.append('category', 'N/A');
                // params.append('desc', this.uploadForm2.desc);
                params.append('file', content.file);
                params.append('cluster_num',this.uploadForm2.cluster_num);


                this.$axios({
                    // method: 'post',
                    // url: '/case/dataset/'+ this.$store.state.user.id,
                    // data: params
                }).then(res => {

                    this.$refs['uploadForm2'].resetFields();
                    this.$message({
                        type: 'success',
                        message: 'Successfully Cluster!',
                        showClose: true
                    });

                    this.$axios({
                        method: 'get',
                        url: '/case/cluster/result',
                    }).then(res => {

                        this.myEcharts(res.data)
                        // let nodes = res.data.nodes;
                        // let links = res.data.links;
                        // this.kgData1.nodes = nodes;
                        // this.kgData1.links = links;
                        // this.paintGraph1(this.kgData1, this.kgSvgComponents1);
                    }).catch(error => {
                        console.log(error);
                    });


                }).catch(error => {
                    this.$message({
                        type: 'error',
                        message: 'Failed to upload! Check console.',
                        showClose: true
                    });
                    console.log(error);
                });
            }
        },
        mounted() {
            this.myEcharts();
        }
    }
</script>

<style scoped>
    #container {
        position: fixed;
        top: 100px;
        left: 300px;
        width: 600px;
    }

    #footer {
        text-align: center;
    }

    #footer1 {
        text-align: center;
    }

    #footer2 {
        text-align: center;
    }
</style>
