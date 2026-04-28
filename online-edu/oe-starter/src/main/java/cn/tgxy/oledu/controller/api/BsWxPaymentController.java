package cn.tgxy.oledu.controller.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.binarywang.wxpay.bean.businesscircle.BusinessCircleNotifyData;

import cn.tgxy.oledu.dto.response.OeWxPaymentDto;
import cn.tgxy.oledu.service.OeOrderService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/wxPayment")
public class BsWxPaymentController {
	
	private static Logger log = LoggerFactory.getLogger(BsWxPaymentController.class);
	
	@Autowired
	private OeOrderService orderService;
	
	@PostMapping("/success")
	@Operation(summary="支付成功回调")
	public ResponseEntity<OeWxPaymentDto> success(BusinessCircleNotifyData businessCircleNotifyData){
		log.info(String.format("支付回调地址被调用，参数：%s",businessCircleNotifyData.toString()));
		//TODO:验签
		boolean isWxPayment=true;
		if(isWxPayment) {
			
			return new ResponseEntity<>(HttpStatus.OK);
		}
		else {
			OeWxPaymentDto wxPaymentDto=new OeWxPaymentDto("FAIL","失败");
			return new ResponseEntity<>(wxPaymentDto,HttpStatus.FORBIDDEN);
		}
	}
	
	@PostMapping("/test")
	@Operation(summary = "测试")
	public void test(HttpServletRequest req) throws IOException {
		log.info(req.getContentType());
		
		// 获取请求体的输入流
        try (InputStream inputStream = req.getInputStream()) {
            // 将输入流转换为字符串（适用于JSON、XML等文本格式）
            StringBuilder requestBody = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
            String body = requestBody.toString();
            System.out.println("请求体内容: " + body);
        }
	}
}
