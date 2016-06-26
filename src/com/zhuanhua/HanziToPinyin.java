package com.zhuanhua;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class HanziToPinyin {
	public static String getPinYin(String src) {
	    char[] t1 = null;
	    t1 = src.toCharArray();
	    String[] t2 = new String[t1.length];
	    // ���ú���ƴ������ĸ�ʽ

	    HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();
	    t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
	    t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
	    t3.setVCharType(HanyuPinyinVCharType.WITH_V);
	    String t4 = "";
	    int t0 = t1.length;
	    try {
	        for (int i = 0; i < t0; i++) {
	            // �ж��ܷ�Ϊ�����ַ�

	            // System.out.println(t1[i]);

	            if (Character.toString(t1[i]).matches("[\\u4E00-\\u9FA5]+")) {
	                t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);// �����ֵļ���ȫƴ���浽t2������

	                t4 += t2[0];// ȡ���ú���ȫƴ�ĵ�һ�ֶ��������ӵ��ַ���t4��

	            } else {
	                // ������Ǻ����ַ������ȡ���ַ������ӵ��ַ���t4��

	                t4 += Character.toString(t1[i]);
	            }
	        }
	    } catch (BadHanyuPinyinOutputFormatCombination e) {
	        e.printStackTrace();
	    }
	    return t4;
	}
	/*public static String toPinYin(char hanzi){
		HanyuPinyinOutputFormat hanyuPinyin = new HanyuPinyinOutputFormat();
	    hanyuPinyin.setCaseType(HanyuPinyinCaseType.LOWERCASE);
	    //hanyuPinyin.setToneType(HanyuPinyinToneType.WITH_TONE_MARK);
	    hanyuPinyin.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
	    hanyuPinyin.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);
	    String[] pinyinArray=null;
	    try {
	    	//�Ƿ��ں��ַ�Χ��
	    	if(hanzi>=0x4e00 && hanzi<=0x9fa5){
	    		pinyinArray = PinyinHelper.toHanyuPinyinStringArray(hanzi, hanyuPinyin);
	    	}
	    } catch (BadHanyuPinyinOutputFormatCombination e) {
	    	e.printStackTrace();
	    }
	    //�����ַ���
		return pinyinArray[0];
	}*/

}
