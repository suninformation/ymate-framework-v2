/*
 * Copyright 2007-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.ymate.framework.core.taglib;

import net.ymate.platform.persistence.Fields;
import net.ymate.platform.persistence.IResultSet;
import net.ymate.platform.persistence.Page;
import net.ymate.platform.persistence.jdbc.query.OrderBy;
import org.apache.commons.lang.StringUtils;

import javax.servlet.jsp.JspException;
import java.util.Arrays;

/**
 * 自定义数据访问标签抽象实现类，增加数据查询相关属性及方法
 *
 * @author 刘镇 (suninformation@163.com) on 14-7-29
 * @version 1.0
 */
public abstract class AbstractDataTagSupport extends AbstractTagSupport {

    /**
     * 数据主键
     */
    private String id;

    /**
     * 自定义类型
     */
    private Integer type;

    /**
     * 站点ID
     */
    private String siteId;

    /**
     * 数据所有者
     */
    private String owner;

    /**
     * 查询页号
     */
    private int page;

    /**
     * 分页大小
     */
    private int pageSize;

    /**
     * 是否开启缓存
     */
    private boolean cacheable;

    /**
     * 缓存KEY
     */
    private String cacheKey;

    /**
     * 缓存过期时间（秒）
     */
    private int cacheTimeout;

    /**
     * 字段过滤集合，多个字段名称间采用'|'进行分隔
     */
    private String fields;

    /**
     * 排序
     */
    private String orderBy;

    /**
     * @return 返回Page分页参数对象
     */
    protected Page __doGetPage() {
        if (page > 0 && pageSize > 0) {
            return Page.create(page).pageSize(pageSize);
        }
        return null;
    }

    /**
     * @return 转换并返回字段过滤对象
     */
    protected Fields __doGetFields() {
        String[] _fields = StringUtils.split(StringUtils.trim(fields), '|');
        if (_fields != null && _fields.length > 0) {
            return Fields.create().add(Arrays.asList(_fields));
        }
        return null;
    }

    /**
     * @return 返回OrderBy排序对象
     */
    protected OrderBy __doGetOrderBy() {
        String[] _fields = StringUtils.split(StringUtils.trim(orderBy), '|');
        if (_fields != null && _fields.length > 0) {
            OrderBy _orderBy = OrderBy.create();
            for (String _field : _fields) {
                String[] _order = StringUtils.split(_field, " ");
                if (_order.length > 1) {
                    if (StringUtils.equalsIgnoreCase(_order[1], "desc")) {
                        _orderBy.desc(_order[0]);
                    } else {
                        _orderBy.asc(_order[0]);
                    }
                } else {
                    _orderBy.asc(_field);
                }
            }
            return _orderBy;
        }
        return null;
    }

    protected boolean __doInitIterator(IResultSet<?> resultSet) throws JspException {
        return __doInitIterator(resultSet.getResultData().iterator(), resultSet.getPageCount(), resultSet.getRecordCount());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public boolean isCacheable() {
        return cacheable;
    }

    public void setCacheable(boolean cacheable) {
        this.cacheable = cacheable;
    }

    public String getCacheKey() {
        return cacheKey;
    }

    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }

    public int getCacheTimeout() {
        return cacheTimeout;
    }

    public void setCacheTimeout(int cacheTimeout) {
        this.cacheTimeout = cacheTimeout;
    }

    public String getFields() {
        return fields;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }
}
