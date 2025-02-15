package kan9hee.nolaejui_management.service

import com.google.protobuf.Timestamp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.devh.boot.grpc.server.service.GrpcService
import org.json.JSONArray
import org.json.JSONObject
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

@GrpcService
class GrpcService(private val discordService: DiscordService)
    :AdminResponseServerGrpcKt.AdminResponseServerCoroutineImplBase() {

    override suspend fun reportMusicProblem(request: Management.MusicProblem): Management.GrpcResult {
        val jsonObject = JSONObject()

        val uploadDate = convertProtoTimestampToLocalDateTime(request.musicInfo.uploadDate)
        val fieldsArray = JSONArray().apply {
            put(JSONObject().put("name", "음원 ID").put("value", request.musicInfo.musicId))
            put(JSONObject().put("name", "음원명").put("value", request.musicInfo.musicTitle))
            put(JSONObject().put("name", "아티스트명").put("value", request.musicInfo.artist))
            put(JSONObject().put("name", "태그").put("value", request.musicInfo.tagsList.joinToString(", ")))
            put(JSONObject().put("name", "음원 데이터 타입").put("value", request.musicInfo.dataType))
            put(JSONObject().put("name", "음원 데이터 url").put("value", request.musicInfo.dataUrl))
            put(JSONObject().put("name", "재생 가능 여부").put("value", request.musicInfo.isPlayable))
            put(JSONObject().put("name", "업로드 유저").put("value", request.musicInfo.uploaderName))
            put(JSONObject().put("name", "업로드 날짜").put("value", uploadDate))
        }

        jsonObject.put("content","음원 신고가 발생했습니다!")
        jsonObject.put("embeds", listOf(JSONObject().apply {
            put("title", "신고 유형: " + request.problemCase)
            put("description", request.problemDetail)
            put("fields", fieldsArray)
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
        val fieldsArray = JSONArray().apply {
            JSONObject().put("name", "재생 로그 ID").put("value", request.playLog.logId)
            JSONObject().put("name", "음원 ID").put("value", request.playLog.musicId)
            JSONObject().put("name", "재생 로그 소유 유저").put("value", request.playLog.userName)
            JSONObject().put("name", "경도").put("value", request.playLog.locationInfo.longitude)
            JSONObject().put("name", "위도").put("value", request.playLog.locationInfo.latitude)
        }

        jsonObject.put("content","재생기록 신고가 발생했습니다!")
        jsonObject.put("embeds", listOf(JSONObject().apply {
            put("title", "신고 유형: "+request.problemCase)
            put("description", request.problemDetail)
            put("fields", fieldsArray)
        }))
        discordService.sendMessageToDiscordChannel(jsonObject)

        return withContext(Dispatchers.Default) {
            Management.GrpcResult.newBuilder()
                .setIsSuccess(true)
                .setResultMessage("PlayLog problem reported")
                .build()
        }
    }

    private fun convertProtoTimestampToLocalDateTime(timestamp: Timestamp): LocalDateTime {
        val instant = Instant.ofEpochSecond(timestamp.seconds, timestamp.nanos.toLong())
        return instant.atOffset(ZoneOffset.UTC).toLocalDateTime()  // UTC 기준으로 LocalDateTime 변환
    }
}