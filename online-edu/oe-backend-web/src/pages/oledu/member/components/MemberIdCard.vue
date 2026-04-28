<template>
  <div>
    <div v-if="formData && formData.id">
      <t-descriptions title="" layout="vertical" bordered>
        <t-descriptions-item label="证件类型">
          {{ dictStore.getDictItemValue('ID_CARD_TYPE', `${formData.type}`) }}
        </t-descriptions-item>
        <t-descriptions-item label="证件号码">{{ formData.number }}</t-descriptions-item>
        <t-descriptions-item label="姓名">{{ formData.realName }}</t-descriptions-item>
        <t-descriptions-item label="身份证正面">
          <img
            v-if="formData.frontImageUrl"
            :src="priImgPrefix + formData.frontImageUrl + '&token=' + tokenInfo"
            style="max-width: 200px; height: auto"
          />
          <img v-else style="max-width: 200px; height: auto" />
        </t-descriptions-item>
        <t-descriptions-item label="身份证背面">
          <img
            v-if="formData.backImageUrl"
            :src="priImgPrefix + formData.backImageUrl + '&token=' + tokenInfo"
            style="max-width: 200px; height: auto"
          />
          <img v-else style="max-width: 200px; height: auto" />
        </t-descriptions-item>
        <!-- <t-descriptions-item label="性别">
          {{ dictStore.getDictItemValue('GENDER', `${formData.gender}`) }}
        </t-descriptions-item>
        <t-descriptions-item label="出生日期">{{ formData.birthday }}</t-descriptions-item>
        <t-descriptions-item label="住址">{{ formData.address }}</t-descriptions-item>
        <t-descriptions-item label="发证日期">{{ formData.issueDate }}</t-descriptions-item> -->
        <t-descriptions-item label="有效期至">{{ formData.expireDate }}</t-descriptions-item>
      </t-descriptions>
      <div style="margin-top: 30px; display: flex; justify-content: center; align-items: center">
        <t-popconfirm
          theme="danger"
          content="确认解绑吗"
          :confirm-btn="{
            content: '解绑',
            theme: 'danger',
          }"
          @confirm="unbind()"
        >
          <t-button theme="danger">解绑身份证</t-button>
        </t-popconfirm>
      </div>
    </div>
    <div v-else style="margin-top: 150px">
      <t-empty />
    </div>
  </div>
</template>

<script setup lang="ts">
import { defineExpose, ref } from 'vue';

import * as memberIdCardMgt from '@/api/oledu/memberIdCardMgt';
import { useDictStore } from '@/store';
import { getToken } from '@/utils/request/auth';

const priImgPrefix = import.meta.env.VITE_PRI_IMG_PRE;
const tokenInfo = ref(getToken());
const dictStore = useDictStore();

export interface FormData {
  id?: number;
  type?: number;
  number?: string;
  realName?: string;
  gender?: number;
  birthday?: string;
  address?: string;
  issueDate?: string;
  expireDate?: string;
  frontImageUrl?: string;
  backImageUrl?: string;
}

const initFormData = ref<FormData>({});
const formData = ref({ ...initFormData.value });
const tid = ref(0);

const init = async () => {
  const data = await memberIdCardMgt.findByMemberId(tid.value);
  formData.value = data;
};

const show = (id: any) => {
  tid.value = id;
  init();
};
const unbind = async () => {
  await memberIdCardMgt.del(formData.value.id);
  init();
};

defineExpose({
  show,
});
</script>
