/*
 * Copyright (c) 2007-2016, the original author or authors. All rights reserved.
 *
 * This program licensed under the terms of the GNU Lesser General Public License version 3.0
 * as published by the Free Software Foundation.
 */
package net.ymate.framework.addons.plugin;

import net.ymate.platform.core.YMP;
import net.ymate.platform.persistence.annotation.Entity;
import net.ymate.platform.persistence.handle.EntityHandler;
import net.ymate.platform.plugin.annotation.Handler;

/**
 * 插件数据实体类处理器
 *
 * @author 刘镇 (suninformation@163.com) on 15/8/16 下午5:14
 * @version 1.0
 */
@Handler(Entity.class)
public class EntityBeanHandler extends EntityHandler {

    public EntityBeanHandler(YMP owner) throws Exception {
        super(owner);
    }
}
