package cn.tgxy.tgbase.common.exception;


import cn.tgxy.tgbase.common.util.spring.SpringMessageUtils;

/**
 * @author Chris Deng
 * @Date 2024/03/08 16:38:30
 */
public class ServiceException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int code;
    private String message;

    public ServiceException(int errorCode, String errorMsg) {
        this.code = errorCode;
        this.message = errorMsg;
    }

    public ServiceException(int code) {
        this.code = code;
        this.message = SpringMessageUtils.getMessage(code);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
