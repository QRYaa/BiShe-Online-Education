<template>
  <t-dialog
    v-model:visible="formVisible"
    header="修改密码"
    :width="680"
    confirm-btn="保存"
    destroy-on-close
    :on-close="close"
    :on-confirm="onConfirm"
    :close-on-overlay-click="false"
  >
    <template #body>
      <!-- 表单内容 -->
      <t-form ref="formRef" :data="formData" :rules="rules" :label-width="100" @submit="onSubmit">
        <t-form-item label="原密码" name="oldPassword">
          <t-input v-model="formData.oldPassword" type="password" placeholder="请输入密码" />
        </t-form-item>
        <t-form-item label="新密码" name="newPassword">
          <t-input v-model="formData.newPassword" type="password" placeholder="请输入密码" />
        </t-form-item>
        <t-form-item label="确认密码" name="rePassword">
          <t-input v-model="formData.rePassword" type="password" placeholder="请重新输入密码" />
        </t-form-item>
      </t-form>
    </template>
  </t-dialog>
</template>

<script setup lang="ts">
import { FormRule, MessagePlugin, SubmitContext } from 'tdesign-vue-next';
import { defineExpose, ref } from 'vue';

import * as userMgt from '@/api/system/userMgt';

// const emit = defineEmits(['fetch-data']);

const formVisible = ref(false);
const initFormData = {
  oldPassword: '',
  newPassword: '',
  rePassword: '',
};
const formData = ref({ ...initFormData });
const formRef = ref(null);

const show = () => {
  formVisible.value = true;
};

const close = () => {
  formVisible.value = false;
  formData.value = { ...initFormData };
  formRef.value.reset();
};

const onSubmit = async (ctx: SubmitContext) => {
  if (ctx.validateResult === true) {
    await userMgt.changeMyPassword(formData.value.oldPassword, formData.value.newPassword);
    close();
    MessagePlugin.success('保存成功');
  } else {
    console.log('Errors: ', ctx.validateResult);
  }
};

const onConfirm = () => {
  formRef.value.submit();
};

defineExpose({
  show,
});

const equalToPassword = (value: string) => {
  return formData.value.newPassword === value;
};

const rules: Record<string, FormRule[]> = {
  oldPassword: [{ required: true, message: '请输入原密码', type: 'error' }],
  newPassword: [{ required: true, message: '请输入新密码', type: 'error' }],
  rePassword: [{ validator: equalToPassword, message: '两次输入的密码不一致' }],
};
</script>
