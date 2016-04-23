package com.janmee.stock.base;

/**
 * 业务状态码和业务信息，
 * 原则上，业务状态码按照顺序设计
 *
 * @author chenyonghui
 * @version 1.0
 * @since 1.0
 */
public enum StatusCode {

    /**
     * 请求成功
     */
    SUCCESS(200, "Success"),

    /**
     * 用户存在
     */
    USER_EXIST(703, "User Exist"),

    /**
     * 请求参数非法
     */
    REQUEST_PARAMS_NOT_VALID(40000, "Request Params Illegal"),

    /**
     * 请求次数太多
     */
    REQUEST_TIMES_TOO_MANY(40001, "Request Times Too Many"),

    /**
     * 请求的数据已存在
     */
    REQUEST_RESOURCE_EXIST(40002, "Request Resource Exist"),

    /**
     * 请求不合法
     */
    REQUEST_UNAUTHORIZED(40100, "Request Unauthorized"),

    /**
     * 错误凭证
     */
    INCORRECT_CREDENTIALS(40101, "Incorrect Credentials"),

    /**
     * 凭证过期
     */
    ACCESS_TOKEN_EXPIRES(40102, "Access Token Expires"),

    /**
     * 账号已锁定
     */
    USERNAME_HAVE_LOCKED(40103, "Username Have Locked"),

    /**
     * 账号不存在
     */
    USERNAME_NOT_FOUND(40104, "Username Not Found"),

    /**
     * 凭证无效
     */
    ACCESS_TOKEN__FAILURE(40105, "Access Token Failure"),

    /**
     * 当前用户未授权
     */
    CURRENT_USER_UNAUTHORIZED(40106, "Current User Unauthorized"),

    /**
     * CSRF随机数无效
     */
    CSRF_RANDOM__FAILURE(40107, "Csrf Random Failure"),

    /**
     * 请求资源不存在
     */
    RESOURCE_NOT_FOUND(40400, "Request Resource Not Found"),

    /**
     * 请求超时
     */
    REQUEST_TIME_EXPIRES_TIMEOUT(40800, "Request Time Expires Timeout"),

    /**
     * 服务器内部错误
     */
    INTERNAL_SERVER_ERROR(50000, "Internal Server Error or IllegalArgumentException"),

    /**
     * 用户已经学习过该课组的课程
     */
    STUDIED_LOG_EXIST(60000, "Already study course in this courseGroup"),

    /**
     * 不存在该课组uid
     */
    COURSE_GROUP_NOT_FOUND(60100, "CourseGroup not found"),

    /**
     * 课程创建失败，直播课开始结束时间不能为空
     */
    LIVE_COURSE_TIME_NULL(60200, "Live Course start or end time is null");


    private final int statusCode;

    private final String message;

    private StatusCode(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    /**
     * 通过业务状态码获取信息
     *
     * @param statusCode
     * @return
     */
    public static String getMessage(int statusCode) {
        String message = "";
        for (StatusCode code : StatusCode.values()) {
            if (code.getStatusCode() == statusCode) {
                message = code.getMessage();
                break;
            }
        }
        return message;
    }
}
