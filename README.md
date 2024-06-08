# 消息增强器（message-plus）

---

### 语言（Language）

- [中文](#中文)
- [English](#English)

---

### 中文

#### 介绍

基于WebSocket的消息增强器，目前支持单发和群发功能。

#### 软件架构
Maven+SpringBoot+WebSocket

#### 使用教程

1. 下载代码到本地，打包安装至Maven仓库

2. Maven项目导入该依赖

   ```xml
   <dependency>
       <groupId>cn.redcoral.messageplus</groupId>
       <artifactId>message-plus</artifactId>
       <version>0.0.2-beta</version>
   </dependency>
   ```

3. 启动类添加@EnableMessagePlus来启动增强器

   ```java
   @SpringBootApplication
   @EnableMessagePlus // 开启message增强
   public class ChatTestApplication {
       public static void main(String[] args) {
           SpringApplication.run(ChatTestApplication.class, args);
       }
   }
   ```

4. 自己创建的WebSocket依赖MessagePlusBase并实现抽象方法（其中的@ServerEndpoint中的路径必须包含sid路径参数，如：/websocket/{sid}）

   ```java
   @Service
   @ServerEndpoint("/websocket/{sid}")
   public class MessageUtil extends MessagePlusBase {
       // 出现新连接时调用
       @Override
       public void onOpen(Session session,  String sid) {}
       // 断开连接时调用
       @Override
       public void onClose() {}
       // 接收到消息时调用（优先级高于下面三个方法）
       @Override
       public void onMessage(Object message, Session session) {}
       // 消息类型为单发时调用
       @Override
       public void onMessageBySingle(Object message, Session session) {}
       // 消息类型为群发时调用
       @Override
       public void onMessageByMass(Object message, Session session) {}
       // 消息类型为系统时调用
       @Override
       public void onMessageBySystem(Object message, Session session) {}
       // 过程中出现异常调用（会断开连接）
       @Override
       public void onError(Session session, Throwable error) {}
   }
   ```

5. 使用ChatUtils工具类来创建群组、单发消息或者群发消息

#### 使用说明

1.  如果使用过程出现bug或者存在不足，可以向red_coral20240606@163.com发送邮箱，我们将会积极修复并提供更强大的功能。
2.  目前该项目还并不支持多个服务间同步或联系的功能。
3.  该项目并不能做群组或用户信息的持久化，需要开发者自己去做持久化的代码。

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

A WebSocket-based message booster that currently supports single and group sending.

#### SoftwareArchitecture
Maven+SpringBoot+WebSocket

#### UsingTheTutorial

1. Download the code to the local, package and install it to the Maven repository.

2. The Maven project imports the dependency.

   ```xml
   <dependency>
       <groupId>cn.redcoral.messageplus</groupId>
       <artifactId>message-plus</artifactId>
       <version>0.0.2-beta</version>
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

4. Create your own WebSocket dependency MessagePlusBase and implement abstract methods (the path in the @ServerEndpoint must contain sid path parameters, such as:/websocket/{sid})

   ```java
   @Service
   @ServerEndpoint("/websocket/{sid}")
   public class MessageUtil extends MessagePlusBase {
       // Called when a new connection appears
       @Override
       public void onOpen(Session session,  String sid) {}
       // Called when disconnected
       @Override
       public void onClose() {}
       // Called when a message is received (precedence over the following three methods)
       @Override
       public void onMessage(Object message, Session session) {}
       // Called when the message type is single
       @Override
       public void onMessageBySingle(Object message, Session session) {}
       // Called when the message type is group sending
       @Override
       public void onMessageByMass(Object message, Session session) {}
       // Called when the message type is system
       @Override
       public void onMessageBySystem(Object message, Session session) {}
       // An abnormal call occurs during the process (will disconnect)
       @Override
       public void onError(Session session, Throwable error) {}
   }
   ```

5. Use the ChatUtils tool class to create groups, single messages, or mass messages.

#### InstructionsForUse

1.  If there is a bug or deficiency in the use process, you can send an email to red_coral20240606@163.com, and we will actively fix it and provide more powerful functions.
2.  At present, the project does not support the function of synchronization or contact between multiple services.
3.  The project can not do group or user information persistence, developers need to do their own persistence code.

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
