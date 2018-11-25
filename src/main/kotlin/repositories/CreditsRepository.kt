package repositories

import exceptions.RemoveAbsentCreditException
import exceptions.RemoveUnidentifiedCreditException
import exceptions.StoreIdentifiedCreditException
import models.CreditInterface


class CreditsRepository : CreditsRepositoryInterface {
    private var identity: Int = 1
    private val credits: HashMap<Int, CreditInterface> = hashMapOf()

    override fun get(id: Int): CreditInterface? {
        return credits[id]
    }

    override fun store(credit: CreditInterface) {
        if (credit.id != null) {
            throw StoreIdentifiedCreditException()
        }
        credit.id = identity
        credits[identity++] = credit
    }

    override fun remove(credit: CreditInterface) {
        if (credit.id != null) {
            throw RemoveUnidentifiedCreditException()
        }
        if (!credits.containsKey(credit.id)) {
            throw RemoveAbsentCreditException()
        }
        credits.remove(credit.id)
    }
}
