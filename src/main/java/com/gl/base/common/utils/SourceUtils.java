package com.gl.base.common.utils;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 获取配置文件信息
 * Created by gl on 2016/11/15.
 */
public class SourceUtils {
    private static Map<String, String> MESSAGE_MAP = null;
    private static Resource[] resources = null;
    private static Properties properties = new Properties();
    private static InputStream in = null;

    static {
        MESSAGE_MAP = new HashMap<String, String>();
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            resources = resolver.getResources("classpath*:*.properties");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取配置，key不存在抛出异常
     *
     * @param key
     * @return
     */
    public static String getMessage(String key) {
        // 通过code取得消息体。
        String ret = getSimpleMessage(key);

        if (StringUtils.isBlank(ret)) {
            // 如果取不到消息体，则抛出参数格式异常，内容为没有定义code[code]的消息。
            throw new IllegalArgumentException("没有定义code[" + key + "]的消息");
        }

        return ret;
    }

    /**
     * 获取配置信息
     *
     * @param key
     * @return
     */
    public static String getSimpleMessage(String key) {
        // 通过code取得消息体。
        String ret = MESSAGE_MAP.get(key);
        if (StringUtils.isBlank(ret)) {
            // 遍历当前被加载的消息体资源绑定的集合。
            try {
                for (int i = 0; i < resources.length; i++) {
                    in = resources[i].getInputStream();
                    properties.load(in);
                    ret = properties.getProperty(key);
                    if (StringUtils.isNotBlank(ret)) {
                        MESSAGE_MAP.put(key, ret);
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (StringUtils.isBlank(ret)) {
            ret = "";
        }

        return ret;
    }

    /**
     * 获取配置信息
     *
     * @param key
     * @param defaultVal 默认值
     * @return
     */
    public static String getSimpleMessage(String key, String defaultVal) {
        // 通过code取得消息体。
        String ret = MESSAGE_MAP.get(key);
        if (StringUtils.isBlank(ret)) {
            // 遍历当前被加载的消息体资源绑定的集合。
            try {
                for (int i = 0; i < resources.length; i++) {
                    in = resources[i].getInputStream();
                    properties.load(in);
                    ret = properties.getProperty(key);
                    if (StringUtils.isNotBlank(ret)) {
                        MESSAGE_MAP.put(key, ret);
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (StringUtils.isBlank(ret)) {
            ret = defaultVal;
        }

        return ret;
    }
}
