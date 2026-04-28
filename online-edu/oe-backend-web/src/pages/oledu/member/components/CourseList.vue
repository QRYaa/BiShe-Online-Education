<template>
  <t-dialog
    v-model:visible="formVisible"
    :header="state.headerTitle"
    :width="680"
    :footer="false"
    destroy-on-close
    :on-close="close"
    :close-on-overlay-click="false"
  >
    <t-card>
      <t-form layout="inline" :data="queryForm" :label-width="50" colon @submit="onSubmit">
        <t-form-item label="类型" name="courseTypeId">
          <t-select v-model="queryForm.courseTypeId" :style="{ width: '200px' }" clearable>
            <t-option v-for="item in state.courseTypeList" :key="item.id" :value="item.id" :label="item.name" />
          </t-select>
        </t-form-item>
        <t-form-item label="名称" name="name">
          <t-input v-model="queryForm.name" :style="{ width: '200px' }" placeholder="请输入名称" clearable />
        </t-form-item>
        <t-form-item label="学员" name="memberId">
          <t-select v-model="queryForm.memberId" :style="{ width: '200px' }" clearable>
            <t-option :value="memberId" :label="memberName"></t-option>
          </t-select>
        </t-form-item>
        <t-button theme="default" type="submit"> 查询 </t-button>
      </t-form>
      <br />
      <t-row>
        <t-popconfirm
          theme="warning"
          content="确认开通所选课程吗"
          :confirm-btn="{
            content: '批量开通',
            theme: 'warning',
          }"
          :disabled="!selectedRowKeys.length"
          :style="{ marginLeft: '8px' }"
          @confirm="handleBatchSave()"
        >
          <t-button theme="warning" :disabled="!selectedRowKeys.length">{{ state.batchSaveTitle }}</t-button>
        </t-popconfirm>
        <t-popconfirm
          theme="danger"
          content="确认取消开通所选课程吗"
          :confirm-btn="{
            content: '批量取消开通',
            theme: 'danger',
          }"
          :disabled="!selectedRowKeys.length"
          :style="{ marginLeft: '8px' }"
          @confirm="handleBatchDelete()"
        >
          <t-button theme="danger" :disabled="!selectedRowKeys.length">{{ state.batchDeleteTitle }}</t-button>
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
          <t-image
            v-if="row.horizontalImage"
            :src="`${imgPre}${row.horizontalImage}`"
            style="max-width: 53px; max-height: 30px"
          />
        </template>
        <template #courseType="{ row }">
          {{ courseTypeNameMap.get(row.courseTypeId) }}
        </template>
        <template #op="{ row }">
          <t-space>
            <t-link v-if="!memberCourseDataMap.get(row.id)" @click="onConfirmSave(row.id)">开通</t-link>
            <t-link v-else @click="onConfirmDelete(row.id)">取消开通</t-link>
          </t-space>
        </template>
      </t-table>
    </t-card>
  </t-dialog>
</template>
<script setup lang="ts">
import { MessagePlugin } from 'tdesign-vue-next';
import { reactive, ref } from 'vue';

import * as courseMgt from '@/api/oledu/courseMgt';
import * as courseTypeMgt from '@/api/oledu/courseTypeMgt';
import * as memberCourseMgt from '@/api/oledu/memberCourseMgt';

const formVisible = ref(false);
const emit = defineEmits(['fetch-data']);
const memberId = ref(0);
const memberName = ref('');
const imgPre = import.meta.env.VITE_SYS_IMG_PRE;
const courseTypeNameMap = new Map();
const state = reactive({
  headerTitle: '学员开通课程',
  batchDeleteTitle: '批量取消开通',
  batchSaveTitle: '批量开通',
  courseTypeList: [],
});

export interface QueryForm {
  courseTypeId?: number;
  name?: string;
  memberId?: number;
  pageNum?: number;
  pageSize?: number;
}
const queryForm = ref<QueryForm>({
  pageNum: 1,
  pageSize: 20,
});

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
    title: '课程类型',
    ellipsis: false,
    colKey: 'courseType',
  },
  {
    title: '编号',
    ellipsis: false,
    colKey: 'code',
    width: 150,
  },
  {
    title: '名称',
    ellipsis: false,
    colKey: 'name',
    width: 200,
  },
  {
    align: 'left',
    fixed: 'right',
    ellipsis: false,
    width: 250,
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

const memberCourseDataMap = ref(new Map());

const selectedRowKeys = ref([]);

const dataLoading = ref(false);

const fetchCourseTypeListData = async () => {
  const records = await courseTypeMgt.listAll();
  state.courseTypeList = records;
  records.forEach((item) => {
    courseTypeNameMap.set(item.id, item.name);
  });
};

const fetchMemberCourseListData = async () => {
  try {
    memberCourseDataMap.value = new Map();
    const mcData = await memberCourseMgt.findByMemberId(memberId.value);
    mcData.forEach((item) => {
      memberCourseDataMap.value.set(item.courseId, item);
    });
  } catch (e) {
    console.log(e);
  }
};

const fetchData = async () => {
  dataLoading.value = true;
  try {
    const { content, totalItems } = await courseMgt.page(queryForm.value);
    await fetchCourseTypeListData();
    await fetchMemberCourseListData();
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
  await memberCourseMgt.del(memberCourseDataMap.value.get(id).id);
  await fetchData();
  MessagePlugin.success('取消开通成功');
  emit('fetch-data');
};

const onConfirmSave = async (id: any) => {
  await memberCourseMgt.activateCourse(memberId.value, id);
  await fetchData();
  MessagePlugin.success('开通成功');
  emit('fetch-data');
};

const handleBatchDelete = async () => {
  // const ids = selectedRowKeys.value.map((item) => item).join();
  const deleteArray = [];
  selectedRowKeys.value.forEach((item) => {
    if (memberCourseDataMap.value.get(item)) deleteArray.push(memberCourseDataMap.value.get(item).id);
  });
  await memberCourseMgt.batchDel(deleteArray);
  await fetchData();
  MessagePlugin.success('取消开通成功');
  state.batchDeleteTitle = '批量取消开通';
  selectedRowKeys.value = [];
  emit('fetch-data');
};

const handleBatchSave = async () => {
  await memberCourseMgt.batchSave(memberId.value, selectedRowKeys.value);
  await fetchData();
  MessagePlugin.success('开通成功');
  state.batchSaveTitle = '批量开通';
  selectedRowKeys.value = [];
  emit('fetch-data');
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
    state.batchDeleteTitle = `批量取消开通(${val.length})`;
  } else {
    state.batchDeleteTitle = '批量取消开通';
  }
};
const show = async (mId: any, mName: any) => {
  formVisible.value = true;
  memberId.value = mId;
  memberName.value = mName;
  state.headerTitle = `学员【${mName}】开通课程`;
  await fetchData();
};
const close = () => {
  formVisible.value = false;
};
defineExpose({
  show,
});
</script>
<style lang="less" scoped>
.op-btn {
  padding: 0 0 0 0;
  height: 22px;
  line-height: 22px;
  margin-top: -2px;
}
</style>
