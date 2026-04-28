<template>
  <t-dialog
    v-model:visible="formVisible"
    :header="state.headerTitle"
    :width="400"
    confirm-btn="保存"
    destroy-on-close
    :on-close="close"
    :on-confirm="onConfirm"
    :close-on-overlay-click="false"
  >
    <template #body>
      <!-- 表单内容 -->
      <t-form ref="formRef" :data="formData" :rules="rules" :label-width="100" @submit="onSubmit">
        <t-form-item label="名称" name="itemValue">
          <t-input v-model="formData.itemValue" :style="{ width: '480px' }" placeholder="请输入名称" />
        </t-form-item>
        <t-form-item label="值" name="itemKey">
          <t-input v-model="formData.itemKey" :style="{ width: '480px' }" placeholder="请输入值" />
        </t-form-item>
        <t-form-item label="排序" name="sort">
          <t-input-number v-model="formData.sort" placeholder="排序号" />
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
const initFormData = {
  id: 0,
  dictId: 0,
  itemKey: '',
  itemValue: '',
  name: '',
  code: '',
  sort: 0,
};
const formData = ref({ ...initFormData });

const state = reactive({
  headerTitle: '新增',
});

const formVisible = ref(false);
const formRef = ref(null);

const init = async (id: any) => {
  const data = await dictMgt.itemGet(id);
  formData.value = data;
};

const showForEdit = (id: any) => {
  formVisible.value = true;
  state.headerTitle = '修改';
  if (id) {
    init(id);
  }
};

const showForCreate = (dictId: any) => {
  formVisible.value = true;
  state.headerTitle = '新增';
  formData.value.dictId = dictId;
};

const close = () => {
  formVisible.value = false;
  formData.value = { ...initFormData };
  formRef.value.reset();
};

const onSubmit = async (ctx: SubmitContext) => {
  if (ctx.validateResult === true) {
    if (formData.value.id > 0) {
      await dictMgt.itemUpdate(formData.value);
    } else {
      await dictMgt.itemSave(formData.value);
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
  showForEdit,
  showForCreate,
});

const rules: Record<string, FormRule[]> = {
  itemValue: [{ required: true, message: '请输入名称', type: 'error' }],
  itemKey: [{ required: true, message: '请输入值', type: 'error' }],
};
</script>
