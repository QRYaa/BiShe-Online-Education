<template>
  <t-dialog
    v-model:visible="formVisible"
    :header="enable.headerTitle"
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
        <t-form-item label="姓名" name="name">
          <t-input v-model="formData.name" :style="{ width: '480px' }" placeholder="" />
        </t-form-item>
        <t-form-item label="性别" name="gender">
          <t-select v-model="formData.gender" :style="{ width: '200px' }">
            <t-option v-for="item in genderMap" :key="item.itemKey" :value="+item.itemKey" :label="item.itemValue" />
          </t-select>
        </t-form-item>
        <t-form-item label="电话号码" name="phoneNumber">
          <t-input v-model="formData.phoneNumber" :style="{ width: '480px' }" placeholder="" />
        </t-form-item>
        <t-form-item label="阶段类型" name="stageType">
          <t-select v-model="formData.stageType" :style="{ width: '200px' }">
            <t-option v-for="item in stageType" :key="item.itemKey" :value="+item.itemKey" :label="item.itemValue" />
          </t-select>
        </t-form-item>
        <t-form-item label="标签" name="tagIdList">
          <t-select v-model="formData.tagIdList" clearable multiple>
            <t-option v-for="item in state.tagIdList" :key="item.id" :value="item.id" :label="item.name" />
          </t-select>
        </t-form-item>
        <t-form-item label="可授课程" name="classIdList">
          <t-select v-model="formData.classIdList" clearable multiple>
            <t-option v-for="item in state.classList" :key="item.id" :value="item.id" :label="item.title" />
          </t-select>
        </t-form-item>
        <t-form-item label="状态" name="enable">
          <t-select v-model="formData.enable" :style="{ width: '200px' }">
            <t-option v-for="item in statusMap" :key="item.itemKey" :value="+item.itemKey" :label="item.itemValue" />
          </t-select>
        </t-form-item>
        <t-form-item label="简介" name="description">
          <t-input v-model="formData.description" :style="{ width: '480px' }" placeholder="" />
        </t-form-item>
      </t-form>
    </template>
  </t-dialog>
</template>

<script setup lang="ts">
import { FormRule, MessagePlugin, SubmitContext } from 'tdesign-vue-next';
import { onMounted, reactive, ref } from 'vue';

import * as classMgt from '@/api/system/classMgt';
import * as teacherMgt from '@/api/system/teacherMgt';
import * as teacherTagMgt from '@/api/system/teacherTagMgt';
import { useDictStore } from '@/store';

const state = reactive({
  tagIdList: [],
  classList: [],
});
const dictStore = useDictStore();
const statusMap = dictStore.getDictItem('STATUS');
const genderMap = dictStore.getDictItem('GENDER');
const stageType = dictStore.getDictItem('STAGE_TYPE');

export interface FormData {
  id?: number;
  name?: string;
  gender?: number;
  phoneNumber?: string;
  stageType?: number;
  tagIdList?: number[];
  classIdList?: number[];
  enable?: number;
  description?: string;
  departmentIdList?: Number[];
}
const emit = defineEmits(['fetch-data']);

const enable = reactive({
  headerTitle: '新增',
  departmentTree: [],
});

const formVisible = ref(false);
const initFormData = ref<FormData>({
  enable: 1,
});
const formData = ref({ ...initFormData.value });
const formRef = ref(null);
onMounted(() => {
  fetchTagIdListData();
  fetchClassListData();
});
const init = async (id: any) => {
  const data = await teacherMgt.get(id);
  formData.value = {
    ...data,
    tagIdList: data.tagIdList || [], // 确保 teachingFormList 始终是一个数组
  };
};

const show = (id: any) => {
  fetchTagIdListData();
  fetchClassListData();
  formVisible.value = true;
  if (id) {
    enable.headerTitle = '修改';
    init(id);
  } else {
    enable.headerTitle = '新增';
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
      await teacherMgt.update(formData.value);
    } else {
      await teacherMgt.save(formData.value);
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
const fetchTagIdListData = async () => {
  const records = await teacherTagMgt.listAll();
  state.tagIdList = records;
};
const fetchClassListData = async () => {
  const records = await classMgt.listAll();
  state.classList = records;
};

const rules: Record<string, FormRule[]> = {
  name: [{ required: true, message: '请输入姓名', type: 'error' }],
  phoneNumber: [{ required: true, message: '请输入电话号码', type: 'error' }],
  stageType: [{ required: true, message: '请选择阶段类型', type: 'error' }],
  enable: [{ required: true, message: '请选择状态', type: 'error' }],
};
</script>
