package cn.tgxy.tgbase.task;

/**
 * 任务处理
 * @author Chris Deng
 * @Date 2024/12/19 14:54:56
 */
public interface BsTaskHandler {

	/**
	 * 任务执行
	 * @param param 参数
	 * @return 任务结果
	 * @throws Exception
	 */
    String execute(String param) throws Exception;
}
