package cn.tgxy.tgbase.controller.admin;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.tgxy.tgbase.common.page.PageContent;
import cn.tgxy.tgbase.common.page.PageRequestConverter;
import cn.tgxy.tgbase.common.response.BsResponse;
import cn.tgxy.tgbase.constant.BsCoreConstant;
import cn.tgxy.tgbase.dto.BsRoleDto;
import cn.tgxy.tgbase.dto.request.BsSaveRolePermissionReqDto;
import cn.tgxy.tgbase.po.BsRole;
import cn.tgxy.tgbase.service.BsRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.criteria.Predicate;

@RestController
@RequestMapping("/admin/bsRole")
@Tag(name = "角色管理")
public class BsRoleController {

	@Autowired
	private BsRoleService roleService;

	@GetMapping("/page")
	@Operation(summary = "分页")
	public BsResponse page(String name, String code, @RequestParam(defaultValue = "1") Integer pageNum,
			@RequestParam(defaultValue = BsCoreConstant.DEFAULT_PAGE_SIZE) Integer pageSize) throws Exception {
		Specification<BsRole> specification = (root, query, criteriaBuilder) -> { // 构造条件
			// 创建条件集合
			List<Predicate> predicates = new ArrayList<>();
			// 判断传入参数是否为空，不为空就添加至查询条件中
			if (StringUtils.isNotEmpty(name)) {
				// equal表示传入字段需要等于数据库字段（这里可以改成like变为模糊查询）
				predicates.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
			}
			if (StringUtils.isNotEmpty(code)) {
				predicates.add(criteriaBuilder.equal(root.get("code"), code));
			}
			// 创建一个条件的集合，长度为上面满足条件的个数（目的是将predicates列表转为集合）
			Predicate[] pre = new Predicate[predicates.size()];
			return query.where(predicates.toArray(pre)).getRestriction();
		};
		Pageable pa = PageRequestConverter.of(pageNum, pageSize, Sort.by("id").descending());

		PageContent<BsRoleDto> pageContent = roleService.findPageData(specification, pa);

		return BsResponse.success(pageContent);
	}

	@GetMapping("/listAll")
	@Operation(summary = "列表所有")
	public BsResponse listAll() throws Exception {

		List<BsRoleDto> list = roleService.findAll();

		return BsResponse.success(list);
	}

	@GetMapping("/get/{id}")
	@Operation(summary = "明细")
	public BsResponse get(@PathVariable Long id) throws Exception {
		BsRoleDto d = roleService.get(id);
		return BsResponse.success(d);
	}

	@PostMapping(value = "/save")
	@Operation(summary = "新增")
	public BsResponse save(@RequestBody BsRoleDto dto) throws Exception {
		dto.setId(null);
		roleService.save(dto);
		return BsResponse.success();
	}

	@PutMapping(value = "/update")
	@Operation(summary = "修改")
	public BsResponse update(@RequestBody BsRoleDto dto) throws Exception {
		roleService.update(dto);
		return BsResponse.success();
	}

	@DeleteMapping("delete/{id}")
	@Operation(summary = "删除")
	public BsResponse delete(@PathVariable Long id) throws Exception {
		roleService.deleteById(id);
		return BsResponse.success();
	}

	@PostMapping("delete")
	@Operation(summary = "批量删除")
	public BsResponse delete(@RequestBody List<Long> idList) throws Exception {
		roleService.deleteByIdInBatch(idList);
		return BsResponse.success();
	}

	@GetMapping("rolePermission/{id}")
	@Operation(summary = "获得角色已分配权限")
	public BsResponse rolePermission(@PathVariable Long id) throws Exception {
		return BsResponse.success(roleService.listRolePermissionId(id));
	}

	@PostMapping("assignRolePermission")
	@Operation(summary = "保存角色权限")
	public BsResponse assignRolePermission(@RequestBody BsSaveRolePermissionReqDto dto) throws Exception {
		roleService.assignRolePermission(dto);
		return BsResponse.success();
	}
}
