<template>
  <t-card>
    <t-form layout="inline" :data="queryForm" :label-width="50" colon @submit="onSubmit">
      <t-form-item label="追踪" name="trackId" :label-width="50">
        <t-input v-model="queryForm.trackId" placeholder="请输入追踪编号" clearable :style="{ width: '200px' }" />
      </t-form-item>
      <t-form-item label="发送人编码" name="senderCode" :label-width="100">
        <t-input v-model="queryForm.senderCode" placeholder="" clearable :style="{ width: '200px' }" />
      </t-form-item>
      <t-form-item label="启用" name="enable" :label-width="50">
        <t-select v-model="queryForm.enable" placeholder="" clearable :style="{ width: '200px' }">
          <t-option v-for="item in yesNoMap" :key="item.itemKey" :value="+item.itemKey" :label="item.itemValue" />
        </t-select>
      </t-form-item>
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
      <template #senderName="{ row }">
        <t-avatar
          v-if="row.senderAvatar"
          shape="circle"
          size="24px"
          :hide-on-load-failed="true"
          :image="imgPre + row.senderAvatar"
        />
        <t-avatar v-else shape="circle" size="24px" :hide-on-load-failed="true" image="/src/assets/avatar.jpg" />
        <span style="margin-left: 10px">{{ row.senderName }}</span>
      </template>
      <template #receiverName="{ row }">
        <t-avatar
          v-if="row.receiverAvatar"
          shape="circle"
          size="24px"
          :hide-on-load-failed="true"
          :image="imgPre + row.receiverAvatar"
        />
        <t-avatar v-else shape="circle" size="24px" :hide-on-load-failed="true" image="/src/assets/avatar.jpg" />
        <span style="margin-left: 10px">{{ row.receiverName }}</span>
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
      <template #root="{ row }">{{ row.root ? '是' : '否' }} </template>
    </t-table>
  </t-card>
</template>
<script setup lang="ts">
import { MessagePlugin } from 'tdesign-vue-next';
import { ref } from 'vue';

import * as replyMgt from '@/api/oledu/replyMgt';
import { useDictStore } from '@/store';

const imgPre = import.meta.env.VITE_SYS_IMG_PRE;
const dictStore = useDictStore();
const yesNoMap = dictStore.getDictItem('YES_NO');
export interface QueryForm {
  noteId?: number;
  trackId?: string;
  senderCode?: string;
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
    title: '发送人名称',
    ellipsis: false,
    colKey: 'senderName',
    width: 200,
  },
  {
    title: '接收人名称',
    ellipsis: false,
    colKey: 'receiverName',
    width: 200,
  },
  {
    title: '内容',
    ellipsis: false,
    colKey: 'content',
    width: 200,
  },
  {
    title: '启用',
    ellipsis: false,
    colKey: 'enable',
  },
  {
    title: '创建时间',
    ellipsis: false,
    colKey: 'createTime',
    width: 200,
  },
  {
    title: '追踪编号',
    ellipsis: false,
    colKey: 'trackId',
    width: 200,
  },
  {
    title: '点赞数',
    ellipsis: false,
    colKey: 'likeNum',
  },
  {
    title: '第一条',
    ellipsis: false,
    colKey: 'root',
  },
];

const pagination = ref({
  defaultPageSize: 20,
  total: 0,
  defaultCurrent: 1,
});

const data = ref([]);

const dataLoading = ref(false);

const setNoteId = async (noteId?: any) => {
  queryForm.value.noteId = noteId;
  await fetchData();
};
const fetchData = async () => {
  dataLoading.value = true;
  try {
    const { content, totalItems } = await replyMgt.page(queryForm.value);
    data.value = content;
    pagination.value.total = Number(totalItems);
  } catch (e) {
    console.log(e);
  } finally {
    dataLoading.value = false;
  }
};

const switchEnable = async (row: any) => {
  try {
    await replyMgt.switchEnable(row);
    MessagePlugin.success('修改成功');
    fetchData();
  } catch (e) {
    console.log(e);
  }
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
defineExpose({
  setNoteId,
});
</script>
