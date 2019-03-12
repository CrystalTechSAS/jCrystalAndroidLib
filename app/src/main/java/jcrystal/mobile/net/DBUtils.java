/*
 * The MIT License
 *
 * Copyright (c) 2018-2019 German Augusto Sotelo Arevalo

 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package jcrystal.mobile.net;

import org.json.JSONObject;

import jcrystal.mobile.net.utils.FunctionExp;

public class DBUtils {
    public static java.io.FileOutputStream openWrite(String partKey, String key)throws java.io.IOException{
        if(partKey == null)return jcrystal.JCrystalApp.getAppContext().openFileOutput(key, android.content.Context.MODE_PRIVATE);
        java.io.File dir = jcrystal.JCrystalApp.getAppContext().getDir(partKey, android.content.Context.MODE_PRIVATE);
        return new java.io.FileOutputStream(new java.io.File(dir, key));
    }
    public static java.io.InputStreamReader openRead(String partKey, String key)throws java.io.IOException{
        if(partKey == null)return new java.io.InputStreamReader(jcrystal.JCrystalApp.getAppContext().openFileInput(key));
        java.io.File dir = jcrystal.JCrystalApp.getAppContext().getDir(partKey, android.content.Context.MODE_PRIVATE);
        return new java.io.InputStreamReader(new java.io.FileInputStream(new java.io.File(dir, key)));
    }
    public static java.io.FileOutputStream openAppend(String partKey, String key)throws java.io.IOException{
        if(partKey == null)return jcrystal.JCrystalApp.getAppContext().openFileOutput(key, android.content.Context.MODE_PRIVATE | android.content.Context.MODE_APPEND);
        java.io.File dir = jcrystal.JCrystalApp.getAppContext().getDir(partKey, android.content.Context.MODE_PRIVATE);
        return new java.io.FileOutputStream(new java.io.File(dir, key), true);
    }
    public static boolean store(String partKey, String key, ISerializable value){
        java.io.PrintStream _pw = null;
        try{
            _pw = new java.io.PrintStream(DBUtils.openWrite(partKey, "V"+key), false, "UTF-8");
            value.toJson(_pw);
            _pw.close();
            return true;
        }
        catch(java.io.IOException ex){if(jcrystal.JCrystalApp.DEBUG)ex.printStackTrace();}
        return false;
    }
    public static <T extends ISerializable> boolean store(String partKey, String key, java.util.List<T> values){
        java.io.PrintStream _pw = null;
        try{
            _pw = new java.io.PrintStream(DBUtils.openWrite(partKey, "L"+key), false, "UTF-8");
            for(T value : values){
                _pw.print(",");
                value.toJson(_pw);
            }
            _pw.close();
            return true;
        }
        catch(java.io.IOException ex){if(jcrystal.JCrystalApp.DEBUG)ex.printStackTrace();}
        return false;
    }
    public static boolean appendToList(String partKey, String key, ISerializable value){
        java.io.PrintStream _pw = null;
        try{
            _pw = new java.io.PrintStream(DBUtils.openAppend(partKey, "L"+key), false, "UTF-8");
            _pw.print(",");
            value.toJson(_pw);
            _pw.close();
            return true;
        }
        catch(java.io.IOException ex){if(jcrystal.JCrystalApp.DEBUG)ex.printStackTrace();}
        return false;
    }
    public static String retrieve(String partKey, String key){
        java.io.InputStreamReader fis;
        try{
            fis = DBUtils.openRead(partKey, "V"+key);
            String data = "";
            int n;
            for(char[] buffer = new char[512]; (n=fis.read(buffer))!=-1; )data+=new String(buffer, 0, n);
            fis.close();
            return data;
        }
        catch(java.io.FileNotFoundException ex){}
        catch(java.io.IOException ex){if(jcrystal.JCrystalApp.DEBUG)ex.printStackTrace();}
        return null;
    }
    public static <T> T retrieve(String partKey, String key, FunctionExp<JSONObject, T, org.json.JSONException> creator){
        try{
            String data = DBUtils.retrieve(partKey, key);
            if(data != null){
                return creator.eval(new org.json.JSONObject(data));
            }
        }
        catch(org.json.JSONException ex){if(jcrystal.JCrystalApp.DEBUG)ex.printStackTrace();}
        return null;
    }
    public static <T, K> java.util.List<T> retrieveList(String partKey, String key, FunctionExp<JSONObject, K, org.json.JSONException> creator){
        java.io.InputStreamReader fis;
        try{
            fis = DBUtils.openRead(partKey, "L"+key);
            String data = "[";
            int n;
            fis.read();
            for(char[] buffer = new char[512]; (n=fis.read(buffer))!=-1; )data+=new String(buffer, 0, n);
            fis.close();
            org.json.JSONArray jsondata = new org.json.JSONArray(data+"]");
            java.util.List<T> ret = new java.util.ArrayList<T>(jsondata.length());
            for(int e = 0, i = jsondata.length(); e < i; e++){
                ret.add((T)creator.eval(jsondata.getJSONObject(e)));
            }
            return ret;
        }
        catch(java.io.FileNotFoundException ex){}
        catch(java.io.IOException ex){if(jcrystal.JCrystalApp.DEBUG)ex.printStackTrace();}
        catch(org.json.JSONException ex){if(jcrystal.JCrystalApp.DEBUG)ex.printStackTrace();}
        return null;
    }
    public static void delete(String partKey, String key){
        if(partKey==null)jcrystal.JCrystalApp.getAppContext().deleteFile("V"+key);
        else{
            java.io.File dir = jcrystal.JCrystalApp.getAppContext().getDir(partKey, android.content.Context.MODE_PRIVATE);
            new java.io.File(dir, "V"+key).delete();
        }
    }
    public static void deleteList(String partKey, String key){
        if(partKey==null)jcrystal.JCrystalApp.getAppContext().deleteFile("L"+key);
        else{
            java.io.File dir = jcrystal.JCrystalApp.getAppContext().getDir(partKey, android.content.Context.MODE_PRIVATE);
            new java.io.File(dir, "L"+key).delete();
        }
    }
    public static void delete(String key){
        jcrystal.JCrystalApp.getAppContext().deleteFile("V"+key);
    }
}
