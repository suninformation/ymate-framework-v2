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
package net.ymate.framework.unpack;

import net.ymate.platform.core.YMP;

/**
 * @author 刘镇 (suninformation@163.com) on 2017/08/02 下午 22:27
 * @version 1.0
 */
public interface IUnpackers {

    String MODULE_NAME = "Unpackers";

    /**
     * @return 返回所属YMP框架管理器实例
     */
    YMP getOwner();

    /**
     * @return 返回模块配置对象
     */
    IUnpackersModuleCfg getModuleCfg();

    /**
     * @return 返回模块是否已初始化
     */
    boolean isInited();

    /**
     * @param name        待提取资源名称
     * @param targetClass 目标类型
     */
    void registerUnpacker(String name, Class<? extends IUnpacker> targetClass);

    void registerUnpacker(Class<? extends IUnpacker> targetClass);

    /**
     * 执行资源提取
     */
    void unpack();
}