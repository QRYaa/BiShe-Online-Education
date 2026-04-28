<template>
  <view style="padding: 10rpx 0;">
    <view class="tn-flex tn-flex-row-between tn-flex-col-center" style="padding: 0 30rpx;">
      <view>全部笔记</view>
      <view style="width: 220rpx; padding: 8rpx; background-color: #edf0f5">
        <tn-tabs
          :showBar="false"
          :isScroll="false"
          :height="42"
          :activeItemStyle="{ backgroundColor: '#ffffff', color: '#353535', padding: '0 10rpx', fontSize: '24rpx' }"
          :inactiveItemStyle="{
            backgroundColor: '#edf0f5',
            color: '#a2a6a9',
            padding: '0 10rpx',
            fontSize: '24rpx',
          }"
          :current="currentNoteStatus"
          :list="noteStatusList"
          @change="noteStatusChange"
        ></tn-tabs>
      </view>
    </view>

    <tn-list-view>
      <tn-list-cell
        padding="26rpx 30rpx"
        :unlined="false"
        :lineLeft="false"
        :lineRight="false"
        v-for="(item, index) in noteList"
        :key="index"
      >
        <view>
          <tn-avatar :src="imagePrefix + item.memberAvatar"></tn-avatar>
          <view class="tn-padding-left-sm" style="display: inline-block">
            <view>{{ item.memberName }}</view>
            <view class="tn-color-gray tn-text-sm">{{ bindFormatTime(item.createTime) }}</view>
          </view>
        </view>
        <!-- <scroll-view
          :scroll-y="false"
          class="tn-padding-top-sm"
          style="max-height: 200rpx"
          @click="handleClickNote(item)"
        >
          <mp-html v-show="item.content" class="" :content="item.content" :selectable="true" />
        </scroll-view> -->
        <view class="tn-padding-top-sm" @click="handleClickNote(item)">{{ bindGetSimpleText(item.content) }}</view>
        <view class="tn-padding-top-sm tn-flex tn-flex-col-center tn-flex-row-between">
          <view class="tn-flex tn-flex-col-center">
            <text
              v-if="!likeIdSet.has(item.id)"
              class="tn-icon-praise"
              style="font-size: 42rpx; color: #5e6367"
              @click="handlePraiseNote(item)"
            />
            <text
              v-else
              class="tn-icon-praise-fill"
              style="font-size: 42rpx; color: #ff5a6b"
              @click="handleCancelPraiseNote(item)"
            />
            <text v-if="item.likeNum && !likeIdSet.has(item.id)" class="tn-padding-left-xs" style="color: #5e6367">{{
              item.likeNum
            }}</text>
            <text
              v-else-if="item.likeNum && likeIdSet.has(item.id)"
              class="tn-padding-left-xs"
              style="color: #ff5a6b"
              >{{ item.likeNum }}</text
            >
            <text
              class="tn-icon-comment tn-padding-left"
              style="font-size: 42rpx; color: #5e6367"
              @click="handleClickReply(item)"
            />
          </view>
          <view
            class="tn-icon-more-vertical"
            style="font-size: 38rpx; color: #cccccc"
            @click="handleClickMore(item)"
          ></view>
        </view>
        <view
          v-if="item.replyNum"
          class="tn-margin-top-sm"
          style="padding: 20rpx 0 20rpx 20rpx; border-radius: 20rpx; background-color: #f6f7f9"
          @click="handleClickNote(item)"
        >
          <text style="color: #186484">共{{ item.replyNum }}条回复&nbsp;> </text>
        </view>
      </tn-list-cell>
    </tn-list-view>
    <view style="height: 80rpx; line-height: 80rpx; text-align: center; padding-bottom: 30rpx">{{ loadMoreText }}</view>
    <tn-popup
      v-model="showPopup"
      mode="bottom"
      :maskCloseable="true"
      :mask="true"
      :borderRadius="15"
      @close="showPopup = false"
    >
      <view style="padding: 10rpx 20rpx 20rpx">
        <tn-input
          v-model="replyDto.content"
          type="textarea"
          placeholder="请文明用语哟~"
          :focus="true"
          :clearable="false"
          :maxLength="255"
        />
        <view class="tn-flex tn-flex-row-between">
          <view></view>
          <tn-button backgroundColor="tn-bg-blue" fontColor="tn-color-white" @click="handleCreateReply">发表</tn-button>
        </view>
      </view>
    </tn-popup>
    <tn-popup
      v-model="showPopup2"
      mode="bottom"
      :maskCloseable="true"
      :mask="true"
      :borderRadius="15"
      @close="showPopup2 = false"
    >
      <view style="padding: 50rpx 50rpx 0" class="tn-flex">
        <view class="tn-flex tn-flex-direction-column tn-flex-col-center" @click="handleCopyNote()">
          <tn-button shape="icon" margin="10rpx 10rpx">
            <text class="tn-icon-copy" style="font-size: 80rpx; color: #5e6367"></text>
          </tn-button>
          <view style="color: #5e6367">复制</view>
        </view>

        <view
          v-if="canDelete"
          class="tn-flex tn-flex-direction-column tn-flex-col-center tn-padding-left"
          @click="handleDeleteNote()"
        >
          <tn-button shape="icon" margin="10rpx 10rpx">
            <text class="tn-icon-delete" style="font-size: 80rpx; color: #5e6367"></text>
          </tn-button>
          <view style="color: #5e6367">删除</view>
        </view>
      </view>

      <view
        style="text-align: center; padding-top: 60rpx; padding-bottom: 40rpx; font-size: 35rpx"
        @click="showPopup2 = false"
        >取消</view
      >
    </tn-popup>
    <tn-toast ref="toast"></tn-toast>
  </view>
</template>
<script>
import { formatTime } from '@/util/date.js';
import { getSimpleText } from '@/util/common.js';

export default {
  props: {
    lessonId: {
      type: String,
      default: '0',
    },
  },
  data() {
    return {
      imagePrefix: process.env.config[process.env.NAME].imagePrefix,
      noteList: [],
      queryForm: {
        pageNum: 1,
        pageSize: 10,
        status: 2,
        lessonId: this.lessonId,
      },
      noteStatusList: [{ name: '公开' }, { name: '私密' }],
      currentNoteStatus: 0,
      totalPages: 0,
      loadMoreText: '加载中...',
      likeIdSet: new Set(),
      showPopup: false,
      replyDto: {
        noteId: 0,
        content: '',
        root: true,
      },
      noteItem: {},
      showPopup2: false,
      canDelete: false,
      member: uni.getStorageSync('member'),
      clickMoreNoteItem: {},
    };
  },

  beforeMount() {
    uni.$on('onReachBottom', async () => {
      // 监听上拉加载
      console.log('到达底部，onReachBottom');
      if (this.totalPages < this.queryForm.pageNum) {
        return;
      }
      await this.fetchNoteData();
    });
  },

  async mounted() {
    await this.fetchNoteData();
    let likeIdList = await this.$tn.http.get('/fe/mp/member/memberLikeNote/noteIdList', {
      params: { lessonId: this.lessonId },
    });
    likeIdList.forEach((item) => {
      this.likeIdSet.add(item);
    });
    this.$forceUpdate();
  },
  methods: {
    handleClickNote(item) {
      uni.navigateTo({
        url: '/pages/learnCourse/noteDetail?id=' + item.id,
      });
    },
    bindFormatTime(time) {
      return formatTime(time);
    },
    async noteStatusChange(index) {
      this.currentNoteStatus = index;
      this.loadMoreText = '加载中...';
      this.noteList = [];
      this.totalPages = 0;
      this.queryForm.pageNum = 1;
      if (index === 0) {
        this.queryForm.status = 2;
      } else if (index === 1) {
        this.queryForm.status = 1;
      }
      this.queryForm.lessonId = this.lessonId;
      await this.fetchNoteData();
    },
    async fetchNoteData() {
      let { content, totalPages } = await this.$tn.http.get('/fe/mp/member/note/page', {
        params: this.queryForm,
      });
      this.noteList = this.noteList.concat(content);
      this.totalPages = totalPages;
      this.queryForm.pageNum++;
      if (this.totalPages < this.queryForm.pageNum) {
        this.loadMoreText = '没有更多数据了!';
      }
    },
    async handlePraiseNote(item) {
      await this.$tn.http.post(
        '/fe/mp/member/memberLikeNote/likeNote',
        {
          noteId: item.id,
        },
        {
          header: {
            'Content-Type': 'application/x-www-form-urlencoded',
          },
        },
      );
      this.likeIdSet.add(item.id);
      if (item.likeNum) {
        item.likeNum += 1;
      } else {
        item.likeNum = 1;
      }
      this.$forceUpdate();
    },
    async handleCancelPraiseNote(item) {
      await this.$tn.http.post(
        '/fe/mp/member/memberLikeNote/deleteLikeNote',
        {
          noteId: item.id,
        },
        {
          header: {
            'Content-Type': 'application/x-www-form-urlencoded',
          },
        },
      );
      this.likeIdSet.delete(item.id);
      if (item.likeNum) {
        item.likeNum -= 1;
      } else {
        item.likeNum = 0;
      }
    },
    handleClickReply(item) {
      this.showPopup = true;

      this.noteItem = item;
      this.replyDto.noteId = item.id;
    },
    async handleCreateReply() {
      this.replyDto.content = this.replyDto.content.trim();
      if (!this.replyDto.content.length) {
        return;
      }
      try {
        await this.$tn.http.post('/fe/mp/member/reply/createReply', this.replyDto);
        if (this.noteItem.replyNum) {
          this.noteItem.replyNum += 1;
        } else {
          this.noteItem.replyNum = 1;
        }
        this.showPopup = false;
        this.replyDto.content = '';
        this.$refs.toast.show({
          content: '发表成功',
          icon: 'success',
          image: '',
          duration: 1500,
        });
      } catch (e) {
        if (e.code === 380105) {
          this.$refs.toast.show({
            content: '包含敏感词，发表失败',
            icon: 'close',
            image: '',
            duration: 1500,
          });
        } else if (e.code === 380106) {
          this.$refs.toast.show({
            content: '笔记不存在',
            icon: 'close',
            image: '',
            duration: 1500,
          });
        }
      }
    },
    handleClickMore(item) {
      this.showPopup2 = true;
      this.canDelete = this.member.id === item.memberId;
      this.clickMoreNoteItem = item;
    },
    handleCopyNote() {
      uni.setClipboardData({
        data: this.bindGetSimpleText(this.clickMoreNoteItem.content), //要被复制的内容
        success: () => {
          this.showPopup2 = false;
          //复制成功的回调函数
          uni.showToast({
            //提示
            title: '复制成功',
          });
        },
      });
    },
    async handleDeleteNote() {
      await this.$tn.http.post(
        '/fe/mp/member/note/delete',
        {
          id: this.clickMoreNoteItem.id,
        },
        {
          header: {
            'Content-Type': 'application/x-www-form-urlencoded',
          },
        },
      );
      let index = this.noteList.findIndex((item) => item.id === this.clickMoreNoteItem.id);
      if (index !== -1) this.noteList.splice(index, 1);
      this.showPopup2 = false;
      uni.showToast({
        //提示
        title: '删除成功',
      });
    },
    bindGetSimpleText(html) {
      return getSimpleText(html);
    },
  },
};
</script>
<style lang="scss" scoped></style>
