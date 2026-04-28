<template>
  <t-dialog
    v-model:visible="formVisible"
    :mode="dialogMode"
    :width="680"
    confirm-btn="保存"
    :on-close="close"
    :on-confirm="onConfirm"
    :close-on-overlay-click="false"
    destroy-on-close
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
        <t-form-item label="封面图" name="image">
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
        </t-form-item>
        <t-form-item label="编号" name="code">
          <t-input v-model="formData.code" :style="{ width: '400px' }" placeholder="" />
        </t-form-item>
        <t-form-item label="标题" name="title">
          <t-input v-model="formData.title" :style="{ width: '400px' }" placeholder="" />
        </t-form-item>

        <t-form-item label="摘要" name="digest">
          <t-input v-model="formData.digest" :style="{ width: '400px' }" placeholder="" />
        </t-form-item>

        <t-form-item label="内容" name="content">
          <div style="width: 100%">
            <ckeditor v-if="editor && config" v-model="formData.content" :editor="editor" :config="config" />
          </div>
        </t-form-item>
        <t-form-item label="发布时间" name="publishedTime">
          <t-date-picker v-model="formData.publishedTime" :style="{ width: '200px' }" enable-time-picker />
        </t-form-item>
        <t-form-item label="作者" name="author">
          <t-input-adornment prepend="&nbsp;&nbsp;by&nbsp;&nbsp;">
            <t-input v-model="formData.author" :style="{ width: '150px' }" placeholder="" />
          </t-input-adornment>
        </t-form-item>
        <t-form-item label="浏览次数" name="viewCount">
          <t-input-number v-model="formData.viewCount" :step="100" />
        </t-form-item>
        <t-form-item label="是否置顶" name="pinned">
          <t-radio-group v-model="formData.pinned">
            <t-radio :value="true">是</t-radio>
            <t-radio :value="false">否</t-radio>
          </t-radio-group>
        </t-form-item>
        <t-form-item label="是否发布" name="published">
          <t-radio-group v-model="formData.published">
            <t-radio :value="true">是</t-radio>
            <t-radio :value="false">否</t-radio>
          </t-radio-group>
        </t-form-item>
      </t-form>
    </template>
  </t-dialog>
</template>

<script setup lang="ts">
import { Ckeditor } from '@ckeditor/ckeditor5-vue';
import { FormRule, MessagePlugin, SubmitContext, UploadFile, UploadProps } from 'tdesign-vue-next';
import { defineExpose, inject, reactive, ref } from 'vue';

import * as newsMgt from '@/api/oledu/newsMgt';
import * as baseMgt from '@/api/system/baseMgt';

const config = inject('ckeditorConfig');
const editor = inject('ckeditorEditor');
const dialogMode = ref('modal');
const imgFile = ref([]);
const uploadDisabled = ref(false);
const imgPre = import.meta.env.VITE_SYS_IMG_PRE;
export interface FormData {
  id?: number;
  code?: string;
  title?: string;
  image?: string;
  publishedTime?: string;
  digest?: string;
  content?: string;
  author?: string;
  viewCount?: number;
  pinned?: boolean;
  published?: boolean;
}
const emit = defineEmits(['fetch-data']);

const state = reactive({
  headerTitle: '新增',
});

const formVisible = ref(false);

const formatDate = (date: Date) => {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');
  const seconds = String(date.getSeconds()).padStart(2, '0');
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
};

const initFormData = ref<FormData>({
  published: true,
  publishedTime: formatDate(new Date()),
  viewCount: 0,
  pinned: false,
});
const formData = ref({ ...initFormData.value });
const formRef = ref(null);

const fullScreen = () => {
  if (dialogMode.value === 'full-screen') {
    dialogMode.value = 'modal';
  } else {
    dialogMode.value = 'full-screen';
  }
};

const init = async (id: any) => {
  const data = await newsMgt.get(id);
  formData.value = data;
  if (formData.value.image) {
    imgFile.value = [
      {
        url: `${imgPre}${formData.value.image}`,
      },
    ];
  }
  if (formData.value.content == null) {
    formData.value.content = '';
  }
  formVisible.value = true;
};

const show = (id: any) => {
  if (id) {
    state.headerTitle = '修改';
    init(id);
  } else {
    formVisible.value = true;
    state.headerTitle = '新增';
  }
};

const close = () => {
  formVisible.value = false;
  formData.value = { ...initFormData.value };
  formRef.value.reset();
  imgFile.value = [];
  dialogMode.value = 'modal';
};

const onSubmit = async (ctx: SubmitContext) => {
  if (ctx.validateResult === true) {
    if (formData.value.id > 0) {
      await newsMgt.update(formData.value);
    } else {
      await newsMgt.save(formData.value);
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

const requestMethod: UploadProps['requestMethod'] = (file: UploadFile) => {
  uploadDisabled.value = true;
  return new Promise((resolve) => {
    const res = baseMgt.uploadImg(file.raw);
    res
      .then((v) => {
        formData.value.image = v;
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

const handleRemove = () => {
  formData.value.image = '';
};

const handleSuccess: UploadProps['onSuccess'] = ({ file }) => {
  console.log(file);
};

const rules: Record<string, FormRule[]> = {
  title: [{ required: true, message: '请输入标题', type: 'error' }],
};
</script>
