/*
 * Copyright (c) 2007-2016, the original author or authors. All rights reserved.
 *
 * This program licensed under the terms of the GNU Lesser General Public License version 3.0
 * as published by the Free Software Foundation.
 */
package net.ymate.framework.addons.plugin;

import net.ymate.platform.core.YMP;
import net.ymate.platform.plugin.annotation.Handler;
import net.ymate.platform.validation.annotation.Validator;
import net.ymate.platform.validation.handle.ValidateHandler;

/**
 * 插件验证器类处理器
 *
 * @author 刘镇 (suninformation@163.com) on 15/8/15 上午9:31
 * @version 1.0
 */
@Handler(Validator.class)
public class ValidateBeanHandler extends ValidateHandler {

    public ValidateBeanHandler(YMP owner) throws Exception {
        super(owner);
    }
}
