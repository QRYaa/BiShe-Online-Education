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
import cn.tgxy.tgbase.dto.BsTaskDto;
import cn.tgxy.tgbase.po.BsTask;
import cn.tgxy.tgbase.service.BsTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@RestController
@RequestMapping("/admin/bsTask")
@Tag(name = "bsTask")
public class BsTaskController  {

	@Autowired
	private BsTaskService taskService;
	
	@GetMapping("/page")
	@Operation(summary = "分页")
	public BsResponse page(String name,Integer status,String handlerName,@RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
			@RequestParam(name = "pageSize", defaultValue = BsCoreConstant.DEFAULT_PAGE_SIZE) Integer pageSize)
			throws Exception {
		Specification<BsTask> specification = new Specification<BsTask>() {

			@Override
			public Predicate toPredicate(Root<BsTask> root,
					CriteriaQuery<?> query, // 查询的条件
					CriteriaBuilder criteriaBuilder) { // 构造条件
				// 创建条件集合
				List<Predicate> predicates = new ArrayList<>();
				// 判断传入参数是否为空，不为空就添加至查询条件中
				if (StringUtils.isNotEmpty(name)) {
					predicates.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
				}
				if (status!=null) {
					predicates.add(criteriaBuilder.equal(root.get("status"), status));
				}
				if (StringUtils.isNotEmpty(handlerName)) {
					predicates.add(criteriaBuilder.like(root.get("handlerName"), "%" + handlerName + "%"));
				}
				// 创建一个条件的集合，长度为上面满足条件的个数（目的是将predicates列表转为集合）
				Predicate[] pre = new Predicate[predicates.size()];
				return query.where(predicates.toArray(pre)).getRestriction();
			}
		};
		Pageable pa = PageRequestConverter.of(pageNum, pageSize, Sort.by("id").descending());

		PageContent<BsTaskDto> pageContent = taskService.findPageData(specification, pa);

		return BsResponse.success(pageContent);
	}

	@GetMapping("/get/{id}")
	@Operation(summary = "明细")
	public BsResponse get(@PathVariable("id") Long id) throws Exception {
		BsTaskDto d = taskService.get(id);
		return BsResponse.success(d);
	}

	@PostMapping(value = "/save")
	@Operation(summary = "新增")
	public BsResponse save(@RequestBody BsTaskDto dto) throws Exception {
		dto.setId(null);
		taskService.insertJob(dto);
		return BsResponse.success();
	}

	@PutMapping(value = "/update")
	@Operation(summary = "修改")
	public BsResponse update(@RequestBody BsTaskDto dto) throws Exception {
		taskService.updateSchedulerJob(dto);
		return BsResponse.success();
	}

	@DeleteMapping("delete/{id}")
	@Operation(summary = "删除")
	public BsResponse delete(@PathVariable("id") Long id) throws Exception {
		taskService.deleteJob(id);
		return BsResponse.success();
	}

	@PostMapping("delete")
	@Operation(summary = "批量删除")
	public BsResponse delete(@RequestBody List<Long> idList) throws Exception {
		taskService.deleteJobByIds(idList);
		return BsResponse.success();
	}
	
	@GetMapping("/listAll")
	@Operation(summary = "列表所有")
	public BsResponse listAll() throws Exception {

		List<BsTaskDto> list = taskService.find(Sort.by("id").ascending());

		return BsResponse.success(list);
	}
	
	@PutMapping(value = "/resumeJob")
	@Operation(summary = "恢复任务")
	public BsResponse resumeJob(@RequestBody BsTaskDto dto) throws Exception {
		taskService.resumeJob(dto);
		return BsResponse.success();
	}
	
	@PutMapping(value = "/run")
	@Operation(summary = "执行一次")
	public BsResponse run(@RequestBody BsTaskDto dto) throws Exception {
		taskService.run(dto);
		return BsResponse.success();
	}
	
	@PutMapping(value = "/pauseJob")
	@Operation(summary = "暂停任务")
	public BsResponse pauseJob(@RequestBody BsTaskDto dto) throws Exception {
		taskService.pauseJob(dto);
		return BsResponse.success();
	}
}
