package com.badboy.crawler;

import com.badboy.formatter.MyDoubleFormatter;
import com.badboy.model.FundModel;
import com.badboy.util.DatabaseUtil;
import com.jfinal.plugin.activerecord.Model;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.ConsolePageModelPipeline;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.Formatter;
import us.codecraft.webmagic.model.annotation.TargetUrl;

import java.sql.PreparedStatement;
import java.sql.SQLException;


@TargetUrl("http://fund.eastmoney.com/\\d+.html")
public class XingChenFund  extends Model<FundModel> implements AfterExtractor {

    @ExtractBy(value = "//*[@id=\"body\"]/div[12]/div/div/div[1]/div[1]/div/text()",notNull = true)
    String name;

    @ExtractBy(value = "//*[@id=\"body\"]/div[12]/div/div/div[1]/div[1]/div/span[2]/text()")
    String code;

    @Formatter(formatter=MyDoubleFormatter.class)
    @ExtractBy(value = "//*[@id=\"body\"]/div[12]/div/div/div[3]/div[1]/div[1]/dl[1]/dd[2]/span[2]/text()")
    Double oneMonth;

    @Formatter(formatter=MyDoubleFormatter.class)

    @ExtractBy(value = "//*[@id=\"body\"]/div[12]/div/div/div[3]/div[1]/div[1]/dl[2]/dd[2]/span[2]/text()")
    Double threeMonth;

    @Formatter(formatter=MyDoubleFormatter.class)
    @ExtractBy(value = "//*[@id=\"body\"]/div[12]/div/div/div[3]/div[1]/div[1]/dl[3]/dd[2]/span[2]/text()")
    Double sixMonth;

    @Formatter(formatter=MyDoubleFormatter.class)
    @ExtractBy(value = "//*[@id=\"body\"]/div[12]/div/div/div[3]/div[1]/div[1]/dl[1]/dd[3]/span[2]/text()")
    Double oneYear;

    @Formatter(formatter=MyDoubleFormatter.class)
    @ExtractBy(value = "//*[@id=\"body\"]/div[12]/div/div/div[3]/div[1]/div[1]/dl[2]/dd[3]/span[2]/text()")
    Double threeYear;


    public static void main(String[] args) {
        Site site =Site.me().setCycleRetryTimes(5).setRetryTimes(5).setSleepTime(500).setTimeOut(3 * 60 * 1000)
                .setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:38.0) Gecko/20100101 Firefox/38.0")
                .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                .addHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")
                .setCharset("UTF-8");

        OOSpider.create(site,new ConsolePageModelPipeline(), XingChenFund.class)
                .addUrl("http://fund.eastmoney.com/dingtou/syph_yndt.html").thread(5).run();
    }

    public void afterProcess(Page page) {
        try {
            PreparedStatement stmt = DatabaseUtil.getConnection().
                    prepareStatement("INSERT INTO fund_info (name,code,oneMonth,threeMonth,sixMonth,oneYear,threeYear) VALUES(?,?,?,?,?,?,?)");
            stmt.setString(1,name);
            stmt.setString(2,code);
            stmt.setDouble(3,oneMonth);
            stmt.setDouble(4,threeMonth!=null?threeMonth:Double.parseDouble("0"));
            stmt.setDouble(5,sixMonth!=null?sixMonth:Double.parseDouble("0"));
            stmt.setDouble(6,oneYear!=null?oneYear:Double.parseDouble("0"));
            stmt.setDouble(7,threeYear!=null?threeYear:Double.parseDouble("0"));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
