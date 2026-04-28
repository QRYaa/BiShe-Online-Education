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
          <t-input v-model="formData.name" :style="{ width: '480px' }" placeholder="" />
        </t-form-item>
        <t-form-item label="图片" name="cover">
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
        <t-form-item label="容量" name="capacity">
          <t-input-number v-model="formData.capacity" :style="{ width: '200px' }" placeholder="" />
        </t-form-item>
        <t-form-item label="备注" name="remark">
          <t-textarea v-model="formData.remark" :style="{ width: '480px' }" placeholder="请输入备注" />
        </t-form-item>
        <t-form-item label="状态" name="enable">
          <t-select v-model="formData.enable" :style="{ width: '200px' }">
            <t-option v-for="item in statusMap" :key="item.itemKey" :value="+item.itemKey" :label="item.itemValue" />
          </t-select>
        </t-form-item>
      </t-form>
    </template>
  </t-dialog>
</template>

<script setup lang="ts">
import { FormRule, MessagePlugin, SubmitContext, UploadFile, UploadProps } from 'tdesign-vue-next';
import { defineExpose, reactive, ref } from 'vue';

import * as baseMgt from '@/api/system/baseMgt';
import * as bsClassRoomMgt from '@/api/system/bsClassRoomMgt';
import { useDictStore } from '@/store';

const dictStore = useDictStore();
const statusMap = dictStore.getDictItem('STATUS');
const imgFile = ref([]);
const uploadDisabled = ref(false);
const imgPre = import.meta.env.VITE_SYS_IMG_PRE;

export interface FormData {
  id?: number;
  cover?: string;
  name?: string;
  capacity?: number;
  remark?: string;
  enable?: number;
}
const emit = defineEmits(['fetch-data']);

const state = reactive({
  headerTitle: '新增',
});

const formVisible = ref(false);
const initFormData = ref<FormData>({
  enable: 1,
});
const formData = ref({ ...initFormData.value });
const formRef = ref(null);

const init = async (id: any) => {
  const data = await bsClassRoomMgt.get(id);
  formData.value = data;
  if (formData.value.cover) {
    imgFile.value = [
      {
        url: `${imgPre}${formData.value.cover}`,
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
  formData.value = { ...initFormData.value };
  formRef.value.reset();
  imgFile.value = [];
};
const requestMethod: UploadProps['requestMethod'] = (file: UploadFile) => {
  uploadDisabled.value = true;
  return new Promise((resolve) => {
    const res = baseMgt.uploadImg(file.raw);
    res
      .then((v) => {
        formData.value.cover = v;
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
  formData.value.cover = '';
};

const onSubmit = async (ctx: SubmitContext) => {
  if (ctx.validateResult === true) {
    if (formData.value.id > 0) {
      await bsClassRoomMgt.update(formData.value);
    } else {
      await bsClassRoomMgt.save(formData.value);
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
  name: [{ required: true, message: '请输入名称', type: 'error' }],
};
</script>
