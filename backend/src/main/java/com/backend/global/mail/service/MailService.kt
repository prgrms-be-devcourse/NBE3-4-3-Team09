package com.backend.global.mail.service;

import com.backend.global.mail.util.TemplateName;
import java.util.List;

/**
 * MailService
 * <p>메일 전송 서비스 인터페이스 입니다.</p>
 *
 * @author Kim Dong O
 */
public interface MailService {

	/**
	 * @param to           받는 사람 이메일들의 이메일
	 * @param templateName 템플릿 이름
	 * @param postId       게시글 ID
	 * @implSpec 비동기로 다수의 회원에게 이메일을 전송 합니다.
	 */
	void sendDeliveryStartEmail(List<String> to, TemplateName templateName, Long postId);
}
