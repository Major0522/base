package com.easyget.terminal.base.util;

import java.util.Arrays;

/**
 * Created by Major on 2015/11/5.
 */
public class ArrayUtils {
	
	/**
     * 将数组用<Code>[split]</Code>分割并转化为字符串
     * @param objects 数组
     * @param split 分隔符
     * @return
     */
    public static String convert(Object [] objects, String split){
    	String ret = "";
    	if(objects != null && objects.length > 0){
    		for(int i = 0; i < objects.length; i++){
    			ret = ret + objects[i] + split;
    		}
    		return ret.substring(0, ret.length() - 1);
    	}
    	return ret;
    }

    @SafeVarargs
	public static <T> T[] concat(T[] first, T[]... rest) {
        int totalLength = first.length;
        for (T[] array : rest) {
            totalLength += array.length;
        }
        T[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;
        for (T[] array : rest) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }
    
    public static int[][] StringToArray(String str){
    	int[][] ret;
    	
    	String[] splitFirst = str.split("\\|");
    	ret = new int[splitFirst.length][]; 
    	for (int i = 0; i < splitFirst.length; i++) {
    		String[] temp = splitFirst[i].split(",");
    		ret[i] = new int[temp.length];
    		for (int j = 0; j < temp.length; j++) {
    			ret[i][j] = Integer.valueOf(temp[j]);
    		}
    	}
		return ret;
    }
}