<template>
  <t-dialog
    v-model:visible="formVisible"
    :mode="dialogMode"
    :header="state.headerTitle"
    :width="680"
    confirm-btn="保存"
    destroy-on-close
    :on-close="close"
    :on-confirm="onConfirm"
    :close-on-overlay-click="false"
  >
    <template #header>
      <div style="width: 100%">
        <span>{{ state.headerTitle }}</span>
        <t-icon name="rectangle" size="13px" style="float: right; margin-top: 0.72vh" @click="fullScreen" />
      </div>
    </template>
    <template #body>
      <!-- 表单内容 -->
      <t-form ref="formRef" :data="formData" :rules="rules" :label-width="100" @submit="onSubmit">
        <t-divider align="left">基本信息</t-divider>
        <t-row style="margin-bottom: 15px">
          <t-col :span="8">
            <t-form-item label="姓名" name="name">
              <t-input v-model="formData.name" :style="{ width: '200px' }" placeholder="" />
            </t-form-item>
            <t-form-item label="编号" name="code">
              <t-input v-model="formData.code" :style="{ width: '200px' }" placeholder="" />
            </t-form-item>
            <t-form-item label="性别" name="gender">
              <t-radio-group v-model="formData.gender">
                <t-radio :value="1">男</t-radio>
                <t-radio :value="2">女</t-radio>
              </t-radio-group>
            </t-form-item>
          </t-col>
          <t-col :span="4">
            <t-upload
              v-model="imgFile"
              theme="image"
              accept="image/*"
              :disabled="uploadDisabled"
              :show-image-file-name="false"
              :request-method="requestMethod"
              @remove="handleRemove"
              @success="handleSuccess"
            ></t-upload>
          </t-col>
        </t-row>
        <t-divider align="left">其他信息</t-divider>
        <t-form-item label="电话" name="tel">
          <t-input-adornment prepend="&nbsp;&nbsp;+86&nbsp;&nbsp;">
            <t-input v-model="formData.tel" :style="{ width: '420px' }" placeholder="" />
          </t-input-adornment>
        </t-form-item>
        <t-form-item label="标签" name="tagIdList">
          <t-select v-model="formData.tagIdList" clearable multiple filterable style="width: 480px">
            <t-option v-for="item in state.tagList" :key="item.id" :value="item.id" :label="item.name" />
          </t-select>
        </t-form-item>
        <t-form-item label="描述" name="description">
          <t-input v-model="formData.description" :style="{ width: '480px' }" placeholder="" />
        </t-form-item>
        <t-form-item label="详介" name="content">
          <div style="width: 100%">
            <ckeditor v-if="editor && config" v-model="formData.content" :editor="editor" :config="config" />
          </div>
        </t-form-item>
      </t-form>
    </template>
  </t-dialog>
</template>

<script setup lang="ts">
import { Ckeditor } from '@ckeditor/ckeditor5-vue';
import { FormRule, MessagePlugin, SubmitContext, UploadFile, UploadProps } from 'tdesign-vue-next';
import { defineExpose, inject, reactive, ref } from 'vue';

import * as teacherMgt from '@/api/oledu/teacherMgt';
import * as teacherTagMgt from '@/api/oledu/teacherTagMgt';
import * as baseMgt from '@/api/system/baseMgt';

export interface FormData {
  id?: number;
  avatar?: string;
  code?: string;
  name?: string;
  gender?: number;
  tel?: string;
  tagIdList?: number[];
  description?: string;
  content?: string;
}
const imgFile = ref([]);
const imgPre = import.meta.env.VITE_SYS_IMG_PRE;
const uploadDisabled = ref(false);

const config = inject('ckeditorConfig');
const editor = inject('ckeditorEditor');

const dialogMode = ref('modal');

const emit = defineEmits(['fetch-data']);

const state = reactive({
  headerTitle: '新增',
  tagList: [],
});

const formVisible = ref(false);
const initFormData = ref<FormData>({});
const formData = ref({ ...initFormData.value });
const formRef = ref(null);

const requestMethod: UploadProps['requestMethod'] = (file: UploadFile) => {
  uploadDisabled.value = true;
  return new Promise((resolve) => {
    const res = baseMgt.uploadImg(file.raw);
    res
      .then((v) => {
        formData.value.avatar = v;
        resolve({
          status: 'success',
          response: { url: `${imgPre}${v}` },
        });
      })
      .catch((e) => {
        console.error(e);
      })
      .finally(() => {
        uploadDisabled.value = false;
      });
  });
};
const handleSuccess: UploadProps['onSuccess'] = ({ file }) => {
  // MessagePlugin.success(`文件 ${file.name} 上传成功`);
  console.log(file);
};
const handleRemove = () => {
  formData.value.avatar = '';
};

const fullScreen = () => {
  if (dialogMode.value === 'full-screen') {
    dialogMode.value = 'modal';
  } else {
    dialogMode.value = 'full-screen';
  }
};

const init = async (id: any) => {
  const data = await teacherMgt.get(id);
  formData.value = data;
  if (formData.value.avatar) {
    imgFile.value = [
      {
        url: `${imgPre}${formData.value.avatar}`,
      },
    ];
  }
};

const show = (id: any) => {
  formVisible.value = true;
  fetchTagListData();
  if (id) {
    state.headerTitle = '修改';
    init(id);
  } else {
    state.headerTitle = '新增';
  }
};

const fetchTagListData = async () => {
  const data = await teacherTagMgt.listAll();
  state.tagList = data;
};

const close = () => {
  formVisible.value = false;
  formData.value = { ...initFormData.value };
  formRef.value.reset();
  dialogMode.value = 'modal';
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

const rules: Record<string, FormRule[]> = {
  code: [
    {
      required: true,
      message: '请输入编号',
      type: 'error',
    },
  ],
  name: [
    {
      required: true,
      message: '请输入姓名',
      type: 'error',
    },
  ],
};
</script>
