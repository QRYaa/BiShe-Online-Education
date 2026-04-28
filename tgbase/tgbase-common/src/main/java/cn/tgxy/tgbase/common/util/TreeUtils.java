package cn.tgxy.tgbase.common.util;

import cn.hutool.core.util.ReflectUtil;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 树形结构工具类，如：菜单、部门等
 *
 */
public class TreeUtils {

	/**
	 * 构建树节点(必须有parentId, parentName和children属性)
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Object> List<T> build(List<T> treeNodes) {
		List<T> result = new ArrayList<>();
		if (treeNodes != null && treeNodes.size() > 0) {
			// list转成map
			Map<Long, T> treeNodeMap = new LinkedHashMap<>(treeNodes.size());
			for (T treeNode : treeNodes) {
				treeNodeMap.put((Long) ReflectUtil.getFieldValue(treeNode, "id"), treeNode);
			}
			// 拼接树状列表
			for (T treeNode : treeNodeMap.values()) {
				T parent = treeNodeMap.get(ReflectUtil.getFieldValue(treeNode, "parentId"));
				// 判断当前是否所属子类
				if (parent != null) {
					List<T> children = (List<T>) ReflectUtil.getFieldValue(parent, "children");
					if (children == null) {
						children = Lists.newArrayList();
					}
					// 设置父级名称
					ReflectUtil.setFieldValue(treeNode, "parentName", ReflectUtil.getFieldValue(parent, "name"));
					children.add(treeNode);
					ReflectUtil.setFieldValue(parent, "children", children);
					continue;
				}
				result.add(treeNode);
			}
		}
		return result;
	}

}
