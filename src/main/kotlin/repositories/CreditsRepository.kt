package repositories

import models.CreditInterface


class CreditsRepository : CreditsRepositoryInterface {
    override fun get(id: Int): CreditInterface? {
        return null
    }

    override fun store(credit: CreditInterface) {

    }

    override fun remove(credit: CreditInterface) {

    }
}
