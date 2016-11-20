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
 * 面板
 *
 * @author 刘镇 (suninformation@163.com) on 16/11/12 上午7:49
 * @version 1.0
 */
public class PanelTag extends ElementsTag {

    private boolean tabs;

    private String style;

    private boolean active;
    private boolean fade;

    private String heading;
    private String title;
    private String icon;

    public PanelTag() {
    }

    @Override
    protected StringBuilder __doTagStart() {
        StringBuilder _classSB = new StringBuilder(StringUtils.trimToEmpty(this.get_class()));
        if (tabs) {
            _classSB.append(" tab-pane");
            //
            if (fade) {
                _classSB.append(" fade");
            }
            if (active) {
                if (fade) {
                    _classSB.append(" in");
                }
                _classSB.append(" active");
            }
        } else {
            _classSB.append(" panel panel-").append(StringUtils.defaultIfBlank(style, "default"));
        }
        //
        this.set_class(_classSB.toString());
        //
        return super.__doTagStart();
    }

    @Override
    protected StringBuilder __doTagContent(StringBuilder tagContent, StringBuilder bodyContent) {
        if (!tabs) {
            if (StringUtils.isNotBlank(heading) || StringUtils.isNotBlank(title)) {
                tagContent.append("<div class=\"panel-heading\">");
                if (StringUtils.isNotBlank(heading)) {
                    tagContent.append(heading);
                } else if (StringUtils.isNotBlank(title)) {
                    tagContent.append("<h3 class=\"panel-title\">");
                    if (StringUtils.isNotBlank(icon)) {
                        tagContent.append("<i class=\"fa fa-").append(icon).append(" fa-fw\"></i> ");
                    }
                    tagContent.append(title).append("</h3>");
                }
                tagContent.append("</div>");
            }
        }
        return super.__doTagContent(tagContent, bodyContent);
    }

    public boolean isTabs() {
        return tabs;
    }

    public void setTabs(boolean tabs) {
        this.tabs = tabs;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isFade() {
        return fade;
    }

    public void setFade(boolean fade) {
        this.fade = fade;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
