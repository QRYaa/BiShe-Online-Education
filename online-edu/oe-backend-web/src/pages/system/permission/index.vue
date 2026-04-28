<template>
  <t-card>
    <t-form ref="form" layout="inline" :data="state.queryForm" :label-width="50" colon @submit="onSubmit">
      <t-form-item label="名称" name="name">
        <t-input v-model="state.queryForm.name" placeholder="请输入名称" clearable />
      </t-form-item>

      <t-form-item label="编码" name="code">
        <t-input v-model="state.queryForm.code" placeholder="请输入字典编码" clearable />
      </t-form-item>

      <t-button theme="default" type="submit"> 查询 </t-button>
    </t-form>
    <br />
    <t-row>
      <t-button @click="showForm(null)"> 新建 </t-button>
      <t-button theme="default" @click="onExpandAllToggle"> {{ expandAll ? '收起全部' : '展开全部' }} </t-button>
    </t-row>

    <t-enhanced-table
      ref="treeTable"
      :bordered="true"
      :data="data"
      row-key="id"
      :columns="COLUMNS"
      vertical-align="top"
      :hover="true"
      table-layout="fixed"
      :tree="{ childrenKey: 'children', indent: 50, defaultExpandAll: true }"
      :loading="dataLoading"
      style="margin-top: 15px"
    >
      <template #op="slotProps">
        <t-space>
          <t-link theme="primary" @click="showSubForm(slotProps.row)">新增下级</t-link>
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
    </t-enhanced-table>
    <permission-form ref="formRef" @fetch-data="fetchData" />
  </t-card>
</template>
<script setup lang="ts">
import {
  ButtonProps,
  EnhancedTable as TEnhancedTable,
  EnhancedTableInstanceFunctions,
  EnhancedTableProps,
  MessagePlugin,
} from 'tdesign-vue-next';
import { onMounted, reactive, ref } from 'vue';

import * as permissionMgt from '@/api/system/permissionMgt';

import PermissionForm from './components/PermissionForm.vue';

const state = reactive({
  queryForm: { name: '', code: '' },
});

const formRef = ref(null);
const treeTable = ref<EnhancedTableInstanceFunctions>(null);
const expandAll = ref(false);

const COLUMNS: EnhancedTableProps['columns'] = [
  {
    title: '名称',
    ellipsis: false,
    colKey: 'name',
  },
  {
    title: '编码',
    ellipsis: false,
    colKey: 'code',
  },
  {
    align: 'left',
    title: '访问路径',
    ellipsis: false,
    width: 300,
    colKey: 'paths',
  },
  {
    title: '排序',
    ellipsis: false,
    colKey: 'sort',
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

const data = ref([]);

const dataLoading = ref(false);

const onExpandAllToggle: ButtonProps['onClick'] = () => {
  expandAll.value = !expandAll.value;
  if (expandAll.value) {
    treeTable.value.expandAll();
  } else {
    treeTable.value.foldAll();
  }
};

const fetchData = async () => {
  dataLoading.value = true;
  data.value = [];
  try {
    const records = await permissionMgt.tree(state.queryForm);
    data.value = records;
    expandAll.value = false;
  } catch (e) {
    console.log(e);
  } finally {
    dataLoading.value = false;
  }
};

const onConfirmDelete = async (id: any) => {
  console.log(`Delete ${id}`);
  // 真实业务请发起请求
  await permissionMgt.del(id);
  await fetchData();
  MessagePlugin.success('删除成功');
};

const showForm = (id: any) => {
  formRef.value.show(id);
};

const showSubForm = (row: any) => {
  formRef.value.showForSub(row);
};

onMounted(() => {
  fetchData();
});

const onSubmit = () => {
  fetchData();
};
</script>
