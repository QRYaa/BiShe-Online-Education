package cn.tgxy.oledu.constant;

public interface OeConstant {
	
	Long EXPIRE_TIME = 3600*24L; // 默认过期时间，单位：秒
	
	String MEM_TOKEN_PREFIX = "MEM:TOKEN:";
	
	String TOKEN_HEADER="OlEduMemAuth";
	
	String MEM_AVATAR_FOLDER="memAvatar";
	
	String ID_CARD_FRONT_IMG_FOLDER="idCardFrontImg";
	
	String ID_CARD_BACK_IMG_FOLDER="idCardBackImg";
	
	String NOTE_IMG_FOLDER="noteImg";
	
	String BUCKET_LESSON= "tgxy-test-online-video";
	
	String BUCKET_PUB = "tgxy-test-pub";
	
	String BUCKET_PRI= "tgxy-test-pri";
	
	String REGEX_IDCARD="^\\d{15}|\\d{18}$";
	
	//banner的状态
	int BANNER_OUTDATED=1;//过期的
	int BANNER_VALID=2;//有效期的
	int BANNER_PENDING=3;//未到开始时间的
	
	//order支付状态
	int ORDER_PENDING=1;//待支付
	int ORDER_PAID=2;//已支付
	int ORDER_CANCELLED=3;//已取消
	int ORDER_PENDING_CONFIRM_PAYMENT=4;//待确认支付
	int ORDER_TIME_OUT=1800000;//订单超时时间
	int ORDER_MIDDLE_TIME_OUT=60000;//订单中间状态超时时间
	
	//orderItem的类型
	int ORDER_ITEM_OLCOURSE=1;//线上课程
	int ORDER_ITEM_QUANTITY=1;
	
	//观看状态
	int WATCHING = 2;//观看中
	int WATCHED = 3;//观看完
	
	double WATCHED_PERCENTAGE=0.8;//观看完百分比
	int WATCH_DURATION=10;//增加的观看持续时间
	
	//笔记状态
	int NOTE_PRI=1;
	int NOTE_PUB=2;
	int NOTE_DRAFT=3;
	
	// 审核资料状态
	int AUDIT_STATUS_PENDING=1;//待审核
	int AUDIT_STATUS_SUCCESS=2;//通过
	int AUDIT_STATUS_FAIL=3;//驳回
	
}
