package com.dongnaoedu.mall.service.impl;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import com.dongnaoedu.mall.common.exception.MallException;
import com.dongnaoedu.mall.common.jedis.JedisClient;
import com.dongnaoedu.mall.common.uploadfile.FastDFSClient;
import com.dongnaoedu.mall.common.uploadfile.FastDFSFile;
import com.dongnaoedu.mall.mapper.TbMemberMapper;
import com.dongnaoedu.mall.pojo.TbMember;
import com.dongnaoedu.mall.pojo.front.Member;
import com.dongnaoedu.mall.service.LoginService;
import com.dongnaoedu.mall.service.MemberFrontService;
import com.dongnaoedu.mall.utils.FileUtil;
import com.google.gson.Gson;

/**
 * 
 */
@Service
public class MemberFrontServiceImpl implements MemberFrontService {
	private static final Logger log = LoggerFactory.getLogger(MemberFrontServiceImpl.class);
	
    @Autowired
    private LoginService loginService;
    @Autowired
    private TbMemberMapper tbMemberMapper;
    @Autowired
    private JedisClient jedisClient;
    @Value("${SESSION_EXPIRE}")
    private Integer SESSION_EXPIRE;

    @Override
    public String imageUpload(Long userId,String token,String imgData) {

		// 过滤data:URL
		String base64 = FileUtil.base64Data(imgData);
		byte[] bs = Base64Utils.decodeFromString(base64);
		
		FastDFSFile file = new FastDFSFile(UUID.randomUUID() + "", bs, "png");
		String[] fileAbsolutePath = FastDFSClient.upload(file);
		
		if (fileAbsolutePath == null) {
			log.error("upload file failed,please upload again!");
			throw new MallException("上传头像失败");
		}
		String imgPath = FastDFSClient.getTrackerUrl() + fileAbsolutePath[0] + "/" + fileAbsolutePath[1];

		TbMember tbMember = tbMemberMapper.selectByPrimaryKey(userId);
		if (tbMember == null) {
			throw new MallException("通过id获取用户失败");
		}
		tbMember.setFile(imgPath);
		if (tbMemberMapper.updateByPrimaryKey(tbMember) != 1) {
			throw new MallException("更新用户头像失败");
		}

		// 更新缓存
		Member member = loginService.getUserByToken(token);
		member.setFile(imgPath);
		jedisClient.set("SESSION:" + token, new Gson().toJson(member));
		return imgPath;
    }
}
