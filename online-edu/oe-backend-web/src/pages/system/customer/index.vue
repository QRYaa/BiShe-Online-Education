<template>
  <t-card>
    <t-form layout="inline" :data="queryForm" :label-width="50" colon @submit="onSubmit">
      <t-form-item label="名称" name="name">
        <t-input v-model="queryForm.name" placeholder="请输入名称" clearable />
      </t-form-item>
      <t-form-item label="等级" name="gradeId">
        <t-select v-model="queryForm.gradeId" clearable>
          <t-option v-for="item in state.gradeList" :key="item.id" :value="item.id" :label="item.name" />
        </t-select>
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
      <t-button theme="default" variant="outline" @click="showGradeList()">客户等级 </t-button>
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
      <template #name="{ row }">
        <t-avatar shape="round" :hide-on-load-failed="true" :image="imgPre + row.avatar">{{ row.name }}</t-avatar>
        {{ row.name }}
      </template>
      <template #grade="{ row }">
        {{ gradeMap.get(row.gradeId) }}
      </template>
      <template #gender="{ row }">
        {{ dictStore.getDictItemValue('GENDER', `${row.gender}`) }}
      </template>
      <template #registerDate="{ row }">
        <t-tag v-if="row.registerDate">{{ row.registerDate }}</t-tag>
      </template>
      <template #source="{ row }">
        {{ dictStore.getDictItemValue('CUSTOMER_SOURCE', `${row.source}`) }}
      </template>
      <template #status="{ row }">
        <t-tag v-if="row.enable == 1" theme="success">{{
          dictStore.getDictItemValue('STATUS', `${row.enable}`)
        }}</t-tag>
        <t-tag v-else theme="danger">{{ dictStore.getDictItemValue('STATUS', `${row.enable}`) }}</t-tag>
      </template>
      <template #op="slotProps">
        <t-space>
          <t-link theme="primary" @click="showForm(slotProps.row.id)">修改</t-link>
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
        </t-space>
      </template>
    </t-table>

    <customer-form ref="customerFormRef" @fetch-data="fetchData" />
    <customer-grade-list ref="customerGradeListRef" />
  </t-card>
</template>
<script setup lang="ts">
import { MessagePlugin } from 'tdesign-vue-next';
import { onMounted, reactive, ref } from 'vue';

import * as customerGradeMgt from '@/api/system/customerGradeMgt';
import * as customerMgt from '@/api/system/customerMgt';
import { useDictStore } from '@/store';

import CustomerForm from './components/CustomerForm.vue';
import CustomerGradeList from './components/CustomerGradeList.vue';

const imgPre = import.meta.env.VITE_SYS_IMG_PRE;
const dictStore = useDictStore();
const gradeMap = new Map();

const state = reactive({
  batchDeleteTitle: '批量删除',
  gradeList: [],
});

export interface QueryForm {
  name?: string;
  gradeId?: number;
  pageNum?: number;
  pageSize?: number;
}
const queryForm = ref<QueryForm>({
  pageNum: 1,
  pageSize: 20,
});

const customerFormRef = ref(null);

const customerGradeListRef = ref(null);

const COLUMNS = [
  {
    fixed: 'left',
    colKey: 'row-select',
    type: 'multiple',
    width: 64,
  },
  {
    title: '编码',
    fixed: 'left',
    ellipsis: false,
    width: 160,
    colKey: 'code',
  },
  {
    fixed: 'left',
    title: '姓名',
    ellipsis: false,
    width: 150,
    colKey: 'name',
  },
  {
    title: '等级',
    ellipsis: true,
    colKey: 'grade',
  },
  {
    title: '性别',
    ellipsis: true,
    colKey: 'gender',
  },
  {
    title: '生日',
    ellipsis: false,
    width: 150,
    colKey: 'birthday',
  },
  {
    title: '来源',
    ellipsis: false,
    colKey: 'source',
  },
  // {
  //   title: '积分',
  //   ellipsis: false,
  //   colKey: 'point',
  // },
  {
    title: '注册时间',
    ellipsis: false,
    width: 180,
    colKey: 'registerDate',
  },
  {
    title: '状态',
    fixed: 'right',
    ellipsis: false,
    colKey: 'enable',
    cell: 'status',
    width: 80,
  },
  {
    align: 'left',
    fixed: 'right',
    ellipsis: false,
    width: 106,
    colKey: 'op',
    title: '操作',
  },
];

const pagination = ref({
  defaultPageSize: 20,
  total: 0,
  defaultCurrent: 1,
});

const data = ref([]);

const selectedRowKeys = ref([]);

const dataLoading = ref(false);

const showGradeList = () => {
  customerGradeListRef.value.show();
};

const fetchGradeListData = async () => {
  const records = await customerGradeMgt.listAll();
  state.gradeList = records;
  records.forEach((item) => {
    gradeMap.set(item.id, item.name);
  });
};

const fetchData = async () => {
  dataLoading.value = true;
  try {
    const { content, totalItems } = await customerMgt.page(queryForm.value);
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
  await customerMgt.del(id);
  await fetchData();
  MessagePlugin.success('删除成功');
};

const showForm = (id?: any) => {
  customerFormRef.value.show(id);
};

onMounted(() => {
  fetchData();
  fetchGradeListData();
});

const handleBatchDelete = async () => {
  // const ids = selectedRowKeys.value.map((item) => item).join();
  await customerMgt.batchDel(selectedRowKeys.value);
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
</script>
