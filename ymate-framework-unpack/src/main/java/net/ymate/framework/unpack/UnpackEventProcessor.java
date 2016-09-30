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

import net.ymate.platform.core.event.Events;
import net.ymate.platform.core.event.IEventListener;
import net.ymate.platform.core.event.IEventRegister;
import net.ymate.platform.core.event.annotation.EventRegister;
import net.ymate.platform.core.module.ModuleEvent;
import net.ymate.platform.core.util.RuntimeUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author 刘镇 (suninformation@163.com) on 16/9/30 下午3:33
 * @version 1.0
 */
@EventRegister
public class UnpackEventProcessor implements IEventRegister {

    private static final Log _LOG = LogFactory.getLog(UnpackEventProcessor.class);

    public void register(Events events) throws Exception {
        events.registerListener(ModuleEvent.class, new IEventListener<ModuleEvent>() {
            @Override
            public boolean handle(ModuleEvent context) {
                switch (context.getEventName()) {
                    case MODULE_INITED:
                        if (context.getSource() instanceof IUnpacker) {
                            String _name = StringUtils.trimToNull(context.getSource().getName());
                            if (_name != null) {
                                String _prefixPath = "META-INF/" + _name;
                                File _locker = new File(RuntimeUtils.getRootPath(), ".unpack/" + _name);
                                if (!_locker.exists()) {
                                    try {
                                        URL _uri = context.getSource().getClass().getResource("/" + _prefixPath);
                                        if (_uri != null) {
                                            URLConnection _conn = _uri.openConnection();
                                            if (JarURLConnection.class.isInstance(_conn)) {
                                                if (__doSyncJarSources(((JarURLConnection) _conn).getJarFile(), _prefixPath)) {
                                                    _locker.getParentFile().mkdirs();
                                                    _locker.createNewFile();
                                                }
                                            }
                                        }
                                    } catch (Exception e) {
                                        _LOG.warn(e.getMessage(), RuntimeUtils.unwrapThrow(e));
                                    }
                                }
                            }
                        }
                    default:
                        break;
                }
                return false;
            }
        });
    }

    private boolean __doSyncJarSources(JarFile jarFile, String prefixPath) throws Exception {
        boolean _results = false;
        Enumeration<JarEntry> _entriesEnum = jarFile.entries();
        for (; _entriesEnum.hasMoreElements(); ) {
            JarEntry _entry = _entriesEnum.nextElement();
            if (StringUtils.startsWith(_entry.getName(), prefixPath)) {
                if (!_entry.isDirectory()) {
                    _LOG.info("Synchronizing resource file: " + _entry.getName());
                    //
                    String _entryName = StringUtils.substringAfter(_entry.getName(), prefixPath);
                    File _targetFile = new File(RuntimeUtils.getRootPath(false), _entryName);
                    _targetFile.getParentFile().mkdirs();
                    IOUtils.copyLarge(jarFile.getInputStream(_entry), new FileOutputStream(_targetFile));
                    _results = true;
                }
            }
        }
        return _results;
    }
}
