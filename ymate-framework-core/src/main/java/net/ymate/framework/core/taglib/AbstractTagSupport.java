/*
 * Copyright (c) 2007-2107, the original author or authors. All rights reserved.
 * 
 * This program licensed under the terms of the GNU Lesser General Public License version 3.0
 * as published by the Free Software Foundation.
 */
package net.ymate.framework.core.taglib;

import net.ymate.platform.core.util.RuntimeUtils;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.DynamicAttributes;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 自定义标签抽象实现类，子类仅需编写业务逻辑数据处理过程，由此类负责变量及作用域的保存
 *
 * @author 刘镇 (suninformation@163.com) on 14-7-6
 * @version 1.0
 */
public abstract class AbstractTagSupport extends BodyTagSupport implements DynamicAttributes {

    public enum Scope {
        APPLICATION, SESSION, REQUEST, PAGE
    }

    /**
     *
     */
    private static final long serialVersionUID = -1603817445681542882L;

    /**
     * 变量名称
     */
    private String var;

    /**
     * 变量存储作用域，默认：page（取值范围：application, session, request, page）
     */
    private String scope;

    /**
     * 总是执行标签体，忽略标签返回数据是否为NULL，开发人员需注意，默认：false
     */
    private boolean always;

    /**
     * 内部迭代器
     */
    private Iterator<?> __iterator;

    /**
     * 序号, 用于内部迭代器执行计数
     */
    private int __sequence;

    /**
     * 内循环开关，通过该属性让子类判断是否需要调用setIterator方法
     */
    private boolean innerLoop;

    /**
     * 存放动态属性数据
     */
    protected Map<String, Object> __dynamicAttributes = new HashMap<String, Object>();

    /**
     * 构造器
     */
    public AbstractTagSupport() {
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.tagext.BodyTagSupport#doStartTag()
     */
    @Override
    public int doStartTag() throws JspException {
        // Init
        __iterator = null;
        __sequence = 0;
        //
        Object _resultObj = doProcessTagData();
        // 若内部迭代器存在，则执行迭代处理逻辑
        if (__iterator != null) {
            if (StringUtils.isBlank(getVar())) {
                throw new NullArgumentException("var");
            }
            if (__iterator.hasNext()) {
                doProcessIteratorTagDataStatus(__iterator.next(), ++__sequence);
                return EVAL_BODY_AGAIN;
            } else {
                return SKIP_BODY;
            }
        } else {
            // 否则按正常逻辑处理
            if (_resultObj != null) {
                if (StringUtils.isNotBlank(getVar())) {
                    switch (Scope.valueOf(StringUtils.defaultIfEmpty(getScope(), Scope.PAGE.name()).toUpperCase())) {
                        case APPLICATION:
                            pageContext.getServletContext().setAttribute(getVar(), _resultObj);
                            break;
                        case REQUEST:
                            pageContext.getRequest().setAttribute(getVar(), _resultObj);
                            break;
                        case SESSION:
                            pageContext.getSession().setAttribute(getVar(), _resultObj);
                            break;
                        default:
                            pageContext.setAttribute(getVar(), _resultObj);
                    }
                } else if (_resultObj instanceof String) {
                    try {
                        pageContext.getOut().write(_resultObj.toString());
                    } catch (IOException e) {
                        throw new JspException(e.getMessage(), RuntimeUtils.unwrapThrow(e));
                    }
                }
                return EVAL_BODY_INCLUDE;
            } else {
                if (isAlways()) {
                    return EVAL_BODY_INCLUDE;
                }
                return SKIP_BODY;
            }
        }
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.tagext.BodyTagSupport#doAfterBody()
     */
    @Override
    public int doAfterBody() throws JspException {
        if (__iterator != null) {
            if (__iterator.hasNext()) {
                doProcessIteratorTagDataStatus(__iterator.next(), ++__sequence);
                return EVAL_BODY_AGAIN;
            } else {
                return SKIP_BODY;
            }
        }
        return super.doAfterBody();
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.tagext.BodyTagSupport#doEndTag()
     */
    @Override
    public int doEndTag() throws JspException {
        if (__iterator != null) {
            try {
                if (this.getBodyContent() != null){
                    pageContext.getOut().write(this.getBodyContent().getString());
                    this.getBodyContent().clearBody();
                }
            } catch (IOException e) {
                throw new JspException(RuntimeUtils.unwrapThrow(e));
            }
        }
        return super.doEndTag();
    }

    /**
     * @return 由子类标签实现具体数据处理过程，并返回处理结果对象
     */
    protected abstract Object doProcessTagData() throws JspException;

    /**
     * 处理迭代数据时，通过该方法进行数据状态的存储等
     *
     * @param data 当前迭代数据
     * @param sequence 当前迭代计数
     * @throws JspException
     */
    protected void doProcessIteratorTagDataStatus(Object data, int sequence) throws JspException {
        pageContext.setAttribute(getVar(), data);
        pageContext.setAttribute(getVar() + "_sequence", sequence);
    }

    /**
     * @return 返回动态属性映射
     */
    public Map<String, Object> getDynamicAttributes() {
        return __dynamicAttributes;
    }

    /**
     * @param attrName 动态属性名称
     * @return 根据动态属性名称获取属性值对象
     */
    public Object getDynamicAttribute(String attrName) {
        return __dynamicAttributes.get(attrName);
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.tagext.DynamicAttributes#setDynamicAttribute()
     */
    public void setDynamicAttribute(String uri, String localName, Object value ) throws JspException {
        __dynamicAttributes.put(localName, value);
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public boolean isAlways() {
        return always;
    }

    public void setAlways(boolean always) {
        this.always = always;
    }

    /**
     * 设置迭代对象引用，该迭代器将影响标签的执行方式和结果
     *
     * @param iterator
     */
    protected void setIterator(Iterator<?> iterator) {
        this.__iterator = iterator;
    }

    public boolean isInnerLoop() {
        return innerLoop;
    }

    public void setInnerLoop(boolean innerLoop) {
        this.innerLoop = innerLoop;
    }
}
