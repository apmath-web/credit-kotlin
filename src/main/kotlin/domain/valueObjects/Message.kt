package domain.valueObjects


class Message (text : String, field : String? = null) : MessageInterface {

    override val field: String? = field

    override val text: String = text
}
