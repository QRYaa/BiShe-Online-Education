package cn.tgxy.oledu.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.houbb.sensitive.word.bs.SensitiveWordBs;

import cn.tgxy.oledu.dao.OeSensitiveWordDao;
import cn.tgxy.oledu.dto.OeSensitiveWordDto;
import cn.tgxy.oledu.po.OeSensitiveWord;
import cn.tgxy.oledu.service.OeSensitiveWordService;
import cn.tgxy.tgbase.service.impl.BsBaseServiceImpl;
import jakarta.annotation.PostConstruct;

@Service
public class OeSensitiveWordServiceImpl extends BsBaseServiceImpl<OeSensitiveWord, OeSensitiveWordDto> implements OeSensitiveWordService{

	@Autowired
	private OeSensitiveWordDao sensitiveWordDao;
	
	@Autowired
	private SensitiveWordBs sensitiveWordBs;

	@Override
	@PostConstruct
	public void initBaseDao() {
		this.baseDao = sensitiveWordDao;
		List<String> wordList=sensitiveWordDao.findAllWords();
		sensitiveWordBs.addWord(wordList);
	}
	
	@Override
	public void save(OeSensitiveWordDto dto) throws Exception {
		dto.setCreateTime(new Date());
		sensitiveWordBs.addWord(dto.getSensitiveWord());
		super.save(dto);
	}
	
	@Override
	public void deleteById(Long id) throws Exception {
		OeSensitiveWord sensitiveWord=sensitiveWordDao.findById(id).orElse(null);
		if(sensitiveWord==null) {
			return;
		}
		sensitiveWordBs.removeWord(sensitiveWord.getSensitiveWord());
		super.deleteById(id);
	}
	
	@Override
	public void deleteByIdInBatch(List<Long> ids) throws Exception {
		for(Long id:ids) {
			OeSensitiveWord sensitiveWord=sensitiveWordDao.findById(id).orElse(null);
			if(sensitiveWord==null) {
				continue;
			}
			sensitiveWordBs.removeWord(sensitiveWord.getSensitiveWord());
		}
		super.deleteByIdInBatch(ids);
	}
	
	@Override
	public void update(OeSensitiveWordDto dto) throws Exception {
		OeSensitiveWord sensitiveWord=sensitiveWordDao.findById(dto.getId()).orElse(null);
		if(sensitiveWord==null) {
			return;
		}
		if(!dto.getSensitiveWord().equals(sensitiveWord.getSensitiveWord())) {
			sensitiveWordBs.addWord(dto.getSensitiveWord());
			sensitiveWordBs.removeWord(sensitiveWord.getSensitiveWord());
		}
		super.update(dto);
	}
	
}
