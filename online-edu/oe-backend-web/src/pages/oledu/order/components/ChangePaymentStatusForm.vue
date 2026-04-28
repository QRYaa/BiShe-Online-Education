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
        <t-form-item label="支付状态" name="paymentStatus">
          <t-select v-model="formData.paymentStatus">
            <t-option
              v-for="item in paymentStatusMap"
              :key="item.itemKey"
              :value="+item.itemKey"
              :label="item.itemValue"
            />
          </t-select>
        </t-form-item>
      </t-form>
    </template>
  </t-dialog>
</template>

<script setup lang="ts">
import { FormRule, MessagePlugin, SubmitContext } from 'tdesign-vue-next';
import { defineExpose, reactive, ref } from 'vue';

import * as orderMgt from '@/api/oledu/orderMgt';
import { useDictStore } from '@/store';

const emit = defineEmits(['fetch-data']);

const dictStore = useDictStore();
const paymentStatusMap = dictStore.getDictItem('ORDER_STATUS');
const formVisible = ref(false);
const initFormData = {
  id: 0,
  paymentStatus: 0,
};
const formData = ref({ ...initFormData });

const formRef = ref(null);

const state = reactive({
  headerTitle: '',
});

const show = async (row: any) => {
  state.headerTitle = `修改订单【${row.code}】支付状态`;
  const data = await orderMgt.get(row.id);
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
    await orderMgt.changePaymentStatus(formData.value.id, formData.value.paymentStatus);
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
  newPassword: [{ required: true, message: '', type: 'error' }],
};
</script>
