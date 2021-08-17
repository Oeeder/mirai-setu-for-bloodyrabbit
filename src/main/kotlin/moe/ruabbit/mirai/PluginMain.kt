package moe.ruabbit.mirai

import moe.ruabbit.mirai.KtorUtils.normalClient
import moe.ruabbit.mirai.config.CommandConfig
import moe.ruabbit.mirai.config.MessageConfig
import moe.ruabbit.mirai.config.SettingsConfig
import moe.ruabbit.mirai.data.SetuData
import moe.ruabbit.mirai.search.searchListenerRegister
import moe.ruabbit.mirai.setu.setuListenerRegister
import io.ktor.client.request.*
import io.ktor.util.*
import kotlinx.coroutines.launch
import net.mamoe.mirai.console.permission.Permission
import net.mamoe.mirai.console.permission.PermissionId
import net.mamoe.mirai.console.permission.PermissionService
import net.mamoe.mirai.console.permission.PermissionService.Companion.hasPermission
import net.mamoe.mirai.console.permission.PermitteeId.Companion.permitteeId
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.console.plugin.name
import net.mamoe.mirai.console.plugin.version
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.contact.isOperator

object PluginMain : KotlinPlugin(
    JvmPluginDescription(
        id = "cn.blrabbit.mirai",
        name = "mirai-setu",
        version = "1.3.0"
    )
) {

    private lateinit var adminPermission: Permission

    @KtorExperimentalAPI
    override fun onEnable() {

        launch {
            checkupdate()
        }

        SettingsConfig.reload() //初始化设置数据
        SetuData.reload()    //初始化配置数据
        CommandConfig.reload()   //初始化插件指令
        MessageConfig.reload()   //初始化自定义回复
        adminPermission = PermissionService.INSTANCE.register(
            PermissionId(name, "admin"),
            "Admin Permission"
        )
        setuListenerRegister()
        searchListenerRegister()
    }

    @KtorExperimentalAPI
    override fun onDisable() {
        // 关闭ktor客户端, 防止堵塞线程无法关闭
        KtorUtils.closeClient()
    }

    // 权限判断（获取以后会搞上更好的方法？）
    fun checkPermission(sender: Member): Boolean {
        when (SettingsConfig.permitMode) {
            0 -> {
                return true
            }
            1 -> {
                if (SettingsConfig.masterId.contains(sender.id))
                    return true
            }
            2 -> {
                if (SettingsConfig.masterId.contains(sender.id))
                    return true
                return sender.isOperator()
            }
            3 -> {
                return sender.permitteeId.hasPermission(adminPermission)
            }
            else -> {
                PluginMain.logger.warning("权限设置信息错误, 请检查权限模式配置")
                return false
            }
        }
        return false
    }

    suspend fun checkupdate() {
        val newversion: String =
            normalClient.get("https://cdn.jsdelivr.net/gh/bloodyrabbit/mirai-setu@main/doc/Version.txt")

        // git给文件加了一个回车我能怎么办呢
        if (newversion.equals(version.toString() + "\n"))
            logger.info("色图插件当前版本:$version")
        else
            logger.warning("色图插件当前版本：$version，检查到新版本：${newversion}请到 https://github.com/bloodyrabbit/mirai-setu/releases 更新")
    }

}
