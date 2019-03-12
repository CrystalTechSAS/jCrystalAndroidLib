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
package jcrystal.mobile.net.utils;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by g on 6/14/16.
 */
public class SimpleStore {
    public static String getString(Context context, String name, String valor){
        String ret = context.getSharedPreferences("vault", Context.MODE_PRIVATE).getString(name, null);
        return ret==null?valor : ret;
    }
    public static String getString(Context context, String name){
        return context.getSharedPreferences("vault", Context.MODE_PRIVATE).getString(name, null);
    }
    public static void putString(Context context, String name, String valor){
        SharedPreferences.Editor editor = context.getSharedPreferences("vault", Context.MODE_PRIVATE).edit();
        if(valor == null)
            editor.remove(name);
        else
            editor.putString(name, valor);
        editor.commit();
    }

    public static long getLong(Context context, String name){
        return context.getSharedPreferences("vault", Context.MODE_PRIVATE).getLong(name, -1L);
    }
    public static void putLong(Context context, String name, long valor){
        SharedPreferences.Editor editor = context.getSharedPreferences("vault", Context.MODE_PRIVATE).edit();
        editor.putLong(name, valor);
        editor.commit();
    }
    public static int getInt(Context context, String name){
        return context.getSharedPreferences("vault", Context.MODE_PRIVATE).getInt(name, 0);
    }
    public static int incInt(Context context, String name){
        SharedPreferences sP = context.getSharedPreferences("vault", Context.MODE_PRIVATE);
        int valor = sP.getInt(name, 0) + 1;
        SharedPreferences.Editor editor = sP.edit();
        editor.putInt(name, valor);
        editor.commit();
        return valor;
    }
    public static void putInt(Context context, String name, int valor){
        SharedPreferences.Editor editor = context.getSharedPreferences("vault", Context.MODE_PRIVATE).edit();
        editor.putInt(name, valor);
        editor.commit();
    }

    public static boolean getBoolean(Context context, String name, boolean valor){
        return context.getSharedPreferences("vault", Context.MODE_PRIVATE).getBoolean(name,valor);

    }
    public static void putBoolean(Context context, String name, boolean valor){
        SharedPreferences.Editor editor = context.getSharedPreferences("vault", Context.MODE_PRIVATE).edit();
        editor.putBoolean(name, valor);
        editor.commit();
    }

}
