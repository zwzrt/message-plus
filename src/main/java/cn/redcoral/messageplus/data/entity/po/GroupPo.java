package cn.redcoral.messageplus.data.entity.po;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("mp_group")
public class GroupPo implements Serializable {
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
     * 用户ID列表（数据库存储字段）
     */
    private String clientIds;
    /**
     * 用户ID列表
     */
    @TableField(exist = false)
    private List<String> clientIdList;

    public GroupPo() {
        this.id = UUID.randomUUID().toString();
        this.userNum = 0;
        this.clientIdList = new ArrayList<>();
    }

    public void setClientIds(String clientIds) {
        this.clientIds = clientIds;
        this.clientIdList = JSON.parseArray(clientIds, String.class);
    }

    public void setClientIdList(List<String> clientIdList) {
        this.clientIdList = clientIdList;
        this.clientIds = JSON.toJSONString(clientIdList);
    }

    /**
     * 向用户ID列表添加ID
     * @param clientId 用户ID
     */
    public GroupPo addClientId(String clientId) {
        this.clientIdList.add(clientId);
        // 总人数增加
        this.userNum++;
        return this;
    }

    /**
     * 添加完成员后调用此方法。
     */
    public void ok() {
        this.clientIds = JSON.toJSONString(clientIdList);
    }

}
