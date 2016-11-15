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
package net.ymate.framework.core.taglib.bootstrap;

import net.ymate.framework.core.taglib.ElementsTag;
import org.apache.commons.lang.StringUtils;

/**
 * 警告框
 *
 * @author 刘镇 (suninformation@163.com) on 16/11/12 上午3:36
 * @version 1.0
 */
public class AlertTag extends ElementsTag {

    /**
     * 可关闭的
     */
    private boolean dismissable;

    /**
     * 指定警告框风格样式，可选值：success|info|warning|danger
     */
    private String style;

    public AlertTag() {
    }

    @Override
    protected StringBuilder __doTagStart() {
        StringBuilder _classSB = new StringBuilder(StringUtils.trimToEmpty(this.get_class())).append(" alert alert-").append(StringUtils.defaultIfBlank(style, "warning"));
        if (dismissable) {
            _classSB.append(" alert-dismissable");
        }
        this.set_class(_classSB.toString());
        //
        return super.__doTagStart();
    }

    @Override
    protected StringBuilder __doTagContent(StringBuilder tagContent, StringBuilder bodyContent) {
        if (dismissable) {
            bodyContent.insert(0, "<button type=\"button\" class=\"close\" data-dismiss=\"alert\"><span aria-hidden=\"true\">&times;</span><span class=\"sr-only\">Close</span></button>");
        }
        //
        return super.__doTagContent(tagContent, bodyContent);
    }

    public boolean isDismissable() {
        return dismissable;
    }

    public void setDismissable(boolean dismissable) {
        this.dismissable = dismissable;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
}
