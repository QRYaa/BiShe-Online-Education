import { DashboardIcon } from 'tdesign-icons-vue-next';
import { shallowRef } from 'vue';

import Layout from '@/layouts/index.vue';

export default [
  {
    path: '/dashboard',
    component: Layout,
    redirect: '/dashboard/base',
    name: 'dashboard',
    meta: {
      title: {
        zh_CN: '仪表盘',
        en_US: 'Dashboard',
      },
      icon: shallowRef(DashboardIcon),
      orderNo: 0,
    },
    children: [
      {
        path: 'base',
        name: 'UserIndexBase',
        component: () => import('@/pages/user/index.vue'),
        meta: { title: { zh_CN: '个人中心', en_US: 'User Center' } },
      },
      {
        path: 'counter',
        name: 'Counter',
        component: () => import('@/pages/user/index.vue'),
        meta: { title: { zh_CN: '办理中心', en_US: 'Counter' }, hidden: true },
      },
      {
        path: 'data',
        name: 'DashboardData',
        component: () => import('@/pages/dashboard/base/index.vue'),
        meta: {
          title: {
            zh_CN: '概览仪表盘',
            en_US: 'Overview',
          },
          hidden: true,
        },
      },
      {
        path: 'detail',
        name: 'DashboardDetail',
        component: () => import('@/pages/dashboard/detail/index.vue'),
        meta: {
          title: {
            zh_CN: '统计报表',
            en_US: 'Dashboard Detail',
          },
          hidden: true,
        },
      },
    ],
  },
];
