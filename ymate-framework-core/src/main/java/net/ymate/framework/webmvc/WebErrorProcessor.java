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
package net.ymate.framework.webmvc;

import net.ymate.framework.core.util.ViewPathUtils;
import net.ymate.platform.webmvc.IRequestContext;
import net.ymate.platform.webmvc.IWebMvc;
import net.ymate.platform.webmvc.impl.DefaultWebErrorProcessor;
import net.ymate.platform.webmvc.view.IView;

/**
 * 默认WebMVC框架异常错误处理器接口
 *
 * @author 刘镇 (suninformation@163.com) on 14/7/6 下午1:47
 * @version 1.0
 */
public class WebErrorProcessor extends DefaultWebErrorProcessor {
    @Override
    public IView onConvention(IWebMvc owner, IRequestContext requestContext) throws Exception {
        return ViewPathUtils.convention(owner, requestContext);
    }
}
