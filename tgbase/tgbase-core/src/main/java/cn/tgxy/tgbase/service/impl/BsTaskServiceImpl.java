package cn.tgxy.tgbase.service.impl;

import java.util.List;

import org.quartz.CronExpression;
import org.quartz.JobDataMap;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.tgxy.tgbase.common.exception.ServiceException;
import cn.tgxy.tgbase.constant.BsConstant;
import cn.tgxy.tgbase.constant.BsCoreConstant;
import cn.tgxy.tgbase.constant.BsCoreErrorConstant;
import cn.tgxy.tgbase.dao.BsTaskDao;
import cn.tgxy.tgbase.dto.BsTaskDto;
import cn.tgxy.tgbase.po.BsTask;
import cn.tgxy.tgbase.service.BsTaskService;
import cn.tgxy.tgbase.task.BsScheduleUtils;
import jakarta.annotation.PostConstruct;

@Service
public class BsTaskServiceImpl extends BsBaseServiceImpl<BsTask, BsTaskDto> implements BsTaskService {

	@Autowired
	private Scheduler scheduler;

	@Autowired
	private BsTaskDao timedTaskDao;
	
	@Value("${system.base.cronjobEnabled}")
	private Boolean cronjobEnabled;

	@Override
	@PostConstruct
	public void initBaseDao() {
		this.baseDao = timedTaskDao;
	}

	/**
	 * 项目启动时，初始化定时器 主要是防止手动修改数据库导致未同步到定时任务处理（注：不能手动修改数据库ID和任务组名，否则会导致脏数据）
	 */
	@PostConstruct
	public void init() throws SchedulerException {
		if(cronjobEnabled) {
			scheduler.clear();
			List<BsTask> taskList = timedTaskDao.findAll();
			for (BsTask task : taskList) {
				BsScheduleUtils.createScheduleJob(scheduler, task);
			}
			
		}
	}

	/**
	 * 暂停任务
	 * 
	 * @param taskDto 调度信息
	 * @throws Exception
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void pauseJob(BsTaskDto taskDto) throws Exception {
		Long taskId = taskDto.getId();
		taskDto.setEnable(BsConstant.INVALID);
		taskDto.setStatus(BsConstant.INVALID);
		update(taskDto);

		scheduler.pauseJob(BsScheduleUtils.getJobKey(taskId, null));
	}

	/**
	 * 恢复任务
	 * 
	 * @param taskDto 调度信息
	 * @throws Exception
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void resumeJob(BsTaskDto taskDto) throws Exception {
		Long taskId = taskDto.getId();
		taskDto.setEnable(BsConstant.VALID);
		taskDto.setStatus(BsCoreConstant.WAIT_RUNNING);
		update(taskDto);

		JobKey jobKey = BsScheduleUtils.getJobKey(taskId, null);
		// 判断是否存在
		if (!scheduler.checkExists(jobKey)) {
			BsTask task = new BsTask();
			BeanUtils.copyProperties(taskDto, task);
			BsScheduleUtils.createScheduleJob(scheduler, task);
		} else {
			scheduler.resumeJob(jobKey);
		}
	}

	/**
	 * 删除任务后，所对应的trigger也将被删除
	 * 
	 * @param task 调度信息
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteJob(Long id) throws SchedulerException {
		scheduler.deleteJob(BsScheduleUtils.getJobKey(id, null));
		timedTaskDao.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteJobByIds(List<Long> idList) throws SchedulerException {
		for (Long id : idList) {
			deleteJob(id);
		}
	}

	/**
	 * 修改任务
	 * 
	 * @param task 调度信息
	 * @throws Exception
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateSchedulerJob(BsTaskDto taskDto) throws Exception {
		if (taskDto.getEnable() == BsConstant.INVALID) {
			taskDto.setStatus(BsConstant.INVALID);
		}
		super.update(taskDto);
		Long jobId = taskDto.getId();
		// 判断是否存在
		JobKey jobKey = BsScheduleUtils.getJobKey(jobId, null);
		if (scheduler.checkExists(jobKey)) {
			// 防止创建时存在数据问题 先移除，然后在执行创建操作
			scheduler.deleteJob(jobKey);
		}
		BsTask task = new BsTask();
		BeanUtils.copyProperties(taskDto, task);
		BsScheduleUtils.createScheduleJob(scheduler, task);
	}

	/**
	 * 新增任务
	 * 
	 * @param task 调度信息 调度信息
	 * @throws Exception
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void insertJob(BsTaskDto taskDto) throws Exception {
		taskDto.setEnable(BsConstant.INVALID);
		taskDto.setStatus(BsConstant.INVALID);

		if (!CronExpression.isValidExpression(taskDto.getCron())) {
			throw new ServiceException(BsCoreErrorConstant.CRON_ERROR);
		}

		save(taskDto);
		BsTask task = new BsTask();
		BeanUtils.copyProperties(taskDto, task);
		BsScheduleUtils.createScheduleJob(scheduler, task);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void run(BsTaskDto dto) throws SchedulerException {
		Long jobId = dto.getId();
		BsTask timedTask = new BsTask();
		BeanUtils.copyProperties(dto, timedTask);
		// 参数
		JobDataMap dataMap = new JobDataMap();
		dataMap.put(BsCoreConstant.TASK_PROPERTIES, timedTask);
		JobKey jobKey = BsScheduleUtils.getJobKey(jobId, null);
		if (scheduler.checkExists(jobKey)) scheduler.triggerJob(jobKey, dataMap);
		else BsScheduleUtils.runOneTime(scheduler, timedTask);

	}

}
