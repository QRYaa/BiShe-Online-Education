import Layout from '@/layouts/index.vue';

export default [
  {
    path: '/customer',
    component: Layout,
    name: 'customer',
    meta: { title: { zh_CN: '客户管理', en_US: 'Customer' }, icon: 'user-vip', orderNo: 1, hidden: true },
    children: [
      {
        path: 'customerList',
        name: 'customerList',
        component: () => import('@/pages/system/customer/index.vue'),
        meta: { title: { zh_CN: '客户', en_US: 'Customer List' } },
      },
      {
        path: 'customerGrade',
        name: 'customerGrade',
        component: () => import('@/pages/system/customerGrade/index.vue'),
        meta: { title: { zh_CN: '客户等级', en_US: 'Customer Grade' }, hidden: true },
      },
    ],
  },
  {
    path: '/setting',
    component: Layout,
    name: 'setting',
    meta: { title: { zh_CN: '系统配置', en_US: 'Settings' }, icon: 'setting-1', orderNo: 100 },
    children: [
      {
        path: 'dictManagement',
        name: 'DictManagement',
        component: () => import('@/pages/system/dict/index.vue'),
        meta: { title: { zh_CN: '字典管理', en_US: 'Dictionary Mgt' } },
      },
      {
        path: 'settingManagement',
        name: 'SettingManagement',
        component: () => import('@/pages/system/setting/index.vue'),
        meta: { title: { zh_CN: '设置管理', en_US: 'Setting Mgt' }, permissionCode: 'SETTING_MGT' },
      },
      {
        path: 'userManagement',
        name: 'UserManagement',
        component: () => import('@/pages/system/user/index.vue'),
        meta: { title: { zh_CN: '用户管理', en_US: 'User Mgt' }, permissionCode: 'USER' },
      },
      {
        path: 'personal',
        name: 'personal',
        component: () => import('@/pages/user/index.vue'),
        meta: { title: { zh_CN: '个人中心', en_US: 'User Center' }, hidden: true },
      },
      {
        path: 'departmentManagement',
        name: 'DepartmentManagement',
        component: () => import('@/pages/system/department/index.vue'),
        meta: { title: { zh_CN: '组织架构', en_US: 'Department Mgt' }, permissionCode: 'DEPARTMENT' },
      },
      {
        path: 'roleManagement',
        name: 'RoleManagement',
        component: () => import('@/pages/system/role/index.vue'),
        meta: { title: { zh_CN: '角色管理', en_US: 'Role Mgt' }, permissionCode: 'ROLE_MGT' },
      },
      {
        path: 'permissionManagement',
        name: 'PermissionManagement',
        component: () => import('@/pages/system/permission/index.vue'),
        meta: { title: { zh_CN: '权限管理', en_US: 'Permission Mgt' }, permissionCode: 'PERMISSION_MGT' },
      },
      {
        path: 'taskManagement',
        name: 'TaskManagement',
        component: () => import('@/pages/system/task/index.vue'),
        meta: { title: { zh_CN: '定时任务', en_US: 'Task Mgt' } },
      },
      {
        path: 'taskLog/index/:taskId(\\d+)',
        name: 'TaskLog',
        component: () => import('@/pages/system/task/components/TaskLogList.vue'),
        meta: { title: '调度日志', hidden: true },
      },
    ],
  },
];
