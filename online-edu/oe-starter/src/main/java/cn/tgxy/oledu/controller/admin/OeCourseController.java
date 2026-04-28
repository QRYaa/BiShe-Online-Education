package cn.tgxy.oledu.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.tgxy.oledu.dto.OeCourseDto;
import cn.tgxy.oledu.service.OeCourseService;
import cn.tgxy.tgbase.common.page.PageContent;
import cn.tgxy.tgbase.common.response.BsResponse;
import cn.tgxy.tgbase.constant.BsCoreConstant;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/admin/oeCourse")
@Tag(name = "oeCourse")
public class OeCourseController  {

	@Autowired
	private OeCourseService courseService;
	
	@GetMapping("/page")
	@Operation(summary = "分页")
	public BsResponse page(Long courseTypeId,Long teacherId,String name,Boolean published,Integer status,Long memberId,String teacherCode,@RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
			@RequestParam(name = "pageSize", defaultValue = BsCoreConstant.DEFAULT_PAGE_SIZE) Integer pageSize)
			throws Exception {
		PageContent<OeCourseDto> page=courseService.findPageData(courseTypeId, teacherId,name, published, status, null, memberId, teacherCode,pageNum, pageSize);
		return BsResponse.success(page);
	}

	@GetMapping("/get/{id}")
	@Operation(summary = "明细")
	public BsResponse get(@PathVariable("id") Long id) throws Exception {
		OeCourseDto d = courseService.get(id);
		return BsResponse.success(d);
	}

	@PostMapping(value = "/save")
	@Operation(summary = "新增")
	public BsResponse save(@RequestBody OeCourseDto dto) throws Exception {
		dto.setId(null);
		courseService.save(dto);
		return BsResponse.success();
	}

	@PutMapping(value = "/update")
	@Operation(summary = "修改")
	public BsResponse update(@RequestBody OeCourseDto dto) throws Exception {
		courseService.update(dto);
		return BsResponse.success();
	}
	
	@PutMapping(value = "changeStatus")
	@Operation(summary = "修改状态")
	public BsResponse changeStatus(@RequestBody OeCourseDto dto) throws Exception {
		courseService.changeStatus(dto);
		return BsResponse.success();
	}
	
	@PutMapping(value = "switchPaid")
	@Operation(summary = "是否免费")
	public BsResponse switchFree(@RequestBody OeCourseDto dto) throws Exception {
		courseService.switchPaid(dto);
		return BsResponse.success();
	}
	
	@PutMapping(value = "switchPublished")
	@Operation(summary = "是否发布")
	public BsResponse switchPublished(@RequestBody OeCourseDto dto) throws Exception {
		courseService.switchPublished(dto);
		return BsResponse.success();
	}

	@DeleteMapping("delete/{id}")
	@Operation(summary = "删除")
	public BsResponse delete(@PathVariable("id") Long id) throws Exception {
		courseService.deleteById(id);
		return BsResponse.success();
	}

	@PostMapping("delete")
	@Operation(summary = "批量删除")
	public BsResponse delete(@RequestBody List<Long> idList) throws Exception {
		courseService.deleteByIdInBatch(idList);
		return BsResponse.success();
	}
}
