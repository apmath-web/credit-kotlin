package valueObjects

import java.util.*


interface ValidationInterface {
    fun addMessage(message: MessageInterface)
    fun getMessages(): List<MessageInterface>
}
