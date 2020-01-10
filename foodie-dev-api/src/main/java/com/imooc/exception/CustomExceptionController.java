package com.imooc.exception;


import com.imooc.utils.IMOOCJSONResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class CustomExceptionController {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public IMOOCJSONResult handlerMaxFileUploadSizeException(MaxUploadSizeExceededException exception){
        return IMOOCJSONResult.errorMsg("文件上传大小不能超过500kb");
    }
}
