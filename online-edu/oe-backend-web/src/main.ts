/* eslint-disable simple-import-sort/imports */
import TDesign from 'tdesign-vue-next';
import { createApp } from 'vue';

import App from './App.vue';
import router from './router';
import { store } from './store';
import i18n from './locales';

import { useUserStore } from '@/store';

import 'tdesign-vue-next/es/style/index.css';
import '@/style/index.less';
import './permission';
import CKEditorConfigPlugin from '@/plugins/ckeditor-config';

const app = createApp(App);

app.use(TDesign);
app.use(store);
app.use(router);
app.use(i18n);
app.use(CKEditorConfigPlugin);
app.mount('#app');

const userStore = useUserStore();

function can(target) {
  let result = false;
  result = target.some((item) => userStore.permissions.includes(item));
  return result;
}

app.directive('permissions', {
  mounted(el, binding) {
    const { value } = binding;
    if (value) {
      if (!can(value) && el.parentNode) {
        el.parentNode.removeChild(el);
      }
    }
  },
});
