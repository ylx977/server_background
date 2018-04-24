package com.gws.enums;

/**
 * 【分页用枚举】
 *
 * @author xiongshihai 2017/4/25
 */
public enum PageEnum {

    SIZE_20(20),
    SIZE_15(15),
    SIZE_10(10),
    SIZE_8(8);

    private int size;

    PageEnum(int size){
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
