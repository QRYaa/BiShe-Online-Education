<template>SSO LOGIN</template>

<script setup lang="ts">
import { onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';

// import { useRouter } from 'vue-router';
import { useUserStore } from '@/store';

const userStore = useUserStore();

const router = useRouter();
const route = useRoute();

onMounted(() => {
  if (userStore.token) {
    router.push({ path: '/' });
  } else {
    const ticket = route.query.ticket as string;
    console.log(ticket);
    if (ticket) {
      // do loginByTicket
    } else {
      // redirect to sso login
    }
    // const data = await baseMgt.loginByTicket(ticket);
    // console.log(data);
    // userStore.login(data);
    // router.push({ path: '/' });
  }
});
</script>

<style lang="less" scoped>
@import '../index.less';
</style>
