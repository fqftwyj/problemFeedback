package com.yuanwang.common.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * MD5Util
 * @author joe.qiu
 * Jul 25, 2010 5:17:04 PM
 * @version 1.0.0
 */
public class MD5Util {
	
	
	/**字符串md5操作
	 * @param toMd5
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String md5(String toMd5) throws NoSuchAlgorithmException{
		if(!StringUtils.isNotEmpty(toMd5)) {
			return null;
		}
		return toString(md5Private(toMd5.getBytes())).toUpperCase();
	}
	
	/**
	 * 进行md5运算
	 * @param cs
	 * @return
	 * @throws NoSuchAlgorithmException 
	 */
	private static byte[] md5Private(byte[] cs) throws NoSuchAlgorithmException {
		byte[] rs = null;
	    MessageDigest md = MessageDigest.getInstance("MD5");
		rs = md.digest(cs);
		return rs;
	}
	
	/**
	 * @param a
	 * @return
	 */
	public static String toString(byte[] a) {
        if(a == null) {
            return "null";
        }
        if(a.length == 0) {
            return "";
        }
        StringBuilder buf = new StringBuilder();
 
        for (int i = 0; i < a.length; i++) {
        	if (a[i] < 0) {
        		buf.append(Integer.toHexString(a[i]&0xff));
        	} else if (a[i] < 16) {
        		buf.append('0');
        		buf.append(Integer.toHexString(a[i]));
        	} else {
        		buf.append(Integer.toHexString(a[i]));
        	}
        }
 
        return buf.toString();
    } 
	
}
