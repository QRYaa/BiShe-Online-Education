<template>
  <view>
    <tn-loading show="show" color="#01BEFF"></tn-loading>
  </view>
</template>
<script>
export default {
  data() {
    return {};
  },
  async onLoad(options) {
    let b = 'false';
    if (uni.getStorageSync(process.env.config.authToken)) {
      if (options.code) {
        let res = await this.$tn.http.get('/fe/mp/member/memberCourse/existsOrNot', { params: { courseCode: options.code } });
        b=res;
      } else if (options.id) {
        let res = await this.$tn.http.get('/fe/mp/member/memberCourse/existsOrNot', { params: { courseId: options.id } });
        b=res;
      }
    }
    if (options.code && b == 'false') {
      uni.redirectTo({ url: '/pages/common/courseDetail?code=' + options.code });
    } else if (options.id && b == 'false') {
      uni.redirectTo({ url: '/pages/common/courseDetail?id=' + options.id });
    } else if (options.code && b == 'true') {
      uni.redirectTo({ url: '/pages/learnCourse/index?code=' + options.code });
    } else if (options.id && b == 'true') {
      uni.redirectTo({ url: '/pages/learnCourse/index?id=' + options.id });
    }
  },
  methods: {},
};
</script>
<style lang="scss" scoped></style>
