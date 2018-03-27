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

import net.ymate.framework.commons.annotation.XPathNode;
import net.ymate.platform.core.lang.BlurObject;
import net.ymate.platform.core.util.ClassUtils;
import net.ymate.platform.core.util.RuntimeUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 刘镇 (suninformation@163.com) on 16/5/22 上午6:52
 * @version 1.0
 */
public class XPathHelper {

    private static final Log _LOG = LogFactory.getLog(XPathHelper.class);

    public static final DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

    public static final XPathFactory xpathFactory = XPathFactory.newInstance();

    /**
     * 用于忽略所有DTD检测
     */
    public static final EntityResolver IGNORE_DTD_ENTITY_RESOLVER = new EntityResolver() {
        @Override
        public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
            return new InputSource(new ByteArrayInputStream("<?xml version='1.0' encoding='UTF-8'?>".getBytes()));
        }
    };

    public static XPathHelper create(Document document) {
        return new XPathHelper(document);
    }

    private XPath __path;

    private Document __document;

    public XPathHelper(Document document) {
        __path = xpathFactory.newXPath();
        __document = document;
    }

    public XPathHelper(InputSource inputSource) throws Exception {
        this(inputSource, null, null);
    }

    public XPathHelper(InputSource inputSource, EntityResolver entityResolver) throws Exception {
        this(inputSource, entityResolver, null);
    }

    public XPathHelper(InputSource inputSource, ErrorHandler errorHandler) throws Exception {
        this(inputSource, null, errorHandler);
    }

    public XPathHelper(InputSource inputSource, EntityResolver entityResolver, ErrorHandler errorHandler) throws Exception {
        __doInit(inputSource, entityResolver, errorHandler);
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

    public XPathHelper(String content, EntityResolver entityResolver) throws Exception {
        this(content, entityResolver, null);
    }

    public XPathHelper(String content, ErrorHandler errorHandler) throws Exception {
        this(content, null, errorHandler);
    }

    public XPathHelper(String content, EntityResolver entityResolver, ErrorHandler errorHandler) throws Exception {
        StringReader _reader = null;
        try {
            _reader = new StringReader(content);
            __doInit(new InputSource(_reader), entityResolver, errorHandler);
            __path = xpathFactory.newXPath();
        } finally {
            IOUtils.closeQuietly(_reader);
        }
    }

    private void __doInit(InputSource inputSource, EntityResolver entityResolver, ErrorHandler errorHandler) throws Exception {
        DocumentBuilder _builder = documentFactory.newDocumentBuilder();
        if (entityResolver != null) {
            _builder.setEntityResolver(entityResolver);
        }
        if (errorHandler != null) {
            _builder.setErrorHandler(errorHandler);
        }
        __path = xpathFactory.newXPath();
        __document = _builder.parse(inputSource);
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

    public <T> T toObject(Class<T> targetClass) {
        try {
            return toObject(targetClass.newInstance());
        } catch (Exception e) {
            _LOG.warn("", RuntimeUtils.unwrapThrow(e));
        }
        return null;
    }

    public <T> T toObject(T targetObject) {
        try {
            Class<?> _targetClass = targetObject.getClass();
            XPathNode _rootNodeAnno = _targetClass.getAnnotation(XPathNode.class);
            if (_rootNodeAnno != null && StringUtils.isNotBlank(_rootNodeAnno.value())) {
                Node _rootNode = getNode(_rootNodeAnno.value());
                if (_rootNode != null) {
                    return __toObject(_rootNode, targetObject);
                }
            } else {
                return __toObject(__document, targetObject);
            }
        } catch (Exception e) {
            _LOG.warn("", RuntimeUtils.unwrapThrow(e));
        }
        return null;
    }

    private <T> T __toObject(Object parentNode, Class<T> targetClass) throws XPathExpressionException, IllegalAccessException {
        if (parentNode != null && targetClass != null) {
            return __doWrapperValues(parentNode, ClassUtils.wrapper(targetClass));
        }
        return null;
    }

    private <T> T __toObject(Object parentNode, T targetObject) throws XPathExpressionException, IllegalAccessException {
        if (parentNode != null && targetObject != null) {
            return __doWrapperValues(parentNode, ClassUtils.wrapper(targetObject));
        }
        return null;
    }

    private <T> T __doWrapperValues(Object parentNode, ClassUtils.BeanWrapper<T> _beanWrapper) throws XPathExpressionException, IllegalAccessException {
        for (Field _field : _beanWrapper.getFields()) {
            if (_field.isAnnotationPresent(XPathNode.class)) {
                XPathNode _fieldNodeAnno = _field.getAnnotation(XPathNode.class);
                if (_fieldNodeAnno.child()) {
                    Object _childNode = StringUtils.isNotBlank(_fieldNodeAnno.value()) ? getNode(parentNode, _fieldNodeAnno.value()) : parentNode;
                    if (_childNode != null) {
                        Object _childObject = null;
                        Object _fieldValue = _beanWrapper.getValue(_field);
                        //
                        if (!INodeValueParser.class.equals(_fieldNodeAnno.parser())) {
                            try {
                                INodeValueParser _parser = _fieldNodeAnno.parser().newInstance();
                                _childObject = _parser.parse(this, parentNode, _field.getType(), _fieldValue);
                            } catch (InstantiationException e) {
                                _LOG.warn("", RuntimeUtils.unwrapThrow(e));
                            }
                        } else {
                            if (_fieldValue != null) {
                                _childObject = __toObject(_childNode, _fieldValue);
                            } else {
                                _childObject = __toObject(_childNode, Void.class.equals(_fieldNodeAnno.implClass()) ? _field.getType() : _fieldNodeAnno.implClass());
                            }
                        }
                        _beanWrapper.setValue(_field, _childObject);
                    }
                } else {
                    String _value = StringUtils.defaultIfBlank(StringUtils.isNotBlank(_fieldNodeAnno.value()) ? getStringValue(parentNode, _fieldNodeAnno.value()) : null, _fieldNodeAnno.defaultValue());
                    if (!INodeValueParser.class.equals(_fieldNodeAnno.parser())) {
                        try {
                            INodeValueParser _parser = _fieldNodeAnno.parser().newInstance();
                            _beanWrapper.setValue(_field, BlurObject.bind(_parser.parse(this, parentNode, _field.getType(), _value)).toObjectValue(_field.getType()));
                        } catch (InstantiationException e) {
                            _LOG.warn("", RuntimeUtils.unwrapThrow(e));
                        }
                    } else {
                        _beanWrapper.setValue(_field, BlurObject.bind(_value).toObjectValue(_field.getType()));
                    }
                }
            }
        }
        return _beanWrapper.getTargetObject();
    }

    public static class Builder {

        private EntityResolver entityResolver;

        private ErrorHandler errorHandler;

        public static Builder create() {
            return new Builder();
        }

        public Builder entityResolver(EntityResolver entityResolver) {
            this.entityResolver = entityResolver;
            return this;
        }

        public Builder ignoreDtdEntityResolver() {
            entityResolver = IGNORE_DTD_ENTITY_RESOLVER;
            return this;
        }

        public Builder errorHandler(ErrorHandler errorHandler) {
            this.errorHandler = errorHandler;
            return this;
        }

        public XPathHelper build(InputSource inputSource) throws Exception {
            return new XPathHelper(inputSource, entityResolver, errorHandler);
        }

        public XPathHelper build(String content) throws Exception {
            return new XPathHelper(content, entityResolver, errorHandler);
        }
    }

    /**
     * 自定义节点值解析器
     */
    public interface INodeValueParser {

        /**
         * @param helper     当前XPathHelper实例
         * @param node       当前节点对象
         * @param fieldType  当前节点值类型
         * @param fieldValue 当前节点值对象
         * @return 解析处理后的值对象
         */
        Object parse(XPathHelper helper, Object node, Class<?> fieldType, Object fieldValue);
    }
}
