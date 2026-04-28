<template>
  <view class="tn-safe-area-inset-bottom" style="background-color: #ffffff;">
    <tn-nav-bar backTitle=" " backgroundColor="#ffffff">讲师详情</tn-nav-bar>
    <view v-if="model.code" class="content-box" :style="{ paddingTop: vuex_custom_bar_height + 10 + 'px' }">
      <view class="tn-bg-white tn-padding-bottom-sm tn-padding-top">
        <view class="avatar-name tn-flex tn-flex-direction-column tn-flex-col-center">
          <tn-avatar :src="imagePrefix + model.avatar" size="150rpx"></tn-avatar>
          <view class="tn-padding-top-sm tn-text-lg">{{ model.name }}</view>
        </view>
        <view class="tn-flex tn-flex-wrap tn-padding-top tn-padding-left" style="width: calc(100% + 20rpx)">
          <view v-for="(item, index) in model.tagDtoList" :key="index" class="tn-padding-right-xl tn-padding-bottom">
            <tn-tag backgroundColor="#e8f2fd" fontColor="#0E7CEA" width="max-content">{{ item.name }}</tn-tag>
          </view>
        </view>
      </view>

      <view class="tn-bg-white tn-padding tn-margin-top-sm">
        <view class="tn-flex">
          <view class="tn-icon-education tn-text-xl" />
          <view class="tn-padding-left-sm tn-text-bold">关于讲师</view>
        </view>
        <view class="tn-padding-top-sm">
          <mp-html v-show="model.content" :content="model.content" :selectable="false" />
        </view>
      </view>

      <view class="tn-bg-white tn-padding tn-margin-top-sm">
        <view class="tn-flex">
          <view class="tn-icon-video-fill tn-text-xl" />
          <view class="tn-padding-left-sm tn-text-bold">讲授课程</view>
        </view>
        <view class="box-container" v-for="(item, index) in courseList" :key="index" @click="openCourseDetail(item)">
          <image
            :src="imagePrefix + item.horizontalImage"
            style="width: 310rpx; height: 185rpx; border-radius: 16rpx"
          ></image>
          <view class="courseBox">
            <view>
              <view style="font-size: 30rpx">{{ item.name }}</view>
              <view class="tn-text-xs tn-color-gray--dark tn-text-ellipsis tn-padding-top-xs">{{
                item.description
              }}</view>
            </view>
            <view class="tn-flex tn-flex-row-between tn-flex-col-center tn-padding-top-xs">
              <view class="tn-color-blue">
                <text v-if="item.price" class="tn-text-lg">¥</text>
                <text class="tn-text-xl tn-text-bold">{{ item.price ? item.price.toFixed(2) : '免费' }}</text>
              </view>
              <view class="tn-color-grey tn-text-sm">{{ item.vlearningCount }}人已学习</view>
            </view>
          </view>
        </view>
      </view>
      <view style="height: 80rpx; line-height: 80rpx; text-align: center; padding-bottom: 30rpx">{{
        loadMoreText
      }}</view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      imagePrefix: process.env.config[process.env.NAME].imagePrefix,
      model: {},
      courseList: [],
      queryForm: {
        pageNum: 1,
        pageSize: 10,
        teacherCode: '',
      },
      loadMoreText: '加载中...',
      totalPages: 0,
    };
  },
  async onLoad(options) {
    if (options.code) {
      this.queryForm.teacherCode = options.code;
      this.model = await this.$tn.http.get('/fe/mp/teacherDetail', { params: { code: options.code } });
      await this.fetchCourseData();
    }
  },
  onUnload() {
    (this.courseList = []), (this.loadMoreText = '加载中...');
  },
  async onReachBottom() {
    console.log('到达底部，onReachBottom');
    if (this.totalPages < this.queryForm.pageNum) {
      //this.loadMoreText = '没有更多数据了!';
      return;
    }
    await this.fetchCourseData();
  },
  methods: {
    async fetchCourseData() {
      let { content, totalPages } = await this.$tn.http.get('/fe/mp/coursePage', {
        params: this.queryForm,
      });
      // content.forEach((element) => {
      //   this.calLessonData(element);
      // });
      this.courseList = this.courseList.concat(content);
      this.totalPages = totalPages;
      this.queryForm.pageNum++;
      if (this.totalPages < this.queryForm.pageNum) {
        this.loadMoreText = '没有更多数据了!';
      }
    },
    openCourseDetail(item) {
      uni.navigateTo({ url: '/pages/common/courseLoading?code=' + item.code });
    },
  },
};
</script>

<style lang="scss" scoped>
.content-box {
  width: 100%;
  min-height: 100vh;
  padding: 0 32rpx;
  margin-top: 40rpx;
  background-color: #f8f7f8;
}
.avatar-name {
  width: 100%;
  margin-top: -80rpx;
}
.box-container {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  padding: 30rpx 0;
}
.courseBox {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  width: calc(100% - 310rpx);
  height: 185rpx;
  padding: 10rpx 0 8rpx 15rpx;
}
</style>
