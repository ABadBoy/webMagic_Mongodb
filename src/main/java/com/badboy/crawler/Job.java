package com.badboy.crawler;

import com.badboy.util.DatabaseUtil;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.downloader.selenium.SeleniumDownloader;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.ConsolePageModelPipeline;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.ExtractByUrl;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@TargetUrl("https://www.lagou.com/jobs/\\d+.html\\?show=\\w+")
@HelpUrl("https://www.lagou.com/shanghai-zhaopin/Java/\\d+/")
public class Job implements AfterExtractor {

    @ExtractByUrl
    String url;

    @ExtractBy(value = "//*[@id=\"job_company\"]/dt/a/div/h3/em/text()")
    String company;

    @ExtractBy(value = "/html/body/div[7]/div/div[1]/div/h1/text()")
    String jobTitle;

    @ExtractBy(value = "/html/body/div[7]/div/div[1]/dd/h3/span[1]/text()")
    String salary;

    @ExtractBy(value = "/html/body/div[7]/div/div[1]/dd/p/text()")
    String time;

    @ExtractBy(value = "//*[@id=\"job_detail\"]/dd[3]/div[1]/a[2]/text()")
    String area;

    @ExtractBy(value = "//*[@id=\"job_detail\"]/dd[3]/div[1]/text()")
    String address;


    public static void main(String[] args) {
        Site site =Site.me().setCycleRetryTimes(5).setRetryTimes(5).setSleepTime(5000).setTimeOut(3 * 60 * 1000)
                .setUserAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.106 Safari/537.36")
                .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .addHeader("accept-encoding", "gzip, deflate, br")
                .setCharset("UTF-8");

       /* HttpClientDownloader httpClientDownloader=new HttpClientDownloader();

        httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(
                new Proxy("163.125.156.84",8888)
                ,new Proxy("60.18.164.46",63000)));*/

        System.getProperties().setProperty("selenuim_config", "C:\\webmagic\\webmagic-selenium\\config.ini");


        OOSpider.create(site,new ConsolePageModelPipeline(), Job.class)/*.setDownloader(httpClientDownloader)*/
                .addUrl("https://www.lagou.com/shanghai-zhaopin/Java/")
                .setDownloader(new SeleniumDownloader("C:\\my\\chromedriver_win32\\chromedriver.exe").setSleepTime(5000)).thread(5).run();
    }

    public void afterProcess(Page page) {
        try {


            PreparedStatement stmt = DatabaseUtil.getConnection().
                    prepareStatement("INSERT INTO job (url,company,jobTitle,salary,time,area,address) VALUES(?,?,?,?,?,?,?)");
            stmt.setString(1,url);
            stmt.setString(2,company);
            stmt.setString(3,jobTitle);
            stmt.setString(4,salary);
            stmt.setString(5,time);
            stmt.setString(6,area);
            stmt.setString(7,address);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
