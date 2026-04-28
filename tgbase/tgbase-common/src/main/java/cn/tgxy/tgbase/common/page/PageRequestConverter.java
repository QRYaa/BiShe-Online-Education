package cn.tgxy.tgbase.common.page;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * @author Chris Deng
 * @Date 2024/07/25 11:13:24
 */
public class PageRequestConverter {
	
	/**
	 * Spring data的第一页是0，与前端的第一页为1不一致
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public static Pageable of(int pageNumber, int pageSize) {
		return PageRequest.of(pageNumber-1, pageSize);
	}
	public static Pageable of(int pageNumber, int pageSize, Sort sort) {
		return PageRequest.of(pageNumber-1, pageSize, sort);
	}

}
