package com.atguigu.quichindex;

/**
 * 作者：杨光福 on 2016/4/14 14:01
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：人对象
 */
public class Person {
    /**
     * 姓
     */
    private String name;
    /**
     * 拼音
     */
    private String pinyin;
    public Person(String name){
        this.name = name;
        this.pinyin = PinYinUtils.getPinYin(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", pinyin='" + pinyin + '\'' +
                '}';
    }
}
