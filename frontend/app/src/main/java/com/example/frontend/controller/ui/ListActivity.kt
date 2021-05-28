package com.example.frontend.controller.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.frontend.R
import com.example.frontend.controller.io.ServiceImpl
import com.example.frontend.controller.models.Reserva
import com.example.frontend.controller.models.Zone
import com.example.frontend.controller.util.PreferenceHelper
import com.example.frontend.controller.util.PreferenceHelper.set
import com.example.frontend.controller.util.ReservaAdapter
import com.opencanarias.pruebasync.util.AppDatabase
import kotlinx.android.synthetic.main.activity_list.*
import kotlinx.android.synthetic.main.activity_list.spinner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ListActivity : AppCompatActivity() {
    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
    }

    override fun onBackPressed(){
        super.onBackPressed();
        val intent = Intent(this, ZoneActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    private lateinit var state: String

    private lateinit var reserva: ArrayList<Reserva>
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: ReservaAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    var getReservas = emptyList<Reserva>()

    @RequiresApi(Build.VERSION_CODES.N)
    private val selectedCalendar: Calendar = Calendar.getInstance()

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        reserva = ArrayList<Reserva>()

        viewManager = LinearLayoutManager(this)
        viewAdapter = ReservaAdapter(reserva, this)
        recyclerView = findViewById<RecyclerView>(R.id.locationViewPager2)
        // use a linear layout manager
        recyclerView.layoutManager = viewManager
        // specify an viewAdapter (see also next example)
        recyclerView.adapter = viewAdapter

        recyclerView.itemAnimator
        val database = AppDatabase.getDatabase(this)

        state = this.intent.getStringExtra("state").toString()
        val zoneId = this.intent.getIntExtra("zoneId", 1)
        preferences["zoneIII"] = zoneId
        preferences["trueState"] = state

        val timeZone = "GMT+1"
        val sdf2 = preferences.getString("sdf2", "1")
        Log.v("Prueba", "sdf2: " + sdf2)
        val sdf3 = preferences.getString("sdf3", "1")
        Log.v("Prueba", "sdf3: " + sdf3)
        val sdf4 = preferences.getString("sdf4", "1")
        Log.v("Prueba", "sdf4: " + sdf4)

        var listaZones = emptyList<Zone>()

        var getReservas = emptyList<Reserva>()
        val data = obtenerFechaActual(timeZone).toString()

        if(state == "Entradas") {
            if (sdf2 != null && sdf3 != null && sdf4 != null) {
                database.reservas().getByDatePer4Days(zoneId, data, sdf2, sdf3, sdf4)
                    .observe(this, Observer {
                        getReservas = it
                        viewAdapter = ReservaAdapter(getReservas as ArrayList<Reserva>, this)
                        recyclerView.adapter = viewAdapter
                        preferences["dataTime"] = data;
                    })
            }
        }else if (state == "Salidas"){
            if (sdf2 != null && sdf3 != null && sdf4 != null) {
                database.reservas().getByDatePer4DaySalidas(zoneId, data, sdf2, sdf3, sdf4).observe(this, Observer {
                    getReservas = it
                    viewAdapter = ReservaAdapter(getReservas as ArrayList<Reserva>, this)
                    recyclerView.adapter = viewAdapter
                    preferences["dataTime"] = data;
                })
            }
        }
        groupRadioFun(zoneId, state)

        syncDBLocalToDBServerBookingsChecked()
        //syncDBServerToDBLocalBookingsNoChecked()
        selects()
    }

    private fun groupRadioFun(zoneId: Int, state: String){
        val database = AppDatabase.getDatabase(this)
        if(state == "Entradas") {
            groupRadio.clearCheck()
            groupRadio.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener() { radioGroup: RadioGroup, i: Int ->
                val dataTime = preferences.getString("dataTime", "1")
                val dataTime2 = preferences.getString("dataTime2", "1")
                val dataTime3 = preferences.getString("dataTime3", "1")
                val dataTime4 = preferences.getString("dataTime4", "1")
                val dataTime5 = preferences.getString("dataTime5", "1")
                val dataTime6 = preferences.getString("dataTime6", "1")
                val dataTime7 = preferences.getString("dataTime7", "1")
                if (radioButton.isChecked) {
                    Log.v("checked", "Checked")
                    radioButton2.isChecked = false
                    if (dataTime != null && dataTime2 != null && dataTime3 != null && dataTime4 != null && dataTime5!=null && dataTime6 != null && dataTime7!=null) {
                        if (spinnerResult2.text == "1 dia") {
                            database.reservas().getByDateChecked(zoneId, "1", dataTime).observe(this, {
                                getReservas = it
                                viewAdapter = ReservaAdapter(getReservas as ArrayList<Reserva>, this)
                                recyclerView.adapter = viewAdapter
                            })
                        }
                        if (spinnerResult2.text == "3 dias") {
                            database.reservas().getByDateCheckedPer4Days(zoneId, "1", dataTime, dataTime2, dataTime3, dataTime4).observe(this, {
                                getReservas = it
                                viewAdapter = ReservaAdapter(getReservas as ArrayList<Reserva>, this)
                                recyclerView.adapter = viewAdapter
                            })
                        }
                        if (spinnerResult2.text == "5 dias") {
                            database.reservas().getByDateCheckedPer7Days(zoneId, "1", dataTime, dataTime2, dataTime3, dataTime4, dataTime5, dataTime6, dataTime7).observe(this, {
                                getReservas = it
                                viewAdapter = ReservaAdapter(getReservas as ArrayList<Reserva>, this)
                                recyclerView.adapter = viewAdapter
                            })
                        }
                    }
                } else if (radioButton2.isChecked) {
                    Log.v("checked2", "Checked2")
                    radioButton.isChecked = false
                    if (dataTime != null && dataTime2 != null && dataTime3 != null && dataTime4 != null && dataTime5!=null && dataTime6 != null && dataTime7!=null) {
                        if (spinnerResult2.text == "1 dia") {
                            database.reservas().getByDateChecked(zoneId, "0", dataTime).observe(this, {
                                getReservas = it
                                viewAdapter = ReservaAdapter(getReservas as ArrayList<Reserva>, this)
                                recyclerView.adapter = viewAdapter
                            })
                        }
                        if (spinnerResult2.text == "3 dias") {
                            dataTime3?.let {
                                if (dataTime2 != null) {
                                    database.reservas().getByDateCheckedPer4Days(zoneId, "0", dataTime, dataTime2,
                                        dataTime3, dataTime4).observe(this, {
                                        getReservas = it
                                        viewAdapter = ReservaAdapter(getReservas as ArrayList<Reserva>, this)
                                        recyclerView.adapter = viewAdapter
                                    })
                                }
                            }
                        }
                        if (spinnerResult2.text == "5 dias") {
                            if (dataTime2 != null && dataTime3 != null && dataTime4 != null && dataTime5!=null && dataTime6 != null && dataTime7!=null) {
                                database.reservas().getByDateCheckedPer7Days(zoneId, "0", dataTime, dataTime2, dataTime3, dataTime4, dataTime5, dataTime6, dataTime7).observe(this, {
                                    getReservas = it
                                    viewAdapter = ReservaAdapter(getReservas as ArrayList<Reserva>, this)
                                    recyclerView.adapter = viewAdapter
                                })
                            }
                        }
                    }
                }
            })
        }else if (state == "Salidas"){
            Log.v("De puta madre", "Puta Madre Todo")
            groupRadio.clearCheck()
            groupRadio.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener() { radioGroup: RadioGroup, i: Int ->
                val dataTime = preferences.getString("dataTime", "1")
                val dataTime2 = preferences.getString("dataTime2", "1")
                val dataTime3 = preferences.getString("dataTime3", "1")
                val dataTime4 = preferences.getString("dataTime4", "1")
                val dataTime5 = preferences.getString("dataTime5", "1")
                val dataTime6 = preferences.getString("dataTime6", "1")
                val dataTime7 = preferences.getString("dataTime7", "1")
                if (radioButton.isChecked) {
                    Log.v("checked", "Checked")
                    radioButton2.isChecked = false
                    if (dataTime != null && dataTime2 != null && dataTime3 != null && dataTime4 != null && dataTime5!=null && dataTime6 != null && dataTime7!=null) {
                        if (spinnerResult2.text == "1 dia") {
                            database.reservas().getByDateCheckedSalidas(zoneId, "1", dataTime).observe(this, {
                                getReservas = it
                                viewAdapter = ReservaAdapter(getReservas as ArrayList<Reserva>, this)
                                recyclerView.adapter = viewAdapter
                            })
                        }
                        if (spinnerResult2.text == "3 dias") {
                            database.reservas().getByDateCheckedPer4DaySalidas(zoneId, "1", dataTime, dataTime2, dataTime3, dataTime4).observe(this, {
                                getReservas = it
                                viewAdapter = ReservaAdapter(getReservas as ArrayList<Reserva>, this)
                                recyclerView.adapter = viewAdapter
                            })
                        }
                        if (spinnerResult2.text == "5 dias") {
                            database.reservas().getByDateCheckedPer7DaySalidas(zoneId, "1", dataTime, dataTime2, dataTime3, dataTime4, dataTime5, dataTime6, dataTime7).observe(this, {
                                getReservas = it
                                viewAdapter = ReservaAdapter(getReservas as ArrayList<Reserva>, this)
                                recyclerView.adapter = viewAdapter
                            })
                        }
                    }
                } else if (radioButton2.isChecked) {
                    Log.v("checked2", "Checked2")
                    radioButton.isChecked = false
                    if (dataTime != null && dataTime2 != null && dataTime3 != null && dataTime4 != null && dataTime5!=null && dataTime6 != null && dataTime7!=null) {
                        if (spinnerResult2.text == "1 dia") {
                            database.reservas().getByDateCheckedSalidas(zoneId, "0", dataTime).observe(this, {
                                getReservas = it
                                viewAdapter = ReservaAdapter(getReservas as ArrayList<Reserva>, this)
                                recyclerView.adapter = viewAdapter
                            })
                        }
                        if (spinnerResult2.text == "3 dias") {
                            dataTime3?.let {
                                if (dataTime2 != null) {
                                    database.reservas().getByDateCheckedPer4DaySalidas(zoneId, "0", dataTime, dataTime2, dataTime3, dataTime4).observe(this, {
                                        getReservas = it
                                        viewAdapter = ReservaAdapter(getReservas as ArrayList<Reserva>, this)
                                        recyclerView.adapter = viewAdapter
                                    })
                                }
                            }
                        }
                        if (spinnerResult2.text == "5 dias") {
                            if (dataTime2 != null && dataTime3 != null && dataTime4 != null && dataTime5!=null && dataTime6 != null && dataTime7!=null) {
                                database.reservas().getByDateCheckedPer7DaySalidas(zoneId, "0", dataTime, dataTime2, dataTime3, dataTime4, dataTime5, dataTime6, dataTime7).observe(this, {
                                    getReservas = it
                                    viewAdapter = ReservaAdapter(getReservas as ArrayList<Reserva>, this)
                                    recyclerView.adapter = viewAdapter
                                })
                            }
                        }
                    }
                }
            })
        }

    }

    private fun syncDBLocalToDBServerBookingsChecked(){
        var listaReserva = emptyList<Reserva>()
        reserva = ArrayList<Reserva>()
        val database = AppDatabase.getDatabase(this)
        val serviceImpl = ServiceImpl()
        database.reservas().getByCheck1().observe(this, Observer {
            listaReserva = it as ArrayList<Reserva>

            for (i in 0 until listaReserva.size) {
                serviceImpl.updateReserve(this, listaReserva[i]) { ->
                    run {
                        Log.v("UPDATED", "Updated: " + listaReserva[i].id)
                    }
                }
                Log.v("Update", "Entro: " + listaReserva.size)
            }
        });
    }

    private fun syncDBServerToDBLocalBookingsNoChecked() {
        val bicycleServiceImpl = ServiceImpl()
        bicycleServiceImpl.getAllBookingsNoChecked(this) { response ->
            run {
                val database = AppDatabase.getDatabase(this)
                CoroutineScope(Dispatchers.IO).launch{
                    database.reservas().deleteNoChecked("0")
                    Log.v("DBBorrao", "BD Borrada, reservas vacias personas")
                    val reservaArray : ArrayList<Reserva>? = response
                    delay(5000)
                    if (reservaArray != null) {
                        for (i in 0 until reservaArray.size) {
                            Log.v("FUNCIONA", "Insertadas")
                            database.reservas().insert(reservaArray[i])
                        }
                    }
                }
            }
        }
    }

    private fun selects(){
        val typesOfNum2 = arrayOf("1 dia", "3 dias", "5 dias")
        val arrayAdapter2 = ArrayAdapter(this, android.R.layout.simple_spinner_item, typesOfNum2)

        spinner.adapter = arrayAdapter2
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                spinnerResult2.text = typesOfNum2[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onClickSheduleDate(v: View?) {

        val year = selectedCalendar.get(Calendar.YEAR)
        val month = selectedCalendar.get(Calendar.MONTH)
        val dayOfMonth = selectedCalendar.get(Calendar.DAY_OF_MONTH)

        val listener = DatePickerDialog.OnDateSetListener { datePicker, y, m, d ->
            //Toast.makeText(this, "$y-$m-$d", Toast.LENGTH_SHORT).show()
            selectedCalendar.set(y, m, d)
            reservationDate.setText(
                resources.getString(
                    R.string.date_format,
                    y,
                    (m + 1).twoDigits(),
                    d.twoDigits()
                )
            )

            val opeIdPref = preferences.getInt("zoneIII", 0)
            val state = preferences.getString("trueState", "1")
            var getReservas = emptyList<Reserva>()
            val database = AppDatabase.getDatabase(this)

            if (spinnerResult2.text == "1 dia" && state == "Entradas") {
                database.reservas().getByDate(
                    opeIdPref, resources.getString(
                        R.string.date_format,
                        y,
                        (m + 1).twoDigits(),
                        d.twoDigits()
                    )
                ).observe(this, Observer {
                    getReservas = it
                    viewAdapter = ReservaAdapter(getReservas as ArrayList<Reserva>, this)
                    recyclerView.adapter = viewAdapter
                    preferences["dataTime"] = resources.getString(
                        R.string.date_format,
                        y,
                        (m + 1).twoDigits(),
                        d.twoDigits()
                    )
                })
            }else if (spinnerResult2.text == "1 dia" && state == "Salidas") {
            database.reservas().getByDateSalidas(
                opeIdPref, resources.getString(
                    R.string.date_format,
                    y,
                    (m + 1).twoDigits(),
                    d.twoDigits()
                )
            ).observe(this, Observer {
                getReservas = it
                viewAdapter = ReservaAdapter(getReservas as ArrayList<Reserva>, this)
                recyclerView.adapter = viewAdapter
                preferences["dataTime"] = resources.getString(
                    R.string.date_format,
                    y,
                    (m + 1).twoDigits(),
                    d.twoDigits()
                )
            })
            }else if (spinnerResult2.text == "3 dias" && state == "Entradas"){
                database.reservas().getByDatePer4Days(
                    opeIdPref, resources.getString(
                        R.string.date_format,
                        y,
                        (m + 1).twoDigits(),
                        d.twoDigits()
                    ), resources.getString(
                        R.string.date_format,
                        y,
                        (m + 1).twoDigits(),
                        (d - 1).twoDigits()
                    ), resources.getString(
                        R.string.date_format,
                        y,
                        (m + 1).twoDigits(),
                        (d - 2).twoDigits()
                    ), resources.getString(
                        R.string.date_format,
                        y,
                        (m + 1).twoDigits(),
                        (d - 3).twoDigits()
                    )
                ).observe(this, Observer {
                    getReservas = it
                    viewAdapter = ReservaAdapter(getReservas as ArrayList<Reserva>, this)
                    recyclerView.adapter = viewAdapter
                    preferences["dataTime"] = resources.getString(
                        R.string.date_format,
                        y,
                        (m + 1).twoDigits(),
                        d.twoDigits()
                    )
                    preferences["dataTime2"] = resources.getString(
                        R.string.date_format,
                        y,
                        (m + 1).twoDigits(),
                        (d - 1).twoDigits()
                    )
                    preferences["dataTime3"] = resources.getString(
                        R.string.date_format,
                        y,
                        (m + 1).twoDigits(),
                        (d - 2).twoDigits()
                    )
                    preferences["dataTime4"] = resources.getString(
                        R.string.date_format,
                        y,
                        (m + 1).twoDigits(),
                        (d - 3).twoDigits()
                    )
                })
            }else if (spinnerResult2.text == "3 dias" && state == "Salidas"){
                database.reservas().getByDatePer4DaySalidas(
                    opeIdPref, resources.getString(
                        R.string.date_format,
                        y,
                        (m + 1).twoDigits(),
                        d.twoDigits()
                    ), resources.getString(
                        R.string.date_format,
                        y,
                        (m + 1).twoDigits(),
                        (d - 1).twoDigits()
                    ), resources.getString(
                        R.string.date_format,
                        y,
                        (m + 1).twoDigits(),
                        (d - 2).twoDigits()
                    ), resources.getString(
                        R.string.date_format,
                        y,
                        (m + 1).twoDigits(),
                        (d - 3).twoDigits()
                    )
                ).observe(this, Observer {
                    getReservas = it
                    viewAdapter = ReservaAdapter(getReservas as ArrayList<Reserva>, this)
                    recyclerView.adapter = viewAdapter
                    preferences["dataTime"] = resources.getString(
                        R.string.date_format,
                        y,
                        (m + 1).twoDigits(),
                        d.twoDigits()
                    )
                    preferences["dataTime2"] = resources.getString(
                        R.string.date_format,
                        y,
                        (m + 1).twoDigits(),
                        (d - 1).twoDigits()
                    )
                    preferences["dataTime3"] = resources.getString(
                        R.string.date_format,
                        y,
                        (m + 1).twoDigits(),
                        (d - 2).twoDigits()
                    )
                    preferences["dataTime4"] = resources.getString(
                        R.string.date_format,
                        y,
                        (m + 1).twoDigits(),
                        (d - 3).twoDigits()
                    )
                })
            }
            else if (spinnerResult2.text == "5 dias" && state == "Entradas"){
                database.reservas().getByDatePer7Days(
                    opeIdPref, resources.getString(
                        R.string.date_format,
                        y,
                        (m + 1).twoDigits(),
                        d.twoDigits()
                    ), resources.getString(
                        R.string.date_format,
                        y,
                        (m + 1).twoDigits(),
                        (d - 1).twoDigits()
                    ), resources.getString(
                        R.string.date_format,
                        y,
                        (m + 1).twoDigits(),
                        (d - 2).twoDigits()
                    ), resources.getString(
                        R.string.date_format,
                        y,
                        (m + 1).twoDigits(),
                        (d - 3).twoDigits()
                    ), resources.getString(
                        R.string.date_format,
                        y,
                        (m + 1).twoDigits(),
                        (d - 4).twoDigits()
                    ), resources.getString(
                        R.string.date_format,
                        y,
                        (m + 1).twoDigits(),
                        (d - 5).twoDigits()
                    ), resources.getString(
                        R.string.date_format,
                        y,
                        (m + 1).twoDigits(),
                        (d - 6).twoDigits()
                    )
                ).observe(this, Observer {
                    getReservas = it
                    viewAdapter = ReservaAdapter(getReservas as ArrayList<Reserva>, this)
                    recyclerView.adapter = viewAdapter
                    preferences["dataTime"] = resources.getString(
                        R.string.date_format,
                        y,
                        (m + 1).twoDigits(),
                        d.twoDigits()
                    )
                    preferences["dataTime2"] = resources.getString(
                        R.string.date_format,
                        y,
                        (m + 1).twoDigits(),
                        (d - 1).twoDigits()
                    )
                    preferences["dataTime3"] = resources.getString(
                        R.string.date_format,
                        y,
                        (m + 1).twoDigits(),
                        (d - 2).twoDigits()
                    )
                    preferences["dataTime4"] = resources.getString(
                        R.string.date_format,
                        y,
                        (m + 1).twoDigits(),
                        (d - 3).twoDigits()
                    )
                    preferences["dataTime5"] = resources.getString(
                        R.string.date_format,
                        y,
                        (m + 1).twoDigits(),
                        (d - 4).twoDigits()
                    )
                    preferences["dataTime6"] = resources.getString(
                        R.string.date_format,
                        y,
                        (m + 1).twoDigits(),
                        (d - 5).twoDigits()
                    )
                    preferences["dataTime7"] = resources.getString(
                        R.string.date_format,
                        y,
                        (m + 1).twoDigits(),
                        (d - 6).twoDigits()
                    )
                })
            }else if (spinnerResult2.text == "5 dias" && state == "Salidas"){
                database.reservas().getByDatePer7DaySalidas(
                    opeIdPref, resources.getString(
                        R.string.date_format,
                        y,
                        (m + 1).twoDigits(),
                        d.twoDigits()
                    ), resources.getString(
                        R.string.date_format,
                        y,
                        (m + 1).twoDigits(),
                        (d - 1).twoDigits()
                    ), resources.getString(
                        R.string.date_format,
                        y,
                        (m + 1).twoDigits(),
                        (d - 2).twoDigits()
                    ), resources.getString(
                        R.string.date_format,
                        y,
                        (m + 1).twoDigits(),
                        (d - 3).twoDigits()
                    ), resources.getString(
                        R.string.date_format,
                        y,
                        (m + 1).twoDigits(),
                        (d - 4).twoDigits()
                    ), resources.getString(
                        R.string.date_format,
                        y,
                        (m + 1).twoDigits(),
                        (d - 5).twoDigits()
                    ), resources.getString(
                        R.string.date_format,
                        y,
                        (m + 1).twoDigits(),
                        (d - 6).twoDigits()
                    )
                ).observe(this, Observer {
                    getReservas = it
                    viewAdapter = ReservaAdapter(getReservas as ArrayList<Reserva>, this)
                    recyclerView.adapter = viewAdapter
                    preferences["dataTime"] = resources.getString(
                        R.string.date_format,
                        y,
                        (m + 1).twoDigits(),
                        d.twoDigits()
                    )
                    preferences["dataTime2"] = resources.getString(
                        R.string.date_format,
                        y,
                        (m + 1).twoDigits(),
                        (d - 1).twoDigits()
                    )
                    preferences["dataTime3"] = resources.getString(
                        R.string.date_format,
                        y,
                        (m + 1).twoDigits(),
                        (d - 2).twoDigits()
                    )
                    preferences["dataTime4"] = resources.getString(
                        R.string.date_format,
                        y,
                        (m + 1).twoDigits(),
                        (d - 3).twoDigits()
                    )
                    preferences["dataTime5"] = resources.getString(
                        R.string.date_format,
                        y,
                        (m + 1).twoDigits(),
                        (d - 4).twoDigits()
                    )
                    preferences["dataTime6"] = resources.getString(
                        R.string.date_format,
                        y,
                        (m + 1).twoDigits(),
                        (d - 5).twoDigits()
                    )
                    preferences["dataTime7"] = resources.getString(
                        R.string.date_format,
                        y,
                        (m + 1).twoDigits(),
                        (d - 6).twoDigits()
                    )
                })
            }
        }

        val datePickerDialog = DatePickerDialog(
            this,
            R.style.datepicker,
            listener,
            year,
            month,
            dayOfMonth
        )
        val datePicker = datePickerDialog.datePicker

        datePickerDialog.show()
    }

    private fun Int.twoDigits() = if (this >= 10) this.toString() else "0$this"

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SimpleDateFormat")
    fun obtenerFechaConFormato(formato: String?, zonaHoraria: String?): String? {
        val calendar = Calendar.getInstance()
        val calendar2 = Calendar.getInstance()
        calendar2.add(Calendar.DAY_OF_MONTH, -1)
        val calendar3 = Calendar.getInstance()
        calendar3.add(Calendar.DAY_OF_MONTH, -2)
        val calendar4 = Calendar.getInstance()
        calendar4.add(Calendar.DAY_OF_MONTH, -3)

        val date = calendar.time
        val date2 = calendar2.time
        val date3 = calendar3.time
        val date4 = calendar4.time

        val sdf: SimpleDateFormat
        sdf = SimpleDateFormat(formato)
        sdf.setTimeZone(TimeZone.getTimeZone(zonaHoraria))

        val sdf2: SimpleDateFormat
        sdf2 = SimpleDateFormat(formato)
        sdf2.setTimeZone(TimeZone.getTimeZone(zonaHoraria))
        preferences["sdf2"] = sdf2.format(date2).toString()

        val sdf3: SimpleDateFormat
        sdf3 = SimpleDateFormat(formato)
        sdf3.setTimeZone(TimeZone.getTimeZone(zonaHoraria))
        preferences["sdf3"] = sdf3.format(date3).toString()

        val sdf4: SimpleDateFormat
        sdf4 = SimpleDateFormat(formato)
        sdf4.setTimeZone(TimeZone.getTimeZone(zonaHoraria))
        preferences["sdf4"] = sdf4.format(date4).toString()

        Log.v("Prueba", "SDF2: " + sdf.format(date))

        return sdf.format(date)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun obtenerFechaActual(zonaHoraria: String?): String? {
        val formato = "yyyy-MM-dd"
        return obtenerFechaConFormato(formato, zonaHoraria)
    }
}

