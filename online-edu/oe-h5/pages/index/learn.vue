<template>
  <view class="list-content">
    <tn-nav-bar backTitle=" " backgroundColor="#ffffff" :isBack="false">广州投资顾问学院</tn-nav-bar>
    <view style="padding: 0 24rpx;">
      <view
        style="border-radius: 16rpx; width: 100%; padding: 40rpx 0; background-color: #ffffff;"
        class="tn-flex tn-flex-col-center tn-flex-row-around"
        :style="{ marginTop: vuex_custom_bar_height + 10 + 'px' }"
      >
        <view class="tn-flex tn-flex-direction-column tn-flex-col-center" @click="handleClickLike()">
          <tn-button shape="icon" margin="10rpx 10rpx">
            <text class="tn-icon-like" style="font-size: 80rpx; color: #5e6367"></text>
          </tn-button>
          <view style="color: #5e6367">收藏</view>
        </view>
        <view class="tn-flex tn-flex-direction-column tn-flex-col-center" @click="handleClickNote()">
          <tn-button shape="icon" margin="10rpx 10rpx">
            <text class="tn-icon-write" style="font-size: 80rpx; color: #5e6367"></text>
          </tn-button>
          <view style="color: #5e6367">笔记</view>
        </view>
        <view class="tn-flex tn-flex-direction-column tn-flex-col-center" @click="handleClickStatistics()">
          <tn-button shape="icon" margin="10rpx 10rpx">
            <text class="tn-icon-statistics" style="font-size: 80rpx; color: #5e6367"></text>
          </tn-button>
          <view style="color: #5e6367">学习报告</view>
        </view>
      </view>
      <view style="width: 100%; background-color: #ffffff; margin-top: 30rpx; padding: 30rpx">
        <view class="tn-text-bold">我的课程</view>
        <view v-if="memberCourses.length > 0" style="width: 100%">
          <view class="mc-item" v-for="(item, index) in memberCourses" :key="index">
            <view class="mc-item-t" @click="openCourseDetail(item)">
              <image
                v-if="item.courseDto && item.courseDto.horizontalImage"
                class="item-img"
                :src="imagePrefix + item.courseDto.horizontalImage"
                mode=""
              ></image>
              <image v-else class="item-img" src="/static/orderItemheng.png" mode=""></image>
              <view class="item-msg tn-flex tn-flex-direction-column tn-flex-row-between tn-flex-col-top">
                <view>
                  <view class="msg-name">
                    {{ item.courseDto ? item.courseDto.name : '课程已删除' }}
                  </view>
                  <!-- <view class="msg-remark">
                  {{ item.courseDto.description }}
                </view> -->
                  <view class="tn-flex tn-flex-row-left tn-padding-top-xs" style="width: 100%">
                    <tn-tag
                      v-if="item.courseDto && item.courseDto.price"
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
                  <text class="tn-padding-left-xs tn-text-sm">{{
                    item.courseDto ? item.courseDto.teacherName : '课程已删除'
                  }}</text>
                </view>
              </view>
            </view>

            <view>
              <view class="tn-flex tn-flex-row-between tn-text-sm">
                <view>
                  <text>共{{ item.courseDto ? item.courseDto.lessonNum : 0 }}节</text>
                  <text class="tn-padding-left-xs">{{
                    item.courseDto ? bindFormatDurationCN(item.courseDto.lessonDuration) : 0
                  }}</text>
                </view>
                <view> 已学{{ item.completionPercentage ? item.completionPercentage : 0 }}% </view>
              </view>
              <view class="tn-padding-top-xs">
                <tn-line-progress :percent="item.completionPercentage" activeColor="#01BEFF"></tn-line-progress>
              </view>
            </view>
            <!-- 添加遮罩层 -->
            <view v-if="!item.courseDto || !item.courseDto.published" class="mask">
              <!-- 不可用标签 -->
              <view class="unavailable-badge">
                <text>不可用</text>
              </view>
            </view>
          </view>
        </view>
        <view v-else style="padding: 400rpx 0"><tn-empty icon="/static/cartEmpty.jpg" text="空空如也"></tn-empty></view>
      </view>
      <view
        v-if="memberCourses.length > 0"
        style="height: 80rpx; line-height: 80rpx; text-align: center; padding-bottom: 30rpx"
        >{{ loadMoreText }}</view
      >
      <tn-toast ref="toast"></tn-toast>
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
      if (!course) {
        return;
      }
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
      if (!item.courseDto || !item.courseDto.published) {
        return;
      }
      uni.navigateTo({ url: '/pages/common/courseLoading?code=' + item.courseDto.code });
    },

    handleClickLike() {
      uni.navigateTo({ url: '/pages/learn/collectCourse' });
    },

    handleClickNote() {
      uni.navigateTo({ url: '/pages/learn/myNote' });
    },

    handleClickStatistics() {
      this.$refs.toast.show({
        content: '功能开发中',
        icon: 'tip',
        image: '',
        duration: 1500,
      });
    },
  },

  beforeMount() {
    uni.$on('onReachBottom', async () => {
      // 监听上拉加载
      console.log('到达底部，onReachBottom');
      if (this.totalPages < this.queryForm.pageNum) {
        //this.loadMoreText = '没有更多数据了!';
        return;
      }
      await this.fetchMCData();
    });
  },

  async mounted() {
    await this.fetchMCData();
  },
};
</script>
<style lang="scss">
.list-content {
  width: 100%;
  min-height: 100vh;
  background-color: #f8f7f8;
  padding-top: 1rpx;
}

.mc-item {
  width: 100%;
  background-color: #ffffff;
  margin-top: 30rpx;
  position: relative;
  overflow: hidden;

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
.mask {
  position: absolute;
  inset: 0;
  background-color: rgba(0, 0, 0, 0.3);
  z-index: 10;
  border-radius: 16rpx;
}
.unavailable-badge {
  position: absolute;
  top: 32rpx;
  left: -62rpx;
  transform: rotate(-30deg);

  background: rgba(156, 163, 175, 0.9);
  width: 308rpx;

  padding: 8rpx 62rpx;
  text-align: center;
  font-weight: 600;
  color: white;
  letter-spacing: 4rpx;
}
</style>
