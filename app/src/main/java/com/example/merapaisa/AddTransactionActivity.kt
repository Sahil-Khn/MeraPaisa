package com.example.merapaisa
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.core.widget.addTextChangedListener
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.budgettracker.R
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddTransactionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction)


        val addTransactionButton = findViewById<Button>(R.id.addTransactionBtn)
        val labelInput = findViewById<EditText>(R.id.labelInput)
        val amountInput = findViewById<EditText>(R.id.amountInput)
        val descriptionInput = findViewById<EditText>(R.id.descriptionInput)
        val labelLayout = findViewById<TextInputLayout>(R.id.labelLayout)
        val amountLayout = findViewById<TextInputLayout>(R.id.amountLayout)
        val closeButton = findViewById<ImageButton>(R.id.closeBtn)


        labelInput.addTextChangedListener {
            if (it!!.isNotEmpty())
                labelLayout.error = null
        }

        amountInput.addTextChangedListener{
            if (it!!.isNotEmpty())
                amountLayout.error = null
        }

        addTransactionButton.setOnClickListener {
            val label = labelInput.text.toString()
            val description = descriptionInput.text.toString()
            val amount = amountInput.text.toString().toDoubleOrNull()

            if(label.isEmpty()){
                labelLayout.error = "Please enter valid label"

            }
            else if(amount==null){
                amountLayout.error = "Please enter a valid amount"
            }else{
                val transaction = amount?.let { it1 -> Transaction(0,label, it1,description) }
                if (transaction != null) {
                    insert(transaction)
                }


            }

        }
        closeButton.setOnClickListener{
            finish()
        }





    }

    private fun insert(transaction: Transaction){
        val db:AppDatabase = Room.databaseBuilder(
            this,AppDatabase::class.java,"transactions").build()

        GlobalScope.launch {
            db.transactionDAO().insertAll(transaction)
            finish()
        }
    }
}