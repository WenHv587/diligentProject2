package task2.cqupt.wenh.test;

import task2.cqupt.wenh.download.SpeedThread;
import task2.cqupt.wenh.utils.DownUtils;

/**
 * @author LWenH
 * @create 2020/11/2 - 17:48
 * <p>
 * 功能测试
 */
public class Test {
    public static void main(String[] args) {
        DownUtils downUtils = new DownUtils();
        downUtils.download();

        // 计算速度的线程
        SpeedThread st = new SpeedThread();
        st.setDaemon(true);
        st.start();
    }
}
