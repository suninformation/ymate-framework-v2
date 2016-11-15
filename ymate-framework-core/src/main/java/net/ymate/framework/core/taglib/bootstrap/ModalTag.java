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
 * 模态框
 *
 * @author 刘镇 (suninformation@163.com) on 16/11/16 上午1:56
 * @version 1.0
 */
public class ModalTag extends ElementsTag {

    private String title;
    private boolean fade;
    private boolean nonStatic;

    private boolean hideCloseBtn;

    private String dialogClass;

    private String footer;

    private boolean small;
    private boolean large;

    public ModalTag() {
    }

    @Override
    protected StringBuilder __doTagStart() {
        StringBuilder _classSB = new StringBuilder(StringUtils.trimToEmpty(this.get_class())).append(" modal");
        if (fade) {
            _classSB.append(" fade");
        }
        if (!nonStatic) {
            this.getDynamicAttributes().put("data-backdrop", "static");
            this.getDynamicAttributes().put("data-keyboard", "false");
        }
        //
        this.set_class(_classSB.toString());
        //
        return super.__doTagStart();
    }

    @Override
    protected StringBuilder __doTagContent(StringBuilder tagContent, StringBuilder bodyContent) {
        tagContent.append("<div class=\"modal-dialog\"");
        if (small) {
            tagContent.append(" modal-sm");
        } else if (large) {
            tagContent.append(" modal-lg");
        }
        if (StringUtils.isNotBlank(dialogClass)) {
            tagContent.append(" ").append(dialogClass);
        }
        tagContent.append(">");
        tagContent.append("<div class=\"modal-content\">");
        if (!hideCloseBtn || StringUtils.isNotBlank(title)) {
            tagContent.append("<div class=\"modal-header\">");
            if (!hideCloseBtn) {
                tagContent.append("<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-hidden=\"true\"><span aria-hidden=\"true\">&times;</span><span class=\"sr-only\">Close</span></button>");
            }
            if (StringUtils.isNotBlank(title)) {
                tagContent.append("<h4 class=\"modal-title\">").append(title).append("</h4>");
            }
            tagContent.append("</div>");
        }
        tagContent.append("<div class=\"modal-body\">").append(bodyContent).append("</div>");
        if (StringUtils.isNotBlank(footer)) {
            tagContent.append("<div class=\"modal-footer\">").append(footer).append("</div>");
        }
        tagContent.append("</div>");
        tagContent.append("</div>");
        return __doTagEnd(tagContent);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isFade() {
        return fade;
    }

    public void setFade(boolean fade) {
        this.fade = fade;
    }

    public boolean isNonStatic() {
        return nonStatic;
    }

    public void setNonStatic(boolean nonStatic) {
        this.nonStatic = nonStatic;
    }

    public boolean isHideCloseBtn() {
        return hideCloseBtn;
    }

    public void setHideCloseBtn(boolean hideCloseBtn) {
        this.hideCloseBtn = hideCloseBtn;
    }

    public String getDialogClass() {
        return dialogClass;
    }

    public void setDialogClass(String dialogClass) {
        this.dialogClass = dialogClass;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public boolean isSmall() {
        return small;
    }

    public void setSmall(boolean small) {
        this.small = small;
    }

    public boolean isLarge() {
        return large;
    }

    public void setLarge(boolean large) {
        this.large = large;
    }
}
