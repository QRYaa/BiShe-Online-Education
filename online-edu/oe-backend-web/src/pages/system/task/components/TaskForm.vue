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
        <t-form-item label="任务名称" name="name">
          <t-input v-model="formData.name" :style="{ width: '480px' }" placeholder="" />
        </t-form-item>
        <t-form-item label="处理器名称" name="handlerName">
          <t-input v-model="formData.handlerName" :style="{ width: '480px' }" placeholder="" />
        </t-form-item>
        <t-form-item label="参数" name="param">
          <t-input v-model="formData.param" :style="{ width: '480px' }" placeholder="" />
        </t-form-item>
        <t-form-item label="cron表达式" name="cron">
          <t-input v-model="formData.cron" :style="{ width: '480px' }" placeholder="请输入cron执行表达式" />
          <t-button theme="primary" @click="openDialog"> 生成表达式 </t-button>
        </t-form-item>
      </t-form>
      <t-dialog v-model:visible="showCron">
        <vue3-cron-core i18n="cn" max-height="300px" style="flex: 0.4" @change="changeCron" />
      </t-dialog>
    </template>
  </t-dialog>
</template>

<script setup lang="ts">
import { FormRule, MessagePlugin, SubmitContext } from 'tdesign-vue-next';
import { defineExpose, reactive, ref } from 'vue';

import * as taskMgt from '@/api/system/taskMgt';
import Vue3CronCore from '@/components/vue3-cron-core/Index.vue';

export interface FormData {
  id?: number;
  name?: string;
  handlerName?: string;
  param?: string;
  cron?: string;
}
const emit = defineEmits(['fetch-data']);

const state = reactive({
  headerTitle: '新增',
});

const formVisible = ref(false);
const initFormData = ref<FormData>({});
const formData = ref({ ...initFormData.value });
const formRef = ref(null);
const showCron = ref(false);
const init = async (id: any) => {
  const data = await taskMgt.get(id);
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

const openDialog = () => {
  showCron.value = true;
};

const changeCron = (cron) => {
  if (typeof cron !== 'string') return;
  formData.value.cron = cron;
  showCron.value = false;
};
const close = () => {
  formVisible.value = false;
  formData.value = { ...initFormData.value };
  formRef.value.reset();
};

const onSubmit = async (ctx: SubmitContext) => {
  if (ctx.validateResult === true) {
    if (formData.value.id > 0) {
      await taskMgt.update(formData.value);
    } else {
      await taskMgt.save(formData.value);
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
  name: [{ required: true, message: '请输入任务名称' }],
  handlerName: [{ required: true, message: '请输入处理器名称' }],
  cron: [{ required: true, message: '请输入cron表达式' }],
};
</script>
