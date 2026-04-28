<template>
  <t-drawer
    v-model:visible="visible"
    :header="state.title"
    :close-btn="true"
    size="800px"
    :footer="false"
    @close="handleDrawerClose"
  >
    <t-tabs v-model="activeTab" :default-value="1">
      <t-tab-panel :value="1" label="章节管理"> <chapter-list ref="chapterListRef" /></t-tab-panel>
    </t-tabs>
  </t-drawer>
</template>
<script setup lang="ts">
import { reactive, ref } from 'vue';

import ChapterList from './ChapterList.vue';

const visible = ref(false);
const activeTab = ref(1);
const chapterListRef = ref(null);
const courseIdRef = ref(0);
const state = reactive({
  title: '',
});

const handleDrawerClose = () => {
  activeTab.value = 1;
};

const show = async (item?: any) => {
  courseIdRef.value = item.id;
  visible.value = true;
  state.title = item.name;
  init(item);
};

const init = (item?: any) => {
  chapterListRef.value.setCourseId(item.id);
  chapterListRef.value.fetchData();
};

defineExpose({
  show,
});
</script>
