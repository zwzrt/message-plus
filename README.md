
[//]: # (# 消息增强器（message-plus）)

![](./doc/img/logo_max_white.png) 

# 消息增强器

<a href='https://gitee.com/modmb/message-plus/stargazers' style="margin-right: 5px">
    <img src='https://gitee.com/modmb/message-plus/badge/star.svg?theme=dark' alt='star'></img>
</a>
<a href='https://gitee.com/modmb/message-plus/members' style="margin-right: 5px">
    <img src='https://gitee.com/modmb/message-plus/badge/fork.svg?theme=dark' alt='fork'></img>
</a>
<img src="https://img.shields.io/static/v1?label=Github&message=message-plus&color=orange" alt="">

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

## 贡献者

感谢以下贡献者所做的一切贡献！

<a class="avatar" title="mo" href="/modmb"><img class="ui avatar image js-popover-card" src="https://foruda.gitee.com/avatar/1720421931102111157/11861406_modmb_1720421931.png!avatar100" alt="11861406 modmb 1720421931"></a>
<a class="avatar" title="墨桑" href="/MS_mos"><img class="ui avatar image js-popover-card" src="https://foruda.gitee.com/avatar/1722943436086411647/10922893_ms_mos_1722943436.png!avatar100" alt="10922893 ms mos 1722943436"></a>
<a class="avatar" title="w_m" href="/Tyrannosaurus_warriors"><img class="ui avatar image js-popover-card" src="data:image/png;base64,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" alt="w_m-Tyrannosaurus_warriors"></a>

<style>
.avatar {
    border-radius: 100px;
    margin-right: 10px;
}
.avatar img {
    width: 50px;
    height: 50px;
}
</style>
