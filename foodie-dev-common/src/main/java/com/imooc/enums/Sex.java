package com.imooc.enums;

import lombok.Getter;


public enum Sex {

    WOMEN(0,"恧"),
    MAN(1,"男"),
    SECRET(2,"保密");

    public final Integer type;
    public final String value;

    Sex(Integer type,String value){
        this.type =type;
        this.value=value;
    }


}
