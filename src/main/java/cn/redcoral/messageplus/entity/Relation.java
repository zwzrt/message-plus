package cn.redcoral.messageplus.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;

/**
 * 关系表
 * @author mo
 **/
@Data
@TableName("mp_relation")
public class Relation {
    private String id1;
    private String id2;
    private Timestamp createTime;
}
