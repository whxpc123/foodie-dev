package com.imooc.enums;

public enum PayMethod {

    WEIXIN(1,"微信"),
    ALIPAY(2,"支付宝");


    public final Integer type;
    public final String value;

    PayMethod(Integer type, String value){
        this.type =type;
        this.value=value;
    }

   public static boolean isSupport(Integer type){
        if(type == null) return false;
        PayMethod[] values = PayMethod.values();
        for (PayMethod payMethod : values) {
            if(payMethod.type == type)
                return true;
        }
        return false;
    }


}
