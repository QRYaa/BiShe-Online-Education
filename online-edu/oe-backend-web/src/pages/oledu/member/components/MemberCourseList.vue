<template>
  <t-card v-model:visible="visible">
    <t-form layout="inline" :data="queryForm" :label-width="50" colon @submit="onSubmit">
      <t-form-item label="课程" name="courseName">
        <t-input v-model="queryForm.courseName" placeholder="请输入名称" clearable />
      </t-form-item>

      <t-button theme="default" type="submit"> 查询 </t-button>
    </t-form>
    <br />
    <t-row><t-button theme="primary" @click="showCourseList()"> 开通课程 </t-button></t-row>
    <t-table
      :data="data"
      row-key="id"
      :columns="COLUMNS"
      vertical-align="top"
      :hover="true"
      table-layout="fixed"
      :pagination="pagination"
      :loading="dataLoading"
      style="margin-top: 15px"
    >
      <template #courseImage="{ row }">
        <t-image
          v-if="row.courseDto && row.courseDto.horizontalImage"
          :src="`${imgPre}${row.courseDto.horizontalImage}`"
          style="max-width: 30px; max-height: 30px"
        />
        <t-image v-else src="/src/assets/deletedCourse.png" style="max-width: 30px; max-height: 30px"></t-image>
      </template>
      <template #courseName="{ row }">{{ row.courseDto ? row.courseDto.name : '' }}</template>
      <template #completionPercentage="{ row }">
        <t-progress theme="circle" :size="30" :label="false" :percentage="row.completionPercentage" />
      </template>
      <template #op="slotProps">
        <t-space>
          <t-link theme="primary" @click="showDetail(slotProps.row)">详情</t-link>
        </t-space>
      </template>
    </t-table>
    <member-lesson-list ref="memberLessonListRef" />
    <course-list ref="courseListRef" @fetch-data="fetchData" />
  </t-card>
</template>
<script setup lang="ts">
import { ref } from 'vue';

import * as memberCourseMgt from '@/api/oledu/memberCourseMgt';

import CourseList from './CourseList.vue';
import MemberLessonList from './MemberLessonList.vue';

const imgPre = import.meta.env.VITE_SYS_IMG_PRE;

export interface QueryForm {
  memberId?: number;
  courseName?: number;
  // name?: string;
  pageNum?: number;
  pageSize?: number;
}
const queryForm = ref<QueryForm>({
  pageNum: 1,
  pageSize: 20,
});

const COLUMNS = [
  {
    title: '课程图',
    ellipsis: false,
    colKey: 'courseImage',
  },
  {
    title: '课程名',
    ellipsis: false,
    colKey: 'courseName',
  },
  {
    title: '上次观看时间',
    ellipsis: false,
    colKey: 'lastViewTime',
    width: 250,
  },

  {
    title: '完成百分比',
    ellipsis: false,
    colKey: 'completionPercentage',
  },
  {
    align: 'left',
    fixed: 'right',
    ellipsis: false,
    width: 100,
    colKey: 'op',
    title: '操作',
  },
];

const pagination = ref({
  defaultPageSize: 20,
  total: 0,
  defaultCurrent: 1,
});
const visible = ref(false);
const memberIdRef = ref(0);
const memberNameRef = ref('');
const memberLessonListRef = ref(null);
const courseListRef = ref(null);
const data = ref([]);

const dataLoading = ref(false);

const show = async (member?: any) => {
  memberIdRef.value = member.id;
  memberNameRef.value = member.name;
  queryForm.value.memberId = member.id;
  await fetchData();
  visible.value = true;
};

defineExpose({
  show,
});

const fetchData = async () => {
  dataLoading.value = true;
  try {
    const { content, totalItems } = await memberCourseMgt.page(queryForm.value);
    data.value = content;
    pagination.value.total = Number(totalItems);
  } catch (e) {
    console.log(e);
  } finally {
    dataLoading.value = false;
  }
};

const showDetail = (row?: any) => {
  memberLessonListRef.value.show(row, memberNameRef.value);
};

const showCourseList = () => {
  courseListRef.value.show(memberIdRef.value, memberNameRef.value);
};

const onSubmit = () => {
  queryForm.value.pageNum = 1;
  fetchData();
};
</script>
