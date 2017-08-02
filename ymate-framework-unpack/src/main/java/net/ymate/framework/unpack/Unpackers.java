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

import net.ymate.framework.unpack.annotation.Unpacker;
import net.ymate.framework.unpack.handle.UnpackerHandler;
import net.ymate.framework.unpack.impl.DefaultModuleCfg;
import net.ymate.platform.core.Version;
import net.ymate.platform.core.YMP;
import net.ymate.platform.core.module.IModule;
import net.ymate.platform.core.module.annotation.Module;
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
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author 刘镇 (suninformation@163.com) on 2017/08/02 下午 22:27
 * @version 1.0
 */
@Module
public class Unpackers implements IModule, IUnpackers {

    private static final Log _LOG = LogFactory.getLog(Unpackers.class);

    public static final Version VERSION = new Version(2, 0, 1, Unpackers.class.getPackage().getImplementationVersion(), Version.VersionType.Release);

    private static volatile IUnpackers __instance;

    private YMP __owner;

    private IUnpackersModuleCfg __moduleCfg;

    private boolean __inited;

    private Map<String, Class<? extends IUnpacker>> __unpackers = new HashMap<String, Class<? extends IUnpacker>>();

    public static IUnpackers get() {
        if (__instance == null) {
            synchronized (VERSION) {
                if (__instance == null) {
                    __instance = YMP.get().getModule(Unpackers.class);
                }
            }
        }
        return __instance;
    }

    public String getName() {
        return IUnpackers.MODULE_NAME;
    }

    public void init(YMP owner) throws Exception {
        if (!__inited) {
            //
            _LOG.info("Initializing ymate-framework-unpack-" + VERSION);
            //
            __owner = owner;
            __moduleCfg = new DefaultModuleCfg(owner);
            //
            __owner.registerHandler(Unpacker.class, new UnpackerHandler(this));
            //
            __inited = true;
        }
    }

    public boolean isInited() {
        return __inited;
    }

    @Override
    public void registerUnpacker(String name, Class<? extends IUnpacker> targetClass) {
        if (StringUtils.isNotBlank(name) && targetClass != null) {
            __unpackers.put(name, targetClass);
        }
    }

    public void registerUnpacker(Class<? extends IUnpacker> targetClass) {
        if (targetClass != null) {
            Unpacker _anno = targetClass.getAnnotation(Unpacker.class);
            if (_anno != null) {
                for (String _name : _anno.value()) {
                    __unpackers.put(_name, targetClass);
                }
            }
        }
    }

    @Override
    public synchronized void unpack() {
        for (Map.Entry<String, Class<? extends IUnpacker>> _entry : __unpackers.entrySet()) {
            String _prefixPath = "META-INF/" + _entry.getKey();
            File _locker = new File(RuntimeUtils.getRootPath(), ".unpack/" + _entry.getKey());
            if (!_locker.exists()) {
                try {
                    URL _uri = _entry.getValue().getResource("/" + _prefixPath);
                    if (_uri != null) {
                        URLConnection _conn = _uri.openConnection();
                        if (JarURLConnection.class.isInstance(_conn)) {
                            if (__unpack(((JarURLConnection) _conn).getJarFile(), _prefixPath)) {
                                _locker.getParentFile().mkdirs();
                                _locker.createNewFile();
                            }
                        }
                    }
                } catch (Exception e) {
                    _LOG.warn("Synchronizing resource [" + _entry.getKey() + "] exception", RuntimeUtils.unwrapThrow(e));
                }
            }
        }
    }

    private boolean __unpack(JarFile jarFile, String prefixPath) throws Exception {
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

    public void destroy() throws Exception {
        if (__inited) {
            __inited = false;
            //
            __unpackers = null;
            //
            __moduleCfg = null;
            __owner = null;
        }
    }

    public YMP getOwner() {
        return __owner;
    }

    public IUnpackersModuleCfg getModuleCfg() {
        return __moduleCfg;
    }
}
