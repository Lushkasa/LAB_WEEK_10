package com.example.lab_week_10

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.lab_week_10.database.Total
import com.example.lab_week_10.database.TotalDatabase
import com.example.lab_week_10.database.TotalObject
import com.example.lab_week_10.viewmodels.TotalViewModel
import java.util.Date

class MainActivity : AppCompatActivity() {
    private val db by lazy { prepareDatabase() }
    private val viewModel by lazy {
        ViewModelProvider(this)[TotalViewModel::class.java]
    }

    override fun onStart() {
        super.onStart()

        val data = db.totalDao().getTotal(ID)

        data?.let {
            Toast.makeText(this, it.total.date, Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeValueFromDatabase()
        prepareViewModel()
    }

    override fun onPause() {
        super.onPause()
        val newDate = Date().toString()

        val updatedTotal = Total(
            id = ID,
            total = TotalObject(
                value = viewModel.total.value ?: 0,
                date = newDate
            )
        )

        db.totalDao().update(updatedTotal)
    }


    private fun updateText(total: Int) {
        findViewById<TextView>(R.id.text_total).text =
            getString(R.string.text_total, total)
    }
    private fun prepareViewModel(){
        viewModel.total.observe(this, {
            // Whenever the value of the LiveData object changes
            // the updateText() is called, with the new value as the parameter
            updateText(it)
        })

        findViewById<Button>(R.id.button_increment).setOnClickListener {
            viewModel.incrementTotal()
        }
    }

    private fun prepareDatabase(): TotalDatabase {
        return Room.databaseBuilder(
            applicationContext,
            TotalDatabase::class.java, "total-database"
        ).fallbackToDestructiveMigration().allowMainThreadQueries().build()
    }

    private fun initializeValueFromDatabase() {
        val existing = db.totalDao().getTotal(ID)

        if (existing == null) {
            db.totalDao().insert(
                Total(
                    id = ID,
                    total = TotalObject(
                        value = 0,
                        date = Date().toString()
                    )
                )
            )
        } else {
            viewModel.setTotal(existing.total.value)
        }
    }
    companion object {
        const val ID: Long = 1
    }
}