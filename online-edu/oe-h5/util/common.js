export const numToChinese = (num) => {
  const chineseNumbers = ['零', '一', '二', '三', '四', '五', '六', '七', '八', '九'];
  const units = ['', '十', '百', '千', '万'];
  if (num === 0) return chineseNumbers[0];
  let result = '';
  let unitIndex = 0;
  while (num > 0) {
    const digit = num % 10;
    if (digit !== 0) {
      result = chineseNumbers[digit] + units[unitIndex] + result;
    } else if (result.charAt(0) !== '零') {
      result = chineseNumbers[0] + result;
    }
    num = Math.floor(num / 10);
    unitIndex++;
  }
  return result.replace(/零+$/, ''); // 去除末尾的零
};

export const getSimpleText = (html) => {
  // 先将img标签替换为[图片]
  let imgReplaced = html.replace(/<img[^>]*>/g, '[图片]');
  
  // 再移除其他HTML标签
  let re1 = new RegExp('<.+?>', 'g');
  let msg = imgReplaced.replace(re1, '');
  if(msg.length > 255){
    msg = msg.substring(0, 255) + '...';
  }
  return msg;
};