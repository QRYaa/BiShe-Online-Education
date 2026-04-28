package cn.tgxy.oledu.service;

import cn.tgxy.tgbase.common.page.PageContent;
import cn.tgxy.tgbase.service.BsBaseService;
import cn.tgxy.oledu.po.OeCourse;
import cn.tgxy.oledu.dto.OeCourseDto;

public interface OeCourseService extends BsBaseService<OeCourse, OeCourseDto>{

	void changeStatus(OeCourseDto dto);

	void switchPaid(OeCourseDto dto);

	void switchPublished(OeCourseDto dto);

	OeCourseDto findByCodeAndEnable(String code) throws Exception;

	OeCourseDto findByIdAndEnable(Long id) throws Exception;

	PageContent<OeCourseDto> findPageData(Long courseTypeId,Long teacherId,String name,Boolean published,Integer status,String courseTypeCode,Long memberId,String teacherCode,int pageNum, int pageSize) throws Exception;

	boolean existsByIdAndEnable(Long id) throws Exception;

	boolean existsByCodeAndEnable(String code) throws Exception;

	Long getIdByCode(String code);

	Long getIdByLessonId(Long lessonId);

	OeCourseDto getSimple(Long id) throws Exception;

}
