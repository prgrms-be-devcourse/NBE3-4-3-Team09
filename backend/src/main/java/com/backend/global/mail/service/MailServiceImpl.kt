package com.backend.global.mail.service;

import com.backend.global.mail.util.MailSender;
import com.backend.global.mail.util.TemplateMaker;
import com.backend.global.mail.util.TemplateName;
import jakarta.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailServiceImpl implements MailService {
	@Value("${mail.chat-url}")
	private String chatUrl;
	private final TemplateMaker templateMaker;
	private final MailSender mailSender;

	@Async("threadPoolTaskExecutor")
	@Override
	public void sendDeliveryStartEmail(List<String> to, TemplateName templateName, Long postId) {
		StringBuilder titleBuilder = new StringBuilder();
		Map<String, String> htmlParameterMap = new HashMap<>();

		switch (templateName) {
			case RECRUITMENT_CHAT -> {
				titleBuilder.append("[TEAM9] 모집 완료 안내 메일 입니다.");
			}
		}

		String title = titleBuilder.toString();
		String chatUrlToString = chatUrl + postId;

		htmlParameterMap.put("chatUrl", chatUrlToString);

		log.info(htmlParameterMap.get("chatUrl"));

		MimeMessage mimeMessage = templateMaker
			.create(mailSender.createMimeMessage(), to, title, templateName, htmlParameterMap);

		mailSender.send(mimeMessage);
	}
}
