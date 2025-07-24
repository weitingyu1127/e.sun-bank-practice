// src/router/index.js
import { createRouter, createWebHistory } from 'vue-router';
import LoginPage from '@/components/LoginPage.vue';
import HomePage from '@/components/HomePage.vue';

const routes = [
  { path: '/', component: LoginPage },
  { path: '/home', component: HomePage }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;
