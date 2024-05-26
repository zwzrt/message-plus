package cn.redcoral.messageplus;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

/**
 * @author mo
 * @Description:
 * @日期: 2024-05-25 12:04
 **/
@Aspect
@Component
public class AopTest {
    @Pointcut("execution(void cn.redcoral.messageplus.MyAopTest.myPrintln(int))")
    void pt() {}

    @Around("pt()")
    public Object around(ProceedingJoinPoint pj) throws Throwable {
        System.out.println("开始...");
        Object o = pj.proceed();
        System.out.println("完成...");
        return o;
    }
}
