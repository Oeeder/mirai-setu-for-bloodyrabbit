package moe.ruabbit.mirai.search

import moe.ruabbit.mirai.config.CommandConfig
import io.ktor.util.*
import moe.ruabbit.mirai.PluginMain
import moe.ruabbit.mirai.config.MessageConfig
import moe.ruabbit.mirai.data.SetuData
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.subscribeMessages
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.MessageSource.Key.quote
import net.mamoe.mirai.message.data.time
import net.mamoe.mirai.message.nextMessage
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.event.subscribeGroupMessages

@KtorExperimentalAPI
fun searchListenerRegister() {
    GlobalEventChannel.subscribeGroupMessages {
        always {
            if (message.contentToString().startsWith(CommandConfig.searchByImage)){
                if (SetuData.saoPolicy[group.id] != null){
                    val sauceNao = SauceNaoRequester(subject)
                    val image = message[Image]
                    if (image == null) {
                        subject.sendMessage(message.quote() + "没有图片的说,请在60s内发送图片")
                        val nextMsg = nextMessage()
                        //判断发送的时间
                        if (nextMsg.time - time < 60) {
                            val nextImage = nextMsg[Image]
                            if (nextImage == null) {
                                subject.sendMessage (nextMsg.quote() + "没有获取图片")
                            } else {
                                sauceNao.search(nextImage)
                                sauceNao.sendResult(nextMsg)
                            }
                        }
                    } else {
                        sauceNao.search(image)
                        sauceNao.sendResult(message)
                    }
                }else {
                    group.sendMessage(message.quote() + MessageConfig.saoModeOff)
                }
            }
        }
        always {
            // 关闭搜图插件
            if (CommandConfig.saooff.contains(message.contentToString())) {
                if (PluginMain.checkPermission(sender)) {
                    if (SetuData.saoPolicy[group.id] == null) {
                        group.sendMessage(MessageConfig.saoeffAlready)
                    } else {
                        group.sendMessage(MessageConfig.saoeOff)
                        SetuData.saoPolicy.remove(group.id)
                    }
                } else {
                    group.sendMessage(MessageConfig.setuNoPermission)
                }
            }
            if (CommandConfig.saoon.contains(message.contentToString())) {
                if (PluginMain.checkPermission(sender)) {
                    if (SetuData.saoPolicy[group.id] == 1) {
                        group.sendMessage(MessageConfig.saoesafeAleady)
                    } else {
                        group.sendMessage(MessageConfig.saoesafe)
                        SetuData.saoPolicy[group.id] = 1
                    }
                } else {
                    group.sendMessage(MessageConfig.setuNoPermission)
                }
            }

        }
    }
}


private fun String.startsWith(sauceNao: MutableList<String>): Boolean {
    sauceNao.forEach {
        if (this.startsWith(it))
            return true
    }
    return false
}
