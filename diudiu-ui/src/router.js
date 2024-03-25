// 引入vue
import Vue from 'vue'
// 引入vue-router
import VueRouter from 'vue-router'

Vue.use(VueRouter)

import login from '@/views/login'
import user_index from "@/views/user/user_index.vue";
import user_admin from "@/views/user/user_admin.vue";
import user_fix_log from "@/views/user/user_fix_log.vue";
import user_post from "@/views/user/user_post.vue";
import user_ai from "@/views/user/user_ai.vue";
import user_chat from "@/views/user/user_chat.vue";
import user_notice from "@/views/user/user_notice.vue";

import admin_index from "@/views/admin/admin_index.vue";
import admin_users from "@/views/admin/admin_users.vue";
import admin_fix_log from "@/views/admin/admin_fix_log.vue";
import admin_chat from "@/views/admin/admin_chat.vue";
import admin_notice from "@/views/admin/admin_notice.vue";

import fixer_login from "@/views/fixer/fixer_login.vue";
import fixer_index from "@/views/fixer/fixer_index.vue";
import fixer_order from "@/views/fixer/fixer_order.vue";
import fixer_record from "@/views/fixer/fixer_record.vue";
// 创建vue实例
export default new VueRouter({
    routes: [
        {
            name: 'default',
            path: '/',
            redirect: '/login'
        },
        {
            path: '/login',
            component: login
        }, {
            name: 'user_index',
            path: '/user_index',
            component: user_index
        }, {
            name: 'user_admin',
            path: '/user_admin',
            component: user_admin
        }, {
            name: 'user_fix_log',
            path: '/user_fix_log',
            component: user_fix_log
        }, {
            name: 'admin_index',
            path: '/admin_index',
            component: admin_index
        }, {
            name: 'admin_users',
            path: '/admin_users',
            component: admin_users
        }, {
            name: 'admin_fix_log',
            path: '/admin_fix_log',
            component: admin_fix_log
        }, {
            name: 'user_post',
            path: '/user_post',
            component: user_post
        }, {
            name: 'user_ai',
            path: '/user_ai',
            component: user_ai
        }, {
            name: 'user_chat',
            path: '/user_chat',
            component: user_chat
        }, {
            name: 'admin_chat',
            path: '/admin_chat',
            component: admin_chat
        },
        {
            // 维修员登录和注册
            name: 'fixer_login',
            path: '/fixer_login',
            component: fixer_login
        },
        {
            // 维修员首页
            name: 'fixer_index',
            path: '/fixer_index',
            component: fixer_index
        },
        {
            // 维修员接单页面
            name: 'fixer_order',
            path: '/fixer_order',
            component: fixer_order
        },
        {
            // 维修员历史订单页面
            name: 'fixer_record',
            path: '/fixer_record',
            component: fixer_record
        },
        {
            // 学生公告栏页面
            name: 'user_notice',
            path: '/user_notice',
            component: user_notice
        },
        {
            // 管理员公告栏页面
            name: 'admin_notice',
            path: '/admin_notice',
            component: admin_notice
        }
    ]
})