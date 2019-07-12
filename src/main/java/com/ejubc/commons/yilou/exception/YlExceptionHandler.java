package com.ejubc.commons.yilou.exception;

import com.ejubc.commons.base.ApiResponse;
import com.ejubc.commons.exception.BusinessException;
import com.ejubc.commons.exception.BusinessExceptionUtils;
import com.ejubc.commons.utils.LocalStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 全局统一异常处理
 *
 * @author xy
 */
@ControllerAdvice
@Slf4j
public class YlExceptionHandler {

    /**
     * 处理spring mvc 入参绑定异常
     */
    @ResponseBody
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse handleBindException(BindException e) {
        return buildingResponseByBindingResult(e.getBindingResult());
    }

    /**
     * 处理方法参数无效异常
     */
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return buildingResponseByBindingResult(e.getBindingResult());
    }

    /**
     * 处理mgt自定义异常
     */
    @ResponseBody
    @ExceptionHandler(YlCloudException.class)
    public ApiResponse handleYlCloudException(HttpServletRequest request, YlCloudException e) {
        log.error("远程服务[{}]异常: {}，URL：{}", e.getServiceName(), e.getMessage(), request.getRequestURI());
        return new ErrorResponse(e.getCode(), e.getMsg(), "远程服务[" + e.getServiceName() + "]异常");
    }

    /**
     * 处理mgt自定义异常
     */
    @ResponseBody
    @ExceptionHandler(YlException.class)
    public ApiResponse handleYlException(HttpServletRequest request, HttpServletResponse response, YlException e) {
        IErrorCode codeEnum = e.getCodeEnum();
        Object[] params = e.getParams();
        // 系统异常打印异常栈，用户异常打印异常信息
        String detail = e.getDetail();
        if (ErrorScope.SYSTEM.equals(codeEnum.getScope())) {
            params = new String[]{codeEnum.getCode()};
            codeEnum = SysErrorCode.SYS0001;
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            log.error("系统异常: {}，URL：{}", e.getMessage(), request.getRequestURI(), e);
            if (detail == null) {
                detail = e.getMessage();
            }
        } else {
            log.info("业务异常: {}，URL：{}", e.getMessage(), request.getRequestURI());
        }


        ApiResponse apiResponse = buildingResponseErrorCodeEnum(codeEnum, detail, params);
        apiResponse.setResponseCode(e.getCodeEnum().getCode());
        return apiResponse;
    }

    /**
     * 处理超过最大上传大小的异常
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseBody
    public ApiResponse handleMaxUploadSizeExceededException(){
        return ErrorResponse.failure(SysErrorCode.SYS0030);
    }

    /**
     * 处理其它异常
     */
    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse handleException(HttpServletRequest request, Exception e) {
        log.error("系统异常：{}，URL：{}", e.getMessage(), request.getRequestURI(), e);
        return buildingResponseErrorCodeEnum(SysErrorCode.SYS0000, e.getMessage());
    }

    /**
     * 处理易居sdk中定义的异常（原封不动输出）
     */
    @ResponseBody
    @ExceptionHandler(BusinessException.class)
    public ApiResponse handleBusinessException(HttpServletRequest request, BusinessException e) {
        log.info("业务异常：{}，URL：{}", e.getMessage(), request.getRequestURI(), e);
        return new ErrorResponse(e.getBusinessCode(), e.getMessage(), null);
    }

    private ApiResponse buildingResponseByBindingResult(BindingResult bindingResult) {

        FieldError fieldError = bindingResult.getFieldError();


        String message;
        if (fieldError != null) {
            if (TypeMismatchException.ERROR_CODE.equals(fieldError.getCode())) {
                // 类型不匹配
                message = "请检查提交参数";
            } else {
                message = fieldError.getDefaultMessage();
            }
        } else {
            message = "请检查提交参数";
        }
        log.warn("参数错误，{}", message);

        return new ErrorResponse(SysErrorCode.SYS1000.getCode(), message, null);
    }

    private ApiResponse buildingResponseErrorCodeEnum(IErrorCode errorCode, String detail, Object... params) {

        // 优先从注册中心中获取code对应的msg，不存在的情况下从枚举定义中获取
        String msg;
        try {
            msg = BusinessExceptionUtils.getBusinessInfo(errorCode.getCode(), params);
        } catch (Exception e) {
            msg = errorCode.getMsg();
            if (params != null && params.length != 0) {
                msg = LocalStringUtils.messageFormat(msg, params);
            }
        }
        if (detail == null) {
            return new ApiResponse(errorCode.getCode(), msg);
        }
        return new ErrorResponse(errorCode.getCode(), msg, detail);
    }

}
