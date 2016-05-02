package com.cqupt.common.statics;

public class StaticValueOfRule {
	/**
	 * 符号定义
	 */
	public static String separator_tab = "\t";
	public static String separator_dot = ",";
	public static String separator_next_line = "\n";
	public static String separator_space = " ";
	public static String separator_file_path = "/";
	public static String separator_whitespace = " ";
	public static String separator_left_bracket = "(";
	public static String separator_right_bracket = ")";

	// http parasmeter
	public static String prefix_http = "http://";
	
	/**
	 * 关于规则库中的分隔符的定义
	 * 解释性语句,除第一类外，其它行是不能出现tab的。#split_big#是最大的分隔符,#=>#是第二分隔符,其前是jsoup的格式
	 * ，其后是正则表达式,#split_small#是第三分隔符 其中的#split_union#是表示前后两个正则匹配是等同层次的
	 */
	public static String rule_file_split_big = "#split_big#";
	public static String rule_file_split_block_index = com.vaolan.utils.StaticValue.split_block_index;
	public static String rule_file_split_regex = "#=>#";
	public static String rule_file_split_union = "#split_union#";
	public static String rule_file_split_small = "#split_small#";
	public static String rule_file_split_paras = "#paras#";

	
	/**
	 * 专为解决网页编码提取而添加
	 */
	// 单点定义
	public static final char point = '.';
	public static int url_data_min_byte_length = 500;

	/**
	 * 以下是对网页的charset下的charset相应定义
	 */
	// 默认编码方式
	public static final String SYSTEM_ENCODING = "utf-8";
	// 默认gbk中文的处理编码
	public static final String GBK_ENCODING = "gbk";
	// 默认gb2312中文的处理编码
	public static final String GB2312_ENCODING = "gb2312";
	// 台湾big5编码
	public static final String BIG5_ENCODING = "big5";
	// 日本Shift_JIS
	public static final String Japan_Shift_ENCODING = "shift_jis";
	public static final String Japan_Euc_ENCODING = "euc-jp";
	// 西里尔文window
	public static final String Xili_Window_ENCODING = "windows-1251";
	/**
	 * 以下是对网页的charset部分的lang来定义
	 */
	public static final String Japan_Lang = "ja";
	public static final String Japan_Lang_First = Japan_Shift_ENCODING;
}
