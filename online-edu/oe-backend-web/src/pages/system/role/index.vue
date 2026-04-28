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
      table-layout="auto"
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
      <template #op="slotProps">
        <t-space>
          <t-link theme="primary" @click="showForm(slotProps.row.id)">修改</t-link>
          <t-link theme="warning" @click="showRolePermission(slotProps.row)">分配权限</t-link>
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

    <role-form ref="roleFormRef" @fetch-data="fetchData" />
    <role-permission ref="rolePermissionRef" />
  </t-card>
</template>
<script setup lang="ts">
import { MessagePlugin } from 'tdesign-vue-next';
import { onMounted, reactive, ref } from 'vue';

import * as roleMgt from '@/api/system/roleMgt';
import { useDictStore } from '@/store';

import RoleForm from './components/RoleForm.vue';
import RolePermission from './components/RolePermission.vue';

const dictStore = useDictStore();
const state = reactive({
  batchDeleteTitle: '批量删除',
  queryForm: {
    name: '',
    code: '',
    pageNum: 1,
    pageSize: 20,
  },
});

const roleFormRef = ref(null);
const rolePermissionRef = ref(null);

const COLUMNS = [
  {
    fixed: 'left',
    colKey: 'row-select',
    type: 'multiple',
    width: 64,
  },
  {
    title: '名称',
    ellipsis: false,
    width: 200,
    colKey: 'name',
  },
  {
    title: '编码',
    ellipsis: false,
    width: 200,
    colKey: 'code',
  },
  {
    title: '状态',
    ellipsis: false,
    colKey: 'enable',
    cell: 'status',
  },
  {
    title: '描述',
    ellipsis: true,
    width: 700,
    colKey: 'description',
  },
  {
    align: 'left',
    fixed: 'right',
    ellipsis: false,
    width: 250,
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
    const { content, totalItems } = await roleMgt.page(state.queryForm);
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
  await roleMgt.del(id);
  await fetchData();
  MessagePlugin.success('删除成功');
};

const showForm = (id?: any) => {
  roleFormRef.value.show(id);
};

const showRolePermission = (row: any) => {
  rolePermissionRef.value.show(row);
};

onMounted(() => {
  fetchData();
});

const handleBatchDelete = async () => {
  // const ids = selectedRowKeys.value.map((item) => item).join();
  await roleMgt.batchDel(selectedRowKeys.value);
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
