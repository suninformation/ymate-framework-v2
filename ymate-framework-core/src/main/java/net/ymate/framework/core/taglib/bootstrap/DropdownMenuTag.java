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
 * 下拉菜单容器
 *
 * @author 刘镇 (suninformation@163.com) on 16/11/15 上午3:00
 * @version 1.0
 */
public class DropdownMenuTag extends ElementsTag {

    private boolean right;
    private boolean left;
    private boolean pull;

    public DropdownMenuTag() {
    }

    @Override
    protected StringBuilder __doTagStart() {
        StringBuilder _classSB = new StringBuilder(StringUtils.trimToEmpty(this.get_class()));
        _classSB.append(" dropdown-menu");
        if (right) {
            if (pull) {
                _classSB.append(" pull-right");
            } else {
                _classSB.append(" dropdown-menu-right");
            }
        } else if (left) {
            if (pull) {
                _classSB.append(" pull-left");
            } else {
                _classSB.append(" dropdown-menu-left");
            }
        }
        //
        this.set_class(_classSB.toString());
        //
        return super.__doTagStart();
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isPull() {
        return pull;
    }

    public void setPull(boolean pull) {
        this.pull = pull;
    }
}
