# diligentProject2

- 这个仓库是用来提交作业的仓库

## 多线程下载
- 使用多线程的方式对网络资源进行下载，除了实现了基本的下载功能以外，还实现了：
  - 断点续传
  - 计算下载速度，进度以及估算下载完成所需时间
- 目录介绍
  - download
    - DownloadThread.java 下载的线程，真正进行下载任务的类。
    - SpeedThread.java 计算下载速度、进度以及估算剩余时间线程。
  - utils
    - DownUtils.java 完成下载任务所需要的工具类。
    - NetUtils.java 处理网络资源的类，对一些方法做了一些简单的包装，方便调用。
    - PropertiesUtils.java 加载配置文件的类。
  - test
    - Test.java 测试类。

- 除了具体功能以外，在src下，还有
  - download.properties 设置网络资源地址和本地的下载地址的配置文件。
    
    - 这里要注意的是在设置本地的下载路径时，需要在选择好目录之后，在后面再加上文件路径的分隔符“\\\”。
  - download.txt 一些用于测试的网络资源的下载链接。



---

- 由于测试需要在联网的环境下进行，还需要修改配置文件，可能不是很方便，在result文件夹下的result.md中，我对程序的运行过程进行了截图记录。其中也对进度、速度等的计算和断点续传进行了测试。如果学长不方便测试的话，可以看我的运行截图记录:-)
---



## 总结

- 此次任务完成的不是很满意，感觉代码写的有一些乱，有很多想法也没写出来。在写代码的时候遇到了一些问题，比如断点续传，当中断下载时如果出现某些线程已经完成了自己的下载任务，而另一些线程没有完成的情况，就会有很多需要注意的细节（都是踩过的坑）。
- 对于速度的问题，其实感觉并没有快很多，测试了很多次，在网速相同的情况下，还是多线程略快一点，但是并不明显（有时候还不如直接下载）。我猜测可能是因为我线程数量设置的问题，我设置开启线程的数量是3，不知道最优的数量是多少。后来我看了网上的一些博客，自己也想了一下，感觉更适合用线程池来写，为每个线程设置固定的任务，比如设置下载3m大小，然后设置连接超时，避免惰性任务，但是写起来感觉有一些难度，后期想要尝试重写一个使用线程池下载的版本。
- 其实写完之后比较想加入一个暂停和继续下载的功能，但是没有任何界面，不知道怎么加合适。我自己的想法是：可以改进一下，写一个带图形化界面版本的，使用上线程池，再优化一下，做一些校验，我自己就算满意了。
- 总的来说我个人多线程平时用的确实不多，学过去之后很久都没有用过，但是感觉对于Java后端多线程非常重要，后面要好好学。

