package cn.powernukkitx.techdawn.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自动将标注过的类添加到注册表中。
 * 务必注意，不能在匿名内部类和局部类中使用此注解！！！
 *
 * @author Superice666
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface AutoRegister {
    Class<?> value();
}
