package cn.tgxy.oledu.controller.admin;

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

import cn.tgxy.oledu.dto.OeTeacherDto;
import cn.tgxy.oledu.po.OeTeacher;
import cn.tgxy.oledu.service.OeTeacherService;
import cn.tgxy.tgbase.common.page.PageContent;
import cn.tgxy.tgbase.common.page.PageRequestConverter;
import cn.tgxy.tgbase.common.response.BsResponse;
import cn.tgxy.tgbase.constant.BsCoreConstant;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@RestController
@RequestMapping("/admin/oeTeacher")
@Tag(name = "oeTeacher")
public class OeTeacherController  {

	@Autowired
	private OeTeacherService teacherService;
	
	@GetMapping("/page")
	@Operation(summary = "分页")
	public BsResponse page(String code,String name,String tel,@RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
			@RequestParam(name = "pageSize", defaultValue = BsCoreConstant.DEFAULT_PAGE_SIZE) Integer pageSize)
			throws Exception {
		Specification<OeTeacher> specification = new Specification<OeTeacher>() {

			@Override
			public Predicate toPredicate(Root<OeTeacher> root,
					CriteriaQuery<?> query, // 查询的条件
					CriteriaBuilder criteriaBuilder) { // 构造条件
				// 创建条件集合
				List<Predicate> predicates = new ArrayList<>();
				// 判断传入参数是否为空，不为空就添加至查询条件中
				if (StringUtils.isNotEmpty(code)) {
					predicates.add(criteriaBuilder.equal(root.get("code"), code));
				}
				if (StringUtils.isNotEmpty(name)) {
					predicates.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
				}
				if (StringUtils.isNotEmpty(tel)) {
					predicates.add(criteriaBuilder.equal(root.get("tel"), tel));
				}
				// 创建一个条件的集合，长度为上面满足条件的个数（目的是将predicates列表转为集合）
				Predicate[] pre = new Predicate[predicates.size()];
				return query.where(predicates.toArray(pre)).getRestriction();
			}
		};
		Pageable pa = PageRequestConverter.of(pageNum, pageSize, Sort.by("id").descending());

		PageContent<OeTeacherDto> pageContent = teacherService.findPageData(specification, pa);

		return BsResponse.success(pageContent);
	}

	@GetMapping("/get/{id}")
	@Operation(summary = "明细")
	public BsResponse get(@PathVariable("id") Long id) throws Exception {
		OeTeacherDto d = teacherService.get(id);
		return BsResponse.success(d);
	}

	@PostMapping(value = "/save")
	@Operation(summary = "新增")
	public BsResponse save(@RequestBody OeTeacherDto dto) throws Exception {
		dto.setId(null);
		teacherService.save(dto);
		return BsResponse.success();
	}

	@PutMapping(value = "/update")
	@Operation(summary = "修改")
	public BsResponse update(@RequestBody OeTeacherDto dto) throws Exception {
		teacherService.update(dto);
		return BsResponse.success();
	}

	@DeleteMapping("delete/{id}")
	@Operation(summary = "删除")
	public BsResponse delete(@PathVariable("id") Long id) throws Exception {
		teacherService.deleteById(id);
		return BsResponse.success();
	}

	@PostMapping("delete")
	@Operation(summary = "批量删除")
	public BsResponse delete(@RequestBody List<Long> idList) throws Exception {
		teacherService.deleteByIdInBatch(idList);
		return BsResponse.success();
	}
	@GetMapping("/listAll")
	@Operation(summary = "列表所有")
	public BsResponse listAll() throws Exception {

		List<OeTeacherDto> list = teacherService.find(Sort.by("id").ascending());

		return BsResponse.success(list);
	}
}
