<template>
  <t-card>
    <t-descriptions layout="vertical" bordered>
      <t-descriptions-item label="学员名称">
        <t-avatar
          v-if="formData.memberAvatar"
          shape="circle"
          size="24px"
          :hide-on-load-failed="true"
          :image="imgPre + formData.memberAvatar"
        />
        <t-avatar v-else shape="circle" size="24px" :hide-on-load-failed="true" image="/src/assets/avatar.jpg" />
        <span style="margin-left: 10px">{{ formData.memberName }}</span>
      </t-descriptions-item>
      <t-descriptions-item label="课程名称">{{ formData.courseName }}</t-descriptions-item>
      <t-descriptions-item label="课节名称">{{ formData.lessonName }}</t-descriptions-item>
      <t-descriptions-item label="内容">{{ formData.content }}</t-descriptions-item>
      <t-descriptions-item label="创建时间">{{ formData.createTime }}</t-descriptions-item>
      <t-descriptions-item label="点赞数">{{ formData.likeNum }}</t-descriptions-item>
      <t-descriptions-item label="评论数">{{ formData.replyNum }}</t-descriptions-item>
      <t-descriptions-item label="状态">{{
        dictStore.getDictItemValue('NOTE_STATUS', `${formData.status}`)
      }}</t-descriptions-item>
      <t-descriptions-item label="启用">{{ formData.enable ? '可用' : '不可用' }}</t-descriptions-item>
    </t-descriptions>
  </t-card>
</template>
<script setup lang="ts">
import { defineExpose, ref } from 'vue';

import * as noteMgt from '@/api/oledu/noteMgt';
import { useDictStore } from '@/store';

export interface FormData {
  id?: number;
  memberId?: number;
  memberName?: string;
  memberAvatar?: string;
  lessonId?: number;
  courseName?: string;
  lessonName?: string;
  content?: string;
  createTime?: string;
  likeNum?: number;
  replyNum?: number;
  status?: number;
  enable?: boolean;
}
const imgPre = import.meta.env.VITE_SYS_IMG_PRE;
const dictStore = useDictStore();
const initFormData = ref<FormData>({});
const formData = ref({ ...initFormData.value });

const init = async (id: any) => {
  const data = await noteMgt.get(id);
  formData.value = data;
};
defineExpose({
  init,
});
</script>
