package com.badboy.github;

import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.ConsolePageModelPipeline;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.TargetUrl;

@TargetUrl("http://cn.morningstar.com/quicktake/\\w+")
public class XingChen {


    @ExtractBy(value = "//*[@id=\"qt_fund\"]/span[1]/text()")
    private String codeAndName;

    @ExtractBy(value = "//*[@id=\"qt_fund\"]/img[1]/@src")
    private String name;


    public static void main(String[] args) {
        OOSpider.create(Site.me().setSleepTime(1000)
                , new ConsolePageModelPipeline(), XingChen.class)
                .addUrl("http://cn.morningstar.com/quickrank/default.aspx").thread(1).run();
    }

}
