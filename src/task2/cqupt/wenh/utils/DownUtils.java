package task2.cqupt.wenh.utils;

import task2.cqupt.wenh.download.DownloadThread;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author LWenH
 * @create 2020/11/2 - 17:49
 * <p>
 * 下载需要用到的工具类
 *
 * <p>给出了下载必须的控制条件，同时开启下载任务</p>
 */
public class DownUtils {
    /**
     * 要下载网络资源的大小
     */
    private long length = new NetUtils().getLength();
    /**
     * 下载任务开始时开启线程的数量（默认为3）
     */
    private int threadNumber = 3;
    /**
     * 当前正在执行任务的线程数量
     *
     * <p>
     * 有可能出现某些线程更快执行完成的情况，
     * 因为断点续传的功能需要用文件保存下载进度，
     * 如果每个线程执行完毕后就删除记录文件，就
     * 有可能出现先下载完成的线程删除掉了它自己
     * 的记录文件，再次断点续传的时候就会因为没
     * 有下载记录造成重复下载
     *
     * 解决方案：创建一个当前正在执行任务的线程数量
     * 变量，当这个变量为0，也就是所有任务
     * 全部都执行完毕了以后，统一删除所有
     * 的记录文件
     * </p>
     */
    public static int currentThreadCount;
    /**
     * 记录线程的下载进度（用于计算下载速度和下载进度）
     */
    public static Map<Integer, Long> threadMap = new HashMap<>();

    public int getThreadNumber() {
        return threadNumber;
    }

    /**
     * 开启下载任务
     */
    public void download() {
        // 每个线程需要下载文件的大小
        long size = length / threadNumber;
        long start;
        long end;
        for (int i = 0; i < threadNumber; i++) {
            start = i * size;
            end = (i + 1) * size - 1;
            if (i == threadNumber - 1) {
                end = length;
            }
            // 开启线程进行下载
            new DownloadThread(i, start, end).start();
        }
    }

    /**
     * 得到三个线程的下载位置总和
     *
     * @return total：下载位置总和
     */
    public static long getPosition() {
        Set<Integer> idSet = threadMap.keySet();
        long position;
        long total = 0;
        for (Integer id : idSet) {
            // 每个线程的下载位置
            position = threadMap.get(id);
            // 多个线程的下载位置总和
            total += position;
        }
        return total;
    }
}

