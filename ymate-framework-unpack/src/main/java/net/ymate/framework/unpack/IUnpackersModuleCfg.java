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

/**
 * @author 刘镇 (suninformation@163.com) on 2017/08/02 下午 22:27
 * @version 1.0
 */
public interface IUnpackersModuleCfg {

    String DISABLED = "disabled";

    String DISABLED_UNPACKER_LIST = "disabled_unpacker_list";

    /**
     * @return 是否禁用解包器模块, 默认值: false
     */
    boolean isDisabled();

    /**
     * @return 禁止解包列表, 多个包名称之间使用'|'分隔, 默认值：空
     */
    String[] getDisabledUnpackers();
}