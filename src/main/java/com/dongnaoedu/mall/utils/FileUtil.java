package com.dongnaoedu.mall.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件工具类
 * 
 * 
 */
public class FileUtil {

	public static String base64Data(String data) {

		if (data == null || data.isEmpty()) {
			return "";
		}
		String base64 = data.substring(data.lastIndexOf(",") + 1);
		return base64;
	}

	/**
	 * 以UUID重命名
	 * 
	 * @param fileName
	 * @return
	 */
	public static String renamePic(String fileName) {
		String extName = fileName.substring(fileName.lastIndexOf("."));
		return UUID.randomUUID().toString().replace("-", "") + extName;
	}

	public static String isValidImage(HttpServletRequest request, MultipartFile file) {
		// 最大文件大小
		long maxSize = 5242880;
		// 定义允许上传的文件扩展名
		HashMap<String, String> extMap = new HashMap<String, String>();
		extMap.put("image", "gif,jpg,jpeg,png,bmp");

		if (!ServletFileUpload.isMultipartContent(request)) {
			return "请选择文件";
		}

		if (file.getSize() > maxSize) {
			return "上传文件大小超过5MB限制";
		}
		// 检查扩展名
		String fileName = file.getOriginalFilename();
		String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
		if (!Arrays.<String>asList(extMap.get("image").split(",")).contains(fileExt)) {
			return "上传文件扩展名是不允许的扩展名\n只允许" + extMap.get("image") + "格式";
		}

		return "valid";
	}

	public static String checkExt(String fileName, String dirName) {
		// 定义允许上传的文件扩展名
		HashMap<String, String> extMap = new HashMap<String, String>();
		extMap.put("image", "gif,jpg,jpeg,png,bmp");
		extMap.put("flash", "swf,flv");
		extMap.put("media", "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb");

		// 检查扩展名
		String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
		if (!Arrays.<String>asList(extMap.get(dirName).split(",")).contains(fileExt)) {
			return "上传文件扩展名是不允许的扩展名\n只允许" + extMap.get(dirName) + "格式";
		}
		return "valid";
	}

	public static void main(String[] args) {
		String b64 = base64Data("data:image/jpeg;base64,/9j/4AAQSkZJRgABAQEASABIAAD/2");
		System.out.println(b64);
	}
}
