package cn.redcoral.messageplus.data.entity.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 群组
 * @author mo
 **/
@Data
public class Group implements Serializable {
    /**
     * 群组ID
     */
    private String id;
    /**
     * 创建者ID
     */
    private String createUserId;
    /**
     * 群组名称
     */
    private String name;
    /**
     * 用户数量
     */
    private int userNum;
    /**
     * 用户ID列表
     */
    private List<String> clientIdList;

    public Group() {
        this.id = UUID.randomUUID().toString();
        this.userNum = 0;
        this.clientIdList = new ArrayList<>();
    }

}
