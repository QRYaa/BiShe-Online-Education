package cn.tgxy.oledu.controller.admin;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import cn.tgxy.oledu.dto.OeNoteDto;
import cn.tgxy.oledu.service.OeNoteService;
import cn.tgxy.tgbase.common.page.PageContent;
import cn.tgxy.tgbase.common.response.BsResponse;
import cn.tgxy.tgbase.constant.BsCoreConstant;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/admin/oeNote")
@Tag(name = "oeNote")
public class OeNoteController  {

	@Autowired
	private OeNoteService noteService;
	
	@GetMapping("/page")
	@Operation(summary = "分页")
	public BsResponse page(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")Date startTime,@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")Date endTime,Integer status,Boolean enable,String memberCode,String courseName,String lessonName,Long memberId,Long lessonId,Long courseId,@RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
			@RequestParam(name = "pageSize", defaultValue = BsCoreConstant.DEFAULT_PAGE_SIZE) Integer pageSize)
			throws Exception {
		PageContent<OeNoteDto> page=noteService.findPageData(startTime,endTime,status,enable,memberCode, courseName, lessonName, memberId,lessonId,courseId,pageNum, pageSize);
		return BsResponse.success(page);
	}

	@GetMapping("/get/{id}")
	@Operation(summary = "明细")
	public BsResponse get(@PathVariable("id") Long id) throws Exception {
		OeNoteDto d = noteService.get(id);
		return BsResponse.success(d);
	}

	@PostMapping(value = "/save")
	@Operation(summary = "新增")
	public BsResponse save(@RequestBody OeNoteDto dto) throws Exception {
		dto.setId(null);
		noteService.save(dto);
		return BsResponse.success();
	}

	@PutMapping(value = "/update")
	@Operation(summary = "修改")
	public BsResponse update(@RequestBody OeNoteDto dto) throws Exception {
		noteService.update(dto);
		return BsResponse.success();
	}

	@DeleteMapping("delete/{id}")
	@Operation(summary = "删除")
	public BsResponse delete(@PathVariable("id") Long id) throws Exception {
		noteService.deleteById(id);
		return BsResponse.success();
	}

	@PostMapping("delete")
	@Operation(summary = "批量删除")
	public BsResponse delete(@RequestBody List<Long> idList) throws Exception {
		noteService.deleteByIdInBatch(idList);
		return BsResponse.success();
	}
	
	@PutMapping("switchEnable")
	@Operation(summary = "切换启用状态")
	public BsResponse switchEnable(@RequestBody OeNoteDto noteDto) throws Exception {
		noteService.switchEnable(noteDto);
		return BsResponse.success();
	}
}
