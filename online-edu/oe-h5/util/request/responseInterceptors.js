/**
 * 相应拦截
 * @param {Object} http 
 */
module.exports = (vm) => {
  uni.$tn.http.interceptors.response.use((response) => { // 可以使用async await 做异步操作
    const data = response.data
    // 自定义参数
    const custom = response.config?.custom
    // 服务端返回的状态码不等于200，则reject()
    if (data.code !== 200) {
      if(data.code == 401){
        //未登录用户或登录过期用户，清除token并跳转到登录页面
        uni.removeStorageSync(process.env.config.authToken);
        uni.removeStorageSync('member');
        uni.$tn.message.toast("登录会话已过期，正在跳转到登录页面");
        setTimeout(()=>{
          uni.redirectTo({url:"/pages/minePages/login"});
        },1000);
      }
      // 如果没有显式定义custom的toast参数为false的话，默认对报错进行toast弹出提示
      if (custom.toast !== false) {
        //uni.$tn.message.toast("错误码："+data.code)
      }
      return Promise.reject(data)
    }
    return data.data || {}
  }, (response) => { // 对响应错误做点什么 （statusCode !== 200）
    return Promise.reject(response)
  })
}