// 引入vue
import Vue from 'vue'
// 引入vue-router
import VueRouter from 'vue-router'

Vue.use(VueRouter)

import login from '@/views/login'
import user_index from "@/views/user_index.vue";
import user_admin from "@/views/user_admin.vue";
import user_fix_log from "@/views/user_fix_log.vue";
import user_post from "@/views/user_post.vue";
import user_ai from "@/views/user_ai.vue";
import user_chat from "@/views/user_chat.vue";

import admin_index from "@/views/admin_index.vue";
import admin_users from "@/views/admin_users.vue";
import admin_fix_log from "@/views/admin_fix_log.vue";
import admin_chat from "@/views/admin_chat.vue";
// 创建vue实例
export default new VueRouter({
    routes: [
        {
            path: '/',
            redirect: '/login'
        },
        {
            path: '/login',
            component: login
        }, {
            path: '/user_index',
            component: user_index
        }, {
            path: '/user_admin',
            component: user_admin
        }, {
            path: '/user_fix_log',
            component: user_fix_log
        }, {
            path: '/admin_index',
            component: admin_index
        }, {
            path: '/admin_users',
            component: admin_users
        }, {
            path: '/admin_fix_log',
            component: admin_fix_log
        }, {
            path: '/user_post',
            component: user_post
        }, {
            path: '/user_ai',
            component: user_ai
        }, {
            name: 'user_chat',
            path: '/user_chat',
            component: user_chat
        },{
            name: 'admin_chat',
            path: '/admin_chat',
            component: admin_chat
        }
    ]
})