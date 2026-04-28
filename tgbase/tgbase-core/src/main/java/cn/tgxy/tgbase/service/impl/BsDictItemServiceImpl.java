package cn.tgxy.tgbase.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import cn.tgxy.tgbase.common.exception.ServiceException;
import cn.tgxy.tgbase.constant.BsCoreErrorConstant;
import cn.tgxy.tgbase.dao.BsDictItemDao;
import cn.tgxy.tgbase.dto.BsDictItemDto;
import cn.tgxy.tgbase.po.BsDictItem;
import cn.tgxy.tgbase.service.BsDictItemService;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Service
public class BsDictItemServiceImpl extends BsBaseServiceImpl<BsDictItem, BsDictItemDto> implements BsDictItemService {

	@Autowired
	private BsDictItemDao dictItemDao;

	@Override
	@PostConstruct
	public void initBaseDao() {
		this.baseDao = dictItemDao;
	}

	@Override
	public void save(BsDictItemDto dto) throws Exception {
		if (dictItemDao.existsByDictIdAndItemKey(dto.getDictId(), dto.getItemKey())) {
			throw new ServiceException(BsCoreErrorConstant.DICTIONARY_ITEM_CODE_EXIST);
		}
		super.save(dto);
	}

	@Override
	public void update(BsDictItemDto dto) throws Exception {
		Long id = dto.getId();
		if (id != null) {
			BsDictItem originalDictItem = dictItemDao.findById(id).get();
			// 当itemKey发生改变时，才判断itemKey是否已存在
			if(!originalDictItem.getItemKey().equals(dto.getItemKey())) {
				if (dictItemDao.existsByDictIdAndItemKey(dto.getDictId(), dto.getItemKey())) {
					throw new ServiceException(BsCoreErrorConstant.DICTIONARY_ITEM_CODE_EXIST);
				}
			}
			super.update(dto);
		}
	}

	@Override
	public List<BsDictItemDto> list(Long dictId) throws Exception {
		List<BsDictItem> list = dictItemDao.findByDictIdAndSort(dictId, Sort.by("sort").descending());
		return transToDtoList(list);
	}

	@Override
	@Transactional
	public void deleteByDictId(Long dictId) throws Exception {
		dictItemDao.deleteByDictId(dictId);
	}

}
