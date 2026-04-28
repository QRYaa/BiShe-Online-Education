package cn.tgxy.oledu.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.tgxy.oledu.po.OeMemberIdCardAudit;
import cn.tgxy.tgbase.dao.BsBaseDao;
import jakarta.transaction.Transactional;

public interface OeMemberIdCardAuditDao extends BsBaseDao<OeMemberIdCardAudit, Long>{

	boolean existsByMemberIdAndStatus(Long id, int auditStatusPending);

	@Query("SELECT a FROM OeMemberIdCardAudit a WHERE a.memberId = :id AND a.status !=2  ORDER BY a.id DESC LIMIT 1")
	OeMemberIdCardAudit findByMemberIdAndNotSuccess(Long id);
	
	@Query("SELECT a.status FROM OeMemberIdCardAudit a WHERE a.memberId = :id ORDER BY a.id DESC LIMIT 1")
	int getStatusByMemberId(Long id);

	@Transactional
	@Modifying
	@Query("UPDATE OeMemberIdCardAudit a SET a.status = :status WHERE a.id = :id")
	void updateStatusById(int status, Long id);

	OeMemberIdCardAudit findByIdCardFrontImageUrl(String path);

	OeMemberIdCardAudit findByIdCardBackImageUrl(String path);
	
}
