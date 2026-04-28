<template>
  <view>
    <tn-nav-bar backTitle=" " backgroundColor="#ffffff">个人信息</tn-nav-bar>
    <view class="content" :style="{ paddingTop: vuex_custom_bar_height + 15 + 'px' }">
      <view class="formbox">
        <view class="msg-input">
          <view class="msg-input-l">
            <view class="text"> 头像 </view>
          </view>
          <view class="msg-input-c">
            <image
              v-if="detailObj.avatar"
              :src="imagePrefix + detailObj.avatar"
              mode=""
              @click="bindShowCroper"
            ></image>
            <image v-else src="@/static/avatar.jpg" mode="" @click="bindShowCroper"></image>
          </view>
          <view class="msg-input-r" @click="bindShowCroper">
            <text class="tn-icon-right" />
          </view>
        </view>

        <view class="msg-input" v-if="detailObj.code">
          <view class="msg-input-l">
            <image src="@/static/user/userNo.svg" mode=""></image>
            <view class="text" style="color: #4e5969"> 学员编号 </view>
          </view>
          <view class="msg-input-c">
            <input
              :disabled="true"
              v-model="detailObj.code"
              type="text"
              placeholder="暂无"
              placeholder-style="color:#999999;font-size:26rpx"
            />
          </view>
        </view>

        <view class="msg-input">
          <view class="msg-input-l">
            <image src="@/static/user/position.svg" mode=""></image>
            <view class="text"> 昵称 </view>
          </view>

          <view class="msg-input-c" @click="bindShowPopup(1)">
            <text v-if="detailObj.name">{{ detailObj.name }}</text>
            <text v-else style="color: #999999">请输入</text>
          </view>
          <view class="msg-input-r" @click="bindShowPopup(1)">
            <text class="tn-icon-right" />
          </view>
        </view>

        <view class="msg-input">
          <view class="msg-input-l">
            <text class="tn-icon-sex tn-padding-right-xs" style="font-weight: 500; font-size: 48rpx; color: #4e5969" />
            <view class="text"> 性别 </view>
          </view>
          <view class="msg-input-c">
            <dictSelect v-model="detailObj.gender" :dictCode="'sex'" @input="handleSexInput"></dictSelect>
          </view>
          <view class="msg-input-r">
            <text class="tn-icon-right" />
          </view>
        </view>

        <view class="msg-input">
          <view class="msg-input-l">
            <image src="@/static/user/phone.svg" mode=""></image>
            <view class="text"> 手机号 </view>
          </view>
          <view class="msg-input-c" @click="bindNavEditPhone">
            <text v-if="detailObj.tel">{{ detailObj.tel }}</text>
            <text v-else>完善手机号</text>
          </view>
          <view class="msg-input-r" @click="bindNavEditPhone()">
            <text class="tn-icon-right" />
          </view>
        </view>

        <view class="msg-input">
          <view class="msg-input-l">
            <image src="@/static/user/city.svg" mode=""></image>
            <view class="text"> 所在城市 </view>
          </view>
          <view class="msg-input-c" @click="bindSelectAddress">
            <text>{{ (detailObj.province || '请点击选择') + ' ' + (detailObj.city || '') }}</text>
          </view>
          <view class="msg-input-r" @click="bindSelectAddress">
            <text class="tn-icon-right" />
          </view>
        </view>
        <lotusAddress v-on:choseVal="choseValue" :lotusAddressData="lotusAddressData"></lotusAddress>
      </view>

      <view
        class="commonBox tn-flex tn-flex-row-between"
        style="margin-top: 20rpx"
        @click="handleClickRealNameAuthenticated()"
      >
        <view>账号实名信息</view>
        <tn-tag
          v-if="detailObj.realNameAuthenticated"
          shape="radius"
          backgroundColor="tn-bg-blue"
          fontColor="tn-color-white"
          >已实名</tn-tag
        >
        <text v-else class="tn-icon-right" />
      </view>

      <view class="btnbox">
        <view class="btn" @click="logout">
          <view class="btn-content"> 退出登录 </view>
        </view>
      </view>

      <view style="width: 100%; height: 120rpx"> </view>
    </view>

    <wyb-popup ref="formPopup" :zIndex="10" type="center" width="690" radius="16" :showCloseIcon="true">
      <view class="popup-content">
        <view class="popup-title"> 修改<text v-if="updateType == 1">昵称</text> </view>
        <view class="list-view" scroll-y="true">
          <view class="textbox">
            <input
              style="width: 100%"
              placeholder-style="color:#9AA3AE;font-size:32rpx;width: 100%;"
              placeholder="请输入"
              v-model="updateInput"
              auto-height
            />
          </view>
        </view>
        <view class="list-footer">
          <view class="footer-btn save" @click="bindConfirm"> 保存 </view>
        </view>
      </view>
    </wyb-popup>

    <qf-image-cropper ref="cropper" :width="256" :height="256" :radius="0" @crop="handleCrop"></qf-image-cropper>
  </view>
</template>

<script>
import dictSelect from '@/components/RlForm/dictSelect.vue';
import lotusAddress from '@/components/Winglau14-lotusAddress/Winglau14-lotusAddress.vue';
import wybPopup from '@/components/wyb-popup/wyb-popup.vue';
export default {
  components: {
    dictSelect,
    lotusAddress,
    wybPopup,
  },
  data() {
    return {
      imagePrefix: process.env.config[process.env.NAME].imagePrefix,
      detailObj: {
        avatar: '', //头像
        realname: '', //姓名
        phone: '',
        birthday: '',
      },
      lotusAddressData: {
        visible: false,
        provinceName: '',
        cityName: '',
        townName: '',
      },
      updateInput: '', //输入修改文字
      updateType: 0, //输入修改类型
    };
  },
  async onLoad(options) {
    await this.bindGetUserDetail();
  },
  methods: {
    bindShowCroper() {
      this.$refs.cropper.chooseImage(); // 显示
    },
    handleCrop(e) {
      this.$refs.cropper.close(); // 显示
      let that = this;
      let baseUrl = process.env.config[process.env.NAME].baseUrl;
      let authToken = uni.getStorageSync(process.env.config.authToken);
      uni.uploadFile({
        url: baseUrl + 'fe/mp/member/uploadImg',
        filePath: e.tempFilePath,
        name: 'file',
        header: {
          OlEduMemAuth: authToken,
        },
        success: async function (uploadRes) {
          console.log(uploadRes);

          uni.hideLoading();
          if (uploadRes.statusCode === 200) {
            let result = JSON.parse(uploadRes.data);
            if (result.code == 200) {
              let avatar = result.data;
              await that.$tn.http
                .post(
                  'fe/mp/member/updateAvatar',
                  { avatar: avatar },
                  {
                    header: {
                      'Content-Type': 'application/x-www-form-urlencoded',
                    },
                  },
                )
                .then((res) => {
                  uni.showToast({
                    title: '修改成功',
                  });
                  that.detailObj.avatar = avatar;
                });
            } else {
              uni.showToast({
                icon: 'none',
                title: result.msg,
              });
            }

            that.$forceUpdate();
          } else {
            uni.showToast({
              title: '网络错误 code=' + uploadRes.statusCode,
              icon: 'none',
            });
          }
        },
      });
    },
    bindShowPopup(type) {
      this.updateType = type;
      if (type == 1) {
        this.updateInput = this.detailObj.name;
        this.$refs.formPopup.show(); // 显示
      }
    },
    bindClosePopup() {
      this.$refs.formPopup.hide(); // 隐藏
    },
    bindNavEditPhone() {
      uni.navigateTo({
        url: '/pages/minePages/bindTel',
      });
    },
    logout() {
      const that = this;
      uni.showModal({
        title: '询问',
        content: '您确定要退出吗？',
        cancelText: '取消',
        confirmText: '确定',
        success: async (e) => {
          if (e.confirm) {
            await that.$tn.http.get('/fe/mp/logout').then((res) => {
              uni.removeStorageSync(process.env.config.authToken);
              uni.removeStorageSync('member');
              uni.reLaunch({
                url: '/pages/index/index',
              });
            });
          }
        },
      });
    },
    bindSelectAddress() {
      console.log('bindSelectAddress');
      this.lotusAddressData.visible = true;
    },
    async choseValue(res) {
      //res数据源包括已选省市区与省市区code
      this.lotusAddressData.visible = false; //visible为显示与关闭组件标识true显示false隐藏
      if (res.isChose && res.cityCode) {
        console.log(res);
        this.lotusAddressData.provinceName = res.provice; //省
        this.lotusAddressData.cityName = res.city; //市
        // this.lotusAddressData.townName = res.town; //区

        //赋值到addressData
        this.detailObj.province = res.provice;
        this.detailObj.city = res.city;
        this.detailObj.areaId = res.cityCode;
        // this.detailObj.county = res.town
        await this.bindConfirm();
      } else {
        uni.showToast({
          icon: 'none',
          title: '请选择城市',
        });
      }
    },

    async bindGetUserDetail() {
      const that = this;
      await this.$tn.http.get('/fe/mp/memberInfo').then(function (res) {
        let result = res;
        if (result.areaName) {
          let array = result.areaName.split(' ');
          console.log('array', array);
          if (array.length > 0) {
            result.province = array[0];
          }
          if (array.length > 1) {
            result.city = array[1];
          }
          // if(array.length>2){
          // 	result.county = array[1]
          // }
        } else {
          result.province = '';
          result.city = '';
          result.county = '';
        }
        that.detailObj = res;
        uni.setStorageSync('member', that.detailObj);
      });
    },

    async bindConfirm() {
      let that = this;

      let data = JSON.parse(JSON.stringify(this.detailObj));
      if (this.updateType == 1) {
        data.name = this.updateInput;
      }
      await that.$tn.http.put('/fe/mp/member/update', data).then(async (res) => {
        uni.showToast({
          title: '修改成功',
        });
        await that.bindGetUserDetail();
        that.bindClosePopup();
        that.updateType = 0;
        // setTimeout(() => {
        // 	uni.navigateBack()
        // }, 500)
        if (that.$api.prePage().loadData) {
          that.$api.prePage().loadData();
        }
      });
    },
    async handleSexInput(value) {
      this.detailObj.gender = value;
      await this.bindConfirm();
    },

    async handleClickRealNameAuthenticated() {
      await this.bindGetUserDetail();
      if (this.detailObj.realNameAuthenticated) {
        uni.navigateTo({
          url: '/pages/minePages/memberIdCard',
        });
      } else {
        uni.navigateTo({
          url: '/pages/minePages/memberIdCardAudit',
        });
      }
    },
  },
};
</script>

<style lang="scss">
page {
  background-color: #f2f6fa;
}

.popup-content {
  box-sizing: border-box;
  padding: 0rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;

  .popup-title {
    width: 100%;
    color: #1d2129;
    box-sizing: border-box;
    padding: 32rpx;
    text-align: center;
    position: relative;
    font-size: 36rpx;

    image {
      width: 34rpx;
      height: auto;
      position: absolute;
      right: 30rpx;
    }
  }

  .list-footer {
    width: 100%;
    height: 90rpx;
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: space-between;
    box-sizing: border-box;
    padding: 0 24rpx;
    font-size: 32rpx;
    margin-top: 48rpx;

    .footer-btn {
      width: 100%;
      height: 100%;
      display: flex;
      flex-direction: row;
      align-items: center;
      justify-content: center;
      border-radius: 48rpx;
      cursor: pointer;
    }

    .cancel {
      border: 1rpx solid #ccc;
    }

    .save {
      background: linear-gradient(to right, #6ebbd3, #1b5fb6);
      color: #fff;
    }
  }

  .list-view {
    width: 100%;
    box-sizing: border-box;
    padding: 0 32rpx;
    margin-top: 8rpx;

    .input-title {
      width: 100%;
      text-align: left;
      font-size: 30rpx;
      color: #1d2129;
      box-sizing: border-box;
      padding: 20rpx 0;
    }
  }

  .textbox {
    box-sizing: border-box;
    padding: 24rpx 32rpx;
    border-radius: 8rpx;
    background-color: #f7f7f7;
    font-size: 32rpx;
    color: #1d2129;
  }
}

.content {
  width: 100%;
  box-sizing: border-box;
  padding: 30rpx;
}

.uploadbox {
  width: 100%;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: flex-start;
  flex-wrap: wrap;
  margin-top: 20rpx;

  .c-noimg {
    width: 170rpx;
    height: 170rpx;
    background-color: #ededed;
    color: #333;
    font-size: 28rpx;
    border-radius: 20rpx;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    margin-right: 30rpx;
    margin-bottom: 30rpx;
    position: relative;

    .del {
      width: 30rpx;
      height: auto;
      font-size: 28rpx;
      position: absolute;
      z-index: 10;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      right: -20rpx;
      top: -12rpx;
    }

    image {
      width: 80rpx;
      height: 80rpx;
    }
  }
}

.logout-btnbox {
  width: 100%;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: center;
  color: #a95920;
  font-size: 30rpx;
  box-sizing: border-box;
  padding: 10rpx 25rpx;
}

.btnbox {
  width: 100%;
  box-sizing: border-box;
  margin-top: 32rpx;

  .btn {
    width: 100%;
    background: linear-gradient(to right, #7fc2d8, #256aba);
    border-radius: 64rpx;
    padding: 2rpx;
  }

  .btn-content {
    width: 100%;
    border-radius: 64rpx;
    background-color: #fff;
    padding: 22rpx 0;
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: center;
    color: #4ca1e2;
    font-size: 28rpx;
  }
}

.formbox {
  width: 100%;
  box-sizing: border-box;
  border-radius: 16rpx;
  background-color: #fff;
  box-shadow: 0rpx 4rpx 32rpx 0rpx rgba(69, 129, 219, 0.1);
  overflow: hidden;
  box-sizing: border-box;
  padding: 0 32rpx;

  .msg-input {
    width: 100%;
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: center;
    box-sizing: border-box;
    padding: 32rpx 0rpx;
    border-bottom: 1rpx solid #e5e6eb;
    background-color: #fff;
    font-size: 32rpx;

    .msg-input-l {
      display: flex;
      flex-direction: row;
      align-items: center;
      justify-content: flex-start;
      white-space: nowrap;

      image {
        width: 48rpx;
        height: 48rpx;
        margin-right: 16rpx;
      }

      .red {
        color: #f45f5f;
      }

      .text {
        width: 120rpx;
      }

      color: #1d2129;
    }

    .msg-input-c {
      flex: 1;
      color: #4e5969;
      display: flex;
      flex-direction: row;
      align-items: center;
      justify-content: flex-end;
      box-sizing: border-box;
      padding-right: 30rpx;
      text-align: right;

      input {
        width: 100%;
        text-align: right;
      }

      image {
        width: 90rpx;
        height: 90rpx;
        border-radius: 50%;
      }
    }

    .msg-input-r {
      image {
        width: 14rpx;
        height: auto;
      }
    }
  }

  .msg-input:last-child {
    border-bottom: none;
  }
}

.menubox {
  width: 100%;
  position: fixed;
  bottom: 0;
  left: 0;
  height: 98rpx;
  background-color: #fff;
  border-top: 1rpx solid #ebebeb;
  box-sizing: border-box;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: center;

  .menubox-item {
    width: 25%;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;

    image {
      width: 36rpx;
      height: 36rpx;
    }

    .text {
      margin-top: 7rpx;
      font-size: 20rpx;
      color: #999999;
    }

    .text-current {
      margin-top: 7rpx;
      font-size: 20rpx;
      color: #f45f5f;
    }
  }
}
.commonBox {
  width: 100%;
  border-radius: 16rpx;
  background-color: #fff;
  box-shadow: 0rpx 4rpx 32rpx 0rpx rgba(69, 129, 219, 0.1);
  padding: 32rpx 32rpx;
  color: #1d2129;
  font-size: 32rpx;
}
</style>
