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
        <t-form-item label="排序" name="sort">
          <t-input-number v-model="formData.sort" placeholder="排序号" />
        </t-form-item>
        <t-form-item label="路径" name="paths">
          <t-textarea v-model="formData.paths" :style="{ width: '480px' }" placeholder="支持多地址，以','分隔" />
        </t-form-item>
      </t-form>
    </template>
  </t-dialog>
</template>

<script setup lang="ts">
import { FormRule, MessagePlugin, SubmitContext } from 'tdesign-vue-next';
import { defineExpose, reactive, ref } from 'vue';

import * as permissionMgt from '@/api/system/permissionMgt';

const emit = defineEmits(['fetch-data']);

const state = reactive({
  headerTitle: '新增',
});
const formVisible = ref(false);
const formRef = ref(null);
const initFormData = {
  id: 0,
  name: '',
  code: '',
  paths: '',
  parentId: -1,
  sort: 0,
  enable: 1,
};
const formData = ref({ ...initFormData });

const init = async (id: any) => {
  const data = await permissionMgt.get(id);
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

const showForSub = (row: any) => {
  formVisible.value = true;
  state.headerTitle = `新增【${row.name}(${row.code})】下级权限`;
  formData.value.parentId = row.id;
};

const close = () => {
  formVisible.value = false;
  formData.value = { ...initFormData };
  formRef.value.reset();
};
const onSubmit = async (ctx: SubmitContext) => {
  if (ctx.validateResult === true) {
    if (formData.value.id > 0) {
      await permissionMgt.update(formData.value);
    } else {
      await permissionMgt.save(formData.value);
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
  showForSub,
});

const rules: Record<string, FormRule[]> = {
  name: [{ required: true, message: '请输入名称', type: 'error' }],
  code: [{ required: true, message: '请输入编码', type: 'error' }],
};
</script>
