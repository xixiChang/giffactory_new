package top.zhangx.gif.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.zhangx.gif.entity.Sentence;

import java.util.List;

@Mapper
public interface SentenceMapper {
    int addSentences(List<Sentence> list);
    List<Sentence> getSentencesByTID(int template_id);
}
