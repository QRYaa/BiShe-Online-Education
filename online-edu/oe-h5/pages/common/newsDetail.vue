<template>
  <view v-if="model.title">
    <!-- 顶部自定义导航 -->
    <!-- <tn-nav-bar fixed alpha customBack>
      <view slot="back" class="tn-custom-nav-bar__back" @click="goBack">
        <text class="icon tn-icon-left"></text>
        <text class="icon tn-icon-home-capsule-fill"></text>
      </view>
    </tn-nav-bar> -->
    <tn-nav-bar backTitle=" " backgroundColor="#F8F8F8">文章详情</tn-nav-bar>
    <view class="container" :style="{ paddingTop: vuex_custom_bar_height + 'px' }">
      <view class="tn-flex tn-flex-row-center tn-padding-top-xl" style="width: 100%">
        <view class="tn-text-bold tn-text-xl">{{ model.title }}</view>
      </view>
      <view class="tn-flex tn-flex-row-center tn-flex-col-center" style="height: 80rpx; border-bottom: 1px solid #ddd">
        <view class="tn-text-sm tn-color-gray--dark tn-padding-right-lg">{{ model.publishedTime }}</view>
        <view class="tn-text-sm tn-color-gray--dark">浏览量：{{ model.viewCount }}</view>
      </view>
      <view class="tn-padding-top">
        <mp-html v-show="model.content" class="rich-text-content" :content="model.content" :selectable="false" />
      </view>
    </view>
  </view>
</template>

<script>
import template_page_mixin from '@/libs/mixin/template_page_mixin.js';
export default {
  mixins: [template_page_mixin],
  data() {
    return {
      model: {
        content: '',
      },
    };
  },
  async onLoad(options) {
    if (options.code) {
      this.model = await this.$tn.http.get('/fe/mp/newsDetail', { params: { code: options.code } });
    }
  },
  methods: {},
};
</script>

<style lang="scss" scoped>
.container {
  padding: 0 30rpx;
  background-color: #ffffff;
}
.rich-text-content {
  //line-height: 1.6em;
  width: 100%;
}
</style>
