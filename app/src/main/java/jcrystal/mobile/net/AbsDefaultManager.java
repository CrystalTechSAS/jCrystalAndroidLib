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
import android.app.Activity;
import android.support.v4.app.Fragment;
import jcrystal.mobile.net.utils.*;
public abstract class AbsDefaultManager<T> extends NetTask<T>{
	public static final String BASE_URL = "";
	private boolean formData;
	protected String boundary;
	public AbsDefaultManager(Activity activity, Fragment fragment, OnErrorListener onError){
		super(activity, fragment, onError);
	}
	@Override protected final T doRequest()throws Exception{
		java.net.HttpURLConnection connection = (java.net.HttpURLConnection) new java.net.URL(BASE_URL + getUrl()).openConnection();
		if(jcrystal.JCrystalApp.DEBUG)System.out.println(type + " " + BASE_URL + getUrl());
		connection.setConnectTimeout(NetConfig.TIMEOUT);
		connection.setRequestMethod(type.name());
		connection.setRequestProperty("Accept", "application/json");
		if(authorization != null){
			connection.setRequestProperty("Authorization", authorization);
		}
		if(type.isPost){
			if(formData){
				connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=---------------------------"+boundary);
			}
			else{
				connection.setRequestProperty("Content-Type", "application/json");
			}
		}
		connection.connect();
		if(type.isPost){
			if(jcrystal.JCrystalApp.DEBUG){
				java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
				java.io.PrintStream _pw = new java.io.PrintStream(baos, false, "UTF-8");
				makeBody(_pw);
				_pw.flush();
				_pw.close();
				System.out.println(new String(baos.toByteArray()));
				connection.getOutputStream().write(baos.toByteArray());
				connection.getOutputStream().close();
			}
			else{
				java.io.PrintStream _pw = new java.io.PrintStream(connection.getOutputStream(), false, "UTF-8");
				makeBody(_pw);
				_pw.flush();
				_pw.close();
			}
		}
		final int responseCode = connection.getResponseCode();
		if(responseCode >= 200 && responseCode <= 299){
			final StringBuilder resp = HTTPUtils.readResponse(connection.getInputStream());
			connection.disconnect();
			if(jcrystal.JCrystalApp.DEBUG)System.out.println(resp);
			return getResponse(resp);
		}
		else{
			if(onError != null){
				final java.io.InputStream errorStream = connection.getErrorStream();
				final StringBuilder resp;
				if(errorStream != null){
					resp = HTTPUtils.readResponse(errorStream);
					if(jcrystal.JCrystalApp.DEBUG)System.out.println(resp);
				}
				else{
					resp = new StringBuilder("");
				}
				connection.disconnect();
				error = new RequestError(responseCode, resp.toString());
			}
			else{
				connection.disconnect();
			}
			return null;
		}
	}
	public AbsDefaultManager doFormData(){
		formData = true;
		boundary = Long.toString(System.currentTimeMillis());
		return this;
	}
	@Override
	protected void onPostExecute(T result){
		if(isContextActive()){
			if(result != null){
				try{
					onResponse(result);
				}
				catch(Exception ex){
					if(jcrystal.JCrystalApp.DEBUG)ex.printStackTrace();
					if(onError!=null)onError.onError(new RequestError(TipoError.SERVER_ERROR, "Ocurrió un error con el servidor"));
				}
			}
			else if(onError != null){
				onError.onError(error);
			}
			if($chain != null)$chain.endTask();
		}
	}
	protected void makeBody(java.io.PrintStream _pw) throws java.io.UnsupportedEncodingException, java.io.IOException{
	}
	protected abstract void onResponse(T result) throws Exception;
	abstract T getResponse(StringBuilder resp) throws Exception;
	public abstract static class StringResp extends AbsDefaultManager<java.lang.String>{
		public StringResp(Activity activity, Fragment fragment, OnErrorListener onError){
			super(activity, fragment, onError);
		}
		@Override protected java.lang.String getResponse(StringBuilder resp)throws Exception{
			return resp.toString();
		}
	}
	public abstract static class JSONObjectResp extends AbsDefaultManager<org.json.JSONObject>{
		public JSONObjectResp(Activity activity, Fragment fragment, OnErrorListener onError){
			super(activity, fragment, onError);
		}
		@Override protected org.json.JSONObject getResponse(StringBuilder resp)throws Exception{
			org.json.JSONObject json = new org.json.JSONObject(resp.toString());
			return json;
		}
	}
	public abstract static class JSONArrayResp extends AbsDefaultManager<org.json.JSONArray>{
		public JSONArrayResp(Activity activity, Fragment fragment, OnErrorListener onError){
			super(activity, fragment, onError);
		}
		@Override protected org.json.JSONArray getResponse(StringBuilder resp)throws Exception{
			org.json.JSONArray json = new org.json.JSONArray(resp.toString());
			return json;
		}
	}
}