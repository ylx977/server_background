package com.gws.exception;

import com.gws.common.constants.backstage.ErrorMsg;
import com.gws.common.constants.backstage.LangMark;

/**
 * @author ylx
 * Created by fuzamei on 2018/5/11.
 */
public class ExceptionUtils {

    public static void throwException(ErrorMsg errorMsg,Integer lang){
        if(null == lang){
            throw new RuntimeException(errorMsg.getMessageCN());
        }
        if(lang == LangMark.CN){
            throw new RuntimeException(errorMsg.getMessageCN());
        }else if(lang == LangMark.EN){
            throw new RuntimeException(errorMsg.getMessageEN());
        }else{
            throw new RuntimeException(errorMsg.getMessageCN());
        }
    }

    public static void throwException(ErrorMsg errorMsg,Integer lang,Object addMsg){
        if(null == lang){
            throw new RuntimeException(errorMsg.getMessageCN());
        }
        if(lang == LangMark.CN){
            throw new RuntimeException(errorMsg.getMessageCN()+addMsg);
        }else if(lang == LangMark.EN){
            throw new RuntimeException(errorMsg.getMessageEN()+addMsg);
        }else{
            throw new RuntimeException(errorMsg.getMessageCN()+addMsg);
        }
    }

}
