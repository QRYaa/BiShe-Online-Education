<template>
  <view>
    <picker @change="PickerChange" :value="index" :range="dictOptions" :range-key="rangeKey">
      <view class="uni-input">
        <view>
          {{ index > -1 ? dictOptions[index][rangeKey] : '请选择' }}
        </view>
      </view>
    </picker>
  </view>
</template>

<script>
export default {
  name: 'dictSelect',
  props: {
    dictCode: String,
    value: [String, Number],
    label: String,
    need: {
      type: String,
      default: '0',
    }, //1 0是否必填
    rangeKey: {
      type: String,
      default: 'label',
    },
    valueKey: {
      type: String,
      default: 'value',
    },
    searchUrl: String,
  },
  data() {
    return {
      settingIndex: '',
      value: [],
      optionList: [],
    };
  },
  watch: {
    dictCode: {
      immediate: true,
      handler() {
        setTimeout(() => {
          this.initDictData();
        }, 500);
      },
    },
  },
  computed: {},
  data() {
    return {
      dictOptions: [],
      index: -1,
    };
  },
  methods: {
    initDictData() {
      //根据字典Code, 初始化字典数组

      let that = this;
      if (this.dictCode == 'sex') {
        that.dictOptions = [
          { label: '男', value: 1 },
          { label: '女', value: 2 },
        ];
        that.getIndex();
      } else if (this.dictCode == 'idCardType') {
        that.dictOptions = [
          { label: '居民身份证', value: 1 },
          { label: '港澳居民往来内地通行证', value: 2 },
		  { label: '台湾居民往来大陆通行证', value: 3 },
        ];
        that.getIndex();
      } else {
        this.$api.request('/sys/dict/getDictItems/' + that.dictCode, 'get', {}).then(function (res) {
          console.log('resres', res);
          if (res.success) {
            that.dictOptions = res.result;
            that.getIndex();
          } else {
            uni.showToast({
              title: res.message,
              icon: 'none',
            });
          }
        });
      }
    },
    PickerChange(e) {
      this.index = e.detail.value;
      if (this.index == -1) {
        this.index = 0;
        this.$emit('input', this.dictOptions[0][this.valueKey]);
      } else {
        this.$emit('input', this.dictOptions[this.index][this.valueKey]);
      }
    },
    getIndex() {
      for (var i = 0; i < this.dictOptions.length; i++) {
        if (this.dictOptions[i].value == this.value) {
          this.index = i;
          return;
        }
      }
      this.index = -1;
    },
  },
};
</script>

<style lang="scss" scoped>
.downSelect {
  width: 0;
  height: 0;
  border-right: 10rpx solid transparent;
  border-top: 15rpx solid #000;
  border-left: 10rpx solid transparent;
}

.imgbox {
  width: 100%;
}

.title {
  width: 100%;
  font-size: 35rpx;
  box-sizing: border-box;
  padding: 20rpx 30rpx;
  background: #f2f2f2;
  color: #5b5b5b;
}

.item-title {
  width: 100%;
  font-size: 30rpx;
  box-sizing: border-box;
  padding: 10rpx 30rpx;
}

.detail {
  width: 100%;
  box-sizing: border-box;
  padding: 0 15rpx;
}

.msg-item {
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  box-sizing: border-box;
  font-size: 28rpx;

  .msg-item-title {
    width: 100%;

    padding: 25rpx 0rpx;
    box-sizing: border-box;
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: flex-start;

    .msg-item-title-l {
      width: 50%;
      display: flex;
      flex-direction: row;
      align-items: center;
      justify-content: flex-start;
    }

    .msg-item-title-r {
      width: 50%;
      display: flex;
      flex-direction: row;
      align-items: center;
      justify-content: flex-end;

      button::after {
        border: none;
      }
    }

    .msg-item-title-r {
      width: 50%;
    }
  }

  .msg-input {
    width: 100%;
    box-sizing: border-box;
    background-color: #f7f7f7;
    border-radius: 8rpx;
    padding: 23rpx 30rpx;

    .uni-input {
      width: 100%;
      display: flex;
      flex-direction: row;
      align-items: center;
      justify-content: space-between;
    }
  }
}
</style>
