/*
 * Copyright (c) 2007-2107, the original author or authors. All rights reserved.
 *
 * This program licensed under the terms of the GNU Lesser General Public License version 3.0
 * as published by the Free Software Foundation.
 */
package net.ymate.framework.core.taglib;

import net.ymate.platform.core.util.DateTimeUtils;

import javax.servlet.jsp.JspException;

/**
 * @author 刘镇 (suninformation@163.com) on 14-8-5
 * @version 1.0
 */
public class DateTimeTag extends AbstractTagSupport {

    private Long date;

    private boolean utc;

    private String pattern;

    protected Object doProcessTagData() throws JspException {
        if (date == null) {
            return DateTimeUtils.formatTime(System.currentTimeMillis(), pattern);
        } else if (utc) {
            date *= 1000L;
        }
        return DateTimeUtils.formatTime(date, pattern);
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public boolean isUtc() {
        return utc;
    }

    public void setUtc(boolean utc) {
        this.utc = utc;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }
}
