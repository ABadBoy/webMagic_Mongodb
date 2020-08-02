package com.badboy.crawler;

import com.badboy.formatter.FileNameFormatter;
import com.badboy.formatter.RoomNumFormatter;
import com.badboy.util.DatabaseUtil;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.ConsolePageModelPipeline;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.model.annotation.*;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@TargetUrl("http://sh.ziroom.com/x/\\d+.html")
@HelpUrl("http://sh.ziroom.com/z/s100025-t100337-p\\d+/")
public class Room implements AfterExtractor {

    @ExtractByUrl
    String url;

    @ExtractBy(value = "/html/body/section/aside/h1/text()",notNull = true)
    String name;

    @ExtractBy(value = "/html/body/section/aside/div[1]/span[1]/text()")
    String identity;

    @Formatter(formatter= RoomNumFormatter.class)
    @ExtractBy(value = ".Z_price .num:eq(1)",type = ExtractBy.Type.Css)
    Integer num1;

    @Formatter(formatter= RoomNumFormatter.class)
    @ExtractBy(value = ".Z_price .num:eq(2)",type = ExtractBy.Type.Css)
    Integer num2;

    @Formatter(formatter= RoomNumFormatter.class)
    @ExtractBy(value = ".Z_price .num:eq(3)",type = ExtractBy.Type.Css)
    Integer num3;

    @Formatter(formatter= RoomNumFormatter.class)
    @ExtractBy(value = ".Z_price .num:eq(4)",type = ExtractBy.Type.Css)
    Integer num4;

    @Formatter(formatter= FileNameFormatter.class)
    @ExtractBy(value = ".Z_price .num:eq(1)",type = ExtractBy.Type.Css)
    String imageFileName;

    @ExtractBy(value = "/html/body/section/aside/div[1]/span[2]/text()")
    String payType;

    @ExtractBy(value = "/html/body/section/aside/div[3]/div[1]/dl[1]/dd/text()")
    String area;

    @ExtractBy(value = "/html/body/section/aside/div[3]/div[1]/dl[2]/dd/text()")
    String towards;

    @ExtractBy(value = "/html/body/section/aside/div[3]/div[1]/dl[3]/dd/text()")
    String houseType;

    @ExtractBy(value = "/html/body/section/aside/div[3]/ul/li[1]/span[2]/span/text()")
    String position;

    @ExtractBy(value = "/html/body/section/aside/div[3]/ul/li[2]/span[2]/text()")
    String floor;

    @ExtractBy(value = "/html/body/section/aside/div[3]/ul/li[3]/span[2]/text()")
    String elevator;

    @ExtractBy(value = "/html/body/section/aside/div[3]/ul/li[4]/span[2]/text()")
    String years;



    public static void main(String[] args) {
        Site site =Site.me().setCycleRetryTimes(5).setRetryTimes(5).setSleepTime(5000).setTimeOut(3 * 60 * 1000)
                .setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:38.0) Gecko/20100101 Firefox/38.0")
                .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                .addHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")
                .setCharset("UTF-8");

       /* HttpClientDownloader httpClientDownloader=new HttpClientDownloader();

        httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(
                new Proxy("163.125.156.84",8888)
                ,new Proxy("60.18.164.46",63000)));*/

        OOSpider.create(site,new ConsolePageModelPipeline(), Room.class)/*.setDownloader(httpClientDownloader)*/
                .addUrl("http://sh.ziroom.com/z/s100025-t100337/?isOpen=0").thread(5).run();
    }

    public void afterProcess(Page page) {
        try {
            Tesseract tesseract = new Tesseract();
            tesseract.setDatapath("C:/my/Tess4J-3.4.8-src/Tess4J/tessdata");

            // the path of your tess data folder
            // inside the extracted file
            String text = tesseract.doOCR(new File("C:\\my\\room\\"+imageFileName));

            // path of your image file
            System.out.print(text);

            String money = new StringBuilder().append(text.charAt(num1)).
                    append(text.charAt(num2)).append(text.charAt(num3)).append(text.charAt(num4)).toString();

            PreparedStatement stmt = DatabaseUtil.getConnection().
                    prepareStatement("INSERT INTO room (url,name,money,area,towards,houseType,position,floor,elevator,years) VALUES(?,?,?,?,?,?,?,?,?,?)");
            stmt.setString(1,url);
            stmt.setString(2,name);
            stmt.setString(3,money);
            stmt.setString(4,area);
            stmt.setString(5,towards);
            stmt.setString(6,houseType);
            stmt.setString(7,position);
            stmt.setString(8,floor);
            stmt.setString(9,elevator);
            stmt.setString(10,years);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (TesseractException e) {
            e.printStackTrace();
        }

    }
}
