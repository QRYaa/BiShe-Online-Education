<template>
  <t-drawer v-model:visible="visible" :header="state.title" :close-btn="true" size="600px" :footer="false">
    <div class="t-drawer-demo-div">
      <t-button @click="showForm()"> 新增 </t-button>
      <customer-grade-form ref="customerGradeFormRef" @fetch-data="fetchData" />
    </div>
    <t-table
      :bordered="true"
      :data="data"
      row-key="id"
      :columns="COLUMNS"
      vertical-align="top"
      :hover="true"
      table-layout="auto"
      :loading="dataLoading"
      style="margin-top: 15px"
    >
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
  </t-drawer>
</template>

<script setup>
import { MessagePlugin } from 'tdesign-vue-next';
import { reactive, ref } from 'vue';

import * as customerGradeMgt from '@/api/system/customerGradeMgt';

import CustomerGradeForm from './CustomerGradeForm.vue';

const state = reactive({
  dictId: 0,
  title: '',
});
const visible = ref(false);
const data = ref([]);
const dataLoading = ref(false);
const customerGradeFormRef = ref(null);

const fetchData = async () => {
  dataLoading.value = true;
  try {
    const list = await customerGradeMgt.listAll();
    data.value = list;
  } catch (e) {
    console.log(e);
  } finally {
    dataLoading.value = false;
  }
};

const showForm = (id) => {
  customerGradeFormRef.value.show(id);
};

const show = () => {
  data.value = [];
  visible.value = true;
  state.title = '客户等级';
  fetchData();
};
defineExpose({
  show,
});

const onConfirmDelete = async (id) => {
  // 真实业务请发起请求
  await customerGradeMgt.del(id);
  await fetchData();
  MessagePlugin.success('删除成功');
};

const COLUMNS = [
  {
    title: '等级',
    ellipsis: false,
    colKey: 'level',
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
    align: 'left',
    fixed: 'right',
    ellipsis: false,
    width: 150,
    colKey: 'op',
    title: '操作',
  },
];
</script>
