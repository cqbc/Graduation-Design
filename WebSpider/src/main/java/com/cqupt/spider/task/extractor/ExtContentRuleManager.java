package com.cqupt.spider.task.extractor;

import com.cqupt.common.statics.StaticValue;
import com.cqupt.common.statics.SystemParasSpider;
import com.cqupt.common.utils.ReadConfigUtil;
import com.vaolan.extkey.utils.IOUtil;

/**
 * 规则配置管理器
 * 
 */
public class ExtContentRuleManager {
	private String ruleString;

	public String getRuleString() {
		return ruleString;
	} 

	public void setRuleString(String ruleString) {
		this.ruleString = ruleString;
	}

	public ExtContentRuleManager(String rule_file_location_fs,
			String file_path) {
		if ("in".equals(rule_file_location_fs)) {
			// 目前只支持jar包内的文件读取
			ReadConfigUtil readConfigUtil = new ReadConfigUtil(file_path, false);
			this.ruleString = readConfigUtil.getLineConfigTxt();
		} else if ("out".equals(rule_file_location_fs)) {
			this.ruleString = IOUtil.readDirOrFile(file_path,
					StaticValue.default_encoding);
		} else {
			try {
				throw new Exception(
						"ExtContentRuleConfigManager 参数的传入方式有问题，请检查!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		// JedisOperatorUtil jedisOperatorUtil= new
		// JedisOperatorUtil(SystemParas.redis_host,
		// SystemParas.redis_port,SystemParas.redis_password);
		// String str =
		// jedisOperatorUtil.getObj(StaticValue.ext_content_rule_key);

		ExtContentRuleManager extContentRuleConfigManager = new ExtContentRuleManager(
				SystemParasSpider.ext_content_rule_config_fs,
				SystemParasSpider.ext_content_rule_config_root_dir);
		
		System.out.println(extContentRuleConfigManager.ruleString);
	}
}
