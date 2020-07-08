package com.dongnaoedu.mall.common.uploadfile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件存取访问工具类
 * <p>
 * 提供一个开关，选择是使用分布式存储，还是使用本地服务存储。
 * </p>
 *
 */
@Component
public class FileAccessUtils {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Value("${mall.file.access.type:2}")
	private int accessType = 2;
	
	@Value("${mall.file.upload.directory:/upload}")
	private String fileDirectoryName = "/upload";
	
	private String uploadfilePath;
	
	/**
	 * 以MultipartFile对象形式存储文件
	 * @param multipartFile
	 * @return
	 * @throws IOException
	 */
	public String saveFile(MultipartFile multipartFile) throws IOException {
		String fileName = multipartFile.getOriginalFilename();
		int extIndex = fileName.lastIndexOf(".");
		String ext = fileName.substring(extIndex + 1);
		
		byte[] file_buff = null;
		InputStream inputStream = multipartFile.getInputStream();
		if (inputStream != null) {
			int len1 = inputStream.available();
			file_buff = new byte[len1];
			inputStream.read(file_buff);
			inputStream.close();
		}else {
			throw new NullPointerException("file stream can not be null");
		}
		
		String filePath = null;
		switch (accessType) {
		case 2:		// 分布式文件，fastdfs
			filePath = save2FastdfsFile(fileName, file_buff, ext);
			break;
		case 1:		// 本地存储
		default:
			filePath = save2LocalFile(fileName, file_buff, ext);
			break;
		}
		return filePath;
	}
	
	/**
	 * 以二进制数据形式进程存储文件
	 * @param fileData
	 * @param fileName
	 * @param ext
	 * @return
	 */
	public String saveFile(byte[] fileData, String fileName, String ext) {
		String filePath = null;
		switch (accessType) {
		case 2:		// 分布式文件，fastdfs
			filePath = save2FastdfsFile(fileName, fileData, ext);
			break;
		case 1:		// 本地存储
		default:
			filePath = save2LocalFile(fileName, fileData, ext);
			break;
		}
		return filePath;
	}
	
	
	
	
	/**
	 * 保存文件到本地服务器
	 * @param fileName
	 * @param fileBuff
	 * @param ext
	 * @return
	 */
	private String save2LocalFile(String fileName, byte[] fileBuff, String ext) {
		File uploadDirectory = new File(uploadfilePath);
	    if (!uploadDirectory.exists()) {
	        uploadDirectory.mkdirs();
	    }
	    
	    int extIndex = fileName.lastIndexOf(".");
	    if(extIndex > -1) {
	    	fileName = fileName.substring(0, extIndex);
	    }
		
	    OutputStream os = null;
	    String filePath = uploadDirectory.getPath() + File.separator + fileName+"."+ext;
		try {
			os = new FileOutputStream(filePath);
			os.write(fileBuff);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return filePath;
	}
	
	/**
	 * 保存文件到fastdfs分布式文件系统
	 * @param fileName
	 * @param fileBuff
	 * @param ext
	 * @return
	 */
	private String save2FastdfsFile(String fileName, byte[] fileBuff, String ext) {
		String[] fileAbsolutePath = null;
		FastDFSFile file = new FastDFSFile(fileName, fileBuff, ext);
		try {
			fileAbsolutePath = FastDFSClient.upload(file); // upload to fastdfs
		} catch (Exception e) {
			log.error("upload file Exception!", e);
		}
		if (fileAbsolutePath == null) {
			log.error("upload file failed,please upload again!");
		}
		// String path = FastDFSClient.getTrackerUrl() + fileAbsolutePath[0] + "/" + fileAbsolutePath[1];
		String path = fileAbsolutePath[0] + "/" + fileAbsolutePath[1];
		return path;
	}

	public String init(String path) {
		uploadfilePath = path+fileDirectoryName;
		if(2 == accessType) {
			return FileManagerUtils.getFilePath(null);
		}
		return "";
	}
}
