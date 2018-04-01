package top.zhangx.gif.entity;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.sql.Timestamp;

@Setter
@Getter
public class AccessInfo {
    private int id;
    private Timestamp time;
    private String ua;
    private String ip;
    private String uri;
    private Date date;

//    public AccessInfo(String ua, String ip, String uri){
//        this.ua = ua;
//        this.ip = ip;
//        this.uri = uri;
//    }
}
