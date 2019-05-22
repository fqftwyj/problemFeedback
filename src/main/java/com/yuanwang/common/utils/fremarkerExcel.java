/*
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class fremarkerExcel {
    private Configuration configuration = null;


    public fremarkerExcel() {
        configuration = new Configuration();
        configuration.setDefaultEncoding("UTF-8"); // 设置编码
    }


    public static void main(String[] args) {
        fremarkerExcel test = new fremarkerExcel();
        try {
            test.createWord();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void createWord() {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        getData(dataMap);
// configuration.setClassForTemplateLoading(this.getClass(), "/com");
// //FTL文件所存在的位置
        try {
            configuration.setDirectoryForTemplateLoading(new File("C:\\fq\\workSpacenew\\finaceManage\\src\\main\\resources\\excel"));
        } catch (IOException e2) {
            e2.printStackTrace();
        } // 线上 ：绝对路径
        Template t = null;
        try {
            t = configuration.getTemplate("excel.ftl", "UTF-8"); // 文件名,并且设置编码
        } catch (IOException e) {
            e.printStackTrace();
        }
        File outFile = new File("C:/fq/workSpacenew/finaceManage/src/main/resources/excel/outFilessa" + Math.random() * 10000 + ".xls"); // 生成文件的路径
        Writer out = null;
        try {
            try {
                out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "UTF-8")); // 设置编码
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }


        try {
            t.process(dataMap, out);
            out.flush();
            out.close();
        } catch (TemplateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // 这里赋值的时候需要注意,xml中需要的数据你必须提供给它,不然会报找不到某元素错的.
    private void getData(Map<String, Object> dataMap) {
// dataMap.put("name", "张三");
// dataMap.put("age", "22");
// dataMap.put("sg", "170cm");


        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < 10; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("name", "张三00" + i);
            map.put("age", "1" + i);
            map.put("sg", "17" + i);
            list.add(map);
        }
        dataMap.put("list", list);


    }

}
*/
