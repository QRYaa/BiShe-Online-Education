package cn.tgxy.tgbase.constant;

public interface BsConstant {
	
	//通用状态，可用/不可用
	int VALID = 1;
	int INVALID = 0;
	
	Long TOP_P_ID = -1L; // 默认树状结构顶级P_ID
	
	Long EXPIRE_TIME = 3600*24L; // 默认过期时间，单位：秒
	
	String BUCKET_PUB = "pub";
	String BUCKET_PRI = "pri";
	String BUCKET_TMP = "tmp";
	    
}
