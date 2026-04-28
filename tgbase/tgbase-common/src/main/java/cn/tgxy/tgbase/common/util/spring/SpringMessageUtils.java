package cn.tgxy.tgbase.common.util.spring;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * @author chris deng
 * @Date 2024/01/16 14:26:15
 */
public class SpringMessageUtils {

    private static MessageSource messageSource;

    static {
        messageSource = (MessageSource) SpringContextUtils.getBean("messageSource");
    }

    public static String getMessage(int code) {
        return getMessage(code, new String[0]);
    }

    public static String getMessage(int code, String... params) {
        return messageSource.getMessage(code + "", params, LocaleContextHolder.getLocale());
    }
}
