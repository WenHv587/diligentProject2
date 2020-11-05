package task2.cqupt.wenh.download;

import task2.cqupt.wenh.utils.DownUtils;
import task2.cqupt.wenh.utils.NetUtils;

/**
 * @author LWenH
 * @create 2020/11/2 - 17:54
 * <p>
 * 计算下载速度，进度以及预估下载时间的线程
 */
public class SpeedThread extends Thread {
    @Override
    public void run() {
        // 此线程运行之初，得到上一次下载位置
        long before = DownUtils.getPosition();
        // 获得目标下载资源的总大小
        long length = new NetUtils().getLength();
        while (DownUtils.currentThreadCount > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 文件的下载位置
            long total = DownUtils.getPosition();
            // 计算一秒钟内文件下载的总量
            long sec = total - before;
            // 计算下载速度
            double speed = sec / 1000.0;
            /*
                计算目前的完成度，用百分比表示
                由于total是三个线程的下载位置，需要用total减去文件长度，才是下载量
             */
            // 当前下载量
            long sum = total - length;
            // 计算目前下载的完成度
            double percent = ((double) sum / length) * 100;
            if (sec != 0) {
                // 估算剩余下载任务所需的时间
                int lastTime = (int) ((length - sum) / sec);
                System.out.println("当前下载速度：" + String.format("%.2f", speed) +
                        "kb/s---完成度：" + String.format("%.2f", percent) +
                        "%---预计所需时间：" + lastTime + "s");
            }
            before = total;
        }
    }
}
