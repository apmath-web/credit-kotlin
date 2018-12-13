package actions.credit

import actions.AbstractHandler
import domain.valueObjects.Message
import domain.valueObjects.Validation


abstract class AbstractCreditHandler : AbstractHandler() {

    protected var creditId: Int? = null

    protected val validation = Validation()

    protected fun validateCreditId(groups: MatchGroupCollection): Boolean {

        val value = groups[ID]!!.value

        try {
            creditId = value.toInt()
            if (creditId ?: 0 < 1) {
                throw Exception()
            }
        } catch (e: Exception) {
            validation.addMessage(Message(ID, "Credit id must be between 1 and ${Int.MAX_VALUE}"))
            return false
        }

        return true
    }

    companion object {
        const val ID    = "id"
    }

}
