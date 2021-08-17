package moe.ruabbit.mirai.setu

import kotlinx.serialization.Serializable

@Serializable
data class LoliconResponse(
    //val code: Int,
   // val msg: String = "",
  //  val count: Int,
    val error: String,
    val data: List<SetuImageInfo>? = null
) {
    @Serializable
    data class SetuImageInfo(
        val pid: Int,
        val p: Int,
        val uid: Int,
        val title: String,
        val author: String,
        val r18: Boolean,
        val width: Int,
        val height: Int,
        val tags: List<String>,
        val ext: String,
        val uploadDate: Double,
        val urls: Resulturls
    )

    @Serializable
    data class Resulturls(
        val original: String
    )
}
