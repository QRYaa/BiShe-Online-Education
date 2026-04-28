package cn.tgxy.oledu.controller.admin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.tgxy.oledu.dto.OeNewsDto;
import cn.tgxy.oledu.po.OeNews;
import cn.tgxy.oledu.service.OeNewsService;
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
@RequestMapping("/admin/oeNews")
@Tag(name = "oeNews")
public class OeNewsController  {

	@Autowired
	private OeNewsService oeNewsService;
	
	@GetMapping("/page")
	@Operation(summary = "分页")
	public BsResponse page(String title,String code,@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")Date startTime,@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")Date stopTime,String author,Boolean published,@RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
			@RequestParam(name = "pageSize", defaultValue = BsCoreConstant.DEFAULT_PAGE_SIZE) Integer pageSize)
			throws Exception {
		Specification<OeNews> specification = new Specification<OeNews>() {

			@Override
			public Predicate toPredicate(Root<OeNews> root,
					CriteriaQuery<?> query, // 查询的条件
					CriteriaBuilder criteriaBuilder) { // 构造条件
				// 创建条件集合
				List<Predicate> predicates = new ArrayList<>();
				// 判断传入参数是否为空，不为空就添加至查询条件中
				if (StringUtils.isNotEmpty(title)) {
					predicates.add(criteriaBuilder.like(root.get("title"), "%" + title + "%"));
				}
				if (StringUtils.isNotEmpty(code)) {
					predicates.add(criteriaBuilder.equal(root.get("code"), code));
				}
				if(startTime!=null) {
					predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("publishedTime"), startTime));
				}
				if(stopTime!=null) {
					predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("publishedTime"), stopTime));
				}
				if (StringUtils.isNotEmpty(author)) {
					predicates.add(criteriaBuilder.equal(root.get("author"), author));
				}
				if(published != null) {
					predicates.add(criteriaBuilder.equal(root.get("published"), published));
				}
				// 创建一个条件的集合，长度为上面满足条件的个数（目的是将predicates列表转为集合）
				Predicate[] pre = new Predicate[predicates.size()];
				return query.where(predicates.toArray(pre)).getRestriction();
			}
		};
		Pageable pa = PageRequestConverter.of(pageNum, pageSize, Sort.by("id").descending());

		PageContent<OeNewsDto> pageContent = oeNewsService.findPageData(specification, pa);

		return BsResponse.success(pageContent);
	}

	@GetMapping("/get/{id}")
	@Operation(summary = "明细")
	public BsResponse get(@PathVariable("id") Long id) throws Exception {
		OeNewsDto d = oeNewsService.get(id);
		return BsResponse.success(d);
	}

	@PostMapping(value = "/save")
	@Operation(summary = "新增")
	public BsResponse save(@RequestBody OeNewsDto dto) throws Exception {
		dto.setId(null);
		oeNewsService.save(dto);
		return BsResponse.success();
	}

	@PutMapping(value = "/update")
	@Operation(summary = "修改")
	public BsResponse update(@RequestBody OeNewsDto dto) throws Exception {
		oeNewsService.update(dto);
		return BsResponse.success();
	}

	@DeleteMapping("delete/{id}")
	@Operation(summary = "删除")
	public BsResponse delete(@PathVariable("id") Long id) throws Exception {
		oeNewsService.deleteById(id);
		return BsResponse.success();
	}

	@PostMapping("delete")
	@Operation(summary = "批量删除")
	public BsResponse delete(@RequestBody List<Long> idList) throws Exception {
		oeNewsService.deleteByIdInBatch(idList);
		return BsResponse.success();
	}
}
