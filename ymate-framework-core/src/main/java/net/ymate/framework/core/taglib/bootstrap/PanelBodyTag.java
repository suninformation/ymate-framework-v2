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
 * 面板内容体
 *
 * @author 刘镇 (suninformation@163.com) on 16/11/20 上午2:26
 * @version 1.0
 */
public class PanelBodyTag extends ElementsTag {

    public PanelBodyTag() {
    }

    @Override
    protected StringBuilder __doTagStart() {
        //
        this.set_class(StringUtils.trimToEmpty(this.get_class()) + " panel-body");
        //
        return super.__doTagStart();
    }
}
