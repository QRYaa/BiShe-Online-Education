<template>
  <t-drawer v-model:visible="visible" :header="state.title" :close-btn="true" size="1200px" :footer="false">
    <t-card>
      <t-form layout="inline" :data="queryForm" :label-width="50" colon @submit="onSubmit">
        <t-form-item label="名称" name="name">
          <t-input v-model="queryForm.name" placeholder="请输入名称" clearable />
        </t-form-item>
        <t-form-item label="编号" name="code">
          <t-input v-model="queryForm.code" placeholder="请输入编号" clearable />
        </t-form-item>
        <t-form-item label="类型" name="classType">
          <t-select v-model="queryForm.classTypeId" :style="{ width: '200px' }" clearable>
            <t-option v-for="item in state.typeNameList" :key="item.id" :value="item.id" :label="item.name" />
          </t-select>
        </t-form-item>
        <t-form-item label="状态" name="enable">
          <t-select v-model="queryForm.enable" :style="{ width: '200px' }" clearable>
            <t-option v-for="item in statusMap" :key="item.itemKey" :value="+item.itemKey" :label="item.itemValue" />
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
        <template #status="{ row }">
          <t-tag v-if="row.enable == 1" theme="success">{{
            dictStore.getDictItemValue('STATUS', `${row.enable}`)
          }}</t-tag>
          <t-tag v-else theme="danger">{{ dictStore.getDictItemValue('STATUS', `${row.enable}`) }}</t-tag>
        </template>

        <template #classType="{ row }">
          {{ typeNameMap.get(row.classTypeId) }}
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

      <class-module-form ref="classModuleFormRef" @fetch-data="fetchData" />
    </t-card>
  </t-drawer>
</template>
<script setup lang="ts">
import { MessagePlugin } from 'tdesign-vue-next';
import { onMounted, reactive, ref } from 'vue';

import * as classModuleMgt from '@/api/system/classModuleMgt';
import * as classTypeMgt from '@/api/system/classTypeMgt';
import { useDictStore } from '@/store';

import ClassModuleForm from './ClassModuleForm.vue';

const state = reactive({
  batchDeleteTitle: '批量删除',
  typeNameList: [],
  title: '',
});
const visible = ref(false);
export interface QueryForm {
  // name?: string;
  pageNum?: number;
  pageSize?: number;
  name?: string;
  code?: string;
  classTypeId?: string;
  enable?: number;
}
const queryForm = ref<QueryForm>({
  pageNum: 1,
  pageSize: 20,
});

const classModuleFormRef = ref(null);

const COLUMNS = [
  {
    fixed: 'left',
    colKey: 'row-select',
    type: 'multiple',
    width: 64,
  },
  {
    title: '编码',
    ellipsis: false,
    colKey: 'code',
  },
  {
    title: '名称',
    ellipsis: false,
    colKey: 'name',
  },
  {
    title: '备注',
    ellipsis: false,
    colKey: 'remark',
  },
  {
    title: '状态',
    ellipsis: false,
    colKey: 'enable',
    cell: 'status',
  },
  {
    title: '课程类型',
    ellipsis: false,
    colKey: 'classType',
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

const dictStore = useDictStore();

const statusMap = dictStore.getDictItem('STATUS');
const typeNameMap = new Map();

const fetchData = async () => {
  dataLoading.value = true;
  try {
    const { content, totalItems } = await classModuleMgt.page(queryForm.value);
    data.value = content;
    pagination.value.total = Number(totalItems);
  } catch (e) {
    console.log(e);
  } finally {
    dataLoading.value = false;
  }
};

const show = () => {
  data.value = [];
  visible.value = true;
  state.title = '课程模块';
  fetchData();
};
defineExpose({
  show,
});

const onConfirmDelete = async (id: any) => {
  // 真实业务请发起请求
  await classModuleMgt.del(id);
  await fetchData();
  MessagePlugin.success('删除成功');
};

const showForm = (id?: any) => {
  classModuleFormRef.value.show(id);
};

onMounted(() => {
  fetchData();
  fetchtypeNameListData();
});

const handleBatchDelete = async () => {
  // const ids = selectedRowKeys.value.map((item) => item).join();
  await classModuleMgt.batchDel(selectedRowKeys.value);
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
const fetchtypeNameListData = async () => {
  const records = await classTypeMgt.listAll();
  state.typeNameList = records;
  records.forEach((item) => {
    typeNameMap.set(item.id, item.name);
  });
};
</script>
