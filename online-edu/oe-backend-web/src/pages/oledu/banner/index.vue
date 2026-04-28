<template>
  <t-card>
    <t-form layout="inline" :data="queryForm" :label-width="50" colon @submit="onSubmit">
      <t-form-item label="名称" name="name">
        <t-input v-model="queryForm.name" :style="{ width: '150px' }" placeholder="请输入名称" clearable />
      </t-form-item>
      <t-form-item label="状态" name="enable">
        <t-select v-model="queryForm.enable" :style="{ width: '150px' }" clearable>
          <t-option v-for="item in statusMap" :key="item.itemKey" :value="+item.itemKey" :label="item.itemValue" />
        </t-select>
      </t-form-item>
      <t-button v-if="!expandForm" variant="text" @click="handleExpandForm">
        展开
        <template #suffix> <t-icon name="chevron-down" size="14" /></template>
      </t-button>
      <t-form-item v-if="expandForm" label="起始" name="startTime">
        <t-date-picker v-model="queryForm.startTime" enable-time-picker clearable />
      </t-form-item>
      <t-form-item v-if="expandForm" label="结束" name="stopTime">
        <t-date-picker v-model="queryForm.stopTime" enable-time-picker clearable />
      </t-form-item>
      <t-button theme="default" type="submit"> 查询 </t-button>
      <t-button v-if="expandForm" variant="text" @click="handleExpandForm">
        收起
        <template #suffix> <t-icon name="chevron-up" size="14" /></template>
      </t-button>
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
      <template #image="{ row }">
        <t-image v-if="row.image" style="max-width: 30px; max-height: 30px" :src="`${imgPre}${row.image}`" />
      </template>
      <template #status="{ row }">
        <t-tag v-if="row.enable == 1" theme="success">{{
          dictStore.getDictItemValue('STATUS', `${row.enable}`)
        }}</t-tag>
        <t-tag v-else theme="danger">{{ dictStore.getDictItemValue('STATUS', `${row.enable}`) }}</t-tag>
      </template>
      <template #time="{ row }">
        <div>{{ row.startTime + '-' + row.endTime }}</div>
      </template>
      <template #url="{ row }">
        <t-link :href="row.url" style="color: blue" target="_blank">{{ row.url }}</t-link>
      </template>
      <template #op="slotProps">
        <t-space>
          <t-link v-if="slotProps.row.enable === 0" theme="primary" @click="updateEnableTrueById(slotProps.row.id)"
            >启用</t-link
          >
          <t-link
            v-else-if="slotProps.row.enable === 1"
            theme="primary"
            @click="updateEnableFalseById(slotProps.row.id)"
            >禁用</t-link
          >
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

    <banner-form ref="bannerFormRef" @fetch-data="fetchData" />
  </t-card>
</template>
<script setup lang="ts">
import { MessagePlugin } from 'tdesign-vue-next';
import { onMounted, reactive, ref } from 'vue';

import * as bannerMgt from '@/api/oledu/bannerMgt';
import { useDictStore } from '@/store';

import BannerForm from './components/BannerForm.vue';

const imgPre = import.meta.env.VITE_SYS_IMG_PRE;
const state = reactive({
  batchDeleteTitle: '批量删除',
});
const expandForm = ref(false);
const dictStore = useDictStore();
const statusMap = dictStore.getDictItem('STATUS');
export interface QueryForm {
  name?: string;
  enable?: number;
  startTime?: string;
  stopTime?: string;
  pageNum?: number;
  pageSize?: number;
}
const queryForm = ref<QueryForm>({
  pageNum: 1,
  pageSize: 20,
});

const bannerFormRef = ref(null);

const COLUMNS = [
  {
    fixed: 'left',
    colKey: 'row-select',
    type: 'multiple',
    width: 64,
  },
  {
    title: '封面图',
    ellipsis: false,
    colKey: 'image',
  },
  {
    title: '名称',
    ellipsis: false,
    colKey: 'name',
  },
  {
    title: '链接',
    ellipsis: false,
    colKey: 'url',
    width: 240,
  },
  {
    title: '状态',
    ellipsis: false,
    colKey: 'enable',
    cell: 'status',
  },
  {
    title: '备注',
    ellipsis: false,
    colKey: 'remark',
    width: 240,
  },
  {
    title: '时间',
    ellipsis: false,
    colKey: 'time',
    width: 300,
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
    const { content, totalItems } = await bannerMgt.page(queryForm.value);
    data.value = content;
    pagination.value.total = Number(totalItems);
  } catch (e) {
    console.log(e);
  } finally {
    dataLoading.value = false;
  }
};

const handleExpandForm = () => {
  expandForm.value = !expandForm.value;
};

const onConfirmDelete = async (id: any) => {
  // 真实业务请发起请求
  await bannerMgt.del(id);
  await fetchData();
  MessagePlugin.success('删除成功');
};

const updateEnableTrueById = async (id: any) => {
  // 真实业务请发起请求
  await bannerMgt.updateEnableTrueById(id);
  await fetchData();
  MessagePlugin.success('启用成功');
};

const updateEnableFalseById = async (id: any) => {
  // 真实业务请发起请求
  await bannerMgt.updateEnableFalseById(id);
  await fetchData();
  MessagePlugin.success('禁用成功');
};

const showForm = (id?: any) => {
  bannerFormRef.value.show(id);
};

onMounted(() => {
  fetchData();
});

const handleBatchDelete = async () => {
  // const ids = selectedRowKeys.value.map((item) => item).join();
  await bannerMgt.batchDel(selectedRowKeys.value);
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
