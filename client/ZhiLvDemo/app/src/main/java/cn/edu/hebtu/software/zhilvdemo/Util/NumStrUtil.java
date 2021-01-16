package cn.edu.hebtu.software.zhilvdemo.Util;
/**
 * @ProjectName:    MoJi
 * @Description:    获取数值
 * @Author:         张璐婷
 * @CreateDate:     2020/5/23 13:28
 * @Version:        1.0
 */
public class NumStrUtil {
    public  static String getNumStr(int count){
        String numStr = null;
        if(count < 10000){
            numStr = count + "";
        }else if (count < 100000){
            numStr = count/10000+ "." + count%10000/1000 +"万";
        }else{
            numStr = count/10000 + "万";
        }
        return numStr;
    }
}
