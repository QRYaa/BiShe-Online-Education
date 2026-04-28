package cn.tgxy.tgbase.constant;

public interface BsCoreConstant {

	String REGEX_MOBILE="^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$";
	String REGEX_EMAIL="^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
	
    String TOKEN_HEADER = "Authorization";
    String SESSION_USER_KEY = "BsUser";
    String SESSION_TICKET_KEY = "Ticket";
    
    String TOKEN_PREFIX = "USER:TOKEN:";//
    String TICKET_PREFIX = "Authorization:login:Ticket:";
    long TICKET_EXPIRE_TIME = 300; // 300秒=5分
    
    String APP_SETTING_SSO_LOGOUT_URL_KEY = "SSO_LOGOUT_URL";
    int APP_TYPE_INTERNAL = 1;
    int APP_TYPE_EXTERNAL = 2;
    
    String STUDENT_TOKEN_HEADER = "studentAuthorization";
    String STUDENT_TOKEN_PREFIX = "STUDENT:TOKEN:";
    
    String STUDENT_WXOPENID_TOKEN_PREFIX = "STUDENT:WXOPENID:TOKEN:";
    String TEL_PREFIX = "TEL:";
    
    String CID_PREFIX = "BASE:CAPTCHA:";
    
    Long CAPTCHA_EXPIRE_TIME = 120L; // 验证码有效期 
    Long SMS_CODE_EXPIRE_TIME = 180L;//短信验证码有效期
    
    Long TOP_P_ID = 0L;

    //图片后缀
    String IMG = ".jpg.png.gif.bmp.jpeg";
    String IMG_FOLDER = "img";
    
    //音频后缀
    //String AUDIO = ".mp3.wma.wav.ape.flac.aac.ogg.m4a.amr";
    String AUDIO = ".mp3.m4a";
    String AUDIO_FOLDER = "audio";
    
    //视频后缀
    //String VIDEO = ".mp4.flv.f4v.webm";
    String VIDEO = ".mp4.mkv.rmvb";
    String VIDEO_FOLDER = "video";
    
    //默认参数
    String DEFAULT_PAGE_SIZE = "20";
    String DEFAULT_COUNT = "10";

    //任务调度
    String TASK_CLASS_NAME = "TASK_CLASS_NAME";
    String TASK_PROPERTIES = "TASK_PROPERTIES";
    //调度日志结果
    int RESULT_SUCCESS = 1;
    int RESULT_DEFEAT = 2;
    int RESULT_SUSPEND = 3;
    //调度状态
    int RUNNING=1;
    int WAIT_RUNNING=2;
    //应用日志类型
    int TYPE_PUSH=1;
    int TYPE_RECEIVE=2;
    int LOG_MESSAGE_LEN=255;
    int LOG_ERROR_LEN=255;
}
