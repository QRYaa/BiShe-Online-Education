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
        <t-row>
          <t-col :span="6">
            <t-form-item label="编号" name="code">
              <t-input
                v-model="formData.code"
                :style="{ width: '200px' }"
                placeholder="请输入编号"
                tips="编号留空则系统自动生成"
              />
            </t-form-item>
            <t-form-item label="名称" name="name">
              <t-input v-model="formData.name" :style="{ width: '200px' }" placeholder="请输入名称" />
            </t-form-item>
            <t-form-item label="联系电话" name="tel">
              <t-input v-model="formData.tel" :style="{ width: '200px' }" placeholder="请输入联系电话" />
            </t-form-item>

            <t-form-item label="性别" name="gender">
              <t-select v-model="formData.gender" :style="{ width: '200px' }">
                <t-option
                  v-for="item in genderMap"
                  :key="item.itemKey"
                  :value="+item.itemKey"
                  :label="item.itemValue"
                />
              </t-select>
            </t-form-item>
          </t-col>
          <t-col :span="6">
            <t-form-item>
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
            <t-form-item label="客户等级" name="gradeId">
              <t-select v-model="formData.gradeId" :style="{ width: '200px' }">
                <t-option v-for="item in state.gradeList" :key="item.id" :value="item.id" :label="item.name" />
              </t-select>
            </t-form-item>
            <t-form-item label="状态" name="enable">
              <t-select v-model="formData.enable" :style="{ width: '200px' }">
                <t-option
                  v-for="item in statusMap"
                  :key="item.itemKey"
                  :value="+item.itemKey"
                  :label="item.itemValue"
                />
              </t-select>
            </t-form-item>
          </t-col>
        </t-row>
        <t-divider align="left">其他信息</t-divider>
        <t-form-item label="生日" name="birthday">
          <t-date-picker v-model="formData.birthday" :style="{ width: '480px' }" placeholder="" />
        </t-form-item>
        <t-form-item label="来源" name="source">
          <t-select v-model="formData.source" :style="{ width: '200px' }">
            <t-option v-for="item in sourceMap" :key="item.itemKey" :value="+item.itemKey" :label="item.itemValue" />
          </t-select>
        </t-form-item>
        <t-form-item label="注册时间" name="registerDate">
          <t-date-picker
            v-model="formData.registerDate"
            enable-time-picker
            placeholder=""
            tips="注册时间留空则保存为当前时间"
          />
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

import * as baseMgt from '@/api/system/baseMgt';
import * as customerGradeMgt from '@/api/system/customerGradeMgt';
import * as customerMgt from '@/api/system/customerMgt';
import { useDictStore } from '@/store';

const dictStore = useDictStore();
const genderMap = dictStore.getDictItem('GENDER');
const statusMap = dictStore.getDictItem('STATUS');
const sourceMap = dictStore.getDictItem('CUSTOMER_SOURCE');
const imgFile = ref([]);
const uploadDisabled = ref(false);
const imgPre = import.meta.env.VITE_SYS_IMG_PRE;

export interface FormData {
  id?: number;
  name?: string;
  code?: string;
  avatar?: string;
  tel?: string;
  gender?: number;
  birthday?: string;
  gradeId?: number;
  source?: number;
  point?: number;
  registerDate?: string;
  enable?: number;
  remark?: string;
}
const emit = defineEmits(['fetch-data']);

const state = reactive({
  headerTitle: '新增',
  gradeList: [],
});

const formVisible = ref(false);
const initFormData = ref<FormData>({
  enable: 1,
});
const formData = ref({ ...initFormData.value });
const formRef = ref(null);

const fetchGradeListData = async () => {
  const records = await customerGradeMgt.listAll();
  state.gradeList = records;
};

const init = async (id: any) => {
  const data = await customerMgt.get(id);
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
  fetchGradeListData();
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
const onSubmit = async (ctx: SubmitContext) => {
  if (ctx.validateResult === true) {
    if (formData.value.id > 0) {
      await customerMgt.update(formData.value);
    } else {
      await customerMgt.save(formData.value);
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
  tel: [{ required: true, message: '请输入联系电话', type: 'error' }],
};
</script>
