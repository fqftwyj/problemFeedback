
package com.yuanwang.common.utils.excelexport;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


public class ExcelService {
	
	private static int DATA_COUNT = 10000;

	@SuppressWarnings({"unchecked" })
	public static List splitList(List bigList){
		List splitList = new ArrayList(10);
		if(bigList == null || bigList.isEmpty()){
			return splitList;
		}
		int dataSize = bigList.size();
		int forCount = dataSize / ExcelService.DATA_COUNT;
		if(dataSize % ExcelService.DATA_COUNT != 0){
			forCount ++;
		}
		for(int i=0; i<forCount; i++){
			List smallList = new ArrayList();
			for(int j = ExcelService.DATA_COUNT*i; 
					j < Math.min(ExcelService.DATA_COUNT*(i+1), dataSize); j++){
						smallList.add(bigList.get(j));
					}
			splitList.add(smallList);
			
		}
		return splitList;
	}
	
	/**
	 * 
	 * @param fileName
	 * @return
	 */
	public static String encodeFileName(String fileName,HttpServletRequest request){
		try {
			String userAgent = request.getHeader("User-Agent");
			if (userAgent.indexOf("MSIE") > 0) {
				return new String(fileName.replaceAll(" ", "").getBytes("GB2312"),"ISO8859-1");
				
			}
			return new String(fileName.replaceAll(" ", "").getBytes("UTF-8"),"ISO8859-1");
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "未命名";
		}
	}
	
	/**
	 * 
	 * @param fields
	 * @param obj
	 * @param replaceStrategyMap
	 * @return
	 */
	public static Map<String, Object> getFieldValues(Field[] fields, Object obj,
			Map<String, IChange> replaceStrategyMap) {

		Map<String, Object> fieldValues = new HashMap<String, Object>();

		for (int k = 0; k < fields.length; k++) {
			try {
				String fieldName = fields[k].getName();
				Object fieldValue = fields[k].get(obj);

				if (replaceStrategyMap != null) {
					IChange strategy = replaceStrategyMap.get(fieldName);
					if (strategy != null && fieldValue != null) {
						fieldValue = strategy.fieldChangeStrategy(fieldValue
								.toString());
					}
				}
				fieldValues.put(fieldName, fieldValue);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return fieldValues;
	}

    public static Map<String, Object> getFieldValues(Map<String,Object> dataObj,
                                                     Map<String, IChange> replaceStrategyMap) {
        for (Map.Entry<String,Object> set : dataObj.entrySet()) {
            if (replaceStrategyMap != null) {
                IChange strategy = replaceStrategyMap.get(set.getKey());
                if (strategy != null && set.getValue() != null) {
                    dataObj.put(set.getKey(), strategy.fieldChangeStrategy(set.getValue().toString()));
                }
            }
        }


        return dataObj;
    }
}
