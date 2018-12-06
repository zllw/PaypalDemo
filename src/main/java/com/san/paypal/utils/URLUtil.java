package com.san.paypal.utils;

import javax.servlet.http.HttpServletRequest;

/**   
* @author xsansan  
* @date 2018年9月12日 
* Description:  
*/
public class URLUtil
{
	public static String getBaseURl(HttpServletRequest request)
	{
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        String contextPath = request.getContextPath();
        StringBuffer url =  new StringBuffer();
        url.append(scheme).append("://").append(serverName);
        if ((serverPort != 80) && (serverPort != 443))
        {
            url.append(":").append(serverPort);
        }
        url.append(contextPath);
        if(url.toString().endsWith("/")) // error code?
        {
            url.append("/");
        }
        System.out.println("URL：" + url.toString());
        return url.toString();
    }
}
