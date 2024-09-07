package cn.redcoral.messageplus.manage;

import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 系统管理类
 * @author mo
 **/
@Configuration
public class SystemManage {
    /**
     * token用户名Map
     */
    private Map<String, String> tokenUsernameMap = new ConcurrentHashMap<>();

    public void put(String token, String username) {
        this.tokenUsernameMap.put(token, username);
    }

    public String get(String token) {
        return this.tokenUsernameMap.get(token);
    }

    public String remove(String token) {
        return this.tokenUsernameMap.remove(token);
    }
}
