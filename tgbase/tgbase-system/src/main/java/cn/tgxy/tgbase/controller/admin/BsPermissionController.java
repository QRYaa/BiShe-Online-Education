package cn.tgxy.tgbase.controller.admin;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.tgxy.tgbase.common.exception.ServiceException;
import cn.tgxy.tgbase.common.response.BsResponse;
import cn.tgxy.tgbase.common.util.TreeUtils;
import cn.tgxy.tgbase.constant.BsCoreErrorConstant;
import cn.tgxy.tgbase.dto.BsPermissionDto;
import cn.tgxy.tgbase.po.BsPermission;
import cn.tgxy.tgbase.service.BsPermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.criteria.Predicate;

@RestController
@RequestMapping("/admin/bsPermission")
@Tag(name = "权限管理")
public class BsPermissionController {

	@Autowired
	private BsPermissionService permissionService;

	@GetMapping("/tree")
	@Operation(summary = "权限树")
	public BsResponse tree(String name, String code) throws Exception {

		Specification<BsPermission> specification = (root, query, criteriaBuilder) -> { // 构造条件
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
		List<BsPermissionDto> list = permissionService.find(specification, Sort.by("sort").descending());
		return BsResponse.success(TreeUtils.build(list));
	}

	@GetMapping("/get/{id}")
	@Operation(summary = "明细")
	public BsResponse get(@PathVariable Long id) throws Exception {
		BsPermissionDto d = permissionService.get(id);
		return BsResponse.success(d);
	}

	@PostMapping(value = "/save")
	@Operation(summary = "新增")
	public BsResponse save(@RequestBody BsPermissionDto dto) throws Exception {
		dto.setId(null);
		permissionService.save(dto);
		return BsResponse.success();
	}

	@PutMapping(value = "/update")
	@Operation(summary = "修改")
	public BsResponse update(@RequestBody BsPermissionDto dto) throws Exception {
		permissionService.update(dto);
		return BsResponse.success();
	}

	@DeleteMapping("delete/{id}")
	@Operation(summary = "删除")
	public BsResponse delete(@PathVariable Long id) throws Exception {

		if (permissionService.checkExistChildById(id)) {
			throw new ServiceException(BsCoreErrorConstant.PERMISSION_HAS_CHILDREN);
		}
		permissionService.deleteById(id);

		return BsResponse.success();
	}
}
