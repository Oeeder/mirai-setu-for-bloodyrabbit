package moe.ruabbit.mirai.setu

import kotlinx.serialization.Serializable

@Serializable
data class FantasyZoneResponse(
    val id: String = "null",
    val code: Int = 200,
    val description: String = "null",
    val userId: String = "null",
    val userName: String = "null",
    val title: String = "null",
    val tags: List<String>  = listOf(),
    val width: String = "null",
    val height: String = "null",
    val url: String = "null"
)
