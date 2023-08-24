package com.codelab.basiclayouts.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.codelab.basiclayouts.data.model.Transaction
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TransactionsRepositoryTest {

    private lateinit var db: TransactionDatabase

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            TransactionDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @Test
    fun writeTransactionToDatabase() {
        val transaction = Transaction(1, 100, "test tx", 69, "Groceries")
        val transactionsRepository = TransactionsRepository(db)

        transactionsRepository.writeTransactionToDatabase(transaction)

        val readTransaction = transactionsRepository.loadTransaction(1)

        assert(transaction.amount == readTransaction.amount)
        assertEquals(transaction.title, readTransaction.title)
    }

    @Test
    fun updateBalance() {
        val transactionsRepository = TransactionsRepository(db)

        transactionsRepository.writeTransactionToDatabase(
            Transaction(1, 100, "test tx", 10, "Groceries")
        )
        transactionsRepository.writeTransactionToDatabase(
            Transaction(2, 90, "test tx", 10, "Groceries")
        )
        transactionsRepository.writeTransactionToDatabase(
            Transaction(3, 110, "test tx", 10, "Groceries")
        )
        transactionsRepository.writeTransactionToDatabase(
            Transaction(4, 95, "test tx", 10, "Groceries")
        )
        transactionsRepository.writeTransactionToDatabase(
            Transaction(5, 105, "test tx", 10, "Groceries")
        )

        assert(transactionsRepository.readBalance(id = 1) == 30)
        assert(transactionsRepository.readBalance(id = 2) == 10)
        assert(transactionsRepository.readBalance(id = 3) == 50)
        assert(transactionsRepository.readBalance(id = 4) == 20)
        assert(transactionsRepository.readBalance(id = 5) == 40)

    }
}