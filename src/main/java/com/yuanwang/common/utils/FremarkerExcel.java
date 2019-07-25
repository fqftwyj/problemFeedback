package com.yuanwang.common.utils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


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
        // 这里赋值的时候需要注意,xml中需要的数据你必须提供给它,不然会报找不到某元素错的.
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
        String reimburseDate=(resMap.get("reimburseDate")!=null && !"".equals(String.valueOf(resMap.get("reimburseDate"))) && !"1900-01-01".equals(String.valueOf(resMap.get("reimburseDate"))))?"-"+resMap.get("reimburseDate"):"";
        String downloadFileName = URLEncoder.encode("外出培训（进修）报销单-"+userName+reimburseDate+".xls","UTF-8");
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
    //下载指定路径下的文件
    public  void dowloadAttachExcel(String path, String templateNM, HttpServletResponse response) throws Exception{
        //先生成excel到本地
        String lastPath=path  +File.separator+ templateNM;
        //页面上直接下载
        //设置响应头和客户端保存文件名
        response.setContentType("multipart/form-data;charset=utf-8");
        String downloadFileName = URLEncoder.encode(templateNM,"UTF-8");
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

        } catch (Exception e){
            throw e;
        }
    }

    //下载指定路径下的文件zip格式
    public  void dowloadAttachZip(String path, String templateNM, HttpServletResponse response) throws Exception{
        //下载文件名乱码问题解决
        templateNM=  new String(templateNM.getBytes("gbk"),"iso8859-1");
        //将文件进行打包下载
        try {
            OutputStream out = response.getOutputStream();
            byte[] data = createZip(path+File.separator+"doc");//服务器存储地址
            response.reset();
            response.setHeader("Content-Disposition","attachment;fileName="+templateNM);
            response.addHeader("Content-Length", ""+data.length);
            response.setContentType("application/octet-stream;charset=UTF-8");
            IOUtils.write(data, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //创建压缩包
    public byte[] createZip(String srcSource) throws Exception{
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        //将目标文件打包成zip导出
        File file = new File(srcSource);
        a(zip,file,"");
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }
    //遍历读文件
    public void a(ZipOutputStream zip, File file, String dir) throws Exception {
        //如果当前的是文件夹，则进行进一步处理
        if (file.isDirectory()) {
            //得到文件列表信息
            File[] files = file.listFiles();
            //将文件夹添加到下一级打包目录
            zip.putNextEntry(new ZipEntry(dir + "/"));
            dir = dir.length() == 0 ? "" : dir + "/";
            //循环将文件夹中的文件打包
            for (int i = 0; i < files.length; i++) {
                a(zip, files[i], dir + files[i].getName());         //递归处理
            }
        } else {   //当前的是文件，打包处理
            //文件输入流
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            ZipEntry entry = new ZipEntry(dir);
            zip.putNextEntry(entry);
            zip.write(FileUtils.readFileToByteArray(file));
            IOUtils.closeQuietly(bis);
            zip.flush();
            zip.closeEntry();
        }
    }


}
