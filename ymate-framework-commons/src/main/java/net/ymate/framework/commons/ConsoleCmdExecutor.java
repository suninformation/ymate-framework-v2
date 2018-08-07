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
package net.ymate.framework.commons;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

/**
 * 控制台命令执行器
 *
 * @author 刘镇 (suninformation@163.com) on 16/7/22 下午1:41
 * @version 1.0
 */
public class ConsoleCmdExecutor {

    public static String exec(String... command) throws Exception {
        return exec(Arrays.asList(command));
    }

    public static String exec(List<String> command) throws Exception {
        return exec(command, new ICmdOutputHandler<String>() {
            @Override
            public String handle(BufferedReader reader) throws Exception {
                StringBuilder _sb = new StringBuilder();
                String _line = null;
                while ((_line = reader.readLine()) != null) {
                    _sb.append(_line).append("\r\n");
                }
                return _sb.toString();
            }
        });
    }

    public static <T> T exec(String[] command, ICmdOutputHandler<T> handler) throws Exception {
        return exec(Arrays.asList(command), handler);
    }

    public static <T> T exec(List<String> command, ICmdOutputHandler<T> handler) throws Exception {
        Process _process = null;
        BufferedReader _outputBuff = null;
        try {
            // 执行命令
            _process = new ProcessBuilder(command).redirectErrorStream(true).start();
            // 读取命令输出流
            _outputBuff = new BufferedReader(new InputStreamReader(_process.getInputStream()), 1024);
            T _result = handler.handle(_outputBuff);
            // 线程阻塞，等待外部转换进程运行成功运行结束
            _process.waitFor();
            //
            return _result;
        } finally {
            if (_process != null) {
                _process.destroy();
            }
            if (_outputBuff != null) {
                _outputBuff.close();
            }
        }
    }
}
