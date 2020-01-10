package com.imooc.controller.center;


import com.imooc.pojo.Users;
import com.imooc.pojo.bo.center.CenterUserBO;
import com.imooc.resources.FileUpload;
import com.imooc.service.center.ICenterUserService;
import com.imooc.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Api(value = "用户中心",tags = {"用户中心相关接口"})
@RestController
@RequestMapping("userInfo")
public class CenterUserController {

    @Autowired
    private ICenterUserService centerUserService;
    @Autowired
    private FileUpload fileUpload;

    private static final String USER_FACE = File.separator+"Users"+File.separator+"xiangpc"+File.separator+"Desktop"+File.separator+"fileImgs";

    @ApiOperation(value = "修改用户",notes = "修改用户",httpMethod = "POST")
    @PostMapping("update")
    public IMOOCJSONResult update(@RequestParam String userId, @RequestBody CenterUserBO centerUserBO, HttpServletRequest request, HttpServletResponse response){
        BeanValidator.check(centerUserBO);
        Users users = centerUserService.updateUserInfo(userId, centerUserBO);
        CookieUtils.setCookie(request,response,"user", JsonUtils.objectToJson(users),true);
        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "用户头像上传",notes = "用户头像上传",httpMethod = "POST")
    @PostMapping("uploadFace")
    public IMOOCJSONResult uploadFace(@RequestParam String userId, MultipartFile file,HttpServletRequest request,HttpServletResponse response){
        //头像保存地址

        String uploadPathPrefix = File.separator+userId;
        String newFieName = "";

        if(file == null){
           return IMOOCJSONResult.errorMsg("文件不能为空");
        }else{
            String fileName = file.getOriginalFilename();
            if(StringUtils.isNotBlank(fileName)){
                FileOutputStream fileOutputStream = null;
                try {
                String[] fileNameArr = fileName.split("\\.");
                String suffix = fileNameArr[fileNameArr.length-1];
                //TODO 图片格式限制
                newFieName = File.separator+"face-" +userId +"."+suffix;
                //最终文件上传格式jpg，png，jpeg
                String finalPath =fileUpload.getImageUserFaceLocation()+uploadPathPrefix+newFieName;
                File outFile = new File(finalPath);
                if(outFile.getParentFile() !=null){
                    outFile.getParentFile().mkdirs();
                }
                    fileOutputStream = new FileOutputStream(outFile);
                    InputStream inputStream = file.getInputStream();
                    IOUtils.copy(inputStream,fileOutputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finally {
                    try {
                        if(fileOutputStream != null){
                            fileOutputStream.flush();
                            fileOutputStream.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

        }
        String finalUserFaceUrl = fileUpload.getImageServerUrl()+"/"+userId+newFieName+"?t="+ DateUtil.getCurrentDateString(DateUtil.DATE_PATTERN);
        Users users = centerUserService.updateUserFace(userId, finalUserFaceUrl);
        CookieUtils.setCookie(request,response,"user", JsonUtils.objectToJson(users),true);
        return IMOOCJSONResult.ok();
    }

}
