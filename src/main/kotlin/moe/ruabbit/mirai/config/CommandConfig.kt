package moe.ruabbit.mirai.config

import net.mamoe.mirai.console.data.ReadOnlyPluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

object CommandConfig : ReadOnlyPluginConfig("Command") {
    @ValueDescription("修改触发的指令")
    val get: MutableList<String> by value(mutableListOf("色图时间", "涩图时间", "涩图来", "色图来", "来张色图"))
    val off: MutableList<String> by value(mutableListOf("关闭插件", "封印"))
    val saooff: MutableList<String> by value(mutableListOf("关闭搜图"))
    val saoon: MutableList<String> by value(mutableListOf("开启搜图"))
    val search: MutableList<String> by value(mutableListOf("搜色图", "搜涩图"))
    val come: MutableList<String> by value(mutableListOf("来色图"))
    val searchByImage: MutableList<String> by value(mutableListOf("以图搜图", "搜图"))
    val setSafeMode: MutableList<String> by value(mutableListOf("普通模式", "青少年模式"))
    val setNsfwMode: MutableList<String> by value(mutableListOf("R-18模式", "青壮年模式"))
    val setBothMode: MutableList<String> by value(mutableListOf("混合模式"))
}
