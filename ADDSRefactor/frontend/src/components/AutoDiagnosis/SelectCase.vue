<template>
    <el-main>
        <div class="option-block">
            <el-select v-model="kgChosen" placeholder="Choose a knowledge graph" @change="loadKG">
                <el-option-group v-for="group in kgGroupList" :key="group.label" :label="group.label">
                    <el-option v-for="item in group.options" :key="item.value" :label="item.label" :value="item.value">
                    </el-option>
                </el-option-group>
            </el-select>
        </div>
        <div v-show="chartone" id="Chartone" :style="{width: '900px', height: '500px'}"></div>
    </el-main>
</template>

<script type="text/ecmascript-6">
    export default {
        name: 'SelectCase',
        data() {
            return {
                msg: 'Welcome to Your Vue.js App',
                kgGroupList: [{
                    options: [{value:1,label: "Influenza case 1"},
                        {value:2,label: "Influenza case 2"}]
                }],
                kgChosen: '',
                chartone:false,
            }
        },
        mounted() {
            this.drawGraph();
        },
        methods: {
            loadKG() {
                console.log(this.kgChosen);
                this.chartone=true;
            },
            drawGraph() {
                let Chartone = this.$echarts.init(document.getElementById('Chartone'),'light');
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
                        top:'1%'
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
                Chartone.setOption(option);
            },
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
    #Chartone {
        position: fixed;
        top: 30px;
        left: -10px;
    }
    .diagnosis{
        position: fixed;
        top: 600px;
        left: 320px;
    }

    .option-block {
        display: inline-block;
        /*margin-right: 40px;*/
    }

    .option-block .el-select {
        width: 400px;
    }
</style>
