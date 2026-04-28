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
        <t-divider align="left">基本信息</t-divider>
        <t-row style="margin-bottom: 15px">
          <t-col :span="8">
            <t-form-item label="编号" name="code">
              <t-input v-model="formData.code" :style="{ width: '200px' }" placeholder="" />
            </t-form-item>
            <t-form-item label="姓名" name="name">
              <t-input v-model="formData.name" :style="{ width: '200px' }" placeholder="" />
            </t-form-item>
            <t-form-item label="类型" name="type">
              <t-tag v-if="formData.type == 1" theme="success">{{
                dictStore.getDictItemValue('MEMBER_TYPE', `${formData.type}`)
              }}</t-tag>
              <t-tag v-else-if="formData.type == 2" theme="default">{{
                dictStore.getDictItemValue('MEMBER_TYPE', `${formData.type}`)
              }}</t-tag>
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
        <t-row>
          <t-col :span="7">
            <t-form-item label="状态" name="enable">
              <t-radio-group v-model="formData.enable">
                <t-radio v-for="item in statusMap" :key="item.itemKey" :value="+item.itemKey">{{
                  item.itemValue
                }}</t-radio>
              </t-radio-group>
            </t-form-item>
          </t-col>
          <t-col :span="5">
            <t-form-item label="性别" name="gender">
              <t-radio-group v-model="formData.gender">
                <t-radio :value="1">男</t-radio>
                <t-radio :value="2">女</t-radio>
              </t-radio-group>
            </t-form-item>
          </t-col>
        </t-row>
        <t-divider align="left">其他信息</t-divider>
        <t-form-item label="电话" name="tel">
          <t-input-adornment prepend="&nbsp;&nbsp;+86&nbsp;&nbsp;">
            <t-input v-model="formData.tel" :style="{ width: '450px' }" placeholder="" />
          </t-input-adornment>
        </t-form-item>
        <t-form-item label="地区" name="areaId">
          <t-cascader v-model="formData.areaId" :options="areaTree" clearable />
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

import * as memberMgt from '@/api/oledu/memberMgt';
import * as baseMgt from '@/api/system/baseMgt';
import { useDictStore } from '@/store';

export interface FormData {
  id?: number;
  name?: string;
  code?: string;
  avatar?: string;
  type?: number;
  tel?: string;
  gender?: number;
  areaId?: number;
  enable?: number;
  remark?: string;
}
const emit = defineEmits(['fetch-data']);

const state = reactive({
  headerTitle: '新增',
});
const dictStore = useDictStore();
const genderMap = dictStore.getDictItem('GENDER');
const statusMap = dictStore.getDictItem('STATUS');
const memberTypeMap = dictStore.getDictItem('MEMBER_TYPE');
const formVisible = ref(false);
const initFormData = ref<FormData>({
  enable: 1,
});
const formData = ref({ ...initFormData.value });
const formRef = ref(null);

const imgFile = ref([]);
const imgPre = import.meta.env.VITE_SYS_IMG_PRE;
const uploadDisabled = ref(false);
const areaTree = ref([]);
const init = async (id: any) => {
  const data = await memberMgt.get(id);
  formData.value = data;
  if (formData.value.avatar) {
    imgFile.value = [
      {
        url: `${imgPre}${formData.value.avatar}`,
      },
    ];
  }
};

const show = async (id: any) => {
  formVisible.value = true;
  const data = await baseMgt.areaTree();
  areaTree.value = data.map((item) => ({
    label: item.name,
    value: item.id,
    children: item.children.map((item2) => ({
      label: item2.name,
      value: item2.id,
    })),
  }));
  if (id) {
    state.headerTitle = '修改';
    init(id);
  } else {
    formData.value.type = 2;
    state.headerTitle = '新增';
  }
};

const close = () => {
  formVisible.value = false;
  formData.value = { ...initFormData.value };
  formRef.value.reset();
  imgFile.value = [];
};

const onSubmit = async (ctx: SubmitContext) => {
  if (ctx.validateResult === true) {
    if (formData.value.id > 0) {
      await memberMgt.update(formData.value);
    } else {
      await memberMgt.save(formData.value);
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
defineExpose({
  show,
});

const rules: Record<string, FormRule[]> = {
  name: [{ required: true, message: '请输入姓名', type: 'error' }],
  tel: [{ required: true, message: '请输入电话', type: 'error' }],
  enable: [{ required: true, message: '状态', type: 'error' }],
};
</script>
