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

import cn.tgxy.oledu.dto.OeMemberCourseDto;
import cn.tgxy.oledu.service.OeMemberCourseService;
import cn.tgxy.tgbase.common.page.PageContent;
import cn.tgxy.tgbase.common.response.BsResponse;
import cn.tgxy.tgbase.constant.BsCoreConstant;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/admin/oeMemberCourse")
@Tag(name = "oeMemberCourse")
public class OeMemberCourseController {

	@Autowired
	private OeMemberCourseService memberCourseService;

	@GetMapping("/page")
	@Operation(summary = "分页")
	public BsResponse page(Long memberId,String courseName,Boolean coursePublished,Integer noteNum,
			@RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
			@RequestParam(name = "pageSize", defaultValue = BsCoreConstant.DEFAULT_PAGE_SIZE) Integer pageSize)
			throws Exception {
		PageContent<OeMemberCourseDto> pageContent = memberCourseService.findPageData(memberId, courseName,coursePublished,noteNum,
				pageNum, pageSize);
		return BsResponse.success(pageContent);
	}

	@GetMapping("/get/{id}")
	@Operation(summary = "明细")
	public BsResponse get(@PathVariable("id") Long id) throws Exception {
		OeMemberCourseDto d = memberCourseService.get(id);
		return BsResponse.success(d);
	}

	@PostMapping(value = "/save")
	@Operation(summary = "新增")
	public BsResponse save(@RequestBody OeMemberCourseDto dto) throws Exception {
		dto.setId(null);
		memberCourseService.save(dto);
		return BsResponse.success();
	}
	
	@PostMapping(value="/activateCourse")
	@Operation(summary = "开通课程")
	public BsResponse activateCourse(Long memberId,Long courseId) throws Exception {
		memberCourseService.activateCourse(memberId,courseId);
		return BsResponse.success();
	}

	@PutMapping(value = "/update")
	@Operation(summary = "修改")
	public BsResponse update(@RequestBody OeMemberCourseDto dto) throws Exception {
		memberCourseService.update(dto);
		return BsResponse.success();
	}

	@DeleteMapping("delete/{id}")
	@Operation(summary = "删除")
	public BsResponse delete(@PathVariable("id") Long id) throws Exception {
		memberCourseService.deleteById(id);
		return BsResponse.success();
	}

	@PostMapping("delete")
	@Operation(summary = "批量删除")
	public BsResponse delete(@RequestBody List<Long> idList) throws Exception {
		memberCourseService.deleteByIdInBatch(idList);
		return BsResponse.success();
	}
	
	@GetMapping("/findByMemberId")
	@Operation(summary = "根据memberId查找所有记录")
	public BsResponse findByMemberId(Long memberId) throws Exception {
		List<OeMemberCourseDto> memberCourseDtos = memberCourseService.findByMemberId(memberId);
		return BsResponse.success(memberCourseDtos);
	}
	
	@PostMapping("/batchSave/{memberId}")
	@Operation(summary = "批量保存")
	public BsResponse batchSave(@PathVariable("memberId") Long memberId,@RequestBody List<Long> courseIdList) throws Exception {
		memberCourseService.saveByMemberIdAndCourseId(memberId,courseIdList);
		return BsResponse.success();
	}

}
