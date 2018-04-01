package top.zhangx.gif.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude (JsonInclude.Include.NON_NULL)
public class Template {
    @JsonIgnore
    private int id;
    private String name;
    private String display_name;
    private String uri;
    private String width;
    private String height;
}
