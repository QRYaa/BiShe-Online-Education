<template>
  <view class="tn-safe-area-inset-bottom">
    <!-- 顶部自定义导航 -->
    <tn-nav-bar fixed backTitle="" :bottomShadow="true" backgroundColor="#ffffff" :height="45">
      <view class="tn-flex tn-flex-col-center tn-flex-row-center">
        <text class="tn-text-md tn-color-black">我的课程</text>
      </view>
    </tn-nav-bar>
    <view class="list-content" :style="{ paddingTop: vuex_custom_bar_height + 'px' }">
      <view v-if="memberCourses.length > 0">
        <view class="mc-item" v-for="(item, index) in memberCourses" :key="index">
          <view class="mc-item-t" @click="openCourseDetail(item)">
            <image class="item-img" :src="imagePrefix + item.courseDto.horizontalImage" mode=""></image>
            <view class="item-msg tn-flex tn-flex-direction-column tn-flex-row-between tn-flex-col-top">
              <view>
                <view class="msg-name">
                  {{ item.courseDto.name }}
                </view>
                <!-- <view class="msg-remark">
                {{ item.courseDto.description }}
              </view> -->
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

          <view>
            <view class="tn-flex tn-flex-row-between tn-text-sm">
              <view>
                <text>共{{ item.courseDto.lessonNum }}节</text>
                <text class="tn-padding-left-xs">{{ bindFormatDurationCN(item.courseDto.lessonDuration) }}</text>
              </view>
              <view> 已学{{ item.completionPercentage?item.completionPercentage:0 }}% </view>
            </view>
            <view class="tn-padding-top-xs">
              <tn-line-progress :percent="item.completionPercentage" activeColor="#01BEFF"></tn-line-progress>
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
import { formatDurationCN } from '@/util/date.js';

export default {
  data() {
    return {
      imagePrefix: process.env.config[process.env.NAME].imagePrefix,
      memberCourses: [],
      queryForm: {
        pageNum: 1,
        pageSize: 10,
      },
      totalPages: 0,
      loadMoreText: '加载中...',
    };
  },
  methods: {
    bindFormatDurationCN(duration) {
      return formatDurationCN(duration);
    },
    async fetchMCData() {
      let { content, totalPages } = await this.$tn.http.get('/fe/mp/member/memberCourse/page', {
        params: this.queryForm,
      });
      content.forEach((element) => {
        this.calLessonData(element);
      });
      this.memberCourses = this.memberCourses.concat(content);
      this.totalPages = totalPages;
      this.queryForm.pageNum++;
      if (this.totalPages < this.queryForm.pageNum) {
        this.loadMoreText = '没有更多数据了!';
      }
    },

    calLessonData(mc) {
      let course = mc.courseDto;
      let lessonNum = 0;
      let lessonDuration = 0;
      if (course.chapterDtoList) {
        for (let i = 0; i < course.chapterDtoList.length; i++) {
          if (course.chapterDtoList[i].lessonDtoList) {
            for (let j = 0; j < course.chapterDtoList[i].lessonDtoList.length; j++) {
              lessonNum++;
              if (course.chapterDtoList[i].lessonDtoList[j].duration) {
                lessonDuration += course.chapterDtoList[i].lessonDtoList[j].duration;
              }
            }
          }
        }
      }
      course.lessonNum = lessonNum;
      course.lessonDuration = lessonDuration;
    },

    openCourseDetail(item) {
      uni.navigateTo({ url: '/pages/common/courseLoading?code=' + item.courseDto.code });
    },
  },

  async onShow() {
    await this.fetchMCData();
  },

  async onReachBottom() {
    console.log('到达底部，onReachBottom');
    if (this.totalPages < this.queryForm.pageNum) {
      return;
    }
    await this.fetchMCData();
  },
};
</script>
<style lang="scss">
.list-content {
  width: 100%;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  padding: 0 24rpx;

  background-color: #f8f7f8;
}

.mc-item {
  width: 100%;
  box-sizing: border-box;
  padding: 20rpx;
  background-color: #ffffff;
  border-radius: 16rpx;
  margin-top: 20rpx;
  box-shadow: 0rpx 4rpx 32rpx 0rpx rgba(69, 129, 219, 0.1);
  display: flex;
  flex-direction: column;
  justify-content: space-between;

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
