    package com.example.merapaisa
    import android.content.Intent
    import androidx.appcompat.app.AppCompatActivity
    import android.os.Bundle
    import android.provider.Settings.Global
    import android.view.View
    import android.view.WindowManager
    import androidx.core.content.ContextCompat
    import androidx.recyclerview.widget.ItemTouchHelper
    import androidx.recyclerview.widget.LinearLayoutManager
    import androidx.recyclerview.widget.RecyclerView
    import androidx.room.Room
    import com.example.budgettracker.databinding.ActivityMainBinding
    import com.example.merapaisa.databinding.ActivityMainBinding
    import com.google.android.material.snackbar.Snackbar
    import kotlinx.coroutines.GlobalScope
    import kotlinx.coroutines.launch

    class MainActivity : AppCompatActivity() {


        private lateinit var deletedTransaction: Transaction
        private lateinit var binding: ActivityMainBinding
        private lateinit var transactions:List<Transaction>
        private lateinit var oldTransactions:List<Transaction>
        private lateinit var transactionAdapter: TransactionAdapter
        private lateinit var linearlayoutManager:LinearLayoutManager
        private lateinit var db:AppDatabase

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)



            supportActionBar?.hide()



            transactions= arrayListOf()

            transactionAdapter  = TransactionAdapter(transactions)
            linearlayoutManager = LinearLayoutManager(this)

            db = Room.databaseBuilder(this,AppDatabase::class.java,"transactions").build()

            binding.recyclerview.apply {
                adapter = transactionAdapter
                layoutManager = linearlayoutManager
            }

            //Swipe to remove
            binding.addBtn.setOnClickListener{
                val intent = Intent(this,AddTransactionActivity::class.java)
                startActivity(intent)
            }




        }
        private fun fetchAll(){

            GlobalScope.launch {

                transactions=db.transactionDAO().getAll()

                runOnUiThread{
                    updateDashBoard()
                    transactionAdapter.setData(transactions)
                }
            }
        }

        private fun updateDashBoard(){
            val totalAmount = transactions.map { it.amount }.sum()
            val budgetAmount = transactions.filter { it.amount>0 }.map { it.amount }.sum()
            val expenseAmount = totalAmount-budgetAmount

            binding.balance.text = "$ %.2f".format(totalAmount)
            binding.budget.text = "$ %.2f".format(budgetAmount)
            binding.expense.text = "$ %.2f".format(expenseAmount)

        }}