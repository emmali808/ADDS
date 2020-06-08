package cn.medicine.utils;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.NumberFormat;

import org.springframework.data.geo.Point;
/**
 * @ClassName: GPSCoder
 * @Description: 坐标转换
 * WGS-84：是国际标准，GPS坐标（Google Earth使用、或者GPS模块）
 * GCJ-02(火星坐标)：中国坐标偏移标准，Google Map、高德、腾讯使用
 * BD-09：百度坐标偏移标准，Baidu Map使用
 * @Function List: TODO
 * @author: xzt
 * @version:
 * @Date: 2016/4/7 
 * @History: //历史修改记录
 * <author>  // 修改人
 * <time> //修改时间
 * <version> //版本
 * <desc> // 描述修改内容
 */
public class GPSCoder {

    private static final double PI =3.14159265358979324;
    private static final double x_pi=PI*3000.0/180.0;
    /**
     *
     * @Function:
     * @Description:  GCJ-02 to WGS-84 exactly Marth to GPS
     *
     * GCJ-02 to WGS-84 精确(二分极限法) threshold = 0.000000001; 目前设置的是精确到小数点后9位，这个值越小，越精确，
     * 但是java中，浮点运算本身就不太精确，九位在GPS里也偏差不大了
     *
     * @param gcjLon longititude
     * @param gcjLat latitude
     * @return  长度为2的一维数组，第一个元是gcjLa，第二个是gcjLon
     */
    public static double[] gcj_decrypt_exact(double gcjLat,double gcjLon) {
        double initDelta = 0.01;
        double threshold = 0.000000001;
        double dLat = initDelta, dLon = initDelta;
        double mLat = gcjLat - dLat, mLon = gcjLon - dLon;
        double pLat = gcjLat + dLat, pLon = gcjLon + dLon;
//        Point tmp;
        double[] tmp;
        double wgsLat, wgsLon, i = 0;
        while (true) {
            wgsLat = (mLat + pLat) / 2;
            wgsLon = (mLon + pLon) / 2;
            tmp = gcj_encrypt(wgsLat, wgsLon);
            dLat = tmp[0] - gcjLat;
            dLon = tmp[1] - gcjLon;
            if ((Math.abs(dLat) < threshold) && (Math.abs(dLon) < threshold))
                break;
            
            if (dLat > 0) 
                pLat = wgsLat; 
            else 
                mLat = wgsLat;
            if (dLon > 0) 
                pLon = wgsLon; 
            else 
                mLon = wgsLon;

            
            if (++i > 10000) 
                break;
        }
        double[] point = new double[2];
        point[0] = wgsLat;
        point[1] = wgsLon;
        return point;
    };
    
    //WGS-84 to GCJ-02
    public static double[] gcj_encrypt(double wgsLat, double wgsLon) {
        double[] point = new double[2];
        if (outOfChina(wgsLat, wgsLon)){
            point[0] = wgsLat;
            point[1] = wgsLon;
        }else{
//            Point d = delta(wgsLat, wgsLon);  
            double[] d = delta(wgsLat, wgsLon);
            point[0] = wgsLat+d[0];
            point[1] = wgsLon+d[1];
        }        
        return point;
    }
  //GCJ-02 to WGS-84
    public static double[] gcj_decrypt(double gcjLat,double gcjLon){
        double[] point = new double[2];
        if(outOfChina(gcjLat,gcjLon)){
            point[0] = gcjLat;
            point[1] = gcjLon;
        }else{
//            Point d=delta(gcjLat,gcjLon);  
            double[] d=delta(gcjLat,gcjLon);
            //保留六位小数
            MathContext v=new MathContext(6,RoundingMode.HALF_DOWN);
            BigDecimal lat=new BigDecimal(gcjLat-d[0],v);
            BigDecimal lon=new BigDecimal(gcjLon-d[1],v);
            point[0]=lat.doubleValue();
            point[1]=lon.doubleValue();         
        }
        return point;
    }
    //GCJ-02 to BD-09
    public static double[] bd_encrypt(double gcjLat,double gcjLon){
        double x=gcjLon,y=gcjLat;
        double z=Math.sqrt(x*x+y*y)+0.00002*Math.sin(y*x_pi);
        double theta=Math.atan2(y, x)+0.000003*Math.cos(x*x_pi);
        double bdLon=z*Math.cos(theta)+0.0065;
        double bdLat=z*Math.sin(theta)+0.006;
        MathContext v=new MathContext(6,RoundingMode.HALF_DOWN);
        BigDecimal lat=new BigDecimal(bdLat,v);
        BigDecimal lon=new BigDecimal(bdLon,v);
        double[] point=new double[2];
        point[0]=lat.doubleValue();
        point[1]=lon.doubleValue();
        return point;
    }
  //BD-09 to GCJ-02
    public static double[] bd_decrypt(double bdLat,double bdLon){
        double x=bdLon-0.0065,y=bdLat-0.006;
        double z=Math.sqrt(x*x+y*y)-0.00002*Math.sin(y*x_pi);
        double theta=Math.atan2(y, x)-0.000003*Math.cos(x*x_pi);
        double gcjLon=z*Math.cos(theta);
        double gcjLat=z*Math.sin(theta);
        MathContext v=new MathContext(6,RoundingMode.HALF_DOWN);
        BigDecimal lat=new BigDecimal(gcjLat,v);
        BigDecimal lon=new BigDecimal(gcjLon,v);
        double[] point=new double[2];
        point[0]=lat.doubleValue();
        point[1]=lon.doubleValue();
        return point;
    }
    /**
     * 
     * @Function:     mercator_encrypt 
     * @Description:   WGS-84 to Web mercator 
     *                 mercatorLat -> y mercatorLon -> x
     *
     * @param wgsLat
     * @param wgsLon
     * @return
     */
    public static double[] mercator_encrypt(double wgsLat,double wgsLon){
        double x=wgsLon*20037508.34/180;
        double y=Math.log(Math.tan((90+wgsLat)*PI/360))/(PI/180);
        y=y*20037508.34/180;
        double[] point=new double[2];
        point[0]=y;
        point[1]=x;
        return point;
    }
    /**
     * 
     * @Function:     mercator_decrypt 
     * @Description:   Web mercator  to   WGS-84
     *                 mercatorLat -> y   mercatorLon -> x
     *
     * @param mercatorLat
     * @param mercatorLon
     * @return
     */
    public static double[] mercator_decrypt(double mercatorLat,double mercatorLon){
        double x=mercatorLon/20037508.34*180;
        double y=mercatorLat/20037508.34*180;
        y=180/Math.PI*(2*Math.atan(Math.exp(y*PI/180))-PI/2);
        
        MathContext v=new MathContext(9,RoundingMode.HALF_DOWN);
        BigDecimal lat=new BigDecimal(y,v);
        BigDecimal lon=new BigDecimal(x,v);
        
        double[] point=new double[2];
       /* point[0]=y;
        point[1]=x*/;
        point[0]=lat.doubleValue();
        point[1]=lon.doubleValue();
        return point;
    }
    
    public static double distance(double latA,double lngA,double latB,double lngB){
        double earthR=6371000;
        double x=Math.cos(latA*PI/180.0)*Math.cos(latB*PI/180.0)*Math.cos((lngA-lngB)*PI/180);
        double y=Math.sin(latA*PI/180)*Math.sin(latB*PI/180);
        double s=x+y;
        if(s>1)
            s=1;
        if(s<-1)
            s=-1;
        double alpha=Math.acos(s);
        double distance=alpha*earthR;
        return distance;
    }

    private  static double[] delta(double lat,double lng){
        // Krasovsky 1940
        //
        // a = 6378245.0, 1/f = 298.3
        // b = a * (1 - f)
        // ee = (a^2 - b^2) / a^2;
        double a = 6378245.0; //  a: 卫星椭球坐标投影到平面地图坐标系的投影因子。
        double ee = 0.00669342162296594323; //  ee: 椭球的偏心率。
        double dLat = transformLat(lng - 105.0, lat - 35.0);
        double dLon = transformLon(lng - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * PI;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * PI);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * PI);
        double[] point = new double[2];
        point[0] = dLat;
        point[1] = dLon;
        return point;
    }

    private static double transformLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * PI) + 20.0 * Math.sin(2.0 * x * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * PI) + 40.0 * Math.sin(y / 3.0 * PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * PI) + 320 * Math.sin(y * PI / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    private static  double transformLon(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x *PI) + 20.0 * Math.sin(2.0 * x * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x *PI) + 40.0 * Math.sin(x / 3.0 * PI)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 *PI) + 300.0 * Math.sin(x / 30.0 * PI)) * 2.0 / 3.0;
        return ret;
    }


    private static boolean outOfChina(double lat,double lng) {
        if (lng < 72.004 || lng > 137.8347)
            return true;
        if (lat < 0.8293 || lat > 55.8271)
            return true;
        return false;
    }
    
    public static void print(double[] point){
        System.out.println("0:"+point[0]+"\n1:"+point[1]);
    }
    
    public static void main(String[] args){
        double lat=30.599349;
        double lon=103.987888;
//        double lat=30.5993;
//        double lon=103.9878;
        double[] point=mercator_encrypt(lat,lon);
        print(point);
        point=mercator_decrypt(point[0],point[1]);
        print(point);
    }
}
