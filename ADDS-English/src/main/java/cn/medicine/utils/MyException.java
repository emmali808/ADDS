package cn.medicine.utils;

import java.io.Serializable;

public class MyException extends Exception implements Serializable{

    
    /**
     * serialVersionUID:TODO<用一句话描述这个变量表示什么>
     *
     */
    
    private static final long serialVersionUID = -4810097410038372998L;
    
    public MyException(){
        super();
    }
    
    public MyException(String message){
        super(message);
    }

}
