package cn.tgxy.tgbase.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.tgxy.tgbase.common.response.BsResponse;
import cn.tgxy.tgbase.dto.BsDictItemDto;
import cn.tgxy.tgbase.service.BsDictItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/admin/bsDict/item")
@Tag(name = "字典数值管理操作")
public class BsDictItemController {

	@Autowired
	private BsDictItemService dictItemService;

	@GetMapping("/list/{dictId}")
	@Operation(summary = "列表（不分页）")
	public BsResponse list(@PathVariable Long dictId) throws Exception {
		List<BsDictItemDto> list = dictItemService.list(dictId);
		return BsResponse.success(list);
	}

	@GetMapping("/get/{id}")
	@Operation(summary = "明细")
	public BsResponse get(@PathVariable Long id) throws Exception {
		BsDictItemDto d = dictItemService.get(id);
		return BsResponse.success(d);
	}

	@PostMapping(value = "/save")
	@Operation(summary = "新增")
	public BsResponse save(@RequestBody BsDictItemDto dto) throws Exception {
		dto.setId(null);
		dictItemService.save(dto);
		return BsResponse.success();
	}

	@PutMapping(value = "/update")
	@Operation(summary = "修改")
	public BsResponse update(@RequestBody BsDictItemDto dto) throws Exception {
		dictItemService.update(dto);
		return BsResponse.success();
	}

	@DeleteMapping("delete/{id}")
	@Operation(summary = "删除")
	public BsResponse delete(@PathVariable Long id) throws Exception {
		dictItemService.deleteById(id);
		return BsResponse.success();
	}

}
