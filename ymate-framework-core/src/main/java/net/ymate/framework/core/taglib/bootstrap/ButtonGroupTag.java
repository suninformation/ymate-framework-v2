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
 * 按钮组容器
 *
 * @author 刘镇 (suninformation@163.com) on 16/11/13 上午2:42
 * @version 1.0
 */
public class ButtonGroupTag extends ElementsTag {

    private boolean toolbar;
    private boolean vertical;
    private boolean justified;

    private boolean dropup;

    private boolean mini;
    private boolean small;
    private boolean large;

    public ButtonGroupTag() {
    }

    @Override
    protected StringBuilder __doTagStart() {
        StringBuilder _classSB = new StringBuilder(StringUtils.trimToEmpty(this.get_class()));
        if (toolbar) {
            _classSB.append(" btn-toolbar");
        } else {
            _classSB.append(" btn-group");
            if (vertical) {
                _classSB.append("-vertical");
            }
            if (justified) {
                _classSB.append(" btn-group-justified");
            }
            if (dropup) {
                _classSB.append(" dropup");
            }
            if (mini) {
                _classSB.append(" btn-group-xs");
            } else if (small) {
                _classSB.append(" btn-group-sm");
            } else if (large) {
                _classSB.append(" btn-group-lg");
            }
        }
        //
        this.set_class(_classSB.toString());
        //
        return super.__doTagStart();
    }

    public boolean isToolbar() {
        return toolbar;
    }

    public void setToolbar(boolean toolbar) {
        this.toolbar = toolbar;
    }

    public boolean isVertical() {
        return vertical;
    }

    public void setVertical(boolean vertical) {
        this.vertical = vertical;
    }

    public boolean isJustified() {
        return justified;
    }

    public void setJustified(boolean justified) {
        this.justified = justified;
    }

    public boolean isDropup() {
        return dropup;
    }

    public void setDropup(boolean dropup) {
        this.dropup = dropup;
    }

    public boolean isMini() {
        return mini;
    }

    public void setMini(boolean mini) {
        this.mini = mini;
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
