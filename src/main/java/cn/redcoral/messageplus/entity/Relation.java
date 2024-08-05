package cn.redcoral.messageplus.entity;

import lombok.Data;

import java.sql.Timestamp;

/**
 * 关系表
 * @author mo
 **/
@Data
public class Relation {
    private String id1;
    private String id2;
    private Timestamp createTime;
}
