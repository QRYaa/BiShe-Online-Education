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
        <t-form-item label="应用" name="app">
          <t-select v-model="formData.appIdList" :style="{ width: '480px' }" multiple>
            <t-option v-for="item in state.subAppList" :key="item.id" :value="item.id" :label="item.name" />
          </t-select>
        </t-form-item>
      </t-form>
    </template>
  </t-dialog>
</template>

<script setup lang="ts">
import { FormRule, MessagePlugin, SubmitContext } from 'tdesign-vue-next';
import { defineExpose, reactive, ref } from 'vue';

import * as applicationMgt from '@/api/system/applicationMgt';
import * as userMgt from '@/api/system/userMgt';

const emit = defineEmits(['fetch-data']);

const formVisible = ref(false);
const initFormData = {
  appIdList: [],
};
const formData = ref({ ...initFormData });
const originData = ref({ ...initFormData });
const formRef = ref(null);

const userId = ref(0);
const state = reactive({
  appList: [],
  subAppList: [],
  headerTitle: '',
});

const fetchAppListData = async () => {
  const records = await applicationMgt.listAll();
  state.appList = records;
  state.subAppList = state.appList.filter((item) => originData.value.appIdList.includes(item.id) === false);
};

const show = async (id: any, name: any) => {
  state.headerTitle = `新增用户【${name}】的应用`;
  userId.value = id;
  originData.value = await userMgt.get(id);
  formVisible.value = true;
  fetchAppListData();
};

const close = () => {
  formVisible.value = false;
  formData.value = { ...initFormData };
  formRef.value.reset();
};
const onSubmit = async (ctx: SubmitContext) => {
  if (ctx.validateResult === true) {
    if (userId.value > 0) {
      await userMgt.addApp(userId.value, formData.value.appIdList);
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
