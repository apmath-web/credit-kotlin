package repositories

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
        credits[identity++] = credit
    }

    override fun remove(credit: CreditInterface) {
        if (credit.id != null) {
            throw RemoveUnidentifiedCreditException()
        }
        credits.remove(credit.id)
    }
}
