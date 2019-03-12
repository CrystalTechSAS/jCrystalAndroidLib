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
import jcrystal.mobile.net.utils.RequestError;

public class NetChain implements jcrystal.mobile.net.utils.OnErrorListener{
	private java.util.ArrayList<RequestError> errors = new java.util.ArrayList<>(); 
	private java.util.ArrayList<NetTask> tasks = new java.util.ArrayList<>(); 
	public final Activity activity;
	public final Fragment fragment;
		
    private int count = 0;
    private Runnable next;
    public NetChain(Activity activity){
		this.activity = activity;
		this.fragment = null;
	}
	public NetChain(Fragment fragment){
		this.fragment = fragment;
		this.activity = null;
	}
	public void add(NetTask task){
		tasks.add(task);
		task.$chain = this;
		count++;
	}	
	public void then(Runnable next){
		this.next = next;
		for(NetTask task : tasks)
			task.execute((String[])null);
	}
	public synchronized final void endTask(){
		count--;
		if(count == 0){
			next.run();
		}
	}
	public void onError(RequestError error){
		errors.add(error);
	}
}
