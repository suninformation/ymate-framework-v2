package net.ymate.framework.commons;

import net.ymate.framework.commons.annotation.ExportColumn;
import net.ymate.platform.core.lang.BlurObject;
import net.ymate.platform.core.support.ConsoleTableBuilder;
import net.ymate.platform.core.util.ClassUtils;
import net.ymate.platform.core.util.DateTimeUtils;
import net.ymate.platform.core.util.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author 刘镇 (suninformation@163.com) on 2017/12/25 下午2:42
 * @version 1.0
 */
public final class ExcelFileExportHelper {

    private Map<String, Object> __data;

    private Map<String, String> __customFieldNames = new HashMap<String, String>();

    private Map<String, IExportDataRender> __renders = new HashMap<String, IExportDataRender>();

    private IExportDataProcessor __processor;

    public static ExcelFileExportHelper bind() {
        return new ExcelFileExportHelper();
    }

    public static ExcelFileExportHelper bind(Map<String, Object> data) {
        return new ExcelFileExportHelper(data);
    }

    public static ExcelFileExportHelper bind(IExportDataProcessor processor) {
        return new ExcelFileExportHelper(processor);
    }

    private ExcelFileExportHelper() {
        __data = new HashMap<String, Object>();
    }

    private ExcelFileExportHelper(Map<String, Object> data) {
        if (data == null) {
            throw new NullArgumentException("data");
        }
        __data = data;
    }

    private ExcelFileExportHelper(IExportDataProcessor processor) {
        if (processor == null) {
            throw new NullArgumentException("processor");
        }
        __processor = processor;
    }

    public ExcelFileExportHelper putCustomFieldName(String name, String customFieldName) {
        if (StringUtils.isNotBlank(name) && StringUtils.isNotBlank(customFieldName)) {
            __customFieldNames.put(name, customFieldName);
        }
        return this;
    }

    public ExcelFileExportHelper putData(String varName, Object data) {
        if (StringUtils.isBlank(varName)) {
            throw new NullArgumentException("varName");
        }
        if (data == null) {
            throw new NullArgumentException("data");
        }
        __data.put(varName, data);
        return this;
    }

    public File export(Class<?> dataType) throws Exception {
        return export(dataType, null);
    }

    public File export(Class<?> dataType, String charset) throws Exception {
        File _file = null;
        if (__processor != null) {
            List<File> _files = new ArrayList<File>();
            for (int _idx = 1; ; _idx++) {
                Map<String, Object> _data = __processor.getData(_idx);
                if (_data == null || _data.isEmpty()) {
                    break;
                }
                _files.add(__export(dataType, _idx, _data, charset));
            }
            _file = __toZip(_files);
        } else if (!__data.isEmpty()) {
            _file = __export(dataType, 1, __data, charset);
        }
        return _file;
    }

    private File __toZip(List<File> _files) throws IOException {
        if (!_files.isEmpty()) {
            if (_files.size() == 1) {
                return _files.get(0);
            }
            return FileUtils.toZip("export_", _files.toArray(new File[0]));
        }
        return null;
    }

    private File __export(Class<?> dataType, int index, Map<String, Object> data, String charset) throws Exception {
        File _file;
        OutputStream _outputStream = null;
        try {
            _file = File.createTempFile("export_", "_" + index + ".csv");
            _outputStream = new FileOutputStream(_file);
            //
            Map<String, ExportColumn> _columns = new HashMap<String, ExportColumn>();
            List<String> _columnNames = new ArrayList<String>();
            for (Field _field : ClassUtils.wrapper(dataType).getFields()) {
                ExportColumn _column = _field.getAnnotation(ExportColumn.class);
                if (_column != null) {
                    _columns.put(_field.getName(), _column);
                    if (!_column.excluded()) {
                        if (!_column.render().equals(IExportDataRender.class)) {
                            IExportDataRender _render = ClassUtils.impl(_column.render(), IExportDataRender.class);
                            if (_render != null) {
                                __renders.put(_field.getName(), _render);
                            }
                        }
                        String _colName = StringUtils.defaultIfBlank(_column.value(), _field.getName());
                        String _customFieldName = __customFieldNames.get(_colName);
                        _columnNames.add(_customFieldName == null ? _colName : _customFieldName);
                    }
                } else {
                    String _customFieldName = __customFieldNames.get(_field.getName());
                    _columnNames.add(_customFieldName == null ? _field.getName() : _customFieldName);
                }
            }
            //
            ConsoleTableBuilder _builder = ConsoleTableBuilder.create(_columnNames.size()).csv();
            if (!_columnNames.isEmpty()) {
                ConsoleTableBuilder.Row _row = _builder.addRow();
                for (String _name : _columnNames) {
                    _row.addColumn(_name);
                }
            }
            for (Object _item : data.values()) {
                if (_item instanceof Collection) {
                    for (Object _obj : (Collection) _item) {
                        ClassUtils.BeanWrapper _wrapper = ClassUtils.wrapper(_obj);
                        ConsoleTableBuilder.Row _row = _builder.addRow();
                        for (Object _fieldName : _wrapper.getFieldNames()) {
                            try {
                                ExportColumn _column = _columns.get((String) _fieldName);
                                if (_column != null && _column.excluded()) {
                                    continue;
                                }
                                IExportDataRender _render = __renders.get(_fieldName);
                                if (_column != null && _render != null) {
                                    String _valueStr = _render.render(_column, (String) _fieldName, _wrapper.getValue((String) _fieldName));
                                    if (StringUtils.isNotBlank(_valueStr)) {
                                        _row.addColumn(_valueStr);
                                    } else {
                                        _row.addColumn(StringUtils.trimToEmpty(BlurObject.bind(_wrapper.getValue((String) _fieldName)).toStringValue()));
                                    }
                                } else if (_column != null && _column.dateTime()) {
                                    _row.addColumn(DateTimeUtils.formatTime(BlurObject.bind(_wrapper.getValue((String) _fieldName)).toLongValue(), DateTimeUtils.YYYY_MM_DD_HH_MM_SS));
                                } else if (_column != null && _column.dataRange().length > 0) {
                                    _row.addColumn(_column.dataRange()[BlurObject.bind(_wrapper.getValue((String) _fieldName)).toIntValue()]);
                                } else if (_column != null && _column.currency()) {
                                    _row.addColumn(MathCalcHelper.bind(BlurObject.bind(_wrapper.getValue((String) _fieldName)).toStringValue()).scale(2).divide("100").toBlurObject().toStringValue());
                                } else {
                                    _row.addColumn(StringUtils.trimToEmpty(BlurObject.bind(_wrapper.getValue((String) _fieldName)).toStringValue()));
                                }
                            } catch (Exception e) {
                                _row.addColumn(StringUtils.trimToEmpty(BlurObject.bind(_wrapper.getValue((String) _fieldName)).toStringValue()));
                            }
                        }
                    }
                }
            }
            IOUtils.write(_builder.toString(), _outputStream, StringUtils.defaultIfBlank(charset, "GB2312"));
        } finally {
            IOUtils.closeQuietly(_outputStream);
        }
        return _file;
    }

    /**
     * @param tmplFile 模板文件名称
     * @return 将导出数据映射到tmplFile指定的Excel文件模板
     * @throws Exception 可能产生的任何异常
     */
    public File export(String tmplFile) throws Exception {
        if (StringUtils.isBlank(tmplFile)) {
            throw new NullArgumentException("tmplFile");
        }
        File _file = null;
        if (__processor != null) {
            List<File> _files = new ArrayList<File>();
            for (int _idx = 1; ; _idx++) {
                Map<String, Object> _data = __processor.getData(_idx);
                if (_data == null || _data.isEmpty()) {
                    break;
                }
                _files.add(__export(tmplFile, _idx, _data));
            }
            _file = __toZip(_files);
        } else if (!__data.isEmpty()) {
            _file = __export(tmplFile, 1, __data);
        }
        return _file;
    }

    private File __export(String tmplFile, int index, Map<String, Object> data) throws Exception {
        File _file;
        InputStream _inputStream = null;
        OutputStream _outputStream = null;
        try {
            _inputStream = ExcelFileExportHelper.class.getResourceAsStream(tmplFile + ".xls");
            _file = File.createTempFile("export_", "_" + index + ".xls");
            _outputStream = new FileOutputStream(_file);
            //
            JxlsHelper.getInstance().processTemplate(_inputStream, _outputStream, new Context(data));
        } finally {
            IOUtils.closeQuietly(_outputStream);
            IOUtils.closeQuietly(_inputStream);
        }
        return _file;
    }

    public interface IExportDataProcessor {

        Map<String, Object> getData(int index) throws Exception;
    }

    public interface IExportDataRender {

        String render(ExportColumn column, String fieldName, Object value) throws Exception;
    }
}
