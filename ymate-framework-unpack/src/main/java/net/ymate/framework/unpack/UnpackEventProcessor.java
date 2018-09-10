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

import net.ymate.platform.core.ApplicationEvent;
import net.ymate.platform.core.event.Events;
import net.ymate.platform.core.event.IEventListener;
import net.ymate.platform.core.event.IEventRegister;
import net.ymate.platform.core.event.annotation.EventRegister;
import net.ymate.platform.core.module.ModuleEvent;

/**
 * @author 刘镇 (suninformation@163.com) on 16/9/30 下午3:33
 * @version 1.0
 */
@EventRegister
public class UnpackEventProcessor implements IEventRegister {

    @Override
    public void register(Events events) throws Exception {
        events.registerListener(ModuleEvent.class, new IEventListener<ModuleEvent>() {
            @Override
            public boolean handle(ModuleEvent context) {
                switch (context.getEventName()) {
                    case MODULE_INITED:
                        if (context.getSource() instanceof IUnpacker) {
                            Unpackers.get().registerUnpacker(context.getSource().getName(), ((IUnpacker) context.getSource()).getClass());
                        }
                        break;
                    default:
                }
                return false;
            }
        }).registerListener(ApplicationEvent.class, new IEventListener<ApplicationEvent>() {
            @Override
            public boolean handle(ApplicationEvent context) {
                switch (context.getEventName()) {
                    case APPLICATION_INITED:
                        Unpackers.get().unpack();
                        break;
                    default:
                }
                return false;
            }
        });

    }
}
