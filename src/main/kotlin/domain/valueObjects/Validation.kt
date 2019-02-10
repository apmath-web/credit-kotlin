package domain.valueObjects

class Validation : ValidationInterface {

    override val messages: MutableList<MessageInterface> = arrayListOf()

    override fun addMessage(message: MessageInterface) {
        messages.add(message)
    }

    override fun addMessages(validation: ValidationInterface) {
        messages.addAll(validation.messages)
    }
}
