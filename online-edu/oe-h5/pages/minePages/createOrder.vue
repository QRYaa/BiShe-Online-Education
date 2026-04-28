<template>
  <view class="tn-safe-area-inset-bottom" style="background-color: #ffffff;">
    <tn-nav-bar backTitle=" " backgroundColor="#ffffff">提交订单</tn-nav-bar>
    <view :style="{ paddingTop: vuex_custom_bar_height +'px' }">
        <view class="tn-strip-bottom"></view>
    <view class="order-item tn-strip-bottom" v-for="(item, index) in orderDto.orderItemDtoList" :key="index">
      <view class="order-item-t">
        <image v-if="item.image" class="item-img" :src="imagePrefix + item.image" mode=""></image>
        <image v-else src="/static/orderItem.png" class="item-img" mode=""></image>
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

    <view class="tn-padding-lg tn-text-md tn-text-bold" style="background-color: #ffffff;">价格明细：</view>
    <view class="tn-flex tn-flex-row-between tn-strip-bottom-min tn-padding-lg">
      <view class="">
        <view>商品金额</view>
      </view>
      <view class="tn-text-lg" style="font-weight: 500"> ¥{{ price.toFixed(2) }} </view>
    </view>
    <view class="tn-flex tn-flex-row-between tn-strip-bottom-min tn-padding-lg" style="background-color: #ffffff;">
      <view class="">
        <view>应付金额</view>
      </view>
      <view class="tn-text-lg" style="font-weight: 500"> ¥{{ finalPrice.toFixed(2) }} </view>
    </view>
    <view class="tn-strip-bottom"></view>

    <view class="tn-padding-lg tn-text-md tn-text-bold" style="background-color: #ffffff;">支付方式：</view>
    <view style="padding:40rpx 40rpx 40rpx 28rpx" class="tn-strip-bottom-min">
    <uv-radio-group v-model="paymentMethodValue" placement="column" iconPlacement="right">
      <uv-radio :name="1">
        <template>
            <view class="tn-flex tn-flex-col-center">
            <view class="tn-icon-wechat" style="font-size: 50rpx;color: #a0a0a0;"></view>
            <view class="tn-padding-left-xs">微信支付</view>
            </view>
        </template>
      </uv-radio>
    </uv-radio-group>
    </view>

    <!-- 添加占位元素 -->
    <view class="tabbar-placeholder"></view>
    <view class="tabbar footerfixed tn-bg-white">
      <!-- flex-row:justify-content,flex-col:align-items -->
      <view class="tn-flex tn-flex-row-between tn-flex-col-center tn-padding-top-xs">
        <view class="tn-padding-left tn-flex tn-flex-col-center" style="padding-right: 80rpx;">
          <view>实&nbsp;付</view>
          <view class="tn-text-xxl tn-text-bold tn-padding-left-sm">¥{{ finalPrice.toFixed(2) }}</view>
        </view>
        <view class="tn-padding-right" style="width: 80%">
          <tn-button backgroundColor="tn-bg-blue" width="100%" height="80rpx" :fontSize="30" @click="handlePay()">
            <text class="tn-color-white" hover-class="tn-hover" :hover-stay-time="150">
              立即支付
            </text>
          </tn-button>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  name: 'createOrder',
  data() {
    return {
      imagePrefix: process.env.config[process.env.NAME].imagePrefix,
      paymentMethodValue: 1,
      orderDto: {
        orderItemDtoList: [],
      },
      price: 0,
      finalPrice: 0,
    };
  },
  methods: {
    async handlePay() {
        let code = await this.$tn.http.post('/fe/mp/member/order/createOrder', this.orderDto);

        //TODO: 调用微信支付SDK模块

        //调后端接口，根据code查询订单状态


        uni.redirectTo({url: '/pages/minePages/order-details?code=' + code});
    },
  },
  async onLoad(options) {
    if (options.courseId) {
      let orderItemDto= await this.$tn.http.get('/fe/mp/member/order/generateOrderItemByReferenceIdAndType', { params: { referenceId: options.courseId,type: 1 } });
      this.orderDto.orderItemDtoList.push(orderItemDto);

      let dto=await this.$tn.http.post('/fe/mp/member/order/calculatePrice', this.orderDto);
      this.price=dto.price;
      this.finalPrice=dto.finalPrice;
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
        font-size: 28rpx;
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
