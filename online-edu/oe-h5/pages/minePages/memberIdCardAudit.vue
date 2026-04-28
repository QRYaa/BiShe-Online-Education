<template>
  <view>
    <tn-nav-bar backTitle=" " backgroundColor="#ffffff">上传证件</tn-nav-bar>
    <view class="content" :style="{ paddingTop: vuex_custom_bar_height + 15 + 'px' }">
      <view
        v-if="model.status"
        class="tn-text-lg tn-text-bold tn-flex tn-flex-row-center tn-flex-col-center"
        style="width: 100%; height: 70rpx"
      >
        <view :style="{ color: bindGetAuditStatus(model.status).color }">
          <text v-if="model.status == 1" class="tn-icon-tip tn-text-lg tn-padding-right-xs" />
          <text v-else-if="model.status == 2" class="tn-icon-success-circle tn-text-lg tn-padding-right-xs" />
          <text v-else-if="model.status == 3" class="tn-icon-close-circle tn-text-lg tn-padding-right-xs" />
          {{ bindGetAuditStatus(model.status).content }}
          <text v-if="model.status == 3" class="tn-padding-left">{{ model.opinion }}</text>
        </view>
      </view>
      <view class="cardBox">
        <view class="tn-flex tn-flex-col-center tn-padding-bottom" style="width: 100%">
          <view style="width: 30%">姓名</view>
          <view style="width: 65%">
            <tn-input
              v-model="model.idCardRealName"
              placeholder="请填写真实姓名"
              :clearable="false"
              :disabled="!canUpdate"
            />
          </view>
        </view>

        <view class="tn-flex tn-flex-col-center tn-padding-bottom">
          <view style="width: 30%">证件类型</view>
          <view style="width: 65%; padding: 10rpx 0">
            <dictSelect
              v-show="canUpdate"
              v-model="model.idCardType"
              :dictCode="'idCardType'"
              @input="handleIdCardTypeInput"
            ></dictSelect>
            <view v-show="!canUpdate">{{ bindGetIdCardType(model.idCardType) }}</view>
          </view>
          <view style="width: 5%">
            <text v-show="canUpdate" class="tn-icon-right" />
          </view>
        </view>

        <view class="tn-flex tn-flex-col-center tn-padding-bottom" style="width: 100%">
          <view style="width: 30%">证件号</view>
          <view style="width: 65%">
            <tn-input
              v-model="model.idCardNumber"
              placeholder="请填写真实证件号码"
              :clearable="false"
              :disabled="!canUpdate"
            />
          </view>
        </view>

        <view class="tn-flex tn-flex-col-center" style="width: 100%" @click="show = true">
          <view style="width: 30%">证件有效期至</view>
          <view style="width: 65%; padding: 10rpx 0">
            <view v-show="model.idCardExpireDate">{{ model.idCardExpireDate }}</view>
            <view v-show="!model.idCardExpireDate" class="tn-color-gray">请选择证件有效期</view>
          </view>
          <view style="width: 5%">
            <text v-show="canUpdate" class="tn-icon-right" />
          </view>
        </view>
      </view>

      <view class="cardBox" style="margin-top: 20rpx">
        <view>身份证正面</view>
        <view
          class="tn-margin-top tn-flex tn-flex-direction-column tn-flex-row-center tn-flex-col-center tn-color-gray--dark"
          style="width: 100%; height: 350rpx; border: 1px dashed #ddd"
        >
          <view
            v-show="!frontImg"
            class="tn-icon-add"
            style="font-size: 65rpx"
            @click="handleUploadFrontImage()"
          ></view>
          <view v-show="!frontImg" class="tn-padding-top-sm" @click="handleUploadFrontImage()">点击上传</view>

          <img v-show="frontImg" :src="frontImg" style="max-width: 600rpx; max-height: 300rpx" />
        </view>
        <view style="width: 100%" class="tn-margin-top tn-flex tn-flex-row-center tn-flex-col-center">
          <tn-button
            v-show="frontImg&&canUpdate"
            backgroundColor="tn-bg-red"
            fontColor="tn-color-white"
            width="100%"
            @click="handleDeleteFrontImage()"
            >删除图片
          </tn-button>
        </view>
      </view>

      <view class="cardBox" style="margin-top: 20rpx">
        <view>身份证反面</view>
        <view
          class="tn-margin-top tn-flex tn-flex-direction-column tn-flex-row-center tn-flex-col-center tn-color-gray--dark"
          style="width: 100%; height: 350rpx; border: 1px dashed #ddd"
        >
          <view v-show="!backImg" class="tn-icon-add" style="font-size: 65rpx" @click="handleUploadBackImage()"></view>
          <view v-show="!backImg" class="tn-padding-top-sm" @click="handleUploadBackImage()">点击上传</view>

          <img v-show="backImg" :src="backImg" style="max-width: 600rpx; max-height: 300rpx" />
        </view>
        <view style="width: 100%" class="tn-margin-top tn-flex tn-flex-row-center tn-flex-col-center">
          <tn-button
            v-show="backImg&&canUpdate"
            backgroundColor="tn-bg-red"
            fontColor="tn-color-white"
            width="100%"
            @click="handleDeleteBackImage()"
            >删除图片
          </tn-button>
        </view>
      </view>
    </view>

    <view v-if="canUpdate">
      <!-- 添加占位元素 -->
      <view class="tabbar-placeholder"></view>
      <view
        class="tabbar footerfixed tn-bg-blue tn-color-white tn-flex tn-flex-row-center tn-flex-col-center tn-text-xl"
        hover-class="tn-hover"
        :hover-stay-time="150"
        @click="handleSubmit()"
      >
        提交
      </view>
    </view>
    <tn-calendar v-model="show" mode="date" maxDate="2100-01-01" @change="handleCalendarChange"></tn-calendar>
  </view>
</template>
<script>
import { getAuditStatus, getIdCardType } from '@/util/dict.js';
import dictSelect from '@/components/RlForm/dictSelect.vue';
export default {
  components: {
    dictSelect,
  },
  data() {
    return {
      oss: process.env.config[process.env.NAME].oss,
      priImgPrefix: process.env.config[process.env.NAME].priImgPrefix,
      token: uni.getStorageSync(process.env.config.authToken),
      model: {
        idCardType: 1,
      },
      show: false,
      canUpdate: false,
      frontImg: '',
      backImg: '',
    };
  },
  async onLoad(options) {
    let data = await this.$tn.http.get('/fe/mp/member/getAudit');
    if (data.idCardType) {
      this.model = data;
      if (!this.oss) {
        this.frontImg = this.priImgPrefix + data.id + '&type=front' + '&token=' + this.token;
        this.backImg = this.priImgPrefix + data.id + '&type=back' + '&token=' + this.token;
      } else if(this.oss) {
        if(data.idCardFrontImageUrl){
          this.frontImg = this.priImgPrefix + data.idCardFrontImageUrl + '?token=' + this.token;
        }
        if(data.idCardBackImageUrl){
          this.backImg = this.priImgPrefix + data.idCardBackImageUrl + '?token=' + this.token;
        }
      }
    }
    if (!data.idCardType || data.status == 3) {
      this.canUpdate = true;
    }
  },
  methods: {
    bindGetAuditStatus(auditStatus) {
      return getAuditStatus(auditStatus);
    },
    bindGetIdCardType(idCardType) {
      return getIdCardType(idCardType);
    },
    handleIdCardTypeInput(value) {
      this.model.idCardType = value;
    },
    handleCalendarChange(date) {
      this.model.idCardExpireDate = date.year + '-' + date.month + '-' + date.day;
    },
    handleUploadFrontImage() {
      let that = this;
      let baseUrl = process.env.config[process.env.NAME].baseUrl;
      let authToken = uni.getStorageSync(process.env.config.authToken);
      uni.chooseImage({
        count: 1,
        success: (res) => {
          uni.uploadFile({
            url: baseUrl + 'fe/mp/member/uploadIdCardFrontImage',
            filePath: res.tempFilePaths[0],
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
                  that.model.idCardFrontImageUrl = result.data;
                  that.frontImg = res.tempFilePaths[0];
                } else {
                  uni.showToast({
                    icon: 'none',
                    title: result.msg,
                  });
                }
              } else {
                uni.showToast({
                  title: '网络错误 code=' + uploadRes.statusCode,
                  icon: 'none',
                });
              }
            },
          });
        },
      });
    },

    handleUploadBackImage() {
      let that = this;
      let baseUrl = process.env.config[process.env.NAME].baseUrl;
      let authToken = uni.getStorageSync(process.env.config.authToken);
      uni.chooseImage({
        count: 1,
        success: (res) => {
          uni.uploadFile({
            url: baseUrl + 'fe/mp/member/uploadIdCardBackImage',
            filePath: res.tempFilePaths[0],
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
                  that.model.idCardBackImageUrl = result.data;
                  that.backImg = res.tempFilePaths[0];
                } else {
                  uni.showToast({
                    icon: 'none',
                    title: result.msg,
                  });
                }
              } else {
                uni.showToast({
                  title: '网络错误 code=' + uploadRes.statusCode,
                  icon: 'none',
                });
              }
            },
          });
        },
      });
    },
    async handleSubmit() {
      if (
        !this.model.idCardType ||
        !this.model.idCardFrontImageUrl ||
        !this.model.idCardBackImageUrl ||
        !this.model.idCardRealName ||
        !this.model.idCardNumber ||
        !this.model.idCardExpireDate
      ) {
        uni.showToast({
          icon: 'none',
          title: '请填写完整',
        });
        return;
      }
      await this.$tn.http.post('/fe/mp/member/submitIdCardAudit', this.model);
      uni.showToast({
        title: '提交成功',
      });
      setTimeout(() => {
        uni.navigateBack({});
      }, 1000);
    },
    handleDeleteFrontImage() {
      this.model.idCardFrontImageUrl = '';
      this.frontImg = '';
    },
    handleDeleteBackImage() {
      this.model.idCardBackImageUrl = '';
      this.backImg = '';
    },
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
  border-top: 1rpx solid #f8f9fb;
}

.tabbar-placeholder {
  min-height: 100rpx;
  height: calc(100rpx + env(safe-area-inset-bottom) / 2); /* 与 tabbar 高度一致 */
}
.tabbar {
  padding: 0, 10rpx, calc(30rpx + env(safe-area-inset-bottom) / 2);
  min-height: 100rpx;
  height: calc(100rpx + env(safe-area-inset-bottom) / 2);
}
.footerfixed {
  position: fixed;
  width: 100%;
  bottom: 0;
  z-index: 999;
}
</style>
