<template>
  <t-card>
    <t-form layout="inline" :data="queryForm" :label-width="100" colon @submit="onSubmit">
      <t-form-item v-if="taskId === '0'" label="任务名称" name="taskId">
        <t-select v-model="queryForm.taskId" clearable>
          <t-option v-for="item in timedTaskList" :key="item.id" :value="item.id" :label="item.name" />
        </t-select>
      </t-form-item>
      <t-form-item label="开始时间" name="startTime">
        <t-date-picker v-model="queryForm.startTime" enable-time-picker clearable />
      </t-form-item>
      <t-form-item label="结束时间" name="stopTime">
        <t-date-picker v-model="queryForm.stopTime" enable-time-picker clearable />
      </t-form-item>
      <t-form-item label="结果" name="result">
        <t-select v-model="queryForm.result" clearable>
          <t-option
            v-for="item in dictStore.getDictItem('TASK_RESULT')"
            :key="item.itemKey"
            :value="+item.itemKey"
            :label="item.itemValue"
          />
        </t-select>
      </t-form-item>
      <t-button theme="default" type="submit"> 查询 </t-button>
    </t-form>
    <br />

    <t-table
      :bordered="true"
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
      <template #handlerName="slotProps">
        {{ handlerNameMap.get(slotProps.row.taskId) }}
      </template>
      <template #executionDuration="slotProps"> {{ slotProps.row.executionDuration }} 毫秒 </template>
      <template #result="slotProps">
        {{ dictStore.getDictItemValue('TASK_RESULT', `${slotProps.row.result}`) }}
      </template>
    </t-table>
  </t-card>
</template>
<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';

import * as taskLogMgt from '@/api/system/taskLogMgt';
import * as taskMgt from '@/api/system/taskMgt';
import { useDictStore } from '@/store';

const taskId = ref(null);
export interface QueryForm {
  startTime?: string;
  stopTime?: string;
  taskId?: number;
  result?: number;
  pageNum?: number;
  pageSize?: number;
}
const queryForm = ref<QueryForm>({
  pageNum: 1,
  pageSize: 20,
});

const COLUMNS = [
  {
    title: '日志编号',
    ellipsis: false,
    colKey: 'id',
  },
  {
    title: '任务编号',
    ellipsis: false,
    colKey: 'taskId',
  },
  {
    title: '处理器名称',
    ellipsis: false,
    colKey: 'handlerName',
  },
  {
    title: '开始时间',
    ellipsis: false,
    colKey: 'startTime',
  },
  {
    title: '结束时间',
    ellipsis: false,
    colKey: 'stopTime',
  },
  {
    title: '耗时',
    ellipsis: false,
    colKey: 'executionDuration',
  },
  {
    title: '信息',
    ellipsis: false,
    colKey: 'message',
  },
  {
    title: '结果',
    ellipsis: false,
    colKey: 'result',
  },
  {
    title: '错误信息',
    ellipsis: false,
    colKey: 'exceptionInfo',
  },
];

const pagination = ref({
  defaultPageSize: 20,
  total: 0,
  defaultCurrent: 1,
});

const router = useRouter();

const dictStore = useDictStore();

const data = ref([]);

const dataLoading = ref(false);

const timedTaskList = ref(null);

const handlerNameMap = new Map();

const fetchData = async () => {
  dataLoading.value = true;
  try {
    const { content, totalItems } = await taskLogMgt.page(queryForm.value);
    await fetchTimedTaskData();
    data.value = content;
    pagination.value.total = Number(totalItems);
  } catch (e) {
    console.log(e);
  } finally {
    dataLoading.value = false;
  }
};

const fetchTimedTaskData = async () => {
  timedTaskList.value = await taskMgt.listAll();
  timedTaskList.value.forEach((item: any) => {
    handlerNameMap.set(item.id, item.handlerName);
  });
};

onMounted(async () => {
  taskId.value = router.currentRoute.value.params && router.currentRoute.value.params.taskId;
  if (taskId.value !== undefined && taskId.value !== '0') {
    queryForm.value.taskId = taskId.value;
  }
  await fetchData();
});

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
</script>
