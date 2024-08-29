
[//]: # (# 消息增强器（message-plus）)

<img src="./doc/img/logo2.png" style="width: 100%; margin: 5% 0;" alt="logo">

<div style="height: 80px; width: 600px; margin: 30px 0;">
    <img src="./doc/img/logo2.png" style="height: 80px; float: left; width: 35%" alt="logo">
    <div style="height: 60px; line-height: 60px; float: left; font-size: 25px; width: 64%; margin-left: 1%">消息增强器（message-plus）</div>
    <div style="float: left; width: 63%; height: 16px; font-size: 10px; line-height: 16px; margin: 2px 1%;">
        <a href='https://gitee.com/modmb/message-plus/stargazers' style="margin-right: 5px">
            <img src='https://gitee.com/modmb/message-plus/badge/star.svg?theme=dark' alt='star'></img>
        </a>
        <a href='https://gitee.com/modmb/message-plus/members' style="margin-right: 5px">
            <img src='https://gitee.com/modmb/message-plus/badge/fork.svg?theme=dark' alt='fork'></img>
        </a>
        <img src="https://img.shields.io/static/v1?label=Github&message=message-plus&color=orange" alt="">
    </div>
</div>

<div style="height: 80px; width: 600px; margin: 30px 0;">
    <img src="./doc/img/logo_min.png" style="height: 60px; float: left; width: 9%; margin: 10px 2%" alt="logo">
    <div style="height: 60px; line-height: 60px; float: left; font-size: 25px; width: 64%; margin-left: 1%">消息增强器（message-plus）</div>
    <div style="float: left; width: 63%; height: 16px; font-size: 10px; line-height: 16px; margin: 2px 1%;">
        <a href='https://gitee.com/modmb/message-plus/stargazers' style="margin-right: 5px">
            <img src='https://gitee.com/modmb/message-plus/badge/star.svg?theme=dark' alt='star'></img>
        </a>
        <a href='https://gitee.com/modmb/message-plus/members' style="margin-right: 5px">
            <img src='https://gitee.com/modmb/message-plus/badge/fork.svg?theme=dark' alt='fork'></img>
        </a>
        <img src="https://img.shields.io/static/v1?label=Github&message=message-plus&color=orange" alt="">
    </div>
</div>

# <img src="./doc/img/logo_white_min.png" width="50"> 消息增强器
<h1>ddd</h1>

<img src="./doc/img/logo_white_min.png" width="100" height="100">

---

<a href="https://www.red-coral.cn/">前往主页</a>

[//]: # (&ensp;|&ensp;)
[//]: # (<a href="https://zwzrt.github.io/">加入我们</a>)

### 介绍

基于WebSocket的消息增强器，帮助开发者快速开发即时聊天系统。支持单发、群发、聊天室及系统功能;以及数据持久化；支持失败消息的持久化及重发功能。

此版本为单机版，集群版请前往[集群版仓库](https://gitee.com/modmb/message-plus-cluster)（Gitee）

### 软件架构

Maven + SpringBoot + WebSocket + MyBatisPlus + Caffeine + Hutool

### 版本功能区别

<table style="width: 100%; text-align: center">
    <tr>
        <th>区别</th>
        <th>单机版</th>
        <th>集群版</th>
    </tr>
    <tr>
        <td>集群部署</td>
        <td style="color: red">不支持</td>
        <td style="color: green">支持</td>
    </tr>
    <tr>
        <td>消息持久化</td>
        <td style="color: green">支持</td>
        <td style="color: green">支持</td>
    </tr>
    <tr>
        <td>消息重发</td>
        <td style="color: green">支持</td>
        <td style="color: green">支持</td>
    </tr>
</table>

### 环境要求区别

<table style="width: 100%; text-align: center">
    <tr>
        <th>区别</th>
        <th>单机版</th>
        <th>集群版</th>
    </tr>
    <tr>
        <td>数据库</td>
        <td style="color: red">需要</td>
        <td style="color: red">需要</td>
    </tr>
    <tr>
        <td>Redis</td>
        <td style="color: green">不需要</td>
        <td style="color: red">需要</td>
    </tr>
</table>

### 快速上手

   ```xml
   <dependency>
       <groupId>io.github.zwzrt</groupId>
       <artifactId>message-plus</artifactId>
       <version>0.2.0-beta</version>
   </dependency>
   ```
前往 [学习文档](https://www.red-coral.cn/nav)

### 使用说明

1.  如果你想要测试一下，可以去我的仓库中的message-plus-test拉取代码来测试。(https://github.com/zwzrt/message-plus-test.git、 https://gitee.com/modmb/message-plus-test.git)
2.  如果使用过程出现bug或者存在不足，可以向red_coral20240606@163.com发送邮箱，我们将会积极修复并提供更强大的功能。
