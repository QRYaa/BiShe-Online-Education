package cn.tgxy.oledu.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.tgxy.tgbase.common.response.BsResponse;
import cn.tgxy.tgbase.service.BsAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api")
@Tag(name = "SSO")
public class BsSsoClientController {

	@Autowired
	private BsAuthService authService;

	@PostMapping("/logout")
	@Operation(summary = "logout")
	public BsResponse logout(String code, String secret, Long loginId) throws Exception {
		boolean flag = authService.checkCurrentSystemSecret(code, secret);
		if (flag) {
			authService.simpleLogout(loginId);
		}
		return BsResponse.success();
	}
}
