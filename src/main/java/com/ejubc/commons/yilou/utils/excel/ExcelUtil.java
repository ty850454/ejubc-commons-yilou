package com.ejubc.commons.yilou.utils.excel;

import com.ejubc.commons.yilou.enums.ExcelErrorCode;
import com.ejubc.commons.yilou.exception.YlException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * excel工具
 *
 * @author xy
 */
@Slf4j
public class ExcelUtil {

    private static final char CHAR_MIN = 'a';
    private static final char CHAR_MAX = 'z';

    /**
     * 输出到流
     * @param sheetName sheet页名
     * @param os 输出流
     * @param sources 源数据，需要导出的字段加{@link ExcelColumn}注解
     */
    @SuppressWarnings("unused")
    public static void outputToStream(String sheetName, OutputStream os, List<?> sources) {

        if (sources == null || sources.size() == 0 || sources.get(0) == null) {
            throw new YlException(ExcelErrorCode.EXCEL01);
        }
        Objects.requireNonNull(os, "请确保os不为null");

        if (sheetName == null) {
            sheetName = "sheet1";
        }

        // 获取表格列定义
        ExcelColumnDefine[] excelColumnDefines = getExcelColumnDefine(sources.get(0).getClass());

        // 创建对象
        XSSFWorkbook sheets = new XSSFWorkbook();
        XSSFSheet sheet = sheets.createSheet(sheetName);

        // 标题行
        List<String> titles = Arrays.stream(excelColumnDefines).map(ExcelColumnDefine::getTitle).collect(Collectors.toList());
        setCellValues(sheet.createRow(0), titles);

        // 内容
        String[][] content = getContext(sources, excelColumnDefines);
        for (int i = 0; i < content.length; i++) {
            setCellValues(sheet.createRow(i + 1), content[i]);
        }

        // 自适应列宽
        for (int i = 0; i < excelColumnDefines.length; i++) {
            sheet.autoSizeColumn(i);
        }

        try {
            sheets.write(os);
        } catch (IOException e) {
            log.error("excel导出失败", e);
            throw new YlException(ExcelErrorCode.EXCEL02);
        }
    }

    private static void setCellValues(XSSFRow row, List<String> values) {
        for (int i = 0; i < values.size(); i++) {
            row.createCell(i).setCellValue(values.get(i));
        }
    }
    private static void setCellValues(XSSFRow row, String[] values) {
        for (int i = 0; i < values.length; i++) {
            row.createCell(i).setCellValue(values[i]);
        }
    }

    private static String initialCapital(String s) {
        char[] chars = s.toCharArray();
        if (chars[0] >= CHAR_MIN && chars[0] <= CHAR_MAX) {
            chars[0] = (char)(chars[0] - 32);
        }
        return new String(chars);
    }

    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    private static class ExcelColumnDefine {
        private ExcelColumn annotation;
        private Field field;
        private Method m;
        private String title;
    }

    private static ExcelColumnDefine[] getExcelColumnDefine(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        ExcelColumnDefine[] excelColumnDefinesList = new ExcelColumnDefine[fields.length];

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
            if (annotation == null) {
                continue;
            }
            String name = field.getName();
            try {
                Method m = clazz.getMethod("get" + initialCapital(name));
                if (m == null) {
                    throw new RuntimeException("字段["+ name + "]无get方法");
                }
                excelColumnDefinesList[i] = new ExcelColumnDefine(annotation, field, m, annotation.title());
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return excelColumnDefinesList;
    }

    private static String[][] getContext(List<?> sources, ExcelColumnDefine[] excelColumnDefines) {
        String[][] content = new String[sources.size()][excelColumnDefines.length];

        for (int x = 0; x < sources.size(); x++) {
            Object t = sources.get(x);
            if (t == null) {
                continue;
            }
            for (int y = 0; y < excelColumnDefines.length; y++) {
                ExcelColumnDefine define = excelColumnDefines[y];
                if (define == null) {
                    continue;
                }
                try {
                    Object value = define.getM().invoke(t);
                    content[x][y] = value == null ? "" : value.toString();
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return content;
    }

}
