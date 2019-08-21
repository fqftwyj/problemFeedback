package com.yuanwang.common.utils;

import java.io.File;

public class FileUtils {
    public static String getFileSize(String path){
        //传入文件路径
        File file = new File(path);
        //测试此文件是否存在
        if(file.exists()){
            //如果是文件夹
            //这里只检测了文件夹中第一层 如果有需要 可以继续递归检测
            if(file.isDirectory()){
                int size = 0;
                for(File zf : file.listFiles()){
                    if(zf.isDirectory()) continue;
                    size += zf.length();
                }
               return (size/1024)+"kb";
            }else{
                return (file.length()/1024)+"kb";
            }
            //如果文件不存在
        }else{
            return "此文件不存在";
        }
    }
}
