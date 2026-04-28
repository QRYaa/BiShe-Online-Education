package cn.tgxy.oledu.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import cn.tgxy.oledu.dao.OeMemberIdCardAuditExtDao;
import cn.tgxy.oledu.po.OeMemberIdCardAudit;
import cn.tgxy.tgbase.common.page.PageContent;
import cn.tgxy.tgbase.dao.impl.BsExtBaseDaoImpl;

@Repository("oeMemberIdCardAuditExtDao")
public class OeMemberIdCardAuditExtDaoImpl extends BsExtBaseDaoImpl implements OeMemberIdCardAuditExtDao {

    @Override
    public PageContent<OeMemberIdCardAudit> findPageContent(String memberCode, Integer status, String idCardNumber,
            String idCardRealName, Integer pageNum, Integer pageSize) throws Exception {
        String hql = "SELECT a FROM OeMemberIdCardAudit a ";
        Map<String, Object> paramMap = new HashMap<>();

        // 判断是否需要关联会员表
        if (StringUtils.isNotEmpty(memberCode)) {
            hql += "LEFT JOIN OeMember m ON a.memberId = m.id ";
        }

        hql += "WHERE 1=1 ";

        if (StringUtils.isNotEmpty(memberCode)) {
            hql += "AND m.code = :memberCode ";
            paramMap.put("memberCode", memberCode);
        }

        if (status != null) {
            hql += "AND a.status = :status ";
            paramMap.put("status", status);
        }

        if (StringUtils.isNotEmpty(idCardNumber)) {
            hql += "AND a.idCardNumber = :idCardNumber ";
            paramMap.put("idCardNumber", idCardNumber);
        }

        if (StringUtils.isNotEmpty(idCardRealName)) {
            hql += "AND a.idCardRealName = :idCardRealName ";
            paramMap.put("idCardRealName", idCardRealName);
        }

        hql += "ORDER BY a.id DESC";

        return super.findPageContent(OeMemberIdCardAudit.class, hql, pageNum, pageSize, paramMap);
    }
}