const { get } = require("lodash");

const orderStatusArray = [
  // { value: '1',title: '等待学员付款', color: '#FFA726' },
  // { value: '2',title: '已完成', color: '#31E749' },
  // { value: '3',title: '已取消', color: '#E83A30' },
  { value: '1',title: '等待学员付款', color: '#3D7EFF' },
  { value: '2',title: '已完成', color: '#3D7EFF' },
  { value: '3',title: '已取消', color: '#3D7EFF' },
];

function getOrderStatusBgColor(paymentStatus) {
  if(paymentStatus===1) return orderStatusArray[0];
  else if(paymentStatus===2) return orderStatusArray[1];
  else if(paymentStatus===3) return orderStatusArray[2];
  return orderStatusArray[2];
}

const paymentMethodArray = [
  { value: '1',title: '微信支付'},
  { value: '2',title: '支付宝'},
];

function getPaymentMethod(paymentMethod) {
  if(paymentMethod===1) return paymentMethodArray[0];
  else if(paymentMethod===2) return paymentMethodArray[1];
  return paymentMethodArray[0];
}

const watchedStatusArray = [
  { value: '1',title: '未观看' },
  { value: '2',title: '观看中'},
  { value: '3',title: '观看完' },
];

function getWatchedStatus(watchedStatus) {
  if(watchedStatus===1) return watchedStatusArray[0];
  else if(watchedStatus===2) return watchedStatusArray[1];
  else if(watchedStatus===3) return watchedStatusArray[2];
  return watchedStatusArray[0];
}

const noteStatusArray = [
  { value: '1',title: '私有' },
  { value: '2',title: '公开'},
  { value: '3',title: '草稿' },
];

function getNoteStatus(noteStatus) {
  if(noteStatus===1) return noteStatusArray[0];
  else if(noteStatus===2) return noteStatusArray[1];
  else if(noteStatus===3) return noteStatusArray[2];
  return noteStatusArray[2];
}

const genderArray = [
  { value: '0',title: '未知' },
  { value: '1',title: '男'},
  { value: '2',title: '女' },
];
function getGender(gender) {
  if(gender===0) return genderArray[0];
  else if(gender===1) return genderArray[1];
  else if(gender===2) return genderArray[2];
  return genderArray[0];
}

const idCardTypeMap=new Map();
idCardTypeMap.set(1, '居民身份证');
idCardTypeMap.set(2, '港澳居民往来内地通行证');
idCardTypeMap.set(3, '台湾居民往来大陆通行证');

function getIdCardType(idCardType) {
  return idCardTypeMap.get(idCardType);
}

const auditStatusMap=new Map();
auditStatusMap.set(1, {content:'待审核',color:'#e37318'});
auditStatusMap.set(2, {content:'审核通过',color:'#31E749'});
auditStatusMap.set(3, {content:'审核驳回',color:'#E83A30'});

function getAuditStatus(auditStatus) {
  return auditStatusMap.get(auditStatus);
}

module.exports = {
	getOrderStatusBgColor:getOrderStatusBgColor,
  getPaymentMethod:getPaymentMethod,
  getWatchedStatus:getWatchedStatus,
  getNoteStatus:getNoteStatus,
  getGender:getGender,
  getIdCardType:getIdCardType,
  getAuditStatus:getAuditStatus,
}

