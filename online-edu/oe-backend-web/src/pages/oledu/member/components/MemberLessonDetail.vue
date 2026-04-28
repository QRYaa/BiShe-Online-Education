<template>
  <t-dialog
    v-model:visible="formVisible"
    :header="headerTitle"
    :width="680"
    destroy-on-close
    :on-close="close"
    :close-on-overlay-click="false"
    :footer="false"
  >
    <template #body>
      <t-descriptions layout="vertical" bordered>
        <t-descriptions-item label="学员名称">{{ memberName }}</t-descriptions-item>
        <t-descriptions-item label="课程名称">{{ courseName }}</t-descriptions-item>
        <t-descriptions-item label="章节名称">{{ chapterName + ' ' + lessonName }}</t-descriptions-item>
        <t-descriptions-item label="节时长">{{ formatDuration(lessonDuration) }}</t-descriptions-item>
        <t-descriptions-item label="已学时长">{{ formatDuration(formData.duration) }}</t-descriptions-item>
        <t-descriptions-item label="完成率">{{ (formData.duration / lessonDuration) * 100 }}%</t-descriptions-item>
        <t-descriptions-item label="时间">{{ formData.lastViewedTime }} </t-descriptions-item>
      </t-descriptions>
    </template>
  </t-dialog>
</template>
<script setup lang="ts">
import { defineExpose, ref } from 'vue';

import { formatDuration } from '@/utils/date';

const headerTitle = ref('学员学习课节详情');
const memberName = ref('');
const courseName = ref('');
const chapterName = ref('');
const lessonName = ref('');
const lessonDuration = ref(0);

export interface FormData {
  progress: number;
  duration: number;
  watchedStatus: number;
  lastViewedTime: string;
}

const formVisible = ref(false);
const initFormData = ref<FormData>({
  progress: 0,
  duration: 0,
  watchedStatus: 0,
  lastViewedTime: '',
});
const formData = ref({ ...initFormData.value });

const show = async (ml: any, mn: string, courseN: string, chapterN: string, ln: string, ld: number) => {
  formVisible.value = true;
  formData.value = ml;
  memberName.value = mn;
  courseName.value = courseN;
  chapterName.value = chapterN;
  lessonName.value = ln;
  lessonDuration.value = ld;
};

const close = () => {
  formVisible.value = false;
};

defineExpose({
  show,
});
</script>
