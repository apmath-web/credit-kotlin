package actions.credit

import actions.AbstractHandler
import domain.exceptions.CreditNotFoundException
import domain.models.CreditInterface
import domain.repositories.CreditsRepositoryInterface
import domain.valueObjects.Message
import domain.valueObjects.Validation
import exceptions.NotFoundException


abstract class AbstractCreditHandler(protected val repository: CreditsRepositoryInterface) : AbstractHandler() {

    private var creditId: Int? = null

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

    protected fun getCredit(): CreditInterface {
        val credit: CreditInterface
        try {
            credit = repository.get(creditId as Int)
        } catch (e: CreditNotFoundException) {
            throw NotFoundException("Credit not found")
        }
        return credit
    }

    companion object {
        const val ID    = "id"
    }

}
