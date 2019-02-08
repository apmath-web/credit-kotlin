package domain.repositories

import domain.exceptions.*
import domain.exceptions.runtime.*
import domain.models.CreditInterface


class CreditsRepository : CreditsRepositoryInterface {
    private var identity: Int = 1
    private val credits: HashMap<Int, CreditInterface> = hashMapOf()

    override fun get(id: Int): CreditInterface {
        return credits[id] ?: throw CreditNotFoundException()
    }

    override fun store(credit: CreditInterface) {
        if (credit.id != null) {
            throw StoreIdentifiedCreditException()
        }
        credit.id = identity
        credits[identity++] = credit
    }

    override fun remove(credit: CreditInterface) {
        when {
            credit.id != null               -> throw RemoveUnidentifiedCreditException()
            !credit.isFinished              -> throw RemoveUnfinishedCreditException()
            !credits.containsKey(credit.id) -> throw RemoveAbsentCreditException()
        }

        credits.remove(credit.id)
    }
}
