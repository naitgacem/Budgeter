package com.aitgacem.budgeter.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.aitgacem.budgeter.data.model.Transaction
import com.aitgacem.budgeter.ui.components.Category
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.math.abs

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
    fun writeTransactionToDatabase() = runTest {
        val transaction = Transaction(1, 100, "test tx", 69.0f, Category.Education)
        val transactionsRepository = TransactionsRepository(db)

        transactionsRepository.writeTransactionToDatabase(transaction)

        val readTransaction = transactionsRepository.loadTransaction(1)

        assert(readTransaction != null && transaction.amount == readTransaction.amount)
        assert(readTransaction != null && transaction.title == readTransaction.title)
    }

    @Test
    fun updateBalance() = runTest {
        val transactionsRepository = TransactionsRepository(db)

        transactionsRepository.writeTransactionToDatabase(
            Transaction(1, 100, "test tx", 10f, Category.Education)
        )
        transactionsRepository.writeTransactionToDatabase(
            Transaction(2, 90, "test tx", 10f, Category.Healthcare)
        )
        transactionsRepository.writeTransactionToDatabase(
            Transaction(3, 110, "test tx", 10f, Category.Entertainment)
        )
        transactionsRepository.writeTransactionToDatabase(
            Transaction(4, 95, "test tx", 10f, Category.Utilities)
        )
        transactionsRepository.writeTransactionToDatabase(
            Transaction(5, 105, "test tx", 10f, Category.Travel)
        )

        assert(abs(transactionsRepository.readBalance(id = 1) + 30f) < 0.001)
        assert(abs(transactionsRepository.readBalance(id = 2) + 10f) < 0.001)
        assert(abs(transactionsRepository.readBalance(id = 3) + 50f) < 0.001)
        assert(abs(transactionsRepository.readBalance(id = 4) + 20f) < 0.001)
        assert(abs(transactionsRepository.readBalance(id = 5) + 40f) < 0.001)

    }
}