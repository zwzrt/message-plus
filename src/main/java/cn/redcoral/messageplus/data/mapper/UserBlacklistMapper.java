package cn.redcoral.messageplus.data.mapper;

import cn.redcoral.messageplus.data.entity.po.UserBlacklistPo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author mo
 **/
@Mapper
public interface UserBlacklistMapper extends BaseMapper<UserBlacklistPo> {
    long selectCountById1AndId2(@Param("id1") String id1, @Param("id2") String id2);
}
