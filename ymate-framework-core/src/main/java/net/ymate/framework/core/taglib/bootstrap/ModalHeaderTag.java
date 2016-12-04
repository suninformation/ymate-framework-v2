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
 * 模态框头
 *
 * @author 刘镇 (suninformation@163.com) on 16/12/4 上午10:34
 * @version 1.0
 */
public class ModalHeaderTag extends ElementsTag {

    private String title;

    private boolean hideCloseBtn;

    public ModalHeaderTag() {
    }

    @Override
    protected StringBuilder __doTagStart() {
        //
        this.set_class(StringUtils.trimToEmpty(this.get_class()) + " modal-header");
        //
        return super.__doTagStart();
    }

    @Override
    protected StringBuilder __doTagContent(StringBuilder tagContent, StringBuilder bodyContent) {
        if (!hideCloseBtn) {
            tagContent.append("<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-hidden=\"true\"><span aria-hidden=\"true\">&times;</span><span class=\"sr-only\">Close</span></button>");
        }
        if (StringUtils.isNotBlank(title)) {
            tagContent.append("<h4 class=\"modal-title\">").append(title).append("</h4>");
        }
        return super.__doTagContent(tagContent, bodyContent);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isHideCloseBtn() {
        return hideCloseBtn;
    }

    public void setHideCloseBtn(boolean hideCloseBtn) {
        this.hideCloseBtn = hideCloseBtn;
    }
}
