package moe.ruabbit.mirai.data

import net.mamoe.mirai.console.data.AutoSavePluginData
import net.mamoe.mirai.console.data.value

object SetuData : AutoSavePluginData("setu-data") {
    // 记录群的色图策略
    var groupPolicy: MutableMap<Long, Int> by value(mutableMapOf())
    var saoPolicy: MutableMap<Long, Int> by value(mutableMapOf())
}
