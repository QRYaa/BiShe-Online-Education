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

import com.aliyun.oss.model.OSSObject;

import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import cn.tgxy.tgbase.service.BsFileService;

/**
 * @author Chris Deng
 * @Date 2024/10/23 17:52:31
 */
@Service("fileService")
@ConditionalOnProperty(name="system.base.aliOss.enable", havingValue = "false")
public class BsFileServiceImpl implements BsFileService {

	private static Logger logger = LoggerFactory.getLogger(BsFileServiceImpl.class);

	@Value("${system.base.fileRoot}")
	private String fileRoot;

	@Override
	public String autoRenameUploadFile(String bucket, MultipartFile file, String uploadFolder) throws Exception {

		if (StrUtil.isBlank(bucket) || file == null || file.isEmpty()) {
			return null;
		}

		Calendar calendar = Calendar.getInstance();
		StringBuilder sb = new StringBuilder();
		sb.append("/").append(calendar.get(Calendar.YEAR)).append("/").append(calendar.get(Calendar.MONTH) + 1)
				.append("/")
				.append(calendar.get(Calendar.DATE));
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

		File fileFolder = new File(fileRoot + "/" + bucket + "/" + uploadFolderPath);

		if (!fileFolder.exists()) {
			fileFolder.mkdirs();
		}

		File upfile = new File(fileFolder, fileName);

		logger.info("upload file to:[" + upfile.getPath() + "]");

		file.transferTo(upfile);

		return relativePath;
	}
	
	@Override
	public String autoSetRelativePathFile(String uploadFolder,String fileExt,Long time) {
		String uploadFolderPath = null;
		if(time!=null) {
			StringBuilder sb = new StringBuilder();
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(time);
			sb.append("/").append(calendar.get(Calendar.YEAR)).append("/").append(calendar.get(Calendar.MONTH) + 1)
					.append("/").append(calendar.get(Calendar.DATE));
			uploadFolderPath = uploadFolder + sb.toString();
		}
		else {
			uploadFolderPath = uploadFolder;
		}
		String ext = "." + fileExt;
		String fileName=UUID.randomUUID().toString() + ext;
		return uploadFolderPath + "/" + fileName;
		
	}

	public void deleteFile(String bucket, String path) {
		String fullPath = fileRoot + "/" + bucket + path;
		File file = new File(fullPath);

		if (file.exists()) {
			if (file.isDirectory()) {
				deleteDirectory(file);
			} else {
				file.delete();
			}
		}
	}

	public void deleteFile(File file) {
		if (file.exists()) {
			if (file.isDirectory()) {
				deleteDirectory(file);
			} else {
				file.delete();
			}
		}
	}

	public File getFile(String bucket, String path) throws FileNotFoundException {
		File file = new File(fileRoot + "/" + bucket + path);
		if (file.exists()) {
			return file;
		} else {
			throw new FileNotFoundException(path);
		}
	}
	public File getFile(String bucket, String path, File defaultFile) {
		File file = new File(fileRoot + "/" + bucket + "/" + path);
		if (file.exists()) {
			return file;
		} else {
			return defaultFile;
		}
	}

	@Override
	public String getFileRealPath(String bucket, String path) {
		return fileRoot + "/" + bucket + path;

	}

	public void deleteDirectory(File directory) {
		File[] files = directory.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				deleteDirectory(files[i]);
			} else {
				files[i].delete();
			}
		}
		directory.delete();
	}

	public void deleteDirectory(String bucket, String path) {
		File directory = new File(fileRoot + "/" + bucket + path);
		File[] files = directory.listFiles();

		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				deleteDirectory(files[i]);
			} else {
				files[i].delete();
			}
		}
		directory.delete();
	}

	@Override
	public File[] listFile(String bucket, String path) {
		String fileRealPath = getFileRealPath(bucket, path);
		File file = new File(fileRealPath);

		if (file.isDirectory()) {
			return file.listFiles();
		}

		return null;
	}

	@Override
	public File getFileByRealPath(String realPath) throws FileNotFoundException {
		File file = new File(realPath);
		if (file.exists()) {
			return file;
		} else {
			throw new FileNotFoundException(realPath);
		}
	}

	@Override
	public String getFileRelativePath(String bucket, String realPath) {
		if (realPath != null) {
			return realPath.substring(fileRoot.length()+bucket.length());
		}
		return null;
	}

	@Override
	public String createFolder(String bucket, String path, String folderName) {
		File folder = new File(fileRoot + "/" + bucket + path, folderName);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		return folder.getPath();
	}

	@Override
	public void rename(String bucket, String path, String newName) {
		File file = new File(fileRoot + "/" + bucket + path);
		if (file.exists()) {
			if (!file.getName().equals(newName)) {
				String parentPath = file.getParent();
				File target = new File(parentPath, newName);

				if (!target.exists()) {
					file.renameTo(target);
				}
			}
		}
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

	// @Override
//	public void download(HttpServletResponse response, String path) {
//		try {
//			File file = getFile(path);
//
//			ServletOutputStream outputStream = null;
//			FileInputStream inputStream = null;
//			try {
//				if (file != null && file.exists()) {
//					String ext = FileNameUtil.getSuffix(file.getPath());
//					String contentType = ContentTypeTool.getContentType(ext);
//					response.setContentType(contentType);
//					response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
//					response.setHeader("filesize", file.length() + "");
//
//					inputStream = new FileInputStream(file);
//					outputStream = response.getOutputStream();
//
//					int b = 0;
//					byte[] buffer = new byte[512];
//					while (-1 != (b = inputStream.read(buffer))) {
//						outputStream.write(buffer, 0, b);
//					}
//					outputStream.flush();
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			} finally {
//				try {
//					if (outputStream != null) {
//						outputStream.close();
//					}
//					if (inputStream != null) {
//						inputStream.close();
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	// @Override
//	public void image(String path, HttpServletRequest request, HttpServletResponse response) {
//		if (path != null) {
//			try {
//				File file = null;
//				if (exists(path)) {
//					file = getFile(path);
//				} else {
//					String basePath = request.getSession().getServletContext()
//							.getRealPath("/static/images/default.png");
//					file = new File(basePath);
//				}
//				if (file != null && file.exists()) {
//					response.setContentType("image");
//					ServletOutputStream out;
//					FileInputStream inputStream = new FileInputStream(file);
//					out = response.getOutputStream();
//
//					int b = 0;
//					byte[] buffer = new byte[512];
//					while (-1 != (b = inputStream.read(buffer))) {
//						out.write(buffer, 0, b);
//					}
//					inputStream.close();
//					out.close();
//					out.flush();
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}

	public boolean exists(String path) {

		File file = new File(fileRoot + path);
		if (file.exists()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public OSSObject getOssObject(String bucket, String path) {
		return null;
	}

}
