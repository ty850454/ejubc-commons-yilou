package com.ejubc.commons.yilou.utils.excel;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * excel列定义
 * @author xy
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelColumn {

    /**
     * @return 列的标题
     */
    String title();

    /**
     * @return 列的索引，从0开始
     */
    int index();

}
