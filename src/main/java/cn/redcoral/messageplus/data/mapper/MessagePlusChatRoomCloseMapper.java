package cn.redcoral.messageplus.data.mapper;

import cn.redcoral.messageplus.data.entity.po.ChatRoomHistoryPo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author mo
 **/
@Mapper
public interface MessagePlusChatRoomCloseMapper extends BaseMapper<ChatRoomHistoryPo> {
}
