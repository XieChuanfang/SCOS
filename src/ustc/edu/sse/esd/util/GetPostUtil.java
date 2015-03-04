package ustc.edu.sse.esd.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
/**
 * 网络访问工具类
 * Copyright: Copyright (c) 2015-2-27 20:47:25
 * Company: 中国科学技术大学 软件学院
 * 
 * @author moon：代码编写，star：代码整理
 * @version 5.0
 */
public class GetPostUtil {
	/**
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * @param params
	 *            请求参数，请求参数应该是name1=value1&name2=value2的形式。
	 * @return URL所代表远程资源的响应
	 */
	public static String sendGet(String url, String params) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlName = url + "?" + params;
			URL realUrl = new URL(urlName);
			HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();  // 打开和URL之间的连接
			/*设置通用的请求属性*/
			conn.setRequestProperty("accept", "*/*");  
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			conn.connect();   // 建立实际的连接
			in = new BufferedReader(                 // 定义BufferedReader输入流来读取URL的响应
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += "\n" + line;
			}
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}
	/**  
     * 向指定URL发送POST方法的请求  
     *   
     * @param url  
     *            发送请求的URL  
     * @param params  
     *            请求参数，请求参数应该是name1=value1&name2=value2的形式。  
     * @return URL所代表远程资源的响应  
     */  
    public static String sendPost(String url, String params)  {  
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) realUrl
					.openConnection(); // 打开和URL之间的连接
			/* 设置通用的请求属性 */
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");

			/* 发送POST请求必须设置如下两行 */
			conn.setDoOutput(true);
			conn.setDoInput(true);
			out = new PrintWriter(conn.getOutputStream()); // 获取URLConnection对象对应的输出流
			out.print(params);             // 发送请求参数
			out.flush();                   // flush输出流的缓冲
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream())); // 定义BufferedReader输入流来读取URL的响应
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送POST请求出现异常！" + e);
			e.printStackTrace();
		} finally { 
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		return result;
    } 
}
