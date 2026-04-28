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
        <t-form-item label="名称" name="name">
          <t-input v-model="formData.name" :style="{ width: '200px' }" placeholder="" />
        </t-form-item>
        <t-form-item label="起始时间" name="startTime">
          <t-date-picker
            v-model="formData.startTime"
            placeholder=""
            format="YYYY-MM-DD HH:mm"
            :enable-time-picker="true"
          />
        </t-form-item>
        <t-form-item label="结束时间" name="endTime">
          <t-date-picker
            v-model="formData.endTime"
            placeholder=""
            format="YYYY-MM-DD HH:mm"
            :enable-time-picker="true"
          />
        </t-form-item>
        <t-form-item label="链接" name="url">
          <t-input v-model="inputUrl" :style="{ width: '400px' }" @change="updateUrl">
            <template #label>
              <t-select v-model="urlPrefix" placeholder="协议" style="max-width: 110px; margin-left: -9px">
                <t-option value="http://" label="http://" />
                <t-option value="https://" label="https://" />
              </t-select>
            </template>
          </t-input>
        </t-form-item>
        <t-form-item label="状态" name="status">
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
import { FormRule, MessagePlugin, SubmitContext, UploadFile, UploadProps } from 'tdesign-vue-next';
import { defineExpose, reactive, ref } from 'vue';

import * as bannerMgt from '@/api/oledu/bannerMgt';
import * as baseMgt from '@/api/system/baseMgt';
import { useDictStore } from '@/store';

const imgPre = import.meta.env.VITE_SYS_IMG_PRE;

export interface FormData {
  id?: number;
  name?: string;
  image?: string;
  startTime?: string;
  endTime?: string;
  url?: string;
  remark?: string;
  enable?: number;
}
const emit = defineEmits(['fetch-data']);

const dictStore = useDictStore();
const statusMap = dictStore.getDictItem('STATUS');
const state = reactive({
  headerTitle: '新增',
});

const formVisible = ref(false);
const initFormData = ref<FormData>({
  enable: 1,
});
const formData = ref({ ...initFormData.value });
const formRef = ref(null);
const inputUrl = ref('');
const urlPrefix = ref('http://');

const imgFile = ref([]);
const uploadDisabled = ref(false);

const updateUrl = () => {
  formData.value.url = urlPrefix.value + inputUrl.value;
};

const init = async (id: any) => {
  const data = await bannerMgt.get(id);
  formData.value = data;
  if (formData.value.image) {
    imgFile.value = [
      {
        url: `${imgPre}${formData.value.image}`,
      },
    ];
  }
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
  imgFile.value = [];
  formData.value = { ...initFormData.value };
  formRef.value.reset();
  inputUrl.value = '';
};

const onSubmit = async (ctx: SubmitContext) => {
  if (ctx.validateResult === true) {
    if (formData.value.id > 0) {
      await bannerMgt.update(formData.value);
    } else {
      await bannerMgt.save(formData.value);
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

const handleRemove = () => {
  formData.value.image = '';
};
const handleSuccess: UploadProps['onSuccess'] = ({ file }) => {
  // MessagePlugin.success(`文件 ${file.name} 上传成功`);
  console.log(file);
};
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

defineExpose({
  show,
});

const rules: Record<string, FormRule[]> = {
  name: [
    {
      required: true,
      message: '请输入名称',
      type: 'error',
    },
  ],
};
</script>
