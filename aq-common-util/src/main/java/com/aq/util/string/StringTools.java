package com.aq.util.string;

/**
 * 字符串处理类
 *
 * @author 郑朋
 * @create 2017/10/24
 **/
public class StringTools {

    /**
     * 将银行卡号限前4后3中间用****填充
     * @param src
     * @return java.lang.String
     * @author 郑朋
     * @create 2017/10/24
     */
    public static String bankNoChange(String src) {
        if (src == null || src.trim().length() <= 0) {
            return "";
        }
        return src.substring(0, 4) + "****" + src.substring(src.length() - 3, src.length());
    }

    /**
     * 将真实姓名限前**后1个名字
     * @param src
     * @return java.lang.String
     * @author 郑朋
     * @create 2017/10/24
     */
    public static String realNameChange(String src) {

        if (src == null || src.trim().length() <= 0) {
            return "";
        }
        return "**" + src.charAt(src.length() - 1);
    }

    /**
     * @apiNote 隐藏手机号码中奖4位
     * @param telphone
     * @author wq
     * @return java.lang.String
     * @since  2017/12/12 15:40
     */
    public static String telphoneChange(String telphone) {
        if (telphone == null || telphone.trim().length() <= 0) {
            return "";
        }
        return telphone.replace(telphone.substring(4, 8), "****");
    }
    /**
     * 
    * @Title: objToString  
    * @Description: obj toString
    * @param @param obj
    * @param @return    参数  
    * @return String    返回类型  
    * @throws
     */
    public static String objToString(Object obj) {

		return null == obj ? "" : obj.toString();
	}
    /**
     * 
    * @Title: getKey  
    * @Description: TODO 
    * @param: @param strings
    * @param: @return
    * @return String
    * @author lijie
    * @throws
     */
    public static String getKey(String... strings) {
		StringBuilder sbud = new StringBuilder();
		for (int i = 0; i < strings.length; i++) {
			sbud.append(strings[i]);
		}
		return sbud.toString();
	}
	
}
