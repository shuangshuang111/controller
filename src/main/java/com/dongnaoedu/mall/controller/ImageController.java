package com.dongnaoedu.mall.controller;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dongnaoedu.mall.common.exception.MallUploadException;
import com.dongnaoedu.mall.common.uploadfile.FastDFSClient;
import com.dongnaoedu.mall.common.uploadfile.FastDFSFile;
import com.dongnaoedu.mall.pojo.common.KindEditorResult;
import com.dongnaoedu.mall.pojo.common.Result;
import com.dongnaoedu.mall.utils.ResultUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 
 */
@RestController
@Api("图片上传统一接口")
public class ImageController {
	private static final Logger log = LoggerFactory.getLogger(ImageController.class);

	@RequestMapping(value = "/image/imageUpload", method = RequestMethod.POST)
	@ApiOperation(value = "WebUploader图片上传")
	public Result<Object> uploadFile(@RequestParam("file") MultipartFile files, HttpServletRequest request) {

		String imagePath = null;
		if (files.isEmpty()) {
			throw new MallUploadException("上次的文件不能为空");
		}
		try {
			imagePath = saveFile(files);
		} catch (Exception e) {
			log.error("文件上次失败");
		}
		
		return new ResultUtil<Object>().setData(imagePath);
	}

	@RequestMapping(value = "/kindeditor/imageUpload", method = RequestMethod.POST)
	@ApiOperation(value = "KindEditor图片上传")
	public KindEditorResult kindeditor(@RequestParam("imgFile") MultipartFile files, HttpServletRequest request) {

		KindEditorResult kindEditorResult = new KindEditorResult();
		
		if (files.isEmpty()) {
			kindEditorResult.setError(1);
			kindEditorResult.setMessage("上传文件不能为空");
			return kindEditorResult;
		}
		// 转存文件
		try {
			String imagePath = saveFile(files);
			kindEditorResult.setError(0);
			kindEditorResult.setUrl(imagePath);
			return kindEditorResult;
		} catch (IOException e) {
			e.printStackTrace();
		}
		kindEditorResult.setError(1);
		kindEditorResult.setMessage("上传失败");
		return kindEditorResult;
	}
	
	/**
	 * @param multipartFile
	 * @return
	 * @throws IOException
	 */
	public String saveFile(MultipartFile multipartFile) throws IOException {
		String fileName=multipartFile.getOriginalFilename();
		String[] fileAbsolutePath=new String[]{};
		String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
		byte[] file_buff = null;
		InputStream inputStream = multipartFile.getInputStream();
		if (inputStream != null) {
			int len1 = inputStream.available();
			file_buff = new byte[len1];
			inputStream.read(file_buff);
		}
		inputStream.close();
		FastDFSFile file = new FastDFSFile(fileName, file_buff, ext);
		try {
			fileAbsolutePath = FastDFSClient.upload(file); // upload to fastdfs
		} catch (Exception e) {
			log.error("upload file Exception!", e);
		}
		if (fileAbsolutePath == null) {
			log.error("upload file failed,please upload again!");
		}
		String path = FastDFSClient.getTrackerUrl() + fileAbsolutePath[0] + "/" + fileAbsolutePath[1];
		//String path = fileAbsolutePath[0] + "/" + fileAbsolutePath[1];
		return path;
	}
}
