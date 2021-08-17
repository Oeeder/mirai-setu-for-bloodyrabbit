package moe.ruabbit.mirai.setu

import moe.ruabbit.mirai.KtorUtils
import moe.ruabbit.mirai.config.MessageConfig
import moe.ruabbit.mirai.config.SettingsConfig
import moe.ruabbit.mirai.data.SetuData
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.util.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.mamoe.mirai.contact.Contact.Companion.sendImage
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.message.MessageReceipt
import net.mamoe.mirai.message.data.MessageSource
import net.mamoe.mirai.message.data.MessageSource.Key.quote
import java.io.InputStream

class FantasyZoneRequester(private val subject: Group, private val source: MessageSource) {

    // 图片数据
    private lateinit var imageResponse: FantasyZoneResponse

    @Throws(Throwable::class)
    @KtorExperimentalAPI
    suspend fun requestSetu(): Boolean {
        try {
            // TODO 增加不使用代理的配置
            imageResponse = Json{coerceInputValues = true}.decodeFromString(
                KtorUtils.proxyClient.get(
                    "https://api.fantasyzone.cc/tu?type=json&class=${
                        SettingsConfig.fantasyZoneType.replace("random",
                            kotlin.run {
                                val random = Math.random()
                                when {
                                    random < 0.33 -> "pc"
                                    random < 0.66 -> "m"
                                    else -> "pixiv"
                                }
                            })
                    }&r18=${SetuData.groupPolicy[subject.id]}"
                )
            )
        } catch (e: Throwable) {
            subject.sendMessage(source.quote() + "出现错误, 请联系管理员检查后台或重试\n${e.message}")
            throw e
        }
        return true
    }

    @Throws(Throwable::class)
    @KtorExperimentalAPI
    suspend fun requestSetu(search: String): Boolean {
        try {
            val jsonResponse: String =
                KtorUtils.proxyClient.get("https://api.fantasyzone.cc/tu/search.php?search=${search}")  //TODO 适配直接取图

            imageResponse = Json{
                coerceInputValues = true
            }.decodeFromString(jsonResponse)

            if (imageResponse.code == 404) {
                subject.sendMessage(source.quote() + "未搜索到图片")
                return false
            }

        } catch (e: Throwable) {
            subject.sendMessage(source.quote() + "出现错误, 请联系管理员检查后台或重试\n${e.message}")
            throw e
        }
        return true
    }

    @Throws(Throwable::class)
    @KtorExperimentalAPI
    suspend fun sendSetu() {
        // 发送信息
        val setuInfoMsg = subject.sendMessage(source.quote() + parseMessage(MessageConfig.setuReply))
        var setuImageMsg: MessageReceipt<Group>? = null
        // 发送setu
        try {
            setuImageMsg = subject.sendImage(getImage())
            // todo 捕获群上传失败的错误信息返回发送失败的信息（涩图被腾讯拦截）
        } catch (e: ClientRequestException) {
            subject.sendMessage(MessageConfig.setuImage404)
        } catch (e: Throwable) {
            subject.sendMessage(source.quote() + "出现错误, 请联系管理员检查后台或重试\n${e.message}")
            throw e
        } finally {
            // 撤回图片
            if (SettingsConfig.autoRecallTime > 0) {
                try {
                    setuImageMsg?.recallIn(millis = SettingsConfig.autoRecallTime)
                } catch (e: Exception) {
                }
                try {
                    setuInfoMsg.recallIn(millis = SettingsConfig.autoRecallTime)
                } catch (e: Exception) {
                }
            }
        }
    }

    // 解析字符串
    private fun parseMessage(message: String): String {
        return message
            .replace("%url%", imageResponse.url)
            .replace("%pid%", imageResponse.id)
            .replace("%p%", null.toString())
            .replace("%uid%", imageResponse.userId)
            .replace("%author%", imageResponse.userName)
            .replace("%title%", imageResponse.title)
            .replace("%url%", imageResponse.toString())
            .replace("%width%", imageResponse.width)
            .replace("%height%", imageResponse.height)
            .replace("%tags%", imageResponse.tags.toString())
    }

    @KtorExperimentalAPI
    suspend fun getImage(): InputStream = KtorUtils.proxyClient.get(imageResponse.url)

}
