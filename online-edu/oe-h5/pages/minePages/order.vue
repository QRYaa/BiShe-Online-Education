<template>
  <view class="tn-safe-area-inset-bottom">
    <tn-nav-bar backTitle=" " backgroundColor="#ffffff">我的订单</tn-nav-bar>
    <view
      class="top-fixed"
      :style="{ paddingTop: vuex_custom_bar_height + 10 + 'px' }"
      style="background-color: #f8f7f8"
    >
      <tn-tabs
        :list="fixedList"
        :current="current"
        :isScroll="false"
        inactiveColor="#838383"
        activeColor="#080808"
        :bold="true"
        backgroundColor="#F8F7F8"
        :fontSize="32"
        :badgeOffset="[20, 50]"
        @change="tabChange"
      ></tn-tabs>
    </view>

    <view v-if="listData[current].length > 0" class="list-content">
      <view class="order-item" v-for="(item, index) in listData[current]" :key="index">
        <view class="order-status">
          <view class="date">
            订单编号：{{ item.code }}
            <text
              class="tn-icon-copy tn-text-sm tn-padding-left-sm"
              style="opacity: 0.5"
              @click="copy(item.code)"
            ></text>
          </view>
          <view class="status">
            <view class="tn-color-gray--dark">
              {{ bindStatus(item.paymentStatus).title }}
            </view>
          </view>
        </view>
        <view class="order-item-t" @click="orderDetail(item)">
          <image v-if="item.orderItemDtoList[0].image" class="item-img" :src="imagePrefix + item.orderItemDtoList[0].image" mode=""></image>
          <image v-else class="item-img" mode="" src="/static/orderItem.png"></image>
          <view class="item-msg">
            <view class="msg-name">
              {{ item.orderItemDtoList[0].name }}
            </view>
            <view class="msg-remark">
              {{ item.orderItemDtoList[0].description }}
            </view>
          </view>
          <view style="text-align: right; width: 150rpx">
            <view class="price">
              <view
                ><text class="unit">¥</text
                ><text class="money">{{ item.finalPrice ? item.finalPrice.toFixed(2) : '0.00' }}</text></view
              >
              <view style="font-size: 20rpx; color: #a6a6a6">共{{ item.orderItemDtoList.length }}件</view>
            </view>
          </view>
        </view>

        <view class="order-item-b">
          <view class="order-item-b-l">
            {{ item.createdTime }}
          </view>
          <view class="order-item-b-r">
            <view class="list-btn pay-btn" @click="pay(item)" v-if="orderPending(item)">
              <view class="list-btn-content pay-content"> 去支付 </view>
            </view>
            <view class="list-btn" @click="" v-else-if="orderCompleted(item)">
              <view class="list-btn-content"> 去学习 </view>
            </view>
          </view>
        </view>
      </view>
      <view style="height: 80rpx; line-height: 80rpx; text-align: center; padding-bottom: 30rpx">{{
        loadMoreText[current]
      }}</view>
    </view>

    <!-- <view class="tn-tabbar-height"></view> -->
  </view>
</template>

<script>
import template_page_mixin from '@/libs/mixin/template_page_mixin.js';
import { getOrderStatusBgColor } from '@/util/dict.js';
export default {
  name: 'TemplateOrder',
  mixins: [template_page_mixin],
  data() {
    return {
      imagePrefix: process.env.config[process.env.NAME].imagePrefix,
      current: 0,
      fixedList: [{ name: '全部' }, { name: '待支付' }, { name: '已完成' }, { name: '已取消' }],
      listData: [[],[],[],[]],
      queryForm: [
        {
          paymentStatus: null,
          codeOrItemName: '',
          pageNum: 1,
          pageSize: 10,
        },
        {
          paymentStatus: 1,
          codeOrItemName: '',
          pageNum: 1,
          pageSize: 10,
        },
        {
          paymentStatus: 2,
          codeOrItemName: '',
          pageNum: 1,
          pageSize: 10,
        },
        {
          paymentStatus: 3,
          codeOrItemName: '',
          pageNum: 1,
          pageSize: 10,
        },
      ],
      totalPages: [0, 0, 0, 0],
      loadMoreText: ['加载中...', '加载中...', '加载中...', '加载中...'],
    };
  },
  methods: {
    // tab选项卡切换
    async tabChange(index) {
      this.current = index;
      if(this.queryForm[this.current].pageNum===1) await this.fetchOrderData();
    },
    // 跳转
    tn(e) {
      uni.navigateTo({
        url: e,
      });
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
    async fetchOrderData() {
      let { content, totalPages } = await this.$tn.http.get('/fe/mp/member/order/page', {
        params: this.queryForm[this.current],
      });
      //this.listData[this.current] = this.listData[this.current].concat(content);
      this.listData[this.current].push(...content);
      this.totalPages[this.current] = totalPages;
      this.queryForm[this.current].pageNum++;
      if (this.totalPages[this.current] < this.queryForm[this.current].pageNum) {
        this.loadMoreText[this.current] = '没有更多数据了!';
      }
    },

    orderDetail(item) {
      uni.navigateTo({
        url: '/pages/minePages/order-details?code=' + item.code
      });
    },
    pay(item) {},
    orderPending(item) {
      return item.paymentStatus === 1;
    },
    orderCompleted(item) {
      return item.paymentStatus === 2;
    },
    bindStatus(paymentStatus) {
      return getOrderStatusBgColor(paymentStatus);
    },
  },
  async onShow() {
    await this.fetchOrderData();
  },
  onUnload() {
    (this.listData = [[],[],[],[]]), (this.loadMoreText = ['加载中...', '加载中...', '加载中...', '加载中...']);
  },
  async onReachBottom() {
    // 监听上拉加载
    console.log('到达底部，onReachBottom');
    if (this.totalPages[this.current] < this.queryForm[this.current].pageNum) {
      //this.loadMoreText = '没有更多数据了!';
      return;
    }
    await this.fetchOrderData();
  },
};
</script>

<style lang="scss" scoped>
/* 底部安全边距 start*/
.tn-tabbar-height {
  min-height: 40rpx;
  height: calc(60rpx + env(safe-area-inset-bottom) / 2);
  height: calc(60rpx + constant(safe-area-inset-bottom));
}
.top-fixed {
  position: fixed;
  top: 0;
  width: 100%;
  transition: all 0.25s ease-out;
  z-index: 100;
}

.list-content {
  width: 100%;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  padding: 0 32rpx;
  margin-top: 180rpx;
  background-color: #F8F7F8;
}

.order-item {
  width: 100%;
  box-sizing: border-box;
  padding: 20rpx;
  background-color: #ffffff;
  border-radius: 16rpx;
  margin-top: 32rpx;
  box-shadow: 0rpx 4rpx 32rpx 0rpx rgba(69, 129, 219, 0.1);

  .order-item-t {
    width: 100%;
    display: flex;
    flex-direction: row;
    //align-items: flex-start;
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

  .order-item-b {
    width: 100%;
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: flex-start;
    color: #27272a;
    font-size: 24rpx;

    .order-item-b-l {
      width: 50%;
      display: flex;
      flex-direction: row;
      align-items: center;
      justify-content: flex-start;
      color: #4e5969;
      font-size: 24rpx;
    }

    .order-item-b-r {
      width: 50%;
      display: flex;
      flex-direction: row;
      align-items: center;
      justify-content: flex-end;
    }

    image {
      width: 32rpx;
      height: auto;
      margin-right: 16rpx;
    }

    .list-btn {
      background: #838383;
      border-radius: 64rpx;
      padding: 1rpx;
      margin-left: 15rpx;
    }
    .list-btn-content {
      width: 100%;
      border-radius: 36rpx;
      background-color: #fff;
      padding: 12rpx 22rpx;
      display: flex;
      flex-direction: row;
      align-items: center;
      justify-content: center;
      color: #838383;
      font-size: 24rpx;
    }
    .pay-btn {
      background: linear-gradient(to right, #7fc2d8, #256aba);
      padding: 2rpx;
    }
    .pay-content {
      color: #4ca1e2;
    }
    .check-btn {
      color: #27272a;
      border: 1rpx solid #cccccc;
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
