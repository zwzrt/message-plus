package cn.redcoral.messageplus.utils;

import cn.hutool.core.lang.Snowflake;

/**
 * 生成雪花ID
 * @author mo
 **/
public class SnowflakeIDUtil {
    /**
     * 用于生成全局唯一ID的雪花算法实例
     */
    protected static Snowflake snowflake = new Snowflake(3, 11);
    protected static Snowflake groupSnowflake = new Snowflake(3, 1);


    public static String getID() {
        return snowflake.nextIdStr();
    }

    public static String getGroupID() {
        return groupSnowflake.nextIdStr();
    }
}
