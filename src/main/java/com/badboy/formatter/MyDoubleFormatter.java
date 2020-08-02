package com.badboy.formatter;

import us.codecraft.webmagic.model.formatter.ObjectFormatter;

public class MyDoubleFormatter implements ObjectFormatter {


    public Object format(String s) throws Exception {
        if ("--".equals(s) || s==null) {
            return null;
        }else {
            return  Double.parseDouble(s.replace("%", ""));
        }

    }

    public Class clazz() {
        return Double.class;
    }

    public void initParam(String[] strings) {

    }


}
