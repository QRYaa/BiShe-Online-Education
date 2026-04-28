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
          <t-input v-model="formData.name" :style="{ width: '480px' }" placeholder="" />
        </t-form-item>
        <t-form-item label="备注" name="remark">
          <t-input v-model="formData.remark" :style="{ width: '480px' }" placeholder="" />
        </t-form-item>
        <t-form-item label="状态" name="enable">
          <t-select v-model="formData.enable" :style="{ width: '200px' }">
            <t-option v-for="item in statusMap" :key="item.itemKey" :value="+item.itemKey" :label="item.itemValue" />
          </t-select>
        </t-form-item>
      </t-form>
    </template>
  </t-dialog>
</template>

<script setup lang="ts">
import { FormRule, MessagePlugin, SubmitContext } from 'tdesign-vue-next';
import { defineExpose, reactive, ref } from 'vue';

import * as classTypeMgt from '@/api/system/classTypeMgt';
import { useDictStore } from '@/store';

export interface FormData {
  id?: number;
  name?: string;
  remark?: string;
  enable?: number;
}
const emit = defineEmits(['fetch-data']);

const state = reactive({
  headerTitle: '新增',
});

const formVisible = ref(false);
const initFormData = ref<FormData>({});
const formData = ref({ ...initFormData.value });
const formRef = ref(null);
const dictStore = useDictStore();

const statusMap = dictStore.getDictItem('STATUS');

const init = async (id: any) => {
  const data = await classTypeMgt.get(id);
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
      await classTypeMgt.update(formData.value);
      MessagePlugin.success('修改成功');
    } else {
      await classTypeMgt.save(formData.value);
      MessagePlugin.success('新增成功');
    }
    emit('fetch-data');
    close();
    
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
  enable: [{ required: true, message: '请选择状态', type: 'error' }],
};
</script>
