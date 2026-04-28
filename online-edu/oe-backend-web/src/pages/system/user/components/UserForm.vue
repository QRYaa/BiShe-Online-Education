<template>
  <t-dialog
    v-model:visible="formVisible"
    :header="state.headerTitle"
    :width="680"
    confirm-btn="保存"
    destroy-on-close
    placement="center"
    :on-close="close"
    :on-confirm="onConfirm"
    :close-on-overlay-click="false"
  >
    <template #body>
      <!-- 表单内容 -->
      <t-form ref="formRef" :data="formData" :rules="rules" :label-width="100" @submit="onSubmit" @reset="onReset">
        <t-divider align="left">基本信息</t-divider>
        <t-row>
          <t-col :span="8">
            <t-form-item label="用户名" name="username">
              <t-input
                v-if="!formData.id"
                v-model="formData.username"
                :style="{ width: '200px' }"
                placeholder="请输入用户名"
              />
              <div v-if="formData.id">{{ formData.username }}</div>
            </t-form-item>
            <t-form-item v-if="!formData.id" label="密码" name="password">
              <t-input
                v-model="formData.password"
                type="password"
                :style="{ width: '200px' }"
                placeholder="请输入密码"
              />
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

        <t-form-item label="工号" name="jobNo">
          <t-input v-model="formData.jobNo" :style="{ width: '480px' }" />
        </t-form-item>
        <t-form-item label="真实姓名" name="realName">
          <t-input v-model="formData.realName" :style="{ width: '480px' }" />
        </t-form-item>
        <t-form-item label="性别" name="gender">
          <t-select v-model="formData.gender" :style="{ width: '480px' }">
            <t-option v-for="item in genderMap" :key="item.itemKey" :value="+item.itemKey" :label="item.itemValue" />
          </t-select>
        </t-form-item>
        <t-form-item label="主岗部门" name="departmentId">
          <t-tree-select
            v-model="formData.departmentId"
            :tree-props="{ keys: { value: 'id', label: 'name' } }"
            :data="state.departmentTree"
            clearable
            placeholder="请选择部门"
          />
        </t-form-item>
        <t-form-item label="兼岗部门" name="departmentIds">
          <t-tree-select
            v-model="formData.departmentIdList"
            :tree-props="{ keys: { value: 'id', label: 'name' }, checkStrictly: true }"
            :data="state.departmentTree"
            clearable
            multiple
            placeholder="可多选"
          />
        </t-form-item>
        <t-form-item v-if="!formData.id" label="应用" name="appIdList">
          <t-select v-model="formData.appIdList" clearable multiple>
            <t-option v-for="item in state.appList" :key="item.id" :value="item.id" :label="item.name" />
          </t-select>
        </t-form-item>
        <t-form-item v-if="formData.id" label="应用" name="appIdList">
          <t-select v-model="formData.appIdList" multiple readonly borderless>
            <t-option v-for="item in state.appList" :key="item.id" :value="item.id" :label="item.name" />
          </t-select>
        </t-form-item>
        <t-form-item label="联系电话" name="mobilePhone">
          <t-input v-model="formData.mobilePhone" :style="{ width: '480px' }" />
        </t-form-item>
        <t-form-item label="邮箱" name="email">
          <t-input v-model="formData.email" :style="{ width: '480px' }" />
        </t-form-item>
      </t-form>
    </template>
  </t-dialog>
</template>

<script setup lang="ts">
import { FormRule, MessagePlugin, SubmitContext, UploadFile, UploadProps } from 'tdesign-vue-next';
import { defineExpose, reactive, ref } from 'vue';

import * as applicationMgt from '@/api/system/applicationMgt';
import * as baseMgt from '@/api/system/baseMgt';
import * as departmentMgt from '@/api/system/departmentMgt';
import * as roleMgt from '@/api/system/roleMgt';
import * as userMgt from '@/api/system/userMgt';
// import { imagePrefix } from '@/config/global';
import { useDictStore } from '@/store';

export interface FormData {
  id?: number;
  avatar?: string;
  realName?: string;
  username?: string;
  password?: string;
  jobNo?: string;
  gender?: Number;
  mobilePhone?: string;
  email?: string;
  enable?: Number;
  uploadAvatar?: boolean;
  departmentId?: Number;
  departmentIdList?: Number[];
  appIdList?: Number[];
}
const dictStore = useDictStore();
const statusMap = dictStore.getDictItem('STATUS');
const genderMap = dictStore.getDictItem('GENDER');
const imgFile = ref([]);
const uploadDisabled = ref(false);
const imgPre = import.meta.env.VITE_SYS_IMG_PRE;

const emit = defineEmits(['fetch-data']);

const state = reactive({
  headerTitle: '新增',
  roleList: [],
  departmentTree: [],
  appList: [],
});

const formVisible = ref(false);
const formRef = ref(null);
const initFormData = ref<FormData>({
  enable: 1,
});
const formData = ref({ ...initFormData.value });
const originalAvatar = ref(formData.value.avatar);

const fetchRoleListData = async () => {
  const records = await roleMgt.listAll();
  state.roleList = records;
};

const fetchDepartmentData = async () => {
  const records = await departmentMgt.tree(null);
  state.departmentTree = records;
};

const fetchAppData = async () => {
  const records = await applicationMgt.listAll();
  state.appList = records;
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

const init = async (id: any) => {
  const data = await userMgt.get(id);
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
  fetchRoleListData();
  fetchDepartmentData();
  fetchAppData();
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
};
const onSubmit = async (ctx: SubmitContext) => {
  if (ctx.validateResult === true) {
    if (formData.value.id > 0) {
      if (
        originalAvatar.value !== formData.value.avatar &&
        formData.value.avatar !== '' &&
        formData.value.avatar !== null
      ) {
        formData.value.uploadAvatar = true;
      }
      await userMgt.update(formData.value);
    } else {
      await userMgt.save(formData.value);
    }
    emit('fetch-data');
    close();
    MessagePlugin.success('保存成功');
  } else {
    console.log('Errors: ', ctx.validateResult);
  }
};

const onReset = async () => {
  console.log('form reset');
};

const onConfirm = () => {
  formRef.value.submit();
};

defineExpose({
  show,
});

const rules: Record<string, FormRule[]> = {
  username: [{ required: true, message: '请输入用户名', type: 'error' }],
  realName: [{ required: true, message: '请输入姓名', type: 'error' }],
  mobilePhone: [{ required: true, message: '请输入手机号', type: 'error' }],
  password: [{ required: true, message: '请输入密码', type: 'error' }],
  departmentId: [{ required: true, message: '请选择部门', type: 'error' }],
  enable: [{ required: true, message: '请设置用户状态', type: 'error' }],
};
</script>
