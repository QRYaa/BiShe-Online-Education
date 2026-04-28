package cn.tgxy.tgbase.common.response;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import cn.tgxy.tgbase.common.util.spring.SpringMessageUtils;


/**
 * @author Chris Deng
 * @Date 2024/01/16 11:41:04
 */
@JsonDeserialize
public class BsResponse {
	
	private int code;
	private String msg;
	private Object data;
	
	public BsResponse(int code, String msg, Object data) {
		super();
		this.code = code;
		this.msg = msg;
		this.data = data;
	}
	

	public static  BsResponse success() {
		return new BsResponse(HttpStatus.OK.value(), "Success", null);
	}
	public static  BsResponse success(Object data) {
		return new BsResponse(HttpStatus.OK.value(), "Success", data);
	}
	public static  BsResponse success(String message, Object data) {
		return new BsResponse(HttpStatus.OK.value(), message, data);
	}
	public static  BsResponse fail(String message, Object data) {
		return new BsResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), message, data);
	}
	public static  BsResponse fail(int code, String message, Object data) {
		return new BsResponse(code, message, data);
	}
	public static  BsResponse fail(int code, String message) {
		return new BsResponse(code, message, null);
	}
	public static  BsResponse fail(int code) {
		return new BsResponse(code, SpringMessageUtils.getMessage(code), null);
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
}
