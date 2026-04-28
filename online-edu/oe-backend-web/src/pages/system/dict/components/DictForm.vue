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
        <t-form-item label="名称" name="name">
          <t-input v-model="formData.name" :style="{ width: '480px' }" placeholder="请输入名称" />
        </t-form-item>
        <t-form-item label="编码" name="code">
          <t-input v-model="formData.code" :style="{ width: '480px' }" placeholder="请输入编码" />
        </t-form-item>
        <t-form-item label="描述" name="description">
          <t-input v-model="formData.description" :style="{ width: '480px' }" placeholder="请输入描述" />
        </t-form-item>
      </t-form>
    </template>
  </t-dialog>
</template>

<script setup lang="ts">
import { FormRule, MessagePlugin, SubmitContext } from 'tdesign-vue-next';
import { defineExpose, reactive, ref } from 'vue';

import * as dictMgt from '@/api/system/dictMgt';

const emit = defineEmits(['fetch-data']);

const state = reactive({
  headerTitle: '新增',
});

const formVisible = ref(false);
const initFormData = {
  id: 0,
  name: '',
  code: '',
  description: '',
};
const formData = ref({ ...initFormData });
const formRef = ref(null);

const init = async (id: any) => {
  const data = await dictMgt.dictGet(id);
  formData.value = data;
};

const show = (id: any) => {
  formVisible.value = true;
  if (id) {
    state.headerTitle = '修改';
    init(id);
  } else {
    state.headerTitle = '新增';
  }
};

const close = () => {
  formVisible.value = false;
  formData.value = { ...initFormData };
  formRef.value.reset();
};

const onSubmit = async (ctx: SubmitContext) => {
  if (ctx.validateResult === true) {
    if (formData.value.id > 0) {
      await dictMgt.dictUpdate(formData.value);
    } else {
      await dictMgt.dictSave(formData.value);
    }
    emit('fetch-data');
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
  name: [{ required: true, message: '请输入名称', type: 'error' }],
  code: [{ required: true, message: '请输入编码', type: 'error' }],
};
</script>
