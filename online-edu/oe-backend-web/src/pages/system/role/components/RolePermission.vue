<template>
  <t-drawer
    v-model:visible="visible"
    :header="state.title"
    :close-btn="true"
    size="400px"
    confirm-btn="保存"
    :on-confirm="save"
    @close="close"
  >
    <t-tree
      :ref="myTree"
      v-model:value="state.checkedData"
      :data="state.data"
      :keys="{
        value: 'id',
        label: 'name',
      }"
      hover
      line
      expand-all
      :checkable="true"
      :check-strictly="true"
      value-mode="all"
    >
      <template #label="{ node }">
        {{ node.data.name }} <t-tag>{{ node.data.code }}</t-tag>
      </template>
    </t-tree>
  </t-drawer>
</template>

<script setup>
import { MessagePlugin } from 'tdesign-vue-next';
import { defineExpose, reactive, ref } from 'vue';

import * as permissionMgt from '@/api/system/permissionMgt';
import * as roleMgt from '@/api/system/roleMgt';

const state = reactive({
  data: [],
  checkData: [],
  roleId: 0,
  title: '',
});
const visible = ref(false);
const myTree = ref(null);

const fetchData = async () => {
  try {
    const list = await permissionMgt.tree(null);
    state.data = list;
    const checkedList = await roleMgt.rolePermission(state.roleId);
    state.checkedData = checkedList;
  } catch (e) {
    console.log(e);
  }
};

const show = (row) => {
  visible.value = true;
  state.roleId = row.id;
  state.title = `分配权限 - ${row.name}`;
  fetchData();
};

const close = () => {
  state.data = [];
  state.checkedData = [];
  visible.value = false;
};

const save = async () => {
  const dto = {};
  dto.roleId = state.roleId;
  dto.permissionIds = state.checkedData;
  await roleMgt.assignRolePermission(dto);
  close();
  MessagePlugin.success('保存成功');
};

defineExpose({
  show,
});
</script>
