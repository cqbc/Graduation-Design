package com.cqupt.common.utils;

import java.util.LinkedList;
import java.util.List;

import com.cqupt.common.enums.CrawlTypeEnum;
import com.cqupt.common.enums.TaskTypeEnum;
import com.cqupt.common.statics.StaticValue4RelationMap;
import com.cqupt.common.statics.StaticValueOfRule;
import com.cqupt.common.statics.SystemParasSpider;
import com.cqupt.spider.pojos.CrawlTaskPojo;
 
/** 
 * 文件行串到对象之间的转换 
 * 
 * @author zel
 * 
 */
public class TaskTxt2ObjectUtil {
	public static List<CrawlTaskPojo> convertTxt2Object(List<String> txtContent) {
		if (StringOperatorUtil.isBlankCollection(txtContent)) {
			return null;
		}
		String[] strArray = null;
		List<CrawlTaskPojo> taskList = new LinkedList<CrawlTaskPojo>();
		CrawlTaskPojo taskPojo = null;
		// 遍历每一行
		for (String line : txtContent) {
			strArray = line.split(StaticValueOfRule.separator_tab);
			// 种子中每个url的tab分隔的长度为4、5、8
			if (strArray.length == 4) {
				taskPojo = new CrawlTaskPojo();
				
				taskPojo.setTitle(strArray[0]);
				taskPojo.setValue(strArray[1]);
				try {
					taskPojo.setMedia_type(Integer.parseInt(strArray[2]));
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("种子行---" + line
							+ ",出现不合理值，请检查，该种子记录将略过!");
					continue;
				}

				// 进行任务类别判断
				if (strArray[3].toLowerCase().equals(
						CrawlTypeEnum.MetaSearch_NEWSPage.toString()
								.toLowerCase())) {
					taskPojo.setType(TaskTypeEnum.Keyword);
					taskPojo.setCrawlEngine(CrawlTypeEnum.MetaSearch_NEWSPage);
				} else {
					taskPojo.setType(TaskTypeEnum.Url);
					taskPojo.setCrawlEngine(CrawlTypeEnum.WebPage_Url);
				}
				
				taskPojo.setLevel(StaticValue4RelationMap.taskLevelDefault);
				taskPojo.setDepth(SystemParasSpider.depth);
				taskPojo.setCurrent_depth(0);
				taskPojo.setTopN(SystemParasSpider.topN);
				taskPojo.setSource_title(taskPojo.getTitle()+"_"+taskPojo.getValue());

				taskList.add(taskPojo);
			} else if (strArray.length == 5) {
				taskPojo = new CrawlTaskPojo();

				taskPojo.setTitle(strArray[0]);
				taskPojo.setValue(strArray[1]);
				try {
					taskPojo.setMedia_type(Integer.parseInt(strArray[2]));
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("种子行---" + line
							+ ",出现不合理值，请检查，该种子记录将略过!");
					continue;
				}
				
				// 进行任务类别判断
				if (strArray[3].toLowerCase().equals(
						CrawlTypeEnum.MetaSearch_NEWSPage.toString()
								.toLowerCase())) {
					taskPojo.setType(TaskTypeEnum.Keyword);
					taskPojo.setCrawlEngine(CrawlTypeEnum.MetaSearch_NEWSPage);
				} else {
					taskPojo.setType(TaskTypeEnum.Url);
					taskPojo.setCrawlEngine(CrawlTypeEnum.WebPage_Url);
				}
				// task level枚举转换
				taskPojo.setLevel(StaticValue4RelationMap
						.getTaskLevelEnumByString(strArray[4].toUpperCase()));
				
				taskPojo.setDepth(SystemParasSpider.depth);
				taskPojo.setCurrent_depth(0);
				taskPojo.setTopN(SystemParasSpider.topN);
				taskPojo.setSource_title(taskPojo.getTitle()+"_"+taskPojo.getValue());
				
				taskList.add(taskPojo);
			} else if (strArray.length == 7) {
				taskPojo = new CrawlTaskPojo();
				
				taskPojo.setTitle(strArray[0]);
				taskPojo.setValue(strArray[1]);
				try {
					taskPojo.setMedia_type(Integer.parseInt(strArray[2]));
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("种子行---" + line
							+ ",出现不合理值，请检查，该种子记录将略过!");
					continue;
				}
				
				if (strArray[3].toLowerCase().equals(
						CrawlTypeEnum.MetaSearch_NEWSPage.toString()
								.toLowerCase())) {
					taskPojo.setType(TaskTypeEnum.Keyword);
					taskPojo.setCrawlEngine(CrawlTypeEnum.MetaSearch_NEWSPage);
				} else {
					taskPojo.setType(TaskTypeEnum.Url);
					taskPojo.setCrawlEngine(CrawlTypeEnum.WebPage_Url);
				}

				// task level枚举转换
				taskPojo.setLevel(StaticValue4RelationMap
						.getTaskLevelEnumByString(strArray[4].toUpperCase()));

				try {
					taskPojo.setDepth(Integer.parseInt(strArray[5]));
				} catch (Exception e) {
					e.printStackTrace();
					taskPojo.setDepth(SystemParasSpider.depth);
				}
				
				try {
					taskPojo.setTopN(Integer.parseInt(strArray[6]));
				} catch (Exception e) {
					e.printStackTrace();
					taskPojo.setTopN(SystemParasSpider.topN);
				}

				taskPojo.setCurrent_depth(0);
				taskPojo.setSource_title(taskPojo.getTitle()+"_"+taskPojo.getValue());
				
				taskList.add(taskPojo);
			}
		}
		
		return taskList;
	}

	public static void main(String[] args) {
		System.out.println(CrawlTypeEnum.MetaSearch_NEWSPage.toString());
		System.out.println(StaticValue4RelationMap.taskLevelDefault);
	}

}
