<template>
  <t-card>
    <t-form layout="inline" :data="queryForm" :label-width="80" colon @submit="onSubmit">
      <t-form-item label="学员编码" name="memberCode">
        <t-input v-model="queryForm.memberCode" placeholder="" clearable />
      </t-form-item>
      <t-form-item label="课程名称" name="courseName">
        <t-input v-model="queryForm.courseName" placeholder="" clearable />
      </t-form-item>
      <t-form-item label="课节名称" name="lessonName">
        <t-input v-model="queryForm.lessonName" placeholder="" clearable />
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
      <t-form-item v-if="expandForm" label="状态" name="status" :label-width="50">
        <t-select v-model="queryForm.status" placeholder="" clearable>
          <t-option
            v-for="item in dictStore.getDictItem('NOTE_STATUS')"
            :key="item.itemKey"
            :value="+item.itemKey"
            :label="item.itemValue"
          />
        </t-select>
      </t-form-item>
      <t-form-item v-if="expandForm" label="启用" name="enable" :label-width="50">
        <t-select v-model="queryForm.enable" placeholder="" clearable>
          <t-option v-for="item in yesNoMap" :key="item.itemKey" :value="+item.itemKey" :label="item.itemValue" />
        </t-select>
      </t-form-item>

      <t-button v-if="expandForm" variant="text" @click="handleExpandForm">
        收起
        <template #suffix> <t-icon name="chevron-up" size="14" /></template>
      </t-button>
      <t-button theme="default" type="submit"> 查询 </t-button>
    </t-form>
    <br />

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
      <template #status="{ row }">
        <t-tag v-if="row.status === 1" theme="warning" variant="light">{{
          dictStore.getDictItemValue('NOTE_STATUS', `${row.status}`)
        }}</t-tag>
        <t-tag v-else-if="row.status === 2" theme="primary" variant="light">{{
          dictStore.getDictItemValue('NOTE_STATUS', `${row.status}`)
        }}</t-tag>
      </template>
      <template #enable="{ row }">
        <t-popconfirm theme="warning" content="此操作将修改启用状态，确认操作？" @confirm="switchEnable(row)">
          <t-switch :value="row.enable" size="large">
            <template #label>
              <template v-if="row.enable">
                <t-icon name="check" />
              </template>
              <template v-else>
                <t-icon name="close" />
              </template>
            </template>
          </t-switch>
        </t-popconfirm>
      </template>
      <template #op="slotProps">
        <t-space>
          <t-link theme="primary" @click="showDetail(slotProps.row)">详情</t-link>
        </t-space>
      </template>
    </t-table>
    <note-detail ref="noteDetailRef" />
  </t-card>
</template>
<script setup lang="ts">
import { MessagePlugin } from 'tdesign-vue-next';
import { onMounted, ref } from 'vue';

import * as noteMgt from '@/api/oledu/noteMgt';
import { useDictStore } from '@/store';

import NoteDetail from './components/NoteDetail.vue';

const imgPre = import.meta.env.VITE_SYS_IMG_PRE;
const dictStore = useDictStore();
const expandForm = ref(false);
const yesNoMap = dictStore.getDictItem('YES_NO');
const noteDetailRef = ref(null);

export interface QueryForm {
  memberCode?: string;
  courseName?: string;
  lessonName?: string;
  startTime?: string;
  endTime?: string;
  status?: number;
  enable?: number;
  pageNum?: number;
  pageSize?: number;
}
const queryForm = ref<QueryForm>({
  pageNum: 1,
  pageSize: 20,
});

const COLUMNS = [
  {
    title: '学员昵称',
    ellipsis: false,
    colKey: 'memberName',
    width: 200,
  },
  {
    title: '课程名称',
    ellipsis: false,
    colKey: 'courseName',
    width: 200,
  },
  {
    title: '课节名称',
    ellipsis: false,
    colKey: 'lessonName',
    width: 200,
  },
  {
    title: '创建时间',
    ellipsis: false,
    colKey: 'createTime',
    width: 200,
  },
  {
    title: '状态',
    ellipsis: false,
    colKey: 'status',
  },
  {
    title: '启用',
    ellipsis: false,
    colKey: 'enable',
  },
  {
    title: '点赞数',
    ellipsis: false,
    colKey: 'likeNum',
  },
  {
    title: '回复数',
    ellipsis: false,
    colKey: 'replyNum',
  },
  {
    align: 'left',
    fixed: 'right',
    ellipsis: false,
    width: 200,
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

const handleExpandForm = () => {
  expandForm.value = !expandForm.value;
};

const showDetail = (row: any) => {
  noteDetailRef.value.show(row);
};
const fetchData = async () => {
  dataLoading.value = true;
  try {
    const { content, totalItems } = await noteMgt.page(queryForm.value);
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
const switchEnable = async (row: any) => {
  try {
    await noteMgt.switchEnable(row);
    MessagePlugin.success('修改成功');
    fetchData();
  } catch (e) {
    console.log(e);
  }
};
</script>
