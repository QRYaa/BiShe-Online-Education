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
        <t-form-item label="状态" name="status">
          <t-select v-model="formData.status" :style="{ width: '480px' }" clearable>
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

import * as courseMgt from '@/api/oledu/courseMgt';
import { useDictStore } from '@/store';

const dictStore = useDictStore();
const statusMap = dictStore.getDictItem('COURSE_STATUS');
const emit = defineEmits(['fetch-data']);

const formVisible = ref(false);
const initFormData = {
  id: 0,
  status: 0,
};
const formData = ref({ ...initFormData });

const formRef = ref(null);

const state = reactive({
  headerTitle: '',
});

const show = async (row: any) => {
  state.headerTitle = `设置课程【${row.name}】的状态`;
  const data = await courseMgt.get(row.id);
  formData.value = data;
  formVisible.value = true;
};

const close = () => {
  formVisible.value = false;
  formData.value = { ...initFormData };
  formRef.value.reset();
};
const onSubmit = async (ctx: SubmitContext) => {
  if (ctx.validateResult === true) {
    if (formData.value.id > 0) {
      await courseMgt.changeStatus(formData.value);
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

const rules: Record<string, FormRule[]> = {};
</script>
