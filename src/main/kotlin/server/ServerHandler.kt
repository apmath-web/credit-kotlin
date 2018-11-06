package server

import actions.Handler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.handler.codec.http.*


class ServerHandler : ChannelInboundHandlerAdapter() {

    override fun channelReadComplete(ctx: ChannelHandlerContext) {
        ctx.flush()
    }

    @Throws(Exception::class)
    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        if (msg is HttpRequest) {
            val response = Handler().handle(msg)
            ctx.write(response).addListener(ChannelFutureListener.CLOSE)
        }
    }

    @Throws(Exception::class)
    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        cause.printStackTrace()
        ctx.close()
    }
}
