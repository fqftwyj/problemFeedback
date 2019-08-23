package com.yuanwang.common.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Random;

/**
 * @ClassName UploadUtil
 * @Description TODO
 * @Author 86159
 * @Date 2019/5/24 23:12
 * @Version 1.0
 **/
public class UploadUtil {
    //上传文件到服务器的方法
    public   static String  uploadFile(MultipartFile file, String path) throws  Exception{
        String name=file.getOriginalFilename();//上传的真实名称
        String suffixName=name.substring(name.lastIndexOf("."));//获取后缀名
        String hash=Integer.toHexString(new Random().nextInt());//自定义随机数（字母+数字）作为文件名
        String fileName=hash+suffixName;
        File tempFile=new File(path,fileName);
        if(!tempFile.getParentFile().exists()){
            tempFile.getParentFile().mkdir();
        }
        if(tempFile.exists()){
            tempFile.delete();
        }
        tempFile.createNewFile();
        file.transferTo(tempFile);
        return tempFile.getName();


    }
    //删除上传的文件


}
