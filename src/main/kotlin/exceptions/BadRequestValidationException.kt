package exceptions

import domain.valueObjects.MessageInterface
import domain.valueObjects.ValidationInterface

class BadRequestValidationException(validation: ValidationInterface) : BadRequestException("Validation error") {
    val messages: MutableList<MessageInterface> = validation.messages
}
