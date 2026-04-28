package cn.tgxy.oledu.dao;

import java.util.Date;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.tgxy.oledu.po.OeOrder;
import cn.tgxy.tgbase.dao.BsBaseDao;
import jakarta.transaction.Transactional;

public interface OeOrderDao extends BsBaseDao<OeOrder, Long>{

	OeOrder findByCodeAndMemberId(String code,Long memberId);

	@Query("UPDATE OeOrder o SET o.paymentStatus = :paymentStatus WHERE o.id = :id")
	@Modifying
	@Transactional
	void changePaymentStatusById(Long id, Integer paymentStatus);

	@Transactional
	@Modifying
    @Query("UPDATE OeOrder o SET o.paymentStatus = :targetStatus WHERE o.paymentStatus = :originStatus AND o.createdTime < :time")
    int updatePaymentStatusByPaymentStatusAndCreatedTime(Integer targetStatus,Integer originStatus,Date time);

	@Query("SELECT o.paymentStatus FROM OeOrder o WHERE o.memberId = :id AND o.code = :code")
	int getStatusByMemberIdAndCode(Long id, String code);
	
}
