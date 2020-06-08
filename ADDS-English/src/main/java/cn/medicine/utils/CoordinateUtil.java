package cn.medicine.utils;

import java.util.HashMap;
import java.util.Map;

public class CoordinateUtil {
    
    private static double EARTH_RADIUS = 6378.137; 
    private static double rad(double d) { 
        return d * Math.PI / 180.0; 
    }
    
    /**
     * 根据两个位置的经纬度，来计算两地的距离（单位为KM）
     * 参数为String类型
     * @param lat1Str 用户经度
     * @param lon1Str 用户纬度
     * @param lat2Str 商家经度
     * @param lon2Str 商家纬度
     * @return
     */
    public static double getDistance(String lat1Str, String lon1Str, String lat2Str, String lon2Str) {
        Double lat1 = Double.parseDouble(lat1Str);
        Double lon1 = Double.parseDouble(lon1Str);
        Double lat2 = Double.parseDouble(lat2Str);
        Double lon2 = Double.parseDouble(lon2Str);
        
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lon1) - rad(lon2);
        double distance = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b/2),2)));
        distance = distance * EARTH_RADIUS;
        distance = Math.round(distance * 10000) / 10000;                  
        return distance;
    }
    
    public static double getDistance(double lat1, double lon1, double lat2, double lon2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lon1) - rad(lon2);
        double distance = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b/2),2)));
        distance = distance * EARTH_RADIUS;
        distance = Math.round(distance * 10000) / 10000;                  
        return distance;
    }
    /**
     * 
     * @Function:     getDistance1 
     * @Description:   单位米  
     *                 <功能详细描述>
     *
     * @param lat1
     * @param lon1
     * @param lat2
     * @param lon2
     * @return
     */
    public static double getDistance1(double lat1, double lon1, double lat2, double lon2) {
        double pk=(double)(180/3.14169);
        double a1=lat1/pk;
        double a2=lon1/pk;
        double b1=lat2/pk;
        double b2=lon2/pk;
        
        double t1=Math.cos(a1)*Math.cos(a2)*Math.cos(b1)*Math.cos(b2);
        double t2=Math.cos(a1)*Math.sin(a2)*Math.cos(b1)*Math.sin(b2);
        double t3=Math.sin(a1)*Math.sin(b1);
        double tt=Math.acos(t1+t2+t3);
        
        return 6366000*tt;
        
        
//        double radLat1 = rad(lat1);
//        double radLat2 = rad(lat2);
//        double a = radLat1 - radLat2;
//        double b = rad(lon1) - rad(lon2);
//        double distance = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
//                + Math.cos(radLat1) * Math.cos(radLat2)
//                * Math.pow(Math.sin(b/2),2)));
//        distance = distance * EARTH_RADIUS;
//        distance = Math.round(distance * 10000) / 10000;                  
//        return distance;
    }
    
    public static double getEDistance(double x1, double y1, double x2, double y2) {
        double distance=Math.sqrt(Math.pow(x1-x2, 2)+Math.pow(y1-y2, 2));                  
        return distance;
    }
      
    /**
     * 获取当前用户一定距离以内的经纬度值
     * 单位米 return minLat
     * 最小经度 minLng
     * 最小纬度 maxLat
     * 最大经度 maxLng
     * 最大纬度 minLat
     */
    public static Map<String,String> getAround(String latStr, String lngStr, String raidus) {
        Map<String,String> map = new HashMap<String,String>();
          
        Double latitude = Double.parseDouble(latStr);// 传值给经度
        Double longitude = Double.parseDouble(lngStr);// 传值给纬度
  
        Double degree = (24901 * 1609) / 360.0; // 获取每度
        double raidusMile = Double.parseDouble(raidus);
          
        Double mpdLng = Double.parseDouble((degree * Math.cos(latitude * (Math.PI / 180))+"").replace("-", ""));
        Double dpmLng = 1 / mpdLng;
        Double radiusLng = dpmLng * raidusMile;
        //获取最小经度
        Double minLat = longitude - radiusLng;
        // 获取最大经度
        Double maxLat = longitude + radiusLng;
          
        Double dpmLat = 1 / degree;
        Double radiusLat = dpmLat * raidusMile;
        // 获取最小纬度
        Double minLng = latitude - radiusLat;
        // 获取最大纬度
        Double maxLng = latitude + radiusLat;
          
        map.put("minLat", minLat+"");
        map.put("maxLat", maxLat+"");
        map.put("minLng", minLng+"");
        map.put("maxLng", maxLng+"");
          
        return map;
    }
    
    public static void main(String[] args){
        System.out.println("GPS经纬度距离："+getDistance(29.329949, 104.828708, 31.524144, 104.699294)*1000);
        System.out.println("GPS经纬度距离1："+getDistance1(29.329949, 104.828708, 31.524144, 104.699294));
//        System.out.println("百度坐标距离："+BDCoordinateUtil.getDistance(37.480563,121.467113,37.480591,121.467926));
    }

}
