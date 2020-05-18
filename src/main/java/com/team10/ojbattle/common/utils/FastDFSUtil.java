package com.team10.ojbattle.common.utils;


import com.team10.ojbattle.entity.FastDFSFile;
import org.csource.fastdfs.*;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author: 陈健航
 * @description: 文件管理
 * @since: 2020/3/21 19:59
 * @version: 1.0
 */
public class FastDFSUtil {

    static {
        try {
            //类路径资源加载器
            String filename = new ClassPathResource("fdfs_client.conf").getPath();
            //加载Tracker链接信息
            ClientGlobal.init(filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 用户封装信息成对象，然后传到该方法
     * @param fastDFSFile 上传对象
     * @return
     */
    public static String[] upload(FastDFSFile fastDFSFile) throws Exception {

        //获取TrackerServer
        TrackerServer trackerServer = getTrackerServer();

        //通过TrackerServer提供的链接信息获取Storage的链接信息，创建Storage对象存储Storage的链接信息
        StorageClient storageClient = new StorageClient(trackerServer, null);

        //通过StorageClient访问Storage,实现文件上传，并且获取文件上传后的信息,第三个参数是附加信息
        //1.文件上传组的名字 group1，2.文件存到Storage的名字，M00/02/44/cjh.jpg
        return storageClient.upload_file(fastDFSFile.getContent(), fastDFSFile.getExt(), null);
    }

    /**
     * 获取文件信息
     * @param group 文件组名
     * @param remoteFileName 文件储存路径信息
     */
    public static FileInfo getFile(String group, String remoteFileName) throws Exception {
        //获取TrackerServer
        TrackerServer trackerServer = getTrackerServer();

        //通过TrackerServer提供的链接信息获取Storage的链接信息，创建Storage对象存储Storage的链接信息
        StorageClient storageClient = new StorageClient(trackerServer, null);

        //获取文件信息
        return storageClient.get_file_info(group, remoteFileName);
    }

    /**
     * 文件下载
     * @param group 文件组名
     * @param remoteFileName 文件储存路径信息
     * @throws Exception 异常
     */
    public static InputStream downloadFile(String group, String remoteFileName) throws Exception {
        //获取TrackerServer
        TrackerServer trackerServer = getTrackerServer();

        //通过TrackerServer提供的链接信息获取Storage的链接信息，创建Storage对象存储Storage的链接信息
        StorageClient storageClient = new StorageClient(trackerServer, null);

        //文件下载
        byte[] downloadFile = storageClient.download_file(group, remoteFileName);

        //下载要配合InputStream，FileOutputStream, byte[] buffer实现
        return new ByteArrayInputStream(downloadFile);
    }

    /**
     * 文件删除
     * @param group 文件组名
     * @param remoteFileName 文件储存路径信息
     * @throws Exception 异常
     */
    public static void deleteFile(String group, String remoteFileName) throws Exception {
        //获取TrackerServer
        TrackerServer trackerServer = getTrackerServer();

        //通过TrackerServer提供的链接信息获取Storage的链接信息，创建Storage对象存储Storage的链接信息
        StorageClient storageClient = new StorageClient(trackerServer, null);

        //文件删除
        storageClient.delete_file(group, remoteFileName);
    }

    /**
     * 获取Storage信息
     * @return Storage信息
     * @throws Exception 异常
     */
    public static StorageServer getStorage() throws Exception {
        //获取TrackerServer
        TrackerServer trackerServer = getTrackerServer();

        TrackerClient trackerClient = new TrackerClient();

        //通过TrackerServer提供的链接信息获取Storage的链接信息，创建Storage对象存储Storage的链接信息
        return trackerClient.getStoreStorage(trackerServer);
    }


    /**
     * 获取Storage组的多台机器的IP和端口信息
     * @param group 文件组名
     * @param remoteFileName 文件储存路径信息
     * @return Storage的IP和端口信息
     * @throws Exception 异常
     */
    public static ServerInfo[] getServerInfo(String group, String remoteFileName) throws Exception {
        //获取TrackerServer
        TrackerServer trackerServer = getTrackerServer();

        TrackerClient trackerClient= new TrackerClient();

        //通过TrackerServer提供的链接信息获取Storage的链接信息，创建Storage对象存储Storage的链接信息
        return trackerClient.getFetchStorages(trackerServer, group, remoteFileName);
    }


    /**
     * 获取Tracker信息
     * @return Tracker访问地址
     * @throws Exception 异常
     */
    public static String getTrackerInfo() throws Exception {
        //获取TrackerServer
        TrackerServer trackerServer = getTrackerServer();

        //Trackerd的IP，端口
        int trackerHttpPort = ClientGlobal.getG_tracker_http_port();
        String ip = trackerServer.getInetSocketAddress().getHostString();

        return "http://" + ip + ":" + trackerHttpPort;
    }

    /**
     * 获取TrackerServer
     * @return 获取TrackerServer
     * @throws IOException 异常
     */
    public static TrackerServer getTrackerServer() throws IOException {
        //创建访问tracker的客户端
        TrackerClient trackerClient = new TrackerClient();

        //通过trackerClient访问Tracker服务，获取连接信息，信息在static代码块已经获取并配置
        return trackerClient.getConnection();
    }

}
