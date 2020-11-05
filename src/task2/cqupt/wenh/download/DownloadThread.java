package task2.cqupt.wenh.download;

import task2.cqupt.wenh.utils.DownUtils;
import task2.cqupt.wenh.utils.NetUtils;
import task2.cqupt.wenh.utils.PropertiesUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;

/**
 * @author LWenH
 * @create 2020/11/2 - 17:48
 * <p>
 * 下载任务的线程
 */
public class DownloadThread extends Thread {
    /**
     * 每个线程的id
     */
    private int id;
    /**
     * 每个线程下载的起点
     */
    private long start;
    /**
     * 每个线程下载的终点
     */
    private long end;
    /**
     * 创建完成下载任务所需的工具类的对象
     */
    private DownUtils downUtils = new DownUtils();
    private NetUtils netUtils = new NetUtils();

    public DownloadThread() {
    }

    public DownloadThread(int id, long start, long end) {
        this.id = id;
        this.start = start;
        this.end = end;
    }

    @Override
    public void run() {
        // 当前下载任务存在的线程数
        synchronized (this) {
            DownUtils.currentThreadCount++;
        }
        File downPositionFile = new File(
                PropertiesUtils.getDestPath() + "thread" + id + ".tmd");

        // 得到HttpURLConnection对象，开启网络连接
        HttpURLConnection huc = netUtils.getHuc();
        InputStream is = null;
        RandomAccessFile raf = null;
        try {
            if (downPositionFile.exists()) {
                /*
                    实现断点续传。如果文件下载到一半中断下载，则需要记录每一个线程目前下载到的位置，然后下一次接着这个位置下载。
                 */
                BufferedReader br = new BufferedReader(new FileReader(downPositionFile));
                String position = br.readLine();
                start = Integer.parseInt(position);
                br.close();
            }
            if (start >= end) {
                /*
                    当断点之后，每个线程的起始下载位置start会被设置为上次结束的位置+1(下载最后一段的线程的start会和end相等)，
                    再去设置range请求头，去连接网络资源就会导致错误（416）。
                    所以如果下载任务已经结束，则不需要再去请求网络资源。
                 */
                System.out.println(getName() + "已下载完毕！");
                synchronized (this) {
                    DownUtils.currentThreadCount--;
                    DownUtils.threadMap.put(id, end);
                }
            } else {
                huc.setRequestMethod("GET");
                huc.setConnectTimeout(10 * 1000);
                huc.setRequestProperty("range", "bytes=" + start + "-" + end);
                huc.connect();
                int code = huc.getResponseCode();
                if (code == 206) {
                    File file = new File(PropertiesUtils.getDestPath() + PropertiesUtils.getFileName());
                    is = huc.getInputStream();
                    raf = new RandomAccessFile(file, "rw");
                    raf.seek(start);
                    byte[] bys = new byte[1024];
                    int len = 0;
                    long total = 0;
                    System.out.println(getName() + "开始下载！");
                    while ((len = is.read(bys)) != - 1) {
                        raf.write(bys, 0, len);
                        total += len;
                        /*
                             每个线程的下载位置（用于断点续传以及计算下载速度等）
                         */
                        long position = start + total;
                        RandomAccessFile raf2 = new RandomAccessFile(downPositionFile, "rw");
                        raf2.write(String.valueOf(position).getBytes());
                        raf2.close();
                        // 记录每个线程的下载量
                        synchronized (this) {
                            DownUtils.threadMap.put(id, position);
                        }
                    }
                    System.out.println(getName() + "已下载完毕！");
                    synchronized (this) {
                        DownUtils.currentThreadCount--;
                        if (DownUtils.currentThreadCount == 0) {
                            for (int i = 0; i < downUtils.getThreadNumber(); i++) {
                                new File(PropertiesUtils.getDestPath() + "thread" + i + ".tmd").delete();
                            }
                            System.out.println("=======================");
                            System.out.println("下载任务已完成！");
                            System.out.println("=======================");
                        }
                    }
                } else {
                    System.out.println(getName() + "错误代码——error:" + code);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (raf != null) {
                try {
                    raf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
