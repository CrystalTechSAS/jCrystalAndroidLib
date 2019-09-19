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
import android.app.Activity;
import android.support.v4.app.Fragment;

import java.util.Map;
import java.util.TreeMap;

import jcrystal.mobile.net.utils.OnErrorListener;
import jcrystal.mobile.net.utils.RequestError;
import jcrystal.mobile.net.utils.TipoError;

public abstract class NetTask<T> extends AsyncTask<String, Void, T>{
    final Activity activity;
	final Fragment fragment;
	protected final OnErrorListener onError;
	protected NetChain $chain;
	protected RequestError error;
	protected RequestType type;
	protected String authorization;
	protected Map<String, String> headers;
	protected NetTask(Activity activity, Fragment fragment, OnErrorListener onError){
		this.activity = activity;
		this.fragment = fragment;
		this.onError = onError;
	}
	protected NetTask(Activity activity, OnErrorListener onError){
		this.activity = activity;
		this.fragment = null;
		this.onError = onError;
	}
	protected NetTask(Fragment fragment, OnErrorListener onError){
		this.fragment = fragment;
		this.activity = null;
		this.onError = onError;
	}
	public NetTask authorization(String authorization){
		this.authorization = authorization;
		return this;
	}
	public NetTask header(String key, String value){
		if(headers == null)
			headers = new TreeMap<>();
		headers.put(key, value);
		return this;
	}
	public NetTask doGet(){
		this.type = RequestType.GET;
		return this;
	}
	public NetTask doPost(){
		this.type = RequestType.POST;
		return this;
	}
	public NetTask doPut(){
		this.type = RequestType.PUT;
		return this;
	}
	public NetTask doDelete(){
		this.type = RequestType.DELETE;
		return this;
	}
	public NetTask doPatch(){
		this.type = RequestType.PATCH;
		return this;
	}
	public final boolean isContextActive(){
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
			return (activity==null && fragment==null) || (activity != null && !activity.isDestroyed()) || (fragment != null && fragment.getContext() != null);
		}else{
			return (activity==null && fragment==null) || (activity != null && !activity.isChangingConfigurations() && !activity.isFinishing()) || (fragment != null && fragment.getContext() != null);
		}
	}
	public final NetTask<T> exec(){
		execute((String[])null);
		return this;
	}
	@Override
	protected T doInBackground(String...paramsService){
		try{
			return doRequest();
		}
		catch (java.net.UnknownHostException | javax.net.ssl.SSLException | java.net.ConnectException | java.io.FileNotFoundException | java.net.HttpRetryException | java.net.SocketTimeoutException ex){
			if(jcrystal.JCrystalApp.DEBUG)ex.printStackTrace();
			error = new RequestError(TipoError.NO_INTERNET, "Check your internet connection");
		}
		catch (java.io.IOException | org.json.JSONException ex){
			if(jcrystal.JCrystalApp.DEBUG)ex.printStackTrace();
			error = new RequestError(TipoError.SERVER_ERROR, "Error connecting to server");
		}
		catch (Exception ex){
			if(jcrystal.JCrystalApp.DEBUG)ex.printStackTrace();
			error = new RequestError(TipoError.SERVER_ERROR, "Error connecting to server");
		
		}
		return null;
	}
	protected abstract T doRequest()throws Exception;
	protected abstract String getUrl()throws java.io.UnsupportedEncodingException;
}
