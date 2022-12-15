package cn.powernukkitx.techdawn.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 为自动添加到注册表的类设置附加信息，如名称等。
 * 请注意，此注解只能用于标注 {@link AutoRegister} 注解的类中。
 *
 * @author Superice666
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface AutoRegisterData {
    String value();
}
