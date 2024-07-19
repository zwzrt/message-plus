# 消息增强器（message-plus）

---

### 语言（Language）

- [中文](#中文)
- [English](#English)

---

### 中文

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
       <version>0.0.6-beta</version>
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
   @Service
   public class MyMessagePlusBase implements MessagePlusBase {
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
        * @param sendId 发生者ID
        * @return 是否允许发送消息
        */
       public boolean onMessageCheck(HttpServerRequest request, String sendId);
       /**
        * 收到系统类型的消息后调用的方法
        * @param senderId 发送者ID
        */
       public void onMessageBySystem(String senderId, Object message);
       /**
        * 收到收件箱的单发消息
        * @param senderId 发送者ID
        * @param receiverId 接收者ID
        * @return 是否成功
        */
       public boolean onMessageByInboxAndSingle(String senderId, String receiverId, Object message);
       /**
        * 收到收件箱的群发消息
        * @param senderId 发送者ID
        * @param groupId 群组ID
        * @param receiverId 接收者ID
        * @return 是否成功
        */
       public boolean onMessageByInboxAndByMass(String senderId, String groupId, String receiverId, Object message);
       /**
        * 处理过程中发生错误
        */
       public void onError(Session session, Throwable error);
   }
   ```

4. 使用MessagePlusUtils工具类来单发消息或者群发消息。

5. 在当前版本即时没有开启持久化的情况下也不在需要去调用MessagePlusUtils.createGroup(createUserId,  name,  client_ids)来创建、导入群组，你只需要实现GroupInterface接口即可（需要被Bean容器管理）。

6. 如果想要使用持久化功能（提前需要你的项目具有Redis），可以配置如下来打开：

   ```yml
   messageplus:
     serviceId: ... # 服务ID，需要唯一，为空时会自动生成
     persistence: true # 开启持久化
     message:
     	message-persistence: true # 消息持久化（默认开启，需要开启persistence才可以生效）
     	expiration-time: -1 # 消息持久化的过期时间
   ```

#### 使用说明

1.  如果你想要测试一下，可以去我的仓库中的message-plus-text拉取代码来测试。(https://github.com/zwzrt/message-plus-test.git、https://gitee.com/modmb/message-plus-test.git)
2.  如果使用过程出现bug或者存在不足，可以向red_coral20240606@163.com发送邮箱，我们将会积极修复并提供更强大的功能。
3.  在大流量的情况下，可能出现同时调用一个session导致的写冲突，引发异常。

#### 参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request

#### 特技

1.  使用 Readme\_XXX.md 来支持不同的语言，例如 Readme\_en.md, Readme\_zh.md
2.  Gitee 官方博客 [blog.gitee.com](https://blog.gitee.com)
3.  你可以 [https://gitee.com/explore](https://gitee.com/explore) 这个地址来了解 Gitee 上的优秀开源项目
4.  [GVP](https://gitee.com/gvp) 全称是 Gitee 最有价值开源项目，是综合评定出的优秀开源项目
5.  Gitee 官方提供的使用手册 [https://gitee.com/help](https://gitee.com/help)
6.  Gitee 封面人物是一档用来展示 Gitee 会员风采的栏目 [https://gitee.com/gitee-stars/](https://gitee.com/gitee-stars/)

---

### English

#### Introduction

Websocket-based message enhancer, supporting single, group and system functions; Support cluster architecture service architecture, and data persistence (persistence based on Redis implementation, please pay attention to the Redis persistence configuration or re-to the database to persist); Supports the persistence and resending of failure messages.

#### SoftwareArchitecture
Maven+SpringBoot+WebSocket

#### UsingTheTutorial

1. The Maven project imports the dependency.

   ```xml
   <dependency>
       <groupId>io.github.zwzrt</groupId>
       <artifactId>message-plus</artifactId>
       <version>0.0.6-beta</version>
   </dependency>
   ```

3. Start the class to add @EnableMessagePlus to start the enhancer.

   ```java
   @SpringBootApplication
   @EnableMessagePlus // Enable MessagePlus
   public class ChatTestApplication {
       public static void main(String[] args) {
           SpringApplication.run(ChatTestApplication.class, args);
       }
   }
   ```

4. Implement MessagePlusBase interface

   ```java
   /**
    * Message receiving implementation class
    * @author mo
    **/
   @Service
   public class MyMessagePlusBase implements MessagePlusBase {
       /**
        * The method called successfully for connection establishment
        * @return If there is a return, the return value is used as the identifier; otherwise, the sid is used as the identifier
        */
       public String onOpen(Session session, String sid);
       /**
        * The connection closes the called method
        */
       public void onClose(String sid);
       /**
        * Permission verification upon message receipt
        * @param request HTTP request information
        * @param sendId Occurrence ID
        * @return Whether to allow messages to be sent
        */
       public boolean onMessageCheck(HttpServerRequest request, String sendId);
       /**
        * Method called after receiving a system-type message
        * @param senderId Sender ID
        */
       public void onMessageBySystem(String senderId, Object message);
       /**
        * Receive a single message from your inbox
        * @param senderId Sender ID
        * @param receiverId Receiver ID
        * @return Success or not
        */
       public boolean onMessageByInboxAndSingle(String senderId, String receiverId, Object message);
       /**
        * Get a mass message in your inbox
        * @param senderId Sender ID
        * @param groupId Group ID
        * @param receiverId Receiver ID
        * @return Success or not
        */
       public boolean onMessageByInboxAndByMass(String senderId, String groupId, String receiverId, Object message);
       /**
        * An error occurred during processing
        */
       public void onError(Session session, Throwable error);
   }
   ```
   
4. Use MessagePlusUtils utility classes to send single or mass messages.

7. In the current version of instant without open persistence. Also no longer need to call MessagePlusUtils createGroup (createUserId, name, client_ids) to create, import groups, You only need to implement the GroupInterface interface (which needs to be managed by the Bean container).

8. If you want to use the persistence feature (which requires your project to have Redis in advance), you can configure it as follows:

   ```yml
   messageplus:
     serviceId: ... # The service ID must be unique. If it is empty, it will be generated automatically.
     persistence: true # enable persistence
     message:
     	message-persistence: true # Message persistence (enabled by default. persistence must be enabled to take effect)
     	expiration-time: -1 # The expiration time of message persistence
   ```

#### InstructionsForUse

1.  If you want to test it, you can go to my repository and pull up the message-plus-text code to test it. (https://github.com/zwzrt/message-plus-test.git, https://gitee.com/modmb/message-plus-test.git)
2.  If there is a bug or deficiency in the use process, you can send an email to red_coral20240606@163.com, and we will actively fix it and provide more powerful functions.
3.  In the case of heavy traffic, write conflicts may occur when a session is called at the same time, resulting in an exception.

#### ParticipationContribution

1.  Fork this warehouse
2.  New Feat_xxx Branch
3.  Submit Code
4.  New Pull Request

#### Stunt

1.  Use Readme\_XXX.md to support different languages, e.g. Readme\_en.md, Readme\_zh.md
2.  Official Gitee Blog [blog.gite.com](https://blog.gitee.com)
3.  You can [https://gitee.com/explore](https://gitee.com/explore) this address to learn about excellent open source projects on Gitee
4.  [GVP](https://gitee.com/gvp) is the full name of Gitee's most valuable open source project and is an excellent open source project comprehensively evaluated.
5.  Gitee official user manual [https://gitee.com/help](https://gitee.com/help)
6.  Gitee's cover character is a column used to show the elegant demeanour of Gitee members [https://gitee.com/gitee-stars/](https://gitee.com/gitee-stars/)
