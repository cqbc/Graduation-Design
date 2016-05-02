package com.cqupt.common.utils;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;

 

/**  
 * 通过cxf设置web service client的超时时间
 *  
 * @author zel
 *    
 */
public class CxfTimeoutOperatorUtil {
	// 设置client的超时时间
	public static void setTimeout(Object aidService, int connectionTimeout,
			int receiveTimeout) {
		Client proxy = ClientProxy.getClient(aidService);
		HTTPConduit conduit = (HTTPConduit) proxy.getConduit();
		HTTPClientPolicy policy = new HTTPClientPolicy();
		conduit.setClient(policy);
		policy.setConnectionTimeout(connectionTimeout);
		policy.setReceiveTimeout(receiveTimeout);
	}
}
