<template>
  <view class="tn-safe-area-inset-bottom">
    <tn-nav-bar backTitle=" " backgroundColor="#ffffff">收藏课程</tn-nav-bar>
    <view class="list-content">
      <view v-if="collectCourseList.length > 0" style="width: 100%;" :style="{ paddingTop: vuex_custom_bar_height + 'px' }">
        <view class="mc-item" v-for="(item, index) in collectCourseList" :key="index">
          <view class="mc-item-t" @click="openCourseDetail(item)">
            <image class="item-img" :src="imagePrefix + item.courseDto.horizontalImage" mode=""></image>
            <view class="item-msg tn-flex tn-flex-direction-column tn-flex-row-between tn-flex-col-top">
              <view>
                <view class="msg-name">
                  {{ item.courseDto.name }}
                </view>
                <view class="tn-flex tn-flex-row-left tn-padding-top-xs" style="width: 100%">
                  <tn-tag
                    v-if="item.courseDto.price"
                    backgroundColor="#e5f3fe"
                    fontColor="#1da2f1"
                    :fontSize="24"
                    width="75rpx"
                    height="40rpx"
                    >付费</tn-tag
                  >
                  <tn-tag
                    v-else
                    backgroundColor="#e5f3fe"
                    fontColor="#1da2f1"
                    :fontSize="24"
                    width="75rpx"
                    height="40rpx"
                    >免费</tn-tag
                  >
                </view>
              </view>

              <view class="tn-flex tn-flex-col-center">
                <text class="tn-icon-my-circle-fill tn-text-xl tn-color-blue" />
                <text class="tn-padding-left-xs tn-text-sm">{{ item.courseDto.teacherName }}</text>
              </view>
            </view>
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
      },
      collectCourseList: [],
      loadMoreText: '加载中...',
      totalPages: 0,
    };
  },
  methods: {
    async fetchCollectCourseData() {
      let { content, totalPages } = await this.$tn.http.get('/fe/mp/member/memberCollectCourse/page', {
        params: this.queryForm,
      });
      this.collectCourseList = this.collectCourseList.concat(content);
      this.totalPages = totalPages;
      this.queryForm.pageNum++;
      if (this.totalPages < this.queryForm.pageNum) {
        this.loadMoreText = '没有更多数据了!';
      }
    },
    openCourseDetail(item) {
      uni.navigateTo({ url: '/pages/common/courseLoading?code=' + item.courseDto.code });
    },
  },
  async onShow() {
    await this.fetchCollectCourseData();
  },
  onUnload() {
    (this.collectCourseList = []), (this.loadMoreText = '加载中...');
  },
  async onReachBottom() {
    console.log('到达底部，onReachBottom');
    if (this.totalPages < this.queryForm.pageNum) {
      //this.loadMoreText = '没有更多数据了!';
      return;
    }
    await this.fetchCollectCourseData();
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

  .mc-item-t {
    width: 100%;
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: start;
    box-sizing: border-box;
    padding-bottom: 15rpx;

    .item-img {
      width: 355rpx;
      height: 200rpx;
      border-radius: 10rpx;
    }

    .item-msg {
      width: calc(100% - 284rpx);
      height: 200rpx;
      box-sizing: border-box;
      padding: 10rpx 0 10rpx 15rpx;
      .msg-name {
        width: 100%;
        font-size: 30rpx;
        color: #1d2129;
        font-weight: 500;
      }
      .msg-remark {
        width: 100%;
        font-size: 24rpx;
        color: #4e5969;
        margin-top: 16rpx;
        overflow: hidden;
        /* 隐藏溢出的文本 */
        white-space: nowrap;
        /* 防止文本换行 */
        text-overflow: ellipsis;
        /* 溢出部分用省略号表示 */
      }
    }
  }

  .order-status {
    width: 100%;
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 30rpx;
    font-size: 28rpx;
    color: #1d2129;
    .date {
      font-size: 20rpx;
      color: #1d2129;
    }
    .status {
      font-size: 24rpx;
    }
  }

  .statusbox {
    box-sizing: border-box;
    padding: 8rpx 20rpx;
    text-align: center;
    border-radius: 32rpx;
    overflow: hidden;
    font-size: 23rpx;
  }

  .status1 {
    background-color: #d7fae0;
    color: #007d3a;
  }
}
</style>
