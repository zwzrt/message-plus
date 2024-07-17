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
 * @param id
 * @param data
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
 * @param id
 * @param data
 * @returns {{msg: string, code: number, data, type: string}}
 * @constructor
 */
function NewMassShotMessage(id, data) {
    var message = {
        code: 200,
        type: MessageType.MASS_SHOT,
        _id: id,
        data: data
    };
    return message;
}
/**
 * 创建系统消息
 * @param data
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