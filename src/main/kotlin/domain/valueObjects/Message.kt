package domain.valueObjects

data class Message(override val field: String, override val text: String) : MessageInterface
