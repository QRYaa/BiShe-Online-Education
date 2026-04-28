package cn.tgxy.tgbase.service.impl;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;

import cn.tgxy.tgbase.common.exception.ServiceException;
import cn.tgxy.tgbase.common.page.PageContent;
import cn.tgxy.tgbase.common.util.password.PasswordUtils;
import cn.tgxy.tgbase.constant.BsCoreConstant;
import cn.tgxy.tgbase.constant.BsCoreErrorConstant;
import cn.tgxy.tgbase.dao.BsPermissionDao;
import cn.tgxy.tgbase.dao.BsUserAppMapDao;
import cn.tgxy.tgbase.dao.BsUserDao;
import cn.tgxy.tgbase.dao.BsUserDepartmentMapDao;
import cn.tgxy.tgbase.dao.BsUserExtDao;
import cn.tgxy.tgbase.dao.BsUserRoleMapDao;
import cn.tgxy.tgbase.dto.BsApplicationDto;
import cn.tgxy.tgbase.dto.BsRoleDto;
import cn.tgxy.tgbase.dto.BsUserDto;
import cn.tgxy.tgbase.po.BsPermission;
import cn.tgxy.tgbase.po.BsUser;
import cn.tgxy.tgbase.po.BsUserAppMap;
import cn.tgxy.tgbase.po.BsUserDepartmentMap;
import cn.tgxy.tgbase.po.BsUserRoleMap;
import cn.tgxy.tgbase.service.BsApplicationService;
import cn.tgxy.tgbase.service.BsRoleService;
import cn.tgxy.tgbase.service.BsUserService;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Service
public class BsUserServiceImpl extends BsBaseServiceImpl<BsUser, BsUserDto> implements BsUserService {

	@Autowired
	private BsUserDao userDao;

	@Autowired
	private BsUserExtDao userExtDao;

	@Autowired
	private BsPermissionDao permissionDao;

	@Autowired
	private BsUserDepartmentMapDao userDepartmentMapDao;

	@Autowired
	private BsUserRoleMapDao userRoleMapDao;

	@Autowired
	private BsUserAppMapDao userAppMapDao;

	@Autowired
	private BsRoleService roleService;

	@Autowired
	private BsApplicationService appService;

	@Override
	@PostConstruct
	public void initBaseDao() {
		baseDao = userDao;
	}

	public BsUserDto findByUsername(String username) throws Exception {
		BsUser user = userDao.findByUsername(username);
		return transToDtoDetail(user);
	}

	@Override
	@Transactional
	public void save(BsUserDto dto) throws Exception {
		boolean existsUsername = userDao.existsByUsername(dto.getUsername());
		// username查重
		if (existsUsername) {
			throw new ServiceException(BsCoreErrorConstant.USERNAME_EXIST);
		}
		// jobNo查重
		if (StringUtils.isNotEmpty(dto.getJobNo()) && userDao.existsByJobNo(dto.getJobNo())) {
			throw new ServiceException(BsCoreErrorConstant.JOBNUM_EXIST);
		}
		// 手机号校验和查重
		if (StringUtils.isNotEmpty(dto.getMobilePhone())
				&& !Pattern.matches(BsCoreConstant.REGEX_MOBILE, dto.getMobilePhone())) {
			throw new ServiceException(BsCoreErrorConstant.MOBILE_FORMAT_ERROR);
		}
		if (StringUtils.isNotEmpty(dto.getMobilePhone()) && userDao.existsByMobilePhone(dto.getMobilePhone())) {
			throw new ServiceException(BsCoreErrorConstant.MOBILE_EXIST);
		}
		// 邮箱校验和查重
		if (StringUtils.isNotEmpty(dto.getEmail()) && !Pattern.matches(BsCoreConstant.REGEX_EMAIL, dto.getEmail())) {
			throw new ServiceException(BsCoreErrorConstant.EMAIL_FORMAT_ERROR);
		}
		if (StringUtils.isNotEmpty(dto.getEmail()) && userDao.existsByEmail(dto.getEmail())) {
			throw new ServiceException(BsCoreErrorConstant.EMAIL_EXIST);
		}

		BsUser user = new BsUser();
		BeanUtils.copyProperties(dto, user);

		// 密码加密
		if (user.getPassword() != null) {
			String password = PasswordUtils.encode(user.getPassword());
			user.setPassword(password);
		}
		userDao.save(user); // 基础属性保存
		dto.setId(user.getId());

		// 保存用户主岗
		Long departmentId = dto.getDepartmentId();
		if (departmentId != null) {
			BsUserDepartmentMap udm = new BsUserDepartmentMap();
			udm.setUserId(user.getId());
			udm.setDepartmentId(departmentId);
			udm.setMajor(true);
			userDepartmentMapDao.save(udm);
		}
		// 保存用户兼岗
		List<Long> departmentIdList = dto.getDepartmentIdList();
		if (departmentIdList != null) {
			departmentIdList.forEach(did -> {
				BsUserDepartmentMap udm = new BsUserDepartmentMap();
				udm.setUserId(user.getId());
				udm.setDepartmentId(did);
				udm.setMajor(false);
				userDepartmentMapDao.save(udm);
			});
		}
		//保存分配的应用
		List<Long> appIdList=dto.getAppIdList();
		if(appIdList!=null) {
			appIdList.forEach(aid->{
				BsUserAppMap uam=new BsUserAppMap();
				uam.setUserId(user.getId());
				uam.setAppId(aid);
				userAppMapDao.save(uam);
			});
		}
	}

	@Override
	@Transactional
	public void update(BsUserDto dto) throws Exception {
		BsUser user = userDao.findById(dto.getId()).orElse(null);
		if (user == null)
			return;
		// username查重
		if (!user.getUsername().equals(dto.getUsername())) {
			boolean existsUsername = userDao.existsByUsername(dto.getUsername());
			if (existsUsername) {
				throw new ServiceException(BsCoreErrorConstant.USERNAME_EXIST);
			}
		}
		// jobNo查重
		if (StringUtils.isNotEmpty(dto.getJobNo()) && !dto.getJobNo().equals(user.getJobNo())
				&& userDao.existsByJobNo(dto.getJobNo())) {
			throw new ServiceException(BsCoreErrorConstant.JOBNUM_EXIST);
		}
		// 手机号校验和查重
		if (StringUtils.isNotEmpty(dto.getMobilePhone())
				&& !Pattern.matches(BsCoreConstant.REGEX_MOBILE, dto.getMobilePhone())) {
			throw new ServiceException(BsCoreErrorConstant.MOBILE_FORMAT_ERROR);
		}
		if (StringUtils.isNotEmpty(dto.getMobilePhone()) && !dto.getMobilePhone().equals(user.getMobilePhone())
				&& userDao.existsByMobilePhone(dto.getMobilePhone())) {
			throw new ServiceException(BsCoreErrorConstant.MOBILE_EXIST);
		}
		// 邮箱校验和查重
		if (StringUtils.isNotEmpty(dto.getEmail()) && !Pattern.matches(BsCoreConstant.REGEX_EMAIL, dto.getEmail())) {
			throw new ServiceException(BsCoreErrorConstant.EMAIL_FORMAT_ERROR);
		}
		if (StringUtils.isNotEmpty(dto.getEmail()) && !dto.getEmail().equals(user.getEmail())
				&& userDao.existsByEmail(dto.getEmail())) {
			throw new ServiceException(BsCoreErrorConstant.EMAIL_EXIST);
		}

		user.setJobNo(dto.getJobNo());
		user.setRealName(dto.getRealName());
		user.setDepartmentId(dto.getDepartmentId());
		user.setGender(dto.getGender());
		user.setAvatar(dto.getAvatar());
		user.setEmail(dto.getEmail());
		user.setMobilePhone(dto.getMobilePhone());
		user.setEnable(dto.getEnable());
		user.setIsSuperAdmin(false);
		userDao.save(user);

		// 修改用户主岗
		Long departmentId = dto.getDepartmentId();
		if (departmentId != null) {
			// 查找用户岗位记录
			BsUserDepartmentMap udm = userDepartmentMapDao.findByUserIdAndDepartmentId(user.getId(), departmentId);
			if (udm == null) { // 不存在则创建主岗记录
				udm = new BsUserDepartmentMap();
				udm.setUserId(user.getId());
				udm.setDepartmentId(departmentId);
				udm.setMajor(true);
				userDepartmentMapDao.save(udm);
			} else {
				if (!udm.getMajor()) { // 原来是兼岗则修改为主岗
					udm.setMajor(true);
					userDepartmentMapDao.save(udm);
				}
			}

		} else {
			// 删除原有主岗
			userDepartmentMapDao.deleteByUserIdAndMajor(user.getId(), true);
		}

		// 修改用户兼岗
		List<Long> departmentIdList = dto.getDepartmentIdList();
		List<BsUserDepartmentMap> udmList = userDepartmentMapDao.findByUserId(user.getId());
		Set<Long> udmIdSet = new HashSet<>();
		udmList.forEach(m -> udmIdSet.add(m.getDepartmentId())); // 把数据库中原有关系放入Set中
		departmentIdList.forEach(did -> {
			if (udmIdSet.contains(did)) {
				udmIdSet.remove(did);
			} else {
				BsUserDepartmentMap udm = new BsUserDepartmentMap();
				udm.setUserId(user.getId());
				udm.setDepartmentId(did);
				udm.setMajor(false);
				userDepartmentMapDao.save(udm);
			}
		});
		udmIdSet.forEach(did -> {
			userDepartmentMapDao.deleteByUserIdAndDepartmentIdAndMajor(user.getId(), did, false);
		});
	}
	
	@Override
	public void deleteById(Long id) throws Exception {
		super.deleteById(id);
	}
	@Override
	public void deleteByIdInBatch(List<Long> ids) throws Exception {
		super.deleteByIdInBatch(ids);
	}
	
	@Override
	public BsUserDto transToDto(BsUser entity) throws Exception {
		BsUserDto dto = super.transToDto(entity);
		if (dto != null) {
			// 设置角色集
			List<BsRoleDto> roleList = roleService.findByUserId(entity.getId());
			dto.setRoleList(roleList);

			// 设置兼岗部门ID集
			List<BsUserDepartmentMap> udmList = userDepartmentMapDao.findByUserIdAndMajor(entity.getId(), false);
			List<Long> departmentIdList = new ArrayList<>();
			udmList.forEach(udm -> departmentIdList.add(udm.getDepartmentId()));
			dto.setDepartmentIdList(departmentIdList);
			
			// 设置应用集
			List<BsApplicationDto> appList = appService.findByUserId(entity.getId());
			dto.setAppList(appList);
			List<Long> appIdList = new ArrayList<>();
			if (appList != null) {
				appList.forEach(appDto -> {
					appIdList.add(appDto.getId());
				});
			}
			dto.setAppIdList(appIdList);
		}
		return dto;
	}

	@Override
	public BsUserDto transToDtoDetail(BsUser entity) throws Exception {
		BsUserDto dto = super.transToDtoDetail(entity);
		if (dto != null) {
			// 设置角色集
			List<BsRoleDto> roleList = roleService.findByUserId(entity.getId());
			dto.setRoleList(roleList);
			List<Long> roleIdList = new ArrayList<>();
			if (roleList != null) {
				roleList.forEach(roleDto -> {
					roleIdList.add(roleDto.getId());
				});
			}
			dto.setRoleIdList(roleIdList);

			// 设置权限编码集
			List<BsPermission> permissionList = permissionDao.findByUserId(entity.getId());
			if (permissionList != null) {
				Set<String> permissionCodes = new HashSet<>();
				permissionList.forEach(permission -> permissionCodes.add(permission.getCode()));
				dto.setPermissions(permissionCodes);
			}

			// 设置兼岗部门ID集
			List<BsUserDepartmentMap> udmList = userDepartmentMapDao.findByUserIdAndMajor(entity.getId(), false);
			List<Long> departmentIdList = new ArrayList<>();
			udmList.forEach(udm -> departmentIdList.add(udm.getDepartmentId()));
			dto.setDepartmentIdList(departmentIdList);

			// 设置应用集
			List<BsApplicationDto> appList = appService.findByUserId(entity.getId());
			dto.setAppList(appList);
			List<Long> appIdList = new ArrayList<>();
			if (appList != null) {
				appList.forEach(appDto -> {
					appIdList.add(appDto.getId());
				});
			}
			dto.setAppIdList(appIdList);
		}

		return dto;
	}

	@Override
	public void changePassword(Long userId, String newPassword) throws Exception {
		BsUser user = userDao.findById(userId).orElse(null);
		if (user != null && StringUtils.isNotBlank(newPassword)) {
			String password = PasswordUtils.encode(newPassword);
			userDao.changeUserPassword(userId, password);
		}
	}

	@Override
	public void changeMyPassword(BsUserDto currentUser, String oldPassword, String newPassword) throws Exception {
		BsUser user = userDao.getReferenceById(currentUser.getId());
		if (!PasswordUtils.matches(oldPassword, user.getPassword())) {
			throw new ServiceException(BsCoreErrorConstant.USER_PASSWORD_ERROR);
		}
		String passwrod = PasswordUtils.encode(newPassword);
		user.setPassword(passwrod);
		userDao.save(user);

	}

	@Override
	public List<BsUserDto> search(String keyword) throws Exception {
		List<BsUser> list = userDao.findByRealNameLike(keyword, Limit.of(10));
		return transToDtoList(list);
	}

	@Override
	public PageContent<BsUserDto> findPageData(String username, String realName, Long departmentId, Integer gender,
			String email, String mobilePhone, Integer enable, Long roleId,Long appId, int pageNum, int pageSize) throws Exception {
		PageContent<BsUser> pageContent = userExtDao.findPageContent(username, realName, departmentId, gender, email,
				mobilePhone, enable, roleId, appId,pageNum, pageSize);
		PageContent<BsUserDto> page = new PageContent<>(transToDtoList(pageContent.getContent()),
				pageContent.getTotalItems(), pageContent.getPageNum(), pageSize);
		return page;
	}

	@Override
	@Transactional
	public void assignUserRole(BsUserDto userDto) throws Exception {
		List<Long> roleIdList = userDto.getRoleIdList();

		Set<Long> dbUserRoleSet = new HashSet<Long>();// 数据库中用户角色

		List<BsRoleDto> roleList = roleService.findByUserId(userDto.getId());
		roleList.forEach(role -> dbUserRoleSet.add(role.getId()));
		roleIdList.forEach(roleId -> {
			if (dbUserRoleSet.contains(roleId)) {
				dbUserRoleSet.remove(roleId);
			} else {
				BsUserRoleMap ur = new BsUserRoleMap();
				ur.setUserId(userDto.getId());
				ur.setRoleId(roleId);
				userRoleMapDao.save(ur);
			}
		});

		dbUserRoleSet.forEach(roleId -> {
			userRoleMapDao.deleteByUserIdAndRoleId(userDto.getId(), roleId);
		});
	}

	@Override
	@Transactional
	public void addApp(Long userId,List<Long> appIdList) throws Exception {
		appIdList.forEach(appId -> {
			BsUserAppMap ua = new BsUserAppMap();
			ua.setUserId(userId);
			ua.setAppId(appId);
			userAppMapDao.save(ua);
			
		});
	}
	
	@Override
	@Transactional
	public void deleteApp(Long userId,Long appId) throws Exception {
		userAppMapDao.deleteByUserIdAndAppId(userId, appId);
	}

	@Override
	public List<BsUserDto> findUserByAppId(Long appId) throws Exception {
		return transToDtoList(userDao.findByAppId(appId));
	}
	
}
