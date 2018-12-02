package domain.valueObjects


class Message (field: String, text: String) : MessageInterface {

    override val field: String = field

    override val text: String = text
}
