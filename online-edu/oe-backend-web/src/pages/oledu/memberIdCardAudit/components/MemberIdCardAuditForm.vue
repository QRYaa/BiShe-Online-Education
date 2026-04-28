<template>
  <t-dialog
    v-model:visible="formVisible"
    header="审核资料"
    :width="680"
    destroy-on-close
    :on-close="close"
    :close-on-overlay-click="false"
  >
    <template #body>
      <t-descriptions title="创建人" layout="vertical" bordered>
        <t-descriptions-item label="学员编号">{{ formData.memberCode }}</t-descriptions-item>
        <t-descriptions-item label="学员头像">
          <img :src="imgPrefix + formData.memberAvatar" style="max-width: 150px; height: auto" />
        </t-descriptions-item>
        <t-descriptions-item label="学员昵称">{{ formData.memberName }}</t-descriptions-item>
      </t-descriptions>
      <div style="margin-top: 20px">
        <t-descriptions title="资料信息" layout="vertical" bordered>
          <t-descriptions-item label="创建时间">{{ formData.createTime }}</t-descriptions-item>
          <t-descriptions-item label="证件类型">
            {{ dictStore.getDictItemValue('ID_CARD_TYPE', `${formData.idCardType}`) }}
          </t-descriptions-item>
          <t-descriptions-item label="证件号码">{{ formData.idCardNumber }}</t-descriptions-item>
          <t-descriptions-item label="姓名">{{ formData.idCardRealName }}</t-descriptions-item>
          <!-- <t-descriptions-item label="性别">
          {{ dictStore.getDictItemValue('GENDER', `${formData.idCardGender}`) }}
        </t-descriptions-item>
        <t-descriptions-item label="出生日期">{{ formData.idCardBirthday }}</t-descriptions-item>
        <t-descriptions-item label="住址">{{ formData.idCardAddress }}</t-descriptions-item>
        <t-descriptions-item label="发证日期">{{ formData.idCardIssueDate }}</t-descriptions-item> -->
          <t-descriptions-item label="有效期至">{{ formData.idCardExpireDate }}</t-descriptions-item>
          <t-descriptions-item label="身份证正面">
            <img
              v-if="formData.idCardFrontImageUrl"
              :src="priImgPrefix + formData.idCardFrontImageUrl + '&token=' + tokenInfo"
              style="max-width: 200px; height: auto"
            />
            <img v-else style="max-width: 200px; height: auto" />
          </t-descriptions-item>
          <t-descriptions-item label="身份证背面">
            <img
              v-if="formData.idCardBackImageUrl"
              :src="priImgPrefix + formData.idCardBackImageUrl + '&token=' + tokenInfo"
              style="max-width: 200px; height: auto"
            />
            <img v-else style="max-width: 200px; height: auto" />
          </t-descriptions-item>
        </t-descriptions>
      </div>
      <div style="margin-top: 20px">
        <t-descriptions v-if="formData.status !== 1" title="审核信息" layout="vertical" bordered>
          <t-descriptions-item label="审核状态">
            {{ dictStore.getDictItemValue('AUDIT_STATUS', `${formData.status}`) }}
          </t-descriptions-item>
          <t-descriptions-item label="审核意见">{{ formData.opinion }}</t-descriptions-item>
          <t-descriptions-item label="审核人">{{ formData.auditorName }}</t-descriptions-item>
          <t-descriptions-item label="审核时间">{{ formData.auditTime }}</t-descriptions-item>
        </t-descriptions>
        <div v-else style="margin-left: -15px">
          <t-form :label-width="100">
            <t-form-item label="审核意见" name="">
              <t-textarea v-model="formData.opinion" :style="{ width: '200px' }" placeholder="" />
            </t-form-item>
          </t-form>
        </div>
      </div>
    </template>
    <template #footer>
      <div v-if="formData.status === 1">
        <t-button theme="danger" @click="rejectAudit()">驳回</t-button>
        <t-button theme="success" @click="passAudit()">通过</t-button>
      </div>
      <div v-else></div>
    </template>
  </t-dialog>
</template>

<script setup lang="ts">
import { defineExpose, ref } from 'vue';

import * as memberIdCardAuditMgt from '@/api/oledu/memberIdCardAuditMgt';
import { useDictStore } from '@/store';
import { getToken } from '@/utils/request/auth';

const priImgPrefix = import.meta.env.VITE_PRI_IMG_PRE;
const imgPrefix = import.meta.env.VITE_SYS_IMG_PRE;
const dictStore = useDictStore();
const tokenInfo = ref(getToken());
const emit = defineEmits(['fetch-data']);

export interface FormData {
  id?: number;
  memberId?: number;
  memberName?: string;
  memberAvatar?: string;
  memberCode?: string;
  status?: number;
  opinion?: string;
  auditorId?: number;
  createTime?: string;
  auditTime?: string;
  idCardType?: number;
  idCardNumber?: string;
  idCardRealName?: string;
  idCardGender?: number;
  idCardBirthday?: string;
  idCardAddress?: string;
  idCardIssueDate?: string;
  idCardExpireDate?: string;
  idCardFrontImageUrl?: string;
  idCardBackImageUrl?: string;
  auditorName?: string;
}

const formVisible = ref(false);
const initFormData = ref<FormData>({});
const formData = ref({ ...initFormData.value });
const formRef = ref(null);
const tid = ref(0);

const init = async (id: any) => {
  const data = await memberIdCardAuditMgt.get(id);
  formData.value = data;
  tid.value = id;
};

const show = (id: any) => {
  formVisible.value = true;
  if (id) {
    init(id);
  }
};

const close = () => {
  formVisible.value = false;
  formData.value = { ...initFormData.value };
  formRef.value.reset();
};

const rejectAudit = async () => {
  await memberIdCardAuditMgt.rejectAudit(formData.value);
  formData.value.status = 3;
  init(tid.value);
  emit('fetch-data');
};

const passAudit = async () => {
  await memberIdCardAuditMgt.passAudit(formData.value);
  formData.value.status = 2;
  init(tid.value);
  emit('fetch-data');
};

defineExpose({
  show,
});
</script>
