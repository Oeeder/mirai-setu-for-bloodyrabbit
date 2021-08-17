package moe.ruabbit.mirai.config

import net.mamoe.mirai.console.data.ReadOnlyPluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

object MessageConfig : ReadOnlyPluginConfig("Message") {
    @ValueDescription(
        """
        发送信息的格式
        pid             图片pid
        p               图片号
        r18             是否为r18
        uid             作者uid
        title           题目
        author          作者
        tags            标签
        width           宽度
        height          高度
        ext             格式
        uploadDate      上传日期
        urls            原始地址
        """
    )
    val setuReply by value(
        """
            pid: %pid%
            title: %title%
            author: %author%
            tags: %tags%
            urls: %urls%
        """.trimIndent()
    )

    @ValueDescription("来自 lolicon 的报错, code(错误代码), msg(错误信息)")
    val setuFailureCode401 by value("Api 错误代码：%error%")
    val setuFailureCode404 by value("没有搜索到指定的色图")
    val setuFailureCode429 by value("ApiKey 超过调用上线")
    val setuFailureCodeElse by value("Api 错误代码：%error%")

    @ValueDescription("下载图片出现404自动回复")
    val setuImage404 by value("图片获取失败, 可能图片已经被原作者删除")

    @ValueDescription("群友没有权限的时候的自动回复")
    val setuNoPermission by value("您没有权限操作")

    @ValueDescription("色图插件未启用自动回复")
    val setuModeOff by value("本群尚未开启插件,请联系管理员")

    @ValueDescription("色图插件未启用自动回复")
    val saoModeOff by value("本群尚未开启搜图插件,请联系管理员")

    @ValueDescription("搜色图提醒关键词自动回复")
    val setuSearchKeyNotSet by value("下一条消息请输入搜索的关键词")

    @ValueDescription("关闭色图插件的自动回复")
    val setuOff by value("已经关闭了本群的色图插件")
    val setuOffAlready by value("本群尚未启用色图插件,无需再次禁用")

    @ValueDescription("关闭色图插件的自动回复")
    val saoeOff by value("已经关闭了本群的搜图插件")
    val saoeffAlready by value("本群尚未启用搜图插件,无需再次禁用")

    @ValueDescription("关闭色图插件的自动回复")
    val saoesafe by value("开启本群的搜图插件")
    val saoesafeAleady by value("本群已启用搜图插件,无需启用")

    @ValueDescription("设置普通模式的自动回复")
    val setuSafe by value("切换为普通模式")
    val setuSafeAlready by value("本群色图已经为普通模式, 无需切换")

    @ValueDescription("设置R-18模式的自动回复")
    val setuNsfw by value("切换为R-18模式")
    val setuNsfwAlready by value("本群色图已经为R-18模式, 无需切换")

    @ValueDescription("设置混合模式的自动回复")
    val setuBoth by value("切换为混合模式")
    val setuBothAlready by value("本群色图已经为混合模式, 无需切换")
}
