package baseDemo;

import cn.hutool.core.util.ReUtil;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.List;

/**
 * 基本Demo
 * 实现PageProcessor，注解方式不好用放弃它了
 */
public class BaseDemo implements PageProcessor {
    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);

    @Override
    // process是定制爬虫逻辑的核心接口，在这里编写抽取逻辑
    public void process(Page page) {
        Html html = page.getHtml();
        String url = page.getUrl().get();

        String gameId = ReUtil.get("gl(\\d*)", url, 1);
        // 部分二：定义如何抽取页面信息，并保存下来
        if (!"gl".equals(gameId)) {
            page.putField("gameid", gameId);
            page.putField("gamename", html.xpath("//div[@class='weizhi']/a[3]/text()"));
            page.putField("id", ReUtil.get(gameId + "/(.*?).html", url, 1));
            page.putField("title", html.xpath("//span[@class='big']/text()"));
            page.putField("date", html.xpath("//span[@class='time']/text()"));
            page.putField("content", html.xpath("//div[@class='contentbox']/p"));
            page.setSkip(false);
        } else {
            // 部分三：从页面发现后续的url地址来抓取
            //<a href="https://m.ali213.net/news/gl2305/1059089.html" target="_blank" title="《崩坏星穹铁道》爱乐之城怎么解锁 爱乐之城成就攻略">《崩坏星穹铁道》爱乐之城怎么解锁 爱乐之城成就攻略</a>
            List<String> targetUrls = html.xpath("//div[@class='zxgl']//a/@href").all();
//        page.addTargetRequests(targetUrls);
            page.addTargetRequest(targetUrls.get(0));
            page.setSkip(true);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        System.out.println(ReUtil.get("/gl(.*?)/", "https://m.ali213.net/news/gl2308/1116091.html", 0));
    }
}
