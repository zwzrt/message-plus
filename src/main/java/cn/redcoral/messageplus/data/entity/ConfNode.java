package cn.redcoral.messageplus.data.entity;

import lombok.Data;

import java.util.List;

/**
 * 配置文件构建树节点
 * @author mo
 **/
@Data
public class ConfNode {
    private String key;
    private String name;
    private Object value;
    private List<ConfNode> nodeList;

    public ConfNode(String key, String name, Object value) {
        this.key = key;
        this.name = name;
        this.value = value;
    }

    public ConfNode(String key, List<ConfNode> nodeList) {
        this.key = key;
        this.nodeList = nodeList;
    }
}
