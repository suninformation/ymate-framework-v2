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
import net.ymate.platform.core.util.UUIDUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Carousel
 *
 * @author 刘镇 (suninformation@163.com) on 16/11/15 下午5:00
 * @version 1.0
 */
public class CarouselTag extends ElementsTag {

    private Integer indicators;
    private boolean controls;

    public CarouselTag() {
    }

    @Override
    protected StringBuilder __doTagStart() {
        this.set_id(StringUtils.defaultIfBlank(this.get_id(), UUIDUtils.UUID()));
        this.set_class(StringUtils.trimToEmpty(this.get_class()) + " carousel slide");
        //
        this.getDynamicAttributes().put("data-ride", "carousel");
        //
        return super.__doTagStart();
    }

    @Override
    protected StringBuilder __doTagContent(StringBuilder tagContent, StringBuilder bodyContent) {
        StringBuilder _tmpSB = new StringBuilder();
        if (indicators != null && indicators > 0) {
            _tmpSB.append("<ol class=\"carousel-indicators\">");
            for (int _idx = 0; _idx < indicators; _idx++) {
                _tmpSB.append("<li data-target=\"#").append(this.get_id()).append("\" data-slide-to=\"").append(_idx).append("\"");
                if (_idx == 0) {
                    _tmpSB.append(" class=\"active\"");
                }
                _tmpSB.append("></li>");
            }
            _tmpSB.append("</ol>");
        }
        _tmpSB.append("<div class=\"carousel-inner\" role=\"listbox\">").append(bodyContent).append("</div>");
        if (controls) {
            _tmpSB.append("<a class=\"left carousel-control\" href=\"#").append(this.get_id()).append("\" role=\"button\" data-slide=\"prev\"><span class=\"glyphicon glyphicon-chevron-left\"></span><span class=\"sr-only\">Previous</span></a>");
            _tmpSB.append("<a class=\"right carousel-control\" href=\"#").append(this.get_id()).append("\" role=\"button\" data-slide=\"next\"><span class=\"glyphicon glyphicon-chevron-right\"></span><span class=\"sr-only\">Next</span></a>");
        }
        return __doTagEnd(tagContent.append(_tmpSB));
    }

    public Integer getIndicators() {
        return indicators;
    }

    public void setIndicators(Integer indicators) {
        this.indicators = indicators;
    }

    public boolean isControls() {
        return controls;
    }

    public void setControls(boolean controls) {
        this.controls = controls;
    }
}
