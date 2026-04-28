<template>
  <t-card>
    <t-form layout="inline" :data="state.queryForm" :label-width="50" colon @submit="onSubmit">
      <t-form-item label="名称" name="name">
        <t-input v-model="state.queryForm.name" placeholder="请输入字典名称" clearable />
      </t-form-item>

      <t-form-item label="编码" name="code">
        <t-input v-model="state.queryForm.code" placeholder="请输入字典编码" clearable />
      </t-form-item>

      <t-button theme="default" type="submit"> 查询 </t-button>
    </t-form>
    <br />
    <t-row>
      <t-button v-permissions="['DICT_MGT']" @click="showDictForm()"> 新建 </t-button>
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
        <t-button v-permissions="['DICT_MGT']" theme="danger" :disabled="!selectedRowKeys.length">
          {{ state.batchDeleteTitle }}
        </t-button>
      </t-popconfirm>
    </t-row>

    <t-table
      :bordered="true"
      :data="data"
      row-key="id"
      :columns="COLUMNS"
      vertical-align="top"
      :hover="true"
      table-layout="auto"
      :pagination="pagination"
      :loading="dataLoading"
      style="margin-top: 15px"
      @change="rehandleChange"
      @select-change="rehandleSelectChange"
    >
      <template #op="slotProps">
        <t-space>
          <t-link theme="primary" @click="showDictItemList(slotProps.row)">字典值</t-link>
          <t-link v-permissions="['DICT_MGT']" theme="primary" @click="showDictForm(slotProps.row.id)">修改</t-link>
          <t-popconfirm
            theme="danger"
            content="确认删除该数据吗"
            :confirm-btn="{
              content: '删除',
              theme: 'danger',
            }"
            @confirm="onConfirmDelete(slotProps.row.id)"
          >
            <t-link v-permissions="['DICT_MGT']" theme="danger">删除</t-link>
          </t-popconfirm>
        </t-space>
      </template>
    </t-table>

    <dict-form ref="dictFormRef" @fetch-data="fetchData" />
    <dict-item-list ref="dictItemListRef" />
  </t-card>
</template>
<script setup lang="ts">
import { MessagePlugin } from 'tdesign-vue-next';
import { onMounted, reactive, ref } from 'vue';

import * as dictMgt from '@/api/system/dictMgt';

import DictForm from './components/DictForm.vue';
import DictItemList from './components/DictItemList.vue';

const state = reactive({
  batchDeleteTitle: '批量删除',
  queryForm: {
    name: '',
    code: '',
    pageNum: 1,
    pageSize: 20,
  },
});

const dictFormRef = ref(null);
const dictItemListRef = ref(null);

const COLUMNS = [
  {
    fixed: 'left',
    colKey: 'row-select',
    type: 'multiple',
    width: 64,
  },
  {
    title: '字典名称',
    ellipsis: false,
    colKey: 'name',
  },
  {
    title: '编码',
    ellipsis: false,
    colKey: 'code',
  },
  {
    title: '描述',
    ellipsis: true,
    colKey: 'description',
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

const data = ref([]);

const selectedRowKeys = ref([]);

const dataLoading = ref(false);
const fetchData = async () => {
  dataLoading.value = true;
  try {
    const { content, totalItems } = await dictMgt.dictPage(state.queryForm);
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
  await dictMgt.dictDel(id);
  await fetchData();
  MessagePlugin.success('删除成功');
};

const showDictForm = (id?: any) => {
  dictFormRef.value.show(id);
};

const showDictItemList = (row: any) => {
  dictItemListRef.value.show(row);
};

onMounted(() => {
  fetchData();
});

const handleBatchDelete = async () => {
  // const ids = selectedRowKeys.value.map((item) => item).join();
  await dictMgt.dictBatchDel(selectedRowKeys.value);
  await fetchData();
  MessagePlugin.success('删除成功');
  state.batchDeleteTitle = '批量删除';
  selectedRowKeys.value = [];
};
const onSubmit = () => {
  state.queryForm.pageNum = 1;
  fetchData();
};
const rehandleChange = (changeParams: any, triggerAndData: any) => {
  console.log('统一Change', changeParams, triggerAndData);
  state.queryForm.pageNum = changeParams.pagination.current;
  state.queryForm.pageSize = changeParams.pagination.pageSize;
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
