package cn.tgxy.oledu.constant;

public interface OeErrorConstant {

	//学员管理 37
	int MEMBER_STATUS_INVAL = 370101; // 学员被禁用
	int MEMBER_NOT_EXISTS=370102;//学员不存在
	int TEL_EMPTY = 370106;//电话不能为空
	int CODE_EXIST = 370301;//编号已存在
	int MEM_TEL_EXIST = 370302;//电话已存在
	int ID_CARD_EXIST = 370303;//身份证已存在
	
	// 学员端 38
	int MEMBER_LESSON_NOT_EXIST=380101;//未加入课程进行学习
	int MEMBER_WATCH_LESSON_TIME_NOT_ENOUGH=380102;//学习时间不足
	int LESSON_NOT_EXISTS=380103;//课节不存在
	int LESSON_NO_VIDEO=380104;//未找到视频
	
	int CONTAINS_SENSITIVE_WORDS=380105;//包含敏感词
	int NOTE_NOT_EXISTS=380106;//笔记不存在
	int REPLY_TRACK_NOT_EXISTS=380107;//缺少回复trackId
	int CONTENT_EMPTY=380108;//笔记内容为空
	
	int CONTAINS_PENDING_AUDIT=380201;//存在待审核的资料，不允许创建
	int IDCARD_FORMAT_ERROR=380202;//身份证格式错误
	int PARAM_NOT_EXISTS=380203;//参数不存在
}
