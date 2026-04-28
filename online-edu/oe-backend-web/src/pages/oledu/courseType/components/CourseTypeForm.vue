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
        <t-form-item label="编号" name="code">
          <t-input v-model="formData.code" :style="{ width: '200px' }" placeholder="" />
        </t-form-item>
        <t-form-item label="名称" name="name">
          <t-input v-model="formData.name" :style="{ width: '200px' }" placeholder="" />
        </t-form-item>
        <t-form-item label="排序" name="sort">
          <t-input-number v-model="formData.sort" placeholder="排序号" />
        </t-form-item>
        <t-form-item label="状态" name="enable">
          <t-radio-group v-model="formData.enable">
            <t-radio v-for="item in statusMap" :key="item.itemKey" :value="+item.itemKey">{{ item.itemValue }}</t-radio>
          </t-radio-group>
        </t-form-item>
        <t-form-item label="备注" name="remark">
          <t-textarea v-model="formData.remark" :style="{ width: '480px' }" placeholder="请输入备注" />
        </t-form-item>
      </t-form>
    </template>
  </t-dialog>
</template>

<script setup lang="ts">
import { FormRule, MessagePlugin, SubmitContext } from 'tdesign-vue-next';
import { defineExpose, reactive, ref } from 'vue';

import * as courseTypeMgt from '@/api/oledu/courseTypeMgt';
import { useDictStore } from '@/store';

export interface FormData {
  id?: number;
  code?: string;
  name?: string;
  sort?: number;
  remark?: string;
  enable?: number;
}
const emit = defineEmits(['fetch-data']);

const state = reactive({
  headerTitle: '新增',
});
const dictStore = useDictStore();
const statusMap = dictStore.getDictItem('STATUS');

const formVisible = ref(false);
const initFormData = ref<FormData>({
  enable: 1,
});
const formData = ref({ ...initFormData.value });
const formRef = ref(null);

const init = async (id: any) => {
  const data = await courseTypeMgt.get(id);
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
  formData.value = { ...initFormData.value };
  formRef.value.reset();
};

const onSubmit = async (ctx: SubmitContext) => {
  if (ctx.validateResult === true) {
    if (formData.value.id > 0) {
      await courseTypeMgt.update(formData.value);
    } else {
      await courseTypeMgt.save(formData.value);
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
  code: [{ required: true, message: '请输入编码', type: 'error' }],
  name: [{ required: true, message: '请输入名称', type: 'error' }],
  enable: [{ required: true, message: '请选择状态', type: 'error' }],
};
</script>
