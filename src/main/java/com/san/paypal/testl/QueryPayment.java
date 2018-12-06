package com.san.paypal.testl;

import java.io.IOException;

import javax.servlet.ServletException;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.san.paypal.config.Client;

/**   
* @author xsansan  
* @date 2018年9月15日 
* Description:  
*/
public class QueryPayment {
	public static void main(String[] args) {
		try {
			new QueryPayment().query();
		} catch (ServletException | IOException e) {
			e.printStackTrace();
		}
	}
	public void query() throws ServletException, IOException {
		try {
			// ### Api Context
			// Pass in a `ApiContext` object to authenticate
			// the call and to send a unique request id
			// (that ensures idempotency). The SDK generates
			// a request id if you do not pass one explicitly.
			APIContext apiContext = new APIContext(Client.clientID, Client.clientSecret, Client.mode);

			// Retrieve the payment object by calling the
			// static `get` method
			// on the Payment class by passing a valid
			// AccessToken and Payment ID
			Payment payment = Payment.get(apiContext, "PAY-8JD97843PD094970KLONDD5I");
			System.out.println(payment);
			//System.out.println(("Payment retrieved ID = " + payment.getId() + ", status = " + payment.getState()));
			
		} catch (PayPalRESTException e) {
			e.getMessage();
		}
	}
}
