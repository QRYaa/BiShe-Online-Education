package cn.tgxy.tgbase.service;

import java.util.List;

import org.quartz.SchedulerException;

import cn.tgxy.tgbase.dto.BsTaskDto;
import cn.tgxy.tgbase.po.BsTask;

public interface BsTaskService extends BsBaseService<BsTask, BsTaskDto>{

	/**
	 * 暂停任务
	 * 
	 * @param task 调度信息
	 * @throws Exception 
	 */
	void pauseJob(BsTaskDto taskDto) throws SchedulerException, Exception;

	/**
	 * 恢复任务
	 * 
	 * @param task 调度信息
	 * @throws Exception 
	 */
	void resumeJob(BsTaskDto taskDto) throws SchedulerException, Exception;

	/**
	 * 删除任务后，所对应的trigger也将被删除
	 * 
	 * @param task 调度信息
	 */
	void deleteJob(Long id) throws SchedulerException;
	
	void deleteJobByIds(List<Long> idList) throws SchedulerException;

	/**
	 * 修改任务
	 * 
	 * @param task 调度信息
	 * @throws Exception 
	 */
	void updateSchedulerJob(BsTaskDto taskDto) throws SchedulerException, Exception;

	/**
	 * 新增任务
	 * 
	 * @param task 调度信息 调度信息
	 * @throws Exception 
	 */
	void insertJob(BsTaskDto taskDto) throws SchedulerException, Exception;

	void run(BsTaskDto dto) throws Exception;

	

	

}
