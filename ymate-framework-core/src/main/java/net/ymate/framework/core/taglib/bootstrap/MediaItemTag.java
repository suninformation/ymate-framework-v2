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
 * 媒体列表项
 *
 * @author 刘镇 (suninformation@163.com) on 16/11/15 上午3:43
 * @version 1.0
 */
public class MediaItemTag extends ElementsTag {

    private String title;
    private String href;

    private String image;
    private String src;
    private String alt;
    private String width;
    private String height;

    private boolean right;
    private boolean middle;
    private boolean bottom;

    public MediaItemTag() {
    }

    @Override
    protected void __doSetTagName() {
        if (this.getParent() instanceof MediaListTag) {
            this.set_tag("li");
        }
        super.__doSetTagName();
    }

    @Override
    protected StringBuilder __doTagStart() {
        this.set_class(StringUtils.trimToEmpty(this.get_class()) + " media");
        //
        return super.__doTagStart();
    }

    @Override
    protected StringBuilder __doTagContent(StringBuilder tagContent, StringBuilder bodyContent) {
        StringBuilder _mediaBody = new StringBuilder("<div class=\"media-body\"><h4 class=\"media-heading\">")
                .append(StringUtils.trimToEmpty(title)).append("</h4>").append(bodyContent).append("</div>");
        StringBuilder _tmpSB = new StringBuilder();
        _tmpSB.append("<div class=\"");
        if (right) {
            _tmpSB.append(" media-right");
        } else {
            _tmpSB.append(" media-left");
        }
        if (middle) {
            _tmpSB.append(" media-middle");
        } else if (bottom) {
            _tmpSB.append(" media-bottom");
        }
        _tmpSB.append("\"><a href=\"").append(StringUtils.defaultIfBlank(href, "#")).append("\">");
        if (StringUtils.isNotBlank(image)) {
            _tmpSB.append(image);
        } else {
            _tmpSB.append("<img src=\"").append(StringUtils.trimToEmpty(src)).append("\" alt=\"").append(StringUtils.trimToEmpty(alt)).append("\"");
            if (StringUtils.isNotBlank(width)) {
                _tmpSB.append(" width=\"").append(width).append("\"");
            }
            if (StringUtils.isNotBlank(height)) {
                _tmpSB.append(" height=\"").append(height).append("\"");
            }
            _tmpSB.append(">");
        }
        _tmpSB.append("</a></div>");
        if (right) {
            _tmpSB.insert(0, _mediaBody);
        } else {
            _tmpSB.append(_mediaBody);
        }
        //
        return __doTagEnd(tagContent.append(_tmpSB));
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
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

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isMiddle() {
        return middle;
    }

    public void setMiddle(boolean middle) {
        this.middle = middle;
    }

    public boolean isBottom() {
        return bottom;
    }

    public void setBottom(boolean bottom) {
        this.bottom = bottom;
    }
}
