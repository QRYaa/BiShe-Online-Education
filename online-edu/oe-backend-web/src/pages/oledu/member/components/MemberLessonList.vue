<template>
  <t-dialog v-model:visible="visible" :width="680" :header="state.title" :footer="false">
    <t-card>
      <t-collapse :default-expand-all="true">
        <div v-if="data && data.courseDto">
          <t-collapse-panel v-for="item in data.courseDto.chapterDtoList" :key="item.id" :header="item.name">
            <t-list>
              <t-list-item v-for="item2 in item.lessonDtoList" :key="item2.id">
                <span v-if="item2.type === 3"><t-icon name="play-demo" />&nbsp;{{ item2.name }} </span>
                <span v-else-if="item2.type === 1"><t-icon name="book-open" />&nbsp;{{ item2.name }}</span>
                <span v-else-if="item2.type === 2"><t-icon name="sound" />&nbsp;{{ item2.name }}</span>
                <template #action>
                  <div style="display: flex; align-items: center">
                    <t-progress
                      v-if="
                        state.memberLessonDataMap.get(item2.id) &&
                        state.memberLessonDataMap.get(item2.id).duration &&
                        item2.duration
                      "
                      theme="circle"
                      :size="30"
                      :label="true"
                      :percentage="(state.memberLessonDataMap.get(item2.id).duration / item2.duration) * 100"
                      style="padding-right: 10px"
                    />
                    <div style="padding-right: 10px">
                      <t-tag v-if="!state.memberLessonDataMap.get(item2.id)" theme="default" variant="light"
                        >未观看</t-tag
                      >
                      <t-tag
                        v-else-if="state.memberLessonDataMap.get(item2.id).watchedStatus == 1"
                        theme="default"
                        variant="light"
                        >{{
                          dictStore.getDictItemValue(
                            'WATCHED_STATUS',
                            `${state.memberLessonDataMap.get(item2.id).watchedStatus}`,
                          )
                        }}</t-tag
                      >
                      <t-tag
                        v-else-if="state.memberLessonDataMap.get(item2.id).watchedStatus == 2"
                        theme="primary"
                        variant="light"
                        >{{
                          dictStore.getDictItemValue(
                            'WATCHED_STATUS',
                            `${state.memberLessonDataMap.get(item2.id).watchedStatus}`,
                          )
                        }}</t-tag
                      >
                      <t-tag
                        v-else-if="state.memberLessonDataMap.get(item2.id).watchedStatus == 3"
                        theme="success"
                        variant="light"
                        >{{
                          dictStore.getDictItemValue(
                            'WATCHED_STATUS',
                            `${state.memberLessonDataMap.get(item2.id).watchedStatus}`,
                          )
                        }}</t-tag
                      >
                    </div>
                    <t-link
                      theme="default"
                      @click="
                        handleClickDetail(
                          state.memberLessonDataMap.get(item2.id),
                          mn,
                          courseN,
                          item.name,
                          item2.name,
                          item2.duration,
                        )
                      "
                    >
                      详情
                    </t-link>
                  </div>
                </template>
              </t-list-item>
            </t-list>
          </t-collapse-panel>
        </div>
      </t-collapse>
      <member-lesson-detail ref="memberLessonDetailRef" />
    </t-card>
  </t-dialog>
</template>
<script setup lang="ts">
import { reactive, ref } from 'vue';

import * as memberCourseMgt from '@/api/oledu/memberCourseMgt';
import { useDictStore } from '@/store';

import MemberLessonDetail from './MemberLessonDetail.vue';

const mn = ref('');
const courseN = ref('');
const memberLessonDetailRef = ref(null);

const dictStore = useDictStore();
const state = reactive({
  title: '',
  memberLessonDataMap: new Map(),
});
const visible = ref(false);
const data = ref(null);

const fetchData = async (id: any) => {
  try {
    data.value = await memberCourseMgt.get(id);
    if (Array.isArray(data.value.memberLessonDtos)) {
      data.value.memberLessonDtos.forEach((item) => {
        state.memberLessonDataMap.set(item.lessonId, item);
      });
    }
  } catch (e) {
    console.log(e);
  }
};

const show = (row: any, memberName: string) => {
  visible.value = true;
  state.title = `学员名称【${memberName}】 课程【${row.courseDto.name}】`;
  mn.value = memberName;
  courseN.value = row.courseDto.name;
  fetchData(row.id);
};
const handleClickDetail = (ml: any, mn: string, courseN: string, chapterN: string, ln: string, ld: number) => {
  memberLessonDetailRef.value.show(ml, mn, courseN, chapterN, ln, ld);
};
defineExpose({
  show,
});
</script>
