package backend.realestate.paypal.controller;

import backend.realestate.message.response.ResponseMessage;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import backend.realestate.paypal.config.PaypalPaymentIntent;
import backend.realestate.paypal.config.PaypalPaymentMethod;
import backend.realestate.paypal.service.PaypalService;
import backend.realestate.paypal.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@CrossOrigin
public class PaymentController {
	
	public static final String URL_PAYPAL_SUCCESS = "pay/success";
	public static final String URL_PAYPAL_CANCEL = "pay/cancel";
	
	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private PaypalService paypalService;

	@PostMapping("/pay")
	 public ResponseEntity<?> pay( @RequestBody  double price){
		try {
			Payment payment = paypalService.createPayment(
					price, 
					"USD", 
					PaypalPaymentMethod.paypal, 
					PaypalPaymentIntent.sale,
					"payment description",
					"http://homespace.website:8081",
					"http://homespace.website:8081");
			for(Links links : payment.getLinks()){
				if(links.getRel().equals("approval_url")){
//					return new ResponseEntity<String>(links.getHref(), HttpStatus.OK);
					return new ResponseEntity<>(new ResponseMessage(links.getHref()), HttpStatus.OK);
				}
			}
		} catch (PayPalRESTException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}

		return new ResponseEntity<>("redirect:/", HttpStatus.OK);
	}

//	@GetMapping(URL_PAYPAL_CANCEL)
//	public String cancelPay(){
//		return "cancel";
//	}
//
//	@GetMapping(URL_PAYPAL_SUCCESS)
//	public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId){
//		try {
//			Payment payment = paypalService.executePayment(paymentId, payerId);
//			if(payment.getState().equals("approved")){
//				return "success";
//			}
//		} catch (PayPalRESTException e) {
//			log.error(e.getMessage());
//		}
//		return "redirect:/";
//	}
	
}
