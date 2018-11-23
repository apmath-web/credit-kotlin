package repositories

import models.CreditInterface


interface CreditsRepositoryInterface {
    fun get(id: Int): CreditInterface?
    fun store(credit: CreditInterface)
    fun remove(credit: CreditInterface)
}
