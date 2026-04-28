package cn.tgxy.tgbase.common.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PageContent<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4950162036875095057L;

	private List<T> content = new ArrayList<>();

	private long totalItems;
	private int totalPages;
	private int pageNum;
	private int pageSize;


	public PageContent(long totalItems, int pageNum, int pageSize) {
		super();
		this.totalItems = totalItems;
		this.pageNum = pageNum;
		this.pageSize = pageSize;
		countTotalPages();
	}
	
	public PageContent(List<T> content, long totalItems, int totalPages, int pageNum, int pageSize) {
		super();
		if (content != null) {
			this.content.addAll(content);
		}
		this.totalItems = totalItems;
		this.totalPages = totalPages;
		this.pageNum = pageNum;
		this.pageSize = pageSize;
	}

	public PageContent(List<T> content, long totalItems, int pageNum, int pageSize) {
		super();
		if (content != null) {
			this.content.addAll(content);
		}
		this.totalItems = totalItems;
		this.pageNum = pageNum;
		this.pageSize = pageSize;
		countTotalPages();
	}

	private void countTotalPages() {
		if (totalItems == 0 || pageSize == 0) {
			totalPages = 1;
		} else if (totalItems % pageSize == 0) {
			totalPages = (int) (totalItems / pageSize);
		} else {
			totalPages = (int) (totalItems / pageSize) + 1;
		}
	}

	/**
	 * End of line, can be used for Oracle page using the (1-based).
	 **/
	public long getEndRow() {
		return pageNum > 0 ? Math.min(pageSize * pageNum, getTotalItems()) : 0;
	}

	/**
	 * Offset, counting from the beginning of 0, can be used for MySQL page using the (0-based)
	 **/
	public int getOffset() {
		return pageNum > 0 ? (pageNum - 1) * getPageSize() : 0;
	}
	/**
	 * Limit, can be used for MySQL page using the (0-based)
	 **/
	public long getLimit() {
		if (pageNum > 0) {
			return Math.min(pageSize * pageNum, getTotalItems()) - (pageSize * (pageNum - 1));
		} else {
			return 0;
		}
	}
	public List<T> getContent() {
		return content;
	}

	public void setContent(List<T> content) {
		this.content = content;
	}
	public void addAll(List<T> content) {
		this.content.addAll(content);
	}

	public long getTotalItems() {
		return totalItems;
	}

	public void setTotalItems(long totalItems) {
		this.totalItems = totalItems;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

}
