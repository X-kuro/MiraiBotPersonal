package net.mamoe.mirai.simpleloader

import com.google.gson.Gson
import com.sun.xml.internal.ws.client.sei.ResponseBuilder
import io.ktor.http.Url
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
import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.message.MessageEvent
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.content
import java.io.File
import java.net.URL

suspend fun main() {
    val qqId = 501621053L//Bot的QQ号，需为Long类型，在结尾处添加大写L
    val password = "qqjerome1993"//Bot的密码
    val miraiBot = Bot(qqId, password).alsoLogin()//新建Bot并登录
    miraiBot.subscribeMessages {

    }
    miraiBot.subscribeAlways<MemberJoinEvent> {
        it.group.sendMessage(PlainText("欢迎 ${it.member.nameCardOrNick} 加入本群！"))
    }
    miraiBot.subscribeAlways<MemberMuteEvent> {
        it.group.sendMessage(PlainText("恭喜老哥 ${it.member.nameCardOrNick} 喜提禁言套餐一份"))
    }

    miraiBot.subscribeAlways<GroupMessageEvent> { event ->
        if(event.message.content.contains("色图"))
        {
            var tag = event.message.content.replace("色图","")
            var fileName = DownloadPic(GetPixivPic(tag))
            File(fileName).sendAsImage()
        }
        //else reply(AutoChat(event.message.content))
    }

    miraiBot.join() // 等待 Bot 离线, 避免主线程退出
}

data class ChatReply(val ret:String,val msg:String,val data:Data)
data class Data(val session:String, val answer:String)

fun AutoChat(msg:String):String
{
    var param = buildMsg(msg)
    val url = "https://api.ai.qq.com/fcgi-bin/nlp/nlp_textchat"
    var replymsg = getDataByPost(param,url).toString()
    val reply = Gson().fromJson(replymsg, ChatReply::class.java)
    return reply.data.answer
}

val proxyAddr = "original.img.cheerfun.dev"
fun GetPixivPic(msg:String):String
{
    var url =
        "https://api.pixivic.com/illustrations?illustType=illust&searchType=original&maxSanityLevel=6&page=1&keyword=$msg&pageSize=40"
    var replymsg = getDataByGet(url)
    val reply = Gson().fromJson(replymsg, PixivClass::class.java)
    var rand = (0..reply.data.size-1).random()
    var picUrl = reply.data[rand].imageUrls[0].original
    return picUrl.replace("i.pximg.net","original.img.cheerfun.dev")
}

fun DownloadPic(url:String):String
{
    var fileName = url.substring(url.lastIndexOf('/')+1)
    var filePath =  System.getProperty("user.dir")+"\\pic\\"
    val file =File(filePath+fileName)
    if(!file.exists())
    {
        val openConnection = URL(url).openConnection()
        openConnection.setRequestProperty("referer","https://pixivic.com/")
        val bytes = openConnection.getInputStream().readBytes()
        file.writeBytes(bytes)
    }
    return filePath+fileName;
}
