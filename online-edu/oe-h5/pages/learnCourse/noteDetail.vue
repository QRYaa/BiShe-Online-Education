<template>
  <view style="background-color: #ffffff;">
    <tn-nav-bar backTitle=" " backgroundColor="#F8F8F8">笔记详情</tn-nav-bar>
    <view style="padding: 30rpx 30rpx 0;" :style="{ paddingTop: vuex_custom_bar_height + 10 + 'px' }">
      <tn-avatar :src="imagePrefix + model.memberAvatar"></tn-avatar>
      <view class="tn-padding-left-sm" style="display: inline-block">
        <view>{{ model.memberName }}</view>
        <view class="tn-color-gray tn-text-sm">{{ bindFormatTime(model.createTime) }}</view>
      </view>
    </view>
    <view class="tn-strip-bottom" style="padding: 30rpx">
      <mp-html v-show="model.content" class="" :content="model.content" :selectable="true" />
    </view>
    <view style="padding: 30rpx 0">
      <view class="tn-text-bold" style="padding: 0 30rpx">回复列表</view>
      <view v-if="!replyList.length" style="padding-top: 50rpx; width: 100%; text-align: center">
        期待你的第一条回复
      </view>
      <view v-else>
        <tn-list-view>
          <tn-list-cell
            padding="26rpx 30rpx"
            :unlined="false"
            :lineLeft="false"
            :lineRight="false"
            v-for="(item, index) in replyList"
            :key="index"
          >
            <view>
              <tn-avatar :src="imagePrefix + item.senderAvatar"></tn-avatar>
              <view class="tn-padding-left-sm" style="display: inline-block">
                <view>{{ item.senderName }}</view>
                <view class="tn-color-gray tn-text-sm">{{ bindFormatTime(item.createTime) }}</view>
              </view>
            </view>
            <view v-if="item.root" class="tn-padding-top-sm">{{ item.content }}</view>
            <view v-else class="tn-padding-top-sm">
              <text>回复</text>
              <text style="color: #a3a3a3; padding-left: 5rpx">{{ item.receiverName }}</text>
              <text>：{{ item.content }}</text>
            </view>
            <view class="tn-padding-top-sm tn-flex tn-flex-col-center tn-flex-row-between">
              <view class="tn-flex tn-flex-col-center">
                <text
                  v-if="!likeIdSet.has(item.id)"
                  class="tn-icon-praise"
                  style="font-size: 42rpx; color: #5e6367"
                  @click="handlePraiseReply(item)"
                />
                <text
                  v-else
                  class="tn-icon-praise-fill"
                  style="font-size: 42rpx; color: #ff5a6b"
                  @click="handleCancelPraiseReply(item)"
                />
                <text
                  v-if="item.likeNum && !likeIdSet.has(item.id)"
                  class="tn-padding-left-xs"
                  style="color: #5e6367"
                  >{{ item.likeNum }}</text
                >
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
          </tn-list-cell>
        </tn-list-view>
      </view>
    </view>
    <!-- 添加占位元素 -->
    <view style="height: 100rpx"></view>
    <view class="footBar tn-padding">
      <view class="tn-flex tn-flex-row-between tn-flex-col-center" style="width: 100%">
        <view
          style="border-radius: 30rpx; background-color: #f1f2f4; color: #9a9a9c; padding: 20rpx; text-align: center"
          :style="{ width: model.likeNum ? '86%' : '79%' }"
          @click="handleClickReply2()"
          >点击即可回复哟~</view
        >
        <view v-if="isLikeNote != 'true'" class="tn-flex tn-flex-col-center" @click="handlePraiseNote">
          <text class="tn-icon-praise" style="font-size: 50rpx; color: #5e6367" />
          <text style="font-size: 32rpx; padding-left: 10rpx; color: #5e6367">{{
            model.likeNum ? model.likeNum : '点赞'
          }}</text>
        </view>

        <view v-else class="tn-flex tn-flex-col-center" @click="handleCancelPraiseNote">
          <text class="tn-icon-praise-fill" style="font-size: 50rpx; color: #ff5a6b" />
          <text style="font-size: 32rpx; padding-left: 10rpx; color: #ff5a6b">{{
            model.likeNum ? model.likeNum : '点赞'
          }}</text>
        </view>
      </view>
    </view>
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
        <view class="tn-flex tn-flex-direction-column tn-flex-col-center" @click="handleCopyReply()">
          <tn-button shape="icon" margin="10rpx 10rpx">
            <text class="tn-icon-copy" style="font-size: 80rpx; color: #5e6367"></text>
          </tn-button>
          <view style="color: #5e6367">复制</view>
        </view>

        <view
          v-if="canDelete"
          class="tn-flex tn-flex-direction-column tn-flex-col-center tn-padding-left"
          @click="handleDeleteReply()"
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
import tnListCell from '../../tuniao-ui/components/tn-list-cell/tn-list-cell.vue';

export default {
  components: { tnListCell },
  data() {
    return {
      imagePrefix: process.env.config[process.env.NAME].imagePrefix,
      model: {
        content: '',
        createTime: '',
      },
      showPopup: false,
      replyDto: {
        content: '',
      },
      replyList: [],
      member: uni.getStorageSync('member'),
      queryForm: {
        pageNum: 1,
        pageSize: 10,
        noteId: 0,
      },
      totalPages: 0,
      loadMoreText: '加载中...',
      likeIdSet: new Set(),
      isLikeNote: 'false',
      showPopup2: false,
      canDelete: false,
      clickMoreReplyItem: {},
    };
  },
  async onLoad(options) {
    if (options.id) {
      this.model = await this.$tn.http.get('/fe/mp/member/note/getDetail', { params: { id: options.id } });
      this.isLikeNote = await this.$tn.http.get('/fe/mp/member/memberLikeNote/existsOrNot', {
        params: { noteId: options.id },
      });
      this.replyDto.noteId = this.model.id;
      this.queryForm.noteId = this.model.id;
      await this.fetchReplyData();

      let likeIdList = await this.$tn.http.get('/fe/mp/member/memberLikeReply/replyIdList', {
        params: { noteId: options.id },
      });
      likeIdList.forEach((item) => {
        this.likeIdSet.add(item);
      });
      this.$forceUpdate();
    }
  },
  async onReachBottom() {
    console.log('到达底部，onReachBottom');
    if (this.totalPages < this.queryForm.pageNum) {
      return;
    }
    await this.fetchReplyData();
  },
  methods: {
    bindFormatTime(time) {
      return formatTime(time);
    },
    async handleCreateReply() {
      this.replyDto.content = this.replyDto.content.trim();
      if (!this.replyDto.content.length) {
        return;
      }
      try {
        let { id, trackId } = await this.$tn.http.post('/fe/mp/member/reply/createReply', this.replyDto);
        let tempReplyDto = { ...this.replyDto };
        tempReplyDto.senderId = this.member.id;
        tempReplyDto.senderName = this.member.name;
        tempReplyDto.senderAvatar = this.member.avatar;
        tempReplyDto.createTime = '刚刚';
        tempReplyDto.id = id;
        tempReplyDto.trackId = trackId;
        this.replyList.unshift(tempReplyDto);
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
    async fetchReplyData() {
      let { content, totalPages } = await this.$tn.http.get('/fe/mp/member/reply/page', {
        params: this.queryForm,
      });
      this.replyList = this.replyList.concat(content);
      this.totalPages = totalPages;
      this.queryForm.pageNum++;
      if (this.totalPages < this.queryForm.pageNum) {
        this.loadMoreText = '没有更多数据了!';
      }
    },
    async handlePraiseReply(item) {
      await this.$tn.http.post(
        '/fe/mp/member/memberLikeReply/likeReply',
        {
          replyId: item.id,
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
    async handleCancelPraiseReply(item) {
      await this.$tn.http.post(
        '/fe/mp/member/memberLikeReply/deleteLikeReply',
        {
          replyId: item.id,
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
      this.replyDto.root = false;
      this.replyDto.trackId = item.trackId;
      this.replyDto.receiverId = item.senderId;
      this.replyDto.receiverName = item.senderName;
      this.showPopup = true;
    },
    handleClickReply2() {
      this.replyDto.root = true;
      this.replyDto.trackId = null;
      this.replyDto.receiverName = null;
      this.showPopup = true;
    },
    async handlePraiseNote() {
      await this.$tn.http.post(
        '/fe/mp/member/memberLikeNote/likeNote',
        {
          noteId: this.model.id,
        },
        {
          header: {
            'Content-Type': 'application/x-www-form-urlencoded',
          },
        },
      );
      if (this.model.likeNum) {
        this.model.likeNum += 1;
      } else {
        this.model.likeNum = 1;
      }
      this.isLikeNote = 'true';
    },
    async handleCancelPraiseNote() {
      await this.$tn.http.post(
        '/fe/mp/member/memberLikeNote/deleteLikeNote',
        {
          noteId: this.model.id,
        },
        {
          header: {
            'Content-Type': 'application/x-www-form-urlencoded',
          },
        },
      );
      if (this.model.likeNum) {
        this.model.likeNum -= 1;
      } else {
        this.model.likeNum = 0;
      }
      this.isLikeNote = 'false';
    },
    handleClickMore(item) {
      this.showPopup2 = true;
      this.canDelete = this.member.id === item.senderId;
      this.clickMoreReplyItem = item;
    },
    handleCopyReply() {
      uni.setClipboardData({
        data: this.bindGetSimpleText(this.clickMoreReplyItem.content), //要被复制的内容
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
    async handleDeleteReply() {
      await this.$tn.http.post(
        '/fe/mp/member/reply/delete',
        {
          id: this.clickMoreReplyItem.id,
        },
        {
          header: {
            'Content-Type': 'application/x-www-form-urlencoded',
          },
        },
      );
      let index = this.replyList.findIndex((item) => item.id === this.clickMoreReplyItem.id);
      if (index !== -1) this.replyList.splice(index, 1);
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

<style lang="scss" scoped>
.footBar {
  position: fixed;
  bottom: 0;
  left: 0;
  z-index: 1;
  width: 100%;
}
.tn-strip-bottom {
  width: 100%;
  border-bottom: 20rpx solid rgba(241, 241, 241, 0.8);
}
</style>
