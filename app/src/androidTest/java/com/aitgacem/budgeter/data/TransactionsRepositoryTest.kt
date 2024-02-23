package com.aitgacem.budgeter.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.aitgacem.budgeter.ui.components.Category
import com.aitgacem.budgeter.ui.components.ItemType.Transaction
import com.aitgacem.budgeter.util.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
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

        val readTransaction = transactionsRepository.loadTransaction(1)

        assertThat(readTransaction).isNotNull()
        assertThat(readTransaction?.amount).isEqualTo(100.0)

    }

    @Test
    fun updateBalance() = runBlocking {
        val repo = TransactionsRepository(db)

        repo.writeTransactionToDatabase(
            Transaction(0, "100", 100.0, 23, 0, Category.Education)
        )
        repo.writeTransactionToDatabase(
            Transaction(0, "100", 50.0, 20, 0, Category.Education)
        )
        repo.writeTransactionToDatabase(
            Transaction(0, "100", 20.0, 19, 0, Category.Education)
        )
        repo.writeTransactionToDatabase(
            Transaction(0, "100", 110.0, 25, 0, Category.Education)
        )
        repo.writeTransactionToDatabase(
            Transaction(0, "100", 15.0, 27, 0, Category.Deposit)
        )


        assertThat(repo.readBalance(19)).isWithin(0.001).of(-20.0)
        assertThat(repo.readBalance(20)).isWithin(0.001).of(-70.0)
        assertThat(repo.readBalance(23)).isWithin(0.001).of(-170.0)
        assertThat(repo.readBalance(25)).isWithin(0.001).of(-280.0)
        assertThat(repo.readBalance(27)).isWithin(0.001).of(-265.0)
    }

    @Test
    fun updateTransaction() = testDispatcher.runBlockingTest {
        val transactionsRepository = TransactionsRepository(db)

        val old = Transaction(1, "200", 110.0, 25, 0, Category.Education)
        transactionsRepository.writeTransactionToDatabase(old)
        val new = Transaction(1, "100", 60.0, 25, 0, Category.Entertainment)
        transactionsRepository.updateTransaction(new, old)
        val all: List<Transaction> =
            transactionsRepository.readAllTransactionsFromDatabase().getOrAwaitValue()

        val newbalance = transactionsRepository.readBalance(25)
        assertThat(newbalance).isEqualTo(-60.0)

        assertThat(all.size).isEqualTo(1)
        assertThat(all[0].amount).isEqualTo(60.0)
        assertThat(all[0].date).isEqualTo(25L)
        assertThat(all[0].category).isEqualTo(Category.Entertainment)
        assertThat(all[0].title).isEqualTo("100")

    }

    @Test
    fun creatingAndUpdatingStuff() = runBlocking {
        val repo = TransactionsRepository(db)

        val tr1 = Transaction(1, "100", 100.0, 23, 0, Category.Education)
        val tr2 = Transaction(2, "200", 50.0, 20, 0, Category.Education)
        val tr3 = Transaction(3, "300", 20.0, 19, 0, Category.Education)
        val tr4 = Transaction(4, "400", 110.0, 25, 0, Category.Education)
        val tr5 = Transaction(5, "500", 15.0, 27, 0, Category.Education)

        repo.writeTransactionToDatabase(tr1)
        repo.writeTransactionToDatabase(tr2)
        repo.writeTransactionToDatabase(tr3)
        repo.writeTransactionToDatabase(tr4)
        repo.writeTransactionToDatabase(tr5)

        assert(abs(repo.readBalance(date = 19) + 20.0) < 0.001)
        assert(abs(repo.readBalance(date = 20) + 70) < 0.001)
        assert(abs(repo.readBalance(date = 23) + 170) < 0.001)
        assert(abs(repo.readBalance(date = 25) + 280) < 0.001)
        assert(abs(repo.readBalance(date = 27) + 295) < 0.001)

        //update all of them then reload them
        val tr1old = Transaction(1, "100", 50.0, 12, 0, Category.Education)
        val tr2old = Transaction(2, "400", 20.0, 12, 0, Category.Deposit)
        val tr3old = Transaction(3, "200", 30.0, 10, 0, Category.Healthcare)
        val tr4old = Transaction(4, "300", 60.0, 25, 0, Category.Education)
        val tr5old = Transaction(5, "600", 15.0, 32, 0, Category.Food)


//region
        repo.updateTransaction(tr1old, tr1)
        repo.updateTransaction(tr2old, tr2)
        repo.updateTransaction(tr3old, tr3)
        repo.updateTransaction(tr4old, tr4)
        repo.updateTransaction(tr5old, tr5)

        val tr1new = repo.loadTransaction(1)
        val tr2new = repo.loadTransaction(2)
        val tr3new = repo.loadTransaction(3)
        val tr4new = repo.loadTransaction(4)
        val tr5new = repo.loadTransaction(5)

        assertThat(tr1new).isNotNull()
        assertThat(tr2new).isNotNull()
        assertThat(tr3new).isNotNull()
        assertThat(tr4new).isNotNull()
        assertThat(tr5new).isNotNull()

        assertThat(tr1new!!.amount).isEqualTo(50.0)
        assertThat(tr2new!!.amount).isEqualTo(20.0)
        assertThat(tr3new!!.amount).isEqualTo(30.0)
        assertThat(tr4new!!.amount).isEqualTo(60.0)
        assertThat(tr5new!!.amount).isEqualTo(15.0)

        assertThat(tr1new.title).isEqualTo("100")
        assertThat(tr2new.title).isEqualTo("400")
        assertThat(tr3new.title).isEqualTo("200")
        assertThat(tr4new.title).isEqualTo("300")
        assertThat(tr5new.title).isEqualTo("600")

        assertThat(tr1new.date).isEqualTo(12L)
        assertThat(tr2new.date).isEqualTo(12L)
        assertThat(tr3new.date).isEqualTo(10L)
        assertThat(tr4new.date).isEqualTo(25L)
        assertThat(tr5new.date).isEqualTo(32L)

        assertThat(tr1new.category).isEqualTo(Category.Education)
        assertThat(tr2new.category).isEqualTo(Category.Deposit)
        assertThat(tr3new.category).isEqualTo(Category.Healthcare)
        assertThat(tr4new.category).isEqualTo(Category.Education)
        assertThat(tr5new.category).isEqualTo(Category.Food)

        assertThat(repo.loadCategoryTotal(Category.Education, 0, 1970)).isWithin(0.001).of(110.0)
        assertThat(repo.loadCategoryTotal(Category.Healthcare, 0, 1970)).isWithin(0.001).of(30.0)
        assertThat(repo.loadCategoryTotal(Category.Entertainment, 0, 1970)).isWithin(0.001).of(0.0)
        assertThat(repo.loadCategoryTotal(Category.Food, 0, 1970)).isWithin(0.001).of(15.0)

        assertThat(repo.readBalance(date = 10)).isWithin(0.001).of(-30.0)
        assertThat(repo.readBalance(date = 12)).isWithin(0.001).of(-60.0)
        assertThat(repo.readBalance(date = 25)).isWithin(0.001).of(-120.0)
        assertThat(repo.readBalance(date = 32)).isWithin(0.001).of(-135.0)

        val tr6 = Transaction(0, "oldest", 100.0, 5, 0, Category.Deposit)
        repo.writeTransactionToDatabase(tr6)
        assertThat(repo.readBalance(date = 5)).isWithin(0.001).of(100.0)
        assertThat(repo.readBalance(date = 10)).isWithin(0.001).of(70.0)
        assertThat(repo.readBalance(date = 12)).isWithin(0.001).of(40.0)
        assertThat(repo.readBalance(date = 25)).isWithin(0.001).of(-20.0)
        assertThat(repo.readBalance(date = 32)).isWithin(0.001).of(-35.0)

        val tr7 = Transaction(0, "middle", 100.0, 20, 0, Category.Groceries)
        repo.writeTransactionToDatabase(tr7)
        assertThat(repo.readBalance(date = 5)).isWithin(0.001).of(100.0)
        assertThat(repo.readBalance(date = 10)).isWithin(0.001).of(70.0)
        assertThat(repo.readBalance(date = 12)).isWithin(0.001).of(40.0)
        assertThat(repo.readBalance(date = 20)).isWithin(0.001).of(-60.0)
        assertThat(repo.readBalance(date = 25)).isWithin(0.001).of(-120.0)
        assertThat(repo.readBalance(date = 32)).isWithin(0.001).of(-135.0)

        val tr8 = Transaction(0, "end", 30.0, 60, 0, Category.Deposit)
        repo.writeTransactionToDatabase(tr8)
        assertThat(repo.readBalance(date = 5)).isWithin(0.001).of(100.0)
        assertThat(repo.readBalance(date = 10)).isWithin(0.001).of(70.0)
        assertThat(repo.readBalance(date = 12)).isWithin(0.001).of(40.0)
        assertThat(repo.readBalance(date = 20)).isWithin(0.001).of(-60.0)
        assertThat(repo.readBalance(date = 25)).isWithin(0.001).of(-120.0)
        assertThat(repo.readBalance(date = 32)).isWithin(0.001).of(-135.0)
        assertThat(repo.readBalance(date = 60)).isWithin(0.001).of(-105.0)


        assertThat(repo.readBalance(date = 70)).isWithin(0.001).of(-105.0)
        assertThat(repo.readBalance(date = 0)).isWithin(0.001).of(0.0)
        assertThat(repo.readBalance(date = 11)).isWithin(0.001).of(70.0)

        //testing withdrawals-----------------------------------------------------------------
        val tr9 = Transaction(0, "oldest", 10.0, 2, 0, Category.Entertainment)
        repo.writeTransactionToDatabase(tr9)
        assertThat(repo.readBalance(date = 2)).isWithin(0.001).of(-10.0)
        assertThat(repo.readBalance(date = 5)).isWithin(0.001).of(90.0)
        assertThat(repo.readBalance(date = 10)).isWithin(0.001).of(60.0)
        assertThat(repo.readBalance(date = 12)).isWithin(0.001).of(30.0)
        assertThat(repo.readBalance(date = 20)).isWithin(0.001).of(-70.0)
        assertThat(repo.readBalance(date = 25)).isWithin(0.001).of(-130.0)
        assertThat(repo.readBalance(date = 32)).isWithin(0.001).of(-145.0)
        assertThat(repo.readBalance(date = 60)).isWithin(0.001).of(-115.0)

//endregion
        val tr10 = Transaction(0, "middle", 100.0, 22, 0, Category.Deposit)
        repo.writeTransactionToDatabase(tr10)
        assertThat(repo.readBalance(date = 2)).isWithin(0.001).of(-10.0)
        assertThat(repo.readBalance(date = 5)).isWithin(0.001).of(90.0)
        assertThat(repo.readBalance(date = 10)).isWithin(0.001).of(60.0)
        assertThat(repo.readBalance(date = 12)).isWithin(0.001).of(30.0)
        assertThat(repo.readBalance(date = 20)).isWithin(0.001).of(-70.0)
        assertThat(repo.readBalance(date = 22)).isWithin(0.001).of(30.0)
        assertThat(repo.readBalance(date = 25)).isWithin(0.001).of(-30.0)
        assertThat(repo.readBalance(date = 32)).isWithin(0.001).of(-45.0)
        assertThat(repo.readBalance(date = 60)).isWithin(0.001).of(-15.0)

        val tr11 = Transaction(0, "end", 70.0, 65, 0, Category.Healthcare)
        repo.writeTransactionToDatabase(tr11)
        assertThat(repo.readBalance(date = 2)).isWithin(0.001).of(-10.0)
        assertThat(repo.readBalance(date = 5)).isWithin(0.001).of(90.0)
        assertThat(repo.readBalance(date = 10)).isWithin(0.001).of(60.0)
        assertThat(repo.readBalance(date = 12)).isWithin(0.001).of(30.0)
        assertThat(repo.readBalance(date = 20)).isWithin(0.001).of(-70.0)
        assertThat(repo.readBalance(date = 22)).isWithin(0.001).of(30.0)
        assertThat(repo.readBalance(date = 25)).isWithin(0.001).of(-30.0)
        assertThat(repo.readBalance(date = 32)).isWithin(0.001).of(-45.0)
        assertThat(repo.readBalance(date = 60)).isWithin(0.001).of(-15.0)
        assertThat(repo.readBalance(date = 65)).isWithin(0.001).of(-85.0)

        assertThat(repo.readBalance(date = 1)).isWithin(0.001).of(0.0)
        assertThat(repo.readBalance(date = 70)).isWithin(0.001).of(-85.0)

    }

    @Test
    fun categoryTypeChange() = runBlockingTest {
        val repo = TransactionsRepository(db)
        val tr1 = Transaction(1, "100", 100.0, 23, 0, Category.Education)
        repo.writeTransactionToDatabase(tr1)

        val tr2 = Transaction(1, "test", 200.0, 43321222146L, 0, Category.Food)
        repo.updateTransaction(tr2, tr1)

        val a = repo.loadCategoryTotal(Category.Education, 0, 1970)
        val b = repo.loadCategoryTotal(Category.Food, 4, 1971)
        val list = repo.dumpCat().getOrAwaitValue()
        print(list)
        assertThat(a).isWithin(0.0001).of(0.0)
        assertThat(b).isWithin(0.0001).of(200.0)
    }
}
