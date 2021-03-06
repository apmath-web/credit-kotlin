package server

import io.netty.handler.codec.http.HttpServerCodec
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.http.HttpObjectAggregator


class ServerInitializer : ChannelInitializer<SocketChannel>() {
    @Throws(Exception::class)
    public override fun initChannel(ch: SocketChannel) {
        val p = ch.pipeline()

        // Uncomment the following line if you want HTTPS
        //SSLEngine engine = SecureChatSslContextFactory.getServerContext().createSSLEngine();
        //engine.setUseClientMode(false);
        //p.addLast("ssl", new SslHandler(engine));

        p.addLast("codec", HttpServerCodec())
        p.addLast("aggregate", HttpObjectAggregator(1048576))
        p.addLast("handler", ServerHandler())
    }
}
