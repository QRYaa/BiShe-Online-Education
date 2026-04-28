<template>
  <t-card>
    <t-form layout="inline" :data="queryForm" :label-width="50" colon @submit="onSubmit">
      <t-form-item label="姓名" name="name">
        <t-input v-model="queryForm.name" placeholder="请输入名称" clearable />
      </t-form-item>
      <t-form-item label="状态" name="enable">
        <t-select v-model="queryForm.enable" :style="{ width: '200px' }" clearable>
          <t-option v-for="item in statusMap" :key="item.itemKey" :value="+item.itemKey" :label="item.itemValue" />
        </t-select>
      </t-form-item>
      <t-form-item label="标签" name="teacherTagId">
        <t-select v-model="queryForm.teacherTagId" clearable>
          <t-option v-for="item in state.tagList" :key="item.id" :value="item.id" :label="item.name" />
        </t-select>
      </t-form-item>
      <t-form-item label="阶段" name="stageType">
        <t-select v-model="queryForm.stageType" :style="{ width: '200px' }" clearable>
          <t-option v-for="item in stageMap" :key="item.itemKey" :value="+item.itemKey" :label="item.itemValue" />
        </t-select>
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
      :bordered="true"
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
      <template #enable="{ row }">
        <t-tag v-if="row.enable == 1" theme="success">{{
          dictStore.getDictItemValue('STATUS', `${row.enable}`)
        }}</t-tag>
        <t-tag v-else theme="danger">{{ dictStore.getDictItemValue('STATUS', `${row.enable}`) }}</t-tag>
      </template>
      <template #gender="{ row }">
        {{ dictStore.getDictItemValue('GENDER', `${row.gender}`) }}
      </template>

      <template #stageType="{ row }">
        {{ dictStore.getDictItemValue('STAGE_TYPE', `${row.stageType}`) }}
      </template>
      <template #tag="{ row }">
        <div v-if="row.tagIdList && row.tagIdList.length">
          <t-tag
            v-for="(tagId, index) in row.tagIdList"
            :key="index"
            :style="{ marginRight: '5px', marginBottom: '5px' }"
            theme="primary"
            variant="light"
          >
            {{ tagMap.get(tagId) }}
          </t-tag>
        </div>
        <div v-else>No Data</div>
      </template>
      <template #teacherableCourse="{ row }">
        <div v-if="row.classIdList && row.classIdList.length">
          <t-tag
            v-for="(classId, index) in row.classIdList"
            :key="index"
            :style="{ marginRight: '5px', marginBottom: '5px' }"
            theme="primary"
            variant="light"
          >
            {{ classMap.get(classId) }}
          </t-tag>
        </div>
        <div v-else>No Data</div>
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

import * as classMgt from '@/api/system/classMgt';
import * as teacherMgt from '@/api/system/teacherMgt';
import * as teacherTagMgt from '@/api/system/teacherTagMgt';
import { useDictStore } from '@/store';

import TeacherForm from './components/TeacherForm.vue';

const dictStore = useDictStore();
const statusMap = dictStore.getDictItem('STATUS');
const stageMap = dictStore.getDictItem('STAGE_TYPE');
const tagMap = new Map();
const classMap = new Map();
const state = reactive({
  batchDeleteTitle: '批量删除',
  tagList: [],
});

export interface QueryForm {
  name?: string;
  pageNum?: number;
  pageSize?: number;
  enable?: number;
  teacherTagId?: number;
  stageType?: number;
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
    title: '电话号码',
    ellipsis: false,
    colKey: 'phoneNumber',
  },
  {
    title: '阶段类型',
    ellipsis: false,
    colKey: 'stageType',
  },
  {
    title: '标签',
    ellipsis: false,
    colKey: 'tag',
  },
  {
    title: '可授课程',
    ellipsis: false,
    colKey: 'teacherableCourse',
  },
  {
    title: '状态',
    ellipsis: false,
    colKey: 'enable',
  },
  {
    title: '简介',
    ellipsis: false,
    colKey: 'description',
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
    await fetchTagMapData(); // 确保 tagMap 已填充
    await fetchClassMapData(); // 确保 classMap 已填充
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
const fetchTagMapData = async () => {
  const records = await teacherTagMgt.listAll();
  state.tagList = records;
  records.forEach((item) => {
    tagMap.set(item.id, item.name);
  });
};
const fetchClassMapData = async () => {
  const records = await classMgt.listAll();
  records.forEach((item) => {
    classMap.set(item.id, item.title);
  });
};
</script>
