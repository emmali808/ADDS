package cn.medicine.utils;
/**
 * 
 * @ClassName:BDCoordinateUtil
 * @Description: 百度坐标工具类
 * @Function List:TODO 主要函数及其功能
 *
 * @author   Administrator
 * @version  
 * @Date	 2016-4-11上午9:34:15
 *
 * @History:// 历史修改记录 
 * <author>  // 修改人
 * <time>    // 修改时间
 * <version> // 版本
 * <desc>    // 描述修改内容
 */
public class BDCoordinateUtil {
    
    private static double RADIUS = 6370996.81;
    
    public static double getDistance(double lat1,double lon1,double lat2,double lon2){
        lon1=ew(lon1,-180,180);
        lat1=lw(lat1,-74,74);
        lon2=ew(lon2,-180,180);
        lat2=lw(lat2,-74,74);
        return Td(oi(lon1),oi(lon2),oi(lat1),oi(lat2));
    }
    
    private static double Td(double a,double b,double c,double d){
        return RADIUS*Math.acos(Math.sin(c)*Math.sin(d)+Math.cos(c)*Math.cos(d)*Math.cos(b-a));
    }
    
    /**
     * 
     * @Function:     oi 
     * @Description:  角度转成弧度 
     *                 <功能详细描述>
     *
     * @param a
     * @return
     */
    private static double oi(double a){
        return Math.PI*a/180;
    }
    
    private static double ew(double a,double b,double c){
        while(a>c){
            a-=c-b;         
        }
        while(a<b){
            a+=c-b;
        }
        return a;
    }
    
    private static double lw(double a,double b,double c){
        a=max(a,b);
        a=min(a,c);
        return a;
    }
    
    private static double max(double a,double b){
        if(a>b){
            return a;
        }else{
            return b;
        }
    }
    
    private static double min(double a,double b){
        if(a>b){
            return b;
        }else{
            return a;
        }
    }
    
    public static void main(String[] args){
        System.out.println(getDistance(37.480563,121.467113,37.480591,121.467926));
    }

}
