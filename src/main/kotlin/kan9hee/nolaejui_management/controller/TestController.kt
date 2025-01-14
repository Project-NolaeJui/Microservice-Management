package kan9hee.nolaejui_management.controller

import kan9hee.nolaejui_management.dto.AdminAccountDto
import kan9hee.nolaejui_management.dto.MusicInfoDto
import kan9hee.nolaejui_management.service.OrderService
import lombok.extern.slf4j.Slf4j
import org.springframework.core.env.Environment
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/management")
@Slf4j
class TestController(private val env: Environment,
                     private val orderService: OrderService) {

    @PostMapping("/changeMusicInfo")
    suspend fun changeMusicInfo(@RequestBody musicInfo: MusicInfoDto){
        orderService.changeMusicInfo(musicInfo)
    }

    @PostMapping("/deleteMusic")
    suspend fun deleteMusic(@RequestBody dataId:Long){
        orderService.deleteMusic(dataId)
    }

    @PostMapping("/disablePlayLog")
    suspend fun disablePlayLog(@RequestBody dataId:Long){
        orderService.disablePlayLog(dataId)
    }

    @PostMapping("/banUser")
    suspend fun banUser(@RequestBody userName:String){
        orderService.banUser(userName)
    }

    @PostMapping("/unbanUser")
    suspend fun unbanUser(@RequestBody userName:String){
        orderService.unbanUser(userName)
    }

    @PostMapping("/deleteUser")
    suspend fun deleteUser(@RequestBody userName:String){
        orderService.deleteUser(userName)
    }

    @PostMapping("/createAdminAccount")
    suspend fun createAdminAccount(@RequestBody adminAccount:AdminAccountDto){
        orderService.createAdminAccount(adminAccount)
    }
}