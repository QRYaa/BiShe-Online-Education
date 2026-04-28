package cn.tgxy.oledu.service.impl;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.houbb.sensitive.word.bs.SensitiveWordBs;

import cn.tgxy.oledu.constant.OeConstant;
import cn.tgxy.oledu.constant.OeErrorConstant;
import cn.tgxy.oledu.dao.OeChapterDao;
import cn.tgxy.oledu.dao.OeCourseDao;
import cn.tgxy.oledu.dao.OeLessonDao;
import cn.tgxy.oledu.dao.OeMemberDao;
import cn.tgxy.oledu.dao.OeMemberLessonDao;
import cn.tgxy.oledu.dao.OeNoteDao;
import cn.tgxy.oledu.dao.OeNoteExtDao;
import cn.tgxy.oledu.dto.OeMemberDto;
import cn.tgxy.oledu.dto.OeNoteDto;
import cn.tgxy.oledu.po.OeChapter;
import cn.tgxy.oledu.po.OeLesson;
import cn.tgxy.oledu.po.OeMember;
import cn.tgxy.oledu.po.OeNote;
import cn.tgxy.oledu.service.OeCourseService;
import cn.tgxy.oledu.service.OeMemberCourseService;
import cn.tgxy.oledu.service.OeNoteService;
import cn.tgxy.tgbase.common.exception.ServiceException;
import cn.tgxy.tgbase.common.page.PageContent;
import cn.tgxy.tgbase.service.impl.BsBaseServiceImpl;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Service
public class OeNoteServiceImpl extends BsBaseServiceImpl<OeNote, OeNoteDto> implements OeNoteService {

	@Autowired
	private OeNoteDao noteDao;

	@Autowired
	private OeNoteExtDao noteExtDao;

	@Autowired
	private OeMemberDao memberDao;

	@Autowired
	private OeLessonDao lessonDao;
	
	@Autowired
	private OeChapterDao chapterDao;

	@Autowired
	private OeCourseDao courseDao;
	
	@Autowired
	private OeMemberLessonDao memberLessonDao;

	@Autowired
	private OeCourseService courseService;

	@Autowired
	private OeMemberCourseService memberCourseService;

	@Autowired
	private SensitiveWordBs sensitiveWordBs;
	
	@Override
	@PostConstruct
	public void initBaseDao() {
		this.baseDao = noteDao;
	}

	@Override
	public PageContent<OeNoteDto> findPageData(Date startTime, Date endTime, Integer status, Boolean enable,
			String memberCode, String courseName, String lessonName,Long memberId,Long lessonId, Long courseId,Integer pageNum, Integer pageSize)
			throws Exception {
		PageContent<OeNote> pageContent = noteExtDao.findPageContent(startTime, endTime, status, enable, memberCode,
				courseName, lessonName, memberId,lessonId,courseId,pageNum, pageSize);
		return new PageContent<OeNoteDto>(transToDtoList(pageContent.getContent()), pageContent.getTotalItems(),
				pageNum, pageSize);
	}

	@Override
	public OeNoteDto transToDto(OeNote note) throws Exception {
		OeNoteDto noteDto = super.transToDto(note);
		OeMember member = memberDao.findById(noteDto.getMemberId()).orElse(null);
		if (member != null) {
			noteDto.setMemberAvatar(member.getAvatar());
			noteDto.setMemberName(member.getName());
		}
		OeLesson lesson = lessonDao.findById(noteDto.getLessonId()).orElse(null);
		if (lesson != null) {
			noteDto.setLessonName(lesson.getName());
			noteDto.setLessonSort(lesson.getSort());
		}
		OeChapter chapter=chapterDao.findById(lesson.getChapterId()).orElse(null);
		if(chapter!=null) {
			noteDto.setChapterName(chapter.getName());
			noteDto.setChapterSort(chapter.getSort());
		}
		Long courseId = courseService.getIdByLessonId(noteDto.getLessonId());
		if (courseId != null) {
			noteDto.setCourseName(courseDao.getNameById(courseId));
		}
		return noteDto;
	}

	@Override
	public OeNoteDto transToDtoDetail(OeNote note) throws Exception {
		OeNoteDto noteDto = super.transToDto(note);
		OeMember member = memberDao.findById(noteDto.getMemberId()).orElse(null);
		if (member != null) {
			noteDto.setMemberAvatar(member.getAvatar());
			noteDto.setMemberName(member.getName());
		}
		OeLesson lesson = lessonDao.findById(noteDto.getLessonId()).orElse(null);
		if (lesson != null) {
			noteDto.setLessonName(lesson.getName());
			noteDto.setLessonSort(lesson.getSort());
		}
		OeChapter chapter=chapterDao.findById(lesson.getChapterId()).orElse(null);
		if(chapter!=null) {
			noteDto.setChapterName(chapter.getName());
			noteDto.setChapterSort(chapter.getSort());
		}
		Long courseId = courseService.getIdByLessonId(noteDto.getLessonId());
		if (courseId != null) {
			noteDto.setCourseName(courseDao.getNameById(courseId));
		}
		return noteDto;
	}

	@Override
	public void switchEnable(OeNoteDto noteDto) {
		noteDao.updateEnableById(!noteDto.getEnable(), noteDto.getId());
	}

	// ------------------------------------Toc------------------------------------
	@Override
	public String createNote(OeMemberDto memberDto, OeNoteDto noteDto) throws Exception {
		// 不在Mc表的学员不能创建笔记
		memberCourseService.checkMemberIdAndLessonId(memberDto.getId(), noteDto.getLessonId());
		// 检测有无敏感词
		if (sensitiveWordBs.contains(noteDto.getContent())) {
			throw new ServiceException(OeErrorConstant.CONTAINS_SENSITIVE_WORDS);
		}
		if(StringUtils.isEmpty(noteDto.getContent())){
			throw new ServiceException(OeErrorConstant.CONTENT_EMPTY);
		}
		OeNote note=new OeNote();
		note.setMemberId(memberDto.getId());
		note.setLessonId(noteDto.getLessonId());
		note.setContent(noteDto.getContent());
		note.setCreateTime(new Date());
		note.setStatus(noteDto.getStatus());
		note.setEnable(true);
		noteDao.save(note);
		
		//memberCourse的noteNum属性加1
		Long memberCourseId=memberLessonDao.getMemberCourseIdByLessonId(noteDto.getLessonId());
		memberCourseService.addNoteNumById(memberCourseId);
		return note.getId().toString();
	}
	
	@Override
	@Transactional
	public OeNoteDto getDetail(Long noteId,OeMemberDto currentMemberDto) throws Exception {
		OeNoteDto noteDto=get(noteId);
		if(noteDto==null) return null;
		if(noteDto.getEnable().equals(false) || (!noteDto.getStatus().equals(OeConstant.NOTE_PUB) && !noteDto.getMemberId().equals(currentMemberDto.getId()))) {
			return null;
		}
		return noteDto;
	}
	
	@Override
	@Transactional
	public void enableNotByIdAndMemberId(Long id,Long memberId) throws Exception {
		noteDao.enableNotByIdAndMemberId(id,memberId);
		Long lessonId=noteDao.getLessonIdById(id);
		Long memberCourseId=memberLessonDao.getMemberCourseIdByLessonId(lessonId);
		memberCourseService.subtractNoteNumById(memberCourseId);
	}
}
