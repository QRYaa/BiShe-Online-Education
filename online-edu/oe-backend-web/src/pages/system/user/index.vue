<template>
  <div class="table-tree-container">
    <div class="list-tree-wrapper">
      <div class="list-tree-operator">
        <t-input v-model="filterText" placeholder="请输入关键词" :clearable="true" @change="onFilterDepartment">
          <template #suffix-icon>
            <search-icon size="var(--td-comp-size-xxxs)" />
          </template>
        </t-input>
        <t-tree
          ref="departmentTree"
          :activable="true"
          :data="state.departmentTreeData"
          :keys="{
            value: 'id',
            label: 'name',
          }"
          :filter="filterByText"
          :default-expanded="[-1]"
          hover
          line
          expand-on-click-node
          @click="onClickDepartment"
        />
      </div>
      <div class="list-tree-content">
        <user-form ref="userFormRef" @fetch-data="fetchData" />
        <assign-role-form ref="assignRoleFormRef" @fetch-data="fetchData" />
        <app-detail ref="appDetailRef" />
        <change-password-form ref="changePasswordFormRef" />
        <t-card :bordered="false">
          <t-form ref="queryFormRef" :data="queryForm" layout="inline" :label-width="50" colon @submit="onSubmit">
            <t-form-item label="名称" name="realName">
              <t-input v-model="queryForm.realName" placeholder="请输入名称" clearable />
            </t-form-item>
            <t-form-item label="用户名" name="username">
              <t-input v-model="queryForm.username" placeholder="请输入用户名" clearable />
            </t-form-item>
            <t-form-item v-permissions="['USER_MGT']" label="角色" name="roleId">
              <t-select v-model="queryForm.roleId" clearable>
                <t-option v-for="item in state.roleList" :key="item.id" :value="item.id" :label="item.name" />
              </t-select>
            </t-form-item>

            <t-button theme="default" type="submit"> 查询 </t-button>
          </t-form>
          <br />
          <t-row>
            <t-button v-permissions="['USER_MGT']" @click="showUserForm()"> 新建 </t-button>
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
              <t-button v-permissions="['USER_MGT']" theme="danger" :disabled="!selectedRowKeys.length">
                {{ state.batchDeleteTitle }}
              </t-button>
            </t-popconfirm>
            <t-popconfirm
              theme="warning"
              content="此操作将全面覆盖企微通讯录，请谨慎操作（仅会处理分配了企微应用的用户）"
              @confirm="initWxCpAddressBook()"
            >
              <t-button v-permissions="['USER_MGT']"> 初始化企微通讯录 </t-button>
            </t-popconfirm>
            <t-popconfirm
              theme="warning"
              content="此操作将增量覆盖企微通讯录，请谨慎操作（仅会处理分配了企微应用的用户）"
              @confirm="pushWxCpAddressBook()"
            >
              <t-button v-permissions="['USER_MGT']"> 推送至企微通讯录 </t-button>
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
            <template #name="{ row }">
              <t-avatar shape="round" :hide-on-load-failed="true" :image="imgPre + row.avatar">{{
                row.realName
              }}</t-avatar>
              {{ row.realName }}
            </template>
            <template #gender="{ row }">
              {{ dictStore.getDictItemValue('GENDER', `${row.gender}`) }}
            </template>
            <template #roles="{ row }">
              <t-tag
                v-for="(role, index) in row.roleList"
                :key="index"
                :style="{ marginRight: '5px', marginBottom: '5px' }"
                theme="primary"
                variant="light"
                >{{ role.name }}</t-tag
              >
            </template>
            <template #status="{ row }">
              <t-tag v-if="row.enable == 1" theme="success">{{
                dictStore.getDictItemValue('STATUS', `${row.enable}`)
              }}</t-tag>
              <t-tag v-else theme="danger">{{ dictStore.getDictItemValue('STATUS', `${row.enable}`) }}</t-tag>
            </template>
            <template #op="{ row }">
              <t-space>
                <t-link v-permissions="['USER_MGT']" theme="primary" @click="showUserForm(row.id)">修改</t-link>
                <t-popconfirm
                  theme="danger"
                  content="确认删除该数据吗"
                  :confirm-btn="{
                    content: '删除',
                    theme: 'danger',
                  }"
                  @confirm="onConfirmDelete(row.id)"
                >
                  <t-link v-permissions="['USER_MGT']" theme="danger">删除</t-link>
                </t-popconfirm>

                <t-dropdown :options="options" trigger="click" @click="(option) => dropdownHandler(option, row)">
                  <t-link> 更多 ></t-link>
                </t-dropdown>
              </t-space>
            </template>
          </t-table>
        </t-card>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
export interface QueryForm {
  realName: string;
  username: string;
  roleId?: number;
  departmentId?: number;
  pageNum?: number;
  pageSize?: number;
}
</script>

<script setup lang="ts">
import { SearchIcon } from 'tdesign-icons-vue-next';
import { InputProps, MessagePlugin, TableProps, TreeProps } from 'tdesign-vue-next';
import { onMounted, reactive, ref } from 'vue';

import * as departmentMgt from '@/api/system/departmentMgt';
import * as roleMgt from '@/api/system/roleMgt';
import * as userMgt from '@/api/system/userMgt';
import * as wxCpUserMgt from '@/api/system/wxCpUserMgt';
// import { imagePrefix } from '@/config/global';
import { useDictStore, useUserStore } from '@/store';

import AppDetail from './components/AppDetail.vue';
import AssignRoleForm from './components/AssignRoleForm.vue';
import ChangePasswordForm from './components/ChangePasswordForm.vue';
import UserForm from './components/UserForm.vue';

const options = [
  { content: '修改密码', value: 1 },
  { content: '分配角色', value: 2 },
  { content: '应用设置', value: 3 },
];

const dropdownHandler = (data, row) => {
  if (data.value === 1) {
    changePasswordFormRef.value.show(row);
  } else if (data.value === 2) {
    assignRoleFormRef.value.show(row);
  } else if (data.value === 3) {
    appDetailRef.value.show(row);
  }
};
const imgPre = import.meta.env.VITE_SYS_IMG_PRE;
const state = reactive({
  batchDeleteTitle: '批量删除',
  departmentTreeData: [],
  roleList: [],
});
const queryForm = ref<QueryForm>({
  realName: '',
  username: '',
  pageNum: 1,
  pageSize: 20,
});

const userFormRef = ref(null);
const assignRoleFormRef = ref(null);
const queryFormRef = ref(null);
const changePasswordFormRef = ref(null);
const appDetailRef = ref(null);

const dictStore = useDictStore();
const userStore = useUserStore();

const filterByText = ref<TreeProps['filter']>();
const filterText = ref('');

const onFilterDepartment: InputProps['onChange'] = (state) => {
  if (filterText.value) {
    filterByText.value = (node) => {
      const rs = (node.data.name as string).indexOf(filterText.value) >= 0;
      return rs;
    };
  } else {
    filterByText.value = null;
  }
};

const additionTreeData = [
  { id: -1, name: '全部用户' },
  { id: -2, name: '未分部门用户' },
];

const COLUMNS = ref<TableProps['columns']>([
  {
    fixed: 'left',
    colKey: 'row-select',
    type: 'multiple',
    width: 64,
  },
  {
    fixed: 'left',
    title: '姓名',
    ellipsis: false,
    colKey: 'name',
    width: 140,
  },
  {
    title: '用户名',
    ellipsis: true,
    colKey: 'username',
  },
  {
    title: '工号',
    ellipsis: true,
    colKey: 'jobNo',
  },
  {
    title: '性别',
    ellipsis: true,
    colKey: 'gender',
  },
  {
    title: '联系电话',
    ellipsis: false,
    width: 130,
    colKey: 'mobilePhone',
  },
  {
    title: '邮箱',
    ellipsis: false,
    width: 150,
    colKey: 'email',
  },
  {
    title: '角色',
    ellipsis: false,
    colKey: 'roles',
  },
  {
    title: '状态',
    fixed: 'right',
    ellipsis: false,
    colKey: 'enable',
    cell: 'status',
    width: 80,
  },
]);

const initColumn = () => {
  if (userStore.permissions.includes('USER_MGT')) {
    COLUMNS.value.push({
      fixed: 'right',
      ellipsis: false,
      colKey: 'op',
      title: '操作',
      width: 180,
    });
  }
};

const pagination = ref({
  defaultPageSize: 20,
  total: 0,
  defaultCurrent: 1,
});

const departmentTree = ref(null);

const fetchDepartmentData = async () => {
  try {
    const records = await departmentMgt.tree(null);
    state.departmentTreeData = [...additionTreeData, ...records];
  } catch (e) {
    console.log(e);
  }
};

const fetchRoleListData = async () => {
  const records = await roleMgt.listAll();
  state.roleList = records;
};

const onClickDepartment: TreeProps['onClick'] = (context) => {
  console.log('click department:', context.node.value);
  queryForm.value.departmentId = Number(context.node.value);
  queryFormRef.value.submit();
};

const showUserForm = (id?: any) => {
  userFormRef.value.show(id);
};

const data = ref([]);

const selectedRowKeys = ref([]);

const dataLoading = ref(false);
const fetchData = async () => {
  dataLoading.value = true;
  try {
    const { content, totalItems } = await userMgt.page(queryForm.value);
    data.value = content;
    pagination.value.total = Number(totalItems);
  } catch (e) {
    console.log(e);
  } finally {
    dataLoading.value = false;
  }
};

const onConfirmDelete = async (id: any) => {
  await userMgt.del(id);
  await fetchData();
  MessagePlugin.success('删除成功');
};

const handleBatchDelete = async () => {
  await userMgt.batchDel(selectedRowKeys.value);
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
const initWxCpAddressBook = () => {
  wxCpUserMgt.init();
  MessagePlugin.info('具体信息请查看日志');
};
const pushWxCpAddressBook = () => {
  wxCpUserMgt.push();
  MessagePlugin.info('具体信息请查看日志');
};

onMounted(() => {
  initColumn();
  fetchDepartmentData();
  fetchRoleListData();
  fetchData();
});
</script>

<style lang="less" scoped>
.table-tree-container {
  background-color: var(--td-bg-color-container);
  border-radius: var(--td-radius-medium);

  .t-tree {
    margin-top: var(--td-comp-margin-xxl);
  }
}

.list-tree-wrapper {
  overflow-y: hidden;
}

.list-tree-operator {
  width: 220px;
  float: left;
  padding: var(--td-comp-paddingTB-xxl) var(--td-comp-paddingLR-xxl);
}

.list-tree-content {
  border-left: 1px solid var(--td-border-level-1-color);
  overflow: auto;
}
</style>
