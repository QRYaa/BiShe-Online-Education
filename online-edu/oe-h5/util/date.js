export const formatDuration = (duration) => {
  const hours = Math.floor(duration / 3600);
  const remainingSeconds = duration % 3600;
  const minutes = Math.floor(remainingSeconds / 60);
  const seconds = remainingSeconds % 60;

  if (hours > 0) {
    return `${String(hours).padStart(2, '0')}:${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}`;
  }
  return `${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}`;
};

export const formatDurationCN = (duration) => {
  const hours = Math.floor(duration / 3600);
  const remainingSeconds = duration % 3600;
  const minutes = Math.floor(remainingSeconds / 60);
  const seconds = remainingSeconds % 60;

  if (hours > 0) {
    return `${String(hours)}小时${String(minutes)}分${String(seconds)}秒`;
  }
  return `${String(minutes)}分${String(seconds)}秒`;
};

export const formatTime = (inputTime) => {
  if(inputTime == '刚刚') return inputTime;
  inputTime = inputTime.replace(/-/g, '/');
  const now = new Date();
  const input = new Date(inputTime);
  const diffMs = now - input;
  const diffMinutes = Math.floor(diffMs / (1000 * 60));
  const diffHours = Math.floor(diffMs / (1000 * 60 * 60));
  const diffDays = Math.floor(diffMs / (1000 * 60 * 60 * 24));

  if(diffMinutes < 1){
    return '刚刚';
  }
  else if (diffMinutes < 60) {
    return `${diffMinutes}分钟前`;
  } else if (diffHours < 24) {
    return `${diffHours}小时前`;
  } else if (diffDays === 1) {
    return `昨天 ${input.getHours().toString().padStart(2, '0')}:${input.getMinutes().toString().padStart(2, '0')}`;
  } else if (diffDays <= 3) {
    return `${diffDays}天前`;
  } else if (input.getFullYear() === now.getFullYear()) {
    return `${input.getMonth() + 1}月${input.getDate()}日`;
  } else {
    return `${input.getFullYear()}年${input.getMonth() + 1}月${input.getDate()}日`;
  }
};
