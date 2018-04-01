package top.zhangx.gif.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.zhangx.gif.entity.Template;

import java.util.List;

@Mapper
public interface TemplateMapper {
    int addTemplate(Template template);
    List<Template> getAllTemplates();
    int getIdByName(String name);
    String getDisplayNameByName(String name);
}
