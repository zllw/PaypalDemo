package com.san.paypal.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.san.paypal.config.PaypalPaymentIntent;
import com.san.paypal.config.PaypalPaymentMethod;
import com.san.paypal.service.PaypalService;
import com.san.paypal.utils.URLUtil;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

/**   
* @author xsansan  
* @date 2018年9月12日 
* Description:  
*/
@Controller
@RequestMapping("/")
public class PaymentController {

    public static final String PAYPAL_SUCCESS_URL = "pay/success";
    public static final String PAYPAL_CANCEL_URL = "pay/cancel";

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private PaypalService paypalService;

    /**
     * 跳入支付按钮页
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public String index(){
        return "index";
    }

    /**
     * 发起支付请求
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "pay")
    public String pay(HttpServletRequest request) {
        String cancelUrl = URLUtil.getBaseURl(request) + "/" + PAYPAL_CANCEL_URL;//请求支付传入的参数（取消支付回调地址）  发现可以乱填，待研究
        String successUrl = URLUtil.getBaseURl(request) + "/" + PAYPAL_SUCCESS_URL;//请求支付传入的参数（支付成功回调地址）  发现可以乱填，待研究
        try {
        	// payment发起请求的请求数据对象,total为请求支付总金额，currency货币类型（USD为美元），description为订单描述
            Payment payment = paypalService.createPayment(
                    500.00, 
                    "USD", 
                    PaypalPaymentMethod.paypal, 
                    PaypalPaymentIntent.sale,
                    "payment description", 
                    cancelUrl, 
                    successUrl);
            System.out.println("createPayment=" + payment);
            for(Links links : payment.getLinks()){
                if(links.getRel().equalsIgnoreCase("approval_url")) {
                    return "redirect:" + links.getHref();
                }
            }
        } catch (PayPalRESTException e) {
            log.error(e.getMessage());
            System.out.println(e.getDetails());
        }
        return "redirect:/";
    }

    @RequestMapping(method = RequestMethod.GET, value = PAYPAL_CANCEL_URL)
    public String cancelPay() {
        return "cancel";
    }

    @RequestMapping(method = RequestMethod.GET, value = PAYPAL_SUCCESS_URL)
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {//继续支付请求方法,paymentId,payerId这两个参数是请求支付方法成功回调自动传入
        try {
        	System.out.println("paymentId=" + paymentId + ",payerId=" + payerId);
            Payment payment = paypalService.executePayment(paymentId, payerId);//payment 支付返回相关信息，其中payer为买家相关信息，
            System.out.println("payment" + payment);
            if(payment.getState().equals("approved")) {
                return "test";//交易成功回调地址
            }
        } catch (PayPalRESTException e) {
            log.error(e.getMessage());
            System.out.println(e.getDetails());
        }
        return "redirect:/";
    }

}
