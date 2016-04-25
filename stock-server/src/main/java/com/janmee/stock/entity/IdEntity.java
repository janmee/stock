package com.janmee.stock.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.seewo.core.util.UidUtils;
import org.apache.commons.lang.StringUtils;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;

/**
 * 统一定义uid的entity基类.
 * <p/>
 * 基类统一定义id，uid的属性名称、数据类型、列名映射及生成策略.
 *
 * @author luojianming
 * @version 1.0
 * @since 1.0
 */
@MappedSuperclass
public class IdEntity implements Serializable {

    private static final long serialVersionUID = 7007049959636882279L;

    public static final String[] IGNORE_PROPERTIES = {"id"};

    /**
     * 自增的主键id
     */
    private int id;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
