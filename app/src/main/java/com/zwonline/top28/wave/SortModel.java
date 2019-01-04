package com.zwonline.top28.wave;

public class SortModel<T> {

    private String name;
    private String letters;//显示拼音的首字母

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLetters() {
        return letters;
    }

    public void setLetters(String letters) {
        this.letters = letters;
    }

    public <T> T fun(T t) {            // 可以接收任意类型的数据
        return t;                    // 直接把参数返回
    }
}
