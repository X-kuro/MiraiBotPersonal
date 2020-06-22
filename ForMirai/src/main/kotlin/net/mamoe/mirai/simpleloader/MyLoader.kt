package net.mamoe.mirai.simpleloader

import net.kuroi.httpok.repack.buildMsg
import net.kuroi.httpok.repack.getDataByGet
import net.kuroi.httpok.repack.getDataByPost
import net.mamoe.mirai.Bot
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.contact.nameCardOrNick
import net.mamoe.mirai.event.events.MemberJoinEvent
import net.mamoe.mirai.event.events.MemberMuteEvent
import net.mamoe.mirai.event.subscribeAlways
import net.mamoe.mirai.event.subscribeMessages
import net.mamoe.mirai.join
import net.mamoe.mirai.message.FriendMessageEvent
import net.mamoe.mirai.message.MessageEvent
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.content

suspend fun main() {
    val qqId = 501621053L//Bot的QQ号，需为Long类型，在结尾处添加大写L
    val password = "qqjerome1993"//Bot的密码
    val miraiBot = Bot(qqId, password).alsoLogin()//新建Bot并登录
    miraiBot.subscribeMessages {
        "你好" reply "你好!"
        case("at me") {
             reply( " 给爷爬 ")
        }
        (contains("舔") or contains("刘老板")) {
            reply(sender.id.toString() + "刘老板太强了")

        }
    }
    miraiBot.subscribeAlways<MemberJoinEvent> {
        it.group.sendMessage(PlainText("欢迎 ${it.member.nameCardOrNick} 加入本群！"))
    }
    miraiBot.subscribeAlways<MemberMuteEvent> {
        it.group.sendMessage(PlainText("恭喜老哥 ${it.member.nameCardOrNick} 喜提禁言套餐一份"))
    }
    miraiBot.subscribeAlways<FriendMessageEvent> {event ->
        AutoChat(event.message.toString())
    }

    miraiBot.join() // 等待 Bot 离线, 避免主线程退出
}

fun AutoChat(msg:String):String
{
    var param = buildMsg(msg)
    val url = "https://api.ai.qq.com/fcgi-bin/nlp/nlp_textchat"
    var msg = getDataByPost(param,url).toString()
    return msg
}