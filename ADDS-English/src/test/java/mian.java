import java.util.Scanner;

/**
 * @ClassName:
 * @Description: TODO
 * @Function List: TODO
 * @author: ytchen
 * @version:
 * @Date: 2016/9/18
 * @History: //历史修改记录
 * <author>  // 修改人
 * <time> //修改时间
 * <version> //版本
 * <desc> // 描述修改内容
 */
public class mian {
    public static void  main(String[] args){
        Scanner cin = new Scanner(System.in);
        while (cin.hasNext()){
            String[] str=cin.nextLine().split(" ");
            for(int i=0;i<str.length;i++){
                char ch=str[i].charAt(0);
                int dif=ch-'a';
                if(dif>=0 && dif<=26){
                    str[i]= Character.toLowerCase(ch)+str[i].substring(1);
                }
            }
            System.out.println( str);
        }
    }
}
