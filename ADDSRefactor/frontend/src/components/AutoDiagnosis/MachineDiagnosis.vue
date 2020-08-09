<template>
    <div>
        <div id="myChart" :style="{width: '900px', height: '500px'}"></div>
<!--        <div id="QABar">-->
<!--            <span style="font-size: 20px; font-weight: bold;">Q&A Links</span>-->
<!--            <div class="QALink">-->
<!--                <div class="content" v-for="(item,index) in dataArray">-->
<!--                    <span style="font-weight: bold">{{item.question}}</span>-->
<!--                    <p v-for="(item1,index1) in item.answer">{{item1}}</p>-->
<!--                </div>-->
<!--            </div>-->
<!--            <div class="sys-btn">-->
<!--                <a style="position: relative;top: 25%;color: white;text-decoration: none" href="<%=basePath %>patient/talk" >Go to Online system</a>-->
<!--            </div>-->
<!--        </div>-->
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
    </div>

</template>

<script type="text/ecmascript-6">
    export default {
        name: 'hello',
        data() {
            return {
                msg: 'Welcome to Your Vue.js App',
                dataArray:[
                    {
                        question: "What is seasonalCold?",
                        shortAnswer: "SeasonalCold (or \"flu\") is most often caused by type A or BCold viruses. Symptoms include sudden onset of fever...",
                        answer: [
                            "SeasonalCold (or \"flu\") is most often caused by type A or BCold viruses. Symptoms include sudden onset of fever, cough (usually dry), headache, muscle and joint pain, sore throat and a runny nose. The cough can be severe and can last 2 or more weeks. Most people recover from fever and other symptoms within a week without requiring medical attention. However,Cold can cause severe illness or death in high-risk groups.",
                            "Seasonal epidemics occur mainly during winter, from October to March in the northern hemisphere and April to September in the southern hemisphere. In tropical and subtropical countries, seasonalCold can happen all year round."
                        ]
                    },
                    {
                        question: "What are the differences between cold and flu?",
                        shortAnswer: "Flu: Appears quickly within a few hours; Affects more than just your nose and throat...",
                        answer: [
                            "Flu: Appears quickly within a few hours; Affects more than just your nose and throat; Makes you feel exhausted and too unwell to carry on as normal",
                            "Cold: Appears gradually; Affects mainly your nose and throat; Makes you feel unwell, but you're OK to carry on as normal (for example, go to work)"
                        ]
                    },
                    {
                        question: "What a pharmacist can help with flu?",
                        shortAnswer: "A pharmacist can give treatment advice and recommend flu remedies...",
                        answer: [
                            "A pharmacist can give treatment advice and recommend flu remedies.",
                            "Be careful not to use flu remedies if you're taking paracetamol and ibuprofen tablets as it's easy to take more than the recommended dose."
                        ]
                    },
                    {
                        question: "Who is most at risk?",
                        shortAnswer: "People most at risk for severe seasonalCold are:\n•\tpregnant women at any stage of pregnancy...",
                        answer: [
                            "People most at risk for severe seasonalCold are:",
                            "•\tpregnant women at any stage of pregnancy",
                            "•\tchildren younger than 5 years",
                            "•\tpeople older than 65 years",
                            "•\tpeople with chronic medical conditions such as HIV/AIDS, asthma, heart and lung diseases and diabetes",
                            "•\tpeople with increased risk of exposure toCold, which includes health care workers"
                        ]
                    },
                    {
                        question: "How is it treated?",
                        shortAnswer: "People with the flu should drink plenty of water and rest. Most people will recover within a week. Antiviral drugs...",
                        answer: [
                            "People with the flu should drink plenty of water and rest. Most people will recover within a week. Antiviral drugs forCold can reduce severe complications and deaths althoughCold viruses can develop resistance to the drugs. They are especially important for high-risk groups. Ideally these drugs need to be administered early (within 48 hours of onset of symptoms). Antibiotics are not effective againstCold viruses."
                        ]
                    },
                    {
                        question: "How do you stop it from spreading?",
                        shortAnswer: "Influenza can spread quickly between people when an infected person coughs or...",
                        answer: [
                            "Influenza can spread quickly between people when an infected person coughs or sneezes, dispersing droplets of the virus into the air. It can be also spread by hands contaminated by the virus.",
                            "Precautionary measures should be taken to limit transmission. People should cover their mouth and nose with a tissue when coughing, then throw it out and wash their hands thoroughly and regularly."
                        ]
                    },
                    {
                        question: "Is seasonalCold linked to pandemic flu?",
                        shortAnswer: "SeasonalCold outbreaks are caused by small changes in viruses that have already...",
                        answer: [
                            "SeasonalCold outbreaks are caused by small changes in viruses that have already circulated, and to which many people have some immunity.",
                            "A pandemic occurs when anCold virus emerges that most people do not have immunity from because it is so different from any previous strain in humans. This enables the strain to spread easily between people.",
                            "SeasonalCold viruses may contribute to the emergence of a pandemic virus; and once a pandemic virus has been established, as with the pandemic A(H1N1) in 2009, it can become a seasonal virus."
                        ]
                    }
                ],
            }
        },
        mounted() {
            this.drawGraph();
        },
        methods: {
            drawGraph() {
                let myChart = this.$echarts.init(document.getElementById('myChart'),'light')
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
                        top:'7%'
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
                myChart.setOption(option);
            },
            toshow() {
                let arr=["answer","none-answer"];
                let index=arr.indexOf(this.QALink);

            }
    },

    }

</script>

<style scoped>
    #myChart {
    position: fixed;
    top: 100px;
    left: 260px;
    }
    .diagnosis{
        position: fixed;
        top: 600px;
        left: 320px;
    }
    #QABar{
        position: fixed;
        top: 100px;
        left: 1130px;
        width: 350px;
        height: 520px;
        text-align: center;
    }
    .QALink {
        margin-top: 10px;
        margin-bottom: 10px;
        width: 350px;
        height: 520px;
        border-radius: 6px;
        border: 1px solid #d0d0d0;
        overflow-y: hidden;
    }
    .QALink:hover {
        overflow: auto;
    }
    .QALink::-webkit-scrollbar {
        width: 10px;
    }
    .QALink::-webkit-scrollbar-track {
        background: #f1f1f1;
        border-radius: 6px;
    }
    .QALink::-webkit-scrollbar-thumb {
        background: #888888;
        border-radius: 6px;
    }
    .QALink::-webkit-scrollbar-thumb:hover {
        background: #777777;
    }
    .sys-btn{
        margin-left: 20%;
        width: 200px;
        height: 40px;
        background-color: #409EFF;
        border-radius: 6px;
        text-align: center;
    }
    .content{
        text-align: left;
        width: 318px;
        height: 400px;
        border-radius: 3px;
        border: 1px solid #d0d0d0;
        margin-top: 10px;
        margin-bottom: 5px;
        margin-left: 14px;
    }
    .page-nav{
        text-align: center;
        position: fixed;
        bottom: 2%;
        left: 570px;
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
