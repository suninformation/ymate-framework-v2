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
 * 表格
 *
 * @author 刘镇 (suninformation@163.com) on 16/11/14 下午5:43
 * @version 1.0
 */
public class TableTag extends ElementsTag {

    private boolean responsive;
    private boolean striped;
    private boolean bordered;
    private boolean hover;
    private boolean condensed;

    public TableTag() {
    }

    @Override
    protected void __doSetTagName() {
        this.set_tag("table");
    }

    @Override
    protected StringBuilder __doTagStart() {
        StringBuilder _classSB = new StringBuilder(StringUtils.trimToEmpty(this.get_class()));
        _classSB.append(" table");
        //
        if (striped) {
            _classSB.append(" table-striped");
        }
        if (bordered) {
            _classSB.append(" table-bordered");
        }
        if (hover) {
            _classSB.append(" table-hover");
        }
        if (condensed) {
            _classSB.append(" table-condensed");
        }
        //
        this.set_class(_classSB.toString());
        //
        return super.__doTagStart();
    }

    @Override
    protected StringBuilder __doTagContent(StringBuilder tagContent, StringBuilder bodyContent) {
        if (responsive) {
            tagContent.insert(0, "<div class=\"table-responsive\">");
            return super.__doTagContent(tagContent, bodyContent).append("</div>");
        }
        return super.__doTagContent(tagContent, bodyContent);
    }

    public boolean isResponsive() {
        return responsive;
    }

    public void setResponsive(boolean responsive) {
        this.responsive = responsive;
    }

    public boolean isStriped() {
        return striped;
    }

    public void setStriped(boolean striped) {
        this.striped = striped;
    }

    public boolean isBordered() {
        return bordered;
    }

    public void setBordered(boolean bordered) {
        this.bordered = bordered;
    }

    public boolean isHover() {
        return hover;
    }

    public void setHover(boolean hover) {
        this.hover = hover;
    }

    public boolean isCondensed() {
        return condensed;
    }

    public void setCondensed(boolean condensed) {
        this.condensed = condensed;
    }
}
