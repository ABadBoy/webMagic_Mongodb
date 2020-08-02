package com.badboy.formatter;

import org.apache.commons.io.IOUtils;
import us.codecraft.webmagic.model.formatter.ObjectFormatter;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RoomNumFormatter implements ObjectFormatter {

    public Object format(String s) throws Exception {
        if (s != null) {
            String position = null;
            String imageUrl = null;
            double result = 0;
            Pattern p = Pattern.compile(":(.*)px");
            Matcher m = p.matcher(s);

            while(m.find()){
                position = m.group(1);
                if (position.contains("-")) {
                    position = position.replace("-", "");
                }
                double i = Double.valueOf(position);
                result = i / 30;
            }

            Pattern p1 = Pattern.compile("\\((.*)\\)");
            Matcher m1 = p1.matcher(s);
            while(m1.find()){
                imageUrl = m1.group(1);
            }

            if (imageUrl != null) {
                String  filename = imageUrl.substring(imageUrl.lastIndexOf("/")+1);
                String str ="C:\\my\\room\\" + filename;
                File saveFile = new File(str);
                if (!saveFile.exists()) {
                    URL url = new URL("http:"+imageUrl);
                    DataInputStream dataInputStream = new DataInputStream(url.openStream());
                    OutputStream outputStream = new FileOutputStream(saveFile);
                    IOUtils.copy(dataInputStream,outputStream);
                }
            }

            return (int)result;
        }
        return null;
    }

    public Class clazz() {
        return null;
    }

    public void initParam(String[] extra) {

    }
}
