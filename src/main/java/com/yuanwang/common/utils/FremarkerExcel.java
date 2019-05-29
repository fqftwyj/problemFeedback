package com.yuanwang.common.utils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;


public class FremarkerExcel {
    private  Configuration configuration = null;


    public FremarkerExcel() {
        configuration = new Configuration();
        configuration.setDefaultEncoding("UTF-8"); // 设置编码
    }


    public static void main(String[] args) {
        FremarkerExcel test = new FremarkerExcel();
        try{
        Map<String, Object> dataMap =new HashMap<>();
        /*test.createWord("F:\\workspace\\finaceManage\\src\\main\\resources\\excel","core-office2007.ftl",dataMap);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public  void createWord(String path, String templateNM, Map<String, Object> dataMap, Map<String, Object> resMap,HttpServletResponse response) throws Exception{
     /*   getData(dataMap);*/
        //先生成excel到本地
        //FTL文件所存在的位置
        try {
            configuration.setDirectoryForTemplateLoading(new File(path));
        } catch (IOException e2) {
            e2.printStackTrace();
        } // 线上 ：绝对路径
        Template t = null;
        try {
            t = configuration.getTemplate(templateNM, "UTF-8"); // 文件名,并且设置编码
        } catch (IOException e) {
            e.printStackTrace();
        }
        //替换斜杆为反斜杠
        path=path.replaceAll("\\\\","/");
        String lastPath=path + Math.random() * 10000 + ".xls";
        File outFile = new File(lastPath); // 生成文件的路径
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
       //页面上直接下载
        //设置响应头和客户端保存文件名
        response.setContentType("multipart/form-data;charset=utf-8");
        String userName=String.valueOf(resMap.get("userName"));
        String reimburseDate=(resMap.get("reimburseDate")!=null && !"".equals(String.valueOf(resMap.get("reimburseDate"))))?"-"+resMap.get("reimburseDate"):"";
        String downloadFileName = URLEncoder.encode("外出培训（进修）报销单-"+userName+reimburseDate+resMap.get("reimburseDate")+".xls","UTF-8");
        // 设置响应头，控制浏览器下载该文件
        response.setHeader("Content-Disposition", "attachment;filename=" + downloadFileName);

        try {
            //打开本地文件流
            InputStream inputStream = new FileInputStream(lastPath);
            //激活下载操作
            OutputStream os = response.getOutputStream();
            //循环写入输出流
            byte[] b = new byte[2048];
            int length;
            while ((length = inputStream.read(b)) > 0) {
                os.write(b, 0, length);
            }
            // 这里主要关闭。
            os.close();
            inputStream.close();
            //下载后删除文件
            if(outFile.exists()){
                outFile.delete();
            }
        } catch (Exception e){
            throw e;
        }
    }


    // 这里赋值的时候需要注意,xml中需要的数据你必须提供给它,不然会报找不到某元素错的.
   /* private  void getData(Map<String, Object> dataMap) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < 10; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("name", "张三00" + i);
            map.put("age", "1" + i);
            map.put("sg", "17" + i);
            list.add(map);
        }
        dataMap.put("list", list);


    }*/

}
