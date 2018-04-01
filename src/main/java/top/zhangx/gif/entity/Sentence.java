package top.zhangx.gif.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Sentence {
    @JsonIgnore
    private int id;
    @JsonIgnore
    private int template_id;
    private String person;
    private String default_value;
}
