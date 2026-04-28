package cn.tgxy.tgbase.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tgxy.tgbase.common.exception.ServiceException;
import cn.tgxy.tgbase.constant.BsCoreErrorConstant;
import cn.tgxy.tgbase.dao.BsDictDao;
import cn.tgxy.tgbase.dto.BsDictDto;
import cn.tgxy.tgbase.dto.BsDictItemDto;
import cn.tgxy.tgbase.po.BsDict;
import cn.tgxy.tgbase.service.BsDictItemService;
import cn.tgxy.tgbase.service.BsDictService;
import jakarta.annotation.PostConstruct;

@Service
public class BsDictServiceImpl extends BsBaseServiceImpl<BsDict, BsDictDto> implements BsDictService {

	@Autowired
	private BsDictDao dictDao;

	@Autowired
	private BsDictItemService dictItemService;

	@Override
	@PostConstruct
	public void initBaseDao() {
		this.baseDao = dictDao;
	}

	@Override
	public BsDictDto transToDtoDetail(BsDict entity) throws Exception {
		BsDictDto dto = super.transToDtoDetail(entity);
		if (dto != null) {
			List<BsDictItemDto> list = dictItemService.list(dto.getId());
			dto.setItems(list);
		}
		return dto;
	}

	@Override
	public BsDictDto transToDto(BsDict entity) throws Exception {
		return this.transToDtoDetail(entity);
	}

	@Override
	public BsDictDto findByCode(String code) throws Exception {
		BsDict dict = dictDao.findByCode(code);
		return transToDtoDetail(dict);
	}

	@Override
	public void save(BsDictDto dto) throws Exception {
		if (dictDao.existsByCode(dto.getCode())) {
			throw new ServiceException(BsCoreErrorConstant.DICTIONARY_CODE_EXIST);
		}
		super.save(dto);
	}

	@Override
	public void update(BsDictDto dto) throws Exception {
		Long id = dto.getId();
		if (id != null) {
			BsDict oDict = dictDao.findById(id).get();
			if (!oDict.getCode().equals(dto.getCode())) {
				if (dictDao.existsByCode(dto.getCode())) {
					throw new ServiceException(BsCoreErrorConstant.DICTIONARY_CODE_EXIST);
				}
			}
			super.update(dto);
		}
	}

	@Override
	public void deleteById(Long id) throws Exception {
		dictItemService.deleteByDictId(id);
		super.deleteById(id);
	}

	@Override
	public void deleteByIdInBatch(List<Long> ids) throws Exception {
		for (Long id : ids) {
			this.deleteById(id);
		}
	}

}
