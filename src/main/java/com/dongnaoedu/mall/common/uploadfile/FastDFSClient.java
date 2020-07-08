package com.dongnaoedu.mall.common.uploadfile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.FileInfo;
import org.csource.fastdfs.ServerInfo;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * fastdfs客户端，包含常用的上传、下载、获取文件信息、删除等方法
 * 
 * 
 *
 */
public class FastDFSClient {
	private static org.slf4j.Logger logger = LoggerFactory.getLogger(FastDFSClient.class);

	// 首先在类加载的时候读取相应的配置信息，并进行初始化
	static {
		try {
			String filePath = new ClassPathResource("fdfs_client.conf").getFile().getAbsolutePath();
			ClientGlobal.init(filePath);
		} catch (Exception e) {
			logger.error("FastDFS Client Init Fail!", e);
		}
	}

	/**
	 * 文件上传
	 * 
	 * @param file
	 * @return
	 */
	public static String[] upload(FastDFSFile file) {
		logger.info("File Name: " + file.getName() + "File Length:" + file.getContent().length);
		
		NameValuePair[] meta_list = new NameValuePair[1];
		meta_list[0] = new NameValuePair("author", file.getAuthor());
		
		long startTime = System.currentTimeMillis();
		String[] uploadResults = null;
		StorageClient storageClient = null;
		try {
			storageClient = getTrackerClient();
			uploadResults = storageClient.upload_file(file.getContent(), file.getExt(), meta_list);
		} catch (IOException e) {
			logger.error("IO Exception when uploadind the file:" + file.getName(), e);
		} catch (Exception e) {
			logger.error("Non IO Exception when uploadind the file:" + file.getName(), e);
		}
		logger.info("upload_file time used:" + (System.currentTimeMillis() - startTime) + " ms");
		
		if (uploadResults == null && storageClient != null) {
			logger.error("upload file fail, error code:" + storageClient.getErrorCode());
		}
		String groupName = uploadResults[0];
		String remoteFileName = uploadResults[1];
		
		logger.info("文件上传成功！" + "group_name:" + groupName + ", remoteFileName:" + " " + remoteFileName);
		return uploadResults;
	}

	/**
	 * 根据groupName和文件名获取文件信息
	 * 
	 * @param groupName
	 * @param remoteFileName
	 * @return
	 */
	public static FileInfo getFile(String groupName, String remoteFileName) {
		try {
			StorageClient storageClient = getTrackerClient();
			return storageClient.get_file_info(groupName, remoteFileName);
		} catch (IOException e) {
			logger.error("IO Exception: Get File from Fast DFS failed", e);
		} catch (Exception e) {
			logger.error("Non IO Exception: Get File from Fast DFS failed", e);
		}
		return null;
	}

	/**
	 * 文件下载
	 * 
	 * @param groupName
	 * @param remoteFileName
	 * @return
	 */
	public static InputStream downFile(String groupName, String remoteFileName) {
		try {
			StorageClient storageClient = getTrackerClient();
			byte[] fileByte = storageClient.download_file(groupName, remoteFileName);
			InputStream ins = new ByteArrayInputStream(fileByte);
			return ins;
		} catch (IOException e) {
			logger.error("IO Exception: Get File from Fast DFS failed", e);
		} catch (Exception e) {
			logger.error("Non IO Exception: Get File from Fast DFS failed", e);
		}
		return null;
	}

	/**
	 * 删除文件
	 * 
	 * @param groupName
	 * @param remoteFileName
	 * @throws Exception
	 */
	public static void deleteFile(String groupName, String remoteFileName) throws Exception {
		StorageClient storageClient = getTrackerClient();
		int i = storageClient.delete_file(groupName, remoteFileName);
		logger.info("delete file successfully!!!" + i);
	}

	public static StorageServer[] getStoreStorages(String groupName) throws IOException {
		TrackerClient trackerClient = new TrackerClient();
		TrackerServer trackerServer = trackerClient.getConnection();
		return trackerClient.getStoreStorages(trackerServer, groupName);
	}

	public static ServerInfo[] getFetchStorages(String groupName, String remoteFileName) throws IOException {
		TrackerClient trackerClient = new TrackerClient();
		TrackerServer trackerServer = trackerClient.getConnection();
		return trackerClient.getFetchStorages(trackerServer, groupName, remoteFileName);
	}

	public static String getTrackerUrl() {
		try {
			return "http://" + getTrackerServer().getInetSocketAddress().getHostString() + ":"
					+ ClientGlobal.getG_tracker_http_port() + "/";
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	private static StorageClient getTrackerClient() throws IOException {
		TrackerServer trackerServer = getTrackerServer();
		StorageClient storageClient = new StorageClient(trackerServer, null);
		return storageClient;
	}

	private static TrackerServer getTrackerServer() throws IOException {
		TrackerClient trackerClient = new TrackerClient();
		TrackerServer trackerServer = trackerClient.getConnection();
		return trackerServer;
	}
	
	public static void main(String[] args) {
		try {
			File fileData = new File("F:/a.txt");
			FileInputStream fis = new FileInputStream(fileData);
			byte[] file_buff = new byte[fis.available()];
			fis.read(file_buff);
			FastDFSFile file = new FastDFSFile("a", file_buff, "txt");
			String[] fileAbsolutePath = FastDFSClient.upload(file);
			String path = FastDFSClient.getTrackerUrl() + fileAbsolutePath[0] + "/" + fileAbsolutePath[1];
			System.out.println(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}