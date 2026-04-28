<template>
  <t-card>
    <t-form layout="inline" :data="queryForm" :label-width="50" colon @submit="onSubmit">
      <t-form-item label="状态" name="status">
        <t-select v-model="queryForm.status" clearable>
          <t-option v-for="item in statusMap" :key="item.itemKey" :value="+item.itemKey" :label="item.itemValue" />
        </t-select>
      </t-form-item>
      <t-button v-if="!expandForm" variant="text" @click="handleExpandForm">
        展开
        <template #suffix> <t-icon name="chevron-down" size="14" /></template>
      </t-button>
      <t-form-item v-if="expandForm" label="学员编码" name="memberCode" :label-width="70">
        <t-input v-model="queryForm.memberCode" placeholder="" clearable />
      </t-form-item>
      <t-form-item v-if="expandForm" label="身份证" name="idCardNumber" :label-width="60">
        <t-input v-model="queryForm.idCardNumber" placeholder="" clearable />
      </t-form-item>
      <t-form-item v-if="expandForm" label="姓名" name="idCardRealName">
        <t-input v-model="queryForm.idCardRealName" placeholder="" clearable />
      </t-form-item>
      <t-button v-if="expandForm" variant="text" @click="handleExpandForm">
        收起
        <template #suffix> <t-icon name="chevron-up" size="14" /></template>
      </t-button>
      <t-button theme="default" type="submit"> 查询 </t-button>
    </t-form>
    <br />

    <t-table
      :bordered="false"
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
      <template #member="{ row }">
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
      <template #status="slotProps">
        <t-tag v-if="slotProps.row.status === 1" theme="warning" variant="light">{{
          dictStore.getDictItemValue('AUDIT_STATUS', '1')
        }}</t-tag>
        <t-tag v-else-if="slotProps.row.status === 2" theme="success" variant="light">{{
          dictStore.getDictItemValue('AUDIT_STATUS', '2')
        }}</t-tag>
        <t-tag v-else-if="slotProps.row.status === 3" theme="default" variant="light">{{
          dictStore.getDictItemValue('AUDIT_STATUS', '3')
        }}</t-tag>
      </template>
      <template #idCardType="slotProps">
        <t-tag v-if="slotProps.row.idCardType === 1" theme="default" variant="light">{{
          dictStore.getDictItemValue('ID_CARD_TYPE', '1')
        }}</t-tag>
        <t-tag v-if="slotProps.row.idCardType === 2" theme="primary" variant="light">{{
          dictStore.getDictItemValue('ID_CARD_TYPE', '2')
        }}</t-tag>
        <t-tag v-if="slotProps.row.idCardType === 3" theme="success" variant="light">{{
          dictStore.getDictItemValue('ID_CARD_TYPE', '3')
        }}</t-tag>
      </template>
      <template #idCardGender="{ row }">
        <t-tag v-if="row.idCardGender == 1" theme="primary" variant="light">{{
          dictStore.getDictItemValue('GENDER', `${row.gender}`)
        }}</t-tag>
        <t-tag v-else-if="row.idCardGender == 2" theme="danger" variant="light">{{
          dictStore.getDictItemValue('GENDER', `${row.gender}`)
        }}</t-tag>
        <t-tag v-else-if="row.idCardGender == 0" theme="default">{{
          dictStore.getDictItemValue('GENDER', `${row.gender}`)
        }}</t-tag>
      </template>
      <template #op="slotProps">
        <t-space>
          <t-link theme="primary" @click="showForm(slotProps.row.id)">审批</t-link>
        </t-space>
      </template>
    </t-table>

    <member-id-card-audit-form ref="memberIdCardAuditFormRef" @fetch-data="fetchData" />
  </t-card>
</template>
<script setup lang="ts">
import { onMounted, ref } from 'vue';

import * as memberIdCardAuditMgt from '@/api/oledu/memberIdCardAuditMgt';
import { useDictStore } from '@/store';

import MemberIdCardAuditForm from './components/MemberIdCardAuditForm.vue';

export interface QueryForm {
  memberCode?: string;
  status?: number;
  idCardNumber?: string;
  idCardRealName?: string;
  pageNum?: number;
  pageSize?: number;
}
const queryForm = ref<QueryForm>({
  pageNum: 1,
  pageSize: 20,
});

const memberIdCardAuditFormRef = ref(null);

const COLUMNS = [
  {
    title: '学员',
    ellipsis: false,
    colKey: 'member',
    width: 200,
  },
  {
    title: '状态',
    ellipsis: false,
    colKey: 'status',
  },
  {
    title: '审核人',
    ellipsis: false,
    colKey: 'auditorName',
    width: 120,
  },
  {
    title: '意见',
    ellipsis: false,
    colKey: 'opinion',
    width: 200,
  },
  {
    title: '创建时间',
    ellipsis: false,
    colKey: 'createTime',
    width: 180,
  },
  {
    title: '审核时间',
    ellipsis: false,
    colKey: 'auditTime',
    width: 180,
  },
  {
    title: '证件类型',
    ellipsis: false,
    colKey: 'idCardType',
  },
  {
    title: '证件姓名',
    ellipsis: false,
    colKey: 'idCardRealName',
  },
  {
    title: '证件性别',
    ellipsis: false,
    colKey: 'idCardGender',
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

const imgPre = import.meta.env.VITE_SYS_IMG_PRE;
const dictStore = useDictStore();
const statusMap = dictStore.getDictItem('AUDIT_STATUS');
const expandForm = ref(false);
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
    const { content, totalItems } = await memberIdCardAuditMgt.page(queryForm.value);
    data.value = content;
    pagination.value.total = Number(totalItems);
  } catch (e) {
    console.log(e);
  } finally {
    dataLoading.value = false;
  }
};

const showForm = (id?: any) => {
  memberIdCardAuditFormRef.value.show(id);
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
const handleExpandForm = () => {
  expandForm.value = !expandForm.value;
};
</script>
