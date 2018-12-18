package com.example.alex.newtestproject.net;

/**
 * 对服务器返回的逻辑错误值进行封装
 */
public class ApiException extends RuntimeException {

    private static final int USER_NOT_EXIST = 100;
    private static final int WRONG_PASSWORD = 101;

    private int errorCode;

    public ApiException(String detailMessage) {
        super(detailMessage);
    }

    public ApiException(int resultCode) {
        this(resultCode, toApiExceptionMessage(resultCode));
    }

    public ApiException(int resultCode, String detailMessage) {
        super(detailMessage);
        this.errorCode = resultCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    /**
     * 映射服务器返回的自定义错误码，
     * （此时的http状态码在[200, 300) 之间）
     *
     * @param resultCode
     * @return
     */
    private static String toApiExceptionMessage(int resultCode) {
        String message;
        switch (resultCode) {
            case USER_NOT_EXIST:
                message = "该用户不存在";
                break;
            case WRONG_PASSWORD:
                message = "密码错误";
                break;
            default:
                message = "未知错误";
        }
        return message;
    }

}

