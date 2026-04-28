<template>
  <t-dialog
    v-model:visible="formVisible"
    :mode="dialogMode"
    :header="state.headerTitle"
    :width="780"
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
        <t-row style="margin-bottom: 25px">
          <t-col :span="6">
            <t-form-item label="横版图" name="horizontalImage">
              <t-upload
                v-model="imgFile2"
                theme="image"
                accept="image/*"
                :disabled="uploadDisabled2"
                :show-image-file-name="false"
                :request-method="requestMethod2"
                @remove="handleRemove2"
                @success="handleSuccess2"
              ></t-upload>
            </t-form-item>
          </t-col>
          <t-col :span="6">
            <t-form-item label="方形图" name="squareImage">
              <t-upload
                v-model="imgFile3"
                theme="image"
                accept="image/*"
                :disabled="uploadDisabled3"
                :show-image-file-name="false"
                :request-method="requestMethod3"
                @remove="handleRemove3"
                @success="handleSuccess3"
              ></t-upload>
            </t-form-item>
          </t-col>
          <!-- <t-col :span="4">
            <t-form-item label="竖版图" name="verticalImage">
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
          </t-col> -->
        </t-row>
        <t-row style="margin-bottom: 25px">
          <t-col :span="6">
            <t-form-item label="课程类型" name="courseTypeId">
              <t-select v-model="formData.courseTypeId" :style="{ width: '250px' }" clearable>
                <t-option v-for="item in state.courseTypeList" :key="item.id" :value="item.id" :label="item.name" />
              </t-select>
            </t-form-item>
          </t-col>
          <t-col :span="6">
            <t-form-item label="讲师" name="teacherId">
              <t-select
                v-model="formData.teacherId"
                placeholder="请选择讲师"
                style="width: 250px; display: inline-block"
                filterable
                clearable
              >
                <t-option v-for="item in state.teacherList" :key="item.id" :value="item.id" :label="item.name">
                  <template #content>
                    <div style="display: flex; align-items: start">
                      <t-avatar shape="circle" size="24px" :hide-on-load-failed="true" :image="imgPre + item.avatar">
                        {{ item.name }}
                      </t-avatar>
                      <div style="width: 180px; padding-left: 10px">{{ item.name }}</div>
                      <div>{{ dictStore.getDictItemValue('GENDER', `${item.gender}`) }}</div>
                    </div>
                  </template>
                </t-option>
              </t-select>
            </t-form-item>
          </t-col>
        </t-row>

        <t-form-item label="编号" name="code">
          <t-input v-model="formData.code" :style="{ width: '250px' }" placeholder="" />
        </t-form-item>
        <t-form-item label="名称" name="name">
          <t-input v-model="formData.name" :style="{ width: '250px' }" placeholder="" />
        </t-form-item>

        <t-form-item label="描述" name="description">
          <t-input v-model="formData.description" :style="{ width: '480px' }" placeholder="" />
        </t-form-item>
        <t-form-item label="内容" name="content">
          <div style="width: 100%">
            <ckeditor v-if="editor && config" v-model="formData.content" :editor="editor" :config="config" />
          </div>
        </t-form-item>
        <t-row style="margin-bottom: 15px">
          <t-col :span="4">
            <t-form-item label="原始价" name="originalPrice">
              <t-input-number v-model="formData.originalPrice" label="￥" auto-width theme="column" />
            </t-form-item>
          </t-col>
          <t-col :span="3">
            <t-form-item label="价格" name="price">
              <t-input-number v-model="formData.price" label="￥" auto-width theme="column" />
            </t-form-item>
          </t-col>
        </t-row>
        <t-form-item label="发布" name="published">
          <t-radio-group v-model="formData.published">
            <t-radio :value="true">是</t-radio>
            <t-radio :value="false">否</t-radio>
          </t-radio-group>
        </t-form-item>
        <t-form-item label="状态" name="status">
          <t-radio-group v-model="formData.status">
            <t-radio v-for="item in statusMap" :key="item.itemKey" :value="+item.itemKey">{{ item.itemValue }}</t-radio>
          </t-radio-group>
        </t-form-item>
        <t-row style="margin-bottom: 15px">
          <t-col :span="6">
            <t-form-item label="基础学习人数" name="learningCount">
              <t-input-number v-model="formData.learningCount" :step="1000" />
            </t-form-item>
          </t-col>
          <t-col :span="6">
            <t-form-item label="实际学习人数" name="actualLearningCount">
              <t-tag theme="default">{{ actualLearningCount }}</t-tag>
            </t-form-item>
          </t-col>
        </t-row>
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

import * as courseMgt from '@/api/oledu/courseMgt';
import * as courseTypeMgt from '@/api/oledu/courseTypeMgt';
import * as teacherMgt from '@/api/oledu/teacherMgt';
import * as baseMgt from '@/api/system/baseMgt';
import { useDictStore } from '@/store';

const config = inject('ckeditorConfig');
const editor = inject('ckeditorEditor');
const dialogMode = ref('modal');
const imgFile = ref([]);
const imgFile2 = ref([]);
const imgFile3 = ref([]);
const uploadDisabled = ref(false);
const uploadDisabled2 = ref(false);
const uploadDisabled3 = ref(false);
const imgPre = import.meta.env.VITE_SYS_IMG_PRE;
const dictStore = useDictStore();
const statusMap = dictStore.getDictItem('COURSE_STATUS');

export interface FormData {
  id?: number;
  courseTypeId?: number;
  teacherId?: number;
  code?: string;
  name?: string;
  verticalImage?: string;
  horizontalImage?: string;
  squareImage?: string;
  description?: string;
  content?: string;
  remark?: string;
  status?: number;
  paid?: boolean;
  originalPrice?: string;
  price?: string;
  pupular?: boolean;
  published?: boolean;
  learningCount?: number;
  vlearningCount?: number;
}
const emit = defineEmits(['fetch-data']);

const state = reactive({
  headerTitle: '新增',
  courseTypeList: [],
  teacherList: [],
});

const formVisible = ref(false);
const initFormData = ref<FormData>({
  published: true,
  paid: true,
});
const formData = ref({ ...initFormData.value });
const formRef = ref(null);
const actualLearningCount = ref(0);

const fullScreen = () => {
  if (dialogMode.value === 'full-screen') {
    dialogMode.value = 'modal';
  } else {
    dialogMode.value = 'full-screen';
  }
};
const init = async (id: any) => {
  const data = await courseMgt.get(id);
  formData.value = data;
  if (formData.value.learningCount != null)
    actualLearningCount.value = formData.value.vlearningCount - formData.value.learningCount;
  else actualLearningCount.value = formData.value.vlearningCount;
  if (formData.value.verticalImage) {
    imgFile.value = [
      {
        url: `${imgPre}${formData.value.verticalImage}`,
      },
    ];
  }
  if (formData.value.horizontalImage) {
    imgFile2.value = [
      {
        url: `${imgPre}${formData.value.horizontalImage}`,
      },
    ];
  }
  if (formData.value.squareImage) {
    imgFile3.value = [
      {
        url: `${imgPre}${formData.value.squareImage}`,
      },
    ];
  }
  if (formData.value.content == null) {
    formData.value.content = '';
  }
};

const fetchCourseTypeListData = async () => {
  const records = await courseTypeMgt.listAll();
  state.courseTypeList = records;
};

const fetchTeachListData = async () => {
  const records = await teacherMgt.listAll();
  state.teacherList = records;
};

const show = (id: any) => {
  formVisible.value = true;
  fetchCourseTypeListData();
  fetchTeachListData();
  if (id) {
    state.headerTitle = '修改';
    init(id);
  } else {
    state.headerTitle = '新增';
  }
};

const close = () => {
  formVisible.value = false;
  imgFile.value = [];
  imgFile2.value = [];
  imgFile3.value = [];
  formData.value = { ...initFormData.value };
  formRef.value.reset();
  dialogMode.value = 'modal';
};

const onSubmit = async (ctx: SubmitContext) => {
  if (ctx.validateResult === true) {
    if (formData.value.id > 0) {
      await courseMgt.update(formData.value);
    } else {
      await courseMgt.save(formData.value);
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
        formData.value.verticalImage = v;
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
const requestMethod2: UploadProps['requestMethod'] = (file: UploadFile) => {
  uploadDisabled2.value = true;
  return new Promise((resolve) => {
    const res = baseMgt.uploadImg(file.raw);
    res
      .then((v) => {
        formData.value.horizontalImage = v;
        resolve({
          status: 'success',
          response: { url: `${imgPre}${v}` },
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
const requestMethod3: UploadProps['requestMethod'] = (file: UploadFile) => {
  uploadDisabled3.value = true;
  return new Promise((resolve) => {
    const res = baseMgt.uploadImg(file.raw);
    res
      .then((v) => {
        formData.value.squareImage = v;
        resolve({
          status: 'success',
          response: { url: `${imgPre}${v}` },
        });
      })
      .catch((e) => {
        console.error(e);
      })
      .finally(() => {
        uploadDisabled3.value = false;
      });
  });
};
const handleSuccess: UploadProps['onSuccess'] = ({ file }) => {
  // MessagePlugin.success(`文件 ${file.name} 上传成功`);
  console.log(file);
};
const handleRemove = () => {
  formData.value.verticalImage = '';
};
const handleSuccess2: UploadProps['onSuccess'] = ({ file }) => {
  // MessagePlugin.success(`文件 ${file.name} 上传成功`);
  console.log(file);
};
const handleRemove2 = () => {
  formData.value.horizontalImage = '';
};
const handleSuccess3: UploadProps['onSuccess'] = ({ file }) => {
  // MessagePlugin.success(`文件 ${file.name} 上传成功`);
  console.log(file);
};
const handleRemove3 = () => {
  formData.value.squareImage = '';
};

const rules: Record<string, FormRule[]> = {
  courseTypeId: [{ required: true, message: '请选择类型', type: 'error' }],
  teacherId: [{ required: true, message: '请选择讲师', type: 'error' }],
  name: [{ required: true, message: '请输入名称', type: 'error' }],
  code: [{ required: true, message: '请输入编号', type: 'error' }],
};
</script>
