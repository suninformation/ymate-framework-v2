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
 * 导航
 *
 * @author 刘镇 (suninformation@163.com) on 16/11/14 下午5:04
 * @version 1.0
 */
public class NavTag extends ElementsTag {

    private boolean left;
    private boolean right;

    private boolean tabs;
    private boolean pills;
    private boolean stacked;
    private boolean justified;

    public NavTag() {
    }

    @Override
    protected void __doSetTagName() {
        this.set_tag(StringUtils.defaultIfBlank(this.get_tag(), "ul"));
    }

    @Override
    protected StringBuilder __doTagStart() {
        StringBuilder _classSB = new StringBuilder(StringUtils.trimToEmpty(this.get_class()));
        _classSB.append(" nav");
        if (this.getParent() instanceof NavbarTag) {
            _classSB.append(" navbar-nav");
            if (left) {
                _classSB.append(" navbar-left");
            } else if (right) {
                _classSB.append(" navbar-right");
            }
        } else {
            if (tabs) {
                _classSB.append(" nav-tabs");
            } else if (pills) {
                _classSB.append(" nav-pills");
                if (stacked) {
                    _classSB.append(" nav-stacked");
                }
            }
        }
        //
        if (justified) {
            _classSB.append(" nav-justified");
        }
        //
        this.set_class(_classSB.toString());
        //
        return super.__doTagStart();
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

    public boolean isTabs() {
        return tabs;
    }

    public void setTabs(boolean tabs) {
        this.tabs = tabs;
    }

    public boolean isPills() {
        return pills;
    }

    public void setPills(boolean pills) {
        this.pills = pills;
    }

    public boolean isStacked() {
        return stacked;
    }

    public void setStacked(boolean stacked) {
        this.stacked = stacked;
    }

    public boolean isJustified() {
        return justified;
    }

    public void setJustified(boolean justified) {
        this.justified = justified;
    }
}
