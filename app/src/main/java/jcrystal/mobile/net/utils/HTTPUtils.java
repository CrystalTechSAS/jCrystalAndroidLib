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
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

public class HTTPUtils {
    public static void putContent(HttpURLConnection connection, String content) throws IOException {
        OutputStream out = connection.getOutputStream();
        out.write(content.toString().getBytes(Charset.forName("UTF-8")));
        out.flush();
    }
    public static StringBuilder readResponse(InputStream in) throws IOException {
        final char[] buffer = new char[1024];
        final StringBuilder cont = new StringBuilder(1024);
        final java.io.InputStreamReader reader = new java.io.InputStreamReader(in);
        for(int n; (n = reader.read(buffer)) != -1; )cont.append(buffer, 0, n);
        return cont;
    }
    
     public static String getAuthToken(String username, String password){
        String credentials = username + ":" + password;
        String credBase64 = android.util.Base64.encodeToString(credentials.getBytes(), android.util.Base64.DEFAULT).replace("\n", "");
        return "Basic "+credBase64;
    }
}
