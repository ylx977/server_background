package com.gws.base.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gws.enums.SystemCode;
import com.gws.utils.GwsLogger;
import com.gws.utils.webservice.RetResult;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

	@ExceptionHandler
	public @ResponseBody RetResult handleBusinessException(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        GwsLogger.error(ex, "handle H5BaseController Exception");
        return response(SystemCode.BAD_REQUEST, ex.getMessage(), response);
	}

    /**
     * 
     * 【枚举错误码返回】
     * 
     * @author wenfei 2017年4月6日
     * @param codeStatus
     * @param data
     * @return
     */
    protected RetResult response(SystemCode codeStatus, Object data, HttpServletResponse response) {
        return new RetResult(codeStatus.getCode(), codeStatus.getMessageCN(), data);
    }

    /**
     * 
     * 【自定义错误码返回】
     * 
     * @author wenfei 2017年4月6日
     * @param code
     * @param message
     * @param data
     * @return
     */
//    protected RetResult response(String code, String message, Object data, HttpServletResponse response) {
//        return new RetResult(code, message, data);
//    }

}
