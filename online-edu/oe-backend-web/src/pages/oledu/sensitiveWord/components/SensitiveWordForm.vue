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
        <t-form-item label="敏感词" name="sensitiveWord">
          <t-input v-model="formData.sensitiveWord" :style="{ width: '480px' }" placeholder="" />
        </t-form-item>
        <t-form-item label="替换词" name="replaceWord">
          <t-input v-model="formData.replaceWord" :style="{ width: '480px' }" placeholder="" />
        </t-form-item>
      </t-form>
    </template>
  </t-dialog>
</template>

<script setup lang="ts">
import { FormRule, MessagePlugin, SubmitContext } from 'tdesign-vue-next';
import { defineExpose, reactive, ref } from 'vue';

import * as sensitiveWordMgt from '@/api/oledu/sensitiveWordMgt';

export interface FormData {
  id?: number;
  sensitiveWord?: string;
  replaceWord?: string;
  createTime?: string;
}
const emit = defineEmits(['fetch-data']);

const state = reactive({
  headerTitle: '新增',
});

const formVisible = ref(false);
const initFormData = ref<FormData>({});
const formData = ref({ ...initFormData.value });
const formRef = ref(null);

const init = async (id: any) => {
  const data = await sensitiveWordMgt.get(id);
  formData.value = data;
};

const show = (id: any) => {
  formVisible.value = true;
  if (id) {
    state.headerTitle = '修改';
    init(id);
  } else {
    state.headerTitle = '新增';
    formData.value.replaceWord = '**';
  }
};

const close = () => {
  formVisible.value = false;
  formData.value = { ...initFormData.value };
  formRef.value.reset();
};

const onSubmit = async (ctx: SubmitContext) => {
  if (ctx.validateResult === true) {
    if (formData.value.id > 0) {
      await sensitiveWordMgt.update(formData.value);
    } else {
      await sensitiveWordMgt.save(formData.value);
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
  sensitiveWord: [{ required: true, message: '敏感词不能为空', type: 'error' }],
};
</script>
