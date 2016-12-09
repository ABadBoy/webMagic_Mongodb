package com.badboy.github;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;


public class XingChenProcessor implements PageProcessor {

    String name;
    String code;
    Double oneMonth;
    Double threeMonth;
    Double sixMonth;

    Double oneYear;
    Double threeYear;

    private Site site = Site.me().setCycleRetryTimes(5).setRetryTimes(5).setSleepTime(500).setTimeOut(3 * 60 * 1000)
            .setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:38.0) Gecko/20100101 Firefox/38.0")
            .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
            .addHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")
            .setCharset("UTF-8");

    private static final MongoCollection<Document> table;

    static {
        MongoClient mongo = new MongoClient();
        MongoDatabase db = mongo.getDatabase("testDB");
        table = db.getCollection("message");
    }


    public static void main(String[] args) {
        Spider.create(new XingChenProcessor())
        .addUrl("http://fund.eastmoney.com/dingtou/syph_yndt.html").thread(5)
                .addPipeline(new ConsolePipeline()).run();
    }


    public void process(Page page) {
        getPageMessage(page);

    }


    public Site getSite() {
        return site;
    }


    private void getPageMessage(Page page) {
        List<String> links = page.getHtml().links().regex("http://fund.eastmoney.com/\\d+.html").all();
        page.addTargetRequests(links);
        name= page.getHtml().xpath("//*[@id=\"body\"]/div[12]/div/div/div[1]/div[1]/div/text()").toString();
        if (name == null) {
            page.setSkip(true);
            return;
        }
        code = page.getHtml().xpath("//*[@id=\"body\"]/div[12]/div/div/div[1]/div[1]/div/span[2]/text()").toString();
        oneMonth = Double.parseDouble(page.getHtml().xpath("//*[@id=\"body\"]/div[12]/div/div/div[2]/div[1]/div[1]/dl[1]/dd[2]/span[2]/text()").toString().replace("%", ""));

        threeMonth = Double.parseDouble(page.getHtml().xpath("//*[@id=\"body\"]/div[12]/div/div/div[2]/div[1]/div[1]/dl[2]/dd[2]/span[2]/text()").toString().replace("%", ""));
        sixMonth = Double.parseDouble(page.getHtml().xpath("//*[@id=\"body\"]/div[12]/div/div/div[2]/div[1]/div[1]/dl[3]/dd[2]/span[2]/text()").toString().replace("%", ""));

        oneYear = Double.parseDouble(page.getHtml().xpath("//*[@id=\"body\"]/div[12]/div/div/div[2]/div[1]/div[1]/dl[1]/dd[3]/span[2]/text()").toString().replace("%", ""));
        String three = page.getHtml().xpath("//*[@id=\"body\"]/div[12]/div/div/div[2]/div[1]/div[1]/dl[2]/dd[3]/span[2]/text()").toString().replace("%", "");
        if ("--".equals(three)) {
            threeYear = null;
        } else {
            threeYear = Double.parseDouble(three);
        }


        page.putField("name", name);
        page.putField("code", code);
        page.putField("oneMonth", oneMonth);
        page.putField("threeMonth", threeMonth);
        page.putField("sixMonth", sixMonth);
        page.putField("oneYear", oneYear);
        page.putField("threeYear", threeYear);



        Document document = new Document(page.getResultItems().getAll());
        table.insertOne(document);


    }



}
