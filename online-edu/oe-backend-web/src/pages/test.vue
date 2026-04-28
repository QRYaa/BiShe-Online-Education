<template>
  <div>
    <div>
      <ckeditor v-if="editor && config" :model-value="config.initialData" :editor="editor" :config="config" />
    </div>
    <div>
      <ckeditor v-if="editor && config" :model-value="config.initialData" :editor="editor" :config="config" />
    </div>
  </div>
</template>
<script setup lang="ts">
import { Ckeditor } from '@ckeditor/ckeditor5-vue';
import { inject } from 'vue';

const config = inject('ckeditorConfig');
const editor = inject('ckeditorEditor');
</script>
