<template>
  <header class="login-header">
    <logo-full-icon class="logo" />
    <div class="operations-container">
      <!-- <t-button theme="default" shape="square" variant="text" @click="navToGitHub">
        <t-icon name="logo-github" class="icon" />
      </t-button>
      <t-button theme="default" shape="square" variant="text" @click="navToHelper">
        <t-icon name="help-circle" class="icon" />
      </t-button> -->

      <t-dropdown trigger="click">
        <t-button theme="default" shape="square" variant="text">
          <translate-icon />
        </t-button>
        <t-dropdown-menu>
          <t-dropdown-item
            v-for="(lang, index) in langList"
            :key="index"
            :value="lang.value"
            @click="(options) => changeLang(options.value as string)"
            >{{ lang.content }}</t-dropdown-item
          ></t-dropdown-menu
        >
      </t-dropdown>
      <t-button theme="default" shape="square" variant="text" @click="toggleSettingPanel">
        <t-icon name="setting" class="icon" />
      </t-button>
    </div>
  </header>
</template>

<script setup lang="ts">
import { TranslateIcon } from 'tdesign-icons-vue-next';

import LogoFullIcon from '@/assets/logo-full.svg?component';
import { langList } from '@/locales/index';
import { useLocale } from '@/locales/useLocale';
import { useSettingStore } from '@/store';

const settingStore = useSettingStore();
const toggleSettingPanel = () => {
  settingStore.updateConfig({
    showSettingPanel: true,
  });
};

// const navToGitHub = () => {
//   window.open('https://github.com/tencent/tdesign-vue-next-starter');
// };

// const navToHelper = () => {
//   window.open('http://tdesign.tencent.com/starter/docs/get-started');
// };

// 切换语言
const { changeLocale } = useLocale();
const changeLang = (lang: string) => {
  changeLocale(lang);
};
</script>

<style lang="less" scoped>
.login-header {
  padding: 0 var(--td-comp-paddingLR-xl);
  display: flex;
  justify-content: space-between;
  align-items: center;
  backdrop-filter: blur(10px);
  color: var(--td-text-color-primary);
  height: var(--td-comp-size-xxxl);

  .logo {
    width: 178px;
    height: var(--td-comp-size-s);
  }

  .operations-container {
    display: flex;
    align-items: center;

    .t-button {
      margin-left: var(--td-comp-margin-l);
    }
  }
}
</style>
