package cn.redcoral.messageplus.controller;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

import cn.hutool.http.server.HttpServerRequest;
import cn.redcoral.messageplus.data.entity.Computer;
import cn.redcoral.messageplus.data.entity.message.Message;
import cn.redcoral.messageplus.manage.SystemManage;
import cn.redcoral.messageplus.manage.UserManage;
import cn.redcoral.messageplus.port.MessagePlusBase;
import cn.redcoral.messageplus.properties.MessagePlusMessageProperties;
import cn.redcoral.messageplus.utils.CounterIdentifierUtil;
import com.sun.management.OperatingSystemMXBean;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 发送消息
 * @author mo
 **/
@Slf4j
@RestController
@RequestMapping("/messageplus/system")
public class MessagePlusSystemController {

    @Autowired
    private MessagePlusBase messagePlusBase;
    @Autowired
    private SystemManage systemManage;

    private static final MemoryMXBean memoryMXBean;

    static {
        memoryMXBean = ManagementFactory.getMemoryMXBean();
    }



    /**
     * 登录接口
     * @param username 用户名
     * @param password 密码
     * @return token（为null或""时登录失败）
     */
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        String token = messagePlusBase.login(username, password);
        if (token == null || token.isEmpty()) return null;
        systemManage.put(token, username);
        return token;
    }

    /**
     * 发送系统类消息
     * @param msg 消息体
     */
    @PostMapping("/send")
    public void sendSystemMessage(HttpServerRequest request, @RequestParam("id1") String senderId, @RequestBody Object msg) throws Exception {
        // 1.并发限流
        // 短时间发送消息达到上限，禁止发送消息
        if (CounterIdentifierUtil.isLessThanOrEqual(senderId, MessagePlusMessageProperties.concurrentNumber)) {
            return;
        }
        // 计数器加一
        CounterIdentifierUtil.numberOfSendsIncrease(senderId);

        // TODO 2.查询是否禁言


        // 3.发送消息
        Message message = Message.buildSystem(senderId, msg);
        messagePlusBase.onMessageBySystem(senderId, message.getData().toString());

        // 计数器减一
        CounterIdentifierUtil.numberOfSendsDecrease(senderId);
    }




    /**
     * 登出接口
     * @param token 令牌
     */
    @DeleteMapping("/logout")
    public void logout(@RequestParam String token) {
        systemManage.remove(token);
    }




    /**
     * 是否登录
     * @param token 令牌
     * @return 是否
     */
    @GetMapping("/isOnLine")
    public String isOnline(@RequestParam String token) {
        return systemManage.get(token);
    }

    /**
     * 获取服务器总人数
     * @return 总人数
     */
    @GetMapping("/onLineNum")
    public String getOnLinePeopleNum() {
        return UserManage.getOnLinePeopleNum().toString();
    }

    /**
     * 获取虚拟机最大内存
     */
    @GetMapping("/vmMaxMemory")
    public String getVirtualMachineMaximumMemory() {
        // 椎内存使用情况
        MemoryUsage memoryUsage = memoryMXBean.getHeapMemoryUsage();
        return String.valueOf(memoryUsage.getMax());
    }

    /**
     * 获取虚拟机已使用内存
     */
    @GetMapping("/vmMemoryUsed")
    public String getVirtualMachineMemoryUsed() {
        // 椎内存使用情况
        MemoryUsage memoryUsage = memoryMXBean.getHeapMemoryUsage();
        return String.valueOf(memoryUsage.getUsed());
    }

    /**
     * 获取物理最大内存（KB）
     */
    @GetMapping("/phMaxMemory")
    public String getPhysicsMaximumMemory() {
        OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        // 操作系统
        String osName = System.getProperty("os.name");
        // 总的物理内存
        String totalMemorySize = new DecimalFormat("#.##")
                .format(osmxb.getTotalPhysicalMemorySize());
        return totalMemorySize;
    }

    /**
     * 获取物理已使用内存
     */
    @GetMapping("/phMemoryUsed")
    public String getPhysicsMemoryUsed() {
        OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        // 操作系统
        String osName = System.getProperty("os.name");
        // 剩余的物理内存
        String freePhysicalMemorySize = new DecimalFormat("#.##")
                .format(osmxb.getFreePhysicalMemorySize());
        // 已使用的物理内存
        String usedMemory = new DecimalFormat("#.##").format(
                (osmxb.getTotalPhysicalMemorySize() - osmxb.getFreePhysicalMemorySize()));
        return usedMemory;
    }

    /**
     * 获取磁盘信息
     */
    @GetMapping("/disk")
    public List<Computer> getDiskInformation() {
        List<Computer> list = new ArrayList<>();
        // 磁盘使用情况
        File[] files = File.listRoots();
        for (File file : files) {
            // 磁盘路径
            String path = file.getPath();
            // 总空间
            String total = new DecimalFormat("#.#").format(file.getTotalSpace());
            // 空闲空间
            String free = new DecimalFormat("#.#").format(file.getFreeSpace());
            // 可用空间
            String un = new DecimalFormat("#.#").format(file.getUsableSpace());

            Computer computer = new Computer();
            computer.setPath(path);
            computer.setTotal(total);
            computer.setFree(free);
            computer.setUn(un);
            list.add(computer);
        }
        return list;
    }

}
