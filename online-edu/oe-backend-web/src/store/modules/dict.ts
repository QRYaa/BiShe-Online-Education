import { defineStore } from 'pinia';

import * as dictMgt from '@/api/system/dictMgt';

export const useDictStore = defineStore('dict', {
  state: () => ({
    init: false,
    dictMap: new Map(),
    dictItemMap: new Map(),
  }),
  getters: {},
  actions: {
    async loadAllDictionary() {
      const dictMap = new Map();
      const dictItemMap = new Map();
      const data = await dictMgt.loadAll();
      data.forEach((element: any) => {
        dictMap.set(element.code, element.items);
        const itemMap = new Map();
        element.items.forEach((item: any) => {
          itemMap.set(item.itemKey, item.itemValue);
        });
        dictItemMap.set(element.code, itemMap);
      });

      this.dictMap = dictMap;
      this.dictItemMap = dictItemMap;
      this.init = true;
    },

    getDictItem(code: string) {
      return this.dictMap.get(code);
    },
    getDictItemValue(code: string, itemKey: string) {
      const valueMap = this.dictItemMap.get(code);
      if (valueMap) {
        return valueMap.get(itemKey);
      }
      return itemKey;
    },
  },
});
