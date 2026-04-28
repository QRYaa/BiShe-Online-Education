<template>
  <t-drawer v-model:visible="visible" :header="state.title" :close-btn="true" size="600px" :footer="false">
    <t-button v-permissions="['USER_MGT']" @click="showAddAppForm(userId, name)"> 新增 </t-button>
    <t-table
      :bordered="true"
      :data="data"
      row-key="id"
      :columns="COLUMNS"
      vertical-align="top"
      :hover="true"
      table-layout="fixed"
      :loading="dataLoading"
      style="margin-top: 15px"
    >
      <template #name="{ row }">
        <t-avatar shape="round" :hide-on-load-failed="true" :image="imgPre + row.logo">{{ row.name }}</t-avatar>
        {{ row.name }}
      </template>
      <template #type="{ row }">
        {{ dictStore.getDictItemValue('APP_TYPE', `${row.type}`) }}
      </template>
      <template #url="{ row }">
        <t-link :href="row.url" style="color: blue">{{ row.url }}</t-link>
      </template>
      <template #enable="{ row }">
        <t-tag v-if="row.enable == 1" theme="success">{{
          dictStore.getDictItemValue('STATUS', `${row.enable}`)
        }}</t-tag>
        <t-tag v-else theme="danger">{{ dictStore.getDictItemValue('STATUS', `${row.enable}`) }}</t-tag>
      </template>
      <template #op="slotProps">
        <t-space>
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
    <add-app-form ref="addAppFormRef" @fetch-data="fetchData" />
  </t-drawer>
</template>
<script setup lang="ts">
import { MessagePlugin } from 'tdesign-vue-next';
import { reactive, ref } from 'vue';

import * as applicationMgt from '@/api/system/applicationMgt';
import * as userMgt from '@/api/system/userMgt';
import { useDictStore } from '@/store';

import AddAppForm from './AddAppForm.vue';

const imgPre = import.meta.env.VITE_SYS_IMG_PRE;
const dictStore = useDictStore();
const visible = ref(false);
const state = reactive({
  title: '应用详情',
});
const COLUMNS = [
  {
    title: '编号',
    ellipsis: false,
    colKey: 'code',
  },
  {
    title: '名称',
    ellipsis: false,
    colKey: 'name',
  },
  {
    title: '类型',
    ellipsis: false,
    colKey: 'type',
  },
  {
    title: '跳转链接',
    ellipsis: false,
    colKey: 'url',
  },
  {
    title: '启用',
    ellipsis: false,
    colKey: 'enable',
  },
  {
    align: 'left',
    fixed: 'right',
    ellipsis: false,
    width: 100,
    colKey: 'op',
    title: '操作',
  },
];
const data = ref([]);
const dataLoading = ref(false);

const userId = ref(0);
const name = ref('');
const addAppFormRef = ref(null);
const show = async (row?: any) => {
  userId.value = row.id;
  name.value = row.realName;
  state.title = `用户【${name.value}】的应用详情`;
  visible.value = true;
  fetchData();
};
const fetchData = async () => {
  dataLoading.value = true;
  try {
    const res = await applicationMgt.findByUserId(userId.value);
    data.value = res;
  } catch (e) {
    console.log(e);
  } finally {
    dataLoading.value = false;
  }
};

const onConfirmDelete = async (id: any) => {
  // 真实业务请发起请求
  await userMgt.deleteApp(userId.value, id);
  await fetchData();
  MessagePlugin.success('删除成功');
};
const showAddAppForm = (userId: any, name: any) => {
  addAppFormRef.value.show(userId, name);
};
defineExpose({
  show,
});
</script>
