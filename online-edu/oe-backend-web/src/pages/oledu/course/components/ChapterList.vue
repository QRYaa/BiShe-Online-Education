<template>
  <t-card>
    <t-row>
      <t-button @click="showForm()"> 新建 </t-button>
    </t-row>

    <t-collapse v-if="data && data.chapterDtoList" :default-expand-all="true">
      <t-collapse-panel v-if="data.chapterDtoList.length == 0" header="暂无数据，请点击新建按钮" disabled />
      <t-collapse-panel v-for="item in data.chapterDtoList" :key="item.id" :header="item.name">
        <template #headerRightContent>
          <t-space>
            <t-link theme="primary" @click="showLessonForm(item.id, null)">新增课节</t-link>
            <t-link theme="primary" @click="showForm(item.id)">修改</t-link>
            <t-popconfirm
              theme="danger"
              content="确认删除该数据吗"
              :confirm-btn="{
                content: '删除',
                theme: 'danger',
              }"
              @confirm="onConfirmDelete(item.id)"
            >
              <t-link theme="danger">删除</t-link>
            </t-popconfirm>
          </t-space>
        </template>
        <t-list>
          <t-list-item v-for="item2 in item.lessonDtoList" :key="item2.id">
            <span v-if="item2.type === 3" style="display: flex; align-items: center">
              <t-icon name="play-demo" />
              <div>&nbsp;{{ item2.name }}&nbsp;&nbsp;&nbsp;</div>
              <t-button v-if="item2.duration > 0" variant="outline" style="border-color: #ffffff">
                <template #icon> <t-icon name="play-rectangle" /></template>{{ formatDuration(item2.duration) }}
              </t-button>
              <span v-else style="color: var(--td-gray-color-3)">（无视频文件，请上传视频）</span>
              <t-tag v-if="item2.previewAble" theme="primary" variant="light">试看</t-tag>
            </span>
            <span v-else-if="item2.type === 1"><t-icon name="book-open" />&nbsp;{{ item2.name }}</span>
            <span v-else-if="item2.type === 2"><t-icon name="sound" />&nbsp;{{ item2.name }}</span>
            <template #action>
              <t-space>
                <t-link theme="primary" @click="showLessonForm(null, item2.id)"> 修改 </t-link>
                <t-popconfirm
                  theme="danger"
                  content="确认删除该数据吗"
                  :confirm-btn="{
                    content: '删除',
                    theme: 'danger',
                  }"
                  @confirm="onConfirmDeleteLesson(item2.id)"
                >
                  <t-link theme="danger">删除</t-link>
                </t-popconfirm>
              </t-space>
            </template>
          </t-list-item>
        </t-list>
      </t-collapse-panel>
    </t-collapse>

    <chapter-form ref="chapterFormRef" @fetch-data="fetchData" />
    <lesson-form ref="lessonFormRef" @fetch-data="fetchData" />
  </t-card>
</template>
<script setup lang="ts">
import { MessagePlugin } from 'tdesign-vue-next';
import { ref } from 'vue';

import * as chapterMgt from '@/api/oledu/chapterMgt';
import * as courseMgt from '@/api/oledu/courseMgt';
import * as lessonMgt from '@/api/oledu/lessonMgt';

import ChapterForm from './ChapterForm.vue';
import LessonForm from './LessonForm.vue';

const chapterFormRef = ref(null);
const lessonFormRef = ref(null);

const data = ref(null);
const courseId = ref(0);

const fetchData = async () => {
  try {
    data.value = await courseMgt.get(courseId.value);
  } catch (e) {
    console.log(e);
  }
};

const onConfirmDelete = async (id: any) => {
  // 真实业务请发起请求
  await chapterMgt.del(id);
  await fetchData();
  MessagePlugin.success('删除成功');
};

const onConfirmDeleteLesson = async (id: any) => {
  // 真实业务请发起请求
  await lessonMgt.del(id);
  await fetchData();
  MessagePlugin.success('删除成功');
};

const showForm = (id?: any) => {
  chapterFormRef.value.setCourseId(courseId.value);
  chapterFormRef.value.show(id);
};

const showLessonForm = (chapterId?: any, id?: any) => {
  lessonFormRef.value.setChapterId(chapterId);
  lessonFormRef.value.show(id);
};

const formatDuration = (duration: any) => {
  const hours = Math.floor(duration / 3600);
  const remainingSeconds = duration % 3600;
  const minutes = Math.floor(remainingSeconds / 60);
  const seconds = remainingSeconds % 60;

  if (hours > 0) {
    return `${String(hours).padStart(2, '0')}:${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}`;
  }
  return `${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}`;
};

const setCourseId = (id?: any) => {
  courseId.value = id;
};
defineExpose({
  fetchData,
  setCourseId,
});
</script>
