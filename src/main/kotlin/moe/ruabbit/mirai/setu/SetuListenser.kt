package moe.ruabbit.mirai.setu

import moe.ruabbit.mirai.PluginMain.checkPermission
import moe.ruabbit.mirai.config.CommandConfig
import moe.ruabbit.mirai.config.MessageConfig
import moe.ruabbit.mirai.config.SettingsConfig
import moe.ruabbit.mirai.data.SetuData
import io.ktor.util.*
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.subscribeGroupMessages
import net.mamoe.mirai.message.data.MessageSource.Key.quote
import net.mamoe.mirai.message.nextMessage

@KtorExperimentalAPI
fun setuListenerRegister() {
    GlobalEventChannel.subscribeGroupMessages {

        //色图时间
        always {
            if (CommandConfig.get.contains(message.contentToString())) {
                if (SetuData.groupPolicy[group.id] != null) {
                    when (SettingsConfig.requestApi) {
                        "FantasyZoneApi" -> {
                            val setu = FantasyZoneRequester(group, source)
                            //group.sendMessage(message.quote() + "Fetching setu...")
                            if (setu.requestSetu())
                                setu.sendSetu()
                        }
                        "LoliconApi" -> {
                            val setu = LoliconRequester(group, source)
                            group.sendMessage(message.quote() + "Fetching setu...")
                            if (setu.requestSetu())
                                setu.sendSetu()
                        }
                        else -> {
                            group.sendMessage(message.quote() + "requestApi 配置错误")
                        }
                    }
                } else {
                    group.sendMessage(message.quote() + MessageConfig.setuModeOff)
                }
            }
        }

        //搜色图
        always {
            CommandConfig.search.startWith(message.contentToString()).let {
                if (it != null) {
                    if (SetuData.groupPolicy[group.id] != null) {
                        val msg = it.ifEmpty {
                            group.sendMessage(message.quote() + MessageConfig.setuSearchKeyNotSet)
                            nextMessage().contentToString()
                        }
                        when (SettingsConfig.searchApi) {
                            "FantasyZoneApi" -> {
                                val setu = FantasyZoneRequester(group, source)
                                group.sendMessage(message.quote() + "Fetching setu...")
                                if (setu.requestSetu(msg))
                                    setu.sendSetu()
                            }
                            "LoliconApi" -> {
                                val setu = LoliconRequester(group, source)
                                //group.sendMessage(message.quote() + "Fetching setu...")
                                if (setu.requestSetu(msg))
                                    setu.sendSetu()
                            }
                            else -> {
                                group.sendMessage(message.quote() + "requestApi 配置错误")
                            }
                        }
                    } else {
                        group.sendMessage(message.quote() + MessageConfig.setuModeOff)
                    }
                }
            }
        }
        //来色图
        always {
            if (CommandConfig.come.contains(message.contentToString())) {
                if (SetuData.groupPolicy[group.id] != null) {
                    when (SettingsConfig.requestApi) {
                        "FantasyZoneApi" -> {
                            val setu = FantasyZoneRequester(group, source)
                            //group.sendMessage(message.quote() + "Fetching setu...")
                            if (setu.requestSetu())
                                setu.sendSetu()
                        }
                        "LoliconApi" -> {
                            val setu = LoliconRequester(group, source)
                            //group.sendMessage(message.quote() + "Fetching setu...")
                            if (setu.requestSetu())
                                setu.sendSetu()
                        }
                        else -> {
                            group.sendMessage(message.quote() + "requestApi 配置错误")
                        }
                    }
                } else {
                    group.sendMessage(message.quote() + MessageConfig.setuModeOff)
                }
            }
        }
        // 涩图插件开关控制
        always {
            // 关闭涩图插件
            if (CommandConfig.off.contains(message.contentToString())) {
                if (checkPermission(sender)) {
                    if (SetuData.groupPolicy[group.id] == null) {
                        group.sendMessage(MessageConfig.setuOffAlready)
                    } else {
                        group.sendMessage(MessageConfig.setuOff)
                        SetuData.groupPolicy.remove(group.id)
                    }
                } else {
                    group.sendMessage(MessageConfig.setuNoPermission)
                }
            }
            // 设置为普通模式
            if (CommandConfig.setSafeMode.contains(message.contentToString())) {
                if (checkPermission(sender)) {
                    if (SetuData.groupPolicy[group.id] == 0) {
                        group.sendMessage(MessageConfig.setuSafeAlready)
                    } else {
                        group.sendMessage(MessageConfig.setuSafe)
                        SetuData.groupPolicy[group.id] = 0
                    }
                } else {
                    group.sendMessage(MessageConfig.setuNoPermission)
                }
            }
            // 设置为R-18模式
            if (CommandConfig.setNsfwMode.contains(message.contentToString())) {
                if (checkPermission(sender)) {
                    if (SetuData.groupPolicy[group.id] == 1) {
                        group.sendMessage(MessageConfig.setuNsfwAlready)
                    } else {
                        group.sendMessage(MessageConfig.setuNsfw)
                        SetuData.groupPolicy[group.id] = 1
                    }
                } else {
                    group.sendMessage(MessageConfig.setuNoPermission)
                }
            }
            // 设置为混合模式
            if (CommandConfig.setBothMode.contains(message.contentToString())) {
                if (checkPermission(sender)) {
                    if (SetuData.groupPolicy[group.id] == 2) {
                        group.sendMessage(MessageConfig.setuBothAlready)
                    } else {
                        group.sendMessage(MessageConfig.setuBoth)
                        SetuData.groupPolicy[group.id] = 2
                    }
                } else {
                    group.sendMessage(MessageConfig.setuNoPermission)
                }
            }
        }
    }
}

// 判断词组开头是否包含，不包含返回null
private fun MutableList<String>.startWith(contentToString: String): String? {
    this.forEach {
        if (contentToString.startsWith(it)) {
            return contentToString.replace(it, "").replace(" ", " ")
        }
    }
    return null
}
