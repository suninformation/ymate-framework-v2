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
 * CarouselItem
 *
 * @author 刘镇 (suninformation@163.com) on 16/11/15 下午5:00
 * @version 1.0
 */
public class CarouselItemTag extends ElementsTag {

    private String caption;
    private boolean active;

    private String src;
    private String dataSrc;
    private String width;
    private String height;
    private String alt;
    private boolean responsive;

    public CarouselItemTag() {
    }

    @Override
    protected StringBuilder __doTagStart() {
        StringBuilder _classSB = new StringBuilder(StringUtils.trimToEmpty(this.get_class()));
        _classSB.append(" item");
        if (active) {
            _classSB.append(" active");
        }
        //
        this.set_class(_classSB.toString());
        //
        return super.__doTagStart();
    }

    @Override
    protected StringBuilder __doTagContent(StringBuilder tagContent, StringBuilder bodyContent) {
        StringBuilder _tmpSB = new StringBuilder();
        if (StringUtils.isNotBlank(src) || StringUtils.isNotBlank(dataSrc)) {
            _tmpSB.append("<img");
            if (responsive) {
                _tmpSB.append(" class=\"img-responsive\"");
            }
            if (StringUtils.isNotBlank(src)) {
                _tmpSB.append(" src=\"").append(src).append("\"");
            } else {
                _tmpSB.append(" data-src=\"").append(dataSrc).append("\"");
            }
            if (StringUtils.isNotBlank(width)) {
                _tmpSB.append(" width=\"").append(width).append("\"");
            }
            if (StringUtils.isNotBlank(height)) {
                _tmpSB.append(" height=\"").append(height).append("\"");
            }
            _tmpSB.append(" alt=\"").append(StringUtils.trimToEmpty(alt)).append("\">");
        } else {
            _tmpSB.append(bodyContent);
        }
        _tmpSB.append("<div class=\"carousel-caption\">").append(StringUtils.trimToEmpty(caption)).append("</div>");
        return __doTagEnd(tagContent.append(_tmpSB));
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getDataSrc() {
        return dataSrc;
    }

    public void setDataSrc(String dataSrc) {
        this.dataSrc = dataSrc;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public boolean isResponsive() {
        return responsive;
    }

    public void setResponsive(boolean responsive) {
        this.responsive = responsive;
    }
}
