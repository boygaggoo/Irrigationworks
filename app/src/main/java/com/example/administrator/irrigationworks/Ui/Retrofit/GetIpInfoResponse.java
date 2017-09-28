package com.example.administrator.irrigationworks.Ui.Retrofit;

/**
 * Created by Administrator on 2017/8/18.
 */
public class GetIpInfoResponse  {
    private String message;



    private String code;

    public GetIpInfoResponse() {
    }

    public GetIpInfoResponse(String message, String code) {
        this.message = message;
        this.code = code;
    }

    @Override
    public String toString() {
        return "GetIpInfoResponse{" +
                "message='" + message + '\'' +
                ", code='" + code + '\'' +
                '}';
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
