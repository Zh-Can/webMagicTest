package utils;

import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.Setting;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class DbUtils {

    /**
     * 数据库配置
     */
    private static final Setting se = new Setting("db.setting");

    /**
     * 批量插入
     * @param sqlPrefix sql前缀
     *                  如：INSERT INTO test (id,user,name,sex,pass,regist_date,remark) VALUES 。
     * @param list 数据list
     *             如：('1','abc','ABC','男','123','2023-08-08','lalala')
     */
    public static void batchinsert(String sqlPrefix, List<String> list) {

        try {
            Connection connection = DriverManager.getConnection(se.get("url"), se.get("user"), se.get("pass"));
            String sqlTemp = sqlPrefix + format(sqlPrefix);
            PreparedStatement pst = connection.prepareStatement(sqlTemp);
            StrBuilder sb = StrUtil.strBuilder();
            sb.append(sqlPrefix);
            for (String s : list) {
                sb.append(s).append(",");
            }
            String sql = sb.toString();
            if (sql.endsWith(",")) sql = sql.substring( 0 , sql.length() - 1 );
            pst.addBatch(sql);
            pst.executeBatch();
            pst.clearBatch();
            // 提交事务
            connection.commit();
            // 清空上一次添加的数据
            sql = null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 插入单条
     * @param sqlPrifx  sql前缀 INSERT INTO test (gameid,gamename,id,title,date,content) VALUES
     * @param data 数据list
     */
    public static void insert(String sqlPrifx, List<String> data) {
        try {
            Connection connection = DriverManager.getConnection(se.get("url"), se.get("user"), se.get("pass"));
            String sqlTemp = format(sqlPrifx);
            PreparedStatement pst = connection.prepareStatement(sqlPrifx + sqlTemp);
            for (int i = 0; i < data.size(); i++) {
                pst.setString(i+1, data.get(i));
            }
            pst.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 格式化成 (?,?,?)
     * @param str INSERT INTO test (gameid,gamename,id,title,date,content) VALUES
     * @return (?,?,?)
     */
    private static String format(String str) {
        StrBuilder ret = StrUtil.strBuilder();
        String s = ReUtil.get("(\\(.*?\\))", str, 1);
        int length = s.split(",").length;
        for (int i = 0; i < length; i++) {
            if (i == 0) {
                ret.append("(?,");
            } else if (i == length - 1) {
                ret.append("?)");
            } else {
                ret.append("?,");
            }
        }
        if (ret.toString().endsWith(",")) return ret.toString().replace(",",")");
        return ret.toString();
    }

    /**
     * 防止插入失败
     * @param param 参数
     * @return 参数
     */
    public static String paramFormat(String param) {
        param = param.replace("'", "\'");
        param = param.replace('"','\"');
        param = param.replace("\\", "\\\\");
        return param;
    }

}
