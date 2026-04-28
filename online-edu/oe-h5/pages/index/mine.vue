<template>
  <view class="tn-safe-area-inset-bottom">
    <view class="top-backgroup">
      <image src="/static/bg1.png" mode="widthFix" class="backgroud-image"></image>
    </view>

    <view class="about__wrap">
      <view class="tn-flex tn-flex-row-between tn-flex-col-center tn-margin-bottom">
        <view class="justify-content-item">
          <view class="tn-padding-left-sm tn-flex tn-flex-col-center tn-flex-row-left" @click="tn('/pages/minePages/set')">
            <!-- 头像 -->
            <view class="logo-pic tn-shadow">
              <view class="logo-image">
                <view
                  class="tn-shadow-blur"
                  :style="{
                    backgroundImage: `url(${member.avatar})`,
                    width: '100rpx',
                    height: '100rpx',
                    backgroundSize: 'cover',
                  }"
                  @click="toLogin"
                >
                </view>
                <!-- <view class="tn-icon-logo-tuniao" style="font-size: 100rpx;color: #01BEFF;padding: 16rpx 0;"></view> -->
              </view>
            </view>
            <view class="tn-padding-left-sm tn-color-white">
              <view class="tn-padding-right tn-padding-left-sm tn-text-xl tn-text-bold"> {{ member.name }} </view>
              <view class="tn-padding-right tn-padding-top-xs tn-padding-left-sm tn-text-ellipsis">
                <text v-if="member.id && member.code" class="" style="opacity: 0.5">学员编号：{{ member.code }}</text>
                <text v-else-if="member.id && !member.tel" class="" style="opacity: 0.5">请点击头像绑定手机号码</text>
                <text v-else-if="member.id">学员编号：暂无</text>
              </view>
            </view>
          </view>
        </view>
      </view>

      <view class="tn-padding-bottom-sm tn-margin-left tn-margin-right" style="padding-top: 100rpx">
        <view class="tn-flex tn-flex-row-between tn-padding-bottom tn-bg-white">
          <view class="tn-padding-xs" @click="tn('/pages/minePages/order')">
            <view class="tn-flex tn-flex-direction-column tn-flex-row-center tn-flex-col-center">
              <view class="icon12__item--icon tn-flex tn-flex-row-center tn-flex-col-center">
                <view class="tn-icon-order-fill tn-color-wallpaper"></view>
              </view>
              <view class="tn-text-center">
                <text class="tn-text-ellipsis">全部订单</text>
              </view>
            </view>
          </view>
          <view class="tn-padding-xs" @click="tn('/pages/minePages/set')">
            <view class="tn-flex tn-flex-direction-column tn-flex-row-center tn-flex-col-center">
              <view class="icon12__item--icon tn-flex tn-flex-row-center tn-flex-col-center">
                <view class="tn-icon-identity-fill tn-color-wallpaper"></view>
              </view>
              <view class="tn-text-center">
                <text class="tn-text-ellipsis">个人信息</text>
              </view>
            </view>
          </view>
          <view class="tn-padding-xs" @click="tn('/pages/common/newsDetail?code='+memberRight)">
            <view class="tn-flex tn-flex-direction-column tn-flex-row-center tn-flex-col-center">
              <view class="icon12__item--icon tn-flex tn-flex-row-center tn-flex-col-center">
                <view class="tn-icon-vip-fill tn-color-wallpaper"></view>
              </view>
              <view class="tn-text-center">
                <text class="tn-text-ellipsis">会员权益</text>
              </view>
            </view>
          </view>
          <view class="tn-padding-xs" @click="tn('/pages/common/newsDetail?code='+commonQuestion)">
            <view class="tn-flex tn-flex-direction-column tn-flex-row-center tn-flex-col-center">
              <view class="icon12__item--icon tn-flex tn-flex-row-center tn-flex-col-center">
                <view class="tn-icon-help-fill tn-color-wallpaper"></view>
              </view>
              <view class="tn-text-center">
                <text class="tn-text-ellipsis">常见问题</text>
              </view>
            </view>
          </view>
        </view>
      </view>

      <view class="tn-strip-bottom"></view>

      <view class="tn-padding-top-sm tn-padding-bottom-sm">
        <tn-list-cell :hover="true" :unlined="true" :radius="true" :fontSize="30" @click="tn('/pages/minePages/myCourse')">
          <view class="tn-flex tn-flex-col-center">
            <view class="icon1__item--icon tn-flex tn-flex-row-center tn-flex-col-center" style="color: #1d2541">
              <view class="tn-icon-play"></view>
            </view>
            <view class="tn-margin-left-sm tn-flex-1">我的课程</view>
            <view class="tn-color-gray tn-icon-right"></view>
          </view>
        </tn-list-cell>
        <tn-list-cell
          :hover="true"
          :unlined="true"
          :radius="true"
          :fontSize="30"
          @click="handelMyCertificate()"
        >
          <view class="tn-flex tn-flex-col-center">
            <view class="icon1__item--icon tn-flex tn-flex-row-center tn-flex-col-center" style="color: #1d2541">
              <view class="tn-icon-honor"></view>
            </view>
            <view class="tn-margin-left-sm tn-flex-1">我的证书</view>
            <view class="tn-color-gray tn-icon-right"></view>
          </view>
        </tn-list-cell>
        <tn-list-cell :hover="true" :unlined="true" :radius="true" :fontSize="30" @click="handleOfflineTraining()">
          <view class="tn-flex tn-flex-col-center">
            <view class="icon1__item--icon tn-flex tn-flex-row-center tn-flex-col-center" style="color: #1d2541">
              <view class="tn-icon-bank"></view>
            </view>
            <view class="tn-margin-left-sm tn-flex-1">线下实训</view>
            <view class="tn-color-gray tn-icon-right"></view>
          </view>
        </tn-list-cell>
      </view>

      <view class="tn-strip-bottom"></view>

      <view class="tn-margin-bottom-lg tn-padding-top-sm tn-padding-bottom-sm">

        <tn-list-cell :hover="true" :unlined="true" :radius="true" :fontSize="30" @click="handleFeedback()">
          <button class="tn-flex tn-flex-col-center tn-button--clear-style" open-type="feedback">
            <view class="icon1__item--icon tn-flex tn-flex-row-center tn-flex-col-center" style="color: #1d2541">
              <view class="tn-icon-comment"></view>
            </view>
            <view class="tn-flex tn-flex-row-between" style="width: 100%">
              <view class="tn-margin-left-sm">意见反馈</view>
              <view class="tn-color-gray tn-icon-right"></view>
            </view>
          </button>
        </tn-list-cell>
        <tn-list-cell :hover="true" :unlined="true" :radius="true" :fontSize="30" @click="tn('/pages/common/newsDetail?code='+aboutUs)">
          <button class="tn-flex tn-flex-col-center tn-button--clear-style">
            <view class="icon1__item--icon tn-flex tn-flex-row-center tn-flex-col-center" style="color: #1d2541">
              <view class="tn-icon-tip"></view>
            </view>
            <view class="tn-flex tn-flex-row-between" style="width: 100%">
              <view class="tn-margin-left-sm">关于我们</view>
              <view class="tn-color-gray tn-icon-right"></view>
            </view>
          </button>
        </tn-list-cell>
        <tn-list-cell
          :hover="true"
          :unlined="true"
          :radius="true"
          :fontSize="30"
          @click="callPhoneNumber"
          data-number="18266666666"
        >
          <view class="tn-flex tn-flex-col-center">
            <view class="icon1__item--icon tn-flex tn-flex-row-center tn-flex-col-center" style="color: #1d2541">
              <view class="tn-icon-tel-circle"></view>
            </view>
            <view class="tn-margin-left-sm tn-flex-1">联系我们</view>
            <view
              class="tn-margin-left-sm tn-color-wallpaper tn-text-sm tn-padding-left-xs tn-padding-right-xs tn-bg-gray--light tn-round"
            >
              020-22012166</view
            >
          </view>
        </tn-list-cell>
      </view>
    </view>

    <view class="tn-text-center tn-margin-top-xl tn-padding-bottom-xl" style="opacity: 0.5">
      <view>
        <text class="">—</text>
        <text class="tn-padding-xs" style="color: #1d2541">广州投资顾问学院</text>
        <text class="">—</text>
      </view>
    </view>
    <tn-toast ref="toast"></tn-toast>
  </view>
</template>

<script>
export default {
  data() {
    return {
      member: {
        name: '登录',
        avatar: './static/avatar.jpg',
        tel: '',
        code: '',
        id: 0,
      },
      memberRight: 'memberRight',
      commonQuestion: 'commonQuestion',
      aboutUs: 'aboutUs',
    };
  },
  async mounted() {
    console.log('--------- mine mounted');
    let token = uni.getStorageSync(process.env.config.authToken);
    //console.log('token: ' + token);
    if (token) {
      //获取个人信息

      let res = await this.$tn.http.get('/fe/mp/memberInfo');
      //成功获取个人信息
      console.log('获取个人信息成功');
      this.member = res;
      uni.setStorageSync('member', this.member);
      if (!this.member.avatar) {
        this.member.avatar = './static/avatar.jpg';
      } else {
        this.member.avatar = process.env.config[process.env.NAME].imagePrefix + this.member.avatar;
      }
    }
  },
  methods: {
    toLogin() {
      let token = uni.getStorageSync(process.env.config.authToken);
      if (!token) {
        // 跳转至login页面
        console.log('toLogin');
        uni.redirectTo({
          url: '/pages/minePages/login',
        });
      } else if (!this.member.tel) {
        uni.redirectTo({
          url: '/pages/minePages/bindTel',
        });
      }
    },

    // 跳转
    tn(e) {
      uni.navigateTo({
        url: e,
      });
    },

    //拨打固定电话
    callPhoneNumber() {
      uni.makePhoneCall({
        phoneNumber: '020-22012166',
      });
    },

    handelMyCertificate() {
      this.$refs.toast.show({
        content: '功能开发中',
        icon: 'tip',
        image: '',
        duration: 1500,
      });
    },

    handleOfflineTraining() {
      this.$refs.toast.show({
        content: '功能开发中',
        icon: 'tip',
        image: '',
        duration: 1500,
      });
    },

    handleFeedback() {
      this.$refs.toast.show({
        content: '功能开发中',
        icon: 'tip',
        image: '',
        duration: 1500,
      });
    },
  },
};
</script>

<style lang="scss" scoped>
/* 底部安全边距 start*/
.tn-tabbar-height {
  min-height: 120rpx;
  height: calc(140rpx + env(safe-area-inset-bottom) / 2);
  height: calc(140rpx + constant(safe-area-inset-bottom));
}

.tn-color-wallpaper {
  color: #1d2541;
}

/* 顶部背景图 start */
.top-backgroup {
  //height: 475rpx;
  z-index: -1;

  .backgroud-image {
    width: 100%;
    //height: 475rpx;
    background-color: #1d2541;
    // z-index: -1;
  }
}

/* 顶部背景图 end */

/* 用户头像 start */

.logo-image {
  width: 100rpx;
  height: 100rpx;
  position: relative;
}

.logo-pic {
  background-size: cover;
  background-repeat: no-repeat;
  // background-attachment:fixed;
  box-shadow: 0px 0px 11px #b6cfff;
  border: 4rpx solid rgba(255, 255, 255, 0.85);
  background-position: top;
  border-radius: 50%;
  overflow: hidden;
  background-color: #ffffff;
}

/* 页面 start*/
.about-shadow {
  border-radius: 15rpx;
  box-shadow: 0rpx 0rpx 50rpx 0rpx rgba(0, 0, 0, 0.07);
}

.about {
  &__wrap {
    position: relative;
    z-index: 1;
    margin: 20rpx 30rpx;
    margin-top: -200rpx;
  }
}

/* 页面 end*/

/* 图标容器12 start */
.icon12 {
  &__item {
    width: 30%;
    background-color: #ffffff;
    border-radius: 10rpx;
    padding: 30rpx;
    margin: 20rpx 10rpx;
    transform: scale(1);
    transition: transform 0.3s linear;
    transform-origin: center center;

    &--icon {
      width: 100rpx;
      height: 100rpx;
      font-size: 40rpx;
      border-radius: 50%;
      border: 1rpx solid #e6e6e6;
      margin-bottom: 18rpx;
      position: relative;
      z-index: 1;
    }
  }
}

/* 图标容器1 start */
.icon1 {
  &__item {
    // width: 30%;
    background-color: #ffffff;
    border-radius: 10rpx;
    padding: 30rpx;
    margin: 20rpx 10rpx;
    transform: scale(1);
    transition: transform 0.3s linear;
    transform-origin: center center;

    &--icon {
      width: 50rpx;
      height: 50rpx;
      font-size: 40rpx;
      border-radius: 50%;
      position: relative;
      z-index: 1;
    }
  }
}

/* 图标容器1 end */

/* 间隔线 start*/
.tn-strip-bottom {
  width: 100%;
  border-bottom: 20rpx solid rgba(241, 241, 241, 0.8);
}
</style>
