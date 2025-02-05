package kan9hee.nolaejui_management.service

import AuthServerGrpcKt
import MusicListServerGrpcKt
import PlayLogServerGrpcKt
import com.google.protobuf.Timestamp
import kan9hee.nolaejui_management.dto.AdminAccountDto
import kan9hee.nolaejui_management.dto.MusicInfoDto
import net.devh.boot.grpc.client.inject.GrpcClient
import org.json.JSONObject
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.ZoneId

@Service
class OrderService(@GrpcClient("nolaejui-auth")
                   private val authStub: AuthServerGrpcKt.AuthServerCoroutineStub,
                   @GrpcClient("nolaejui-playlist")
                   private val musicListStub: MusicListServerGrpcKt.MusicListServerCoroutineStub,
                   @GrpcClient("nolaejui-location")
                   private val playLogStub: PlayLogServerGrpcKt.PlayLogServerCoroutineStub,
                   private val discordService: DiscordService) {

    suspend fun deleteMusic(idValue:Long){
        val request = Management.MusicDataId.newBuilder()
            .setId(idValue)
            .build()

        val response = musicListStub.deleteMusic(request)
        resultReportToAdmin("음원 삭제 ",response)
    }

    suspend fun changeMusicInfo(musicInfo:MusicInfoDto){
        val request = Management.MusicInfo.newBuilder()
            .setMusicId(musicInfo.musicId)
            .setMusicTitle(musicInfo.musicTitle)
            .setArtist(musicInfo.artist)
            .addAllTags(musicInfo.tags)
            .setDataType(musicInfo.dataType)
            .setDataUrl(musicInfo.dataUrl)
            .setIsPlayable(musicInfo.isPlayable)
            .setUploaderName(musicInfo.uploaderName)
            .setUploadDate(convertLocalDateToProtoTimestamp(musicInfo.uploadDate))
            .build()

        val response = musicListStub.changeMusicInfo(request)
        resultReportToAdmin("음원 정보 변경 ",response)
    }

    suspend fun disablePlayLog(idValue:String){
        val request = Management.PlayLogId.newBuilder()
            .setId(idValue)
            .build()

        val response = playLogStub.deletePlayLog(request)
        resultReportToAdmin("재생 기록 비활성화 ",response)
    }

    suspend fun banUser(name:String){
        val request = Management.UserName.newBuilder()
            .setUserName(name)
            .build()

        val response = authStub.banUser(request)
        resultReportToAdmin("계정 차단 ",response)
    }

    suspend fun unbanUser(name:String){
        val request = Management.UserName.newBuilder()
            .setUserName(name)
            .build()

        val response = authStub.unbanUser(request)
        resultReportToAdmin("계정 해지 ",response)
    }

    suspend fun deleteUser(name:String){
        val request = Management.UserName.newBuilder()
            .setUserName(name)
            .build()

        val response = authStub.deleteUser(request)
        resultReportToAdmin("계정 삭제 ",response)
    }
    suspend fun createAdminAccount(adminAccount: AdminAccountDto){
        val request = Management.AdminAccount.newBuilder()
            .setAdminId(adminAccount.adminId)
            .setAdminPassword(adminAccount.adminPassword)
            .build()

        val response = authStub.createAdminAccount(request)
        resultReportToAdmin("관리자 계정 생성 ",response)
    }

    private fun resultReportToAdmin(actName:String,result: Management.GrpcResult){
        val jsonObject = JSONObject()
        jsonObject.put("content",actName+"처리 동작이 끝났습니다.")
        jsonObject.put("embeds", listOf(JSONObject().apply {
            put("title", "처리 결과: "+result.isSuccess)
            put("description", result.resultMessage)
        }))

        discordService.sendMessageToDiscordChannel(jsonObject)
    }

    private fun convertLocalDateToProtoTimestamp(localDate: LocalDate): Timestamp {
        val instant = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()
        return Timestamp.newBuilder()
            .setSeconds(instant.epochSecond)
            .setNanos(instant.nano)
            .build()
    }
}