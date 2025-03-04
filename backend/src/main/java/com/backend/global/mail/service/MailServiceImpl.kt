package com.backend.global.mail.service;


import com.backend.global.mail.util.MailSender
import com.backend.global.mail.util.TemplateMaker
import com.backend.global.mail.util.TemplateName
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

private val log = KotlinLogging.logger {}

@Service
class MailServiceImpl : MailService {
    @Value("\${mail.chat_url}")
    lateinit var chatUrl: String
    lateinit var templateMaker: TemplateMaker
    lateinit var mailSender: MailSender

    @Async("threadPoolTaskExecutor")
    override fun sendDeliveryStartEmail(to:List<String>, templateName: TemplateName, postId: Long) {
        val titleBuilder = StringBuilder()
        val htmlParameterMap = mutableMapOf<String, String>()

        when (templateName) {
            TemplateName.RECRUITMENT_CHAT -> titleBuilder.append("[TEAM9] 모집 완료 안내 메일 입니다.")
        }

        val title = titleBuilder.toString()
        val chatUrlToString = "$chatUrl$postId"

        htmlParameterMap["chatUrl"] = chatUrlToString

        log.info { htmlParameterMap["chatUrl"] }

        val mimeMessage =
            templateMaker.create(mailSender.createMimeMessage(), to, title, templateName, htmlParameterMap)

        mailSender.send(mimeMessage)
    }
}
