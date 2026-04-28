<template>
  <t-row :gutter="[24, 24]">
    <t-col :flex="3">
      <div class="user-left-greeting">
        <div>
          Hi，{{ state.userInfo.realName }}
          <span class="regular">您好</span>
        </div>
        <img src="@/assets/assets-tencent-logo.png" class="logo" :hidden="true" />
      </div>

      <t-card class="user-info-list" :title="$t('pages.user.personalInfo.title')" :bordered="false">
        <template #actions>
          <t-button theme="default" shape="square" variant="text">
            <t-icon name="ellipsis" />
          </t-button>
        </template>
        <t-row class="content" justify="space-between">
          <t-col class="contract" :span="3">
            <div class="contract-title">真实姓名</div>
            <div class="contract-detail">{{ state.userInfo.realName }}</div>
          </t-col>
          <t-col class="contract" :span="3">
            <div class="contract-title">用户名</div>
            <div class="contract-detail">{{ state.userInfo.username }}</div>
          </t-col>
          <t-col class="contract" :span="3">
            <div class="contract-title">工号</div>
            <div class="contract-detail">{{ state.userInfo.jobNo }}</div>
          </t-col>
          <t-col class="contract" :span="3">
            <div class="contract-title">性别</div>
            <div class="contract-detail">{{ dictStore.getDictItemValue('GENDER', `${state.userInfo.gender}`) }}</div>
          </t-col>
          <t-col class="contract" :span="3">
            <div class="contract-title">Email</div>
            <div class="contract-detail">{{ state.userInfo.email }}</div>
          </t-col>
          <t-col class="contract" :span="9">
            <div class="contract-title">联系电话</div>
            <div class="contract-detail">{{ state.userInfo.mobilePhone }}</div>
          </t-col>
        </t-row>
      </t-card>
    </t-col>

    <t-col :flex="1">
      <t-card class="user-intro" :bordered="false">
        <t-avatar size="80px" :image="imgPre + state.userInfo.avatar">T</t-avatar>
        <div class="name">{{ state.userInfo.realName }}</div>
        <div class="position"></div>
      </t-card>
    </t-col>
  </t-row>
</template>
<script setup>
import { onMounted, reactive } from 'vue';

import { useDictStore, useUserStore } from '@/store';

const imgPre = import.meta.env.VITE_SYS_IMG_PRE;
const userStore = useUserStore();
const dictStore = useDictStore();

const state = reactive({
  userInfo: {},
});
const fetchData = async () => {
  state.userInfo = userStore.userInfo;
};

onMounted(() => {
  fetchData();
});
</script>

<style lang="less" scoped>
@import './index.less';
</style>
