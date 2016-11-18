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
 * 字体图标
 *
 * @author 刘镇 (suninformation@163.com) on 16/11/13 上午8:07
 * @version 1.0
 */
public class IconTag extends ElementsTag {

    private boolean fa;
    private boolean faW;
    private Integer faX;

    private boolean faStack;
    private boolean faStackLarge;
    private boolean faStack1X;
    private boolean faStack2X;

    private boolean faBorder;
    private boolean faLeft;
    private boolean faRight;
    private boolean faSpin;
    private boolean faPulse;

    private boolean faFlipH;
    private boolean faFlipV;

    private boolean faRotate90;
    private boolean faRotate180;
    private boolean faRotate270;

    private boolean faInverse;

    private String style;

    private boolean caret;

    public IconTag() {
    }

    @Override
    protected void __doSetTagName() {
        this.set_tag("span");
    }

    @Override
    protected StringBuilder __doTagStart() {
        StringBuilder _classSB = new StringBuilder(StringUtils.trimToEmpty(this.get_class()));
        if (caret) {
            _classSB.append(" caret");
        } else if (faStack) {
            _classSB.append(" fa-stack");
            if (faStackLarge) {
                _classSB.append(" fa-lg");
            }
        } else if (fa) {
            _classSB.append(" fa fa-").append(style);
            if (faX != null && faX > 0) {
                if (faX > 5) {
                    faX = 5;
                }
                _classSB.append(" fa-").append(faX).append("x");
            }
            if (faW) {
                _classSB.append(" fa-fw");
            }
            if (faBorder) {
                _classSB.append(" fa-border");
            }
            //
            if (faLeft) {
                _classSB.append(" fa-pull-left");
            } else if (faRight) {
                _classSB.append(" fa-pull-right");
            }
            //
            if (faSpin) {
                _classSB.append(" fa-spin");
            } else if (faPulse) {
                _classSB.append(" fa-pulse");
            }
            //
            if (faFlipH) {
                _classSB.append(" fa-flip-horizontal");
            } else if (faFlipV) {
                _classSB.append(" fa-flip-vertical");
            }
            //
            if (faRotate90) {
                _classSB.append(" fa-rotate-90");
            } else if (faRotate180) {
                _classSB.append(" fa-rotate-180");
            } else if (faRotate270) {
                _classSB.append(" fa-rotate-270");
            }
            //
            if (faStack1X) {
                _classSB.append(" fa-stack-1x");
            } else if (faStack2X) {
                _classSB.append(" fa-stack-2x");
            }
            //
            if (faInverse) {
                _classSB.append(" fa-inverse");
            }
        } else {
            _classSB.append(" glyphicon glyphicon-").append(style);
        }
        //
        this.set_class(_classSB.toString());
        //
        return super.__doTagStart();
    }

    @Override
    protected StringBuilder __doTagContent(StringBuilder tagContent, StringBuilder bodyContent) {
        if (faStack) {
            return __doTagEnd(tagContent.append(bodyContent));
        }
        return __doTagEnd(tagContent).append(bodyContent);
    }

    public boolean isFa() {
        return fa;
    }

    public void setFa(boolean fa) {
        this.fa = fa;
    }

    public boolean isFaW() {
        return faW;
    }

    public void setFaW(boolean faW) {
        this.faW = faW;
    }

    public Integer getFaX() {
        return faX;
    }

    public void setFaX(Integer faX) {
        this.faX = faX;
    }

    public boolean isFaStack() {
        return faStack;
    }

    public void setFaStack(boolean faStack) {
        this.faStack = faStack;
    }

    public boolean isFaStackLarge() {
        return faStackLarge;
    }

    public void setFaStackLarge(boolean faStackLarge) {
        this.faStackLarge = faStackLarge;
    }

    public boolean isFaStack1X() {
        return faStack1X;
    }

    public void setFaStack1X(boolean faStack1X) {
        this.faStack1X = faStack1X;
    }

    public boolean isFaStack2X() {
        return faStack2X;
    }

    public void setFaStack2X(boolean faStack2X) {
        this.faStack2X = faStack2X;
    }

    public boolean isFaBorder() {
        return faBorder;
    }

    public void setFaBorder(boolean faBorder) {
        this.faBorder = faBorder;
    }

    public boolean isFaLeft() {
        return faLeft;
    }

    public void setFaLeft(boolean faLeft) {
        this.faLeft = faLeft;
    }

    public boolean isFaRight() {
        return faRight;
    }

    public void setFaRight(boolean faRight) {
        this.faRight = faRight;
    }

    public boolean isFaSpin() {
        return faSpin;
    }

    public void setFaSpin(boolean faSpin) {
        this.faSpin = faSpin;
    }

    public boolean isFaPulse() {
        return faPulse;
    }

    public void setFaPulse(boolean faPulse) {
        this.faPulse = faPulse;
    }

    public boolean isFaFlipH() {
        return faFlipH;
    }

    public void setFaFlipH(boolean faFlipH) {
        this.faFlipH = faFlipH;
    }

    public boolean isFaFlipV() {
        return faFlipV;
    }

    public void setFaFlipV(boolean faFlipV) {
        this.faFlipV = faFlipV;
    }

    public boolean isFaRotate90() {
        return faRotate90;
    }

    public void setFaRotate90(boolean faRotate90) {
        this.faRotate90 = faRotate90;
    }

    public boolean isFaRotate180() {
        return faRotate180;
    }

    public void setFaRotate180(boolean faRotate180) {
        this.faRotate180 = faRotate180;
    }

    public boolean isFaRotate270() {
        return faRotate270;
    }

    public void setFaRotate270(boolean faRotate270) {
        this.faRotate270 = faRotate270;
    }

    public boolean isFaInverse() {
        return faInverse;
    }

    public void setFaInverse(boolean faInverse) {
        this.faInverse = faInverse;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public boolean isCaret() {
        return caret;
    }

    public void setCaret(boolean caret) {
        this.caret = caret;
    }
}
