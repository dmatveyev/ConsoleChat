package application.aspects;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;


@Aspect
@Component
public class SendMessageAspect {

   

    @Pointcut("execution(* *onApplicationEvent(..))")
    private void countSending(){}

    @Before("countSending()")
    public void sendingBefore() {
        Logger.getLogger("as").info("Aspect was created");
        System.out.println("sending message");
    }
}