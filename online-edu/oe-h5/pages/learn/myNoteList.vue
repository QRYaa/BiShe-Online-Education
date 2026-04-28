<template>
  <view class="tn-safe-area-inset-bottom">
    <tn-nav-bar backTitle=" " backgroundColor="#ffffff">课程笔记</tn-nav-bar>
    <view class="list-content">
      <view class="note-item tn-flex tn-flex-row-between" @click="handleClickCourse()" :style="{ marginTop: vuex_custom_bar_height + 10 + 'px' }">
        <view class="tn-flex tn-flex-direction-column tn-flex-row-between">
          <view>{{ mcDto.courseDto.name }}</view>
          <view class="tn-flex tn-flex-col-center">
            <text class="tn-icon-write"></text>
            <text class="tn-padding-left-sm">共{{ mcDto.noteNum }}条笔记</text>
          </view>
          <view class="tn-flex tn-flex-col-center">
            <text class="tn-icon-video"></text>
            <text class="tn-padding-left-sm">已学{{ mcDto.completionPercentage*100 }}%</text>
          </view>
        </view>
        <image
          v-if="mcDto.courseDto.squareImage"
          class="item-img"
          :src="imagePrefix + mcDto.courseDto.squareImage"
          mode=""
        ></image>
        <image v-else class="item-img" mode="" src="/static/orderItem.png"></image>
      </view>
      <view style="padding: 20rpx 20rpx 0;font-weight: 500;">笔记列表</view>
      <view v-if="list.length > 0" style="width: 100%">
        <view class="note-item tn-flex" v-for="(item, index) in list" :key="index">
          <text class="tn-icon-idea tn-color-grey--dark tn-text-bold" style="font-size: 35rpx"></text>
          <view class="tn-padding-left-sm" style="flex-grow: 1" @click="handleClickNote(item)">
            <view>{{ bindGetSimpleText(item.content) }}</view>
            <view style="background-color: #dfdfdf; border-radius: 16rpx" class="tn-padding-sm tn-margin-top-sm">
              <text class="tn-icon-video"></text>
              <text class="tn-padding-left-sm">{{ item.chapterSort }}-{{ item.lessonSort + item.lessonName }}</text>
            </view>
            <view class="tn-flex tn-flex-row-between tn-padding-top-sm tn-text-sm tn-color-gray--dark">
              <view>{{ bindGetNoteStatus(item.status).title }}</view>
              <view>{{ bindFormatTime(item.createTime) }}</view>
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
import { getSimpleText } from '@/util/common.js';
import { getNoteStatus } from '@/util/dict.js';
import { formatTime } from '@/util/date.js';
import { union } from 'lodash';
export default {
  data() {
    return {
      imagePrefix: process.env.config[process.env.NAME].imagePrefix,
      queryForm: {
        pageNum: 1,
        pageSize: 10,
      },
      list: [],
      loadMoreText: '加载中...',
      totalPages: 0,
      mcDto:  {
        courseDto:  { name: '',squareImage: '' },
        noteNum: 0,
        completionPercentage: 0,
      },
    };
  },
  methods: {
    async fetchList() {
      let { content, totalPages } = await this.$tn.http.get('/fe/mp/member/note/page', {
        params: this.queryForm,
      });
      this.list = this.list.concat(content);
      this.totalPages = totalPages;
      this.queryForm.pageNum++;
      if (this.totalPages < this.queryForm.pageNum) {
        this.loadMoreText = '没有更多数据了!';
      }
    },
    bindGetSimpleText(html) {
      return getSimpleText(html);
    },
    bindGetNoteStatus(status) {
      return getNoteStatus(status);
    },
    bindFormatTime(time) {
      return formatTime(time);
    },
    handleClickCourse(){
      uni.navigateTo({ url: '/pages/common/courseLoading?code='+this.mcDto.courseDto.code });
    },
    handleClickNote(item) {
      uni.navigateTo({ url: '/pages/learnCourse/noteDetail?id=' + item.id });
    },
  },
  async onLoad(options) {
    if (options.courseId) {
      this.queryForm.courseId = options.courseId;
    }
    await this.fetchList();
    this.mcDto = await this.$tn.http.get('/fe/mp/member/memberCourse/getDetail?courseId='+this.queryForm.courseId);
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
.note-item {
  width: 100%;
  padding: 20rpx;
  background-color: #ffffff;
  border-radius: 16rpx;
  margin-top: 20rpx;
  box-shadow: 0rpx 4rpx 32rpx 0rpx rgba(69, 129, 219, 0.1);
}
.item-img {
  width: 150rpx;
  height: 150rpx;
  border-radius: 10rpx;
}
</style>
