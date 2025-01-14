package kan9hee.nolaejui_management.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.lognet.springboot.grpc.GRpcService


@GRpcService
class ManagementService(private val discordService: DiscordService)
    :AdminResponseServiceGrpcKt.AdminResponseServiceCoroutineImplBase() {

    override suspend fun reportMusicProblem(request: Management.MusicProblem): Management.GrpcResult {
        val jsonObject = JSONObject()
        jsonObject.put("content","음원 신고가 발생했습니다!")
        jsonObject.put("embeds", listOf(JSONObject().apply {
            put("title", "신고 유형: "+request.problemCase)
            put("description", request.problemDetail)
            put("fields", listOf(
                JSONObject().put("name", "음원 ID").put("value", request.musicInfoList.map { it.musicId }),
                JSONObject().put("name", "음원명").put("value", request.musicInfoList.map { it.musicTitle }),
                JSONObject().put("name", "아티스트명").put("value", request.musicInfoList.map { it.artist }),
                JSONObject().put("name", "태그").put("value", request.musicInfoList.map { it.tagsList }),
                JSONObject().put("name", "음원 데이터 타입").put("value", request.musicInfoList.map { it.dataType }),
                JSONObject().put("name", "음원 데이터 url").put("value", request.musicInfoList.map { it.dataUrl }),
                JSONObject().put("name", "재생 가능 여부").put("value", request.musicInfoList.map { it.isPlayable }),
                JSONObject().put("name", "업로드 유저").put("value", request.musicInfoList.map { it.uploaderName }),
                JSONObject().put("name", "업로드 날짜").put("value", request.musicInfoList.map { it.uploadDate })
            ))
        }))
        discordService.sendMessageToDiscordChannel(jsonObject)

        return withContext(Dispatchers.Default) {
            Management.GrpcResult.newBuilder()
                .setIsSuccess(true)
                .setResultMessage("Music problem reported")
                .build()
        }
    }

    override suspend fun reportPlayLogProblem(request: Management.PlayLogProblem): Management.GrpcResult {
        val jsonObject = JSONObject()
        jsonObject.put("content","재생기록 신고가 발생했습니다!")
        jsonObject.put("embeds", listOf(JSONObject().apply {
            put("title", "신고 유형: "+request.problemCase)
            put("description", request.problemDetail)
            put("fields", listOf(
                JSONObject().put("name", "재생 로그 ID").put("value", request.playLogList.map { it.logId }),
                JSONObject().put("name", "음원 ID").put("value", request.playLogList.map { it.musicId }),
                JSONObject().put("name", "재생 로그 소유 유저").put("value", request.playLogList.map { it.userName }),
                JSONObject().put("name", "경도").put("value", request.playLogList.map { it.longitude }),
                JSONObject().put("name", "위도").put("value", request.playLogList.map { it.latitude })
            ))
        }))
        discordService.sendMessageToDiscordChannel(jsonObject)

        return withContext(Dispatchers.Default) {
            Management.GrpcResult.newBuilder()
                .setIsSuccess(true)
                .setResultMessage("PlayLog problem reported")
                .build()
        }
    }
}