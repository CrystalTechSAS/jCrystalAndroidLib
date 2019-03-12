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
import android.app.IntentService;
import android.content.Intent;

import java.util.concurrent.ExecutionException;

import jcrystal.mobile.net.utils.OnErrorListener;
import jcrystal.mobile.net.utils.RequestError;
import jcrystal.mobile.net.utils.TipoError;

public class AsyncNetService extends IntentService {
    private RequestError taskError;
    public AsyncNetService() {
        super("AsyncNetService");
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        taskError = null;
        for(AsyncNetTask task : AsyncNetTask.next()){
            try {
                if(task.getFallas()>0)
                    Thread.sleep(1000);
                task.send(new OnErrorListener(){
                    public void onError(RequestError error){
                        taskError = error;
                    }
                }).get();
                if(taskError != null){
                    if(taskError.tipoError == TipoError.NO_INTERNET)
                        break;
                }else task.delete(); 
            } catch (InterruptedException e) {
                break;
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            taskError = null;
        }
    }
    public static void startService(){
        jcrystal.JCrystalApp.getAppContext().startService(new android.content.Intent(jcrystal.JCrystalApp.getAppContext(), AsyncNetService.class));
    }
}
