package cn.redcoral.messageplus.data.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 用户黑名单
 * @author mo
 **/
@Data
@TableName("mp_user_blacklist")
public class UserBlacklistPo {
    private Long id;
    private String id1;
    private String id2;
}
