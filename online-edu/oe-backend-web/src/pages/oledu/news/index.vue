<template>
  <t-card>
    <t-form layout="inline" :data="queryForm" :label-width="50" colon @submit="onSubmit">
      <t-form-item label="标题" name="title">
        <t-input v-model="queryForm.title" placeholder="请输入标题" clearable />
      </t-form-item>
      <t-form-item label="编码" name="code">
        <t-input v-model="queryForm.code" placeholder="" clearable />
      </t-form-item>
      <t-button v-if="!expandForm" variant="text" @click="handleExpandForm">
        展开
        <template #suffix> <t-icon name="chevron-down" size="14" /></template>
      </t-button>
      <t-form-item v-if="expandForm" label="发布" name="published">
        <t-select v-model="queryForm.published" :style="{ width: '200px' }" clearable>
          <t-option v-for="item in yesNoMap" :key="item.itemKey" :value="+item.itemKey" :label="item.itemValue" />
        </t-select>
      </t-form-item>
      <t-row>
        <t-form-item v-if="expandForm" label="起始" name="startTime">
          <t-date-picker v-model="queryForm.startTime" enable-time-picker clearable :style="{ width: '175px' }" />
        </t-form-item>
        <t-form-item v-if="expandForm" label="结束" name="stopTime">
          <t-date-picker v-model="queryForm.stopTime" enable-time-picker clearable :style="{ width: '175px' }" />
        </t-form-item>

        <t-button theme="default" type="submit"> 查询 </t-button>
        <t-button v-if="expandForm" variant="text" @click="handleExpandForm">
          收起
          <template #suffix> <t-icon name="chevron-up" size="14" /></template>
        </t-button>
      </t-row>
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
        <t-image v-if="row.image" :src="`${imgPre}${row.image}`" style="max-width: 30px; max-height: 30px" />
      </template>
      <template #top="{ row }">
        <t-tag v-if="row.pinned" theme="success">{{
          dictStore.getDictItemValue('YES_NO', `${Number(row.pinned)}`)
        }}</t-tag>
        <t-tag v-else theme="default">{{ dictStore.getDictItemValue('YES_NO', `${Number(row.pinned)}`) }}</t-tag>
      </template>
      <template #published="{ row }">
        <t-tag v-if="row.published" theme="success">{{
          dictStore.getDictItemValue('YES_NO', `${Number(row.published)}`)
        }}</t-tag>
        <t-tag v-else theme="danger">{{ dictStore.getDictItemValue('YES_NO', `${Number(row.published)}`) }}</t-tag>
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

    <news-form ref="newsFormRef" @fetch-data="fetchData" />
  </t-card>
</template>
<script setup lang="ts">
import { MessagePlugin } from 'tdesign-vue-next';
import { onMounted, reactive, ref } from 'vue';

import * as newsMgt from '@/api/oledu/newsMgt';
import { useDictStore } from '@/store';

import NewsForm from './components/NewsForm.vue';

const imgPre = import.meta.env.VITE_SYS_IMG_PRE;
const state = reactive({
  batchDeleteTitle: '批量删除',
  catagoryList: [],
});

export interface QueryForm {
  title?: string;
  code?: string;
  startTime?: string;
  stopTime?: string;
  author?: string;
  published?: number;
  pageNum?: number;
  pageSize?: number;
}
const queryForm = ref<QueryForm>({
  pageNum: 1,
  pageSize: 20,
});

const expandForm = ref(false);

const newsFormRef = ref(null);

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
    title: '编号',
    ellipsis: false,
    colKey: 'code',
    width: 180,
  },
  {
    title: '标题',
    ellipsis: false,
    colKey: 'title',
    width: 200,
  },
  {
    title: '摘要',
    ellipsis: false,
    colKey: 'digest',
    width: 200,
  },
  {
    title: '是否置顶',
    ellipsis: false,
    colKey: 'pinned',
    cell: 'top',
  },
  {
    title: '是否发布',
    ellipsis: false,
    colKey: 'published',
    cell: 'published',
  },
  {
    title: '发表时间',
    ellipsis: false,
    colKey: 'publishedTime',
    width: 200,
  },
  {
    title: '作者',
    ellipsis: false,
    colKey: 'author',
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
const yesNoMap = dictStore.getDictItem('YES_NO');

const handleExpandForm = () => {
  expandForm.value = !expandForm.value;
};

const fetchData = async () => {
  dataLoading.value = true;
  try {
    const { content, totalItems } = await newsMgt.page(queryForm.value);
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
  await newsMgt.del(id);
  await fetchData();
  MessagePlugin.success('删除成功');
};

const showForm = (id?: any) => {
  newsFormRef.value.show(id);
};

onMounted(() => {
  fetchData();
});

const handleBatchDelete = async () => {
  // const ids = selectedRowKeys.value.map((item) => item).join();
  await newsMgt.batchDel(selectedRowKeys.value);
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
