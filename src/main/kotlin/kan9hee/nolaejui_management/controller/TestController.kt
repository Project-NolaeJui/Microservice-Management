package kan9hee.nolaejui_management.controller

import jakarta.servlet.http.HttpServletRequest
import lombok.extern.slf4j.Slf4j
import org.springframework.core.env.Environment
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/management")
@Slf4j
class TestController(private val env: Environment) {

    @GetMapping("/welcome")
    fun welcome():String{
        return "This is management server"
    }

    @GetMapping("/check")
    fun check(request: HttpServletRequest):String{
        return String.format("Check management server on PORT %s", env.getProperty("local.server.port"))
    }
}