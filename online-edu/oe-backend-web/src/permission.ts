import 'nprogress/nprogress.css'; // progress bar style

import NProgress from 'nprogress'; // progress bar

// import { MessagePlugin } from 'tdesign-vue-next';
import router from '@/router';
import { getPermissionStore, useDictStore, useUserStore } from '@/store';
// import { PAGE_NOT_FOUND_ROUTE } from '@/utils/route/constant';

NProgress.configure({ showSpinner: false });

router.beforeEach(async (to, from, next) => {
  NProgress.start();

  const permissionStore = getPermissionStore();
  const { whiteListRouters } = permissionStore;

  const userStore = useUserStore();
  const dictStore = useDictStore();

  if (userStore.token) {
    try {
      if (whiteListRouters.indexOf(to.path) !== -1) {
        next();
      }
      if (!dictStore.init) {
        await dictStore.loadAllDictionary();
      }
      if (!userStore.init) {
        await userStore.getUserInfo();
      }
      if (!permissionStore.init) {
        await permissionStore.initRoutes(userStore.permissions);
      }
      if (router.hasRoute(to.name)) {
        next();
      } else {
        next('/');
      }
    } catch (error) {
      // MessagePlugin.error(error.message);
      console.error(error.message);
      next('/result/maintenance');
      NProgress.done();
    }
  } else {
    /* white list router */
    if (whiteListRouters.indexOf(to.path) !== -1) {
      next();
    } else {
      next('/login');
    }
    NProgress.done();
  }
});

router.afterEach(() => {
  NProgress.done();
});
