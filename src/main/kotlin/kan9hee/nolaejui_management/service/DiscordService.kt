package kan9hee.nolaejui_management.service

import kan9hee.nolaejui_management.config.WebhookConfig
import org.json.JSONObject
import org.springframework.http.HttpEntity
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class DiscordService(private val webhookConfig: WebhookConfig) {
    fun sendMessageToDiscordChannel(jsonMessage: JSONObject) {
        val headers = org.springframework.http.HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        val restTemplate = RestTemplate()
        val entity = HttpEntity(jsonMessage.toString(), headers)
        restTemplate.postForObject(webhookConfig.webhook, entity, String::class.java)
    }
}