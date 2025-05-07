package kan9hee.nolaejui_management.service

import com.google.protobuf.Timestamp
import kan9hee.nolaejui_management.dto.discord.DiscordEmbed
import kan9hee.nolaejui_management.dto.discord.DiscordField
import kan9hee.nolaejui_management.dto.discord.DiscordMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.devh.boot.grpc.server.service.GrpcService
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

@GrpcService
class GrpcService(private val discordService: DiscordService)
    :AdminResponseServerGrpcKt.AdminResponseServerCoroutineImplBase() {

    override suspend fun reportMusicProblem(request: Management.MusicProblem): Management.GrpcResult {
        val uploadDate = convertProtoTimestampToLocalDateTime(request.musicInfo.uploadDate)
        val fields = listOf(
            DiscordField("음원 ID",request.musicInfo.musicId),
            DiscordField("음원명",request.musicInfo.musicTitle),
            DiscordField("아티스트명",request.musicInfo.artist),
            DiscordField("태그",request.musicInfo.musicId),
            DiscordField("음원 데이터 타입",request.musicInfo.dataType),
            DiscordField("음원 데이터 url",request.musicInfo.dataUrl),
            DiscordField("재생 가능 여부",request.musicInfo.isPlayable),
            DiscordField("업로드 유저",request.musicInfo.uploaderName),
            DiscordField("업로드 날짜",uploadDate)
        )
        val embed = DiscordEmbed("신고 유형: ${request.problemCase}", request.problemDetail, fields)
        val message = DiscordMessage("음원 신고가 발생했습니다!",listOf(embed))

        val jsonBody = jacksonObjectMapper().writeValueAsString(message)
        discordService.sendMessageToDiscordChannel(jsonBody)

        return withContext(Dispatchers.Default) {
            Management.GrpcResult.newBuilder()
                .setIsSuccess(true)
                .setResultMessage("Music problem reported")
                .build()
        }
    }

    override suspend fun reportPlayLogProblem(request: Management.PlayLogProblem): Management.GrpcResult {
        val fields = listOf(
            DiscordField("재생 로그 ID",request.playLog.logId),
            DiscordField("음원 ID",request.playLog.musicId),
            DiscordField("재생 로그 소유 유저",request.playLog.userName),
            DiscordField("경도",request.playLog.locationInfo.longitude),
            DiscordField("위도",request.playLog.locationInfo.latitude)
        )
        val embed = DiscordEmbed("신고 유형: ${request.problemCase}", request.problemDetail, fields)
        val message = DiscordMessage("재생기록 신고가 발생했습니다!",listOf(embed))

        val jsonBody = jacksonObjectMapper().writeValueAsString(message)
        discordService.sendMessageToDiscordChannel(jsonBody)

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