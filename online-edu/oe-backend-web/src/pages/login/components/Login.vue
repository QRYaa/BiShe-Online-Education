<template>
  <t-form
    ref="form"
    :class="['item-container', 'login-password']"
    :data="formData"
    :rules="FORM_RULES"
    label-width="0"
    @submit="onSubmit"
  >
    <t-form-item name="username" :hidden="!loginEnabled">
      <t-input v-model="formData.username" clearable size="large" :placeholder="`${$t('pages.login.input.account')}`">
        <template #prefix-icon>
          <t-icon name="user" />
        </template>
      </t-input>
    </t-form-item>

    <t-form-item name="password" :hidden="!loginEnabled">
      <t-input
        v-model="formData.password"
        size="large"
        :type="showPsw ? 'text' : 'password'"
        clearable
        :placeholder="`${$t('pages.login.input.password')}`"
      >
        <template #prefix-icon>
          <t-icon name="lock-on" />
        </template>
        <template #suffix-icon>
          <t-icon :name="showPsw ? 'browse' : 'browse-off'" @click="showPsw = !showPsw" />
        </template>
      </t-input>
    </t-form-item>

    <t-form-item v-if="captchaEnabled" name="captchaCode">
      <t-input v-model="formData.captchaCode" size="large" :placeholder="$t('pages.login.input.captchaCode')">
        <template #label>
          <t-icon name="secured" />
        </template>
      </t-input>
      <div class="login-code">
        <img :src="codeUrl" @click="getCaptchaCode" />
      </div>
    </t-form-item>

    <t-form-item class="btn-container" :hidden="!loginEnabled">
      <t-button block size="large" type="submit"> {{ $t('pages.login.signIn') }} </t-button>
    </t-form-item>
  </t-form>
</template>

<script setup lang="ts">
// import QrcodeVue from 'qrcode.vue';
import type { FormInstanceFunctions, FormRule, SubmitContext } from 'tdesign-vue-next';
import { MessagePlugin } from 'tdesign-vue-next';
import { onMounted, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import * as baseMgt from '@/api/system/baseMgt';
// import { useCounter } from '@/hooks';
import { t } from '@/locales';
import { useDictStore, usePermissionStore, useUserStore } from '@/store';

const userStore = useUserStore();

const INITIAL_DATA = {
  username: 'admin',
  password: 'admin',
  cid: '',
  captchaCode: '',
  sso: false,
};

const FORM_RULES: Record<string, FormRule[]> = {
  phone: [{ required: true, message: t('pages.login.required.phone'), type: 'error' }],
  username: [{ required: true, message: t('pages.login.required.account'), type: 'error' }],
  password: [{ required: true, message: t('pages.login.required.password'), type: 'error' }],
  captchaCode: [{ required: true, message: t('pages.login.required.captchaCode'), type: 'error' }],
};

const form = ref<FormInstanceFunctions>();
const formData = ref({ ...INITIAL_DATA });
const showPsw = ref(false);
const loginEnabled = ref(false);
// 验证码开关
const captchaEnabled = ref(false);
const codeUrl = ref('');

// query参数
const qType = ref('');
const qRedirect = ref('');
const isSso = ref(false);

const router = useRouter();
const route = useRoute();
const permissionStore = usePermissionStore();
const dictStore = useDictStore();

const getCaptchaCode = async () => {
  const record = await baseMgt.captcha();
  captchaEnabled.value = record.captchaEnabled;
  if (captchaEnabled.value) {
    codeUrl.value = `data:image/gif;base64,${record.image}`;
    formData.value.cid = record.cid;
  }
};

const onSubmit = async (ctx: SubmitContext) => {
  if (ctx.validateResult === true) {
    try {
      await userStore.login(formData.value);
      await userStore.getUserInfo();
      await dictStore.loadAllDictionary();
      await permissionStore.initRoutes(userStore.permissions);

      MessagePlugin.success('登录成功');
      // const redirect = route.query.redirect as string;
      // const redirectUrl = redirect ? decodeURIComponent(redirect) : '/dashboard';
      // router.push(redirectUrl);
      if (isSso.value) {
        const { ticket } = userStore;
        window.location.href = `${qRedirect.value}?ticket=${ticket}`;
      } else {
        router.push('/');
      }
    } catch (e) {
      console.log(e);
      getCaptchaCode();
      // MessagePlugin.error(e.message);
    }
  }
};

const initialize = () => {
  const type = route.query.type as string;
  const redirect = route.query.redirect as string;
  qType.value = type;
  qRedirect.value = decodeURIComponent(redirect || '');
  isSso.value = type === 'sso' && redirect && redirect.startsWith('http');
  formData.value.sso = isSso.value;
};

onMounted(async () => {
  initialize();
  if (userStore.token) {
    if (isSso.value) {
      const data = await baseMgt.getTicket();
      console.log('=====ticket', data);
      window.location.href = `${qRedirect.value}?ticket=${data}`;
    } else {
      router.push('/');
    }
  } else {
    loginEnabled.value = true;
    getCaptchaCode();
  }
});
</script>

<style lang="less" scoped>
@import '../index.less';
</style>
