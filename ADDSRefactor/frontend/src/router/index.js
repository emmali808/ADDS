import Vue from 'vue'
import Router from 'vue-router'

import login from "../pages/login";
import signUp from "../pages/signUp";

import deepLearning from "../pages/deepLearning";
import DeepModels from "../components/DeepLearning/DeepModels";
import UploadDataset from "../components/DeepLearning/UploadDataset";
import UploadKg from "../components/DeepLearning/UploadKg";
import ModelEvaluation from "../components/DeepLearning/ModelEvaluation";
import AutoSelection from "../components/DeepLearning/AutoSelection";
import KnowledgeExploration from "../components/DeepLearning/KnowledgeExploration";

import knowledgeGraph from "../pages/knowledgeGraph";

import medicalArchivePreprocess from "../pages/medicalArchivePreprocess";

import autoDiagnosis from "../pages/autoDiagnosis";
import UploadMedicalRecords from "../components/AutoDiagnosis/Uploadmedicalrecords";
import MachineDiagnosis from "../components/AutoDiagnosis/MachineDiagnosis";
import SubmitQuestions from "../components/AutoDiagnosis/SubmitQuestions";

import QA from "../pages/QA";
import Questions from "../components/QA/Questions";
import QuestionDetail from "../components/QA/QuestionDetail";

import consult from "../pages/consult";

import my from "../pages/my";



Vue.use(Router);

const router = new Router({
  // 使用 history 模式消除 URL 中的 # 号
  // mode: "history",
  linkActiveClass: 'is-active',
  routes: [
    {
      path: '/',
      redirect: '/login'
    }, {
      path: '/login',
      name: 'login',
      component: login
    }, {
      path: '/signUp',
      name: 'signUp',
      component: signUp
    }, {
      path: '/deepLearning',
      component: deepLearning,
      children: [
        {
          path: '',
          redirect: 'deepModels'
        }, {
          path: 'deepModels',
          name: 'DeepModels',
          component: DeepModels,
          meta: {
            keepAlive: true
          }
        }, {
          path: 'uploadDataset',
          name: 'UploadDataset',
          component: UploadDataset
        }, {
          path: 'uploadKnowledgeGraph',
          name: 'UploadKg',
          component: UploadKg
        }, {
          path: 'modelEvaluation',
          name: 'ModelEvaluation',
          component: ModelEvaluation
        }, {
          path: 'autoSelection',
          name: 'AutoSelection',
          component: AutoSelection
        }, {
          path: 'knowledgeExploration',
          name: 'KnowledgeExploration',
          component: KnowledgeExploration
        }
      ]
    }, {
      path: '/knowledgeGraph',
      name: 'knowledgeGraph',
      component: knowledgeGraph,
      meta: {
        keepAlive: true
      }
    }, {
      path: '/medicalArchivePreprocess',
      name: 'medicalArchivePreprocess',
      component: medicalArchivePreprocess,
      meta: {
        keepAlive: false
      }
    }, {
      path: '/autoDiagnosis',
      component: autoDiagnosis,
      children: [
        {
          path: '',
          redirect: 'uploadMedicalRecords'
        }, {
          path: 'uploadMedicalRecords',
          name: 'UploadMedicalRecords',
          component: UploadMedicalRecords,
          meta: {
            keepAlive: true
          }
        }, {
          path: 'machineDiagnosis',
          name: 'MachineDiagnosis',
          component: MachineDiagnosis,
          meta: {
            keepAlive: true
          }
        }, {
          path: 'submitQuestions',
          name: 'SubmitQuestions',
          component: SubmitQuestions
        }
      ]
    }, {
      path: '/QA',
      component: QA,
      children: [
        {
          path: '',
          redirect: 'questions'
        }, {
          path: 'questions',
          name: 'Questions',
          component: Questions,
          meta: {
            keepAlive: true
          }
        }, {
          path: 'questionDetail/:id',
          name: 'QuestionDetail',
          component: QuestionDetail
        }
      ]
    }, {
      path: '/consult',
      name: 'consult',
      component: consult,
      meta: {
        keepAlive: true
      }
    }, {
      path: '/my',
      name: 'my',
      component: my,
      meta: {
        keepAlive: true
      }
    }
  ]
});

// 导航守卫
// 使用 router.beforeEach 注册一个全局前置守卫，判断用户是否登陆
router.beforeEach((to, from, next) => {
  if (to.path === '/login') {
    // alert("可添加“路由至登录页”的效果！！记得弄一下！！");
    next();
  } else if (to.path === '/signUp') {
    next();
  } else {
    // Why I write this??
    // let token = localStorage.getItem('Authorization');

    let userInfo = JSON.parse(sessionStorage.getItem("addsCurrentUserInfo"));
    let token = sessionStorage.getItem("addsCurrentUserToken");
    if (token === 'null' || token === '' || userInfo === null || userInfo === '') {
      next('/login');
    } else {
      next();
    }
  }
});

export default router;
