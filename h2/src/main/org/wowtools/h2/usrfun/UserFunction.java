package org.wowtools.h2.usrfun;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 加入此注解的静态方法可通过UserFunctionManager注册为h2函数
 * @author liuyu
 * @date 2016年12月22日
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface UserFunction {
	String value() default "";
}
