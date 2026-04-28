package cn.tgxy.oledu.dao;

import org.springframework.data.jpa.repository.Query;

import cn.tgxy.oledu.po.OeMemberIdCard;
import cn.tgxy.tgbase.dao.BsBaseDao;

public interface OeMemberIdCardDao extends BsBaseDao<OeMemberIdCard, Long>{

	OeMemberIdCard findByMemberId(Long memberId);

	@Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
		       "FROM OeMemberIdCard c " +
		       "WHERE c.number = :number AND c.memberId <> :memberId")
	boolean existsByNumberButMemberId(String number, Long memberId);

}
