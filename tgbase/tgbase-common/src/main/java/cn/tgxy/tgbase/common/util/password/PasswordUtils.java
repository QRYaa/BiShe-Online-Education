package cn.tgxy.tgbase.common.util.password;

/**
 * 密码工具类
 */
public class PasswordUtils {

    private static PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();//默认构encoder，可以添加参数构造encoder

    /**
     * 加密
     * @param str 字符串
     * @return 返回加密字符串
     */
    public static String encode(String str) {
        return passwordEncoder.encode(str);
    }


    /**
     * 比较密码是否相等
     *
     * @param str      明文密码
     * @param password 加密后密码
     * @return true：成功    false：失败
     */
    public static boolean matches(String str, String password) {
        return passwordEncoder.matches(str, password);
    }
    
    public static void main(String[] args) {
    	//$2a$10$PcoIIToNOXGJEZmXZrnzsOkfau.RvqSTRH2HMK/GfFwJ4HWkn9Oga
    	//$2a$10$HAPCNEODMeTMzhqJ9S.TCOg5sAvIoZ8dG.Wy4zsNz9eDSrG8UtR5e
    	//$2a$10$SbgMfpjaB1SbmhupQgrssOLWwazrDH/dwgiMNLxlW3Jy1FEKQwQz2

		System.out.println(PasswordUtils.encode("admin"));
	}

}
