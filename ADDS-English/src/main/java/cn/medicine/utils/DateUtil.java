package cn.medicine.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @T:	VehicleTrack工具类
 * @author songyuan
 *@Date:	2015年8月24日
 *
 */
public class DateUtil {


	/**
	 * 将Date数据类型转换为String类型
	 * @param date
	 * @return
	 */
	public static String makeDateToString(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = null;
		if(date != null)
			time = sdf.format(date);
		return time;
	}

	/**
	 * 将Date数据类型转换为String
	 * @param time
	 * @return
	 */
	public static Date makeStringToDate(String time){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		if(time != null){
			try {
				date = sdf.parse(time);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return date;
	}
	
	/**
     * @param time
     * @return java.lang.String
     * @Function: StringToString
     * @Description: 将时间格式由"yyyy-MM-dd hh:mm:ss"转化为" yyyy.MM.dd E a hh:mm:ss"格式，以便于前端显示
     */
    public static String StringToString(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date date = sdf.parse(time);
            SimpleDateFormat formatter = new SimpleDateFormat(" yyyy.MM.dd E a hh:mm:ss");
            time = formatter.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }


}
