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
 * 菜单项
 *
 * @author 刘镇 (suninformation@163.com) on 16/11/15 上午12:55
 * @version 1.0
 */
public class ItemTag extends ElementsTag {

    private boolean divider;
    private boolean header;

    private String title;

    private String href;

    private boolean dropdown;
    private String dropMenuClass;
    private String dropMenuAttrs;
    private boolean left;
    private boolean right;
    private boolean pull;

    private String tabId;
    private String modalId;

    private boolean subitem;

    private boolean previous;
    private boolean next;

    private boolean active;
    private boolean disabled;

    public ItemTag() {
    }

    @Override
    protected void __doSetTagName() {
        this.set_tag(StringUtils.defaultIfBlank(this.get_tag(), "li"));
    }

    @Override
    protected StringBuilder __doTagStart() {
        StringBuilder _classSB = new StringBuilder(StringUtils.trimToEmpty(this.get_class()));
        if (divider) {
            _classSB.append(" divider");
        } else if (header) {
            _classSB.append(" dropdown-header");
        } else if (dropdown) {
            _classSB.append(" dropdown");
        } else {
            if (active) {
                _classSB.append(" active");
            }
            if (disabled) {
                _classSB.append(" disabled");
            }
            if (this.getParent() instanceof PaginationTag
                    && ((PaginationTag) this.getParent()).isPager()) {
                if (previous) {
                    _classSB.append(" previous");
                } else if (next) {
                    _classSB.append(" next");
                }
            }
        }
        //
        this.set_class(_classSB.toString());
        //
        return super.__doTagStart();
    }

    @Override
    protected StringBuilder __doTagContent(StringBuilder tagContent, StringBuilder bodyContent) {
        if (dropdown) {
            StringBuilder _tmpSB = new StringBuilder("<a class=\"dropdown-toggle\" data-toggle=\"dropdown\" href=\"")
                    .append(StringUtils.defaultIfBlank(href, "#")).append("\">")
                    .append(StringUtils.trimToEmpty(title)).append("</a>");
            _tmpSB.append("<ul class=\"dropdown-menu");
            if (right) {
                if (pull) {
                    _tmpSB.append(" pull-right");
                } else {
                    _tmpSB.append(" dropdown-menu-right");
                }
            } else if (left) {
                if (pull) {
                    _tmpSB.append(" pull-left");
                } else {
                    _tmpSB.append(" dropdown-menu-left");
                }
            }
            _tmpSB.append(" ").append(StringUtils.trimToEmpty(dropMenuClass)).append("\"");
            _tmpSB.append(" ").append(StringUtils.trimToEmpty(dropMenuAttrs)).append(">");
            tagContent.append(_tmpSB).append(bodyContent).append("</ul>");
            //
            return __doTagEnd(tagContent);
        } else {
            StringBuilder _tmpSB = new StringBuilder();
            if (StringUtils.isNotBlank(href) || StringUtils.isNotBlank(tabId) || StringUtils.isNotBlank(modalId)) {
                _tmpSB.append("<a href=\"").append(StringUtils.defaultIfBlank(href, "#")).append("\"");
                if (StringUtils.isNotBlank(tabId)) {
                    _tmpSB.append(" data-toggle=\"tab\" data-target=\"#").append(tabId).append("\">");
                } else if (StringUtils.isNotBlank(modalId)) {
                    _tmpSB.append(" data-toggle=\"modal\" data-target=\"#").append(modalId).append("\">");
                } else {
                    _tmpSB.append(">");
                }
                if (subitem) {
                    _tmpSB.append(StringUtils.trimToEmpty(title)).append("</a>");
                    _tmpSB.append(bodyContent);
                } else {
                    _tmpSB.append(bodyContent).append("</a>");
                }
            } else {
                _tmpSB.append(bodyContent);
            }
            return __doTagEnd(tagContent.append(_tmpSB));
        }
    }

    public boolean isDivider() {
        return divider;
    }

    public void setDivider(boolean divider) {
        this.divider = divider;
    }

    public boolean isHeader() {
        return header;
    }

    public void setHeader(boolean header) {
        this.header = header;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public boolean isDropdown() {
        return dropdown;
    }

    public void setDropdown(boolean dropdown) {
        this.dropdown = dropdown;
    }

    public String getDropMenuClass() {
        return dropMenuClass;
    }

    public void setDropMenuClass(String dropMenuClass) {
        this.dropMenuClass = dropMenuClass;
    }

    public String getDropMenuAttrs() {
        return dropMenuAttrs;
    }

    public void setDropMenuAttrs(String dropMenuAttrs) {
        this.dropMenuAttrs = dropMenuAttrs;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isPull() {
        return pull;
    }

    public void setPull(boolean pull) {
        this.pull = pull;
    }

    public String getTabId() {
        return tabId;
    }

    public void setTabId(String tabId) {
        this.tabId = tabId;
    }

    public String getModalId() {
        return modalId;
    }

    public void setModalId(String modalId) {
        this.modalId = modalId;
    }

    public boolean isSubitem() {
        return subitem;
    }

    public void setSubitem(boolean subitem) {
        this.subitem = subitem;
    }

    public boolean isPrevious() {
        return previous;
    }

    public void setPrevious(boolean previous) {
        this.previous = previous;
    }

    public boolean isNext() {
        return next;
    }

    public void setNext(boolean next) {
        this.next = next;
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
