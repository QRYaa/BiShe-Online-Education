package cn.tgxy.tgbase.task;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;

import cn.tgxy.tgbase.constant.BsConstant;
import cn.tgxy.tgbase.constant.BsCoreConstant;
import cn.tgxy.tgbase.po.BsTask;

/**
 * 定时任务工具类
 * 
 * 
 *
 */
public class BsScheduleUtils {

	/**
	 * 得到quartz任务类
	 *
	 * @param BsTask 执行计划
	 * @return 具体执行任务类
	 */
	private static Class<? extends Job> getQuartzJobClass() {
		return BsQuartzJob.class;
	}

	/**
	 * 构建任务触发对象
	 */
	public static TriggerKey getTriggerKey(Long jobId, String jobGroup) {
		return TriggerKey.triggerKey(BsCoreConstant.TASK_CLASS_NAME + jobId, jobGroup);
	}

	/**
	 * 构建任务键对象
	 */
	public static JobKey getJobKey(Long jobId, String jobGroup) {
		return JobKey.jobKey(BsCoreConstant.TASK_CLASS_NAME + jobId, jobGroup);
	}
	
	/**
	 * 构建临时任务触发对象
	 */
	private static TriggerKey getTempTriggerKey(Long jobId, String jobGroup) {
		return TriggerKey.triggerKey(BsCoreConstant.TASK_CLASS_NAME + "temp" + jobId, jobGroup);
	}

	/**
	 * 构建临时任务键对象
	 */
	private static JobKey getTempJobKey(Long jobId, String jobGroup) {
		return JobKey.jobKey(BsCoreConstant.TASK_CLASS_NAME + "temp" + jobId, jobGroup);
	}

	/**
	 * 创建定时任务
	 */
	public static void createScheduleJob(Scheduler scheduler, BsTask job) throws SchedulerException {
		Class<? extends Job> jobClass = getQuartzJobClass();
		// 构建job信息
		Long jobId = job.getId();

		JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(getJobKey(jobId, null)).build();

		// 表达式调度构建器
		CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCron());

		// 按新的cronExpression表达式构建一个新的trigger
		CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(getTriggerKey(jobId, null))
				.withSchedule(cronScheduleBuilder).build();

		// 放入参数，运行时的方法可以获取
		jobDetail.getJobDataMap().put(BsCoreConstant.TASK_PROPERTIES, job);

		// 判断是否存在
		if (scheduler.checkExists(getJobKey(jobId, null))) {
			// 防止创建时存在数据问题 先移除，然后在执行创建操作
			scheduler.deleteJob(getJobKey(jobId, null));
		}

		// 启用且未过期
		if (job.getEnable().equals(BsConstant.VALID)) {
			if (BsCronExpressionUtils.getNextExecution(job.getCron()) != null) {
				scheduler.scheduleJob(jobDetail, trigger);
			}
		}
	}

	/**
	 * 执行一次定时任务
	 */
	public static void runOneTime(Scheduler scheduler, BsTask job) throws SchedulerException {
		Class<? extends Job> jobClass = getQuartzJobClass();
		// 构建job信息
		Long jobId = job.getId();
		TriggerKey tempTriggerKey=getTempTriggerKey(jobId, null);
		JobKey tempJobKey=getTempJobKey(jobId, null);
		JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(tempJobKey).build();
		// 放入参数，运行时的方法可以获取
		jobDetail.getJobDataMap().put(BsCoreConstant.TASK_PROPERTIES, job);
		Trigger trigger = TriggerBuilder.newTrigger().forJob(tempJobKey)
				.withIdentity(tempTriggerKey).startNow().usingJobData(jobDetail.getJobDataMap()).build();
		// 判断是否存在
		if (scheduler.checkExists(tempJobKey)) {
			// 防止创建时存在数据问题 先移除，然后在执行创建操作
			scheduler.deleteJob(tempJobKey);
		}
		scheduler.scheduleJob(jobDetail, trigger);
		// 任务执行完成后删除任务（确保任务只执行一次）
		scheduler.triggerJob(tempJobKey, jobDetail.getJobDataMap());
	    scheduler.unscheduleJob(tempTriggerKey);
	    scheduler.deleteJob(tempJobKey);
	}

}
