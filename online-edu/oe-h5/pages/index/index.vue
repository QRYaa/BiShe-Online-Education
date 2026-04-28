<template>
  <view class="start-index">
    <view v-if="tabberPageLoadFlag[0]" :style="{ display: currentIndex === 0 ? '' : 'none' }">
      <scroll-view class="custom-tabbar-page" scroll-y enable-back-to-top @scrolltolower="tabbarPageScrollLower">
        <home ref="home"></home>
      </scroll-view>
    </view>
    <view v-if="tabberPageLoadFlag[1]" :style="{ display: currentIndex === 1 ? '' : 'none' }">
      <scroll-view class="custom-tabbar-page" scroll-y enable-back-to-top @scrolltolower="tabbarPageScrollLower">
        <learn ref="learn"></learn>
      </scroll-view>
    </view>
    <view v-if="tabberPageLoadFlag[2]" :style="{ display: currentIndex === 2 ? '' : 'none' }">
      <scroll-view class="custom-tabbar-page" scroll-y enable-back-to-top @scrolltolower="tabbarPageScrollLower">
        <mine ref="mine"></mine>
      </scroll-view>
    </view>

    <tn-tabbar
      v-model="currentIndex"
      :list="tabbarList"
      activeColor="#838383"
      inactiveColor="#AAAAAA"
      activeIconColor="#3e4864"
      :animation="true"
      :safeAreaInsetBottom="true"
      @change="switchTabbar"
    ></tn-tabbar>
  </view>
</template>

<script>
import Home from './home.vue';
import Mine from './mine.vue';
import learn from './learn.vue';

export default {
  components: {
    Home,
    Mine,
    learn,
  },
  data() {
    return {
      // 底部tabbar菜单数据
      tabbarList: [
        {
          title: '首页',
          activeIcon: 'home-in',
          inactiveIcon: 'home-in',
        },
        {
          title: '学习',
          activeIcon: 'learn',
          inactiveIcon: 'learn',
        },
        {
          title: '我的',
          activeIcon: 'my-simple',
          inactiveIcon: 'my-simple',
        },
      ],
      // tabbar当前被选中的序号
      currentIndex: 0,
      // 自定义底栏对应页面的加载情况
      tabberPageLoadFlag: [],
    };
  },
  onLoad(options) {
    const index = Number(options.index || 0);
    // 根据底部tabbar菜单列表设置对应页面的加载情况
    this.tabberPageLoadFlag = this.tabbarList.map((item, tabbar_index) => {
      return index === tabbar_index;
    });
    this.switchTabbar(index);
  },
  onReachBottom() {
    uni.$emit('onReachBottom'); // 设置监听事件
  },
  methods: {
    // 切换导航
    switchTabbar(index) {
      this._switchTabbarPage(index);
    },

    // 导航页面滚动到底部
    tabbarPageScrollLower(e) {},

    // 切换导航页面
    _switchTabbarPage(index) {
      // wx.vibrateShort();
      const selectPageFlag = this.tabberPageLoadFlag[index];
      if (selectPageFlag === undefined) {
        return;
      }
      if (selectPageFlag === false) {
        this.tabberPageLoadFlag[index] = true;
      }
      this.currentIndex = index;
    },
  },
};
</script>

<style lang="scss" scoped></style>
