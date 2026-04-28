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
          <t-input v-model="formData.name" :style="{ width: '200px' }" placeholder="" />
        </t-form-item>
        <t-form-item label="排序" name="sort">
          <t-input-number v-model="formData.sort" placeholder="排序号" />
        </t-form-item>
        <t-form-item label="描述" name="description">
          <t-input v-model="formData.description" :style="{ width: '480px' }" placeholder="" />
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

import * as chapterMgt from '@/api/oledu/chapterMgt';

export interface FormData {
  id?: number;
  courseId?: number;
  sort?: number;
  name?: string;
  description?: string;
  remark?: string;
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
  const data = await chapterMgt.get(id);
  formData.value = data;
};

const setCourseId = async (id: any) => {
  formData.value.courseId = id;
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
      await chapterMgt.update(formData.value);
    } else {
      await chapterMgt.save(formData.value);
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
  setCourseId,
});

const rules: Record<string, FormRule[]> = {
  name: [
    {
      required: true,
      message: '请输入名称',
      type: 'error',
    },
  ],
  sort: [
    {
      required: true,
      message: '请输入排序',
      type: 'error',
    },
  ],
};
</script>
