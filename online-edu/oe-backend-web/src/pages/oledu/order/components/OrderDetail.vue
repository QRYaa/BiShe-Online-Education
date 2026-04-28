<template>
  <t-dialog
    v-model:visible="visible"
    :header="state.title"
    :width="880"
    :footer="false"
    destroy-on-close
    :close-on-overlay-click="false"
  >
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
      <template #image="{ row }">
        <t-image v-if="row.image" style="max-width: 30px; max-height: 30px" :src="`${imgPre}${row.image}`" />
      </template>
      <template #type="{ row }">
        <t-tag v-if="row.type === 1" variant="light" theme="primary">{{
          dictStore.getDictItemValue('ORDER_ITEM_TYPE', `${row.type}`)
        }}</t-tag>
        <t-tag v-else variant="light" theme="default">{{
          dictStore.getDictItemValue('ORDER_ITEM_TYPE', `${row.type}`)
        }}</t-tag>
      </template>
    </t-table>
  </t-dialog>
</template>
<script setup lang="ts">
import { reactive, ref } from 'vue';

import * as orderItemMgt from '@/api/oledu/orderItemMgt';
import { useDictStore } from '@/store';

const visible = ref(false);

const state = reactive({
  title: '',
});
const imgPre = import.meta.env.VITE_SYS_IMG_PRE;
const dictStore = useDictStore();
export interface QueryForm {
  orderId?: number;
  pageNum?: number;
  pageSize?: number;
}
const queryForm = ref<QueryForm>({
  pageNum: 1,
  pageSize: 20,
});

const COLUMNS = [
  {
    title: '图片',
    ellipsis: false,
    colKey: 'image',
    width: 100,
  },
  {
    title: '类型',
    ellipsis: false,
    colKey: 'type',
    width: 100,
  },
  {
    title: '名称',
    ellipsis: false,
    colKey: 'name',
    width: 170,
  },
  {
    title: '描述',
    ellipsis: false,
    colKey: 'description',
    width: 300,
  },
  {
    title: '价格',
    ellipsis: false,
    colKey: 'price',
    width: 100,
  },
  {
    title: '数量',
    ellipsis: false,
    colKey: 'quantity',
    width: 100,
  },
  {
    title: '总价',
    ellipsis: false,
    colKey: 'totalPrice',
    width: 100,
  },
];

const pagination = ref({
  defaultPageSize: 20,
  total: 0,
  defaultCurrent: 1,
});

const data = ref([]);

const dataLoading = ref(false);
const fetchData = async () => {
  dataLoading.value = true;
  try {
    const { content, totalItems } = await orderItemMgt.page(queryForm.value);
    data.value = content;
    pagination.value.total = Number(totalItems);
  } catch (e) {
    console.log(e);
  } finally {
    dataLoading.value = false;
  }
};

const rehandleChange = (changeParams: any, triggerAndData: any) => {
  console.log('统一Change', changeParams, triggerAndData);
  queryForm.value.pageNum = changeParams.pagination.current;
  queryForm.value.pageSize = changeParams.pagination.pageSize;
  fetchData();
};
const show = async (order?: any) => {
  visible.value = true;
  state.title = `订单编号【${order.code}】`;
  queryForm.value.orderId = order.id;
  await fetchData();
};

defineExpose({
  show,
});
</script>
