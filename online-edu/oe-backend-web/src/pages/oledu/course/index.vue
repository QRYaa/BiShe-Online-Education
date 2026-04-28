<template>
  <t-card id="oleduCourseIndex">
    <update-status-form ref="updateStatusFormRef" @fetch-data="fetchData" />
    <t-form layout="inline" :data="queryForm" :label-width="50" colon @submit="onSubmit">
      <t-form-item label="类型" name="courseTypeId">
        <t-select v-model="queryForm.courseTypeId" :style="{ width: '200px' }" clearable>
          <t-option v-for="item in state.courseTypeList" :key="item.id" :value="item.id" :label="item.name" />
        </t-select>
      </t-form-item>
      <t-form-item label="名称" name="name">
        <t-input v-model="queryForm.name" :style="{ width: '200px' }" placeholder="请输入名称" clearable />
      </t-form-item>
      <t-button v-if="!expandForm" variant="text" @click="handleExpandForm">
        展开
        <template #suffix> <t-icon name="chevron-down" size="14" /></template>
      </t-button>
      <t-form-item v-if="expandForm" label="讲师" name="teacherId">
        <t-select
          v-model="queryForm.teacherId"
          placeholder="请选择讲师"
          style="width: 250px; display: inline-block"
          filterable
          clearable
        >
          <t-option v-for="item in state.teacherList" :key="item.id" :value="item.id" :label="item.name">
            <template #content>
              <div style="display: flex; align-items: start">
                <t-avatar shape="circle" size="24px" :hide-on-load-failed="true" :image="imgPre + item.avatar">
                  {{ item.name }}
                </t-avatar>
                <div style="width: 180px; padding-left: 10px">{{ item.name }}</div>
                <div>{{ dictStore.getDictItemValue('GENDER', `${item.gender}`) }}</div>
              </div>
            </template>
          </t-option>
        </t-select>
      </t-form-item>
      <t-form-item v-if="expandForm" label="状态" name="status">
        <t-select v-model="queryForm.status" :style="{ width: '200px' }" clearable>
          <t-option v-for="item in statusMap" :key="item.itemKey" :value="+item.itemKey" :label="item.itemValue" />
        </t-select>
      </t-form-item>
      <t-form-item v-if="expandForm" label="发布" name="published">
        <t-select v-model="queryForm.published" :style="{ width: '200px' }" clearable>
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
    <t-row>
      <t-button @click="showForm()"> 新建 </t-button>
      <!-- <t-button v-permissions="['COURSE_MGT']" @click="showForm()"> 新建 </t-button> -->
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
        <!-- <t-button v-permissions="['COURSE_MGT']" theme="danger" :disabled="!selectedRowKeys.length">
          {{ state.batchDeleteTitle }}
        </t-button> -->
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
      <template #teacher="{ row }">
        {{ teacherNameMap.get(row.teacherId) }}
      </template>
      <template #courseType="{ row }">
        {{ courseTypeNameMap.get(row.courseTypeId) }}
      </template>
      <template #status="{ row }">
        {{ dictStore.getDictItemValue('COURSE_STATUS', `${row.status}`) }}
      </template>
      <template #paid="{ row }">
        <t-tag v-if="row.paid == 1" theme="primary">{{
          dictStore.getDictItemValue('YES_NO', `${Number(row.paid)}`)
        }}</t-tag>
        <t-tag v-else theme="default">{{ dictStore.getDictItemValue('YES_NO', `${Number(row.paid)}`) }}</t-tag>
      </template>
      <template #published="{ row }">
        <t-popconfirm theme="warning" content="此操作将修改课程上架状态，确认操作？" @confirm="switchPublished(row)">
          <t-switch :value="row.published" size="large">
            <template #label>
              <template v-if="row.published">
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
          <t-link theme="primary" @click="showForm(slotProps.row.id)">修改</t-link>
          <!-- <t-link v-permissions="['COURSE_MGT']" theme="primary" @click="showForm(slotProps.row.id)">修改</t-link> -->
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
            <!-- <t-link v-permissions="['COURSE_MGT']" theme="danger">删除</t-link> -->
          </t-popconfirm>
          <t-dropdown
            v-if="!slotProps.row.paid && !slotProps.row.published"
            :options="options"
            trigger="click"
            @click="(option) => clickHandler(option, slotProps.row)"
          >
            <t-space>
              <t-button variant="text" class="op-btn">
                更多
                <template #suffix> <t-icon name="chevron-down" size="14" /></template>
              </t-button>
            </t-space>
          </t-dropdown>
          <t-dropdown
            v-else-if="slotProps.row.paid && slotProps.row.published"
            :options="options2"
            trigger="click"
            @click="(option) => clickHandler(option, slotProps.row)"
          >
            <t-space>
              <t-button variant="text" class="op-btn">
                更多
                <template #suffix> <t-icon name="chevron-down" size="14" /></template>
              </t-button>
            </t-space>
          </t-dropdown>
          <t-dropdown
            v-else-if="slotProps.row.paid && !slotProps.row.published"
            :options="options3"
            trigger="click"
            @click="(option) => clickHandler(option, slotProps.row)"
          >
            <t-space>
              <t-button variant="text" class="op-btn">
                更多
                <template #suffix> <t-icon name="chevron-down" size="14" /></template>
              </t-button>
            </t-space>
          </t-dropdown>
          <t-dropdown
            v-else-if="!slotProps.row.paid && slotProps.row.published"
            :options="options4"
            trigger="click"
            @click="(option) => clickHandler(option, slotProps.row)"
          >
            <t-space>
              <t-button variant="text" class="op-btn">
                更多
                <template #suffix> <t-icon name="chevron-down" size="14" /></template>
              </t-button>
            </t-space>
          </t-dropdown>
        </t-space>
      </template>
    </t-table>

    <course-form ref="courseFormRef" @fetch-data="fetchData" />
    <course-detail ref="courseDetailRef" />
  </t-card>
</template>
<script setup lang="ts">
import { MessagePlugin } from 'tdesign-vue-next';
import { onMounted, reactive, ref } from 'vue';

import * as courseMgt from '@/api/oledu/courseMgt';
import * as courseTypeMgt from '@/api/oledu/courseTypeMgt';
import * as teacherMgt from '@/api/oledu/teacherMgt';
import { useDictStore } from '@/store';

import CourseDetail from './components/CourseDetail.vue';
import CourseForm from './components/CourseForm.vue';
import UpdateStatusForm from './components/UpdateStatusForm.vue';

const imgPre = import.meta.env.VITE_SYS_IMG_PRE;
const dictStore = useDictStore();
const statusMap = dictStore.getDictItem('COURSE_STATUS');
const yesNoMap = dictStore.getDictItem('YES_NO');
const courseTypeNameMap = new Map();
const teacherNameMap = new Map();
const state = reactive({
  batchDeleteTitle: '批量删除',
  courseTypeList: [],
  teacherList: [],
});

const expandForm = ref(false);
const updateStatusFormRef = ref(null);
const courseDetailRef = ref(null);
export interface QueryForm {
  courseTypeId?: number;
  teacherId?: number;
  name?: string;
  status?: number;
  paid?: number;
  published?: number;
  pageNum?: number;
  pageSize?: number;
}
const queryForm = ref<QueryForm>({
  pageNum: 1,
  pageSize: 20,
});

const courseFormRef = ref(null);

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
    title: '讲师',
    ellipsis: false,
    colKey: 'teacher',
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
    title: '描述',
    ellipsis: false,
    colKey: 'description',
    width: 250,
  },
  {
    title: '是否发布',
    ellipsis: false,
    colKey: 'published',
  },
  {
    title: '状态',
    ellipsis: false,
    colKey: 'status',
  },
  {
    title: '原始价格',
    ellipsis: false,
    colKey: 'originalPrice',
  },
  {
    title: '价格',
    ellipsis: false,
    colKey: 'price',
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

const selectedRowKeys = ref([]);

const dataLoading = ref(false);

const options = [
  { content: '修改状态', value: 1 },

  { content: '收费', value: 2 },

  { content: '发布', value: 3 },
];
const options2 = [
  { content: '修改状态', value: 1 },

  { content: '取消收费', value: 2 },

  { content: '取消发布', value: 3 },
];
const options3 = [
  { content: '修改状态', value: 1 },

  { content: '取消收费', value: 2 },

  { content: '发布', value: 3 },
];
const options4 = [
  { content: '修改状态', value: 1 },

  { content: '收费', value: 2 },

  { content: '取消发布', value: 3 },
];

const fetchCourseTypeListData = async () => {
  const records = await courseTypeMgt.listAll();
  state.courseTypeList = records;
  records.forEach((item) => {
    courseTypeNameMap.set(item.id, item.name);
  });
};

const fetchTeachListData = async () => {
  const records = await teacherMgt.listAll();
  state.teacherList = records;
  records.forEach((item) => {
    teacherNameMap.set(item.id, item.name);
  });
};

const fetchData = async () => {
  dataLoading.value = true;
  try {
    const { content, totalItems } = await courseMgt.page(queryForm.value);
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
  await courseMgt.del(id);
  await fetchData();
  MessagePlugin.success('删除成功');
};

const showForm = (id?: any) => {
  courseFormRef.value.show(id);
};

onMounted(() => {
  fetchData();
  fetchCourseTypeListData();
  fetchTeachListData();
});

const handleExpandForm = () => {
  expandForm.value = !expandForm.value;
};
const handleBatchDelete = async () => {
  // const ids = selectedRowKeys.value.map((item) => item).join();
  await courseMgt.batchDel(selectedRowKeys.value);
  await fetchData();
  MessagePlugin.success('删除成功');
  state.batchDeleteTitle = '批量删除';
  selectedRowKeys.value = [];
};
const onSubmit = () => {
  queryForm.value.pageNum = 1;
  fetchData();
};
const clickHandler = async (option, row) => {
  console.log(option.content, ' 被点击 ');
  if (option.value === 1) {
    changeStatus(row);
  } else if (option.value === 2) {
    switchPaid(row);
  } else if (option.value === 3) {
    switchPublished(row);
  }
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
const showDetail = (row?: any) => {
  courseDetailRef.value.show(row);
};
const changeStatus = async (row: any) => {
  updateStatusFormRef.value.show(row);
};
const switchPaid = async (row: any) => {
  await courseMgt.switchPaid(row);
  MessagePlugin.success('修改成功');
  fetchData();
};
const switchPublished = async (row: any) => {
  await courseMgt.switchPublished(row);
  MessagePlugin.success('修改成功');
  fetchData();
};
</script>
<style lang="less" scoped>
.op-btn {
  padding: 0 0 0 0;
  height: 22px;
  line-height: 22px;
  margin-top: -2px;
}
</style>
