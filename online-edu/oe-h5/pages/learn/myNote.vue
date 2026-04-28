<template>
  <view class="tn-safe-area-inset-bottom">
    <tn-nav-bar backTitle=" " backgroundColor="#ffffff">我的笔记</tn-nav-bar>
    <view class="list-content" :style="{ paddingTop: vuex_custom_bar_height + 'px' }">
      <view v-if="list.length > 0" style="width: 100%">
        <view class="mc-item" v-for="(item, index) in list" :key="index" @click="handleClickItem(item)">
          <view class="tn-flex tn-flex-col-center tn-flex-row-between">
            <view>
              <view class="tn-flex tn-flex-col-bottom">
                <text style="font-size: 60rpx; font-weight: bold">{{ item.noteNum }}</text>
                <text style="font-size: 35rpx; padding-left: 10rpx; padding-bottom: 10rpx">条笔记</text>
              </view>
              <view class="tn-text-ellipsis tn-padding-top">{{ item.courseDto.name }}</view>
            </view>
            <image
              v-if="item.courseDto.squareImage"
              class="item-img"
              :src="imagePrefix + item.courseDto.squareImage"
              mode=""
            ></image>
            <image v-else class="item-img" mode="" src="/static/orderItem.png"></image>
          </view>
        </view>
        <view style="height: 80rpx; line-height: 80rpx; text-align: center; padding-bottom: 30rpx">{{
          loadMoreText
        }}</view>
      </view>
      <view v-else style="padding-top: 50rpx"><tn-empty mode="data"></tn-empty></view>
    </view>
  </view>
</template>
<script>
export default {
  data() {
    return {
      imagePrefix: process.env.config[process.env.NAME].imagePrefix,
      queryForm: {
        pageNum: 1,
        pageSize: 10,
        noteNum: 0,
      },
      list: [],
      loadMoreText: '加载中...',
      totalPages: 0,
    };
  },
  methods: {
    async fetchList() {
      let { content, totalPages } = await this.$tn.http.get('/fe/mp/member/memberCourse/page', {
        params: this.queryForm,
      });
      this.list = this.list.concat(content);
      this.totalPages = totalPages;
      this.queryForm.pageNum++;
      if (this.totalPages < this.queryForm.pageNum) {
        this.loadMoreText = '没有更多数据了!';
      }
    },

    handleClickItem(item) {
        uni.navigateTo({ url: '/pages/learn/myNoteList?courseId='+item.courseDto.id });
    },
  },
  async onShow() {
    await this.fetchList();
  },
  onUnload() {
    (this.list = []), (this.loadMoreText = '加载中...');
  },
  async onReachBottom() {
    console.log('到达底部，onReachBottom');
    if (this.totalPages < this.queryForm.pageNum) {
      return;
    }
    await this.fetchList();
  },
};
</script>
<style lang="scss" scoped>
.list-content {
  width: 100%;
  min-height: 100vh;
  padding: 1rpx 24rpx 0;

  background-color: #f8f7f8;
}
.mc-item {
  width: 100%;
  padding: 20rpx;
  background-color: #ffffff;
  border-radius: 16rpx;
  margin-top: 20rpx;
  box-shadow: 0rpx 4rpx 32rpx 0rpx rgba(69, 129, 219, 0.1);
}
.item-img {
  width: 200rpx;
  height: 200rpx;
  border-radius: 10rpx;
}
</style>
