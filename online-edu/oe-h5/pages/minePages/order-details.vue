<template>
  <view class="tn-safe-area-inset-bottom" style="background-color: #ffffff;">
    <tn-nav-bar backTitle=" " backgroundColor="#ffffff">订单详情</tn-nav-bar>
    <view v-if="model.code">
      <view :style="{ paddingTop: vuex_custom_bar_height + 'px' }">
        <view
          class="tn-bg-white tn-text-lg tn-text-bold tn-flex tn-flex-row-center tn-flex-col-center"
          style="width: 100%; height: 70rpx"
        >
          <view :style="{ color: bindOrderStatus(model.paymentStatus).color }">
            <text v-if="model.paymentStatus == 1" class="tn-icon-time tn-text-lg tn-padding-right-xs" />
            <text v-else-if="model.paymentStatus == 2" class="tn-icon-success-circle tn-text-lg tn-padding-right-xs" />
            <text v-else-if="model.paymentStatus == 3" class="tn-icon-tip tn-text-lg tn-padding-right-xs" />
            {{ bindOrderStatus(model.paymentStatus).title }}
          </view>
        </view>
      </view>
      <view v-if="model.orderItemDtoList">
        <view class="order-item tn-strip-bottom" v-for="(item, index) in model.orderItemDtoList" :key="index">
          <view class="order-item-t" @click="openCourseDetail(item)">
            <image v-if="item.image" class="item-img" :src="imagePrefix + item.image" mode=""></image>
            <image v-else class="item-img" mode="" src="/static/orderItem.png" ></image>
            <view class="item-msg">
              <view class="msg-name">
                {{ item.name }}
              </view>
              <view class="msg-remark">
                {{ item.description }}
              </view>
            </view>
            <view style="text-align: right; width: 150rpx">
              <view class="price">
                <view>
                  <text class="unit">¥</text>
                  <text class="money">{{ item.totalPrice ? item.totalPrice.toFixed(2) : '0.00' }}</text>
                </view>
              </view>
            </view>
          </view>
        </view>
      </view>
      <view v-if="model.paymentStatus === 1 || model.paymentStatus === 3">
        <view class="tn-padding-lg tn-text-md tn-text-bold">订单信息：</view>
        <view class="tn-flex tn-flex-row-between tn-strip-bottom-min tn-padding-lg">
          <view class="">
            <view>订单编号</view>
          </view>
          <view class="tn-text-sm tn-flex tn-flex-col-center">
            <text class="tn-color-grey">{{ model.code }}</text>
            <text class="tn-icon-copy tn-text-md tn-padding-left-xs" @click="copy(model.code)"> </text>
          </view>
        </view>
        <view class="tn-flex tn-flex-row-between tn-strip-bottom-min tn-padding-lg">
          <view class="">
            <view>下单时间</view>
          </view>
          <view class="tn-text-sm tn-color-grey">
            {{ model.createdTime }}
          </view>
        </view>
        <view class="tn-flex tn-flex-row-between tn-strip-bottom-min tn-padding-lg">
          <view class="">
            <view>支付方式</view>
          </view>
          <view class="tn-text-sm tn-color-grey">
            {{ bindPaymentMethod(model.paymentMethod).title }}
          </view>
        </view>
        <view class="tn-flex tn-flex-row-between tn-strip-bottom-min tn-padding-lg">
          <view class="">
            <view>交易编号</view>
          </view>
          <view class="tn-text-sm tn-color-grey"> {{ model.transactionId }} </view>
        </view>
        <view class="tn-strip-bottom"></view>
        <view class="tn-padding-lg tn-text-md tn-text-bold">价格明细：</view>
        <view class="tn-flex tn-flex-row-between tn-strip-bottom-min tn-padding-lg">
          <view class="">
            <view>商品金额</view>
          </view>
          <view class="tn-text-lg" style="font-weight: 500"> ¥{{ model.price.toFixed(2) }} </view>
        </view>
        <view class="tn-flex tn-flex-row-between tn-strip-bottom-min tn-padding-lg">
          <view class="">
            <view>应付金额</view>
          </view>
          <view class="tn-text-lg" style="font-weight: 500"> ¥{{ model.finalPrice.toFixed(2) }} </view>
        </view>
      </view>
      <view v-else-if="model.paymentStatus === 2">
        <view class="tn-padding-lg tn-text-md tn-text-bold">订单信息：</view>
        <view class="tn-flex tn-flex-row-between tn-strip-bottom-min tn-padding-lg">
          <view class="">
            <view>订单编号</view>
          </view>
          <view class="tn-text-sm">
            <text class="tn-color-grey">{{ model.code }}</text>
            <text class="tn-icon-copy tn-text-md tn-padding-left-xs" @click="copy(model.code)"> </text>
          </view>
        </view>
        <view class="tn-flex tn-flex-row-between tn-strip-bottom-min tn-padding-lg">
          <view class="">
            <view>订单金额</view>
          </view>
          <view class="tn-text-lg" style="font-weight: 500"> ¥{{ model.price.toFixed(2) }} </view>
        </view>
        <view class="tn-flex tn-flex-row-between tn-strip-bottom-min tn-padding-lg">
          <view class="">
            <view>付款金额</view>
          </view>
          <view class="tn-text-lg" style="font-weight: 500"> ¥{{ model.finalPrice.toFixed(2) }} </view>
        </view>
        <view class="tn-flex tn-flex-row-between tn-strip-bottom-min tn-padding-lg">
          <view class="">
            <view>支付方式</view>
          </view>
          <view class="tn-text-sm tn-color-grey">
            {{ bindPaymentMethod(model.paymentMethod).title }}
          </view>
        </view>
        <view class="tn-flex tn-flex-row-between tn-strip-bottom-min tn-padding-lg">
          <view class="">
            <view>交易编号</view>
          </view>
          <view class="tn-text-sm tn-color-grey"> {{ model.transactionId }} </view>
        </view>
        <view class="tn-flex tn-flex-row-between tn-strip-bottom-min tn-padding-lg">
          <view class="">
            <view>下单时间</view>
          </view>
          <view class="tn-text-sm tn-color-grey">
            {{ model.createdTime }}
          </view>
        </view>
        <view class="tn-flex tn-flex-row-between tn-strip-bottom-min tn-padding-lg">
          <view class="">
            <view>支付时间</view>
          </view>
          <view class="tn-text-sm tn-color-grey">
            {{ model.paymentTime }}
          </view>
        </view>
      </view>

      <view v-if="model.paymentStatus === 1">
        <!-- 添加占位元素 -->
        <view class="tabbar-placeholder"></view>
        <view class="tabbar footerfixed tn-bg-white">
          <!-- flex-row:justify-content,flex-col:align-items -->
          <view class="tn-flex tn-flex-row-between tn-flex-col-center tn-padding-top-xs">
            <view class="tn-padding-left">
              <view class="tn-text-sm tn-color-gray">取消订单</view>
            </view>
            <view class="tn-padding-right" style="width: 70%">
              <tn-button backgroundColor="tn-bg-blue" width="100%" @click="">
                <text class="tn-color-white" hover-class="tn-hover" :hover-stay-time="150">
                  立即支付&nbsp;&nbsp;¥{{ model.finalPrice }}
                </text>
              </tn-button>
            </view>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { getPaymentMethod, getOrderStatusBgColor } from '@/util/dict.js';
export default {
  name: 'orderDetails',
  data() {
    return {
      imagePrefix: process.env.config[process.env.NAME].imagePrefix,
      model: {},
    };
  },
  methods: {
    bindPaymentMethod(paymentMethod) {
      return getPaymentMethod(paymentMethod);
    },
    bindOrderStatus(paymentStatus) {
      return getOrderStatusBgColor(paymentStatus);
    },
    copy(value) {
      uni.setClipboardData({
        data: value, //要被复制的内容
        success: () => {
          //复制成功的回调函数
          uni.showToast({
            //提示
            title: '复制成功',
          });
        },
      });
    },
    openCourseDetail(item) {
      uni.navigateTo({ url: '/pages/common/courseLoading?id=' + item.referenceId });
    },
  },
  async onLoad(options) {
    if (options.code) {
      this.model = await this.$tn.http.get('/fe/mp/member/order/detail', { params: { code: options.code } });
    }
  },
};
</script>

<style lang="scss" scoped>
.footerfixed {
  position: fixed;
  width: 100%;
  bottom: 0;
  z-index: 999;
  // background-color: rgba(255,255,255,1);
  // box-shadow: 0rpx 0rpx 30rpx 0rpx rgba(0, 0, 0, 0.07);
}

.tabbar {
  align-items: center;
  min-height: 130rpx;
  padding: 0;
  height: calc(130rpx + env(safe-area-inset-bottom) / 2);
  padding-bottom: calc(30rpx + env(safe-area-inset-bottom) / 2);
  padding-left: 10rpx;
  padding-right: 10rpx;
}

.tn-strip-bottom-min {
  width: 100%;
  border-top: 1rpx solid #f8f9fb;
}

.tn-strip-bottom {
  width: 100%;
  border-bottom: 20rpx solid rgba(241, 241, 241, 0.8);
}

.tabbar-placeholder {
  min-height: 130rpx;
  height: calc(130rpx + env(safe-area-inset-bottom) / 2); /* 与 tabbar 高度一致 */
}

.order-item {
  width: 100%;
  box-sizing: border-box;
  padding: 40rpx;
  background-color: #ffffff;
  border-radius: 16rpx;
  box-shadow: 0rpx 4rpx 32rpx 0rpx rgba(69, 129, 219, 0.1);

  .order-item-t {
    width: 100%;
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: flex-start;
    box-sizing: border-box;
    padding-bottom: 26rpx;

    .item-img {
      width: 130rpx;
      height: 130rpx;
    }

    .item-msg {
      width: calc(100% - 284rpx);
      box-sizing: border-box;
      padding-left: 24rpx;
      .msg-name {
        width: 100%;
        font-size: 28rpx;
        color: #1d2129;
        font-weight: 500;

        overflow: hidden;
        /* 隐藏溢出的文本 */
        white-space: nowrap;
        /* 防止文本换行 */
        text-overflow: ellipsis;
        /* 溢出部分用省略号表示 */
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
    .price {
      .unit {
        color: #1d2129;
        font-size: 20rpx;
      }
      .money {
        font-size: 24rpx;
        font-weight: bold;
        //color: #f10a0a;
        color: #1d2129;
      }
    }

    .item-price-box {
      width: 140rpx;
      text-align: right;
    }
  }
}
</style>
