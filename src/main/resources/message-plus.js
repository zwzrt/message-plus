/**
 * 消息类型
 * @type {{SINGLE_SHOT: string, MASS_SHOT: string, SYSTEM_SHOT: string}}
 */
const MessageType = {
    /**
     * 单发
     */
    SINGLE_SHOT: "SINGLE_SHOT",
    /**
     * 群发
     */
    MASS_SHOT: "MASS_SHOT",
    /**
     * 系统
     */
    SYSTEM_SHOT: "SYSTEM_SHOT",
}
/**
 * 创建单发消息
 * @param id 用户ID
 * @param data 消息内容
 * @returns {{msg: string, code: number, data, type: string}}
 * @constructor
 */
function NewSingleShotMessage(id, data) {
    var message = {
        code: 200,
        type: MessageType.SINGLE_SHOT,
        _id: id,
        data: data
    };
    return message;
}
/**
 * 创建群发消息
 * @param id 群组ID
 * @param data 消息内容
 * @returns {{msg: string, code: number, data, type: string}}
 * @constructor
 */
function NewMassShotMessage(id, data) {
    return {
        code: 200,
        type: MessageType.MASS_SHOT,
        _id: id,
        data: data
    };
}
/**
 * 创建系统消息
 * @param data 消息内容
 * @returns {{msg: string, code: number, data, type: string}}
 * @constructor
 */
function NewSystemShotMessage(data) {
    var message = {
        code: 200,
        type: MessageType.SYSTEM_SHOT,
        _id: '',
        data: data
    };
    return message;
}
/**
 * 转换消息
 * @param json
 * @returns {any}
 */
function getMessage(json) {
    return JSON.parse(json);
}

const messagePlusWebSocket = {
    wsUrl: undefined,
    suffixUrl: undefined,
    // 我的ID
    myId: undefined,
    // 套接字
    websocket: undefined,
    // 服务器地址
    url: undefined,
    /**
     * 连接服务器
     * @param url1 地址
     * @param suffixUrl1 地址后缀（访问路径）
     * @param id1 用户ID
     */
    join(url1, suffixUrl1, id1) {
        if (url1===undefined || suffixUrl1===undefined || id1===undefined) return;
        this.url = url1;
        this.suffixUrl = suffixUrl1;
        this.myId = id1;
        this.wsUrl = "ws://"+this.url+this.suffixUrl+"/"+this.myId;
        //判断当前浏览器是否支持WebSocket
        if('WebSocket' in window) {
            //改成你的地址
            this.websocket = new WebSocket(this.wsUrl);
        } else {
            alert('当前浏览器 Not support websocket')
        }

        //连接发生错误的回调方法
        this.websocket.onerror = this.onerror;
        //连接成功建立的回调方法
        this.websocket.onopen = this.onopen;
        //接收到消息的回调方法
        this.websocket.onmessage = this.onmessage;
        //连接关闭的回调方法
        this.websocket.onclose = this.onclose;
        //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
        window.onbeforeunload = this.onbeforeunload;
    },
    //关闭WebSocket连接
    closeWebSocket() {
        this.websocket.close();
    },
    /**
     * 发送单发消息
     * @param id 用户ID
     * @param msg 消息内容
     */
    sendSingleShotMessage(id, msg) {
        const message = NewSingleShotMessage(id, msg);
        this.websocket.send(JSON.stringify(message));
    },
    /**
     * 发送群发消息
     * @param id 群组ID
     * @param msg 消息内容
     */
    sendMassShotMessage(id, msg) {
        const message = NewMassShotMessage(id, msg);
        this.websocket.send(JSON.stringify(message));
    },
    /**
     * 发送系统消息
     * @param msg 消息内容
     */
    sendSystemShotMessage(msg) {
        const message = NewSystemShotMessage(msg);
        this.websocket.send(JSON.stringify(message));
    },
    //连接发生错误的回调方法
    onerror() {
        setMessageInnerHTML("WebSocket连接发生错误");
    },
    //连接成功建立的回调方法
    onopen() {
        setMessageInnerHTML("WebSocket连接成功");
    },
    //接收到消息的回调方法
    onmessage(event) {
        // console.log(event);
        setMessageInnerHTML(event.data);
    },
    //连接关闭的回调方法
    onclose() {
        setMessageInnerHTML("WebSocket连接关闭");
    },
    //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
    onbeforeunload() {
        closeWebSocket();
    },
}

export default messagePlusWebSocket;
