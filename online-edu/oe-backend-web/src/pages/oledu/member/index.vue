<template>
  <t-card>
    <t-form layout="inline" :data="queryForm" :label-width="50" colon @submit="onSubmit">
      <t-form-item label="昵称" name="name">
        <t-input v-model="queryForm.name" placeholder="请输入昵称" clearable />
      </t-form-item>
      <t-form-item label="编号" name="code">
        <t-input v-model="queryForm.code" placeholder="请输入编号" clearable />
      </t-form-item>
      <t-form-item label="电话" name="tel">
        <t-input v-model="queryForm.tel" placeholder="请输入电话" clearable />
      </t-form-item>
      <t-button v-if="!expandForm" variant="text" @click="handleExpandForm">
        展开
        <template #suffix> <t-icon name="chevron-down" size="14" /></template>
      </t-button>
      <t-form-item v-if="expandForm" label="性别" name="gender">
        <t-select v-model="queryForm.gender" :style="{ width: '175px' }" clearable>
          <t-option v-for="item in genderMap" :key="item.itemKey" :value="+item.itemKey" :label="item.itemValue" />
        </t-select>
      </t-form-item>
      <t-form-item v-if="expandForm" label="地区" name="areaId">
        <t-cascader v-model="queryForm.areaId" :style="{ width: '175px' }" :options="areaTree" clearable />
      </t-form-item>
      <t-form-item v-if="expandForm" label="状态" name="enable">
        <t-select v-model="queryForm.enable" :style="{ width: '175px' }" clearable>
          <t-option v-for="item in statusMap" :key="item.itemKey" :value="+item.itemKey" :label="item.itemValue" />
        </t-select>
      </t-form-item>
      <t-form-item v-if="expandForm" label="姓名" name="idCardName">
        <t-input v-model="queryForm.idCardName" placeholder="请输入姓名" clearable />
      </t-form-item>
      <t-form-item v-if="expandForm" label="身份证" name="idCardNumber" :label-width="60">
        <t-input v-model="queryForm.idCardNumber" placeholder="请输入身份证" clearable />
      </t-form-item>
      <t-button v-if="expandForm" variant="text" @click="handleExpandForm">
        收起
        <template #suffix> <t-icon name="chevron-up" size="14" /></template>
      </t-button>
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
      <template #type="{ row }">
        <t-tag v-if="row.type == 1" theme="default">{{
          dictStore.getDictItemValue('MEMBER_TYPE', `${row.type}`)
        }}</t-tag>
        <t-tag v-else-if="row.type == 2" theme="primary">{{
          dictStore.getDictItemValue('MEMBER_TYPE', `${row.type}`)
        }}</t-tag>
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
      <template #status="{ row }">
        <t-tag v-if="row.enable == 1" theme="success">{{
          dictStore.getDictItemValue('STATUS', `${row.enable}`)
        }}</t-tag>
        <t-tag v-else theme="danger">{{ dictStore.getDictItemValue('STATUS', `${row.enable}`) }}</t-tag>
      </template>
      <template #op="slotProps">
        <t-space>
          <t-link theme="primary" @click="showDetail(slotProps.row)">详情</t-link>
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

    <member-form ref="studentFormRef" @fetch-data="fetchData" />
    <member-detail ref="memberDetailRef" />
  </t-card>
</template>
<script setup lang="ts">
import { MessagePlugin } from 'tdesign-vue-next';
import { onMounted, reactive, ref } from 'vue';

import * as memberMgt from '@/api/oledu/memberMgt';
import * as baseMgt from '@/api/system/baseMgt';
import { useDictStore } from '@/store';

import MemberDetail from './components/MemberDetail.vue';
import MemberForm from './components/MemberForm.vue';

const state = reactive({
  batchDeleteTitle: '批量删除',
});

export interface QueryForm {
  name?: string;
  code?: string;
  tel?: string;
  gender?: number;
  areaId?: number;
  enable?: number;
  idCardName?: string;
  idCardNumber?: string;
  pageNum?: number;
  pageSize?: number;
}
const queryForm = ref<QueryForm>({
  pageNum: 1,
  pageSize: 20,
});
const memberDetailRef = ref(null);
const studentFormRef = ref(null);
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
    width: 150,
  },
  {
    title: '姓名',
    ellipsis: false,
    colKey: 'name',
  },
  {
    title: '类型',
    ellipsis: false,
    colKey: 'type',
  },
  {
    title: '性别',
    ellipsis: false,
    colKey: 'gender',
  },
  {
    title: '电话号码',
    ellipsis: false,
    colKey: 'tel',
    width: 150,
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
    width: 200,
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
const dictStore = useDictStore();
const genderMap = dictStore.getDictItem('GENDER');
const statusMap = dictStore.getDictItem('STATUS');
const expandForm = ref(false);
const areaTree = ref([]);
const data = ref([]);

const selectedRowKeys = ref([]);

const dataLoading = ref(false);

const imgPre = import.meta.env.VITE_SYS_IMG_PRE;
const fetchData = async () => {
  dataLoading.value = true;
  try {
    const { content, totalItems } = await memberMgt.page(queryForm.value);
    data.value = content;
    pagination.value.total = Number(totalItems);
    const areaData = await baseMgt.areaTree();
    areaTree.value = areaData.map((item) => ({
      label: item.name,
      value: item.id,
      children: item.children.map((item2) => ({
        label: item2.name,
        value: item2.id,
      })),
    }));
  } catch (e) {
    console.log(e);
  } finally {
    dataLoading.value = false;
  }
};
const onConfirmDelete = async (id: any) => {
  // 真实业务请发起请求
  await memberMgt.del(id);
  await fetchData();
  MessagePlugin.success('删除成功');
};

const handleExpandForm = () => {
  expandForm.value = !expandForm.value;
};

const showDetail = (row?: any) => {
  memberDetailRef.value.show(row);
};

const showForm = (id?: any) => {
  studentFormRef.value.show(id);
};

onMounted(() => {
  fetchData();
});

const handleBatchDelete = async () => {
  // const ids = selectedRowKeys.value.map((item) => item).join();
  await memberMgt.batchDel(selectedRowKeys.value);
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
