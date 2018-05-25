package net.ymate.framework.commons;

import net.ymate.platform.core.lang.BlurObject;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Excel文件数据导入助手类
 *
 * @author 刘镇 (suninformation@163.com) on 2018/5/25 上午6:37
 * @version 1.0
 */
public class ExcelFileAnalysisHelper implements Closeable {

    private Workbook __workbook;

    private String[] __sheetNames;

    public static ExcelFileAnalysisHelper bind(File file) throws IOException, InvalidFormatException {
        return new ExcelFileAnalysisHelper(new FileInputStream(file));
    }

    public static ExcelFileAnalysisHelper bind(InputStream inputStream) throws IOException, InvalidFormatException {
        return new ExcelFileAnalysisHelper(inputStream);
    }

    private ExcelFileAnalysisHelper(InputStream inputStream) throws IOException, InvalidFormatException {
        __workbook = WorkbookFactory.create(inputStream);
        __sheetNames = new String[__workbook.getNumberOfSheets()];
        for (int _idx = 0; _idx < __sheetNames.length; _idx++) {
            __sheetNames[_idx] = __workbook.getSheetName(_idx);
        }
    }

    /**
     * @return 返回SHEET名称集合
     */
    public String[] getSheetNames() {
        return __sheetNames;
    }

    public <T> List<T> openSheet(int sheetIdx, ISheetHandler<T> handler) throws Exception {
        Sheet _sheet = __workbook.getSheetAt(sheetIdx);
        return handler.handle(_sheet);
    }

    public List<Object[]> openSheet(int sheetIdx) throws Exception {
        return openSheet(sheetIdx, new DefaultSheetHandler());
    }

    public <T> List<T> openSheet(String sheetName, ISheetHandler<T> handler) throws Exception {
        Sheet _sheet = __workbook.getSheet(sheetName);
        return handler.handle(_sheet);
    }

    public List<Object[]> openSheet(String sheetName) throws Exception {
        return openSheet(sheetName, new DefaultSheetHandler());
    }

    @Override
    public void close() throws IOException {
        if (__workbook != null) {
            __workbook.close();
        }
    }

    /**
     * CELL单元格描述对象
     */
    public static class CellMeta {

        private final String name;

        private final int cellIndex;

        public CellMeta(String name, int cellIndex) {
            this.name = name;
            this.cellIndex = cellIndex;
        }

        public String getName() {
            return name;
        }

        public int getCellIndex() {
            return cellIndex;
        }
    }

    static class DefaultSheetHandler implements ISheetHandler<Object[]> {

        private CellMeta[] __cellMeta;

        @Override
        public List<Object[]> handle(Sheet sheet) throws Exception {
            List<Object[]> _results = new ArrayList<Object[]>();
            for (int _rowIdx = sheet.getFirstRowNum(); _rowIdx <= sheet.getLastRowNum(); _rowIdx++) {
                Row _row = sheet.getRow(_rowIdx);
                if (_rowIdx == sheet.getFirstRowNum()) {
                    List<CellMeta> _cellMetas = new ArrayList<CellMeta>();
                    for (short _cellIdx = _row.getFirstCellNum(); _cellIdx <= _row.getLastCellNum(); _cellIdx++) {
                        Object _cellValue = __parseCell(_row.getCell(_cellIdx));
                        if (_cellValue != null) {
                            _cellMetas.add(new CellMeta(BlurObject.bind(_cellValue).toStringValue(), _cellIdx));
                        }
                    }
                    __cellMeta = _cellMetas.toArray(new CellMeta[0]);
                } else {
                    _results.add(parseRow(_row));
                }
            }
            return _results;
        }

        @Override
        public CellMeta[] getCellMetas() {
            return __cellMeta;
        }

        @Override
        public Object[] parseRow(Row row) throws Exception {
            Object[] _result = new Object[__cellMeta.length];
            for (int _idx = 0; _idx < __cellMeta.length; _idx++) {
                CellMeta _meta = __cellMeta[_idx];
                _result[_idx] = new Object[]{_meta.getName(), __parseCell(row.getCell(_meta.getCellIndex()))};
            }
            return _result;
        }

        private Object __parseCell(Cell cell) throws Exception {
            Object _value = null;
            if (cell != null) {
                switch (cell.getCellTypeEnum()) {
                    case STRING:
                        _value = cell.getStringCellValue();
                        break;
                    case NUMERIC:
                        if (HSSFDateUtil.isCellDateFormatted(cell) && cell.getDateCellValue() != null) {
                            _value = cell.getDateCellValue().getTime();
                        } else {
                            _value = new DecimalFormat("0").format(cell.getNumericCellValue());
                        }
                        break;
                    case FORMULA:
                        _value = cell.getCellFormula();
                        break;
                    case BOOLEAN:
                        _value = cell.getBooleanCellValue();
                        break;
                    case BLANK:
                    case ERROR:
                    default:
                        _value = "";
                }
            }
            return _value;
        }
    }

    /**
     * SHEET页分析处理器接口
     */
    interface ISheetHandler<T> {

        /**
         * @param sheet Sheet页接口对象
         * @return 处理Sheet页并返回数据
         * @throws Exception 可能产生的任何异常
         */
        List<T> handle(Sheet sheet) throws Exception;

        /**
         * @return 返回单元格描述对象集合
         */
        CellMeta[] getCellMetas();

        /**
         * @param row 记录行接口对象
         * @return 分析行数据并返回
         * @throws Exception 可能产生的任何异常
         */
        T parseRow(Row row) throws Exception;
    }
}
