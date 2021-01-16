package cn.edu.hebtu.software.zhilvdemo.Util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ProjectName:    ZhiLv
 * @Description:    判断字符串是否符合规则
 * @Author:         张璐婷
 * @CreateDate:     2021/1/11  18:46
 * @Version:        1.0
 */
public class JudgeStrUtil {
    public static boolean isPhone(String str){
        Pattern pattern = Pattern.compile("((^(13|15|18)[0-9]{9}$)|(^0[1,2]{1}\\d{1}-?\\d{8}$)|(^0[3-9] {1}\\d{2}-?\\d{7,8}$)|(^0[1,2]{1}\\d{1}-?\\d{8}-(\\d{1,4})$)|(^0[3-9]{1}\\d{2}-?\\d{7,8}-(\\d{1,4})$))");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public static boolean isEmail(String str){
        Pattern pattern = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
        Matcher mc = pattern.matcher(str);
        return mc.matches();
    }

}
