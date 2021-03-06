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
package jcrystal;
import org.json.JSONObject;
import java.util.*;
import java.io.PrintStream;

public class JSONUtils {
    public static String jsonQuote(String valor){
        return JSONObject.quote(valor);
    }
    public static String jsonQuote(long a){return Long.toString(a);}
    public static String jsonQuote(int a){return Integer.toString(a);}
    public static String jsonQuote(int[] a){
        String ret = "[";
        if(a.length>0)ret += a[0];
        for(int e=1;e<a.length;e++)
            ret+=","+a[e];
        return ret+"]";
    }
    public static String jsonQuote(long[] a){
        String ret = "[";
        if(a.length>0)ret += a[0];
        for(int e=1;e<a.length;e++)
            ret+=","+a[e];
        return ret+"]";
    }
    public static String jsonQuote(byte[] array){
        return jsonQuote(android.util.Base64.encodeToString(array, android.util.Base64.NO_WRAP|android.util.Base64.NO_CLOSE));
    }
    public static String jsonQuote(double[][] a){
        String ret = "[";
        boolean pe = false;
        for(int e = 0; e < a.length; e++, pe = true) {
            if(pe)
                ret+=",[";
            else ret+="[";
            boolean pi = false;
            for (int i = 0; i < a[e].length; i++, pi = true) {
                if(pi)
                    ret+=",";
                ret+= doubleToString(a[e][i]);
            }
            ret+="]";
        }
        return ret+"]";
    }

    public static String jsonQuote(double[] a){
        String ret = "[";
        boolean pi = false;
        for (int i = 0; i < a.length; i++, pi = true) {
            if(pi)
                ret+=","+doubleToString(a[i]);
            else ret+= doubleToString(a[i]);
        }
        return ret+"]";
    }
    public static String jsonQuote(boolean[] a){
        String ret = "[";
        boolean pi = false;
        for (int i = 0; i < a.length; i++, pi = true) {
            if(pi)
                ret+=","+Boolean.toString(a[i]);
            else ret+= Boolean.toString(a[i]);
        }
        return ret+"]";
    }
    public static String jsonQuote(double a){return Double.toString(a);}
    public static String jsonQuote(boolean a){return Boolean.toString(a);}
    private static String doubleToString(double number){
        String string = Double.toString(number);
        if(string.indexOf(46) > 0 && string.indexOf(101) < 0 && string.indexOf(69) < 0) {
            while(string.endsWith("0")) {
                string = string.substring(0, string.length() - 1);
            }

            if(string.endsWith(".")) {
                string = string.substring(0, string.length() - 1);
            }
        }
        return string;
    }
    public static String jsonQuoteString(List<String> puntos){
        Iterator<String> it = puntos.iterator();
        String ret = "[";
        if(it.hasNext())ret += jsonQuote(it.next());
        while(it.hasNext())
        		ret += ","+jsonQuote(it.next());
        return ret+"]";
    }
    public static void jsonQuoteString(PrintStream _pw, List<String> puntos){
        Iterator<String> it = puntos.iterator();
        _pw.print("[");
        if(it.hasNext())_pw.print(jsonQuote(it.next()));
        while(it.hasNext()) {
            _pw.print(",");
            _pw.print(jsonQuote(it.next()));
        }
        _pw.print("]");
    }
    public static String jsonQuoteLong(List<Long> puntos){
        Iterator<Long> it = puntos.iterator();
        String ret = "[";
        if(it.hasNext())ret += it.next();
        while(it.hasNext())
            ret += ","+it.next();
        return ret+"]";
    }
    public static void jsonQuoteLong(PrintStream _pw, List<Long> puntos){
        Iterator<Long> it = puntos.iterator();
        _pw.print("[");
        if(it.hasNext())_pw.print(it.next());
        while(it.hasNext()) {
            _pw.print(",");
            _pw.print(it.next());
        }
        _pw.print("]");
    }
    public static void jsonQuoteInteger(PrintStream _pw, List<Integer> puntos){
        Iterator<Integer> it = puntos.iterator();
        _pw.print("[");
        if(it.hasNext())_pw.print(it.next());
        while(it.hasNext()) {
            _pw.print(",");
            _pw.print(it.next());
        }
        _pw.print("]");
    }
    public static Integer parseInt(String val){
        if(val==null)return null;
        try{
            return Integer.parseInt(val);
        }catch (Exception ex){
            //TODO: Es mejor verificar que sea un número a lanzar expceciones, piense en el costo del objeto exception
        }
        return null;
    }
    public static Date parcelDate(String val){
        if(val==null)return null;
        try{
            return new Date(Long.parseLong(val));
        }catch (Exception ex){
            //TODO: Es mejor verificar que sea un número a lanzar expceciones, piense en el costo del objeto exception
        }
        return null;
    }
}
