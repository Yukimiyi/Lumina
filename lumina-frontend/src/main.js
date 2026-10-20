import { createApp } from 'vue'
import { createRouter, createWebHashHistory } from 'vue-router'
import App from './App.vue'
import HomeView from './views/HomeView.vue'
import ChatView from './views/ChatView.vue'
import './style.css'

const router = createRouter({
  history: createWebHashHistory(),
  routes: [
    { path: '/', component: HomeView },
    { path: '/love', component: ChatView, props: { appId: 'love' } },
    { path: '/manus', component: ChatView, props: { appId: 'manus' } },
    { path: '/:pathMatch(.*)*', redirect: '/' },
  ],
})

createApp(App).use(router).mount('#app')
