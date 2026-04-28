package cn.tgxy.tgbase.controller.admin;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.tgxy.tgbase.common.exception.ServiceException;
import cn.tgxy.tgbase.common.page.PageContent;
import cn.tgxy.tgbase.common.response.BsResponse;
import cn.tgxy.tgbase.constant.BsCoreConstant;
import cn.tgxy.tgbase.constant.BsCoreErrorConstant;
import cn.tgxy.tgbase.dto.BsUserDto;
import cn.tgxy.tgbase.service.BsAuthService;
import cn.tgxy.tgbase.service.BsUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/admin/bsUser")
@Tag(name = "用户操作")
public class BsUserController {

	@Autowired
	private BsUserService userService;

	@Autowired
	private BsAuthService authService;

	@GetMapping("/userInfo")
	@Operation(summary = "当前用户信息")
	public BsResponse userInfo() throws Exception {
		BsUserDto dto = authService.getCurrentUser();
		return BsResponse.success(dto);
	}

	@GetMapping("/page")
	@Operation(summary = "分页")
	public BsResponse page(String username, String realName, Long departmentId, Integer gender, String email,
			String mobilePhone, Integer enable, Long roleId,Long appId, @RequestParam(defaultValue = "1") Integer pageNum,
			@RequestParam(defaultValue = BsCoreConstant.DEFAULT_PAGE_SIZE) Integer pageSize) throws Exception {

		PageContent<BsUserDto> pageContent = userService.findPageData(username, realName, departmentId, gender, email,
				mobilePhone, enable, roleId,appId, pageNum, pageSize);
		return BsResponse.success(pageContent);
	}
//	@GetMapping("/page")
//	@Operation(summary = "分页")
//	public BsResponse page(String username, String realName, /*
//																 * Long departmentId, Integer gender, String email,
//																 * String mobilePhone, Integer enable, Long roleId,
//																 */@RequestParam(defaultValue = "1") Integer pageNum,
//			@RequestParam(defaultValue = BsCoreConstant.DEFAULT_PAGE_SIZE) Integer pageSize) throws Exception {
//		Specification<BsUser> specification = new Specification<BsUser>() {
//
//			@Override
//			public Predicate toPredicate(Root<BsUser> root, // 查询的类型（实体类的一个路径,告诉查询用的是哪个字段），使用get获取字段
//					CriteriaQuery<?> query, // 查询的条件
//					CriteriaBuilder criteriaBuilder) { // 构造条件
//				// 创建条件集合
//				List<Predicate> predicates = new ArrayList<>();
//				if (StringUtils.isNotEmpty(username)) {
//					predicates.add(criteriaBuilder.equal(root.get("username"), username));
//				}
//				if (StringUtils.isNotEmpty(realName)) {
//					predicates.add(criteriaBuilder.like(root.get("realName"), "%" + realName + "%"));
//				}
//				// 创建一个条件的集合，长度为上面满足条件的个数（目的是将predicates列表转为集合）
//				Predicate[] pre = new Predicate[predicates.size()];
//				return query.where(predicates.toArray(pre)).getRestriction();
//			}
//		};
//		Pageable pa = PageRequestConverter.of(pageNum, pageSize, Sort.by("id").descending());
//
//		PageContent<BsUserDto> pageContent = userService.findPageData(specification, pa);
//		return BsResponse.success(pageContent);
//	}

	@GetMapping("/search")
	@Operation(summary = "寻找用户", description = "通过关键字（姓名）寻找用户,最多返回10人")
	public BsResponse search(String keyword) throws Exception {
		List<BsUserDto> list = userService.search(keyword);
		return BsResponse.success(list);
	}

	@GetMapping("/get/{id}")
	@Operation(summary = "明细")
	public BsResponse get(@PathVariable Long id) throws Exception {
		BsUserDto u = userService.get(id);
		return BsResponse.success(u);
	}

	@PostMapping(value = "/save")
	@Operation(summary = "新增")
	public BsResponse save(@RequestBody BsUserDto dto) throws Exception {
		dto.setId(null);
		userService.save(dto);
		return BsResponse.success();
	}

	@PutMapping(value = "/update")
	@Operation(summary = "修改")
	public BsResponse update(@RequestBody BsUserDto dto) throws Exception {
		userService.update(dto);
		return BsResponse.success();
	}

	@DeleteMapping("delete/{id}")
	@Operation(summary = "删除")
	public BsResponse delete(@PathVariable Long id) throws Exception {
		userService.deleteById(id);
		return BsResponse.success();
	}

	@PostMapping("delete")
	@Operation(summary = "批量删除")
	public BsResponse delete(@RequestBody List<Long> idList) throws Exception {
		userService.deleteByIdInBatch(idList);
		return BsResponse.success();
	}
	
	@GetMapping("/listAll")
	@Operation(summary = "列表所有")
	public BsResponse listAll() throws Exception {
		List<BsUserDto> dto=userService.find(Sort.by("id"));
		return BsResponse.success(dto);
	}

	// TODO
	@PutMapping(value = "changeStatus")
	@Operation(summary = "修改状态")
	public BsResponse changeStatus(@RequestBody BsUserDto dto) throws Exception {
//		userService.update(dto);
		return BsResponse.success();
	}
	

	@PutMapping(value = "assignRole")
	@Operation(summary = "分配角色")
	public BsResponse assignRole(@RequestBody BsUserDto dto) throws Exception {
		userService.assignUserRole(dto);
		return BsResponse.success();
	}
	
	@PostMapping(value = "addApp")
	@Operation(summary = "新增应用")
	public BsResponse addApp(Long userId,@RequestParam("appId") String appIdString) throws Exception {
		 List<Long> appIdList = Arrays.stream(appIdString.split(","))
			        .map(Long::valueOf)
			        .collect(Collectors.toList());
		userService.addApp(userId,appIdList);
		return BsResponse.success();
	}
	
	@PostMapping(value = "deleteApp")
	@Operation(summary = "删除应用")
	public BsResponse deleteApp(Long userId,Long appId) throws Exception {
		userService.deleteApp(userId,appId);
		return BsResponse.success();
	}

	@PostMapping("changePassword")
	@Operation(summary = "管理员修改密码")
	public BsResponse changePassword(Long id, String newPassword) throws Exception {
		userService.changePassword(id, newPassword);
		return BsResponse.success();
	}

	@PostMapping("changeMyPassword")
	@Operation(summary = "个人修改密码")
	public BsResponse changeMyPassword(String oldPassword, String newPassword) throws Exception {
		if (StringUtils.isNotBlank(newPassword)) {
			BsUserDto currentUser = authService.getCurrentUser();
			userService.changeMyPassword(currentUser, oldPassword, newPassword);
//			authService.updateCurrentUser(currentUser);
		} else {
			throw new ServiceException(BsCoreErrorConstant.PASSWORD_EMPTY);
		}
		return BsResponse.success();
	}
	
	@GetMapping("findByUsername")
	@Operation(summary="通过username查询用户")
	public BsResponse findByUsername(String username) throws Exception {
		BsUserDto dto = userService.findByUsername(username);
		return BsResponse.success(dto);
	}
	
}
