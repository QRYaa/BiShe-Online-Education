<template>
  <t-card>
    <t-form layout="inline" :data="queryForm" :label-width="80" colon @submit="onSubmit">
      <t-form-item label="订单编号" name="code">
        <t-input v-model="queryForm.code" clearable />
      </t-form-item>
      <t-form-item label="交易编号" name="transactionId">
        <t-input v-model="queryForm.transactionId" clearable />
      </t-form-item>
      <t-button v-if="!expandForm" variant="text" @click="handleExpandForm">
        展开
        <template #suffix> <t-icon name="chevron-down" size="14" /></template>
      </t-button>
      <t-form-item v-if="expandForm" label="起始" name="startTime" :label-width="50">
        <t-date-picker v-model="queryForm.startTime" enable-time-picker clearable />
      </t-form-item>
      <t-form-item v-if="expandForm" label="结束" name="endTime" :label-width="50">
        <t-date-picker v-model="queryForm.endTime" enable-time-picker clearable />
      </t-form-item>
      <t-form-item v-if="expandForm" label="支付状态" name="paymentStatus">
        <t-select v-model="queryForm.paymentStatus" :style="{ width: '175px' }" clearable>
          <t-option
            v-for="item in paymentStatusMap"
            :key="item.itemKey"
            :value="+item.itemKey"
            :label="item.itemValue"
          />
        </t-select>
      </t-form-item>
      <t-form-item v-if="expandForm" label="学员编码" name="memberCode">
        <t-input v-model="queryForm.memberCode" placeholder="请输入学员编码" clearable />
      </t-form-item>
      <t-button v-if="expandForm" variant="text" @click="handleExpandForm">
        收起
        <template #suffix> <t-icon name="chevron-up" size="14" /></template>
      </t-button>
      <t-button theme="default" type="submit"> 查询 </t-button>
    </t-form>
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
    >
      <template #memberName="{ row }">
        <t-avatar
          v-if="row.memberAvatar"
          shape="circle"
          size="24px"
          :hide-on-load-failed="true"
          :image="imgPre + row.memberAvatar"
        />
        <t-avatar v-else shape="circle" size="24px" :hide-on-load-failed="true" image="/src/assets/avatar.jpg" />
        <span style="margin-left: 10px">{{ row.memberName }}</span>
      </template>
      <template #paymentMethod="{ row }">
        <t-tag v-if="row.paymentMethod === 1" theme="success" variant="light">{{
          dictStore.getDictItemValue('PAYMENT_METHOD', `${row.paymentMethod}`)
        }}</t-tag>
        <t-tag v-if="row.paymentMethod === 2" theme="primary" variant="light">{{
          dictStore.getDictItemValue('PAYMENT_METHOD', `${row.paymentMethod}`)
        }}</t-tag>
      </template>
      <template #paymentStatus="{ row }">
        <t-tag v-if="row.paymentStatus === 1" theme="default" variant="light">{{
          dictStore.getDictItemValue('ORDER_STATUS', `${row.paymentStatus}`)
        }}</t-tag>
        <t-tag v-if="row.paymentStatus === 2" theme="success" variant="light">{{
          dictStore.getDictItemValue('ORDER_STATUS', `${row.paymentStatus}`)
        }}</t-tag>
        <t-tag v-if="row.paymentStatus === 3" theme="default" variant="light">{{
          dictStore.getDictItemValue('ORDER_STATUS', `${row.paymentStatus}`)
        }}</t-tag>
      </template>
      <template #op="slotProps">
        <t-space>
          <t-link theme="primary" @click="showDetail(slotProps.row)">详情</t-link>
          <t-link theme="primary" @click="changePaymentStatus(slotProps.row)">修改状态</t-link>
        </t-space>
      </template>
    </t-table>
    <order-detail ref="orderDetailRef" />
    <change-payment-status-form ref="changePaymentStatusFormRef" @fetch-data="fetchData" />
  </t-card>
</template>
<script setup lang="ts">
import { onMounted, ref } from 'vue';

import * as orderMgt from '@/api/oledu/orderMgt';
import { useDictStore } from '@/store';

import ChangePaymentStatusForm from './components/ChangePaymentStatusForm.vue';
import OrderDetail from './components/OrderDetail.vue';

const imgPre = import.meta.env.VITE_SYS_IMG_PRE;
const expandForm = ref(false);
const dictStore = useDictStore();
const changePaymentStatusFormRef = ref(null);
const paymentStatusMap = dictStore.getDictItem('ORDER_STATUS');
export interface QueryForm {
  code?: string;
  transactionId?: string;
  startTime?: string;
  endTime?: string;
  paymentStatus?: number;
  memberCode?: string;
  pageNum?: number;
  pageSize?: number;
}
const queryForm = ref<QueryForm>({
  pageNum: 1,
  pageSize: 20,
});

const COLUMNS = [
  {
    title: '编号',
    ellipsis: false,
    colKey: 'code',
    width: 200,
  },
  {
    title: '学员昵称',
    ellipsis: false,
    colKey: 'memberName',
    width: 200,
  },
  {
    title: '交易编号',
    ellipsis: false,
    colKey: 'transactionId',
    width: 200,
  },
  {
    title: '实付金额',
    ellipsis: false,
    colKey: 'finalPrice',
  },
  {
    title: '支付状态',
    ellipsis: false,
    colKey: 'paymentStatus',
  },
  {
    title: '支付方式',
    ellipsis: false,
    colKey: 'paymentMethod',
  },
  {
    title: '创建时间',
    ellipsis: false,
    colKey: 'createdTime',
    width: 200,
  },
  {
    title: '支付时间',
    ellipsis: false,
    colKey: 'paymentTime',
    width: 200,
  },
  {
    title: '备注',
    ellipsis: false,
    colKey: 'remark',
    width: 150,
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

const dataLoading = ref(false);

const orderDetailRef = ref(null);

const handleExpandForm = () => {
  expandForm.value = !expandForm.value;
};
const showDetail = (row?: any) => {
  orderDetailRef.value.show(row);
};

const changePaymentStatus = (row?: any) => {
  changePaymentStatusFormRef.value.show(row);
};

const fetchData = async () => {
  dataLoading.value = true;
  try {
    const { content, totalItems } = await orderMgt.page(queryForm.value);
    data.value = content;
    pagination.value.total = Number(totalItems);
  } catch (e) {
    console.log(e);
  } finally {
    dataLoading.value = false;
  }
};

onMounted(() => {
  fetchData();
});

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
</script>
