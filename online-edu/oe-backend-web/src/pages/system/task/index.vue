<template>
  <t-card>
    <t-form layout="inline" :data="queryForm" :label-width="80" colon @submit="onSubmit">
      <t-form-item label="名称" name="name">
        <t-input v-model="queryForm.name" placeholder="请输入名称" clearable />
      </t-form-item>
      <t-form-item label="状态" name="status">
        <t-select v-model="queryForm.status" clearable>
          <t-option v-for="item in statusMap" :key="item.itemKey" :value="+item.itemKey" :label="item.itemValue" />
        </t-select>
      </t-form-item>
      <t-form-item label="处理器" name="handlerName">
        <t-input v-model="queryForm.handlerName" placeholder="请输入处理器名称" clearable />
      </t-form-item>
      <t-button theme="default" type="submit"> 查询 </t-button>
    </t-form>
    <br />
    <t-row>
      <t-button @click="showForm()"> 新建 </t-button>
      <t-popconfirm
        theme="danger"
        content="确认删除所选数据吗"
        :confirm-btn="{
          content: '批量删除',
          theme: 'danger',
        }"
        :disabled="!selectedRowKeys.length"
        :style="{ marginLeft: '8px' }"
        @confirm="handleBatchDelete()"
      >
        <t-button theme="danger" :disabled="!selectedRowKeys.length">
          {{ state.batchDeleteTitle }}
        </t-button>
      </t-popconfirm>
      <t-button @click="handleJobLog"> 日志 </t-button>
    </t-row>

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
      @select-change="rehandleSelectChange"
    >
      <template #lastResult="{ row }">
        {{ dictStore.getDictItemValue('TASK_RESULT', `${row.lastResult}`) }}
      </template>
      <template #enable="{ row }">
        {{ dictStore.getDictItemValue('STATUS', `${row.enable}`) }}
      </template>
      <template #status="{ row }">
        {{ dictStore.getDictItemValue('TASK_STATUS', `${row.status}`) }}
      </template>
      <template #op="slotProps">
        <t-space>
          <t-link theme="primary" @click="showForm(slotProps.row.id)">修改</t-link>
          <t-link v-if="slotProps.row.enable === 0" theme="primary" @click="resumeJob(slotProps.row)">开启</t-link>
          <t-link v-if="slotProps.row.enable === 1" theme="primary" @click="pauseJob(slotProps.row)">暂停</t-link>
          <t-popconfirm
            theme="danger"
            content="确认删除该数据吗"
            :confirm-btn="{
              content: '删除',
              theme: 'danger',
            }"
            @confirm="onConfirmDelete(slotProps.row.id)"
          >
            <t-link theme="danger">删除</t-link>
          </t-popconfirm>
          <t-dropdown :options="options" trigger="click" @click="(option) => clickHandler(option, slotProps.row)">
            <t-space>
              <t-button variant="text" class="op-btn">
                更多
                <template #suffix> <t-icon name="chevron-down" size="14" /></template>
              </t-button>
            </t-space>
          </t-dropdown>
        </t-space>
      </template>
    </t-table>

    <task-form ref="taskFormRef" @fetch-data="fetchData" />
  </t-card>
</template>
<script setup lang="ts">
import { MessagePlugin } from 'tdesign-vue-next';
import { onMounted, reactive, ref } from 'vue';
import { useRouter } from 'vue-router';

import * as taskMgt from '@/api/system/taskMgt';
import { useDictStore } from '@/store';

import TaskForm from './components/TaskForm.vue';

const state = reactive({
  batchDeleteTitle: '批量删除',
});

export interface QueryForm {
  name?: string;
  handlerName?: string;
  status?: number;
  pageNum?: number;
  pageSize?: number;
}
const queryForm = ref<QueryForm>({
  pageNum: 1,
  pageSize: 20,
});

const taskFormRef = ref(null);

const COLUMNS = [
  {
    fixed: 'left',
    colKey: 'row-select',
    type: 'multiple',
    width: 64,
  },
  {
    title: '任务编号',
    ellipsis: false,
    colKey: 'id',
  },
  {
    title: '任务名称',
    ellipsis: false,
    width: 150,
    colKey: 'name',
  },
  {
    title: '处理器的名字',
    ellipsis: false,
    width: 250,
    colKey: 'handlerName',
  },
  {
    title: '参数',
    ellipsis: false,
    width: 250,
    colKey: 'param',
  },
  {
    title: '上一次执行结果',
    ellipsis: false,
    colKey: 'lastResult',
  },
  {
    title: '上一次执行时间',
    ellipsis: false,
    colKey: 'lastTime',
    width: 200,
  },
  {
    title: '下一次执行时间',
    ellipsis: false,
    colKey: 'nextValidTime',
    width: 200,
  },
  {
    title: 'cron表达式',
    ellipsis: false,
    colKey: 'cron',
  },
  {
    title: '启用',
    ellipsis: false,
    colKey: 'enable',
  },
  {
    title: '任务状态',
    ellipsis: false,
    colKey: 'status',
  },
  {
    align: 'left',
    fixed: 'right',
    ellipsis: false,
    width: 300,
    colKey: 'op',
    title: '操作',
  },
];

const pagination = ref({
  defaultPageSize: 20,
  total: 0,
  defaultCurrent: 1,
});
const router = useRouter();
const dictStore = useDictStore();
const statusMap = dictStore.getDictItem('TASK_STATUS');
const options = [
  { content: '执行一次', value: 1 },

  // { content: '任务详细', value: 2 },

  { content: '调度日志', value: 3 },
];
const data = ref([]);

const selectedRowKeys = ref([]);

const dataLoading = ref(false);

const fetchData = async () => {
  dataLoading.value = true;
  try {
    const { content, totalItems } = await taskMgt.page(queryForm.value);
    data.value = content;
    pagination.value.total = Number(totalItems);
  } catch (e) {
    console.log(e);
  } finally {
    dataLoading.value = false;
  }
};

const onConfirmDelete = async (id: any) => {
  // 真实业务请发起请求
  await taskMgt.del(id);
  await fetchData();
  MessagePlugin.success('删除成功');
};

const showForm = (id?: any) => {
  taskFormRef.value.show(id);
};

const handleJobLog = (row?: any) => {
  const taskId = row.id || 0;
  router.push(`/setting/taskLog/index/${taskId}`);
};

onMounted(() => {
  fetchData();
});

const handleBatchDelete = async () => {
  // const ids = selectedRowKeys.value.map((item) => item).join();
  await taskMgt.batchDel(selectedRowKeys.value);
  await fetchData();
  MessagePlugin.success('删除成功');
  state.batchDeleteTitle = '批量删除';
  selectedRowKeys.value = [];
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
const rehandleSelectChange = (val: number[]) => {
  selectedRowKeys.value = val;
  if (val.length > 0) {
    state.batchDeleteTitle = `批量删除(${val.length})`;
  } else {
    state.batchDeleteTitle = '批量删除';
  }
};
const resumeJob = async (row: any) => {
  await taskMgt.resumeJob(row);
  await fetchData();
  MessagePlugin.success('开启成功');
};
const pauseJob = async (row: any) => {
  await taskMgt.pauseJob(row);
  await fetchData();
  MessagePlugin.success('暂停成功');
};
const clickHandler = async (option, row) => {
  console.log(option.content, ' 被点击 ');
  if (option.value === 1) {
    await taskMgt.run(row);
  } else if (option.value === 3) {
    router.push(`/setting/taskLog/index/${row.id}`);
  }
};
</script>
<style lang="less" scoped>
.op-btn {
  color: #0052d9;
  padding: 0 0 0 0;
  height: 22px;
  line-height: 22px;
  margin-top: -2px;
}
</style>
