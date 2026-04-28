package cn.tgxy.tgbase.service;

import cn.tgxy.tgbase.dto.BsDepartmentDto;
import cn.tgxy.tgbase.po.BsDepartment;

public interface BsDepartmentService extends BsBaseService<BsDepartment, BsDepartmentDto>{
	
	boolean checkChild(Long id) throws Exception;

	boolean checkUser(Long id) throws Exception;

}
