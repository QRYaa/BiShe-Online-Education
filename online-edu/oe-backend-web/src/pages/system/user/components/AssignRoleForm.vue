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
        <t-form-item label="角色" name="role">
          <t-select v-model="formData.roleIdList" :style="{ width: '480px' }" clearable multiple>
            <t-option v-for="item in state.roleList" :key="item.id" :value="item.id" :label="item.name" />
          </t-select>
        </t-form-item>
      </t-form>
    </template>
  </t-dialog>
</template>

<script setup lang="ts">
import { FormRule, MessagePlugin, SubmitContext } from 'tdesign-vue-next';
import { defineExpose, reactive, ref } from 'vue';

import * as roleMgt from '@/api/system/roleMgt';
import * as userMgt from '@/api/system/userMgt';

const emit = defineEmits(['fetch-data']);

const formVisible = ref(false);
const initFormData = {
  id: 0,
  roleIdList: [],
};
const formData = ref({ ...initFormData });

const formRef = ref(null);

const state = reactive({
  roleList: [],
  headerTitle: '',
});

const fetchRoleListData = async () => {
  const records = await roleMgt.listAll();
  state.roleList = records;
};

const show = async (row: any) => {
  state.headerTitle = `设置用户【${row.username}】的角色`;
  const data = await userMgt.get(row.id);
  formData.value = data;
  formVisible.value = true;
  fetchRoleListData();
};

const close = () => {
  formVisible.value = false;
  formData.value = { ...initFormData };
  formRef.value.reset();
};
const onSubmit = async (ctx: SubmitContext) => {
  if (ctx.validateResult === true) {
    if (formData.value.id > 0) {
      await userMgt.assignRole(formData.value);
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
