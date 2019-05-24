package com.yuanwang.common.controller;

import com.yuanwang.common.utils.UploadUtil;
import com.yuanwang.sys.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName UploadController
 * @Description TODO
 * @Author 86159
 * @Date 2019/5/24 23:09
 * @Version 1.0
 **/
@Controller
@RequestMapping("/upload")
public class UploadController {
    @RequestMapping("uploadFile")
    @ResponseBody
/*    public Map<String,Object> uploadFile(MultipartFile file, HttpServletRequest request, @PathVariable String type){*/
    public Map<String,Object> uploadFile(MultipartFile file, HttpServletRequest request, HttpSession session){
        Map<String,Object> map=new HashMap<String,Object>( );
        try{
            User user=(User)session.getAttribute("user");
            String type=user.getUserName();
            String path=request.getSession().getServletContext().getRealPath("\\upload\\"+type+"\\");
            String image= UploadUtil.uploadFile(file,path);
            map.put("code",0);
            map.put("image",image);
        }catch(Exception e){
            map.put("code",1);
            e.printStackTrace();
        }
         return  map;
    }

}
