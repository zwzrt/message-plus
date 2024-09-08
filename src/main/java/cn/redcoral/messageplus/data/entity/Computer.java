package cn.redcoral.messageplus.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mo
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Computer {
    // 磁盘路径
    private String path;
    // 总空间
    private String total;
    // 可用空间
    private String un;
    // 空闲空间
    private String free;
}
