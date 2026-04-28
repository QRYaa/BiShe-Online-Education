<template>
  <t-dialog
    v-model:visible="formVisible"
    :attach="dialogAttach"
    confirm-btn="保存"
    :width="600"
    destroy-on-close
    :on-close="close"
    :on-confirm="onConfirm"
    :close-on-overlay-click="false"
    :mode="dialogMode"
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
        <t-form-item label="名称" name="name">
          <t-input v-model="formData.name" :style="{ width: '200px' }" placeholder="" />
        </t-form-item>
        <t-form-item label="序号" name="sort">
          <t-input-number v-model="formData.sort" placeholder="序号" />
        </t-form-item>
        <t-form-item label="文件类型" name="type">
          <t-select v-model="formData.type" :style="{ width: '200px' }" @change="handleTypeChange">
            <t-option v-for="item in typeMap" :key="item.itemKey" :value="+item.itemKey" :label="item.itemValue" />
          </t-select>
        </t-form-item>
        <t-form-item v-if="formData.type === 1" label="图文" name="content">
          <div style="width: 100%">
            <ckeditor v-if="editor && config" v-model="formData.content" :editor="editor" :config="config" />
          </div>
        </t-form-item>
        <t-form-item v-if="formData.type === 2" label="音频文件" name="mediaUrl">
          <t-upload
            v-model="audioFile"
            theme="file"
            accept="audio/*"
            :size-limit="{ size: 1000, unit: 'MB' }"
            :disabled="uploadDisabled"
            :request-method="requestMethod"
            @remove="handleRemove"
            @success="handleUploadSuccess"
          >
            <t-button v-if="audioFile.length > 0" variant="outline">
              <template #icon><t-icon name="upload" /></template>
              重新选择
            </t-button>
            <t-button v-else variant="outline">
              <template #icon><t-icon name="upload" /></template>
              选择文件
            </t-button>
          </t-upload>
        </t-form-item>
        <t-form-item v-if="formData.type === 3" label="视频文件" name="mediaUrl">
          <t-upload
            v-model="videoFile"
            theme="file"
            accept="video/*"
            :size-limit="{ size: 1000, unit: 'MB' }"
            :disabled="uploadDisabled2"
            :request-method="requestMethod2"
            @remove="handleRemove2"
            @success="handleUploadSuccess2"
          >
            <t-button v-if="videoFile.length > 0" variant="outline">
              <template #icon><t-icon name="upload" /></template>
              重新选择
            </t-button>
            <t-button v-else variant="outline">
              <template #icon><t-icon name="upload" /></template>
              选择文件
            </t-button>
          </t-upload>
        </t-form-item>
        <t-form-item v-if="formData.mediaUrl && formData.type === 3">
          <video
            ref="videoPlayerRef"
            style="max-width: 200px; max-height: 200px"
            :src="`${adminVideoPre}${formData.mediaUrl}&token=${tokenInfo}`"
            controls
            @loadedmetadata="setDuration"
          ></video>
        </t-form-item>
        <t-form-item v-else-if="formData.mediaUrl && formData.type === 2">
          <audio :src="`${adminAudioPre}${formData.mediaUrl}&token=${tokenInfo}`" controls></audio>
        </t-form-item>
        <t-form-item label="试看" name="previewAble">
          <t-radio-group v-model="formData.previewAble">
            <t-radio :value="true">是</t-radio>
            <t-radio :value="false">否</t-radio>
          </t-radio-group>
        </t-form-item>
        <t-form-item label="状态" name="enable">
          <t-radio-group v-model="formData.enable">
            <t-radio v-for="item in statusMap" :key="item.itemKey" :value="+item.itemKey">{{ item.itemValue }}</t-radio>
          </t-radio-group>
        </t-form-item>
        <t-form-item label="备注" name="remark">
          <t-textarea v-model="formData.remark" :style="{ width: '480px' }" placeholder="请输入备注" />
        </t-form-item>
      </t-form>
    </template>
  </t-dialog>
</template>

<script setup lang="ts">
import { Ckeditor } from '@ckeditor/ckeditor5-vue';
import { FormRule, MessagePlugin, SubmitContext, UploadFile, UploadProps } from 'tdesign-vue-next';
import { defineExpose, inject, reactive, ref } from 'vue';

import * as lessonMgt from '@/api/oledu/lessonMgt';
import * as baseMgt from '@/api/system/baseMgt';
import { useDictStore } from '@/store';
import { getToken } from '@/utils/request/auth';

const config = inject('ckeditorConfig');
const editor = inject('ckeditorEditor');
const dialogAttach = ref('');
const dialogMode = ref('modal');
const tokenInfo = ref(getToken());
const dictStore = useDictStore();
const statusMap = dictStore.getDictItem('STATUS');
const typeMap = dictStore.getDictItem('FILE_TYPE');
const audioFile = ref([]);
const videoFile = ref([]);
const uploadDisabled = ref(false);
const uploadDisabled2 = ref(false);
const adminAudioPre = import.meta.env.VITE_LESSON_AUDIO_PRE;
const adminVideoPre = import.meta.env.VITE_LESSON_VIDEO_PRE;

export interface FormData {
  id?: number;
  chapterId?: number;
  name?: string;
  type?: number;
  content?: string;
  mediaUrl?: string;
  mediaName?: string;
  duration?: number;
  sort?: number;
  previewAble?: boolean;
  remark?: string;
  enable?: number;
}
const emit = defineEmits(['fetch-data']);

const state = reactive({
  headerTitle: '新增',
});

const formVisible = ref(false);
const initFormData = ref<FormData>({
  previewAble: false,
  enable: 1,
  type: 3,
});
const formData = ref({ ...initFormData.value });
const formRef = ref(null);
const videoPlayerRef = ref<HTMLVideoElement | null>(null);

const init = async (id: any) => {
  const data = await lessonMgt.get(id);
  formData.value = data;
  if (formData.value.mediaUrl && formData.value.type === 2) {
    audioFile.value = [
      {
        url: `${adminAudioPre}${formData.value.mediaUrl}&token=${tokenInfo.value}`,
        name: `${formData.value.mediaName}`,
      },
    ];
  }
  if (formData.value.mediaUrl && formData.value.type === 3) {
    videoFile.value = [
      {
        url: `${adminVideoPre}${formData.value.mediaUrl}&token=${tokenInfo.value}`,
        name: `${formData.value.mediaName}`,
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

const setDuration = () => {
  formData.value.duration = Math.round(videoPlayerRef.value.duration);
};

const close = () => {
  formVisible.value = false;
  formData.value = { ...initFormData.value };
  formRef.value.reset();
  audioFile.value = [];
  videoFile.value = [];
  dialogAttach.value = '';
  dialogMode.value = 'modal';
};

const fullScreen = () => {
  if (dialogAttach.value === '#oleduCourseIndex') {
    dialogAttach.value = '';
    dialogMode.value = 'modal';
  } else {
    dialogAttach.value = '#oleduCourseIndex';
    dialogMode.value = 'full-screen';
  }
};

const handleTypeChange = () => {
  audioFile.value = [];
  videoFile.value = [];
  formData.value.mediaUrl = '';
  formData.value.mediaName = '';
  formData.value.duration = 0;
};

const requestMethod: UploadProps['requestMethod'] = (file: UploadFile) => {
  uploadDisabled.value = true;
  return new Promise((resolve) => {
    const res = baseMgt.uploadLessonAudio(file.raw); // 使用通用的上传接口
    res
      .then((v) => {
        formData.value.mediaUrl = v;
        formData.value.mediaName = file.raw.name;
        resolve({
          status: 'success',
          response: { url: `${adminAudioPre}${v}&token=${tokenInfo.value}` },
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

const requestMethod2: UploadProps['requestMethod'] = (file: UploadFile) => {
  uploadDisabled2.value = true;
  return new Promise((resolve) => {
    const res = baseMgt.uploadLessonVideo(file.raw); // 使用通用的上传接口
    res
      .then((v) => {
        formData.value.mediaUrl = v;
        formData.value.mediaName = file.raw.name;
        resolve({
          status: 'success',
          response: { url: `${adminVideoPre}${v}&token=${tokenInfo.value}` },
        });
      })
      .catch((e) => {
        console.error(e);
      })
      .finally(() => {
        uploadDisabled2.value = false;
      });
  });
};

const handleUploadSuccess: UploadProps['onSuccess'] = ({ file }) => {
  MessagePlugin.success(`文件 ${file.name} 上传成功`);
};
const handleUploadSuccess2: UploadProps['onSuccess'] = ({ file }) => {
  MessagePlugin.success(`文件 ${file.name} 上传成功`);
};

const handleRemove = () => {
  formData.value.mediaUrl = '';
  formData.value.duration = 0;
};

const handleRemove2 = () => {
  formData.value.mediaUrl = '';
  formData.value.duration = 0;
};
const onSubmit = async (ctx: SubmitContext) => {
  if (ctx.validateResult === true) {
    if (formData.value.id > 0) {
      await lessonMgt.update(formData.value);
    } else {
      await lessonMgt.save(formData.value);
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

const setChapterId = (id?: any) => {
  formData.value.chapterId = id;
};

defineExpose({
  show,
  setChapterId,
});
const rules: Record<string, FormRule[]> = {
  name: [{ required: true, message: '请输入名称', type: 'error' }],
  enable: [{ required: true, message: '状态', type: 'error' }],
  type: [{ required: true, message: '类型', type: 'error' }],
  sort: [{ required: true, message: '排序', type: 'error' }],
};
</script>
<style scoped>
.t-icon:hover {
  background-color: #eeeeee;
}
</style>
