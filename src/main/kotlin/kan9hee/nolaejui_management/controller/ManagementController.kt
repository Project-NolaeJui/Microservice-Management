package kan9hee.nolaejui_management.controller

import kan9hee.nolaejui_management.dto.AdminAccountDto
import kan9hee.nolaejui_management.dto.MusicInfoDto
import kan9hee.nolaejui_management.service.OrderService
import lombok.extern.slf4j.Slf4j
import org.springframework.core.env.Environment
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/management")
@Slf4j
class ManagementController(private val env: Environment,
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
    suspend fun disablePlayLog(@RequestBody dataId:String){
        orderService.disablePlayLog(dataId)
    }

    @PostMapping("/deleteUser")
    suspend fun deleteUser(@RequestBody targetUserName:String){
        orderService.deleteUser(targetUserName)
    }

    @PostMapping("/createAdminAccount")
    suspend fun createAdminAccount(@RequestBody adminAccount:AdminAccountDto){
        orderService.createAdminAccount(adminAccount)
    }
}