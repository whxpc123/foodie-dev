package com.imooc.aspect;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ServiceLogAspect {

    /**
     * 切面表达式
     * execution 代表所要执行的表达式主体
     * 第一处 * 代表方法返回类型 *代表所有类型
     * 第二处 包名代表aop监控所在的包
     * 第三处 .. 代表该包及其子包下的所有类方法
     * 第四处 * 代表类名，*代表所有类
     * 第五处 *(..) *代表类中的方法名，(..)表示方法中的任何参数
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("execution(* com.imooc.service.impl..*.*(..))")
    public Object recordTimeLog(ProceedingJoinPoint joinPoint) throws Throwable{

        log.info("======  开始执行 {}.{} =======",
                joinPoint.getTarget().getClass(),
                joinPoint.getSignature().getName());


        long startTime = System.currentTimeMillis();//记录开始时间

        Object result = joinPoint.proceed();

        long endTime = System.currentTimeMillis(); //记录结束时间

        long takeTime =endTime-startTime;

        if(takeTime>3000){
            log.error("======= 执行结束，耗时{}毫秒 ======",takeTime);
        }else if(takeTime>2000){
            log.warn("======= 执行结束，耗时{}毫秒 ======",takeTime);
        }else{
            log.info("======= 执行结束，耗时{}毫秒 ======",takeTime);
        }

        return result;
    }


}
