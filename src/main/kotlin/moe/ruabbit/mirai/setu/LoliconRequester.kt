package moe.ruabbit.mirai.setu

import moe.ruabbit.mirai.KtorUtils
import moe.ruabbit.mirai.PluginMain
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
import net.mamoe.mirai.utils.error
import java.io.InputStream

class LoliconRequester(private val subject: Group, private val source: MessageSource) {
    // 图片数据
    private lateinit var imageResponse: LoliconResponse.SetuImageInfo

    @Throws(Throwable::class)
    @KtorExperimentalAPI
    suspend fun requestSetu(): Boolean {
        try {
            val response: String =
                KtorUtils.proxyClient.get(
                    "http://api.lolicon.app/setu/v2?r18=${
                        SetuData.groupPolicy[subject.id]
                    }"
                )
            parseSetu(response)
        } catch (e: RemoteApiException) {
            subject.sendMessage(source.quote() + "出现错误: ${e.message}")
            return false
        } catch (e: Throwable) {
            //PluginMain.logger.error{imageResponse.urls.original.replace("i.pixiv.cat", SettingsConfig.domainProxy)}
            subject.sendMessage(source.quote() + "出现错误, 请联系机器人管理员或者更换搜索TAG进行尝试\n${e.message}\nhttp://api.lolicon.app/setu/v2?r18=${SetuData.groupPolicy[subject.id]}")
            throw e
        }
        return true
    }

    @Throws(Throwable::class)
    @KtorExperimentalAPI
    suspend fun requestSetu(keyword: String): Boolean {
        try {
            val setuResponse: String =
                KtorUtils.proxyClient.get(
                    "http://api.lolicon.app/setu/v2?keyword=${keyword}&r18=${
                        SetuData.groupPolicy[subject.id]
                    }"
                )
            parseSetu(setuResponse)
        } catch (e: RemoteApiException) {
            subject.sendMessage(source.quote() + "出现错误: ${e.message}")
            return false
        } catch (e: Throwable) {
            subject.sendMessage(source.quote() + "请求API时出现错误,请联系管理员\n${e.message}\nhttp://api.lolicon.app/setu/v2?tag=${keyword}&r18=${SetuData.groupPolicy[subject.id]}")
            throw e
        }
        return true
    }

    @Throws(Throwable::class)
    private fun parseSetu(rawJson: String) {
        val loliconResponse: LoliconResponse = Json.decodeFromString(rawJson)

        fun parseErrCode(message: String): String {
            return message
              //  .replace("%code%", loliconResponse.code.toString())
                .replace("%error%", loliconResponse.error)
        }


        when (loliconResponse.data) {
            null -> {
                PluginMain.logger.error { "lolicon 错误信息：${loliconResponse.error}${loliconResponse.data}" }
                throw RemoteApiException(parseErrCode(MessageConfig.setuFailureCode404))
            }
            // -1和403 错误等一系列未知错误
            else -> {
                /*PluginMain.logger.error { "发生此错误请到github反馈错误 lolicon错误代码：${loliconResponse.code} 错误信息：${loliconResponse.msg}" }
                throw RemoteApiException(parseErrCode(MessageConfig.setuFailureCodeElse))*/
                //PluginMain.logger.error{imageResponse.urls.original.replace("i.pixiv.cat", SettingsConfig.domainProxy)}
                loliconResponse.data?.get(0)?.let {
                    imageResponse = it
                }
                PluginMain.logger.error { "输出日志：\n${loliconResponse.error}${loliconResponse.data}" }
            }
        }
    }

    // 解析字符串
    private fun parseMessage(message: String): String {
        return message
            .replace("%pid%", imageResponse.pid.toString())
            .replace("%p%", imageResponse.p.toString())
            .replace("%r18%", imageResponse.r18.toString())
            .replace("%uid%", imageResponse.uid.toString())
            .replace("%title%", imageResponse.title)
            .replace("%author%", imageResponse.author)
            .replace("%tags%", imageResponse.tags.toString())
            .replace("%width%", imageResponse.width.toString())
            .replace("%height%", imageResponse.height.toString())
            .replace("%ext%", imageResponse.ext)
            .replace("%uploadDate%", imageResponse.uploadDate.toString())
            .replace("%urls%", imageResponse.urls.original)
    }

    @KtorExperimentalAPI
    suspend fun getImage(): InputStream =
        KtorUtils.proxyClient.get(imageResponse.urls.original.replace("i.pixiv.cat", SettingsConfig.domainProxy).replace("img-original","img-master").replace(".jpg","_master1200.jpg").replace(".png","_master1200.jpg")) {
            headers.append("referer", "https://www.pixiv.net/")
        }

    @Throws(Throwable::class)
    @KtorExperimentalAPI
    suspend fun sendSetu() {
        // 发送信息
        //val setuInfoMsg = subject.sendMessage(source.quote() + parseMessage(MessageConfig.setuReply))
        var setuImageMsg: MessageReceipt<Group>? = null
        // 发送setu
        try {
            setuImageMsg = subject.sendImage(getImage())
            PluginMain.logger.error{imageResponse.urls.original.replace("i.pixiv.cat", SettingsConfig.domainProxy)}
            /*PluginMain.logger.error{SettingsConfig.socksProxy.toString()}
            PluginMain.logger.error{SettingsConfig.httpProxy.toString()}
            PluginMain.logger.error{SettingsConfig.proxyConfig.toString()}*/
            // todo 捕获群上传失败的错误信息返回发送失败的信息（涩图被腾讯拦截）
        } catch (e: ClientRequestException) {
            subject.sendMessage(MessageConfig.setuImage404)
            PluginMain.logger.error{imageResponse.urls.original.replace("i.pixiv.cat", SettingsConfig.domainProxy)}
            //PluginMain.logger.error{SettingsConfig.socksProxy.toString()}
            //PluginMain.logger.error{SettingsConfig.httpProxy.toString()}
           // PluginMain.logger.error{SettingsConfig.proxyConfig.toString()}
        } catch (e: Throwable) {
            subject.sendMessage(source.quote() + "请联系管理员或者更换搜索TAG进行尝试\n${e.message}\n${imageResponse.urls.original.replace("i.pixiv.cat", SettingsConfig.domainProxy)}")
            //PluginMain.logger.error{imageResponse.urls.original.replace("i.pixiv.cat", SettingsConfig.domainProxy)}
            //PluginMain.logger.error{SettingsConfig.socksProxy.toString()}
           // PluginMain.logger.error{SettingsConfig.httpProxy.toString()}
           // PluginMain.logger.error{SettingsConfig.proxyConfig.toString()}
            throw e
        } finally {
            // 撤回图片
            if (SettingsConfig.autoRecallTime > 0) {
                try {
                    setuImageMsg?.recallIn(millis = SettingsConfig.autoRecallTime)
                } catch (e: Exception) {
                }
                /*try {
                    setuInfoMsg.recallIn(millis = SettingsConfig.autoRecallTime)
                } catch (e: Exception) {
                }*/
            }
        }
    }

}
