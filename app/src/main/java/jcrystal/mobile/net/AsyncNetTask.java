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
import android.os.AsyncTask;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import jcrystal.mobile.net.utils.HTTPUtils;
import jcrystal.mobile.net.utils.OnErrorListener;
import jcrystal.mobile.net.utils.RequestError;
import jcrystal.mobile.net.utils.TipoError;

public class AsyncNetTask {
    private static int COUNTER = 0;
    private static long INIT = System.currentTimeMillis();
    private static final int MAX_FAILURES = 6;

    private String id;
    private int fallas = 0;
    public final String method;
    public final String url;
    public final String getParams;
    public final String postParams;
    public final String authToken;

    public AsyncNetTask(String method, String url, String getParams, String postParams) {
    	this(method, url, getParams, postParams, null);
    }
    public AsyncNetTask(String method, String url, String getParams, String postParams, String authToken) {
        this.method = method;
        this.url = url;
        this.getParams = getParams;
        this.postParams = postParams;
        this.authToken = authToken;
    }
    public void save(){
        synchronized (AsyncNetTask.class){
            this.id = "V"+Long.toHexString(INIT)+"_"+Integer.toHexString(COUNTER++);
        }
        java.io.FileOutputStream fos = null;
        try{
            fos = openWrite();
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(method);
            oos.writeObject(url);
            oos.writeObject(getParams);
            oos.writeObject(postParams);
            oos.writeObject(authToken);
            oos.writeInt(fallas);
            oos.close();
        }
        catch(java.io.IOException ex){if(jcrystal.JCrystalApp.DEBUG)ex.printStackTrace();}
        if(fallas == 0)
            jcrystal.JCrystalApp.getAppContext().startService(new android.content.Intent(jcrystal.JCrystalApp.getAppContext(), AsyncNetService.class));
    }
    public AsyncTask send(final OnErrorListener onError){
        AsyncTask<String, Void, org.json.JSONObject> ret = new AsyncTask<String, Void, org.json.JSONObject>(){
            private RequestError error;
            @Override
            protected org.json.JSONObject doInBackground(String ... paramsService){
                try{
                    String ruta = url;
                    final String params = AsyncNetTask.this.getParams;
                    if(params != null)ruta+=params;
                    if(jcrystal.JCrystalApp.DEBUG)System.out.println("GET "+ruta);
                    java.net.HttpURLConnection connection = (java.net.HttpURLConnection) new java.net.URL(ruta).openConnection();
                    connection.setConnectTimeout(5000);
                    connection.setRequestMethod(method);
                    if("POST".equals(method))
                        connection.setRequestProperty("Content-Type", "application/json");
                    connection.setRequestProperty("Accept", "application/json");
                    if(authToken != null)
                    	connection.setRequestProperty("Authorization", authToken);
                    connection.connect();
                    if("POST".equals(method)){
                        java.io.OutputStream out = connection.getOutputStream();
                        out.write(postParams.getBytes(java.nio.charset.Charset.forName("UTF-8")));
                        out.flush();
                    }
                    final int responseCode = connection.getResponseCode();
                    if(responseCode >= 200 && responseCode <= 299){
                        final StringBuilder resp = HTTPUtils.readResponse(connection.getInputStream());
                        connection.disconnect();
                        if(jcrystal.JCrystalApp.DEBUG)System.out.println(resp);
                        org.json.JSONObject json = new org.json.JSONObject(resp.toString());
                        final int success = json.optInt("success", 1);
                        if(success == 1)return json;
                        else if (success == 2)error = new RequestError(json.getInt("code"), json.getString("mensaje"));
                        else if (success == 3)error = new RequestError(TipoError.UNAUTHORIZED, json.getString("mensaje"));
                        else error = new RequestError(TipoError.SERVER_ERROR, json.getString("mensaje"));
                    }
                    else{
                        if(onError != null){
                            final StringBuilder resp = HTTPUtils.readResponse(connection.getErrorStream());
                            if(jcrystal.JCrystalApp.DEBUG)System.out.println(resp);
                            connection.disconnect();
                            if(jcrystal.JCrystalApp.DEBUG)System.out.println(resp);
                            error = new RequestError(responseCode, resp.toString());
                        }
                        else{
                            connection.disconnect();
                        }
                    }
                }
                catch (java.net.UnknownHostException ex){
                    if(jcrystal.JCrystalApp.DEBUG)ex.printStackTrace();
                    error = new RequestError(TipoError.NO_INTERNET, "Check your internet connection");
                }
                catch (java.net.ConnectException ex){
                    if(jcrystal.JCrystalApp.DEBUG)ex.printStackTrace();
                    error = new RequestError(TipoError.NO_INTERNET, "Check your internet connection");
                }
                catch (java.io.FileNotFoundException ex){
                    if(jcrystal.JCrystalApp.DEBUG)ex.printStackTrace();
                    error = new RequestError(TipoError.NO_INTERNET, "Check your internet connection");
                }
                catch (java.io.IOException ex){
                    if(jcrystal.JCrystalApp.DEBUG)ex.printStackTrace();
                    error = new RequestError(TipoError.SERVER_ERROR, "Error connecting to server");
                }
                catch (org.json.JSONException ex){
                    if(jcrystal.JCrystalApp.DEBUG)ex.printStackTrace();
                    error = new RequestError(TipoError.SERVER_ERROR, "Error connecting to server");
                }
                catch (Exception ex){
                    if(jcrystal.JCrystalApp.DEBUG)ex.printStackTrace();
                    error = new RequestError(TipoError.SERVER_ERROR, "Error connecting to server");
                }
                return null;
            }
            @Override
            protected void onPostExecute(org.json.JSONObject result){
                if(result != null){
                	try{
                    	delete();
                	}catch(Exception ex){
                		onError.onError(error);
                	}
                }
                else if(onError != null){
                    if(error.tipoError != TipoError.NO_INTERNET) {
                        fallas++;
                        if(fallas >= MAX_FAILURES){
                            reportFailure(error);
                            return;
                        }else {
                            save();
                        }
                    }
                    onError.onError(error);
                }
            }
        }
                ;
        ret.execute((String[])null);
        return ret;
    }
    private void reportFailure(RequestError error){
        delete();
    }
    public final void delete(){
        java.io.File dir = jcrystal.JCrystalApp.getAppContext().getDir("crystqueue", android.content.Context.MODE_PRIVATE);
        new java.io.File(dir, id).delete();
    }
    private java.io.FileOutputStream openWrite()throws java.io.IOException{
        java.io.File dir = jcrystal.JCrystalApp.getAppContext().getDir("crystqueue", android.content.Context.MODE_PRIVATE);
        return new java.io.FileOutputStream(new java.io.File(dir, id));
    }

    public static AsyncNetTask[] next(){
        java.io.File dir = jcrystal.JCrystalApp.getAppContext().getDir("crystqueue", android.content.Context.MODE_PRIVATE);
        String[] files = dir.list();
        if(files!=null){
            AsyncNetTask[] tasks = new AsyncNetTask[Math.min(100, files.length)];
            for(int e = 0; e < tasks.length; e++){
                try{
                    java.io.FileInputStream fis = openRead(files[e]);
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    AsyncNetTask task = new AsyncNetTask((String)ois.readObject(), (String)ois.readObject(), (String)ois.readObject(), (String)ois.readObject(), (String)ois.readObject());
                    task.fallas = ois.readInt();
                    task.id = files[e];
                    fis.close();
                    tasks[e] = task;
                }catch(java.io.IOException ex){if(jcrystal.JCrystalApp.DEBUG)ex.printStackTrace();} catch (ClassNotFoundException ex) {ex.printStackTrace();}
            }
            return tasks;
        }
        return new AsyncNetTask[0];
    }
    private static java.io.FileInputStream openRead(String key)throws java.io.IOException{
        java.io.File dir = jcrystal.JCrystalApp.getAppContext().getDir("crystqueue", android.content.Context.MODE_PRIVATE);
        return new java.io.FileInputStream(new java.io.File(dir, key));
    }
    public int getFallas() {
        return fallas;
    }
}
