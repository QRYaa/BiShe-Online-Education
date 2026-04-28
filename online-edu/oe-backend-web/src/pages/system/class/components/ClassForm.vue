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
      <div v-if="loading">加载中...</div>
      <div v-else></div>
      <!-- 表单内容 -->
      <t-form-item label="类型" name="classType">
        <t-select v-model="formData.classType" clearable>
          <t-option v-for="item in state.typeNameList" :key="item.id" :value="item.id" :label="item.name" />
        </t-select>
      </t-form-item>
      <t-form-item label="模块" name="classModule">
        <t-select v-model="formData.classModuleId" clearable>
          <t-option v-for="item in filteredModules" :key="item.id" :value="item.id" :label="item.name" />
        </t-select>
      </t-form-item>
      <t-form ref="formRef" :data="formData" :rules="rules" :label-width="100" @submit="onSubmit">
        <t-form-item label="课程标题" name="title">
          <t-input v-model="formData.title" :style="{ width: '480px' }" placeholder="" />
        </t-form-item>
        <t-form-item label="提供机构" name="organization">
          <t-input v-model="formData.organization" :style="{ width: '480px' }" placeholder="" />
        </t-form-item>
        <t-form-item label="教师" name="teacherIdList">
          <t-select v-model="formData.teacherIdList" clearable multiple>
            <t-option v-for="item in state.teacherList" :key="item.id" :value="item.id" :label="item.name" />
          </t-select>
        </t-form-item>
        <t-form-item label="适用对象" name="suitableUser">
          <t-input v-model="formData.suitableUser" :style="{ width: '480px' }" placeholder="" />
        </t-form-item>
        <t-form-item label="授课形式" name="teachingFormList">
          <t-select v-model="formData.teachingFormList" :style="{ width: '200px' }" multiple>
            <t-option v-for="item in formMap" :key="item.itemKey" :value="+item.itemKey" :label="item.itemValue" />
          </t-select>
        </t-form-item>
        <t-form-item label="备注" name="remark">
          <t-input v-model="formData.remark" :style="{ width: '480px' }" placeholder="" />
        </t-form-item>
      </t-form>
    </template>
  </t-dialog>
</template>

<script setup lang="ts">
import { FormRule, MessagePlugin, SubmitContext } from 'tdesign-vue-next';
import { computed, defineExpose, onMounted, reactive, ref, watch } from 'vue';

import * as classMgt from '@/api/system/classMgt';
import * as classModuleMgt from '@/api/system/classModuleMgt';
import * as classTypeMgt from '@/api/system/classTypeMgt';
import * as teacherMgt from '@/api/system/teacherMgt';
import { useDictStore } from '@/store';

export interface FormData {
  id?: number;
  title?: string;
  organization?: string;
  suitableUser?: string;
  teachingFormList?: number[];
  remark?: string;
  classModuleId?: number;
  teacherIdList?: number[];
  classType?: number;
}
const emit = defineEmits(['fetch-data']);
const loading = ref(true);
const state = reactive({
  headerTitle: '新增',
  moduleNameList: [],
  teacherList: [],
  typeNameList: [],
});

const formVisible = ref(false);
const initFormData = ref<FormData>({});
const formData = ref({ ...initFormData.value });
const formRef = ref(null);

const dictStore = useDictStore();
const formMap = dictStore.getDictItem('TEACHING_FORM');
const classModuleMap = new Map();

onMounted(() => {
  fetchData();
});

const init = async (id: any, classModuleId: any) => {
  const data = await classMgt.get(id);
  formData.value = {
    ...data,
    classType: data.classType || classModuleMap.get(classModuleId),
    teachingFormList: data.teachingFormList || [], // 确保 teachingFormList 始终是一个数组
    teacherIdList: data.teacherIdList || [],
  };
};

const show = (id: any, classModuleId: any) => {
  fetchData();
  formVisible.value = true;
  if (id) {
    state.headerTitle = '修改';
    init(id, classModuleId);
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
      await classMgt.update(formData.value);
    } else {
      await classMgt.save(formData.value);
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
watch(
  () => formData.value.classType,
  (newVal, oldVal) => {
    if (oldVal != null) {
      if (newVal !== oldVal) {
        formData.value.classModuleId = null;
      }
    }
  },
);

defineExpose({
  show,
});

const fetchData = async () => {
  loading.value = true;
  try {
    await Promise.all([fetchModuleNameListData(), fetchTeacherListData(), fetchTypeNameListData()]);
  } catch (e) {
    console.log(e);
  } finally {
    loading.value = false;
  }
};

const fetchModuleNameListData = async () => {
  const records = await classModuleMgt.listAll();
  state.moduleNameList = records;
  records.forEach((item) => {
    classModuleMap.set(item.id, item.classTypeId);
  });
};
const fetchTeacherListData = async () => {
  const records = await teacherMgt.listAll();
  state.teacherList = records;
};
const fetchTypeNameListData = async () => {
  const records = await classTypeMgt.listAll();
  state.typeNameList = records;
};
const filteredModules = computed(() => {
  return state.moduleNameList.filter((module) => module.classTypeId === formData.value.classType);
});

const rules: Record<string, FormRule[]> = {
  title: [{ required: true, message: '请输入标题', type: 'error' }],
  // classModule: [{ required: true, message: '请选择模块', type: 'error' }],
  speaker: [{ required: true, message: '请输入主讲人姓名', type: 'error' }],
};
</script>
