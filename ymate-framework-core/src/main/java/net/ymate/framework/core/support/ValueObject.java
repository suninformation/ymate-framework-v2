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
package net.ymate.framework.core.support;

import com.alibaba.fastjson.JSONObject;
import net.ymate.platform.core.util.ClassUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 刘镇 (suninformation@163.com) on 16/10/29 上午1:09
 * @version 1.0
 */
public abstract class ValueObject<T extends ValueObject> implements Serializable {

    private String id;

    private Long createTime;

    private Long lastModifyTime;

    private Map<String, Attribute> attributes;

    public ValueObject() {
        this.attributes = new HashMap<String, Attribute>();
    }

    public String getId() {
        return id;
    }

    @SuppressWarnings("unchecked")
    public T setId(String id) {
        this.id = id;
        return (T) this;
    }

    public Long getCreateTime() {
        return createTime;
    }

    @SuppressWarnings("unchecked")
    public T setCreateTime(Long createTime) {
        this.createTime = createTime;
        return (T) this;
    }

    public Long getLastModifyTime() {
        return lastModifyTime;
    }

    @SuppressWarnings("unchecked")
    public T setLastModifyTime(Long lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
        return (T) this;
    }

    public T addAttribute(String key, Object value) {
        return addAttribute(new Attribute(key, value));
    }

    @SuppressWarnings("unchecked")
    public T addAttribute(Attribute attr) {
        this.attributes.put(attr.getKey(), attr);
        return (T) this;
    }

    public Attribute getAttribute(String attrKey) {
        return this.attributes.get(attrKey);
    }

    public Map<String, Attribute> getAttributes() {
        return attributes;
    }

    @SuppressWarnings("unchecked")
    public T setAttributes(Map<String, Attribute> attributes) {
        this.attributes = attributes;
        return (T) this;
    }

    public JSONObject toJSON() {
        JSONObject _json = new JSONObject(ClassUtils.wrapper(this).toMap(new ClassUtils.IFieldValueFilter() {
            @Override
            public boolean filter(String fieldName, Object fieldValue) {
                return !"attributes".equals(fieldName);
            }
        }));
        //
        JSONObject _attrs = new JSONObject();
        for (Attribute _attr : attributes.values()) {
            _attrs.put(_attr.getKey(), _attr.getValue());
        }
        _json.put("attributes", _attrs);
        //
        return _json;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
    }

    public static class Attribute implements Serializable {

        private String id;

        private String title;

        private String key;

        private Object value;

        private Long sort;

        private Integer type;

        private String owner;

        private String remark;

        public Attribute() {
        }

        public Attribute(String key, Object value) {
            this.key = key;
            this.value = value;
        }

        public Attribute(String id, String key, Object value) {
            this.id = id;
            this.key = key;
            this.value = value;
        }

        public Attribute(String id, String key, Object value, Integer type, String owner) {
            this.id = id;
            this.key = key;
            this.value = value;
            this.type = type;
            this.owner = owner;
        }

        public Attribute(String id, String title, String key, Object value, Long sort, Integer type, String owner, String remark) {
            this.id = id;
            this.title = title;
            this.key = key;
            this.value = value;
            this.sort = sort;
            this.type = type;
            this.owner = owner;
            this.remark = remark;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public Long getSort() {
            return sort;
        }

        public void setSort(Long sort) {
            this.sort = sort;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public String getOwner() {
            return owner;
        }

        public void setOwner(String owner) {
            this.owner = owner;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        @Override
        public String toString() {
            return value == null ? null : value.toString();
        }
    }
}
