package cn.redcoral.messageplus.data.mapper;

import cn.redcoral.messageplus.data.entity.po.FailedMessagePo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 失败消息表
 * @author mo
 **/
@Mapper
public interface MessagePlusFailedMessageMapper extends BaseMapper<FailedMessagePo> {
}
