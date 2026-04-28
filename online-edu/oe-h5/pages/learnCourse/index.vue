<template>
  <view>
    <!-- 顶部自定义导航 -->
    <tn-nav-bar fixed alpha customBack>
      <view slot="back" class="tn-custom-nav-bar__back" @click="goBack">
        <text class="icon tn-icon-left"></text>
        <text class="icon tn-icon-home-capsule-fill"></text>
      </view>
    </tn-nav-bar>
    <view class="image-wrapper top-fixed1">
      <video
        v-if="lessonId"
        class="image-content"
        :src="lessonVideo"
        :initial-time="lessonInitialTime"
        :direction="0"
        :autoplay="autoPlay"
        @play="handleLessonPlay"
        @ended="handleLessonEnd"
        @timeupdate="handleLessonTimeUpdate"
      ></video>
      <video v-else class="image-content"></video>
    </view>
    <view class="top-fixed2" style="margin-top: 56.25%">
      <tn-tabs
        :list="fixedList"
        :current="current"
        :isScroll="false"
        inactiveColor="#838383"
        activeColor="#3D7EFF"
        :bold="true"
        backgroundColor="#ffffff"
        :fontSize="32"
        :badgeOffset="[20, 50]"
        @change="tabChange"
      ></tn-tabs>
    </view>
    <view style="padding-top: calc(56.25% + 50px)">
      <view v-if="current === 0">
        <mp-html v-show="model.courseDto.content" class="" :content="model.courseDto.content" :selectable="false" />
        <view v-if="!model.courseDto.content" style="padding-top: 50rpx">
          <tn-empty mode="data"></tn-empty>
        </view>
      </view>
      <view v-else-if="current === 1 && model.courseDto.chapterDtoList">
        <view class="tn-padding-left tn-padding-right tn-padding-bottom" style="padding-top: 5rpx">
          <view class="tn-text-bold tn-text-xl">{{ model.courseDto.name }}</view>
          <view class="tn-padding-top-sm tn-color-blue" style="font-size: 30rpx" @click="scrollToRecentLesson()"
            >跳转到最近学习</view
          >
          <view class="tn-padding-top-sm tn-flex">
            <view class="tn-color-grey tn-text-sm"
              >已学{{ model.completionPercentage ? model.completionPercentage : 0 }}%</view
            >
            <view class="tn-color-grey tn-text-sm tn-padding-left">{{ model.courseDto.vlearningCount }}人已学习</view>
          </view>
        </view>
        <view v-if="!model.courseDto.chapterDtoList.length" style="padding-top: 50rpx"
          ><tn-empty mode="data"></tn-empty
        ></view>
        <tn-collapse :accordion="false" :headStyle="{ backgroundColor: '#F8F7F8' }" :bodyStyle="{ padding: 0 }">
          <tn-collapse-item
            v-for="(item, index) in model.courseDto.chapterDtoList"
            :key="index"
            :title="formChapterTitle(item, index)"
            :open="true"
          >
            <tn-list-view>
              <tn-list-cell
                v-for="(item2, index2) in item.lessonDtoList"
                :key="index2"
                :unlined="false"
                :lineLeft="false"
                :lineRight="false"
                @click="handleLessonClick(item2)"
                :class="{ 'tn-color-blue': lessonId === item2.id }"
              >
                <view class="message">
                  <view class="message__left">
                    <text class="tn-icon-video tn-text-xl" :class="{ grayText: lessonId !== item2.id }"></text>
                  </view>
                  <view class="message__middle">
                    <view class="message__name">{{ item2.name }}</view>
                    <tn-tag v-if="lessonId === item2.id" backgroundColor="#e5f3fe" fontColor="#1da2f1" :fontSize="24"
                      >最近学习</tn-tag
                    >
                  </view>
                  <view class="message__right tn-flex tn-flex-col-center">
                    <view class="message__time tn-padding-right-xs">{{ bindFormatDuration(item2.duration) }}</view>

                    <view
                      v-if="getWatchedStatus(item2.id) === 3"
                      class="tn-icon-success-circle tn-text-sm"
                      :class="{ grayText: lessonId !== item2.id }"
                    ></view>
                    <view
                      v-else-if="getWatchedStatus(item2.id) === 2"
                      class="pie"
                      :class="{ grayPie: lessonId !== item2.id, bluePie: lessonId === item2.id }"
                    ></view>
                  </view>
                </view>
              </tn-list-cell>
            </tn-list-view>
          </tn-collapse-item>
        </tn-collapse>
      </view>
      <view v-else-if="current === 2">
        <note-list :lessonId="lessonId" ref="noteListRef"></note-list>
      </view>
    </view>
    <!-- 添加占位元素 -->
    <view class="tabbar-placeholder"></view>
    <view class="tabbar footerfixed tn-bg-white">
      <!-- flex-row:justify-content,flex-col:align-items -->
      <view class="tn-flex tn-flex-row-between tn-flex-col-center tn-padding-top">
        <view class="tn-padding-left-xl">
          <view
            v-if="collect == 'true'"
            class="tn-icon-like-fill tn-text-xxl"
            style="color: #ff5a6b"
            @click="cancelCollect"
          ></view>
          <view v-else class="tn-icon-like tn-text-xxl" @click="collectCourse"></view>
          <view class="tn-text-sm">收藏</view>
        </view>
        <view class="tn-padding-right-xl" style="width: 80%">
          <tn-button width="100%" :plain="true" backgroundColor="#dbdbdb" @click="handleNoteLesson">
            <text class="tn-icon-edit-write" style="color: #727272; font-size: 50rpx"></text>
            <text
              hover-class="tn-hover"
              :hover-stay-time="150"
              class="tn-padding-left-xs"
              style="color: #757575; font-size: 30rpx"
              >记笔记</text
            >
          </tn-button>
        </view>
      </view>
    </view>
    <tn-toast ref="toast"></tn-toast>
    <tn-popup
      :zIndex="10"
      v-model="showPopup"
      mode="bottom"
      :maskCloseable="false"
      :mask="false"
      @close="showPopup = false"
    >
      <view class="" style="">
        <view class="tn-flex tn-flex-row-between tn-flex-col-center tn-strip-bottom-min" style="padding: 15rpx 30rpx">
          <view class="tn-text-bold">我的笔记</view>
          <view>
            <view style="display: inline-block" @click="handleCreateNote"
              ><tn-button fontColor="tn-color-blue">发布</tn-button></view
            >
            <view class="tn-padding-left" style="display: inline-block" @click="showPopup = false"
              ><tn-button backgroundColor="tn-bg-gray--light">退出</tn-button></view
            >
          </view>
        </view>
        <scroll-view :scroll-y="true" class="" style="padding: 30rpx; height: calc(100vh - 625rpx)">
          <editor
            id="editor"
            placeholder="期待您的笔记"
            style="height: 100%"
            @statuschange="onStatusChange"
            @ready="onEditorReady"
            @touchstart.stop
            @touchmove.stop
          >
          </editor>
        </scroll-view>
        <!-- 占位符 -->
        <view style="height: 110rpx" class="place-holder"></view>
        <view class="footerfixed tn-strip-top-min" style="padding: 15rpx 60rpx">
          <view class="tn-flex tn-flex-row-between tn-flex-col-center">
            <view style="width: 50%">
              <view class="tn-flex tn-flex-row-between tn-flex-col-center">
                <view style="font-size: 50rpx" :class="formats.bold ? 'ql-active' : ''" data-name="bold" @tap="format">
                  B
                </view>
                <view
                  style="font-size: 50rpx"
                  :class="formats.header === 1 ? 'ql-active' : ''"
                  data-name="header"
                  :data-value="1"
                  @tap="format"
                  >H</view
                >
                <view class="tn-icon-image" style="font-size: 60rpx" @tap="insertImage"></view>
              </view>
            </view>
            <view style="border-radius: 100rpx; background-color: #f7f8fa" @click="publicNote = !publicNote">
              <view v-if="publicNote" style="padding: 20rpx 40rpx" class="tn-flex tn-flex-col-center tn-color-blue">
                <text class="tn-icon-eye" style="font-size: 40rpx"></text>
                <text class="tn-padding-left-xs">公开</text>
              </view>
              <view v-else style="padding: 20rpx 40rpx" class="tn-flex tn-flex-col-center">
                <text class="tn-icon-eye-hide" style="font-size: 40rpx"></text>
                <text class="tn-padding-left-xs">私密</text>
              </view>
            </view>
          </view>
        </view>
      </view>
    </tn-popup>
  </view>
</template>

<script>
import { formatDuration } from '@/util/date.js';
import { numToChinese } from '@/util/common.js';
import noteList from './noteList.vue';
import template_page_mixin from '@/libs/mixin/template_page_mixin.js';
export default {
  mixins: [template_page_mixin],
  components: { noteList },
  data() {
    return {
      lessonVideoPrefix: process.env.config[process.env.NAME].lessonVideoPrefix,
      oss: process.env.config[process.env.NAME].oss,
      token: uni.getStorageSync(process.env.config.authToken),
      model: {
        courseDto: {
          chapterDtoList: [],
          content: '',
        },
      },
      memberLessonDataMap: new Map(),
      lessonId: '0',
      lessonInitialTime: 0,
      lessonVideo: '',
      fixedList: [{ name: '详情' }, { name: '目录' }, { name: '笔记' }],
      current: 1,
      collect: 'false',
      lessonTimeUpdateCount: 0,
      autoPlay: false,
      showPopup: false,
      formats: {},
      publicNote: true,
      editorCtx: null,
      noteDto: {
        lessonId: 0,
        content: null,
        status: 3,
      },
      member: uni.getStorageSync('member'),
    };
  },
  async onLoad(options) {
    if (options.code) {
      this.model = await this.$tn.http.get('/fe/mp/member/memberCourse/getDetail', {
        params: { courseCode: options.code },
      });
    } else if (options.id) {
      this.model = await this.$tn.http.get('/fe/mp/member/memberCourse/getDetail', {
        params: { courseId: options.id },
      });
    }

    this.collect = await this.$tn.http.get('/fe/mp/member/memberCollectCourse/existsOrNot', {
      params: { courseId: this.model.courseDto.id },
    });

    if (Array.isArray(this.model.memberLessonDtos)) {
      this.model.memberLessonDtos.forEach((item) => {
        this.memberLessonDataMap.set(item.lessonId, item);
      });
    }

    if (!this.model.lessonId) {
      this.lessonId = this.model.courseDto.chapterDtoList[0].lessonDtoList[0].id;
    } else {
      this.lessonId = this.model.lessonId;
      if (
        this.memberLessonDataMap.get(this.model.lessonId) != null &&
        this.memberLessonDataMap.get(this.model.lessonId).progress
      )
        this.lessonInitialTime = this.memberLessonDataMap.get(this.model.lessonId).progress;
    }

    if (this.oss) {
      let lessonPath = await this.$tn.http.get('/fe/mp/getLessonPathById', {
        params: { lessonId: this.lessonId },
      });
      this.lessonVideo = this.lessonVideoPrefix + lessonPath + '?token=' + this.token;
    } else {
      this.lessonVideo = this.lessonVideoPrefix + this.lessonId + '&token=' + this.token;
    }

    this.autoPlay = true;
  },

  onReachBottom() {
    uni.$emit('onReachBottom'); // 设置监听事件
  },

  methods: {
    scrollToRecentLesson() {
      const query = uni.createSelectorQuery().in(this);
      query
        .select('.tn-tag')
        .boundingClientRect((res) => {
          if (res && res.top) {
            uni.pageScrollTo({
              scrollTop: res.top - 305,
              duration: 300,
              success: () => {
                console.log('滚动到最近学习位置成功');
              },
            });
          }
        })
        .exec();
    },
    tabChange(index) {
      this.current = index;
    },
    bindFormatDuration(duration) {
      return formatDuration(duration);
    },

    bindNumToChinese(num) {
      return numToChinese(num);
    },
    formChapterTitle(item, index) {
      let sort = `第${this.bindNumToChinese(index + 1)}章： `;
      let num = '（共' + item.lessonDtoList.length + '节）';
      return sort + item.name + num;
    },

    async collectCourse() {
      this.collect = 'true';
      await this.$tn.http.post(
        '/fe/mp/member/memberCollectCourse/collectCourse',
        { courseId: this.model.courseDto.id },
        {
          header: {
            'Content-Type': 'application/x-www-form-urlencoded',
          },
        },
      );
      this.$refs.toast.show({
        content: '收藏成功',
        icon: 'success',
        image: '',
        duration: 1500,
      });
    },
    async cancelCollect() {
      this.collect = 'false';
      await this.$tn.http.post(
        '/fe/mp/member/memberCollectCourse/deleteCollectCourse',
        { courseId: this.model.courseDto.id },
        {
          header: {
            'Content-Type': 'application/x-www-form-urlencoded',
          },
        },
      );
      this.$refs.toast.show({
        content: '已取消收藏',
        icon: 'like-break',
        image: '',
        duration: 1500,
      });
    },

    getWatchedStatus(lessonId) {
      if (!this.memberLessonDataMap.get(lessonId)) return null;
      return this.memberLessonDataMap.get(lessonId).watchedStatus;
    },
    async handleLessonClick(item) {
      this.lessonId = item.id;
      if (this.memberLessonDataMap.get(this.lessonId) != null && this.memberLessonDataMap.get(this.lessonId).progress)
        this.lessonInitialTime = this.memberLessonDataMap.get(this.lessonId).progress;
      if (this.oss) {
        this.lessonVideo = this.lessonVideoPrefix + item.mediaUrl + '?token=' + this.token;
      } else {
        this.lessonVideo = this.lessonVideoPrefix + item.id + '&token=' + this.token;
      }
    },

    async handleLessonPlay() {
      await this.$tn.http.post(
        '/fe/mp/member/memberCourse/changeLessonId',
        {
          lessonId: this.lessonId,
          courseId: this.model.courseDto.id,
        },
        {
          header: {
            'Content-Type': 'application/x-www-form-urlencoded',
          },
        },
      );
      if (
        !this.memberLessonDataMap.get(this.lessonId) ||
        this.memberLessonDataMap.get(this.lessonId).watchedStatus === 1
      ) {
        this.memberLessonDataMap.set(this.lessonId, { watchedStatus: 2 });
        this.$forceUpdate();
        await this.$tn.http.post(
          '/fe/mp/member/memberLesson/changeWatchedStatus',
          {
            watchedStatus: 2,
            lessonId: this.lessonId,
          },
          {
            header: {
              'Content-Type': 'application/x-www-form-urlencoded',
            },
          },
        );

        await this.$tn.http.post(
          '/fe/mp/member/memberLesson/changeLastViewedTime',
          {
            lessonId: this.lessonId,
          },
          {
            header: {
              'Content-Type': 'application/x-www-form-urlencoded',
            },
          },
        );

        await this.$tn.http.post(
          '/fe/mp/member/memberCourse/changeLastViewTime',
          {
            courseId: this.model.courseDto.id,
          },
          {
            header: {
              'Content-Type': 'application/x-www-form-urlencoded',
            },
          },
        );
      }
    },

    async handleLessonEnd() {
      if (
        this.memberLessonDataMap.get(this.lessonId) &&
        this.memberLessonDataMap.get(this.lessonId).watchedStatus === 3
      ) {
        await this.$tn.http.post(
          '/fe/mp/member/memberLesson/changeProgress',
          {
            progress: 0,
            lessonId: this.lessonId,
          },
          {
            header: {
              'Content-Type': 'application/x-www-form-urlencoded',
            },
          },
        );
      }
      try {
        await this.$tn.http.post(
          '/fe/mp/member/memberLesson/changeWatchedStatus',
          {
            watchedStatus: 3,
            lessonId: this.lessonId,
          },
          {
            header: {
              'Content-Type': 'application/x-www-form-urlencoded',
            },
          },
        );
        this.memberLessonDataMap.set(this.lessonId, { watchedStatus: 3 });
        this.$forceUpdate();
        this.$refs.toast.show({
          content: '课程观看完成',
          icon: 'success',
          image: '',
          duration: 1500,
        });
        await this.$tn.http.post(
          '/fe/mp/member/memberLesson/changeProgress',
          {
            progress: 0,
            lessonId: this.lessonId,
          },
          {
            header: {
              'Content-Type': 'application/x-www-form-urlencoded',
            },
          },
        );
        await this.$tn.http.post(
          '/fe/mp/member/memberCourse/changeCompletionPercentage',
          {
            courseId: this.model.courseDto.id,
          },
          {
            header: {
              'Content-Type': 'application/x-www-form-urlencoded',
            },
          },
        );
      } catch (e) {
        if (e.code === 380102) {
          this.$refs.toast.show({
            content: '课程观看时间不足',
            icon: 'tip',
            image: '',
            duration: 1500,
          });
        }
      }
    },
    async handleLessonTimeUpdate(event) {
      this.lessonTimeUpdateCount += 1;
      if (event.detail.currentTime % 10 < 0.2) {
        await this.$tn.http.post(
          '/fe/mp/member/memberLesson/changeProgress',
          {
            progress: Math.floor(event.detail.currentTime),
            lessonId: this.lessonId,
          },
          {
            header: {
              'Content-Type': 'application/x-www-form-urlencoded',
            },
          },
        );
      }
      if (this.lessonTimeUpdateCount % 40 === 0) {
        await this.$tn.http.post(
          '/fe/mp/member/memberLesson/changeDuration',
          {
            lessonId: this.lessonId,
          },
          {
            header: {
              'Content-Type': 'application/x-www-form-urlencoded',
            },
          },
        );
      }
    },

    handleNoteLesson() {
      this.showPopup = true;
    },

    onStatusChange(e) {
      const formats = e.detail;
      this.formats = formats;
    },

    onEditorReady() {
      uni
        .createSelectorQuery()
        .select('#editor')
        .context((res) => {
          this.editorCtx = res.context;
        })
        .exec();
    },
    format(e) {
      let { name, value } = e.target.dataset;
      if (!name) return;
      // console.log('format', name, value)
      this.editorCtx.format(name, value);
    },
    insertImage() {
      uni.chooseImage({
        count: 1,
        success: (res) => {
          this.uploadFile(res.tempFilePaths[0]);
        },
      });
    },
    uploadFile(path) {
      let that = this;
      let baseUrl = process.env.config[process.env.NAME].baseUrl;
      let authToken = uni.getStorageSync(process.env.config.authToken);
      uni.uploadFile({
        url: baseUrl + 'fe/mp/member/uploadNoteImg',
        filePath: path,
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
              that.editorCtx.insertImage({
                src: result.data,
                alt: '图像',
                success: function () {
                  console.log('insert image success');
                },
              });
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
    async handleCreateNote() {
      let that = this;
      this.editorCtx.getContents({
        success: function (contents) {
          that.noteDto.content = contents.html;
        },
      });
      this.noteDto.lessonId = this.lessonId;
      this.noteDto.status = this.publicNote ? 2 : 1;
      try {
        let id = await this.$tn.http.post('/fe/mp/member/note/createNote', this.noteDto);
        this.showPopup = false;
        let tempNoteDto = { ...this.noteDto };
        tempNoteDto.memberId = this.member.id;
        tempNoteDto.memberName = this.member.name;
        tempNoteDto.memberAvatar = this.member.avatar;
        tempNoteDto.createTime = '刚刚';
        tempNoteDto.id = id;
        this.$refs.noteListRef.noteList.unshift(tempNoteDto);
        this.$refs.toast.show({
          content: '创建笔记成功',
          icon: 'success',
          image: '',
          duration: 1500,
        });
      } catch (e) {
        if (e.code === 380105) {
          this.$refs.toast.show({
            content: '包含敏感词，发表失败',
            icon: 'close',
            image: '',
            duration: 1500,
          });
        }
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.grayText {
  color: #aaaaaa;
}

.image-wrapper {
  width: 100%;
  padding-top: 56.25%; /* 16:9比例计算（9/16=56.25%） */
}

.image-content {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}

.top-fixed1 {
  position: fixed;
  top: 0;
  transition: all 0.25s ease-out;
  z-index: 100;
  width: 100%;
}

.top-fixed2 {
  position: fixed;
  top: 0;
  transition: all 0.25s ease-out;
  z-index: 1;
  width: 100%;
}

.rich-text-content {
  line-height: 1.6em;
  padding: 20rpx;
  width: 100%;
}

.message {
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-around;

  &__left {
    width: 10%;
  }
  &__middle {
    width: 75%;
    padding-left: 20rpx;
    padding-right: 40rpx;
  }
  &__right {
    width: 15%;
  }

  &__name {
    font-size: 32rpx;
    margin-bottom: 8rpx;
  }
  &__time {
    font-size: 24rpx;
  }
}
.pie {
  width: 20rpx;
  height: 20rpx;
  border-radius: 50%;
  border: 1rpx solid #aaaaaa;
  background-image: conic-gradient(#aaaaaa 0deg 120deg, white 90deg 360deg);
}
.grayPie {
  border: 1rpx solid #aaaaaa;
  background-image: conic-gradient(#aaaaaa 0deg 120deg, white 90deg 360deg);
}
.bluePie {
  border: 1rpx solid #3d7eff;
  background-image: conic-gradient(#3d7eff 0deg 120deg, white 90deg 360deg);
}
.tabbar-placeholder {
  min-height: 130rpx;
  height: calc(130rpx + env(safe-area-inset-bottom) / 2); /* 与 tabbar 高度一致 */
}
.tabbar {
  padding: 0, 10rpx, calc(30rpx + env(safe-area-inset-bottom) / 2);
  min-height: 130rpx;
  height: calc(130rpx + env(safe-area-inset-bottom) / 2);
}
.footerfixed {
  position: fixed;
  width: 100%;
  bottom: 0;
  z-index: 1;
}

.ql-active {
  color: #06c;
}
.tn-strip-top-min {
  width: 100%;
  border-top: 5rpx solid #f8f8f8;
}
.tn-strip-bottom-min {
  width: 100%;
  border-bottom: 5rpx solid #f8f8f8;
}
</style>
