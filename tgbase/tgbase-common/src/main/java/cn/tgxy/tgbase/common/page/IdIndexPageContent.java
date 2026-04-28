package cn.tgxy.tgbase.common.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Sort.Direction;

import cn.hutool.core.bean.BeanUtil;

/**
 * 仅限用于基于ID正序或逆序排序！！！
 * 
 * @author Chris Deng
 * @Date 2024/10/23 14:34:39
 */
public class IdIndexPageContent<T> implements Serializable {

	private static final long serialVersionUID = 6960162036875095057L;
	private List<T> content = new ArrayList<>();

	private int count = 0; // 数据条数

	private Direction direction = Direction.ASC; // 排序方向，默认升序

	private Long minId = 0L; // 最小的ID

	private Long maxId = 0L; // 最大的ID

	public IdIndexPageContent(List<T> content, Direction direction) {
		super();
		this.direction = direction;
		if (content != null) {
			this.content.addAll(content);
			this.count = this.content.size();
			if(count > 0) {
				if (Direction.DESC == direction) {
					// 逆序
					T maxPo = content.get(0);
					Long maxId = (Long) BeanUtil.getFieldValue(maxPo, "id");
					this.maxId = maxId;
					
					T minPo = content.get(count-1);
					Long minId = (Long) BeanUtil.getFieldValue(minPo, "id");
					this.minId = minId;
					
				} else {				
					// 正序
					T maxPo = content.get(count-1);
					Long maxId = (Long) BeanUtil.getFieldValue(maxPo, "id");
					this.maxId = maxId;
					
					T minPo = content.get(0);
					Long minId = (Long) BeanUtil.getFieldValue(minPo, "id");
					this.minId = minId;

				}
			}
		}
		
	}

	public List<T> getContent() {
		return content;
	}

	public void setContent(List<T> content) {
		this.content = content;
	}


	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void addAll(List<T> content) {
		this.content.addAll(content);
	}

	public Long getMinId() {
		return minId;
	}

	public void setMinId(Long minId) {
		this.minId = minId;
	}

	public Long getMaxId() {
		return maxId;
	}

	public void setMaxId(Long maxId) {
		this.maxId = maxId;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}
}
