package com.dongnaoedu.mall.common.uploadfile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.ProtoCommon;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * Copyright dayhr2014-2015 All rights reserved
 * 
 * @ClassName: FileManagerUtils
 * @Description: 操作文档存储服务器中的文件
 * 
 * @date 2015-1-20 下午4:56:05
 */

public class FileManagerUtils {

	private static Logger log = LoggerFactory.getLogger(FileManagerUtils.class);
	private static String HEADER = "FileManagerUtils:------------";

	static { // Initialize Fast DFS Client configurations

		try {
			// 初始化操作,配置文件中可以指定多个tracker
			String filePath = new ClassPathResource("fdfs_client.conf").getFile().getAbsolutePath();
			ClientGlobal.init(filePath);

		} catch (Exception e) {
			log.error(HEADER + "Exception" + e);

		}
	}

	/**
	 * 以字节数据流的方式上传文件
	 * 
	 * @param inStream
	 * @param uploadFileName
	 * @return
	 */
	public static String uploadFile(InputStream inStream, String uploadFileName) {

		String fileId = "";
		try {
			byte[] fileBuff = readStream(inStream);
			String fileExtName = "";
			if (uploadFileName.contains(".")) {
				fileExtName = uploadFileName.substring(uploadFileName.lastIndexOf(".") + 1);
			} else {
				log.warn("Fail to upload file, because the format of filename is illegal.");
				return fileId;
			}
			// 建立连接
			StorageClient1 client = getTrackerClient();

			// 设置元信息
			/*
			 * NameValuePair[] metaList = new NameValuePair[3]; 
			 * metaList[0] = new NameValuePair("fileName", uploadFileName); 
			 * metaList[1] = new NameValuePair("fileExtName", fileExtName); 
			 * metaList[2] = new NameValuePair("fileLength", String.valueOf(fileLength));
			 * client.upload_file1(fileBuff, fileExtName.toLowerCase(), metaList);
			 */
			

			// 上传文件
			try {
				fileId = client.upload_file1(fileBuff, fileExtName.toLowerCase(), null);
				fileBuff = null;
			} catch (Exception e) {
				log.warn("Upload file \"" + uploadFileName + "\"fails");
			}
		} catch (Exception e) {
			log.error(HEADER + "======uploadFile error=======" + e);
		}
		return fileId;
	}

	/**
	 * 以字节数组的方式上传文件
	 * 
	 * @param fileBuff
	 * @param uploadFileName
	 * @return
	 */
	public static String uploadFile(byte[] fileBuff, String uploadFileName) {
		String fileId = "";
		try {
			String fileExtName = "";
			if (uploadFileName.contains(".")) {
				fileExtName = uploadFileName.substring(uploadFileName.lastIndexOf(".") + 1);
			} else {
				log.warn("Fail to upload file, because the format of filename is illegal.");
				return fileId;
			}
			// 建立连接
			StorageClient1 client = getTrackerClient();

			// 上传文件
			try {
				fileId = client.upload_file1(fileBuff, fileExtName.toLowerCase(), null);
				fileBuff = null;
			} catch (Exception e) {
				log.warn("Upload file \"" + uploadFileName + "\"fails");
			}
		} catch (Exception e) {
			log.error(HEADER + "======uploadFile error=======" + e);
		}
		return fileId;
	}

	/**
	 * 读取的方法
	 * 
	 * @param inStream
	 * @return
	 * @throws Exception
	 */
	public static byte[] readStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outstream = new ByteArrayOutputStream();
		byte[] buffer = new byte[256 * 1024]; // 用于缓存每次读取的数据
		int len = -1;
		while ((len = inStream.read(buffer)) != -1) {
			outstream.write(buffer, 0, len);
		}
		// 关闭流一定要记得。
		outstream.close();
		inStream.close();
		return outstream.toByteArray();
	}

	/**
	 * 删除文件 return 0 for success, none zero for fail (error code)
	 * 
	 * @param file_id
	 * @return
	 */
	public static int deleteFile(String file_id) {
		if (StringUtils.isEmpty(file_id))
			return 0;
		int i = 0;
		try {
			// 建立连接
			StorageClient1 client = getTrackerClient();
			i = client.delete_file1(file_id);
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error(HEADER + "======deleteFile error=======" + ex);
		}
		return i;
	}

	/**
	 * 以字节数据流的方式下载文件
	 * 
	 * @param file_id
	 * @return
	 */
	public static byte[] downloadFile(String file_id) {

		if (StringUtils.isEmpty(file_id))
			return null;
		byte[] file_buff = null;
		;
		try {
			// 建立连接
			StorageClient1 client = getTrackerClient();

			file_buff = client.download_file1(file_id);

		} catch (Exception ex) {
			ex.printStackTrace();
			log.error(HEADER + "======downloadFile error=======" + ex);
		}

		return file_buff;
	}

	/**
	 * 以附件的方式下载文件
	 * 
	 * @param request
	 * @param response
	 * @param file_id
	 * @param contentType
	 * @param realName
	 * @throws Exception
	 */
	public static void download(HttpServletRequest request, HttpServletResponse response, String file_id,
			String contentType, String realName) throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");

		byte[] file_buff = downloadFile(file_id);

		response.setContentType(contentType);
		response.setHeader("Content-disposition",
				"attachment; filename=\"" + new String(realName.getBytes("utf-8"), "ISO8859-1") + "\"");
		response.setHeader("Content-Length", String.valueOf(file_buff));

		OutputStream out = response.getOutputStream();
		out.write(file_buff);
		out.flush();
		out.close();
	}

	/**
	 * 获得图片的URL路径，由于图片服务器可以有2个tracker,当一个挂掉后，以下代码可以自动获取另一个的连接，保证系统正常运行
	 * 例如：http://192.168.1.211:86/group1/M00/00/00/wKgB0lTR72WAJFfGAAF_5TzBJ10067.jpg
	 * 
	 * @param file_id
	 * @return
	 */
	public static String getFilePath(String file_id) {
		String file_url = null;
		try {
			// 建立连接
			InetSocketAddress inetSockAddr;

			TrackerServer trackerServer = getTrackerServer();
			inetSockAddr = trackerServer.getInetSocketAddress();
			file_url = "http://" + inetSockAddr.getAddress().getHostName();
			int ts;
			String token;
//			if (ClientGlobal.g_tracker_http_port != 80) {
//				file_url += ":" + ClientGlobal.g_tracker_http_port;
//			}
			file_url += "/";
			if (file_id != null) {
				file_url += file_id;
			}
			// 如果有防盗链，则
			if (ClientGlobal.g_anti_steal_token) {
				ts = (int) (System.currentTimeMillis() / 1000);
				String appender_file_idForToken = file_id.substring(file_id.indexOf("/") + 1);
				token = ProtoCommon.getToken(appender_file_idForToken, ts, ClientGlobal.g_secret_key);
				file_url += "?token=" + token + "&ts=" + ts;
			}
			log.info(HEADER + "======getFilePath =======" + file_url);
			trackerServer.close();

		} catch (Exception ex) {
			ex.printStackTrace();
			log.error(HEADER + "======getFilePath error=======" + ex);
		}

		return file_url;
	}
	
	private static StorageClient1 getTrackerClient() throws IOException {
		TrackerServer trackerServer = getTrackerServer();
		StorageClient1 storageClient = new StorageClient1(trackerServer, null);
		return storageClient;
	}
	
	private static TrackerServer getTrackerServer() throws IOException {
		TrackerClient trackerClient = new TrackerClient();
		TrackerServer trackerServer = trackerClient.getConnection();
		return trackerServer;
	}

}
