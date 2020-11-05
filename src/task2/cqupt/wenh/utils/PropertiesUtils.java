package task2.cqupt.wenh.utils;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * @author LWenH
 * @create 2020/11/2 - 18:24
 * <p>
 * 加载配置文件的工具类
 */
public class PropertiesUtils {
    private static Properties properties;

    static {
        properties = new Properties();
        try {
            properties.load(new FileReader("src\\download.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取网络资源的下载地址
     *
     * @return 返回资源的下载地址
     */
    static String getUrlPath() {
        return properties.getProperty("urlPath");
    }

    /**
     * 获取用户下载时的保存路径
     *
     * @return 返回用户保存的目录（此目录不包括文件名）
     */
    public static String getDestPath() {
        return properties.getProperty("destPath");
    }

    /**
     * 获取网络资源默认的文件名（一般是资源链接的最后一部分）
     *
     * @return 返回文件名
     */
    public static String getFileName() {
        String urlPath = getUrlPath();
        assert urlPath != null;
        int index = urlPath.lastIndexOf('/');
        return urlPath.substring(index + 1);
    }
}
