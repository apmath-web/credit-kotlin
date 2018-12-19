package domain.valueObjects

class Validation : ValidationInterface {

    override val messages: MutableList<MessageInterface> = arrayListOf()

    override fun addMessage(message: MessageInterface) {
        messages.add(message)
    }

    operator fun plus(validation: ValidationInterface): ValidationInterface {
        messages.addAll(validation.messages)
        return this
    }
}
