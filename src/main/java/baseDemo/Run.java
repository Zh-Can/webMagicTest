package baseDemo;

import cn.hutool.core.util.ReUtil;
import pipeline.YxglPipline;
import us.codecraft.webmagic.Spider;

import java.sql.SQLException;

public class Run {


    public static void main(String[] args) throws SQLException {
//        baseDemoRun();
        String s = ReUtil.get("https://m.ali213.net/news/\\w+/(.*?).html", "https://m.ali213.net/news/gl2308/1114607.html", 1);
        System.out.println(s);

    }

    private static void baseDemoRun() {
        Spider.create(new BaseDemo())
        //从"https://gl.ali213.net/m/217061/"开始抓
        .addUrl("https://gl.ali213.net/m/217061/")
        //处理数据
        .addPipeline(new YxglPipline())
        //开启5个线程抓取
        .thread(2)
        //启动爬虫
        .run();
    }
}
