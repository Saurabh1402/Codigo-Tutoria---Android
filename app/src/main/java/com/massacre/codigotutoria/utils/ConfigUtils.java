package com.massacre.codigotutoria.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import static com.massacre.codigotutoria.utils.CodigoTutoriaConstant.FORWARD_SLASH;

/**
 * Created by saurabh on 25/7/17.
 */

public class ConfigUtils {
    public String getProperty(String key,Context context) throws IOException {
        Properties properties=new Properties();
        AssetManager manager=context.getAssets();
        InputStream inputStream=manager.open("config.properties");
        properties.load(inputStream);
        String value=properties.getProperty(key);
        inputStream.close();
        return value;
    }
    public String[] getProperties(String keys[],Context context) throws IOException{
        Properties properties=new Properties();
        AssetManager manager=context.getAssets();
        InputStream inputStream=manager.open("config.properties");
        properties.load(inputStream);
        String values[]=new String[keys.length];
        for(int i=0;i<keys.length;i++){
            values[i]=properties.getProperty(keys[i]);
        }
        inputStream.close();
        return values;
    }

    public String getContentFromAssets(String fileName,String path,Context context) throws IOException {
        InputStream is=context.getAssets().open(path+FORWARD_SLASH+fileName);
        BufferedReader br=new BufferedReader(new InputStreamReader(is));
        String data="";
        String temp;
        while((temp=br.readLine())!=null){
            data+=temp;
        }
        return data;
    }
}
