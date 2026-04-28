package cn.tgxy.oledu.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.AsyncRequestNotUsableException;
import org.springframework.web.multipart.MultipartFile;

import com.aliyun.oss.model.OSSObject;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.tgxy.oledu.constant.OeConstant;
import cn.tgxy.tgbase.common.exception.ServiceException;
import cn.tgxy.tgbase.common.response.BsResponse;
import cn.tgxy.tgbase.common.util.area.Area;
import cn.tgxy.tgbase.common.util.area.AreaNodeRespVO;
import cn.tgxy.tgbase.common.util.area.AreaUtils;
import cn.tgxy.tgbase.constant.BsCoreConstant;
import cn.tgxy.tgbase.constant.BsCoreErrorConstant;
import cn.tgxy.tgbase.constant.BsErrorConstant;
import cn.tgxy.tgbase.dto.BsAuthDto;
import cn.tgxy.tgbase.dto.BsCaptchaDto;
import cn.tgxy.tgbase.dto.response.BsAustRespDto;
import cn.tgxy.tgbase.dto.response.BsCkeditorUploadDto;
import cn.tgxy.tgbase.service.BsAuthService;
import cn.tgxy.tgbase.service.BsFileService;
import cn.tgxy.tgbase.service.RedisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@Tag(name = "基本操作")
public class BaseStoneController {

	@Autowired
	private BsAuthService authService;

	@Autowired
	private BsFileService fileService;

	@Autowired
	private RedisService redisService;

	@Value("${system.base.captchaEnabled}")
	private Boolean captchaEnabled = false;

	@Value("${ckeditorImgPre}")
	private String ckeditorImgPre = null;

	@Value("${system.base.aliOss.enable}")
	private boolean oss;

	@PostMapping("/login")
	@Operation(summary = "登录")
	public BsResponse login(@RequestBody BsAuthDto authDto) throws Exception {
		if (captchaEnabled) {
			String cid = authDto.getCid();
			if (StringUtils.isEmpty(cid)) {
				throw new ServiceException(BsCoreErrorConstant.USER_CAPTCHA_WRONG);
			}
			String code = (String) redisService.get(BsCoreConstant.CID_PREFIX + cid);
			if (StringUtils.isEmpty(code)) {
				throw new ServiceException(BsCoreErrorConstant.USER_CAPTCHA_WRONG);
			}
			if (!code.equalsIgnoreCase(authDto.getCaptchaCode())) {
				throw new ServiceException(BsCoreErrorConstant.USER_CAPTCHA_WRONG);
			}
		}

		BsAustRespDto dto = authService.login(authDto);
		return BsResponse.success(dto);
	}

	@PostMapping("/loginByTicket")
	@Operation(summary = "Ticket登录")
	public BsResponse loginByTicket(String ticket) throws Exception {
		BsAustRespDto dto = authService.loginByTicket(ticket);
		return BsResponse.success(dto);
	}

	@GetMapping("/logout")
	@Operation(summary = "登出")
	public BsResponse logout() throws Exception {
		authService.logout();
		return BsResponse.success();
	}

	@GetMapping("/captcha")
	public BsResponse captcha(HttpServletRequest request, HttpServletResponse response) throws IOException {

		BsCaptchaDto dto = new BsCaptchaDto();
		dto.setCaptchaEnabled(captchaEnabled);
		if (captchaEnabled) {
			// 定义图形验证码的长、宽、验证码字符数、干扰线宽度
			ShearCaptcha captcha = CaptchaUtil.createShearCaptcha(120, 40, 4, 2);

			// 获取验证码中的文字内容
			String verifyCode = captcha.getCode();
			String cid = IdUtil.simpleUUID();
			dto.setCid(cid);
			dto.setImage(Base64.getEncoder().encodeToString(captcha.getImageBytes()));
			redisService.set(BsCoreConstant.CID_PREFIX + cid, verifyCode, BsCoreConstant.CAPTCHA_EXPIRE_TIME);

		}
		return BsResponse.success(dto);
	}

	/**
	 * 上传图片到BUCKET_PUB的IMG_FOLDER
	 * 
	 * @param
	 * @return
	 * @throws
	 */
	@PostMapping(value = "/admin/uploadImg")
	@Operation(summary = "上传图片")
	public BsResponse uploadImg(MultipartFile file) throws Exception {
		String path = "";
		if (file != null) {
			String originalFilename = file.getOriginalFilename();
			if (StringUtils.isNotEmpty(originalFilename)) {
				boolean isImg = fileService.checkFileType(file, BsCoreConstant.IMG);
				if (isImg) {
					path = fileService.autoRenameUploadFile(OeConstant.BUCKET_PUB, file, BsCoreConstant.IMG_FOLDER);
				} else {
					throw new ServiceException(BsErrorConstant.FILE_TYPE_ERROR);
				}
			}
		}
		return BsResponse.success(path);
	}

	/**
	 * ckeditor上传图片接口，设置了图片访问地址前缀ckeditorImgPre
	 * 
	 * @param
	 * @return
	 * @throws
	 */
	@PostMapping(value = "/admin/ckeditorUploadImg")
	@Operation(summary = "上传图片")
	public BsCkeditorUploadDto ckeditorUploadImg(MultipartFile upload) throws Exception {
		String path = "";
		if (upload != null) {
			String originalFilename = upload.getOriginalFilename();
			if (StringUtils.isNotEmpty(originalFilename)) {
				boolean isImg = fileService.checkFileType(upload, BsCoreConstant.IMG);
				if (isImg) {
					path = fileService.autoRenameUploadFile(OeConstant.BUCKET_PUB, upload, BsCoreConstant.IMG_FOLDER);
				} else {
					throw new ServiceException(BsErrorConstant.FILE_TYPE_ERROR);
				}
			}
		}
		BsCkeditorUploadDto dto = new BsCkeditorUploadDto();
		dto.setUrl(ckeditorImgPre + path);
		return dto;
	}

	/**
	 * 上传音频到BUCKET_LESSON的AUDIO_FOLDER
	 * 
	 * @param
	 * @return
	 * @throws
	 */
	@PostMapping(value = "/admin/lesson/uploadAudio")
	@Operation(summary = "上传音频")
	public BsResponse uploadLessonAudio(MultipartFile file) throws Exception {
		String path = "";
		if (file != null) {
			String originalFilename = file.getOriginalFilename();
			if (StringUtils.isNotEmpty(originalFilename)) {
				boolean isAudio = fileService.checkFileType(file, BsCoreConstant.AUDIO);
				if (isAudio) {
					path = fileService.autoRenameUploadFile(OeConstant.BUCKET_LESSON, file,
							BsCoreConstant.AUDIO_FOLDER);
				} else {
					throw new ServiceException(BsErrorConstant.FILE_TYPE_ERROR);
				}
			}
		}
		return BsResponse.success(path);
	}

	/**
	 * 上传视频到BUCKET_LESSON的VIDEO_FOLDER
	 * 
	 * @param
	 * @return
	 * @throws
	 */
	@PostMapping(value = "/admin/lesson/uploadVideo")
	@Operation(summary = "上传视频")
	public BsResponse uploadLessonVideo(MultipartFile file) throws Exception {
		String path = "";
		if (file != null) {
			String originalFilename = file.getOriginalFilename();
			if (StringUtils.isNotEmpty(originalFilename)) {
				boolean isVideo = fileService.checkFileType(file, BsCoreConstant.VIDEO);
				if (isVideo) {
					path = fileService.autoRenameUploadFile(OeConstant.BUCKET_LESSON, file,
							BsCoreConstant.VIDEO_FOLDER);
				} else {
					throw new ServiceException(BsErrorConstant.FILE_TYPE_ERROR);
				}
			}
		}
		return BsResponse.success(path);
	}

	/**
	 * 下载BUCKET_PUB的图片
	 * 
	 * @param
	 * @return
	 * @throws
	 */

	@GetMapping("/ximages")
	public void image(HttpServletRequest request, HttpServletResponse response, String path) throws Exception {

		if (path != null) {
			try {
				File file = fileService.getFile(OeConstant.BUCKET_PUB, path, null); // 第三个参数可以放默认图片
				if (file != null && file.exists()) {
					response.setContentType("image");
					ServletOutputStream out;
					FileInputStream inputStream = new FileInputStream(file);
					out = response.getOutputStream();

					int b = 0;
					byte[] buffer = new byte[512];
					while (-1 != (b = inputStream.read(buffer))) {
						out.write(buffer, 0, b);
					}
					inputStream.close();
					out.close();
					out.flush();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 下载BUCKET_LESSON的视频
	 * 
	 * @param
	 * @return
	 * @throws
	 */
	@GetMapping("fadmin/lesson/xvideos")
	public void lessonVideo(HttpServletRequest request, HttpServletResponse response, String path) throws Exception {
		if (path != null) {
			try {
				File file = fileService.getFile(OeConstant.BUCKET_LESSON, path, null);
				if (file != null && file.exists()) {
					// 新增范围请求处理
					long fileLength = file.length();
					String rangeHeader = request.getHeader("Range");

					// 声明支持范围请求
					response.setHeader("Accept-Ranges", "bytes");
					response.setHeader("Content-Length", String.valueOf(fileLength));

					if (rangeHeader != null && rangeHeader.startsWith("bytes=")) {
						// 解析Range参数:处理单个范围请求
						String rangeValue = rangeHeader.replace("bytes=", "");
						String[] ranges = rangeValue.split("-");
						long start = Long.parseLong(ranges[0]);
						long end = ranges.length > 1 ? Long.parseLong(ranges[1]) : fileLength - 1;

						// 设置部分内容响应头
						response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
						response.setHeader("Content-Range", "bytes " + start + "-" + end + "/" + fileLength);
						response.setContentLength((int) (end - start + 1));

						// 分块传输
						try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
							raf.seek(start);
							byte[] buffer = new byte[8192]; // 优化缓冲区大小
							int bytesRead;
							while ((bytesRead = raf.read(buffer)) != -1) {
								// 控制传输范围
								if (start + bytesRead > end) {
									bytesRead = (int) (end - start + 1);
								}
								response.getOutputStream().write(buffer, 0, bytesRead);
								start += bytesRead;
								if (start > end)
									break;
							}
						}
					} else {
						// 完整文件传输
						Files.copy(file.toPath(), response.getOutputStream());
					}
				}
			} catch (AsyncRequestNotUsableException ae) {

			} catch (Exception e) {
				// 异常处理...
			}
		}
	}

	/**
	 * 下载BUCKET_LESSON的音频
	 * 
	 * @param
	 * @return
	 * @throws
	 */

	@GetMapping("fadmin/lesson/xaudios")
	public void lessonAudio(HttpServletRequest request, HttpServletResponse response, String path, String token)
			throws Exception {
		if (path != null) {
			try {
				File file = fileService.getFile(OeConstant.BUCKET_LESSON, path, null);
				if (file != null && file.exists()) {
					// 新增范围请求处理
					long fileLength = file.length();
					String rangeHeader = request.getHeader("Range");

					// 声明支持范围请求
					response.setHeader("Accept-Ranges", "bytes");
					response.setHeader("Content-Length", String.valueOf(fileLength));

					if (rangeHeader != null && rangeHeader.startsWith("bytes=")) {
						// 解析Range参数:处理单个范围请求
						String rangeValue = rangeHeader.replace("bytes=", "");
						String[] ranges = rangeValue.split("-");
						long start = Long.parseLong(ranges[0]);
						long end = ranges.length > 1 ? Long.parseLong(ranges[1]) : fileLength - 1;

						// 设置部分内容响应头
						response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
						response.setHeader("Content-Range", "bytes " + start + "-" + end + "/" + fileLength);
						response.setContentLength((int) (end - start + 1));

						// 分块传输
						try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
							raf.seek(start);
							byte[] buffer = new byte[8192]; // 优化缓冲区大小
							int bytesRead;
							while ((bytesRead = raf.read(buffer)) != -1) {
								// 控制传输范围
								if (start + bytesRead > end) {
									bytesRead = (int) (end - start + 1);
								}
								response.getOutputStream().write(buffer, 0, bytesRead);
								start += bytesRead;
								if (start > end)
									break;
							}
						}
					} else {
						// 完整文件传输
						Files.copy(file.toPath(), response.getOutputStream());
					}
				}
			} catch (AsyncRequestNotUsableException ae) {

			} catch (Exception e) {
				// 异常处理...
			}
		}
	}

	@GetMapping("fadmin/aliOssLessonVideo")
	@Operation(summary = "阿里oss的lessonBucket的视频资源")
	public void aliOssLessonVideo(HttpServletRequest request, HttpServletResponse response, String objectName) {
		OSSObject ossObject = fileService.getOssObject(OeConstant.BUCKET_LESSON, objectName);
		try (InputStream inputStream = ossObject.getObjectContent()) {
			long fileLength = ossObject.getObjectMetadata().getContentLength();
			if (inputStream != null) {
				String rangeHeader = request.getHeader("Range");

				// 声明支持范围请求
				response.setHeader("Accept-Ranges", "bytes");

				if (rangeHeader != null && rangeHeader.startsWith("bytes=")) {
					// 解析Range参数:处理单个范围请求
					String rangeValue = rangeHeader.replace("bytes=", "");
					String[] ranges = rangeValue.split("-");
					long start = Long.parseLong(ranges[0]);
					long end = ranges.length > 1 ? Long.parseLong(ranges[1]) : fileLength - 1;

					// 设置部分内容响应头
					response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
					response.setHeader("Content-Range", "bytes " + start + "-" + end + "/" + fileLength);
					response.setContentLength((int) (end - start + 1));

					// 定位到请求的起始位置
					inputStream.skip(start);

					byte[] buffer = new byte[8192];
					int bytesRead;
					long remaining = end - start + 1;
					try (OutputStream outputStream = response.getOutputStream()) {
						while (remaining > 0 && (bytesRead = inputStream.read(buffer, 0,
								(int) Math.min(buffer.length, remaining))) != -1) {
							outputStream.write(buffer, 0, bytesRead);
							remaining -= bytesRead;
						}
					} catch (AsyncRequestNotUsableException e) {

					}
				} else {
					// 处理完整文件传输
					response.setStatus(HttpServletResponse.SC_OK);
					response.setContentLength((int) fileLength);
					byte[] buffer = new byte[8192];
					int bytesRead;
					try (OutputStream outputStream = response.getOutputStream()) {
						while ((bytesRead = inputStream.read(buffer)) != -1) {
							outputStream.write(buffer, 0, bytesRead);
						}
					} catch (AsyncRequestNotUsableException e) {

					}
				}
			}
		} catch (IOException | NumberFormatException e) {
			e.printStackTrace();
		}
	}

	@GetMapping("fadmin/pri/ximages")
	public void priImg(HttpServletRequest request, HttpServletResponse response, String path, String token)
			throws Exception {
		if (path != null) {
			try {
				File file = fileService.getFile(OeConstant.BUCKET_PRI, path, null);
				if (file != null && file.exists()) {
					// 完整文件传输
					Files.copy(file.toPath(), response.getOutputStream());

				}
			} catch (AsyncRequestNotUsableException ae) {

			} catch (Exception e) {
				// 异常处理...
			}
		}
	}

	@GetMapping("fadmin/aliOssPriImg")
	@Operation(summary = "阿里oss的priBucket的图片资源")
	public void aliOssPriImg(HttpServletRequest request, HttpServletResponse response, String objectName) {
		OSSObject ossObject = fileService.getOssObject(OeConstant.BUCKET_PRI, objectName);

		try (InputStream inputStream = ossObject.getObjectContent()) {
			long fileLength = ossObject.getObjectMetadata().getContentLength();
			if (inputStream != null) {
				response.setStatus(HttpServletResponse.SC_OK);
				response.setContentLength((int) fileLength);
				byte[] buffer = new byte[1024];
				int bytesRead;
				try (OutputStream outputStream = response.getOutputStream()) {
					while ((bytesRead = inputStream.read(buffer)) != -1) {
						outputStream.write(buffer, 0, bytesRead);
					}
				} catch (AsyncRequestNotUsableException e) {

				}

			}
		} catch (IOException | NumberFormatException e) {
			e.printStackTrace();
		}

	}

	@GetMapping("/areaTree")
	@Operation(summary = "获得地区树")
	public BsResponse areaTree() {
		Area area = AreaUtils.getArea(Area.ID_CHINA);
		Assert.notNull(area, "获取不到中国");
		return BsResponse.success(toBean(area.getChildren(), AreaNodeRespVO.class));
	}

	public static <S, T> List<T> toBean(List<S> source, Class<T> targetType) {
		if (source == null) {
			return null;
		}
		return convertList(source, s -> BeanUtil.toBean(s, targetType));
	}

	public static <T, U> List<U> convertList(Collection<T> from, Function<T, U> func) {
		if (CollUtil.isEmpty(from)) {
			return new ArrayList<>();
		}
		return from.stream().map(func).filter(Objects::nonNull).collect(Collectors.toList());
	}

}
