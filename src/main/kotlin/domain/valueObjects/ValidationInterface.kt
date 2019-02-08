package domain.valueObjects


interface ValidationInterface {
    val messages : MutableList<MessageInterface>
    fun addMessage(message: MessageInterface)
}
