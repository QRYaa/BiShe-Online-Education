<template>
  <view class="container">
        <!-- 顶部自定义导航 -->
    <tn-nav-bar fixed alpha customBack>
      <view slot="back" class="tn-custom-nav-bar__back" @click="goBack">
        <text class="icon tn-icon-left"></text>
        <text class="icon tn-icon-home-capsule-fill"></text>
      </view>
    </tn-nav-bar>
    <view class="image-wrapper">
      <image v-if="!showLessonVideo" :src="imagePrefix + model.horizontalImage" class="image-content"></image>
      <video v-else class="image-content" :src="lessonVideo" :direction="0" :autoplay="true"></video>
    </view>
    <view class="tn-strip-bottom tn-padding">
      <view class="tn-text-bold tn-text-xl">{{ model.name }}</view>
      <view class="tn-color-grey tn-padding-top-xs tn-text-sm">{{ model.description }}</view>
      <view class="between-box tn-padding-top-sm">
        <view class="tn-color-blue">
          <text v-if="model.price" class="tn-text-lg">¥</text>
          <text class="tn-text-xl tn-text-bold">{{
            model.price ? model.price.toFixed(2) : '免费'
          }}</text>
        </view>
        <view class="tn-color-grey tn-text-sm">{{ model.vlearningCount }}人已学习</view>
      </view>
    </view>
    <view class="sticky-tab">
      <tn-tabs
        :list="fixedList"
        :current="current"
        :isScroll="false"
        inactiveColor="#838383"
        activeColor="#3D7EFF"
        :bold="true"
        backgroundColor="#ffffff"
        :fontSize="32"
        :badgeOffset="[20, 50]"
        @change="tabChange"
      ></tn-tabs>
    </view>
    <view v-if="current === 0">
      <view v-if="teacher.avatar" class="tn-padding-lg tn-flex tn-flex-row-between tn-flex-col-center tn-strip-bottom-min tn-strip-top-min"  @click="handleClickTeacher()">
        <view class="tn-flex tn-flex-col-center">
          <tn-avatar :src="imagePrefix + teacher.avatar" size="110rpx"></tn-avatar>
          <view class="tn-padding-left">
          <view class="" style="font-size: 30rpx;">{{ teacher.name }}</view>
          <view class="tn-padding-top-xs tn-text-sm tn-color-gray--dark">{{ teacher.description }}</view>
          </view>
        </view>
        <view class="tn-icon-right" />
      </view>
      <mp-html v-show="model.content" class="rich-text-content" :content="model.content" :selectable="false" />
      <view v-if="!model.content" style="padding-top: 50rpx">
        <tn-empty mode="data"></tn-empty>
      </view>
    </view>
    <view v-else-if="current === 1 && model.chapterDtoList">
      <view v-if="!model.chapterDtoList.length" style="padding-top: 50rpx"><tn-empty mode="data"></tn-empty></view>
      <tn-collapse :accordion="false" :headStyle="{ backgroundColor: '#F8F7F8' }">
        <tn-collapse-item
          v-for="(item, index) in model.chapterDtoList"
          :key="index"
          :title="formChapterTitle(item, index)"
          :open="true"
        >
          <tn-list-view>
            <tn-list-cell v-for="(item2, index2) in item.lessonDtoList" :key="index2" :unlined="false">
              <view class="message" @click="handleLessonClick(item2)">
                <view class="message__left">
                  <text class="tn-icon-video tn-text-xl tn-color-gray"></text>
                </view>
                <view class="message__middle">
                  <view class="message__name">{{ item2.name }}</view>
                </view>
                <view class="message__right">
                  <view class="tn-flex tn-flex-row-between tn-flex-col-center">
                    <view class="message__time">{{ formatDuration(item2.duration) }}</view>
                    <tn-tag
                      v-if="item2.previewAble"
                      backgroundColor="tn-bg-blue"
                      fontColor="tn-color-white"
                      shape="circle"
                      height="28rpx"
                      width="50rpx"
                      :fontSize="14"
                      >试看</tn-tag
                    >
                  </view>
                </view>
              </view>
            </tn-list-cell>
          </tn-list-view>
        </tn-collapse-item>
      </tn-collapse>
    </view>
    <!-- 添加占位元素 -->
    <view class="tabbar-placeholder"></view>
    <view class="tabbar footerfixed tn-bg-white">
      <!-- flex-row:justify-content,flex-col:align-items -->
      <view class="tn-flex tn-flex-row-between tn-flex-col-center tn-padding-top">
        <view class="tn-padding-left-xl">
          <view
            v-if="collect == 'true'"
            class="tn-icon-like-fill tn-text-xxl"
            style="color: #ff5a6b"
            @click="cancelCollect"
          ></view>
          <view v-else class="tn-icon-like tn-text-xxl" @click="collectCourse"></view>
          <view class="tn-text-sm">收藏</view>
        </view>
        <view class="tn-padding-right-xl" style="width: 80%">
          <tn-button backgroundColor="tn-bg-blue" width="100%" height="70rpx" @click="handleBuyCourse">
            <text class="tn-color-white" hover-class="tn-hover" :hover-stay-time="150" style="font-size: 30rpx"
              >购买课程</text
            >
          </tn-button>
        </view>
      </view>
    </view>
    <tn-toast ref="toast"></tn-toast>
  </view>
</template>

<script>
import template_page_mixin from '@/libs/mixin/template_page_mixin.js';
export default {
  mixins: [template_page_mixin],
  data() {
    return {
      imagePrefix: process.env.config[process.env.NAME].imagePrefix,
      commonLessonVideoPrefix: process.env.config[process.env.NAME].commonLessonVideoPrefix,
      model: {},
      fixedList: [{ name: '详情' }, { name: '目录' }],
      current: 0,
      collect: 'false',
      showLessonVideo: false,
      lessonVideo: '',
      teacher: {},
      oss: process.env.config[process.env.NAME].oss,
    };
  },
  async onLoad(options) {
    if (options.code) {
      this.model = await this.$tn.http.get('/fe/mp/courseDetail', { params: { code: options.code } });
    } else if (options.id) {
      this.model = await this.$tn.http.get('/fe/mp/courseDetail', { params: { id: options.id } });
    }
    if (uni.getStorageSync(process.env.config.authToken)) {
      this.collect = await this.$tn.http.get('/fe/mp/member/memberCollectCourse/existsOrNot', {
        params: { courseId: this.model.id },
      });
    }

    if(this.model.teacherCode){
      this.teacher = await this.$tn.http.get('/fe/mp/teacherDetail', { params: { code: this.model.teacherCode } });
    }

  },
  methods: {
    tabChange(index) {
      this.current = index;
    },
    formatDuration(duration) {
      const hours = Math.floor(duration / 3600);
      const remainingSeconds = duration % 3600;
      const minutes = Math.floor(remainingSeconds / 60);
      const seconds = remainingSeconds % 60;

      if (hours > 0) {
        return `${String(hours).padStart(2, '0')}:${String(minutes).padStart(2, '0')}:${String(seconds).padStart(
          2,
          '0',
        )}`;
      }
      return `${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}`;
    },
    formChapterTitle(item, index) {
      let sort = `第${this.numToChinese(index + 1)}章： `;
      let num = '（共' + item.lessonDtoList.length + '节）';
      return sort + item.name + num;
    },
    async collectCourse() {
      this.collect = 'true';
      await this.$tn.http.post(
        '/fe/mp/member/memberCollectCourse/collectCourse',
        { courseId: this.model.id },
        {
          header: {
            'Content-Type': 'application/x-www-form-urlencoded',
          },
        },
      );
      this.$refs.toast.show({
        content: '收藏成功',
        icon: 'success',
        image: '',
        duration: 1500,
      });
    },
    handleBuyCourse() {
      uni.navigateTo({
        url: '/pages/minePages/createOrder?courseId=' + this.model.id,
      });
    },
    async cancelCollect() {
      this.collect = 'false';
      await this.$tn.http.post(
        '/fe/mp/member/memberCollectCourse/deleteCollectCourse',
        { courseId: this.model.id },
        {
          header: {
            'Content-Type': 'application/x-www-form-urlencoded',
          },
        },
      );
      this.$refs.toast.show({
        content: '已取消收藏',
        icon: 'like-break',
        image: '',
        duration: 1500,
      });
    },
    async handleLessonClick(item2) {
      if (!item2.previewAble) {
        this.$refs.toast.show({
          content: '请先购买课程',
          icon: 'tip',
          image: '',
          duration: 1500,
        });
      } else {
        this.showLessonVideo=true;
        if(this.oss){
          this.lessonVideo = this.commonLessonVideoPrefix + item2.mediaUrl;
        }
        else{
          this.lessonVideo=this.commonLessonVideoPrefix+item2.id;
        }
      }
    },

    handleClickTeacher(){
      uni.navigateTo({
        url: '/pages/common/teacherDetail?code='+this.teacher.code,
      });
    },
    numToChinese(num) {
      const chineseNumbers = ['零', '一', '二', '三', '四', '五', '六', '七', '八', '九'];
      const units = ['', '十', '百', '千', '万'];
      if (num === 0) return chineseNumbers[0];
      let result = '';
      let unitIndex = 0;
      while (num > 0) {
        const digit = num % 10;
        if (digit !== 0) {
          result = chineseNumbers[digit] + units[unitIndex] + result;
        } else if (result.charAt(0) !== '零') {
          result = chineseNumbers[0] + result;
        }
        num = Math.floor(num / 10);
        unitIndex++;
      }
      return result.replace(/零+$/, ''); // 去除末尾的零
    },
  },
};
</script>

<style lang="scss" scoped>
.container {
  width: 100%;
  background-color: #ffffff;
}

.image-wrapper {
  position: relative;
  width: 100%;
  padding-top: 56.25%; /* 16:9比例计算（9/16=56.25%） */
}

.image-content {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}

.sticky-tab {
  position: sticky;
  top: 0;
  width: 100%;
  transition: all 0.25s ease-out;
  z-index: 100;
}

.tn-strip-bottom {
  width: 100%;
  border-bottom: 20rpx solid rgba(241, 241, 241, 0.8);
}

.tn-strip-top-min {
  width: 100%;
  border-top: 5rpx solid #f8f8f8;
}

.tn-strip-bottom-min {
  width: 100%;
  border-bottom: 5rpx solid #f8f8f8;
}

.between-box {
  display: flex;
  justify-content: space-between;
}
.rich-text-content {
  line-height: 1.6em;
  padding: 20rpx;
  width: 100%;
}

.message {
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-around;

  &__left {
    width: 8%;
  }
  &__middle {
    width: 75%;
    padding-left: 20rpx;
    padding-right: 40rpx;
  }
  &__right {
    width: 17%;
  }

  &__name {
    font-size: 32rpx;
    margin-bottom: 8rpx;
  }
  &__time {
    font-size: 24rpx;
  }
}
.tabbar-placeholder {
  min-height: 130rpx;
  height: calc(130rpx + env(safe-area-inset-bottom) / 2); /* 与 tabbar 高度一致 */
}
.tabbar {
  padding: 0, 10rpx, calc(30rpx + env(safe-area-inset-bottom) / 2);
  min-height: 130rpx;
  height: calc(130rpx + env(safe-area-inset-bottom) / 2);
}
.footerfixed {
  position: fixed;
  width: 100%;
  bottom: 0;
  z-index: 999;
}
</style>
