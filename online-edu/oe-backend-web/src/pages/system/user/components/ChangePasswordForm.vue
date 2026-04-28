<template>
  <t-dialog
    v-model:visible="formVisible"
    :header="state.headerTitle"
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
        <t-form-item label="新密码" name="newPassword">
          <t-input v-model="formData.newPassword" type="password" placeholder="请输入密码" />
        </t-form-item>
      </t-form>
    </template>
  </t-dialog>
</template>

<script setup lang="ts">
import { FormRule, MessagePlugin, SubmitContext } from 'tdesign-vue-next';
import { defineExpose, reactive, ref } from 'vue';

import * as userMgt from '@/api/system/userMgt';

// const emit = defineEmits(['fetch-data']);

const formVisible = ref(false);
const initFormData = {
  id: 0,
  newPassword: '',
};
const formData = ref({ ...initFormData });

const formRef = ref(null);

const state = reactive({
  user: { id: 0 },
  headerTitle: '',
});

const show = (row: any) => {
  state.user = row;
  state.headerTitle = `修改用户【${row.username}】密码`;
  formVisible.value = true;
};

const close = () => {
  formVisible.value = false;
  formData.value = { ...initFormData };
  formRef.value.reset();
};
const onSubmit = async (ctx: SubmitContext) => {
  if (ctx.validateResult === true) {
    userMgt.changePassword(state.user.id, formData.value.newPassword);
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

const rules: Record<string, FormRule[]> = {
  newPassword: [{ required: true, message: '请输入新密码', type: 'error' }],
};
</script>
