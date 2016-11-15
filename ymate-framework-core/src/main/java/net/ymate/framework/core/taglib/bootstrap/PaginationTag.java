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
 * 分页
 *
 * @author 刘镇 (suninformation@163.com) on 16/11/15 上午1:11
 * @version 1.0
 */
public class PaginationTag extends ElementsTag {

    private boolean pager;

    private boolean nonNav;

    private boolean small;
    private boolean large;

    public PaginationTag() {
    }

    @Override
    protected void __doSetTagName() {
        this.set_tag(StringUtils.defaultIfBlank(this.get_tag(), "ul"));
    }

    @Override
    protected StringBuilder __doTagStart() {
        StringBuilder _classSB = new StringBuilder(StringUtils.trimToEmpty(this.get_class()));
        if (pager) {
            _classSB.append(" pager");
        } else {
            _classSB.append(" pagination");
            if (small) {
                _classSB.append(" pagination-sm");
            } else if (large) {
                _classSB.append(" pagination-lg");
            }
        }
        //
        this.set_class(_classSB.toString());
        //
        return super.__doTagStart();
    }

    @Override
    protected StringBuilder __doTagContent(StringBuilder tagContent, StringBuilder bodyContent) {
        if (!nonNav) {
            return super.__doTagContent(tagContent.insert(0, "<nav>"), bodyContent).append("</nav>");
        }
        return super.__doTagContent(tagContent, bodyContent);
    }

    public boolean isPager() {
        return pager;
    }

    public void setPager(boolean pager) {
        this.pager = pager;
    }

    public boolean isNonNav() {
        return nonNav;
    }

    public void setNonNav(boolean nonNav) {
        this.nonNav = nonNav;
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
