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

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 刘镇 (suninformation@163.com) on 16/5/22 上午6:52
 * @version 1.0
 */
public class XPathHelper {

    public static final DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

    public static final XPathFactory xpathFactory = XPathFactory.newInstance();

    private XPath __path;

    private Document __document;

    public XPathHelper(Document document) {
        __path = xpathFactory.newXPath();
        __document = document;
    }

    public XPathHelper(InputSource inputSource) throws Exception {
        this(documentFactory.newDocumentBuilder().parse(inputSource));
    }

    public XPathHelper(String content) throws Exception {
        StringReader _reader = null;
        try {
            _reader = new StringReader(content);
            __document = documentFactory.newDocumentBuilder().parse(new InputSource(_reader));
            __path = xpathFactory.newXPath();
        } finally {
            IOUtils.closeQuietly(_reader);
        }
    }

    public Document getDocument() {
        return __document;
    }

    public Map<String, Object> toMap() {
        return toMap(__document.getDocumentElement());
    }

    public Map<String, Object> toMap(Node parent) {
        Map<String, Object> _returnMap = new HashMap<String, Object>();
        NodeList _nodes = parent.getChildNodes();
        for (int _idx = 0; _idx < _nodes.getLength(); _idx++) {
            Node _node = _nodes.item(_idx);
            if (_node.getNodeType() == Node.ELEMENT_NODE) {
                // 多级标签不予支持
                if (_node.hasChildNodes() && _node.getChildNodes().getLength() == 1) {
                    _returnMap.put(_node.getNodeName(), _node.getTextContent());
                }
            }
        }
        return _returnMap;
    }

    private Object __doEvaluate(String expression, Object item, QName returnType) throws XPathExpressionException {
        return __path.evaluate(expression, (null == item ? __document : item), returnType);
    }

    public String getStringValue(String expression) throws XPathExpressionException {
        return (String) __doEvaluate(expression, null, XPathConstants.STRING);
    }

    public Number getNumberValue(String expression) throws XPathExpressionException {
        return (Number) __doEvaluate(expression, null, XPathConstants.NUMBER);
    }

    public Boolean getBooleanValue(String expression) throws XPathExpressionException {
        return (Boolean) __doEvaluate(expression, null, XPathConstants.BOOLEAN);
    }

    public Node getNode(String expression) throws XPathExpressionException {
        return (Node) __doEvaluate(expression, null, XPathConstants.NODE);
    }

    public NodeList getNodeList(String expression) throws XPathExpressionException {
        return (NodeList) __doEvaluate(expression, null, XPathConstants.NODESET);
    }

    //

    public String getStringValue(Object item, String expression) throws XPathExpressionException {
        return (String) __doEvaluate(expression, item, XPathConstants.STRING);
    }

    public Number getNumberValue(Object item, String expression) throws XPathExpressionException {
        return (Number) __doEvaluate(expression, item, XPathConstants.NUMBER);
    }

    public Boolean getBooleanValue(Object item, String expression) throws XPathExpressionException {
        return (Boolean) __doEvaluate(expression, item, XPathConstants.BOOLEAN);
    }

    public Node getNode(Object item, String expression) throws XPathExpressionException {
        return (Node) __doEvaluate(expression, item, XPathConstants.NODE);
    }

    public NodeList getNodeList(Object item, String expression) throws XPathExpressionException {
        return (NodeList) __doEvaluate(expression, item, XPathConstants.NODESET);
    }
}
