package cn.redcoral.messageplus;

import cn.redcoral.messageplus.entity.MessageType;
import org.junit.jupiter.api.Test;

/**
 * @author mo
 * @Description:
 * @日期: 2024-06-06 19:10
 **/
public class MenuTest {
    enum t {
        A, B, C, D;
    }

    @Test
    public void test() {
        System.out.println(t.A.toString());
        System.out.println(MessageType.SINGLE_SHOT);
        System.out.println(MessageType.valueOf("SINGLE_SHOT"));
        System.out.println("SINGLE_SHOT".equals(MessageType.SINGLE_SHOT.toString()));
    }
}
