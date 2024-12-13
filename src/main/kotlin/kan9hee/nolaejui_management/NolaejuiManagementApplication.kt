package kan9hee.nolaejui_management

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@SpringBootApplication
@EnableDiscoveryClient
class NolaejuiManagementApplication

fun main(args: Array<String>) {
	runApplication<NolaejuiManagementApplication>(*args)
}
