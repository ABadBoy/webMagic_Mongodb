package com.badboy.formatter;

import us.codecraft.webmagic.model.formatter.ObjectFormatter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileNameFormatter implements ObjectFormatter {
    public Object format(String s) throws Exception {
        if (s != null) {
            String imageUrl = null;

            Pattern p1 = Pattern.compile("\\((.*)\\)");
            Matcher m1 = p1.matcher(s);
            while(m1.find()){
                imageUrl = m1.group(1);
            }

            if (imageUrl != null) {
                imageUrl = imageUrl.substring(imageUrl.lastIndexOf("/")+1);

            }
            return imageUrl;
        }
        return null;
    }

    public Class clazz() {
        return null;
    }

    public void initParam(String[] extra) {

    }
}
