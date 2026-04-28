package cn.tgxy.tgbase.task;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import cn.tgxy.tgbase.common.util.spring.SpringContextUtils;
import cn.tgxy.tgbase.constant.BsCoreConstant;
import cn.tgxy.tgbase.dao.BsTaskDao;
import cn.tgxy.tgbase.po.BsTask;
import cn.tgxy.tgbase.po.BsTaskLog;
import cn.tgxy.tgbase.service.BsTaskLogService;

public class BsQuartzJob implements Job {

	private static final Logger log = LoggerFactory.getLogger(BsQuartzJob.class);

	/**
	 * 线程本地变量
	 */
	private static ThreadLocal<Date> threadLocal = new ThreadLocal<>();

	private static BsTaskDao taskDao = (BsTaskDao) SpringContextUtils.getBean(BsTaskDao.class);
	private static BsTaskLogService taskLogService=(BsTaskLogService)SpringContextUtils.getBean(BsTaskLogService.class);

	@Override
	public void execute(JobExecutionContext context) {
		Boolean flag = false;
		BsTask timedTask = new BsTask();
		BeanUtils.copyProperties(context.getMergedJobDataMap().get(BsCoreConstant.TASK_PROPERTIES), timedTask);
		taskDao.updateStatus(BsCoreConstant.RUNNING, timedTask.getId());
		String str = null;
		try {
			before(context, timedTask);
			if (timedTask != null) {
				str = doExecute(context, timedTask);
			}
			after(context, timedTask, str, null);
			flag = true;
		} catch (Exception e) {
			log.error("任务执行异常  - ：", e);
			after(context, timedTask, str, e);
			flag = true;
		} finally {
			if (!flag) {
				Date date = new Date();
				final BsTaskLog taskLog = new BsTaskLog();
				taskLog.setTaskId(timedTask.getId());
				taskLog.setStartTime(threadLocal.get());
				taskLog.setStopTime(date);
				taskLog.setMessage(str);
				taskLog.setResult(BsCoreConstant.RESULT_SUSPEND);
				taskDao.updateLastTime(date, timedTask.getId());
				taskDao.updateLastResult(BsCoreConstant.RESULT_SUSPEND, timedTask.getId());
				taskLogService.save(taskLog);
			}
		}
	}

	/**
	 * 执行前
	 *
	 * @param context   工作执行上下文对象
	 * @param timedTask 系统计划任务
	 */
	protected void before(JobExecutionContext context, BsTask timedTask) {
		threadLocal.set(new Date());
	}

	/**
	 * 执行后
	 *
	 * @param context   工作执行上下文对象
	 * @param timedTask 系统计划任务
	 */
	protected void after(JobExecutionContext context, BsTask timedTask, String str, Exception e) {
		Date startTime = threadLocal.get();
		Date date = new Date();
		threadLocal.remove();

		final BsTaskLog taskLog = new BsTaskLog();
		taskLog.setTaskId(timedTask.getId());
		taskLog.setStartTime(startTime);
		taskLog.setStopTime(date);
		
		taskLog.setMessage(str);
		if (e != null) {
			taskLog.setResult(BsCoreConstant.RESULT_DEFEAT);
			String errorMessage=e.getMessage();
			if(errorMessage.length()>BsCoreConstant.LOG_ERROR_LEN) errorMessage=errorMessage.substring(0, BsCoreConstant.LOG_ERROR_LEN);
			taskLog.setExceptionInfo(errorMessage);
			taskDao.updateLastTime(date, timedTask.getId());
			taskDao.updateLastResult(BsCoreConstant.RESULT_DEFEAT, timedTask.getId());
		} else {
			taskLog.setResult(BsCoreConstant.RESULT_SUCCESS);
			taskDao.updateLastTime(date, timedTask.getId());
			taskDao.updateLastResult(BsCoreConstant.RESULT_SUCCESS, timedTask.getId());
		}
		taskLogService.save(taskLog); 
		taskDao.updateStatus(BsCoreConstant.WAIT_RUNNING, timedTask.getId());
		;
	}

	/**
	 * 
	 * @param context   工作执行上下文对象
	 * @param timedTask 系统计划任务
	 * @throws Exception 执行过程中的异常
	 */
	protected String doExecute(JobExecutionContext context, BsTask timedTask) throws Exception {
		String handlerName = timedTask.getHandlerName();
		String param=timedTask.getParam();
		BsTaskHandler taskHandler=SpringContextUtils.getBean(handlerName, BsTaskHandler.class);
		return taskHandler.execute(param);
	};
}
