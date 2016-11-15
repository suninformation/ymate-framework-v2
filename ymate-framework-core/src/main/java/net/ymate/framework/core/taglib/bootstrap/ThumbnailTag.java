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
 * 缩略图
 *
 * @author 刘镇 (suninformation@163.com) on 16/11/16 上午2:38
 * @version 1.0
 */
public class ThumbnailTag extends ElementsTag {

    private String caption;

    private String src;
    private String dataSrc;
    private String width;
    private String height;
    private String alt;
    private boolean responsive;

    private String href;
    private boolean blank;

    public ThumbnailTag() {
    }

    @Override
    protected void __doSetTagName() {
        if (StringUtils.isBlank(caption)) {
            this.set_tag("a");
            if (StringUtils.isNotBlank(href)) {
                this.getDynamicAttributes().put("href", href);
                if (blank) {
                    this.getDynamicAttributes().put("target", "_blank");
                }
            }
        } else {
            super.__doSetTagName();
        }
    }

    @Override
    protected StringBuilder __doTagStart() {
        //
        this.set_class(StringUtils.trimToEmpty(this.get_class()) + " thumbnail");
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
        if (StringUtils.isNotBlank(caption)) {
            _tmpSB.append("<div class=\"caption\">").append(caption).append("</div>");
        }
        return __doTagEnd(tagContent.append(_tmpSB));
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
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

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public boolean isBlank() {
        return blank;
    }

    public void setBlank(boolean blank) {
        this.blank = blank;
    }
}
