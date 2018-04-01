package top.zhangx.gif.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import top.zhangx.gif.constants.WebStatusInfo;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result {
    private int status = 0;
    private String msg = WebStatusInfo.MSG_OK;
    private Object result;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
