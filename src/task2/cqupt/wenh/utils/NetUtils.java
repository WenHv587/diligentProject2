package task2.cqupt.wenh.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author LWenH
 * @create 2020/11/2 - 17:49
 * <p>
 * 对URL类中的一些方法进行简单的包装
 */
public class NetUtils {
    private HttpURLConnection huc;

    {
        try {
            URL url = new URL(PropertiesUtils.getUrlPath());
            huc = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回HttpURLConnection对象
     *
     * @return HttpURLConnection对象
     */
    public HttpURLConnection getHuc() {
        return huc;
    }

    /**
     * 获取要下载资源的长度
     *
     * @return 资源的长度
     */
    public long getLength() {
        return huc.getContentLength();
    }
}
