import Layout from '@/layouts/index.vue';

export default [
  {
    path: '/cms',
    component: Layout,
    name: 'cms',
    meta: { title: { zh_CN: '内容管理', en_US: 'CMS' }, icon: 'article', orderNo: 1, hidden: false },
    children: [
      {
        path: 'bannerList',
        name: 'bannerList',
        component: () => import('@/pages/oledu/banner/index.vue'),
        meta: { title: { zh_CN: '横幅管理', en_US: 'Banner Mgt' } },
      },
      {
        path: 'newsList',
        name: 'newsList',
        component: () => import('@/pages/oledu/news/index.vue'),
        meta: { title: { zh_CN: '文章管理', en_US: 'News Mgt' } },
      },
    ],
  },
  {
    path: '/edu',
    component: Layout,
    name: 'edu',
    meta: { title: { zh_CN: '在线学习', en_US: 'Edu Online' }, icon: 'book-open', orderNo: 10, hidden: false },
    children: [
      {
        path: 'courseTypes',
        name: 'CourseTypes',
        component: () => import('@/pages/oledu/courseType/index.vue'),
        meta: { title: { zh_CN: '课程类别', en_US: 'Course Type' } },
      },
      {
        path: 'CourseList',
        name: 'CourseList',
        component: () => import('@/pages/oledu/course/index.vue'),
        meta: { title: { zh_CN: '课程管理', en_US: 'Course Mgt' } },
      },
    ],
  },
  {
    path: '/teacher',
    component: Layout,
    name: 'teacher',
    meta: { title: { zh_CN: '讲师管理', en_US: 'Teacher Mgt' }, icon: 'user-business', orderNo: 11, hidden: false },
    children: [
      {
        path: 'teacherList',
        name: 'teacherList',
        component: () => import('@/pages/oledu/teacher/index.vue'),
        meta: { title: { zh_CN: '讲师列表', en_US: 'Teacher List' } },
      },
      {
        path: 'teacherTagList',
        name: 'teacherTagList',
        component: () => import('@/pages/oledu/teacherTag/index.vue'),
        meta: { title: { zh_CN: '讲师标签', en_US: 'TeacherTag List' } },
      },
    ],
  },
  {
    path: '/member',
    component: Layout,
    name: 'member',
    meta: { title: { zh_CN: '学员管理', en_US: 'Member Mgt' }, icon: 'usergroup', orderNo: 15, hidden: false },
    children: [
      {
        path: 'memberList',
        name: 'MemberList',
        component: () => import('@/pages/oledu/member/index.vue'),
        meta: { title: { zh_CN: '学员列表', en_US: 'Member List' } },
      },
      {
        path: 'memberIdCardAuditList',
        name: 'MemberIdCardAuditList',
        component: () => import('@/pages/oledu/memberIdCardAudit/index.vue'),
        meta: { title: { zh_CN: '资料审核', en_US: 'MemberIdCardAudit List' } },
      },
    ],
  },
  {
    path: '/shop',
    component: Layout,
    name: 'shop',
    meta: { title: { zh_CN: '交易管理', en_US: 'Transaction Mgt' }, icon: 'cart', orderNo: 20, hidden: false },
    children: [
      {
        path: 'orderList',
        name: 'OrderList',
        component: () => import('@/pages/oledu/order/index.vue'),
        meta: { title: { zh_CN: '交易订单', en_US: 'Order List' } },
      },
      {
        path: 'transactionLog',
        name: 'TransactionLog',
        component: () => import('@/pages/test.vue'),
        meta: { title: { zh_CN: '付款日志(未实现)', en_US: 'Transaction Log' } },
      },
    ],
  },
  {
    path: '/word',
    component: Layout,
    name: 'word',
    meta: { title: { zh_CN: '言论管理', en_US: 'Word Mgt' }, icon: 'chat-message', orderNo: 25, hidden: false },
    children: [
      {
        path: 'sensitiveWordList',
        name: 'SensitiveWordList',
        component: () => import('@/pages/oledu/sensitiveWord/index.vue'),
        meta: { title: { zh_CN: '敏感词管理', en_US: 'SensitiveWord Mgt' } },
      },
      {
        path: 'noteList',
        name: 'NoteList',
        component: () => import('@/pages/oledu/note/index.vue'),
        meta: { title: { zh_CN: '笔记管理', en_US: 'Note Mgt' } },
      },
    ],
  },
];
