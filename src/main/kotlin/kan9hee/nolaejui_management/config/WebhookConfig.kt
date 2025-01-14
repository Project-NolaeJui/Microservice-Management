package kan9hee.nolaejui_management.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "discord")
class WebhookConfig {
    lateinit var webhook:String
}