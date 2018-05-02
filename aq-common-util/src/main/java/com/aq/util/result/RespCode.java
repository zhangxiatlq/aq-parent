package com.aq.util.result;

/**
 * 返回抽象对象
 *
 * @author 郑朋
 * @create 2017/4/25
 **/
public interface RespCode {

    /**
     * 获取message
     * @param
     * @return java.lang.String
     * @author 郑朋
     * @create 2017/10/31
     */
    String getMsg();

    /**
     * 获取code
     * @param
     * @return java.lang.String
     * @author 郑朋
     * @create 2017/10/31
     */
    String getCode();


    enum Code implements RespCode {
        /**
         * 系统错误提示枚举值
         */
        SUCCESS("200","操作成功"),
        FAIL("300","操作失败"),
        INTERNAL_SERVER_ERROR("0405", "服务器内部错误"),
        PERMISSION_DENIED("10101","没有权限访问！"),
        REQUEST_DATA_ERROR("10100","请求参数不全"),
        REPETITION("10102", "当前操作未处理完，请稍后再试！"),
        ILLEGAL_OPTION("10103","请勿非法操作"),
        NOT_QUERY_DATA("10104","未查询到相关数据"),
        APP_ERROR("500","网络异常，请稍后重试"),
        NOT_LOGIN("10001","未登录");
    	
        private String code;
        private String msg;

        Code(String code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        @Override
        public String getMsg() {
            return msg;
        }

        @Override
        public String getCode() {
            return code;
        }
    }

}
