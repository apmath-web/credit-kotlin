import server.Server

@Throws(Exception::class)
fun main(args: Array<String>) {
    val port: Int
    if (args.size > 0) {
        port = Integer.parseInt(args[0])
    } else {
        port = 8080
    }
    Server(port).run()
}
