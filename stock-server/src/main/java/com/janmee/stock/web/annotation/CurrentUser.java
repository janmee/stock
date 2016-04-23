package com.janmee.stock.web.annotation;

import com.seewo.core.base.Constants;

import java.lang.annotation.*;

/**
 * 当前用户的标签
 *
 * @author chenyonghui
 * @version 1.0
 * @since 1.0
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CurrentUser {

    /**
     * 当前用户在request中的名字
     *
     * @return
     */
    String value() default Constants.CURRENT_USER;

}
