package cn.tgxy.tgbase.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.CredentialsProvider;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.common.comm.SignVersion;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyuncs.exceptions.ClientException;

import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import cn.tgxy.tgbase.service.BsFileService;
import jakarta.annotation.PostConstruct;

@Service("fileServiceAliOssImpl")
@ConditionalOnProperty(name = "system.base.aliOss.enable", havingValue = "true")
public class BsFileServiceAliOssImpl implements BsFileService {

	private OSS ossClient = null;

	private static Logger logger = LoggerFactory.getLogger(BsFileServiceAliOssImpl.class);
	
	@Value("${system.base.aliOss.accessKeyId}")
	private String accessKeyId;
	
	@Value("${system.base.aliOss.accessKeySecret}")
	private String accessKeySecret;
	
	@Value("${system.base.aliOss.endpoint}")
	private String endpoint;
	
	@Value("${system.base.aliOss.region}")
	private String region;

	@PostConstruct
	public void init() throws ClientException {
		// 从环境变量中获取访问凭证。运行本代码示例之前，请先配置环境变量
//		EnvironmentVariableCredentialsProvider credentialsProvider = CredentialsProviderFactory
//				.newEnvironmentVariableCredentialsProvider();
		
		// 使用DefaultCredentialProvider方法直接设置AK和SK
        CredentialsProvider credentialsProvider = new DefaultCredentialProvider(accessKeyId, accessKeySecret);


		ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
		// 显式声明使用 V4 签名算法
		clientBuilderConfiguration.setSignatureVersion(SignVersion.V4);
		
		clientBuilderConfiguration.setCrcCheckEnabled(false);

		ossClient = OSSClientBuilder.create().endpoint(endpoint).credentialsProvider(credentialsProvider)
				.clientConfiguration(clientBuilderConfiguration).region(region).build();
	}

	@Override
	public String autoRenameUploadFile(String bucket, MultipartFile file, String uploadFolder) throws Exception {

		if (StrUtil.isBlank(bucket) || file == null || file.isEmpty()) {
			return null;
		}

		Calendar calendar = Calendar.getInstance();
		StringBuilder sb = new StringBuilder();
		sb.append("/").append(calendar.get(Calendar.YEAR)).append("/").append(calendar.get(Calendar.MONTH) + 1)
				.append("/").append(calendar.get(Calendar.DATE));
		String uploadFolderPath = uploadFolder + sb.toString();

		String originalName = file.getOriginalFilename();
		String ext = FileNameUtil.getSuffix(originalName);
		if (StrUtil.isNotBlank(ext)) {
			ext = "." + ext;
		}
		String renameFileName = UUID.randomUUID().toString() + ext;

		return uploadFile(bucket, file, uploadFolderPath, renameFileName);
	}

	public String uploadFile(String bucket, MultipartFile file, String uploadFolderPath, String renameFileName)
			throws Exception {

		logger.debug(" uploadFile ..upload to the file:" + uploadFolderPath + "/" + renameFileName);

		if (file.isEmpty() || StrUtil.isBlank(uploadFolderPath)) {
			return null;
		}

		String fileName = renameFileName == null ? file.getOriginalFilename() : renameFileName;

		String relativePath = uploadFolderPath + "/" + fileName;

		PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, relativePath, file.getInputStream());
		ossClient.putObject(putObjectRequest);// 有返回值，但没用

		return relativePath;
	}

	@Override
	public String autoSetRelativePathFile(String uploadFolder, String fileExt, Long time) {
		String uploadFolderPath = null;
		if (time != null) {
			StringBuilder sb = new StringBuilder();
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(time);
			sb.append("/").append(calendar.get(Calendar.YEAR)).append("/").append(calendar.get(Calendar.MONTH) + 1)
					.append("/").append(calendar.get(Calendar.DATE));
			uploadFolderPath = uploadFolder + sb.toString();
		} else {
			uploadFolderPath = uploadFolder;
		}
		String ext = "." + fileExt;
		String fileName = UUID.randomUUID().toString() + ext;
		return uploadFolderPath + "/" + fileName;

	}

	public void deleteFile(String bucket, String path) {

	}

	public void deleteFile(File file) {

	}

	public File getFile(String bucket, String path) throws FileNotFoundException {
		return null;
	}

	public File getFile(String bucket, String path, File defaultFile) {
		return null;
	}

	@Override
	public String getFileRealPath(String bucket, String path) {
		return null;
	}

	public void deleteDirectory(File directory) {

	}

	public void deleteDirectory(String bucket, String path) {

	}

	@Override
	public File[] listFile(String bucket, String path) {
		return null;
	}

	@Override
	public File getFileByRealPath(String realPath) throws FileNotFoundException {
		return null;
	}

	@Override
	public String getFileRelativePath(String bucket, String realPath) {
		return null;
	}

	@Override
	public String createFolder(String bucket, String path, String folderName) {
		return null;
	}

	@Override
	public void rename(String bucket, String path, String newName) {

	}

	@Override
	public boolean checkFileType(MultipartFile multipartFile, String type) {
		boolean result = false;
		String fileName = multipartFile == null || multipartFile.isEmpty() ? null : multipartFile.getOriginalFilename();
		if (StrUtil.isNotBlank(fileName) && type.indexOf(FileNameUtil.getSuffix(fileName)) != -1) {
			result = true;
		}
		return result;
	}

	public boolean exists(String path) {
		return false;
	}

	@Override
	public OSSObject getOssObject(String bucket, String path) {
		OSSObject ossObject = ossClient.getObject(bucket,path);
        return ossObject;
	}

}
