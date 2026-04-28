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
import cn.tgxy.tgbase.dto.BsDictDto;
import cn.tgxy.tgbase.po.BsDict;
import cn.tgxy.tgbase.service.BsDictService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.criteria.Predicate;

@RestController
@RequestMapping("/admin/bsDict")
@Tag(name = "字典管理操作")
public class BsDictController {

	@Autowired
	private BsDictService dictService;

	@GetMapping("/page")
	@Operation(summary = "分页")
	public BsResponse page(@RequestParam(name = "name", required = false) String name, 
			@RequestParam(name = "code", required = false) String code, 
			@RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
			@RequestParam(name = "pageSize", defaultValue = BsCoreConstant.DEFAULT_PAGE_SIZE) Integer pageSize) throws Exception {
		Specification<BsDict> specification = (root, query, criteriaBuilder) -> { // 构造条件
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

		PageContent<BsDictDto> pageContent = dictService.findPageData(specification, pa);

		return BsResponse.success(pageContent);
	}

	@GetMapping("/get/{id}")
	@Operation(summary = "明细")
	public BsResponse get(@PathVariable Long id) throws Exception {
		BsDictDto d = dictService.get(id);
		return BsResponse.success(d);
	}

	@PostMapping(value = "/save")
	@Operation(summary = "新增")
	public BsResponse save(@RequestBody BsDictDto dto) throws Exception {
		dto.setId(null);
		dictService.save(dto);
		return BsResponse.success();
	}

	@PutMapping(value = "/update")
	@Operation(summary = "修改")
	public BsResponse update(@RequestBody BsDictDto dto) throws Exception {
		dictService.update(dto);
		return BsResponse.success();
	}

	@DeleteMapping("delete/{id}")
	@Operation(summary = "删除")
	public BsResponse delete(@PathVariable Long id) throws Exception {
		dictService.deleteById(id);
		return BsResponse.success();
	}

	@PostMapping("delete")
	@Operation(summary = "批量删除")
	public BsResponse delete(@RequestBody List<Long> idList) throws Exception {
		dictService.deleteByIdInBatch(idList);
		return BsResponse.success();
	}

	@GetMapping("/loadAll")
	@Operation(summary = "获得所有字典")
	public BsResponse loadAll() throws Exception {
		List<BsDictDto> list = dictService.findAll();
		return BsResponse.success(list);
	}

	@GetMapping("/load/{code}")
	@Operation(summary = "根据Code获得字典数值")
	public BsResponse load(@PathVariable String code) throws Exception {
		BsDictDto dto = dictService.findByCode(code);
		return BsResponse.success(dto);
	}

}
