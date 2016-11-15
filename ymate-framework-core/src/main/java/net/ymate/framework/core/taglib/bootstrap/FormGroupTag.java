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
package net.ymate.framework.core.taglib.bootstrap;

import net.ymate.framework.core.taglib.ElementsTag;
import org.apache.commons.lang.StringUtils;

/**
 * 表单组容器
 *
 * @author 刘镇 (suninformation@163.com) on 16/11/13 上午2:55
 * @version 1.0
 */
public class FormGroupTag extends ElementsTag {

    private String feedbackIcon;
    private boolean hasWarning;
    private boolean hasError;
    private boolean hasSuccess;

    private boolean small;
    private boolean large;

    public FormGroupTag() {
    }

    @Override
    protected StringBuilder __doTagStart() {
        StringBuilder _classSB = new StringBuilder(StringUtils.trimToEmpty(this.get_class()));
        _classSB.append(" form-group");
        if (StringUtils.isNotBlank(feedbackIcon)) {
            _classSB.append(" has-feedback");
        }
        if (hasWarning) {
            _classSB.append(" has-warning");
        } else if (hasError) {
            _classSB.append(" has-error");
        } else if (hasSuccess) {
            _classSB.append(" has-success");
        }
        if (small) {
            _classSB.append(" form-group-sm");
        } else if (large) {
            _classSB.append(" form-group-lg");
        }
        //
        this.set_class(_classSB.toString());
        //
        return super.__doTagStart();
    }

    public String getFeedbackIcon() {
        return feedbackIcon;
    }

    public void setFeedbackIcon(String feedbackIcon) {
        this.feedbackIcon = feedbackIcon;
    }

    public boolean isHasWarning() {
        return hasWarning;
    }

    public void setHasWarning(boolean hasWarning) {
        this.hasWarning = hasWarning;
    }

    public boolean isHasError() {
        return hasError;
    }

    public void setHasError(boolean hasError) {
        this.hasError = hasError;
    }

    public boolean isHasSuccess() {
        return hasSuccess;
    }

    public void setHasSuccess(boolean hasSuccess) {
        this.hasSuccess = hasSuccess;
    }

    public boolean isSmall() {
        return small;
    }

    public void setSmall(boolean small) {
        this.small = small;
    }

    public boolean isLarge() {
        return large;
    }

    public void setLarge(boolean large) {
        this.large = large;
    }
}
