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
 * 输入框组容器
 *
 * @author 刘镇 (suninformation@163.com) on 16/11/13 上午8:37
 * @version 1.0
 */
public class InputGroupTag extends ElementsTag {

    private boolean addon;
    private boolean btn;

    private boolean small;
    private boolean large;

    public InputGroupTag() {
    }

    @Override
    protected StringBuilder __doTagStart() {
        StringBuilder _classSB = new StringBuilder(StringUtils.trimToEmpty(this.get_class()));
        if (addon || btn) {
            if (btn) {
                _classSB.append(" input-group-btn");
            } else {
                _classSB.append(" input-group-addon");
            }
        } else {
            _classSB.append(" input-group");
            if (small) {
                _classSB.append(" input-group-sm");
            } else if (large) {
                _classSB.append(" input-group-lg");
            }
        }
        //
        this.set_class(_classSB.toString());
        //
        return super.__doTagStart();
    }

    public boolean isAddon() {
        return addon;
    }

    public void setAddon(boolean addon) {
        this.addon = addon;
    }

    public boolean isBtn() {
        return btn;
    }

    public void setBtn(boolean btn) {
        this.btn = btn;
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
