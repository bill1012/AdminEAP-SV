package com.eap.common.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PinYingUtil {
    /***
     * 将汉字转成拼音(取首字母或全拼)
     * @param hanzi
     * @param full 是否全拼
     * @return
     */
    public static String convertHanzi2Pinyin(String hanzi,boolean full){
        /***
         * ^[\u2E80-\u9FFF]+$ 匹配所有东亚区的语言
         * ^[\u4E00-\u9FFF]+$ 匹配简体和繁体
         * ^[\u4E00-\u9FA5]+$ 匹配简体
         */
        String regExp="^[\u4E00-\u9FFF]+$";
        StringBuffer sb=new StringBuffer();
        if(hanzi==null||"".equals(hanzi.trim()))
        {
            return "";
        }
        String pinyin="";
        for(int i=0;i<hanzi.length();i++)
        {
            char unit=hanzi.charAt(i);
            if(match(String.valueOf(unit),regExp))//是汉字，则转拼音
            {
                pinyin=convertSingleHanzi2Pinyin(unit);
                if(full)
                {
                    sb.append(pinyin);
                }
                else
                {
                    sb.append(pinyin.charAt(0));
                }
            }
            else
            {
                sb.append(unit);
            }
        }
        return sb.toString().toLowerCase();
    }
    /***
     * 将单个汉字转成拼音
     * @param hanzi
     * @return
     */
    private static String convertSingleHanzi2Pinyin(char hanzi)
    {
        HanyuPinyinOutputFormat outputFormat = new HanyuPinyinOutputFormat();
        outputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        String[] res;
        StringBuffer sb=new StringBuffer();
        try {
            res = PinyinHelper.toHanyuPinyinStringArray(hanzi,outputFormat);
            sb.append(res[0]);//对于多音字，只用第一个拼音
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return sb.toString();
    }

    /***
     * @param str 源字符串
     * @param regex 正则表达式
     * @return 是否匹配
     */
    public static boolean match(String str,String regex)
    {
        Pattern pattern=Pattern.compile(regex);
        Matcher matcher=pattern.matcher(str);
        return matcher.find();
    }


}
