package kan9hee.nolaejui_management.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.lognet.springboot.grpc.GRpcService


@GRpcService
class GrpcService(private val discordService: DiscordService)
    :AdminResponseServerGrpcKt.AdminResponseServerCoroutineImplBase() {

    override suspend fun reportMusicProblem(request: Management.MusicProblem): Management.GrpcResult {
        val jsonObject = JSONObject()
        jsonObject.put("content","음원 신고가 발생했습니다!")
        jsonObject.put("embeds", listOf(JSONObject().apply {
            put("title", "신고 유형: "+request.problemCase)
            put("description", request.problemDetail)
            put("fields", listOf(
                JSONObject().put("name", "음원 ID").put("value", request.musicInfo.musicId),
                JSONObject().put("name", "음원명").put("value", request.musicInfo.musicTitle),
                JSONObject().put("name", "아티스트명").put("value", request.musicInfo.artist),
                JSONObject().put("name", "태그").put("value", request.musicInfo.tagsList),
                JSONObject().put("name", "음원 데이터 타입").put("value", request.musicInfo.dataType),
                JSONObject().put("name", "음원 데이터 url").put("value", request.musicInfo.dataUrl),
                JSONObject().put("name", "재생 가능 여부").put("value", request.musicInfo.isPlayable),
                JSONObject().put("name", "업로드 유저").put("value", request.musicInfo.uploaderName),
                JSONObject().put("name", "업로드 날짜").put("value", request.musicInfo.uploadDate)
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
                JSONObject().put("name", "재생 로그 ID").put("value", request.playLog.logId),
                JSONObject().put("name", "음원 ID").put("value", request.playLog.musicId),
                JSONObject().put("name", "재생 로그 소유 유저").put("value", request.playLog.userName),
                JSONObject().put("name", "경도").put("value", request.playLog.locationInfo.longitude),
                JSONObject().put("name", "위도").put("value", request.playLog.locationInfo.latitude)
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