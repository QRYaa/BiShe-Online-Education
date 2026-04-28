<template>
  <view>
    <tn-swiper
      style=""
      :height="543"
      :list="bannerList"
      :effect3d="true"
      :effect3dPreviousSpacing="0"
      :interval="8000"
      mode="scaleToFill"
      @click="openBannerUrl"
    ></tn-swiper>
    <view class="box-container">
      <view class="authenCard tn-main-gradient-blue--light" @click="openAuthenNews">
        <view class="tn-flex tn-flex-row-between tn-flex-col-bottom">
        <view class="cardText">
          <view class="tn-text-bold tn-text-lg">认证介绍&nbsp;&nbsp;></view>
          <view class="tn-text-sm tn-color-gray--dark tn-padding-top-xs">了解认证流程</view>
        </view>
        <view class="tn-icon-book" style="font-size: 80rpx;"></view>
      </view>
      </view>
      <view class="messageCard tn-main-gradient-blue--light--reverse" @click="openMessageNews">
        <view class="tn-flex tn-flex-row-between tn-flex-col-bottom">
        <view class="cardText">
          <view class="tn-text-bold tn-text-lg">考证资讯&nbsp;&nbsp;></view>
          <view class="tn-text-sm tn-color-gray--dark tn-padding-top-xs">最新考证信息</view>
        </view>
        <view class="tn-icon-read" style="font-size: 80rpx;"></view>
        </view>
      </view>
    </view>
    <view class="tn-text-bold tn-text-lg" style="margin: 30rpx 30rpx 0rpx 30rpx">先导课</view>
    <view class="tn-strip-bottom tn-padding">
      <image :src="imagePrefix + preliminaryCourse.horizontalImage" style="width: 100%;height: 386rpx;" @click="openCourseDetail(preliminaryCourse)" />
      <view class="tn-padding-top-sm tn-text-lg">{{ preliminaryCourse.name }}</view>
      <view class="tn-padding-top-xs tn-text-sm tn-color-gray--dark">{{ preliminaryCourse.description }}</view>
    </view>
    <view class="tn-text-bold tn-text-lg" style="margin: 30rpx 30rpx 0rpx 30rpx">精品推荐</view>
    <view class="box-container" v-for="(item, index) in courseList" :key="index" @click="openCourseDetail(item)">
      <image
        :src="imagePrefix + item.horizontalImage"
        style="width: 310rpx; height: 185rpx; border-radius: 16rpx"
      ></image>
      <view class="courseBox">
        <view>
          <view style="font-size: 30rpx;">{{ item.name }}</view>
          <view class="tn-text-xs tn-color-gray--dark tn-text-ellipsis tn-padding-top-xs">{{ item.description }}</view>
        </view>
        <view class="tn-flex tn-flex-row-between tn-flex-col-center tn-padding-top-xs">
          <view class="tn-color-blue">
            <text v-if="item.price" class="tn-text-lg">¥</text>
            <text class="tn-text-xl tn-text-bold">{{
              item.price ? item.price.toFixed(2) : '免费'
            }}</text>
          </view>
          <view class="tn-color-grey tn-text-sm">{{ item.vlearningCount }}人已学习</view>
        </view>
        <!-- <view class="tn-text-sm tn-color-gray--dark tn-padding-top-xs">{{ item.lessonNum }}节&nbsp;{{ formatDuration(item.lessonDuration) }}</view> -->
      </view>
    </view>
    <view style="height: 80rpx; line-height: 80rpx; text-align: center; padding-bottom: 30rpx">{{ loadMoreText }}</view>
  </view>
</template>

<script>
export default {
  name: 'Home',
  data() {
    return {
      bannerList: [],
      courseList: [],
      authenNewsCode: 'CertificationIntroduction',
      messageNewsCode: 'ExamInformation',
      imagePrefix: process.env.config[process.env.NAME].imagePrefix,
      preliminaryCourseCode: 'preliminary',
      preliminaryCourse: {},
      loadMoreText: '加载中...',
      queryForm: {
        pageNum: 1,
        pageSize: 10,
        courseTypeCode: 'homePageRecommend',
      },
      totalPages: 0,
    };
  },
  methods: {
    async fetchData() {
      try {
        let bannerDatas = await this.$tn.http.get('/fe/mp/bannerList');
        this.bannerList = bannerDatas.map((item) => ({
          image: process.env.config[process.env.NAME].imagePrefix + item.image,
          title: item.name,
          url: item.url,
        }));
        this.preliminaryCourse=await this.$tn.http.get('/fe/mp/courseDetail', { params: { code: this.preliminaryCourseCode } });
        await this.fetchCourseData();
      } catch (e) {
        console.log(e);
      }
    },
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
    // calLessonData(course) {
    //   let lessonNum = 0;
    //   let lessonDuration = 0;
    //   if (course.chapterDtoList) {
    //     for (let i = 0; i < course.chapterDtoList.length; i++) {
    //       if (course.chapterDtoList[i].lessonDtoList) {
    //         for (let j = 0; j < course.chapterDtoList[i].lessonDtoList.length; j++) {
    //           lessonNum++;
    //           if (course.chapterDtoList[i].lessonDtoList[j].duration) {
    //             lessonDuration += course.chapterDtoList[i].lessonDtoList[j].duration;
    //           }
    //         }
    //       }
    //     }
    //   }
    //   course.lessonNum = lessonNum;
    //   course.lessonDuration = lessonDuration;
    // },
    formatDuration(duration) {
      let hours = Math.floor(duration / 3600);
      let remainingSeconds = duration % 3600;
      let minutes = Math.floor(remainingSeconds / 60);
      let seconds = remainingSeconds % 60;

      if (hours > 0) {
        return `${String(hours).padStart(2, '0')}:${String(minutes).padStart(2, '0')}:${String(seconds).padStart(
          2,
          '0',
        )}`;
      }
      return `${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}`;
    },
    openBannerUrl(index) {
      let url = this.bannerList[index].url;
      if (url) {
        location.href = url;
      }
    },
    openAuthenNews() {
      uni.navigateTo({ url: '/pages/common/newsDetail?code=' + this.authenNewsCode });
    },
    openMessageNews() {
      uni.navigateTo({ url: '/pages/common/newsDetail?code=' + this.messageNewsCode });
    },
    openCourseDetail(item) {
      uni.navigateTo({ url: '/pages/common/courseLoading?code=' + item.code });
    },
  },
  async mounted() {
    await this.fetchData();
  },
  onUnload() {
    (this.courseList = []), (this.loadMoreText = '加载中...');
  },

  beforeMount() {
    uni.$on('onReachBottom', async () => {
      // 监听上拉加载
      console.log('到达底部，onReachBottom');
      if (this.totalPages < this.queryForm.pageNum) {
        //this.loadMoreText = '没有更多数据了!';
        return;
      }
      await this.fetchCourseData();
    });
  },
  destroyed() {
    uni.$off('onReachBottom'); // 销毁onReachBottom监听
  },
};
</script>

<style lang="scss" scoped>
.box-container {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  padding: 30rpx 30rpx 0rpx 30rpx;
}
.authenCard,
.messageCard {
  height: 140rpx;
  width: calc(50% - 30rpx);
  padding: 30rpx;
  border-radius: 20rpx;
}
.cardText{
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: flex-start;
}

.tn-strip-bottom {
  width: 100%;
  border-bottom: 20rpx solid rgba(241, 241, 241, 0.8);
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
