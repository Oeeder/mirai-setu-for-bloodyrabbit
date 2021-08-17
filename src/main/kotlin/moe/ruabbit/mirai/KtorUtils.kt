package moe.ruabbit.mirai

import moe.ruabbit.mirai.config.SettingsConfig
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.engine.okhttp.*
import io.ktor.util.*
import net.mamoe.mirai.utils.error

object KtorUtils {
    // 使用代理的ktor客户端
    @OptIn(KtorExperimentalAPI::class)
    val proxyClient = HttpClient(OkHttp) {
        engine {
            /*when(SettingsConfig.proxyConfig) {
                2 -> proxy = ProxyBuilder.socks(host = SettingsConfig.socksProxy.host, port = SettingsConfig.socksProxy.port)
                1 -> proxy = ProxyBuilder.http(SettingsConfig.httpProxy.proxy)
                0 -> null
                else -> {
                    null
                }
            }*/
            proxy = ProxyBuilder.socks(host ="127.0.0.1", port = 7891)
            //null
        }
    }

    // 未使用代理的Ktor客户端
    val normalClient = HttpClient(OkHttp)

    // 安全的关闭客户端, 防止堵塞主线程
    fun closeClient() {
        proxyClient.close()
        normalClient.close()
    }

}
