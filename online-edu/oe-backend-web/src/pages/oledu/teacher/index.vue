<template>
  <t-card>
    <t-form layout="inline" :data="queryForm" :label-width="50" colon @submit="onSubmit">
      <t-form-item label="编号" name="code">
        <t-input v-model="queryForm.code" placeholder="请输入编号" clearable />
      </t-form-item>
      <t-form-item label="名字" name="name">
        <t-input v-model="queryForm.name" placeholder="请输入名字" clearable />
      </t-form-item>
      <t-form-item label="电话" name="tel">
        <t-input v-model="queryForm.tel" placeholder="请输入电话" clearable />
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
      <template #avatar="{ row }">
        <t-image v-if="row.avatar" :src="`${imgPre}${row.avatar}`" style="max-width: 30px; max-height: 30px" />
      </template>
      <template #gender="{ row }">
        <t-tag v-if="row.gender == 1" theme="primary" variant="light">{{
          dictStore.getDictItemValue('GENDER', `${row.gender}`)
        }}</t-tag>
        <t-tag v-else-if="row.gender == 2" theme="danger" variant="light">{{
          dictStore.getDictItemValue('GENDER', `${row.gender}`)
        }}</t-tag>
        <t-tag v-else-if="row.gender == 0" theme="default">{{
          dictStore.getDictItemValue('GENDER', `${row.gender}`)
        }}</t-tag>
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

    <teacher-form ref="teacherFormRef" @fetch-data="fetchData" />
  </t-card>
</template>
<script setup lang="ts">
import { MessagePlugin } from 'tdesign-vue-next';
import { onMounted, reactive, ref } from 'vue';

import * as teacherMgt from '@/api/oledu/teacherMgt';
import { useDictStore } from '@/store';

import TeacherForm from './components/TeacherForm.vue';

const imgPre = import.meta.env.VITE_SYS_IMG_PRE;
const dictStore = useDictStore();

const state = reactive({
  batchDeleteTitle: '批量删除',
});

export interface QueryForm {
  code?: string;
  name?: string;
  tel?: string;
  pageNum?: number;
  pageSize?: number;
}
const queryForm = ref<QueryForm>({
  pageNum: 1,
  pageSize: 20,
});

const teacherFormRef = ref(null);

const COLUMNS = [
  {
    fixed: 'left',
    colKey: 'row-select',
    type: 'multiple',
    width: 64,
  },
  {
    title: '头像',
    ellipsis: false,
    colKey: 'avatar',
  },
  {
    title: '编号',
    ellipsis: false,
    colKey: 'code',
    width: 200,
  },
  {
    title: '姓名',
    ellipsis: false,
    colKey: 'name',
  },
  {
    title: '性别',
    ellipsis: false,
    colKey: 'gender',
  },
  {
    title: '电话',
    ellipsis: false,
    colKey: 'tel',
    width: 150,
  },
  {
    title: '描述',
    ellipsis: false,
    colKey: 'description',
    width: 250,
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
    const { content, totalItems } = await teacherMgt.page(queryForm.value);
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
  await teacherMgt.del(id);
  await fetchData();
  MessagePlugin.success('删除成功');
};

const showForm = (id?: any) => {
  teacherFormRef.value.show(id);
};

onMounted(() => {
  fetchData();
});

const handleBatchDelete = async () => {
  // const ids = selectedRowKeys.value.map((item) => item).join();
  await teacherMgt.batchDel(selectedRowKeys.value);
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
