package cn.tgxy.oledu.dao;

import cn.tgxy.tgbase.dao.BsBaseDao;
import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.tgxy.oledu.po.OeMember;

public interface OeMemberDao extends BsBaseDao<OeMember, Long>{

	OeMember findByWeixinOpenId(String openId);

	boolean existsByTel(String tel);

	@Modifying
	@Transactional
	@Query("UPDATE OeMember m SET m.tel = :tel WHERE m.id = :id")
	void updateTelById(String tel, Long id);

	boolean existsByCode(String code);

	@Modifying
	@Transactional
	@Query("UPDATE OeMember m SET m.avatar = :avatar WHERE m.id = :id")
	void updateAvatarById(String avatar, Long id);

	@Query("SELECT m.code FROM OeMember m WHERE m.id = :memberId")
	String getCodeById(Long memberId);
	
	@Modifying
	@Transactional
	@Query("UPDATE OeMember m SET m.realNameAuthenticated = :b WHERE m.id = :id")
	void updateRealNameAuthenticatedById(Boolean b, Long id);

}
