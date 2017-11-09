package com.dev.base.util;

import com.dev.base.test.BaseTest;
import org.junit.Test;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by koukouken on 2017/11/7.
 */
public class PropertiesUtilsTest extends BaseTest {
    @Test
    public void getFile(){
        String DB_NAME = null;
        String DB_DESC = null;

            InputStream in = null;
            try{
                Properties prop = new Properties();
                in = PropertiesUtilsTest.class.getClassLoader().getResourceAsStream("jdbc-mysql.properties");
                prop.load(in);
                DB_NAME = prop.getProperty("DB_NAME");
                DB_DESC = prop.getProperty("DB_DESC");
                System.out.println("DB_NAME:"+DB_NAME);
            }catch(Exception e){
                e.printStackTrace();
            }finally{
                if(in != null){
                    try{
                        in.close();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }

    }
}
