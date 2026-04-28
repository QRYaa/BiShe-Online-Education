<template>
  <view> </view>
</template>
<script>
export default {
  data() {
    return {
      loginUrl: process.env.config[process.env.NAME].loginUrl,
    };
  },
  methods: {},
  async onLoad(options) {
    console.log('login mounted');

    let code = options.code;
    if (!code&&process.env.config[process.env.NAME].appid) {
      let url = process.env.config.authorizeUrl
        .replace('###APPID###', process.env.config[process.env.NAME].appid)
        .replace('###REURI###', process.env.config[process.env.NAME].redirect_uri);
      location.replace(url);
      //页面跳转，然后重新加载该页面
    } else {
      //调用后端登录接口
      console.log('调用后端登录接口');
      let { token, hasTel } = await this.$tn.http.post(
        this.loginUrl,
        {
          code: code,
          userInfo: false,
        },
        {
          header: {
            'Content-Type': 'application/x-www-form-urlencoded',
          },
        },
      );
      uni.setStorageSync(process.env.config.authToken, token);
      //没有绑定手机号,需要跳转到绑定手机号页面
      if (!hasTel)
        uni.redirectTo({
          url: '/pages/minePages/bindTel',
        });
      else {
        this.$tn.message.toast('登录成功，页面跳转中');
        setTimeout(() => {
          uni.redirectTo({ url: '/pages/index/index' });
        }, 1500);
      }
    }
  },
};
</script>
