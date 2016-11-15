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
 * 进度条容器
 *
 * @author 刘镇 (suninformation@163.com) on 16/11/15 下午6:35
 * @version 1.0
 */
public class ProgressTag extends ElementsTag {

    private boolean striped;
    private boolean active;

    public ProgressTag() {
    }

    @Override
    protected StringBuilder __doTagStart() {
        StringBuilder _classSB = new StringBuilder(StringUtils.trimToEmpty(this.get_class()));
        _classSB.append(" progress");
        if (striped) {
            _classSB.append(" progress-striped");
        }
        if (active) {
            _classSB.append(" active");
        }
        //
        this.set_class(_classSB.toString());
        //
        return super.__doTagStart();
    }

    public boolean isStriped() {
        return striped;
    }

    public void setStriped(boolean striped) {
        this.striped = striped;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
