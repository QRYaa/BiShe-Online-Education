package cn.tgxy.tgbase.service;

import java.io.File;
import java.io.FileNotFoundException;

import org.springframework.web.multipart.MultipartFile;

import com.aliyun.oss.model.OSSObject;

/** 增加bucket，bucket是在fileRoot下创建多一个文件夹
 * @author Chris Deng
 * @Date 2024/10/24 10:04:38
 */
public interface BsFileService {

	/**
	 * 
	 * @param bucket
	 * @param file
	 * @param uploadFolderPath 如果上传文件至根目录，uploadFolderPath为【/】
	 * @param renameFileName
	 * @return
	 * @throws Exception
	 */
	public String uploadFile(String bucket, MultipartFile file, String uploadFolderPath, String renameFileName) throws Exception;
	
	/**
	 * 自动根据年月日生成文件夹
	 * @param bucket
	 * @param file
	 * @param uploadFolder
	 * @return
	 * @throws Exception
	 */
	public String autoRenameUploadFile(String bucket, MultipartFile file, String uploadFolder) throws Exception;

	public String createFolder(String bucket, String path, String folderName);

	public File getFile(String bucket, String path) throws FileNotFoundException;
	public File getFile(String bucket, String path, File defaultFile);

	public File getFileByRealPath(String realPath) throws FileNotFoundException;

	/**
	 * 根据bucket和相对路径path，删除文件
	 * @param bucket
	 * @param path
	 */
	public void deleteFile(String bucket, String path);

	public void deleteFile(File file);

	/**
	 * 根据bucket和相对路径path，删除文件夹
	 * @param bucket
	 * @param path
	 */
	public void deleteDirectory(String bucket, String path);

	public void deleteDirectory(File file);

	public String getFileRealPath(String bucket, String path);

	public String getFileRelativePath(String bucket, String realPath);

	public File[] listFile(String bucket, String path);

	public void rename(String bucket, String path, String newName);

//	public void download(HttpServletResponse response, String path);

	public boolean checkFileType(MultipartFile multipartFile, String type);

	/**
	 * 根据上传文件夹，文件后缀，时间自动命名
	 * @param 文件的path
	 * @return
	 * @throws
	 */
	String autoSetRelativePathFile(String uploadFolder, String fileExt, Long time);

//	void image(String path, HttpServletRequest request, HttpServletResponse response);
	
	OSSObject getOssObject(String bucket, String path);

}