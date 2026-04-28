package cn.tgxy.oledu.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.tgxy.tgbase.common.exception.ServiceException;
import cn.tgxy.tgbase.common.response.BsResponse;

/**
 * 全局异常处理
 * @author chrisdeng
 * 2021-08-12
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 服务异常
     */
	@ResponseBody
    @ExceptionHandler(value = {ServiceException.class})
    public BsResponse serviceException(ServiceException exception) {
        log.debug("ServiceException", exception);
        return BsResponse.fail(exception.getCode());
    }


    /**
     * 基本异常
     */
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public BsResponse exception(Exception exception) {
        log.error(exception.getMessage(), exception);
        return BsResponse.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
    }


}
