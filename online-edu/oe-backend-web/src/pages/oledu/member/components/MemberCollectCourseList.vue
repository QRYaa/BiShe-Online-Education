<template>
  <t-card v-model:visible="visible">
    <t-form layout="inline" :data="queryForm" :label-width="50" colon @submit="onSubmit">
      <t-form-item label="课程" name="courseName">
        <t-input v-model="queryForm.courseName" placeholder="请输入名称" clearable />
      </t-form-item>
      <t-button theme="default" type="submit"> 查询 </t-button>
    </t-form>
    <br />
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
      @change="rehandleChange"
    >
      <template #courseImage="{ row }">
        <t-image
          v-if="row.courseDto.horizontalImage"
          :src="`${imgPre}${row.courseDto.horizontalImage}`"
          style="max-width: 53px; max-height: 30px"
        />
      </template>
      <template #courseName="{ row }">{{ row.courseDto.name }}</template>
    </t-table>
  </t-card>
</template>
<script setup lang="ts">
import { ref } from 'vue';

import * as memberCollectCourseMgt from '@/api/oledu/memberCollectCourseMgt';

const imgPre = import.meta.env.VITE_SYS_IMG_PRE;
const visible = ref(false);
export interface QueryForm {
  courseName?: string;
  memberId?: number;
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
];

const pagination = ref({
  defaultPageSize: 20,
  total: 0,
  defaultCurrent: 1,
});

const data = ref([]);

const dataLoading = ref(false);

const fetchData = async () => {
  dataLoading.value = true;
  try {
    const { content, totalItems } = await memberCollectCourseMgt.page(queryForm.value);
    data.value = content;
    pagination.value.total = Number(totalItems);
  } catch (e) {
    console.log(e);
  } finally {
    dataLoading.value = false;
  }
};

const onSubmit = () => {
  queryForm.value.pageNum = 1;
  fetchData();
};
const rehandleChange = (changeParams: any, triggerAndData: any) => {
  console.log('统一Change', changeParams, triggerAndData);
  queryForm.value.pageNum = changeParams.pagination.current;
  queryForm.value.pageSize = changeParams.pagination.pageSize;
  fetchData();
};

const show = (member?: any) => {
  queryForm.value.memberId = member.id;
  fetchData();
  visible.value = true;
};
defineExpose({
  show,
});
</script>
