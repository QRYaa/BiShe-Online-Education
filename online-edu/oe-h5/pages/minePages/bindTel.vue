<template>
  <view class="card">
    <tn-nav-bar backTitle=" " backgroundColor="#f8f8f8">修改手机号</tn-nav-bar>
    <view style="padding: 0 30rpx">
      <view class="tn-flex tn-flex-row-center tn-flex-wrap" :style="{ paddingTop: vuex_custom_bar_height + 10 + 'px' }">
        <view style="width: 100%">
          <tn-form :model="form" ref="form" :errorType="['message']" labelPosition="left" :labelWidth="120">
            <tn-form-item label="手机号" prop="tel">
              <tn-input v-model="form.tel" />
            </tn-form-item>
            <tn-form-item label="验证码" prop="mobileSmsCode">
              <tn-input v-model="form.mobileSmsCode" />
              <view slot="right">
                <tn-button backgroundColor="#01BEFF" fontColor="tn-color-white" size="sm" @click="getCode">{{
                  tips
                }}</tn-button>
              </view>
            </tn-form-item>
            <tn-button
              width="100%"
              backgroundColor="tn-bg-blue"
              fontColor="tn-color-white"
              margin="30rpx 0 0 0"
              style="width: 100%"
              @click="submit"
              >提交</tn-button
            >
          </tn-form>
        </view>
      </view>
      <!-- verification-code -->
      <tn-verification-code
        ref="code"
        :keepRunning="keepRunning"
        :seconds="seconds"
        :startText="startText"
        :countDownText="countDownText"
        :endText="endText"
        @end="codeEnd"
        @start="codeStart"
        @change="codeChange"
      >
      </tn-verification-code>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      tips: '获取验证码',
      keepRunning: true,
      seconds: 60,
      startText: '获取验证码',
      countDownText: 's秒',
      endText: '重新获取',
      form: {
        tel: '',
        mobileSmsCode: '',
      },
      rules: {
        tel: [
          {
            required: true,
            pattern: '^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$',
            message: '注意手机号格式',
            trigger: 'change',
          },
        ],
        mobileSmsCode: [
          {
            required: true,
            message: '验证码不能为空',
            trigger: 'change',
          },
        ],
      },
    };
  },
  // 必须要在onReady生命周期，因为onLoad生命周期组件可能尚未创建完毕
  onReady() {
    this.$refs.form.setRules(this.rules);
  },
  methods: {
    submit() {
      this.$refs.form.validate(async (valid) => {
        if (valid) {
          //TODO: 错误处理
          await this.$tn.http.post('/fe/mp/bindTel', this.form).then((res) => {
            this.$tn.message.toast('绑定手机号成功，页面跳转中');
            setTimeout(() => {
              uni.redirectTo({ url: '/pages/index/index' });
            }, 1500);
          });
        }
      });
    },
    // 获取验证码
    getCode() {
      if (this.$refs.code.canGetCode) {
        this.$tn.message.loading('正在获取验证码');
        //防止点击过快导致发送多条验证码
        setTimeout(async () => {
          await this.$tn.http.post('/fe/mp/sendMobileSmsCode', this.form);
          this.$tn.message.closeLoading();
          this.$tn.message.toast('验证码已经发送');
          // 通知组件开始计时
          this.$refs.code.start();
        }, 1000);
      } else {
        this.$tn.message.toast(this.$refs.code.secNum + '秒后再重试');
      }
    },
    // 开始倒计时
    codeStart() {
      this.$tn.message.toast('短信已发送，请注意查收');
    },
    // 结束倒计时
    codeEnd() {},
    // 正在倒计时
    codeChange(event) {
      this.tips = event;
    },
  },
};
</script>

<style lang="scss" scoped>
.card {
  background-color: #ffffff;
}
</style>
