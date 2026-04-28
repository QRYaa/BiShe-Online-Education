<template>
  <t-drawer v-model:visible="visible" :header="state.title" :close-btn="true" size="600px" :footer="false">
    <div class="t-drawer-demo-div">
      <t-button v-permissions="['DICT_MGT']" @click="showDictItemFormForCreate(state.dictId)"> 新增 </t-button>
      <dict-item-form ref="dictItemFormRef" @fetch-data="fetchData" />
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
          <t-link v-permissions="['DICT_MGT']" theme="primary" @click="showDictItemFormForEdit(slotProps.row.id)"
            >修改</t-link
          >
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
  </t-drawer>
</template>

<script setup>
import { MessagePlugin } from 'tdesign-vue-next';
import { defineExpose, reactive, ref } from 'vue';

import * as dictMgt from '@/api/system/dictMgt';

import DictItemForm from './DictItemForm.vue';

const state = reactive({
  dictId: 0,
  title: '',
});
const visible = ref(false);
const data = ref([]);
const dataLoading = ref(false);
const dictItemFormRef = ref(null);

const fetchData = async () => {
  dataLoading.value = true;
  try {
    const list = await dictMgt.itemList(state.dictId);
    data.value = list;
  } catch (e) {
    console.log(e);
  } finally {
    dataLoading.value = false;
  }
};

const showDictItemFormForCreate = (dictId) => {
  dictItemFormRef.value.showForCreate(dictId);
};

const showDictItemFormForEdit = (id) => {
  dictItemFormRef.value.showForEdit(id);
};

const show = (row) => {
  data.value = [];
  visible.value = true;
  state.dictId = row.id;
  state.title = `字典设置 - ${row.name}`;
  fetchData();
};
defineExpose({
  show,
});

const onConfirmDelete = async (id) => {
  // 真实业务请发起请求
  await dictMgt.itemDel(id);
  await fetchData();
  MessagePlugin.success('删除成功');
};

const COLUMNS = [
  {
    title: '名称',
    ellipsis: false,
    colKey: 'itemValue',
  },
  {
    title: '值',
    ellipsis: false,
    colKey: 'itemKey',
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
    colKey: 'op',
    title: '操作',
  },
];
</script>
