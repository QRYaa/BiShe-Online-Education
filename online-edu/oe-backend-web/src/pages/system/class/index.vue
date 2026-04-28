<template>
  <t-card>
    <t-form layout="inline" :data="queryForm" :label-width="50" colon @submit="onSubmit">
      <t-form-item label="模块" name="classModuleId">
        <t-select v-model="queryForm.classModuleId" clearable>
          <t-option v-for="item in state.moduleNameList" :key="item.id" :value="item.id" :label="item.name" />
        </t-select>
      </t-form-item>
      <t-form-item label="标题" name="title">
        <t-input v-model="queryForm.title" placeholder="请输入标题" clearable />
      </t-form-item>
      <!-- <t-form-item label="机构" name="organization">
        <t-input v-model="queryForm.organization" placeholder="请输入机构" clearable />
      </t-form-item> -->
      <t-form-item label="形式" name="teachingFormItemValue">
        <t-select v-model="queryForm.teachingFormItemValue" :style="{ width: '200px' }" clearable>
          <t-option v-for="item in formMap" :key="item.itemKey" :value="+item.itemKey" :label="item.itemValue" />
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
      <t-button theme="default" variant="outline" @click="showTypeList()">课程类型 </t-button>
      <t-button theme="default" variant="outline" @click="showModuleList()">课程模块 </t-button>
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
      <template #teacher="{ row }">
        <div v-if="row.teacherIdList && row.teacherIdList.length">
          <t-tag
            v-for="(teacherId, index) in row.teacherIdList"
            :key="index"
            :style="{ marginRight: '5px', marginBottom: '5px' }"
            theme="primary"
            variant="light"
          >
            {{ teacherMap.get(teacherId) }}
          </t-tag>
        </div>
        <div v-else>No Data</div>
      </template>
      <template #teachingForms="{ row }">
        <div v-if="row.teachingFormList && row.teachingFormList.length">
          <t-tag
            v-for="(teachingForm, index) in row.teachingFormList"
            :key="index"
            :style="{ marginRight: '5px', marginBottom: '5px' }"
            theme="primary"
            variant="light"
          >
            {{ dictStore.getDictItemValue('TEACHING_FORM', `${teachingForm}`) }}
          </t-tag>
        </div>
        <div v-else>No Data</div>
      </template>

      <template #moduleName="{ row }">
        {{ moduleMap.get(row.classModuleId) }}
      </template>

      <template #classType="{ row }">
        {{ typeNameMap.get(typeIdMap.get(row.classModuleId)) }}
      </template>
      <template #op="slotProps">
        <t-space>
          <t-link theme="primary" @click="showForm(slotProps.row.id, slotProps.row.classModuleId)">修改</t-link>
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
    <class-form ref="classFormRef" @fetch-data="fetchData" />
    <class-module-list ref="classModuleListRef" />
    <class-type-list ref="classTypeListRef" />
  </t-card>
</template>
<script setup lang="ts">
import { MessagePlugin } from 'tdesign-vue-next';
import { onMounted, reactive, ref } from 'vue';

import * as classMgt from '@/api/system/classMgt';
import * as classModuleMgt from '@/api/system/classModuleMgt';
import * as classTypeMgt from '@/api/system/classTypeMgt';
import * as teacherMgt from '@/api/system/teacherMgt';
import { useDictStore } from '@/store';

import ClassForm from './components/ClassForm.vue';
import ClassModuleList from './components/ClassModuleList.vue';
import ClassTypeList from './components/ClassTypeList.vue';

const state = reactive({
  batchDeleteTitle: '批量删除',
  moduleNameList: [],
  typeIdList: [],
  typeNameList: [],
});

export interface QueryForm {
  // name?: string;
  pageNum?: number;
  pageSize?: number;
  classModuleId?: number;
  title?: string;
  organizatin?: string;
  teachingFormItemValue?: number;
}
const queryForm = ref<QueryForm>({
  pageNum: 1,
  pageSize: 20,
});

const classFormRef = ref(null);

const COLUMNS = [
  {
    fixed: 'left',
    colKey: 'row-select',
    type: 'multiple',
    width: 64,
  },
  {
    title: '课程标题',
    ellipsis: false,
    colKey: 'title',
  },
  {
    title: '提供机构',
    ellipsis: false,
    colKey: 'organization',
  },
  {
    title: '教师',
    ellipsis: false,
    colKey: 'teacher',
  },
  {
    title: '适用对象',
    ellipsis: false,
    colKey: 'suitableUser',
  },
  {
    title: '授课形式',
    ellipsis: false,
    colKey: 'teachingForms',
  },
  {
    title: '备注',
    ellipsis: false,
    colKey: 'remark',
  },

  {
    title: '模块名称',
    ellipsis: false,
    colKey: 'moduleName',
  },

  {
    title: '课程类型',
    ellipsis: false,
    colKey: 'classType',
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

const classModuleListRef = ref(null);
const classTypeListRef = ref(null);

const pagination = ref({
  defaultPageSize: 20,
  total: 0,
  defaultCurrent: 1,
});

const data = ref([]);

const selectedRowKeys = ref([]);

const dataLoading = ref(false);

const dictStore = useDictStore();

const moduleMap = new Map();
const typeIdMap = new Map();
const typeNameMap = new Map();
const formMap = dictStore.getDictItem('TEACHING_FORM');
const teacherMap = new Map();
const showModuleList = () => {
  classModuleListRef.value.show();
};
const showTypeList = () => {
  classTypeListRef.value.show();
};

const fetchData = async () => {
  dataLoading.value = true;
  try {
    await Promise.all([fetchTeacherMap(), fetchModuleNameListData(), fetchtypeIdListData(), fetchtypeNameListData()]);
    const { content, totalItems } = await classMgt.page(queryForm.value);
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
  await classMgt.del(id);
  await fetchData();
  MessagePlugin.success('删除成功');
};

const showForm = (id?: any, classModuleId?: any) => {
  classFormRef.value.show(id, classModuleId);
};

onMounted(() => {
  fetchData();
});

const handleBatchDelete = async () => {
  // const ids = selectedRowKeys.value.map((item) => item).join();
  await classMgt.batchDel(selectedRowKeys.value);
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

const fetchModuleNameListData = async () => {
  const records = await classModuleMgt.listAll();
  state.moduleNameList = records;
  records.forEach((item) => {
    moduleMap.set(item.id, item.name);
  });
};

const fetchtypeIdListData = async () => {
  const records = await classModuleMgt.listAll();
  state.typeIdList = records;
  records.forEach((item) => {
    typeIdMap.set(item.id, item.classTypeId);
  });
};
const fetchtypeNameListData = async () => {
  const records = await classTypeMgt.listAll();
  state.typeNameList = records;
  records.forEach((item) => {
    typeNameMap.set(item.id, item.name);
  });
};
const fetchTeacherMap = async () => {
  const records = await teacherMgt.listAll();
  records.forEach((item) => {
    teacherMap.set(item.id, item.name);
  });
};
</script>
