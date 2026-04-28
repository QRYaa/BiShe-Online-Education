package cn.tgxy.tgbase.controller.admin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.tgxy.tgbase.common.page.PageContent;
import cn.tgxy.tgbase.common.page.PageRequestConverter;
import cn.tgxy.tgbase.common.response.BsResponse;
import cn.tgxy.tgbase.constant.BsCoreConstant;
import cn.tgxy.tgbase.dto.BsAppLogDto;
import cn.tgxy.tgbase.po.BsAppLog;
import cn.tgxy.tgbase.service.BsAppLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@RestController
@RequestMapping("/admin/bsAppLog")
@Tag(name = "bsAppLog")
public class BsAppLogController {

	@Autowired
	private BsAppLogService bsAppLogService;

	@GetMapping("/page")
	@Operation(summary = "分页")
	public BsResponse page(Long appId, String trackId, @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
			@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date stopTime, Integer type, String content, Integer result,
			@RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
			@RequestParam(name = "pageSize", defaultValue = BsCoreConstant.DEFAULT_PAGE_SIZE) Integer pageSize)
			throws Exception {
		Specification<BsAppLog> specification = new Specification<BsAppLog>() {

			@Override
			public Predicate toPredicate(Root<BsAppLog> root, CriteriaQuery<?> query, // 查询的条件
					CriteriaBuilder criteriaBuilder) { // 构造条件
				// 创建条件集合
				List<Predicate> predicates = new ArrayList<>();
				// 判断传入参数是否为空，不为空就添加至查询条件中
				if(appId!=null) {
					predicates.add(criteriaBuilder.equal(root.get("appId"), appId));
				}
				if (StringUtils.isNotEmpty(trackId)) {
					predicates.add(criteriaBuilder.equal(root.get("trackId"), trackId));
				}
				if(startTime!=null) {
					predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createTime"), startTime));
				}
				if(stopTime!=null) {
					predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createTime"), stopTime));
				}
				if(type!=null) {
					predicates.add(criteriaBuilder.equal(root.get("type"), type));
				}
				if (StringUtils.isNotEmpty(content)) {
					predicates.add(criteriaBuilder.like(root.get("content"), "%"+content+"%"));
				}
				if(result!=null) {
					predicates.add(criteriaBuilder.equal(root.get("result"), result));
				}
				// 创建一个条件的集合，长度为上面满足条件的个数（目的是将predicates列表转为集合）
				Predicate[] pre = new Predicate[predicates.size()];
				return query.where(predicates.toArray(pre)).getRestriction();
			}
		};
		Pageable pa = PageRequestConverter.of(pageNum, pageSize, Sort.by("id").descending());

		PageContent<BsAppLogDto> pageContent = bsAppLogService.findPageData(specification, pa);

		return BsResponse.success(pageContent);
	}

	@GetMapping("/get/{id}")
	@Operation(summary = "明细")
	public BsResponse get(@PathVariable("id") Long id) throws Exception {
		BsAppLogDto d = bsAppLogService.get(id);
		return BsResponse.success(d);
	}
}
