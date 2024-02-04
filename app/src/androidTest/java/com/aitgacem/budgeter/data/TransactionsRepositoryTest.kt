package com.aitgacem.budgeter.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.aitgacem.budgeter.data.model.Transaction
import com.aitgacem.budgeter.ui.components.Category
import com.aitgacem.budgeter.util.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.math.abs


@RunWith(AndroidJUnit4::class)
class TransactionsRepositoryTest {

    private lateinit var db: TransactionDatabase

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    private val testDispatcher = TestCoroutineDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(), TransactionDatabase::class.java
        ).allowMainThreadQueries().build()
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun writeTransactionToDatabase() = runTest {
        val transaction = Transaction(0, "100", 100.0, 12121L, 120L, Category.Education)
        val transactionsRepository = TransactionsRepository(db)

        transactionsRepository.writeTransactionToDatabase(transaction)
//
        val readTransaction = transactionsRepository.loadTransaction(1)
//
        assert(readTransaction != null && transaction.amount == readTransaction.amount)
        assert(readTransaction != null && transaction.title == readTransaction.title)
    }

    @Test
    fun updateBalance() = runBlocking {
        val transactionsRepository = TransactionsRepository(db)
//
        transactionsRepository.writeTransactionToDatabase(
            Transaction(0, "100", 100.0, 23, 0, Category.Education)
        )
        transactionsRepository.writeTransactionToDatabase(
            Transaction(0, "100", 50.0, 20, 0, Category.Education)
        )
        transactionsRepository.writeTransactionToDatabase(
            Transaction(0, "100", 20.0, 19, 0, Category.Education)
        )
        transactionsRepository.writeTransactionToDatabase(
            Transaction(0, "100", 110.0, 25, 0, Category.Education)
        )
        transactionsRepository.writeTransactionToDatabase(
            Transaction(0, "100", 15.0, 27, 0, Category.Education)
        )
        assert(abs(transactionsRepository.readBalance(date = 19) - 20.0) < 0.001)
        assert(abs(transactionsRepository.readBalance(date = 20) - 70) < 0.001)
        assert(abs(transactionsRepository.readBalance(date = 23) - 170) < 0.001)
        assert(abs(transactionsRepository.readBalance(date = 25) - 280) < 0.001)
        assert(abs(transactionsRepository.readBalance(date = 27) - 295) < 0.001)

    }

    @Test
    fun updateTransaction() = testDispatcher.runBlockingTest {
        val transactionsRepository = TransactionsRepository(db)
        val old = Transaction(0, "100", 110.0, 25, 0, Category.Education)
        transactionsRepository.writeTransactionToDatabase(old)
        val new = Transaction(1, "100", 60.0, 25, 0, Category.Entertainment)
        transactionsRepository.updateTransaction(new, old)
        val all: List<Transaction> =
            transactionsRepository.readAllTransactionsFromDatabase().getOrAwaitValue()
        assert(all.size == 1)
        print(all[0])
        assert(all[0].title == "100")
        assert(all[0].amount == 60.0)
        assert(all[0].date == 25L)
        assert(all[0].category == Category.Entertainment)
        val newbalance = transactionsRepository.readBalance(25)
        assert(newbalance == 60.0)
    }

    @Test
    fun creatingAndUpdatingStuff() = runBlocking {
        val transactionsRepository = TransactionsRepository(db)

        val tr1 = Transaction(0, "100", 100.0, 23, 0, Category.Education)
        val tr2 = Transaction(0, "200", 50.0, 20, 0, Category.Education)
        val tr3 = Transaction(0, "300", 20.0, 19, 0, Category.Education)
        val tr4 = Transaction(0, "400", 110.0, 25, 0, Category.Education)
        val tr5 = Transaction(0, "500", 15.0, 27, 0, Category.Education)

        transactionsRepository.writeTransactionToDatabase(tr1)
        transactionsRepository.writeTransactionToDatabase(tr2)
        transactionsRepository.writeTransactionToDatabase(tr3)
        transactionsRepository.writeTransactionToDatabase(tr4)
        transactionsRepository.writeTransactionToDatabase(tr5)

        assert(abs(transactionsRepository.readBalance(date = 19) - 20.0) < 0.001)
        assert(abs(transactionsRepository.readBalance(date = 20) - 70) < 0.001)
        assert(abs(transactionsRepository.readBalance(date = 23) - 170) < 0.001)
        assert(abs(transactionsRepository.readBalance(date = 25) - 280) < 0.001)
        assert(abs(transactionsRepository.readBalance(date = 27) - 295) < 0.001)

        val tr1old = Transaction(1, "100", 50.0, 12, 0, Category.Education)
        val tr2old = Transaction(2, "400", 20.0, 12, 0, Category.Entertainment)
        val tr3old = Transaction(3, "200", 30.0, 10, 0, Category.Healthcare)
        val tr4old = Transaction(4, "300", 60.0, 25, 0, Category.Education)
        val tr5old = Transaction(5, "600", 15.0, 32, 0, Category.Food)

        transactionsRepository.updateTransaction(tr1old, tr1)
        transactionsRepository.updateTransaction(tr2old, tr2)
        transactionsRepository.updateTransaction(tr3old, tr3)
        transactionsRepository.updateTransaction(tr4old, tr4)
        transactionsRepository.updateTransaction(tr5old, tr5)


        val tr1new = transactionsRepository.loadTransaction(1)
        val tr2new = transactionsRepository.loadTransaction(2)
        val tr3new = transactionsRepository.loadTransaction(3)
        val tr4new = transactionsRepository.loadTransaction(4)
        val tr5new = transactionsRepository.loadTransaction(5)

        assert(tr1new != null)
        assert(tr2new != null)
        assert(tr3new != null)
        assert(tr4new != null)
        assert(tr5new != null)


        assert(tr1new!!.amount == 50.0)
        assert(tr2new!!.amount == 20.0)
        assert(tr3new!!.amount == 30.0)
        assert(tr4new!!.amount == 60.0)
        assert(tr5new!!.amount == 15.0)

        assert(tr1new.title == "100")
        assert(tr2new.title == "400")
        assert(tr3new.title == "200")
        assert(tr4new.title == "300")
        assert(tr5new.title == "600")

        assert(tr1new.date == 12L)
        assert(tr2new.date == 12L)
        assert(tr3new.date == 10L)
        assert(tr4new.date == 25L)
        assert(tr5new.date == 32L)

        assert(tr1new.category == Category.Education)
        assert(tr2new.category == Category.Entertainment)
        assert(tr3new.category == Category.Healthcare)
        assert(tr4new.category == Category.Education)
        assert(tr5new.category == Category.Food)

        val all = transactionsRepository.readAllTransactionsFromDatabase().getOrAwaitValue()
        val all2 = transactionsRepository.getBalancesAfterDate(-1)
        print(all2)

        assert(abs(transactionsRepository.loadCategoryTotal(Category.Education) - 110.0) < 0.001)
        assert(abs(transactionsRepository.loadCategoryTotal(Category.Healthcare) - 30.0) < 0.001)
        assert(abs(transactionsRepository.loadCategoryTotal(Category.Entertainment) - 20.0) < 0.001)
        assert(abs(transactionsRepository.loadCategoryTotal(Category.Food) - 15.0) < 0.001)

        assert(abs(transactionsRepository.readBalance(date = 10) - 30.0) < 0.001)
        assert(abs(transactionsRepository.readBalance(date = 12) - 100) < 0.001)
        assert(abs(transactionsRepository.readBalance(date = 25) - 160) < 0.001)
        assert(abs(transactionsRepository.readBalance(date = 32) - 175) < 0.001)


        val tr6 = Transaction(0, "oldest", 35.0, 5, 0, Category.Food)
        transactionsRepository.writeTransactionToDatabase(tr6)
        assert(abs(transactionsRepository.readBalance(date = 5) - 35.0) < 0.001)
        assert(abs(transactionsRepository.readBalance(date = 10) - 65.0) < 0.001)
        assert(abs(transactionsRepository.readBalance(date = 12) - 135) < 0.001)
        assert(abs(transactionsRepository.readBalance(date = 25) - 195) < 0.001)
        assert(abs(transactionsRepository.readBalance(date = 32) - 210) < 0.001)

        val tr7 = Transaction(0, "middle", 100.0, 20, 0, Category.Groceries)
        transactionsRepository.writeTransactionToDatabase(tr7)
        assert(abs(transactionsRepository.readBalance(date = 5) - 35.0) < 0.001)
        assert(abs(transactionsRepository.readBalance(date = 10) - 65.0) < 0.001)
        assert(abs(transactionsRepository.readBalance(date = 12) - 135) < 0.001)
        assert(abs(transactionsRepository.readBalance(date = 20) - 235) < 0.001)
        assert(abs(transactionsRepository.readBalance(date = 25) - 295) < 0.001)
        assert(abs(transactionsRepository.readBalance(date = 32) - 310) < 0.001)

        val tr8 = Transaction(0, "end", 30.0, 60, 0, Category.Groceries)
        transactionsRepository.writeTransactionToDatabase(tr8)
        assert(abs(transactionsRepository.readBalance(date = 5) - 35.0) < 0.001)
        assert(abs(transactionsRepository.readBalance(date = 10) - 65.0) < 0.001)
        assert(abs(transactionsRepository.readBalance(date = 12) - 135) < 0.001)
        assert(abs(transactionsRepository.readBalance(date = 20) - 235) < 0.001)
        assert(abs(transactionsRepository.readBalance(date = 25) - 295) < 0.001)
        assert(abs(transactionsRepository.readBalance(date = 32) - 310) < 0.001)
        assert(abs(transactionsRepository.readBalance(date = 60) - 340) < 0.001)


        assert(abs(transactionsRepository.readBalance(date = 70) - 340) < 0.001)
        assert(abs(transactionsRepository.readBalance(date = 0) - 0.0) < 0.001)
        assert(abs(transactionsRepository.readBalance(date = 11) - 65.0) < 0.001)


    }
}
