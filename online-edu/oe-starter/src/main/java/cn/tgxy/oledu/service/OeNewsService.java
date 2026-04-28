package cn.tgxy.oledu.service;

import cn.tgxy.tgbase.service.BsBaseService;
import cn.tgxy.oledu.po.OeNews;
import cn.tgxy.oledu.dto.OeNewsDto;

public interface OeNewsService extends BsBaseService<OeNews, OeNewsDto>{

	OeNewsDto findByCodeAndEnable(String code) throws Exception;

}
