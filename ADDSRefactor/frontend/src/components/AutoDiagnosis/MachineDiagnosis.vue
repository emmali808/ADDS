<template>
    <el-main>
        <div class="title">
            <h3>Select Your Case:</h3>
        </div>
        <div class="option-block">
            <el-select v-model="kgChosen" placeholder="Choose a knowledge graph">
                <el-option-group v-for="group in kgGroupList" :key="group.label" :label="group.label">
                    <el-option v-for="item in group.options" :key="item.value" :label="item.label" :value="item.value">
                    </el-option>
                </el-option-group>
            </el-select>
        </div>
        <div id="chartOne" :style="{height: '500px'}"></div>
        <div class="title">
            <h3>Similar Cases:</h3>
        </div>
        <div id="chartTwo" :style="{height: '500px'}"></div>
        <div class="diagnosis" style="margin-right: auto;margin-left: auto;width: 750px;text-align: left;font-size: 16px">
            <span style="font-weight: bold">Diagnosis：</span>
            The patient has a runny nose and may haveCold. There are several small red dots that do not protrude in the back of the head. The measured body temperature was 37.8 degrees, and the electrocardiogram results in sinus rhythm and T wave changes. It is recommended to take acetyl guitarmycin granules twice a day, one bag at a time. Pediatric heat-clearing granules, three times a day, half a bag each time.
        </div>
        <div class="page-nav" >
            <a href="#" class="prev">«</a>
            <a href="#" class="num">1</a>
            <a href="#" class="on num">2</a>
            <a href="#" class="num">3</a>
            <a href="#" class="num">4</a>
            <a href="#" class="next">»</a>
        </div>
    </el-main>

</template>

<script type="text/ecmascript-6">
    export default {
        name: 'hello',
        data() {
            return {
                msg: 'Welcome to Your Vue.js App',
                kgGroupList: [{
                    options: [{value:1,label: "Influenza case 1"},
                        {value:2,label: "Influenza case 2"}]
                }],
                kgChosen: 1,
            }
        },
        mounted() {
            this.drawGraph1();
            this.drawGraph2();
        },
        methods: {
            loadKG() {
                this.chartOne=true;
            },
            drawGraph1() {
                let chartOne = this.$echarts.init(document.getElementById('chartOne'),'light');
                let categories = [];
                categories[0]={name:'DisorderOrSyndrome'};
                categories[1]={name:'Symptom'};
                categories[2]={name:'Finding'};
                categories[3]={name:'LabTest'};
                categories[4]={name:'Medication'};
                categories[5]={name:'Value'};
                let option = {
                    tooltip: {
                        formatter: function (x) {
                            return x.data.des;
                        }
                    },
                    legend: [{
                        data: categories.map(function (a) {
                            return a.name;
                        }),
                        top:'1%',
                        center: '50%'
                    }],
                    series: [{
                        type: 'graph', // 类型:关系图
                        top: '15%',
                        layout: 'force', //图的布局，类型为力导图
                        symbolSize: 40, // 调整节点的大小
                        move: true, // 是否开启鼠标缩放和平移漫游。默认不开启。如果只想要开启缩放或者平移,可以设置成 'scale' 或者 'move'。设置成 true 为都开启
                        edgeSymbol: ['circle', 'arrow'],
                        edgeSymbolSize: [2, 10],
                        force: {
                            repulsion: 500,
                            gravity:0.1,
                            edgeLength: [10, 50]
                        },
                        draggable: true,
                        lineStyle: {
                            normal: {
                                width: 2,
                                color: '#4b565b',
                            }
                        },
                        edgeLabel: {
                            normal: {
                                show: true,
                                formatter: function (x) {
                                    return x.data.name;
                                }
                            }
                        },
                        label: {
                            normal: {
                                show: true,
                                // textStyle: {}
                            }
                        },
                        data: [{name: 'Cold', des: 'Cold', symbolSize: 70, category: 0,},
                            {name: ' Influenza', des: ' Influenza', symbolSize: 60, category: 1,},
                            {name: 'Dizzy', des: 'Dizzy', symbolSize: 60, category: 1,},
                            {name: 'Temperature', des: 'Temperature', symbolSize: 60, category: 1,},
                            {name: 'Electrocardiogram', des: 'Electrocardiogram', symbolSize: 50, category: 3,},
                            {name: 'Acetylkitasamycin Granules', des: 'Acetylkitasamycin Granules', symbolSize: 50, category: 4,},
                            {name: 'One bag twice a day', des: 'One bag twice a day', symbolSize: 50, category: 5,},
                            {name: 'Sinus rhythm, T wave change', des: 'Sinus rhythm, T wave change', symbolSize: 50, category: 5,},
                            {name: '37.8', des: '37.8', symbolSize: 50, category: 5,},
                            {name: 'A runny nose', des: 'A runny nose', symbolSize: 50, category: 5,}],
                        links: [{source: 'Cold', target: ' Influenza', name: '', des: 'link01des'},
                            {source: 'Cold', target: 'Dizzy', name: '', des: 'link02des'},
                            {source: 'Cold', target: 'Temperature', name: '', des: 'link03des'},
                            {source: 'Acetylkitasamycin Granules', target: 'Cold', name: '', des: 'link05des'},
                            {source: 'Electrocardiogram', target: 'Cold', name: '', des: 'link07des'},
                            {source: 'One bag twice a day', target: 'Acetylkitasamycin Granules', name: '', des: 'link08des'},
                            {source: 'A runny nose', target: ' Influenza', name: '', des: 'link10des'},
                            {source: '37.8', target: 'Temperature', name: '', des: 'link12des'},
                            {source: 'Sinus rhythm, T wave change', target: 'Electrocardiogram', name: '', des: 'link13des'},],
                        categories: categories,
                    }]
                };
                chartOne.setOption(option);
            },
            drawGraph2() {
                let chartTwo = this.$echarts.init(document.getElementById('chartTwo'),'light')
                let categories = [];
                categories[0]={name:'DisorderOrSyndrome'};
                categories[1]={name:'Symptom'};
                categories[2]={name:'Finding'};
                categories[3]={name:'LabTest'};
                categories[4]={name:'Medication'};
                categories[5]={name:'Value'};
                let option = {
                    title: {
                        text: 'Date:2016-06-01',
                        left:'center'
                    },
                    tooltip: {
                        formatter: function (x) {
                            return x.data.des;
                        }
                    },
                    // toolbox: {
                    //     show: true,
                    //     feature: {
                    //         mark: {
                    //             show: true
                    //         },
                    //         // restore: {
                    //         //     show: true
                    //         // },
                    //         saveAsImage: {
                    //             show: true
                    //         }
                    //     }
                    // },
                    legend: [{
                        data: categories.map(function (a) {
                            return a.name;
                        }),
                        top:'7%',
                        center: '50%'
                    }],
                    series: [{
                        type: 'graph', // 类型:关系图
                        top: '15%',
                        layout: 'force', //图的布局，类型为力导图
                        symbolSize: 40, // 调整节点的大小
                        move: true, // 是否开启鼠标缩放和平移漫游。默认不开启。如果只想要开启缩放或者平移,可以设置成 'scale' 或者 'move'。设置成 true 为都开启
                        edgeSymbol: ['circle', 'arrow'],
                        edgeSymbolSize: [2, 10],
                        force: {
                            repulsion: 400,
                            gravity:0.1,
                            edgeLength: [10, 50]
                        },
                        draggable: true,
                        lineStyle: {
                            normal: {
                                width: 2,
                                color: '#4b565b',
                            }
                        },
                        edgeLabel: {
                            normal: {
                                show: true,
                                formatter: function (x) {
                                    return x.data.name;
                                }
                            }
                        },
                        label: {
                            normal: {
                                show: true,
                                // textStyle: {}
                            }
                        },
                        data: [{name: 'Cold', des: 'Cold', symbolSize: 70, category: 0,},
                            {name: ' Influenza', des: ' Influenza', symbolSize: 60, category: 1,},
                            {name: 'Faeces', des: 'Faeces', symbolSize: 60, category: 1,},
                            {name: 'Temperature', des: 'Temperature', symbolSize: 60, category: 1,},
                            {name: 'The back of the head', des: 'The back of the head', symbolSize: 60, category: 1,},
                            {name: 'Electrocardiogram', des: 'Electrocardiogram', symbolSize: 50, category: 3,},
                            {name: 'Acetylkitasamycin Granules', des: 'Acetylkitasamycin Granules', symbolSize: 50, category: 4,},
                            {name: 'Xiaoer qingrening granule', des: 'Xiaoer qingrening granule', symbolSize: 50, category: 4,},
                            {name: 'One bag twice a day', des: 'One bag twice a day', symbolSize: 50, category: 5,},
                            {name: 'A few small red spots', des: 'A few small red spots', symbolSize: 50, category: 5,},
                            {name: 'Sinus rhythm, T wave change', des: 'Sinus rhythm, T wave change', symbolSize: 50, category: 5,},
                            {name: '37.8', des: '37.8', symbolSize: 50, category: 5,},
                            {name: 'Abnormal', des: 'Abnormal', symbolSize: 50, category: 5,},
                            {name: '1/2 bag three times a day', des: '1/2 bag three times a day', symbolSize: 50, category: 5,},
                            {name: 'A runny nose', des: 'A runny nose', symbolSize: 50, category: 5,}],
                        links: [{source: 'Cold', target: ' Influenza', name: '', des: 'link01des'},
                            {source: 'Cold', target: 'Faeces', name: '', des: 'link02des'},
                            {source: 'Cold', target: 'Temperature', name: '', des: 'link03des'},
                            {source: 'Cold', target: 'The back of the head', name: '', des: 'link04des'},
                            {source: 'Acetylkitasamycin Granules', target: 'Cold', name: '', des: 'link05des'},
                            {source: 'Xiaoer qingrening granule', target: 'Cold', name: '', des: 'link06des'},
                            {source: 'Electrocardiogram', target: 'Cold', name: '', des: 'link07des'},
                            {source: 'One bag twice a day', target: 'Acetylkitasamycin Granules', name: '', des: 'link08des'},
                            {source: '1/2 bag three times a day', target: 'Xiaoer qingrening granule', name: '', des: 'link09des'},
                            {source: 'A runny nose', target: ' Influenza', name: '', des: 'link10des'},
                            {source: 'Abnormal', target: 'Faeces', name: '', des: 'link11des'},
                            {source: '37.8', target: 'Temperature', name: '', des: 'link12des'},
                            {source: 'A few small red spots', target: 'The back of the head', name: '', des: 'link13des'},
                            {source: 'Sinus rhythm, T wave change', target: 'Electrocardiogram', name: '', des: 'link13des'},],
                        categories: categories,
                    }]
                };
                chartTwo.setOption(option);
            },
            toshow() {
                let arr=["answer","none-answer"];

            }
    },

    }

</script>

<style scoped>
    .el-main {
        position: fixed;
        top: 80px;
        left: 260px;
        right: 30px;
        bottom: 0;
        margin: 20px;
        padding: 10px;
    }

    .title {
        margin: 10px 0;
    }

    .option-block {
        display: inline-block;
        /*margin-right: 40px;*/
    }

    .option-block .el-select {
        width: 400px;
    }
    
    #chartOne {
        margin: 20px 0;
    }
    
    #chartTwo {
        margin: 20px 0;
    }
    .diagnosis{
        margin: 10px 0;
    }
    .page-nav{
        text-align: center;
    }
    .page-nav a{
        margin-right: 12px;
    }
    .page-nav .num{
        display: inline-block;
        color: black;
        text-decoration: none;
        width: 32px;
        height: 32px;
        text-align: center;
        line-height: 32px;
    }
    .page-nav .on {
        background-color: #409EFF;
        color: white;
        border-radius: 100%;
    }
    .page-nav .prev,.page-nav .next{
        display: inline-block;
        color: black;
        text-decoration: none;
        width: 32px;
        height: 32px;
        text-align: center;
        line-height: 32px;
        cursor: text;
    }
</style>
