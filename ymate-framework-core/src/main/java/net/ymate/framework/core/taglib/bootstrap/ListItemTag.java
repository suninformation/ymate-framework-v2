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
 * 列表项
 *
 * @author 刘镇 (suninformation@163.com) on 16/11/14 下午7:00
 * @version 1.0
 */
public class ListItemTag extends ElementsTag {

    private String style;

    private String href;

    private boolean blank;

    private String heading;
    private String badge;

    private boolean active;
    private boolean disabled;

    public ListItemTag() {
    }

    @Override
    protected void __doSetTagName() {
        if (this.getParent() instanceof ListGroupTag && ((ListGroupTag) this.getParent()).isLink()) {
            this.set_tag("a");
        } else {
            this.set_tag("li");
        }
    }

    @Override
    protected StringBuilder __doTagStart() {
        StringBuilder _classSB = new StringBuilder(StringUtils.trimToEmpty(this.get_class()));
        _classSB.append(" list-group-item");
        if (StringUtils.isNotBlank(style)) {
            _classSB.append("list-group-item-").append(style);
        }
        if (active) {
            _classSB.append(" active");
        }
        if (disabled) {
            _classSB.append(" disabled");
        }
        if (this.getParent() instanceof ListGroupTag && ((ListGroupTag) this.getParent()).isLink() && StringUtils.isNotBlank(href)) {
            this.getDynamicAttributes().put("href", href);
            if (blank) {
                this.getDynamicAttributes().put("target", "_blank");
            }
        }
        //
        this.set_class(_classSB.toString());
        //
        return super.__doTagStart();
    }

    @Override
    protected StringBuilder __doTagContent(StringBuilder tagContent, StringBuilder bodyContent) {
        if (StringUtils.isNotBlank(badge)) {
            tagContent.append("<span class=\"badge\">").append(badge).append("</span>");
        }
        if (StringUtils.isNotBlank(heading)) {
            tagContent.append("<h4 class=\"list-group-item-heading\">").append(heading).append("</h4>");
            tagContent.append("<p class=\"list-group-item-text\">").append(bodyContent).append("</p>");
            return __doTagEnd(tagContent);
        }
        return super.__doTagContent(tagContent, bodyContent);
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public boolean isBlank() {
        return blank;
    }

    public void setBlank(boolean blank) {
        this.blank = blank;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
}
