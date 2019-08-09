package com.yuanwang.common.utils;

import com.aspose.cells.License;
import com.aspose.cells.SaveFormat;
import com.aspose.cells.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class ExcelToPdf {
    /**
     * 获取license
     *
     * @return
     */
    public static boolean getLicense() {
        boolean result = false;
        try {
            InputStream is = ExcelToPdf.class.getClassLoader().getResourceAsStream("\\license.xml");
            License aposeLic = new License();
            aposeLic.setLicense(is);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 支持DOC, DOCX, OOXML, RTF, HTML, OpenDocument, PDF, EPUB, XPS, SWF等相互转换<br>
     *
     * @param args
     */
    public static void main(String[] args) {
        // 验证License
        if (!getLicense()) {
            return;
        }

        try {
            long old = System.currentTimeMillis();
            Workbook wb = new Workbook("D:\\222.xls");// 原始excel路径
            File pdfFile = new File("D:\\222.pdf");// 输出路径
            FileOutputStream fileOS = new FileOutputStream(pdfFile);
            wb.save(fileOS, SaveFormat.PDF);
            long now = System.currentTimeMillis();
            System.out.println("共耗时：" + ((now - old) / 1000.0) + "秒");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
