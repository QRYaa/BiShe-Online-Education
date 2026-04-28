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
import cn.tgxy.tgbase.dto.BsDepartmentDto;
import cn.tgxy.tgbase.po.BsDepartment;
import cn.tgxy.tgbase.service.BsDepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.criteria.Predicate;

@RestController
@RequestMapping("/admin/bsDepartment")
@Tag(name = "部门管理")
public class BsDepartmentController {
	@Autowired
	private BsDepartmentService departmentService;

	@GetMapping("/tree")
	@Operation(summary = "部门树")
	public BsResponse tree(String name) throws Exception {

		Specification<BsDepartment> specification = (root, query, criteriaBuilder) -> { // 构造条件
			// 创建条件集合
			List<Predicate> predicates = new ArrayList<>();
			// 判断传入参数是否为空，不为空就添加至查询条件中
			if (StringUtils.isNotEmpty(name)) {
				// equal表示传入字段需要等于数据库字段（这里可以改成like变为模糊查询）
				predicates.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
			}
			// 创建一个条件的集合，长度为上面满足条件的个数（目的是将predicates列表转为集合）
			Predicate[] pre = new Predicate[predicates.size()];
			return query.where(predicates.toArray(pre)).getRestriction();
		};
		List<BsDepartmentDto> list = departmentService.find(specification, Sort.by("sort").descending());
		return BsResponse.success(TreeUtils.build(list));
	}
	
	@GetMapping("/listAll")
	@Operation(summary = "列表所有")
	public BsResponse listAll() throws Exception {

		List<BsDepartmentDto> list = departmentService.find(Sort.by("id").ascending());

		return BsResponse.success(list);
	}

	@GetMapping("/get/{id}")
	@Operation(summary = "明细")
	public BsResponse get(@PathVariable Long id) throws Exception {
		BsDepartmentDto d = departmentService.get(id);
		return BsResponse.success(d);
	}

	@PostMapping(value = "/save")
	@Operation(summary = "新增")
	public BsResponse save(@RequestBody BsDepartmentDto dto) throws Exception {
		dto.setId(null);
		departmentService.save(dto);
		return BsResponse.success();
	}

	@PutMapping(value = "/update")
	@Operation(summary = "修改")
	public BsResponse update(@RequestBody BsDepartmentDto dto) throws Exception {
		departmentService.update(dto);
		return BsResponse.success();
	}

	@DeleteMapping("delete/{id}")
	@Operation(summary = "删除")
	public BsResponse delete(@PathVariable Long id) throws Exception {

		if (departmentService.checkChild(id)) {
			throw new ServiceException(BsCoreErrorConstant.DEPARTMENT_HAS_CHILDRENT);
		}
		
		if (departmentService.checkUser(id)) {
			throw new ServiceException(BsCoreErrorConstant.DEPARTMENT_HAS_USER);
		}
		
		departmentService.deleteById(id);

		return BsResponse.success();
	}
}
