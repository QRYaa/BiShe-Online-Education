import { defineStore } from 'pinia';

import * as baseMgt from '@/api/system/baseMgt';
import * as userMgt from '@/api/system/userMgt';
import { TOKEN_NAME } from '@/config/global';
// import { usePermissionStore } from '@/store';
import type { UserInfo } from '@/types/interface';

const InitUserInfo: UserInfo = {
  name: '', // 用户名，用于展示在页面右上角头像处
};

export const useUserStore = defineStore('user', {
  state: () => ({
    init: false,
    // token: 'main_token', // 默认token不走权限
    token: localStorage.getItem(TOKEN_NAME) || '',
    userInfo: { ...InitUserInfo },
    ticket: '',
  }),
  getters: {
    permissions: (state) => {
      return state.userInfo?.permissions;
    },
  },
  actions: {
    async login(userInfo: Record<string, unknown>) {
      const { token, ticket } = await baseMgt.login(userInfo);
      this.token = token;
      this.ticket = ticket;
      localStorage.setItem(TOKEN_NAME, token);
    },
    async getUserInfo() {
      this.userInfo = await userMgt.userInfo();
      // console.log(this.userInfo);
      this.init = true;
    },
    async logout() {
      await baseMgt.logout();
      localStorage.removeItem(TOKEN_NAME);
      this.token = null;
      this.userInfo = { ...InitUserInfo };
    },
    async cleanLocalStorage() {
      localStorage.removeItem(TOKEN_NAME);
      this.token = null;
      this.userInfo = { ...InitUserInfo };
    },
  },
  // persist: {
  //   afterRestore: () => {
  //     const permissionStore = usePermissionStore();
  //     permissionStore.initRoutes();
  //   },
  //   key: 'user',
  //   paths: ['token'],
  // },
});
