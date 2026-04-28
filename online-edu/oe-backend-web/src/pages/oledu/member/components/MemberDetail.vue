<template>
  <t-drawer v-model:visible="visible" :header="state.title" :close-btn="true" size="900px" :footer="false">
    <t-tabs :default-value="1">
      <t-tab-panel :destroy-on-hide="false" :value="1" label="学习课程记录">
        <member-course-list ref="memberCourseListRef"
      /></t-tab-panel>
      <t-tab-panel :destroy-on-hide="false" :value="2" label="收藏课程记录">
        <member-collect-course-list ref="memberCollectCourseListRef"
      /></t-tab-panel>
      <t-tab-panel :destroy-on-hide="false" :value="3" label="实名认证">
        <member-id-card ref="memberIdCardRef" />
      </t-tab-panel>
    </t-tabs>
  </t-drawer>
</template>
<script setup lang="ts">
import { reactive, ref } from 'vue';

import MemberCollectCourseList from './MemberCollectCourseList.vue';
import MemberCourseList from './MemberCourseList.vue';
import MemberIdCard from './MemberIdCard.vue';

const visible = ref(false);
const memberCourseListRef = ref(null);
const memberCollectCourseListRef = ref(null);
const memberIdCardRef = ref(null);
const state = reactive({
  title: '',
});
const show = async (member?: any) => {
  visible.value = true;
  state.title = `学员名称【${member.name}】`;
  // 初始化第一个标签下的页面
  memberCourseListRef.value.show(member);
  // 初始化第二个标签下的页面
  memberCollectCourseListRef.value.show(member);
  // 初始化第三个标签下的页面
  memberIdCardRef.value.show(member.id);
};

defineExpose({
  show,
});
</script>
