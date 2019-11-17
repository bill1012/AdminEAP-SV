package com.eap.common.util;

import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;

public class CodeUtil {


	public static void main(String[] args) {

		System.out.println(nextCode("000001", null, 6));
		System.out.println(nextCode("","001",3));
		System.out.println(nextCode("000001", "00000100009", 6));
		System.out.println(nextCode("syc", "sya2016012", 7));
		System.out.println(nextCode("syb", "sya2016030", 7));
		System.out.println(nextCode("2016", "2016099", 3));
	}


	/**
	 * 根据当前编号和前缀生成下一个编号
	 *
	 * @param prefix
	 *            生成编号前缀
	 * @param maxCode
	 *            最大编号
	 * @param length
	 *            自动累加部分长度
	 * @return String 返回计算后的编号
	 *
	 * @Author: billjiang
	 * @Date: 2016年4月14日 上午11:27:12
	 */
	public synchronized static String nextCode(String prefix, String maxCode, int length) {

		String code;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			sb.append("0");
		}
		DecimalFormat df = new DecimalFormat(sb.toString());
		if (StringUtils.isEmpty(maxCode)) {
			code = prefix + df.format(1);
		} else {
			String num = maxCode.substring(prefix.length());
			code = prefix + df.format(1 + Integer.valueOf(num));
		}
		return code;
	}

    public static String getNextLevelCode(String levelCode, String lCode) {
		if(StringUtils.isEmpty(lCode)){
            lCode=levelCode+"000000";
		}
		return CodeUtil.nextCode(levelCode,lCode,6);
    }
}
