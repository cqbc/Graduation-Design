package com.cqupt.common.statics;

import java.util.HashMap;
import java.util.Map;

import com.cqupt.common.enums.TaskLevelEnum;

public class StaticValue4RelationMap {
	/** 优先级与循环周期的对应关系，Map中的key为任务级别，field为循环周期 */
	public static Map<TaskLevelEnum, Integer> levelToCircleTimeMap = new HashMap<TaskLevelEnum, Integer>();
	public static TaskLevelEnum[] taskLevelEnumArray = null;
	public static TaskLevelEnum taskLevelDefault = null;
	static {
		// 分钟为单位，即多少分钟循环一次,暂设置成12个级别
		levelToCircleTimeMap.put(TaskLevelEnum.A, 10);
		levelToCircleTimeMap.put(TaskLevelEnum.B, 30);
		levelToCircleTimeMap.put(TaskLevelEnum.C, 60);
		levelToCircleTimeMap.put(TaskLevelEnum.D, 3 * 60);
		levelToCircleTimeMap.put(TaskLevelEnum.E, 6 * 60);
		levelToCircleTimeMap.put(TaskLevelEnum.F, 12 * 60);
		levelToCircleTimeMap.put(TaskLevelEnum.G, 24 * 60);

		// 代表从不更新
		levelToCircleTimeMap.put(TaskLevelEnum.Z, 0 * 60);

		taskLevelDefault = TaskLevelEnum.B;
	}

	/** 规则key对应的数据库中字段的映身,Map中的key为ruleKey，field为字段 */
	public static Map<String, String> ruleKeyToFieldMap = new HashMap<String, String>();

	static {
		// 分钟为单位，即多少分钟循环一次,暂设置成12个级别
		ruleKeyToFieldMap.put("标题", "title");
		ruleKeyToFieldMap.put("作者", "author");
		ruleKeyToFieldMap.put("评论数", "discuss_number");
		ruleKeyToFieldMap.put("转发数", "transmit_number");
		// 因为发布时间多样化，故暂以字符串来保存
		ruleKeyToFieldMap.put("发布时间", "publish_time_string");

	}

	/**
	 * 获取任务级别对应的循环周期，单位为分钟
	 * 
	 * @param taskLevelEnum
	 *            任务级别
	 * @return 任务级别对应的循环周期，单位为分钟
	 */

	public static int getLevelToCircleTime(TaskLevelEnum taskLevelEnum) {
		return levelToCircleTimeMap.get(taskLevelEnum);
	}

	public static int getLevelToCircleTime(String taskLevelString) {
		return getLevelToCircleTime(getTaskLevelEnumByString(taskLevelString));
	}

	public static TaskLevelEnum getTaskLevelEnumByString(String taskLevelString) {
		TaskLevelEnum task_level = null;
		try {
			task_level = TaskLevelEnum.valueOf(taskLevelString);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("task level类型由字符串转化为枚举时出错，请检查！暂以默认值替代!");
			task_level = taskLevelDefault;
		}
		return task_level;
	}

	public static void main(String[] args) {
		String str = "A";
		System.out.println(getLevelToCircleTime(str));
	}
}
