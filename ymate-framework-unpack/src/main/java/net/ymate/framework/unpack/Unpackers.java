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
import net.ymate.framework.unpack.impl.DefaultUnpackerModuleCfg;
import net.ymate.platform.core.Version;
import net.ymate.platform.core.YMP;
import net.ymate.platform.core.module.IModule;
import net.ymate.platform.core.module.annotation.Module;
import net.ymate.platform.core.util.FileUtils;
import net.ymate.platform.core.util.RuntimeUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 刘镇 (suninformation@163.com) on 2017/08/02 下午 22:27
 * @version 1.0
 */
@Module
public class Unpackers implements IModule, IUnpackers {

    private static final Log _LOG = LogFactory.getLog(Unpackers.class);

    public static final Version VERSION = new Version(2, 0, 8, Unpackers.class.getPackage().getImplementationVersion(), Version.VersionType.Release);

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

    @Override
    public String getName() {
        return IUnpackers.MODULE_NAME;
    }

    @Override
    public void init(YMP owner) {
        if (!__inited) {
            //
            _LOG.info("Initializing ymate-framework-unpack-" + VERSION);
            //
            __owner = owner;
            __moduleCfg = new DefaultUnpackerModuleCfg(owner);
            //
            if (!__moduleCfg.isDisabled()) {
                __owner.registerHandler(Unpacker.class, new UnpackerHandler(this));
            }
            //
            __inited = true;
        }
    }

    @Override
    public boolean isInited() {
        return __inited;
    }

    @Override
    public void registerUnpacker(String name, Class<? extends IUnpacker> targetClass) {
        if (!__moduleCfg.isDisabled() && StringUtils.isNotBlank(name) && targetClass != null) {
            __unpackers.put(name, targetClass);
        }
    }

    @Override
    public void registerUnpacker(Class<? extends IUnpacker> targetClass) {
        if (!__moduleCfg.isDisabled() && targetClass != null) {
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
        boolean _flag = false;
        File _targetFile = new File(RuntimeUtils.getRootPath(false));
        String _rootPath = RuntimeUtils.getRootPath();
        for (Map.Entry<String, Class<? extends IUnpacker>> _entry : __unpackers.entrySet()) {
            if (!ArrayUtils.contains(__moduleCfg.getDisabledUnpackers(), _entry.getKey())) {
                File _locker = new File(_rootPath, ".unpack" + File.separator + _entry.getKey());
                if (!_locker.exists()) {
                    try {
                        _flag = true;
                        _LOG.info("Unpacking " + _entry.getValue() + "...");
                        if (FileUtils.unpackJarFile(_entry.getKey(), _targetFile, _entry.getValue())) {
                            _locker.getParentFile().mkdirs();
                            _locker.createNewFile();
                        }
                    } catch (Exception e) {
                        _LOG.warn("Synchronizing resource [" + _entry.getKey() + "] exception", RuntimeUtils.unwrapThrow(e));
                    }
                }
            }
        }
        if (_flag) {
            _LOG.info("Synchronizing resource completed.");
        }
    }

    @Override
    public void destroy() {
        if (__inited) {
            __inited = false;
            //
            __unpackers = null;
            //
            __moduleCfg = null;
            __owner = null;
        }
    }

    @Override
    public YMP getOwner() {
        return __owner;
    }

    @Override
    public IUnpackersModuleCfg getModuleCfg() {
        return __moduleCfg;
    }
}
