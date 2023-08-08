package baseDemo;

import cn.hutool.core.util.StrUtil;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import utils.DbUtils;

import java.util.ArrayList;
import java.util.List;

public class BaseDemoPipline implements Pipeline {

    @Override
    public void process(ResultItems resultItems, Task task) {
        if (StrUtil.isBlank(resultItems.get("id"))) {
            return;
        }
        String sqlPrifx = "INSERT IGNORE INTO yxgl (gameid,gamename,id,title,date,content) VALUES ";
        List<String> list = new ArrayList<>();
        list.add(resultItems.get("gameid"));
        list.add(DbUtils.paramFormat(resultItems.get("gamename").toString()));
        list.add(resultItems.get("id").toString());
        list.add(DbUtils.paramFormat(resultItems.get("title").toString()));
        list.add(resultItems.get("date").toString());
        list.add(DbUtils.paramFormat(resultItems.get("content").toString()));
        DbUtils.insert(sqlPrifx, list);
    }

}
