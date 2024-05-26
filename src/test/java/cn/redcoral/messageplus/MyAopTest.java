package cn.redcoral.messageplus;

import org.springframework.stereotype.Component;

/**
 * @author mo
 * @Description:
 * @日期: 2024-05-25 13:43
 **/
@Component
public class MyAopTest {
    public void myPrintln(int i) {
        System.out.println("Hello MessagePlus! " + i);
    }
}
