package application.aspects;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DateBaseAspect {

    @Pointcut("execution(* *.update(..))")
    private void updateMethod() {
    }

    @Pointcut("execution(* *.insert(..))")
    private void insertMethod() {
    }

    @Pointcut("execution(* *.delete(..))")
    private void deleteMethod() {
    }

    @Before("updateMethod()")
    public void beforeUpdate(JoinPoint jp) {
        System.out.println("Call database. Class: " + jp.getTarget().getClass().getSimpleName() + ", method: "
                + jp.getSignature().getName());
    }

    @Before("deleteMethod()")
    public void beforeDelete(JoinPoint jp) {
        System.out.println("Call database. Class: " + jp.getTarget().getClass().getSimpleName() + ", method: "
                + jp.getSignature().getName());
    }

    @Before("insertMethod()")
    public void beforeIncert(JoinPoint jp) {
        System.out.println("Call database. Class: " + jp.getTarget().getClass().getSimpleName() + ", method: "
                + jp.getSignature().getName());
    }

}