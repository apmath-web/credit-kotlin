package valueObjects


class Message (text : String) : MessageInterface {

    constructor(field : String?, text: String) : this(text)

    override val field: String? = null

    override val text: String = ""
}
