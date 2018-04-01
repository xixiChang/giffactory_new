package top.zhangx.gif.mapper;

import top.zhangx.gif.entity.AccessInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AccessInfoMapper {
    int addAccessInfo(AccessInfo accessInfo);
    List<AccessInfo> getAllAccessInfo();
    List<AccessInfo> getDayAccessInfo();
}
