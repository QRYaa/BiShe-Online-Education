<template>
  <view>
    <tn-nav-bar backTitle=" " backgroundColor="#ffffff">证件信息</tn-nav-bar>
    <view class="content" :style="{ paddingTop: vuex_custom_bar_height + 15 + 'px' }">
      <view class="cardBox">
        <view class="tn-flex tn-flex-row-between tn-strip-bottom-min" style="padding: 30rpx 0">
          <view class="">
            <view>姓名</view>
          </view>
          <view class="tn-text-md tn-color-grey">
            {{ model.realName }}
          </view>
        </view>
        <!-- <view class="tn-flex tn-flex-row-between tn-strip-bottom-min">
          <view class="">
            <view>性别</view>
          </view>
          <view class="tn-text-md tn-color-grey">
            {{ bindGetGender(model.gender).title }}
          </view>
        </view> -->
        <view class="tn-flex tn-flex-row-between tn-strip-bottom-min" style="padding: 30rpx 0">
          <view class="">
            <view>证件类型</view>
          </view>
          <view class="tn-text-md tn-color-grey">
            {{ bindGetIdCardType(model.type) }}
          </view>
        </view>
        <view class="tn-flex tn-flex-row-between tn-strip-bottom-min" style="padding: 30rpx 0">
          <view class="">
            <view>证件号</view>
          </view>
          <view class="tn-text-md tn-color-grey">
            {{ model.number }}
          </view>
        </view>
        <view class="tn-flex tn-flex-row-between" style="padding: 30rpx 0">
          <view class="">
            <view>证件有效期</view>
          </view>
          <view class="tn-text-md tn-color-grey">
            {{ model.expireDate }}
          </view>
        </view>
      </view>
      <view class="cardBox tn-flex tn-flex-row-between" style="margin-top: 20rpx" @click="handleClickUpdate()">
        <view>更新证件信息</view>
        <tn-tag v-if="auditStatus == 1" shape="radius" backgroundColor="tn-bg-blue" fontColor="tn-color-white"
          >审核中</tn-tag
        >
        <tn-tag v-else-if="auditStatus == 3" shape="radius" backgroundColor="tn-bg-orange" fontColor="tn-color-white"
          >被驳回</tn-tag
        >
        <text v-else class="tn-icon-right" />
      </view>
    </view>

    <view
      class="footerfixed tn-flex tn-flex-row-center tn-flex-col-center tn-color-gray"
      hover-class="tn-hover"
      :hover-stay-time="150"
      @click="handleUnbind()"
    >
      取消实名
    </view>
  </view>
</template>
<script>
import { getGender, getIdCardType } from '@/util/dict.js';
export default {
  data() {
    return {
      model: {},
      auditStatus: 0,
    };
  },
  methods: {
    bindGetGender(value) {
      return getGender(value);
    },
    bindGetIdCardType(value) {
      return getIdCardType(value);
    },
    async handleClickUpdate() {
      uni.navigateTo({
        url: '/pages/minePages/memberIdCardAudit',
      });
    },

    async handleUnbind() {
      await this.$tn.http.post('/fe/mp/member/unbindIdCard');
      uni.showToast({
          icon: 'none',
          title: '解绑成功',
        });
      setTimeout(() => {
        uni.redirectTo({ url: '/pages/index/index' });
      }, 1000);
    },
  },
  async onLoad(options) {
    this.model = await this.$tn.http.get('/fe/mp/member/idCardInfo');
    this.auditStatus = await this.$tn.http.get('/fe/mp/member/getAuditStatus');
  },
};
</script>
<style lang="scss">
page {
  background-color: #f2f6fa;
}
.content {
  padding: 30rpx;
  background-color: #f2f6fa;
  width: 100%;
}
.cardBox {
  background-color: #ffffff;
  padding: 30rpx;
  border-radius: 10rpx;
  width: 100%;
}
.tn-strip-bottom-min {
  width: 100%;
  border-bottom: 1rpx solid #f8f9fb;
}
.footerfixed {
  position: fixed;
  width: 100%;
  bottom: 50rpx;
  z-index: 999;
}
</style>
