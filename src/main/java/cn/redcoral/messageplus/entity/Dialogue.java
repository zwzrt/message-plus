package cn.redcoral.messageplus.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.websocket.Session;

/**
 * 对话
 * @author mo
 * @日期: 2024-05-26 11:55
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Dialogue {
    private Session session;

    public boolean isOnLine() {
        return session!=null;
    }
}
