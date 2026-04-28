package cn.tgxy.oledu.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import cn.tgxy.oledu.dao.OeMemberExtDao;
import cn.tgxy.oledu.po.OeMember;
import cn.tgxy.tgbase.common.page.PageContent;
import cn.tgxy.tgbase.dao.impl.BsExtBaseDaoImpl;

@Repository("oeMemberExtDao")
public class OeMemberExtDaoImpl extends BsExtBaseDaoImpl implements OeMemberExtDao {

    @Override
    public PageContent<OeMember> findPageContent(String name, String code, String tel, Integer gender, Integer areaId,
            Integer enable, String idCardName, String idCardNumber, Integer pageNum, Integer pageSize)
            throws Exception {
        String hql = "SELECT m FROM OeMember m ";
        Map<String, Object> paramMap = new HashMap<>();

        boolean needIdCardJoin = StringUtils.isNotEmpty(idCardName) || StringUtils.isNotEmpty(idCardNumber);
        if (needIdCardJoin) {
            hql += "LEFT JOIN OeMemberIdCard ic ON m.id=ic.memberId ";
        }

        hql += "WHERE 1=1 ";

        if (StringUtils.isNotEmpty(name)) {
            hql += "AND m.name LIKE :name ";
            paramMap.put("name", "%" + name + "%");
        }
        if (StringUtils.isNotEmpty(code)) {
            hql += "AND m.code = :code ";
            paramMap.put("code", code);
        }
        if (StringUtils.isNotEmpty(tel)) {
            hql += "AND m.tel = :tel ";
            paramMap.put("tel", tel);
        }
        if (gender != null) {
            hql += "AND m.gender = :gender ";
            paramMap.put("gender", gender);
        }
        if (areaId != null) {
            hql += "AND m.areaId = :areaId ";
            paramMap.put("areaId", areaId);
        }
        if (enable != null) {
            hql += "AND m.enable = :enable ";
            paramMap.put("enable", enable);
        }

        if (StringUtils.isNotEmpty(idCardName)) {
            hql += "AND ic.realName = :idCardName ";
            paramMap.put("idCardName", idCardName);
        }
        if (StringUtils.isNotEmpty(idCardNumber)) {
            hql += "AND ic.number = :idCardNumber ";
            paramMap.put("idCardNumber", idCardNumber);
        }

        hql += "ORDER BY m.id DESC";

        return super.findPageContent(OeMember.class, hql, pageNum, pageSize, paramMap);
    }
}
