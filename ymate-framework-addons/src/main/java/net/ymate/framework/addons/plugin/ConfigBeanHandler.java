/*
 * Copyright (c) 2007-2107, the original author or authors. All rights reserved.
 *
 * This program licensed under the terms of the GNU Lesser General Public License version 3.0
 * as published by the Free Software Foundation.
 */
package net.ymate.framework.addons.plugin;

import net.ymate.platform.configuration.annotation.Configuration;
import net.ymate.platform.configuration.handle.ConfigHandler;
import net.ymate.platform.core.YMP;
import net.ymate.platform.plugin.annotation.Handler;

/**
 * 插件配置文件加载路径注解
 *
 * @author 刘镇 (suninformation@163.com) on 15/8/15 上午10:27
 * @version 1.0
 */
@Handler(Configuration.class)
public class ConfigBeanHandler extends ConfigHandler {

    public ConfigBeanHandler(YMP owner) throws Exception {
        super(owner);
    }
}
