/*
 * Copyright 2007-2018 the original author or authors.
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
package net.ymate.framework.webmvc;

import org.apache.commons.lang.StringUtils;

/**
 * @author 刘镇 (suninformation@163.com) on 2018/6/14 上午12:10
 * @version 1.0
 */
public class ErrorMsg {

    private int code;

    private String msg;

    public ErrorMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public WebResult toResult() {
        return WebResult.CODE(code).msg(StringUtils.trimToNull(msg));
    }
}
