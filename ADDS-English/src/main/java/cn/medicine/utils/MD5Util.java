package cn.medicine.utils;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
/**
 * 
 * @ClassName:MD5Util
 * @Description: 加密工具，提供普通md5加密和盐值md5加密
 * @Function List:TODO 主要函数及其功能
 *
 * @author   Administrator
 * @version  
 * @Date	 2016-8-14下午2:44:43
 *
 * @History:// 历史修改记录 
 * <author>  // 修改人
 * <time>    // 修改时间
 * <version> // 版本
 * <desc>    // 描述修改内容
 */
public class MD5Util {
    private static Md5PasswordEncoder encoder=new Md5PasswordEncoder();
    /**
     * 
     * @Function:     md5 
     * @Description:   不带盐值的md5加密  
     *                 <功能详细描述>
     *
     * @param str
     * @return
     */
    public static String md5(String str){
        return md5(str,null);
    }
    /**
     * 
     * @Function:     md5 
     * @Description:   带盐值的md5加密  
     *                 <功能详细描述>
     *
     * @param str
     * @param salt
     * @return
     */
    public static String md5(String str,String salt){
        return encoder.encodePassword(str, salt);
    }

}
