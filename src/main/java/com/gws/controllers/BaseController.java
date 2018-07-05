/*
 * Copyright (C) 2016  HangZhou YuShi Technology Co.Ltd  Holdings Ltd. All rights reserved
 *
 * 本代码版权归杭州宇石科技所有，且受到相关的法律保护。
 * 没有经过版权所有者的书面同意，
 * 任何其他个人或组织均不得以任何形式将本文件或本文件的部分代码用于其他商业用途。
 *
 */
package com.gws.controllers;

import com.gws.common.constants.backstage.LangMark;
import com.gws.configuration.backstage.LangConfig;
import com.gws.enums.CodeStatus;
import com.gws.enums.SystemCode;
import com.gws.utils.GwsLogger;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * api请求基类
 */

@ResponseBody
public class BaseController {

    @ExceptionHandler(Exception.class)
    public JsonResult handleException(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        return response(SystemCode.BAD_REQUEST, null);
    }

    public static JsonResult success(Object data) {
        int lang = LangConfig.getLang();
        if(lang == LangMark.CN){
            return new JsonResult(SystemCode.SUCCESS.getCode(), SystemCode.SUCCESS.getMessageCN(), data);
        }else if(lang == LangMark.EN){
            return new JsonResult(SystemCode.SUCCESS.getCode(), SystemCode.SUCCESS.getMessageEN(), data);
        }else{
            return new JsonResult(SystemCode.SUCCESS.getCode(), SystemCode.SUCCESS.getMessageCN(), data);
        }
    }

    public JsonResult error(CodeStatus codeStatus) {
        return new JsonResult(codeStatus, null);
    }

    public JsonResult error(String code,String message) {
        return new JsonResult(code, message, null);
    }

    public JsonResult response(CodeStatus codeStatus, Object data) {
        return new JsonResult(codeStatus, data);
    }

    public JsonResult response(CodeStatus codeStatus) {
        return new JsonResult(codeStatus, null);
    }

    public static JsonResult sysError(Exception e) {
        int lang = LangConfig.getLang();
        if(lang == LangMark.CN){
            //如果是1显示中文
            return new JsonResult(SystemCode.SYS_ERROR.getCode(), SystemCode.SYS_ERROR.getMessageCN()+":"+e.getMessage(), null);
        }else if(lang == LangMark.EN){
            //如果是2显示英文
            return new JsonResult(SystemCode.SYS_ERROR.getCode(), SystemCode.SYS_ERROR.getMessageEN()+":"+e.getMessage(), null);
        }else{
            //默认都显示中文
            return new JsonResult(SystemCode.SYS_ERROR.getCode(), SystemCode.SYS_ERROR.getMessageCN()+":"+e.getMessage(), null);
        }
    }

    public static JsonResult valiError(Exception e) {
        int lang = LangConfig.getLang();
        if(lang == LangMark.CN){
            return new JsonResult(SystemCode.VALIDATION_ERROR.getCode(), SystemCode.VALIDATION_ERROR.getMessageCN()+":"+e.getMessage(), null);
        }else if(lang == LangMark.EN){
            return new JsonResult(SystemCode.VALIDATION_ERROR.getCode(), SystemCode.VALIDATION_ERROR.getMessageEN()+":"+e.getMessage(), null);
        }else{
            return new JsonResult(SystemCode.VALIDATION_ERROR.getCode(), SystemCode.VALIDATION_ERROR.getMessageCN()+":"+e.getMessage(), null);
        }

    }

    public static JsonResult loginSuccess(Object data){
        int lang = LangConfig.getLang();
        if(lang == LangMark.CN){
            return new JsonResult(SystemCode.SUCCESS_LOGIN.getCode(), SystemCode.SUCCESS_LOGIN.getMessageCN(),data);
        }else if(lang == LangMark.EN){
            return new JsonResult(SystemCode.SUCCESS_LOGIN.getCode(), SystemCode.SUCCESS_LOGIN.getMessageEN(),data);
        }else{
            return new JsonResult(SystemCode.SUCCESS_LOGIN.getCode(), SystemCode.SUCCESS_LOGIN.getMessageCN(),data);
        }
    }

    public static JsonResult loginError(Exception e){
        int lang = LangConfig.getLang();
        if(lang == LangMark.CN){
            return new JsonResult(SystemCode.FAIL_LOGIN.getCode(), SystemCode.FAIL_LOGIN.getMessageCN()+":"+e.getMessage(), null);
        }else if(lang == LangMark.EN){
            return new JsonResult(SystemCode.FAIL_LOGIN.getCode(), SystemCode.FAIL_LOGIN.getMessageEN()+":"+e.getMessage(), null);
        }else{
            return new JsonResult(SystemCode.FAIL_LOGIN.getCode(), SystemCode.FAIL_LOGIN.getMessageCN()+":"+e.getMessage(), null);
        }
    }

    public static JsonResult noUserError(){
        int lang = LangConfig.getLang();
        if(lang == LangMark.CN){
            return new JsonResult(SystemCode.NO_USERNAME.getCode(), SystemCode.NO_USERNAME.getMessageCN(), null);
        }else if(lang == LangMark.EN){
            return new JsonResult(SystemCode.NO_USERNAME.getCode(), SystemCode.NO_USERNAME.getMessageEN(), null);
        }else{
            return new JsonResult(SystemCode.NO_USERNAME.getCode(), SystemCode.NO_USERNAME.getMessageCN(), null);
        }
    }

    public static JsonResult loginFail(){
        int lang = LangConfig.getLang();
        if(lang == LangMark.CN){
            return new JsonResult(SystemCode.WRONG_USER_PWD.getCode(), SystemCode.WRONG_USER_PWD.getMessageCN(), null);
        }else if(lang == LangMark.EN){
            return new JsonResult(SystemCode.WRONG_USER_PWD.getCode(), SystemCode.WRONG_USER_PWD.getMessageEN(), null);
        }else{
            return new JsonResult(SystemCode.WRONG_USER_PWD.getCode(), SystemCode.WRONG_USER_PWD.getMessageCN(), null);
        }
    }

    public static JsonResult authError(Exception e){
        int lang = LangConfig.getLang();
        if(lang == LangMark.CN){
            return new JsonResult(SystemCode.NOT_AUTH.getCode(),SystemCode.NOT_AUTH.getMessageCN()+":"+e.getMessage(),null);
        }else if(lang == LangMark.EN){
            return new JsonResult(SystemCode.NOT_AUTH.getCode(),SystemCode.NOT_AUTH.getMessageEN()+":"+e.getMessage(),null);
        }else{
            return new JsonResult(SystemCode.NOT_AUTH.getCode(),SystemCode.NOT_AUTH.getMessageCN()+":"+e.getMessage(),null);
        }
    }

    public static JsonResult tokenError(Exception e){
        int lang = LangConfig.getLang();
        if(lang == LangMark.CN){
            return new JsonResult(SystemCode.TOKEN_ERROR.getCode(),SystemCode.TOKEN_ERROR.getMessageCN()+":"+e.getMessage(),null);
        }else if(lang == LangMark.EN){
            return new JsonResult(SystemCode.TOKEN_ERROR.getCode(),SystemCode.TOKEN_ERROR.getMessageEN()+":"+e.getMessage(),null);
        }else{
            return new JsonResult(SystemCode.TOKEN_ERROR.getCode(),SystemCode.TOKEN_ERROR.getMessageCN()+":"+e.getMessage(),null);
        }
    }

    public static JsonResult freezeFail(){
        int lang = LangConfig.getLang();
        if(lang == LangMark.CN){
            return new JsonResult(SystemCode.FREEZE_FAIL.getCode(),SystemCode.FREEZE_FAIL.getMessageCN(),null);
        }else if(lang == LangMark.EN){
            return new JsonResult(SystemCode.FREEZE_FAIL.getCode(),SystemCode.FREEZE_FAIL.getMessageEN(),null);
        }else{
            return new JsonResult(SystemCode.FREEZE_FAIL.getCode(),SystemCode.FREEZE_FAIL.getMessageCN(),null);
        }
    }
    public static JsonResult deleteFail(){
        int lang = LangConfig.getLang();
        if(lang == LangMark.CN){
            return new JsonResult(SystemCode.DELETE_FAIL.getCode(),SystemCode.DELETE_FAIL.getMessageCN(),null);
        }else if(lang == LangMark.EN){
            return new JsonResult(SystemCode.DELETE_FAIL.getCode(),SystemCode.DELETE_FAIL.getMessageEN(),null);
        }else{
            return new JsonResult(SystemCode.DELETE_FAIL.getCode(),SystemCode.DELETE_FAIL.getMessageCN(),null);
        }
    }

}
