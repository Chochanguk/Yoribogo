import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  {
    path: '/question/:qid',
    name: 'question',
    component: () => import('../views/QuestionView.vue')
  },
  {
    path: '/question/result',
    name: 'result',
    component: () => import('../views/ResultView.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior() {
    return { top: 0 }
  }
})

export default router
