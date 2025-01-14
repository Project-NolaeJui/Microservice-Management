package kan9hee.nolaejui_management.dto

import java.time.LocalDate

data class MusicInfoDto(
    val musicId:Long,
    val musicTitle:String,
    val artist:String,
    val tags:List<String>,
    val dataType:String,
    val dataUrl:String,
    val isPlayable:Boolean,
    val uploaderName:String,
    val uploadDate: LocalDate
)
