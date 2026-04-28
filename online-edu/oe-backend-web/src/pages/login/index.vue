<template>
  <div class="login-wrapper">
    <login-header />

    <div class="login-container">
      <div class="title-container">
        <h1 class="title margin-no">{{ $t('pages.login.loginTitle') }}</h1>
        <h1 class="title">{{ $t('pages.login.systemTitle') }}</h1>
        <div class="sub-title">
          <!-- <p class="tip">{{ type == 'register' ? $t('pages.login.existAccount') : $t('pages.login.noAccount') }}</p>
          <p class="tip" @click="switchType(type == 'register' ? 'login' : 'register')">
            {{ type == 'register' ? $t('pages.login.signIn') : $t('pages.login.createAccount') }}
          </p> -->
        </div>
      </div>

      <login v-if="type == '1'" />
      <login-sso v-else-if="type == '9'" />
      <empty v-else-if="type == '0'" />
      <tdesign-setting />
    </div>

    <footer class="copyright">Copyright @ 2015-{{ new Date().getFullYear() }} gziia. All Rights Reserved</footer>
  </div>
</template>
<script lang="ts">
export default {
  name: 'LoginIndex',
};
</script>
<script setup lang="ts">
import { onMounted, ref } from 'vue';

// import { useRoute } from 'vue-router';
import TdesignSetting from '@/layouts/setting.vue';

import Empty from './components/Empty.vue';
import LoginHeader from './components/Header.vue';
import Login from './components/Login.vue';
import LoginSso from './components/LoginSso.vue';

const LOGIN_TYPE = import.meta.env.VITE_LOGIN_TYPE;
// import Register from './components/Register.vue';

const type = ref(LOGIN_TYPE);
// const route = useRoute();

onMounted(() => {
  // const qType = route.query.type as string;
  // type.value = LOGIN_TYPE;
  // const redirect = route.query.redirect as string;
  // if (redirect) {
  //   const redirectUrl = decodeURIComponent(redirect);
  //   console.log('redirect:', redirectUrl);
  //   if (redirectUrl.startsWith('http')) {
  //     type.value = qType;
  //   }
  // }
  // console.log('login onMounted', type.value);
});
</script>

<style lang="less" scoped>
@import './index.less';
</style>
