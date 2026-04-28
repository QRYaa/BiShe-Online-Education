<template>
  <t-drawer v-model:visible="visible" :header="state.title" :close-btn="true" size="800px" :footer="false">
    <t-tabs :default-value="1">
      <t-tab-panel :destroy-on-hide="false" :value="1" label="笔记详情"> <note-form ref="noteFormRef" /></t-tab-panel>
      <t-tab-panel :destroy-on-hide="false" :value="2" label="回复列表"> <reply-list ref="replyListRef" /></t-tab-panel>
    </t-tabs>
  </t-drawer>
</template>
<script setup lang="ts">
import { reactive, ref } from 'vue';

import NoteForm from './NoteForm.vue';
import ReplyList from './ReplyList.vue';

const visible = ref(false);
const noteFormRef = ref(null);
const replyListRef = ref(null);
const noteIdRef = ref(0);
const state = reactive({
  title: '笔记详情',
});

const show = async (item?: any) => {
  noteIdRef.value = item.id;
  visible.value = true;
  await init(item);
};

const init = async (item?: any) => {
  await noteFormRef.value.init(item.id);
  await replyListRef.value.setNoteId(item.id);
};

defineExpose({
  show,
});
</script>
