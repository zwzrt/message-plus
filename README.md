# 消息增强器（message-plus）

<a href='https://gitee.com/modmb/message-plus/stargazers'><img src='https://gitee.com/modmb/message-plus/badge/star.svg?theme=dark' alt='star'></img></a><a href='https://gitee.com/modmb/message-plus/members'><img src='https://gitee.com/modmb/message-plus/badge/fork.svg?theme=dark' alt='fork'></img></a>![DingTalk](https://img.shields.io/static/v1?label=Github&message=message-plus&color=orange)

---

#### [进入主页](https://zwzrt.github.io/)

#### 介绍

基于WebSocket的消息增强器，支持单发、群发及系统功能；支持集群架构的服务架构，以及数据持久化（持久化基于Redis实现，使用中请开发者注意Redis的持久化配置或重新去数据库做持久化）；支持失败消息的持久化及重发功能。

#### 软件架构
Maven + SpringBoot + WebSocket + Redis

#### 使用教程

1. Maven项目导入该依赖。

   ```xml
   <dependency>
       <groupId>io.github.zwzrt</groupId>
       <artifactId>message-plus</artifactId>
       <version>0.1.1-beta</version>
   </dependency>
   ```

2. 启动类添加@EnableMessagePlus来启动增强器。

   ```java
   @SpringBootApplication
   @EnableMessagePlus // 开启message增强
   public class ChatTestApplication {
       public static void main(String[] args) {
           SpringApplication.run(ChatTestApplication.class, args);
       }
   }
   ```

3. 实现MessagePlusBase接口

   ```java
   /**
    * 消息接收实现类
    * @author mo
    **/
   public interface MessagePlusBase {
       /**
        * 连接建立成功调用的方法
        * @return 有返回，则使用返回值作为标识，否则使用sid作为标识
        */
       public String onOpen(Session session, String sid);
       /**
        * 连接关闭调用的方法
        */
       public void onClose(String sid);
       /**
        * 收到消息时的权限校验
        * @param request HTTP请求信息
        * @param message 消息对象
        * @return 是否允许发送消息
        */
       public boolean onMessageCheck(HttpServerRequest request, Message message);
       /**
        * 收到系统消息
        * @param senderId 发送者ID
        * @param message 消息内容
        */
       public void onMessageBySystem(String senderId, String message);
   }
   ```
   
5. 在当前版本即时没有开启持久化的情况下也不在需要去调用MessagePlusUtils.createGroup(createUserId,  name,  client_ids)来创建、导入群组，你只需要实现GroupInterface接口即可（需要被Bean容器管理）。

6. 如果想要使用持久化功能（提前需要你的项目具有Redis），可以配置如下来打开：

   ```yml
   messageplus:
     serviceId: ... # 服务ID，需要唯一，为空时会自动生成
     persistence: true # 开启持久化
     message:
     	message-persistence: true # 消息持久化（默认开启，需要开启persistence才可以生效）
     	concurrent-number: 1 # 并发量（默认为1）
     	expiration-time: -1 # 消息持久化的过期时间
   ```

#### 使用说明

1.  如果你想要测试一下，可以去我的仓库中的message-plus-text拉取代码来测试。(https://github.com/zwzrt/message-plus-test.git、https://gitee.com/modmb/message-plus-test.git)
2.  如果使用过程出现bug或者存在不足，可以向red_coral20240606@163.com发送邮箱，我们将会积极修复并提供更强大的功能。
