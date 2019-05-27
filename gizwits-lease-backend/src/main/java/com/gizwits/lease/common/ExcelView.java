package com.gizwits.lease.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsView;

/**
 * View - Excel视图
 *
 * @author lilh
 * @date 2017/8/9 14:38
 */
public class ExcelView extends AbstractXlsView {

    /** 默认日期格式配比 */
    private static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /** 文件名称 */
    private String filename;

    /** 表名称 */
    private String sheetName;

    /** 属性 */
    private String[] properties;

    /** 标题 */
    private String[] titles;

    /** 列宽 */
    private Integer[] widths;

    /** 类型转换 */
    private Converter[] converters;

    /** 数据 */
    private List<?> data;

    /** 附加内容 */
    private String[] contents;

    static {
        DateConverter dateConverter = new DateConverter();
        dateConverter.setPattern(DEFAULT_DATE_PATTERN);
        ConvertUtils.register(dateConverter, Date.class);
    }

    /**
     * @param filename   文件名称
     * @param sheetName  表名称
     * @param properties 属性
     * @param titles     标题
     * @param widths     列宽
     * @param converters 类型转换
     * @param data       数据
     * @param contents   附加内容
     */
    public ExcelView(String filename, String sheetName, String[] properties, String[] titles, Integer[] widths, Converter[] converters, List<?> data,
                     String[] contents) {
        super.setContentType("application/vnd.ms-excel;charset=utf-8");
        this.filename = filename;
        this.sheetName = sheetName;
        this.properties = properties;
        this.titles = titles;
        this.widths = widths;
        this.converters = converters;
        this.data = data;
        this.contents = contents;
    }

    /**
     * @param properties 属性
     * @param titles     标题
     * @param data       数据
     * @param contents   附加内容
     */
    public ExcelView(String[] properties, String[] titles, List<?> data, String[] contents) {
        this.properties = properties;
        this.titles = titles;
        this.data = data;
        this.contents = contents;
    }

    /**
     * @param properties 属性
     * @param titles     标题
     * @param data       数据
     */
    public ExcelView(String[] properties, String[] titles, List<?> data) {
        this.properties = properties;
        this.titles = titles;
        this.data = data;
    }

    /**
     * @param properties 属性
     * @param data       数据
     */
    public ExcelView(String[] properties, List<?> data) {
        this.properties = properties;
        this.data = data;
    }


    /**
     * 生成Excel文档
     *
     * @param model    数据
     * @param workbook workbook
     * @param request  request
     * @param response response
     */
    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
        buildExcelDocumentInternal(model, (HSSFWorkbook)workbook, request, response);
    }


    private void buildExcelDocumentInternal(Map<String, Object> model, HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HSSFSheet sheet = workbook.createSheet();
        if (StringUtils.isNotEmpty(sheetName)) {
            workbook.setSheetName(0, sheetName);
        }

        int rowNumber = 0;
        if (titles != null && titles.length > 0) {
            HSSFRow header = sheet.createRow(rowNumber);
            for (int i = 0; i < properties.length; i++) {
                HSSFCell cell = header.createCell(i);
                setCellStyle(workbook, cell);
                if (titles.length > i && titles[i] != null) {
                    cell.setCellValue(titles[i]);
                } else {
                    cell.setCellValue(properties[i]);
                }
            }
            rowNumber++;
        }
        if (data != null) {
            for (Object item : data) {
                HSSFRow row = sheet.createRow(rowNumber);
                for (int i = 0; i < properties.length; i++) {
                    HSSFCell cell = row.createCell(i);
                    Object value = null;
                    String objs[] = null;
                    if (properties[i].indexOf('.') < 0) {
                        value = PropertyUtils.getProperty(item, properties[i]);
                    } else {
                        objs = StringUtils.split(properties[i], ".");
                        value = PropertyUtils.getProperty(item, objs[0]);
                        for (int j = 1; j < objs.length; j++) {
                            if (value == null) {
                                break;
                            } else {
                                value = PropertyUtils.getProperty(value, objs[j]);
                            }

                        }
                    }

                    if (value != null) {
                        if (value instanceof Date) {
                            value = new SimpleDateFormat(DEFAULT_DATE_PATTERN).format(value);
                        }
                        cell.setCellValue(String.valueOf(value));
                    }
                    if (rowNumber == 0 || rowNumber == 1) {
                        if (widths != null && widths.length > i && widths[i] != null) {
                            sheet.setColumnWidth(i, widths[i]);
                        } else {
                            sheet.autoSizeColumn( i);
                        }
                    }
                }
                rowNumber++;
            }
        }
        //response.setContentType("application/force-download");
        response.setCharacterEncoding("UTF-8");
        if (StringUtils.isNotEmpty(filename)) {
            response.setHeader("Content-Disposition", "attachment; filename=" + new String(filename.getBytes("gb2312"), "UTF-8"));
        } else {
            response.setHeader("Content-disposition", "attachment");
        }
    }

    private void setCellStyle(HSSFWorkbook workbook, HSSFCell cell) {
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillForegroundColor(HSSFColor.LIGHT_CORNFLOWER_BLUE.index);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        HSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 11);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        cellStyle.setFont(font);
        cell.setCellStyle(cellStyle);
    }

    /**
     * 获取文件名称
     *
     * @return 文件名称
     */
    public String getFileName() {
        return filename;
    }

    /**
     * 设置文件名称
     *
     * @param filename 文件名称
     */
    public void setFileName(String filename) {
        this.filename = filename;
    }

    /**
     * 获取表名称
     *
     * @return 表名称
     */
    public String getSheetName() {
        return sheetName;
    }

    /**
     * 设置表名称
     *
     * @param sheetName 表名称
     */
    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    /**
     * 获取属性
     *
     * @return 属性
     */
    public String[] getProperties() {
        return properties;
    }

    /**
     * 设置属性
     *
     * @param properties 属性
     */
    public void setProperties(String[] properties) {
        this.properties = properties;
    }

    /**
     * 获取标题
     *
     * @return 标题
     */
    public String[] getTitles() {
        return titles;
    }

    /**
     * 设置标题
     *
     * @param titles 标题
     */
    public void setTitles(String[] titles) {
        this.titles = titles;
    }

    /**
     * 获取列宽
     *
     * @return 列宽
     */
    public Integer[] getWidths() {
        return widths;
    }

    /**
     * 设置列宽
     *
     * @param widths 列宽
     */
    public void setWidths(Integer[] widths) {
        this.widths = widths;
    }

    /**
     * 获取类型转换
     *
     * @return 类型转换
     */
    public Converter[] getConverters() {
        return converters;
    }

    /**
     * 设置类型转换
     *
     * @param converters 类型转换
     */
    public void setConverters(Converter[] converters) {
        this.converters = converters;
    }

    /**
     * 获取数据
     *
     * @return 数据
     */
    public List<?> getData() {
        return data;
    }

    /**
     * 设置数据
     *
     * @param data 数据
     */
    public void setData(List<?> data) {
        this.data = data;
    }

    /**
     * 获取附加内容
     *
     * @return 附加内容
     */
    public String[] getContents() {
        return contents;
    }

    /**
     * 设置附加内容
     *
     * @param contents 附加内容
     */
    public void setContents(String[] contents) {
        this.contents = contents;
    }
}