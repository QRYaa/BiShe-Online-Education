package cn.tgxy.oledu.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import cn.tgxy.tgbase.service.BsAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FileSecurityInterceptor implements AsyncHandlerInterceptor{

    private static Logger log = LoggerFactory.getLogger(FileSecurityInterceptor.class);
    
    @Autowired
	private BsAuthService authService;
    
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	String requestURL=request.getServletPath();
    	log.debug(requestURL);
    	
		String token=request.getParameterValues("token")!=null&&request.getParameterValues("token").length>=1?request.getParameterValues("token")[0]:null;
		authService.checkToken(token);
		return true;
	}

}
