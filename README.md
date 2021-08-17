<div align="center">
   <img width="160" src="doc/mirai.png" alt="logo"></br>

   <img width="95" src="doc/mirai.svg" alt="title">

mirai 是一个在全平台下运行, 提供 QQ Android 协议支持的高效率机器人库

这个项目的名字来源于
<p><a href = "http://www.kyotoanimation.co.jp/">京都动画</a>作品<a href = "https://zh.moegirl.org.cn/zh-hans/%E5%A2%83%E7%95%8C%E7%9A%84%E5%BD%BC%E6%96%B9">《境界的彼方》</a>的<a href = "https://zh.moegirl.org.cn/zh-hans/%E6%A0%97%E5%B1%B1%E6%9C%AA%E6%9D%A5">栗山未来(Kuriyama <b>mirai</b>)</a></p>
<p><a href = "https://www.crypton.co.jp/">CRYPTON</a>以<a href = "https://www.crypton.co.jp/miku_eng">初音未来</a>为代表的创作与活动<a href = "https://magicalmirai.com/2019/index_en.html">(Magical <b>mirai</b>)</a></p>
图标以及形象由画师<a href = "https://github.com/DazeCake">DazeCake</a>绘制
</div>

# mirai-setu

一个使用[loliconAPI](https://api.lolicon.app/#/setu) 的[mirai-console](https://github.com/mamoe/mirai-console) 的色图插件

# 声明

本插件为开发学习用, 请勿用于非法用途。部分搜索的图片可能会引发不适, 甚至导致账号封禁以及群封禁, 请谨慎使用。作者本人不承担任何责任。

本插件遵循mirai软件AGPLv3协议开源, 不参与一切商业活动。

由于作者学业压力较大, 不能保证插件的及时更新以及bug修复。

## 使用说明

1. 环境配置
   - 插件兼容mirai-console2.0及以上版本。
   - mirai-console需要java环境, 建议使用java11以及以上的版本运行。
2. 插件下载
   - 前往本项目的[releases](https://github.com/meaningtree/mirai-setu/releases) 下载插件
3. 插件运行
   - 将本放入mirai-console的plugins的文件夹然后运行mirai-console。
4. 关于lollicon的注意事项
   -
   lolicon是一个公开的setu库的APi, 此API不属于本人, 如果此API出现问题, 恕我无力解决。图片获取是从i.pixiv.cat反向代理得到的, 可以不使用科学上网获取, 但是国内网络获取比较慢,
   获取卡顿和失败是正常情况。如果有条件的话可以使用科学上网进行加速, 或者部署到没有限制的服务器上。
   - lolicon的APIKEy申请需要到Telegram上申请, 需要使用科学上网, 请自备工具。

## 注意事项

1.插件更新版本之后配置文件不会出现新的配置信息, 请删除配置文件让插件重新生成即可。

2.已知在使用缩略图（默认方法）发送图片的时候, 图片在pc端无法显示, 而且手机端无法直接转发图片, 修改为发送原图可以解决此问题。

3.图片发送模式群主和管理员以及bot管理员（在配置文件中可以增加）都可以使用指令修改。

## 指令

指令可以参考指令配置文件, 指令可以在配置文件进行修改, 表格只是默认命令, 可以自行修改命令

|  指令   | 功能  |
|  ----  | ----  |
| 色图时间/涩图时间/色图来/涩图来  | 从lolicon随机获取一张setu |
| 搜色图/搜涩图 <关键词>  | 根据关键词搜索一张setu |
| 关闭插件/封印 | 关闭此群的涩图插件服务 |
| 青少年模式/普通模式 | **开启此群插件**并切换为普通模式, 普通模式发送普通的setu（以不漏点为标准）|
| 青壮年模式/R-18模式 | **开启此群插件**并切换为R-18模式, R-18模式发送R-18的setu（被举报极易封号, 慎重使用） |
| 混合模式 | **开启此群插件**并切换为混合模式, 可能获取普通setu和R-18setu |
| 以图搜图 | SauceNAO搜图 |

## 指令授权教程

使用权限模式3，采用mirai-console官方提供的授权方法 。
(不区分大小写. 不区分 Bot).

|    被许可人类型    | 字符串表示示例 | 备注                                 |
|:----------------:|:-----------:|:------------------------------------|
|      控制台       |   console   |                                     |
|   任意其他客户端    |   client*   | 即 Bot 自己发消息给自己                |
|      精确群       |   g123456   | 表示群, 而不表示群成员                  |
|      精确好友      |   f123456   | 必须通过好友消息                       |
|   精确群临时会话    | t123456.789 | 群 123456 内的成员 789. 必须通过临时会话 |
|     精确群成员     | m123456.789 | 群 123456 内的成员 789. 同时包含临时会话 |
|      精确用户      |   u123456   | 同时包含群成员, 好友, 临时会话           |
|      任意群       |     g\*     | g 意为 group                         |
|  任意群的任意群员   |     m\*     | m 意为 member                        |
|  精确群的任意群员   | m123456.\*  | 群 123456 内的任意成员. 同时包含临时会话  |
|    任意临时会话    |     t\*      | t 意为 temp. 必须通过临时会话          |
| 精确群的任意临时会话 | t123456.\*  | 群 123456 内的任意成员. 必须通过临时会话  |
|      任意好友      |     f\*     | f 意为 friend                       |
|      任意用户      |     u\*     | u 意为 user. 任何人在任何环境           |
|     任意陌生人     |     s\*     | s 意为 stranger. 任何人在任何环境       |
|    任意联系对象    |      \*      | 即任何人, 任何群. 不包括控制台           |

## 配置文件

配置文件可以配置代理一些配置, 请参考配置文件注释

## 未来计划

- [ ] 适配SauceNAO搜图的功能

   - [x] pixiv适配
- [ ] 适配ascii2d搜图功能
- [ ] 提供图片缓存到本地的功能
- [ ] 提供GUI界面修改配置的功能
- [ ] 绕过sni审查实现直连pixiv 有什么新奇的想法和建议也可以在issue留言给我

## 鸣谢

感谢Mirai团队

感谢lolicon提供的图片库

感谢fantasyzone提供的图片库

感谢 [利姆露酱](https://github.com/RimuruChan) 提供的帮助
