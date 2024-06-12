package cn.redcoral.messageplus.initialize;

import cn.redcoral.messageplus.entity.Group;
import cn.redcoral.messageplus.utils.GroupManage;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

import static cn.redcoral.messageplus.utils.GroupManage.GROUP_KEY;

/**
 * 持久化功能的初始化
 * @author mo
 * @日期: 2024-06-11 15:11
 **/
@Slf4j
@Aspect
@Component
public class MessagePersistenceInitialize {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 创建群组的AOP增强
     */
    @Pointcut("execution(public cn.redcoral.messageplus.entity.Group cn.redcoral.messageplus.utils.GroupManage.createGroup(String, String, java.util.List))")
    void createGroup() {}
    /**
     * 环绕通知
     */
    @Around("createGroup()")
    public Group around(ProceedingJoinPoint pj) throws Throwable {
        Object[] args = pj.getArgs();
        String createUserId = (String) args[0];
        String name = (String) args[1];
        // 查询该数组是否存在
        String value = stringRedisTemplate.opsForValue().get(GROUP_KEY+createUserId+":"+name);
        Group group = null;
        // 该群组不存在，进行创建
        if (value == null) {
            // 调用原始方法
            group = (Group) pj.proceed();
            // 添加群组缓存（用于判断是否存在）
            stringRedisTemplate.opsForValue().set(GROUP_KEY+createUserId+":"+name, group.getId());
            // 添加群组信息缓存
            stringRedisTemplate.opsForValue().set(GROUP_KEY +group.getId(), JSON.toJSONString(group));
            // 返回群组信息
            return group;
        }
        group = new Group();
        group.setId(value);
        return group;
    }
}
