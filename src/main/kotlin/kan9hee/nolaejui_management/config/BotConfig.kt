package kan9hee.nolaejui_management.config

import kan9hee.nolaejui_management.service.DiscordService
import kan9hee.nolaejui_management.service.GrpcService
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BotConfig(private val applicationContext: ApplicationContext) {

    @Bean
    fun managementService(): GrpcService {
        return GrpcService(
            DiscordService(
                WebhookConfig()))
    }
}