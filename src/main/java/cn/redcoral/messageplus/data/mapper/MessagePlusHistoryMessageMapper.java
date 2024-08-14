package cn.redcoral.messageplus.data.mapper;

import cn.redcoral.messageplus.data.entity.po.HistoryMessagePo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 历史消息类
 * @author mo
 **/
@Mapper
public interface MessagePlusHistoryMessageMapper extends BaseMapper<HistoryMessagePo> {
}
