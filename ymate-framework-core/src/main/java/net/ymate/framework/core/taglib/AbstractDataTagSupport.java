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

import org.apache.commons.lang.StringUtils;

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
     * 是否已逻辑删除
     */
    private Boolean deleted;

    /**
     * 查询页号
     */
    private int page;

    /**
     * 分页大小
     */
    private int pageSize;

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
     * @return 转换并返回字段过滤集合数组
     */
    protected String[] __doGetFieldList() {
        return StringUtils.split(fields, '|');
    }

    /**
     * @return 返回OrderBy字段数组
     */
    protected String[] __doGetOrderByArray() {
        return StringUtils.split(orderBy, '|');
    }

    /**
     * @return 处理补全OrderBy子句
     */
    protected String __doGetOrderBy() {
        orderBy = StringUtils.trimToEmpty(orderBy);
        if (!orderBy.toLowerCase().startsWith("order by")) {
            orderBy += " ORDER BY ";
        }
        return orderBy;
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

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
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
