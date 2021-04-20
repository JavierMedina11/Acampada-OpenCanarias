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
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_home.*
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

        state = this.intent.getStringExtra("state").toString()
        val zoneId = this.intent.getIntExtra("zoneId", 1)
        preferences["zoneIII"] = zoneId

        //getZone(zoneId)
        val timeZone = "GMT+1"
        val prueba = "1"
        val dataTime = preferences.getString("dataTime", "2021-03-24")
        val dataTime2 = preferences.getString("dataTime2", "1")
        val dataTime3 = preferences.getString("dataTime3", "1")
        val dataTime4 = preferences.getString("dataTime4", "1")
        val dataTime5 = preferences.getString("dataTime5", "1")

        var listaZones = emptyList<Zone>()

        val database = AppDatabase.getDatabase(this)

        var getReservas = emptyList<Reserva>()
        val data = obtenerFechaActual(timeZone).toString()

        database.reservas().getByDate(zoneId, data).observe(this, Observer {
            getReservas = it
            viewAdapter = ReservaAdapter(getReservas as ArrayList<Reserva>, this)
            recyclerView.adapter = viewAdapter
            preferences["dataTime"] = data;
        })

        /*database.zonas().getById(zoneId).observe(this, Observer {
            listaZones = it
            val url = "https://cryptic-dawn-95434.herokuapp.com/img/"
            val imageUrl = url + listaZones[0].url_img + ".jpg"
            Picasso.with(this).load(imageUrl).into(bg_lists);
        })*/

        groupRadio.clearCheck()
        groupRadio.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener() { radioGroup: RadioGroup, i: Int ->
            val dataTime = preferences.getString("dataTime", "1")
            val dataTime2 = preferences.getString("dataTime2", "1")
            val dataTime3 = preferences.getString("dataTime3", "1")
            val dataTime4 = preferences.getString("dataTime4", "1")
            val dataTime5 = preferences.getString("dataTime5", "1")
            if (radioButton.isChecked) {
                Log.v("checked", "Checked")
                radioButton2.isChecked = false
                if (dataTime != null && dataTime2 != null && dataTime3 != null && dataTime4 != null && dataTime5!=null) {
                    if (spinnerResult2.text == "1 dia") {
                        database.reservas().getByDateChecked(zoneId, "1", dataTime).observe(this, {
                            getReservas = it
                            viewAdapter = ReservaAdapter(getReservas as ArrayList<Reserva>, this)
                            recyclerView.adapter = viewAdapter
                        })
                    }
                    if (spinnerResult2.text == "3 dias") {
                        database.reservas().getByDateCheckedPer3Days(zoneId, "1", dataTime, dataTime2, dataTime3).observe(this, {
                            getReservas = it
                            viewAdapter = ReservaAdapter(getReservas as ArrayList<Reserva>, this)
                            recyclerView.adapter = viewAdapter
                        })
                    }
                    if (spinnerResult2.text == "5 dias") {
                        database.reservas().getByDateCheckedPer5Days(zoneId, "1", dataTime, dataTime2, dataTime3, dataTime4, dataTime5).observe(this, {
                            getReservas = it
                            viewAdapter = ReservaAdapter(getReservas as ArrayList<Reserva>, this)
                            recyclerView.adapter = viewAdapter
                        })
                    }
                }
            } else if (radioButton2.isChecked) {
                Log.v("checked2", "Checked2")
                radioButton.isChecked = false
                if (dataTime != null) {
                    if (spinnerResult2.text == "1 dia") {
                        database.reservas().getByDateChecked(zoneId, dataTime, "0").observe(this, {
                            getReservas = it
                            viewAdapter = ReservaAdapter(getReservas as ArrayList<Reserva>, this)
                            recyclerView.adapter = viewAdapter
                        })
                    }
                    if (spinnerResult2.text == "3 dias") {
                        dataTime3?.let {
                            if (dataTime2 != null) {
                                database.reservas().getByDateCheckedPer3Days(zoneId, "0", dataTime, dataTime2,
                                    dataTime3).observe(this, {
                                    getReservas = it
                                    viewAdapter = ReservaAdapter(getReservas as ArrayList<Reserva>, this)
                                    recyclerView.adapter = viewAdapter
                                })
                            }
                        }
                    }
                    if (spinnerResult2.text == "5 dias") {
                        if (dataTime2 != null && dataTime3 != null && dataTime4 != null && dataTime5!=null) {
                            database.reservas().getByDateCheckedPer5Days(zoneId, "0", dataTime, dataTime2, dataTime3, dataTime4, dataTime5).observe(this, {
                                getReservas = it
                                viewAdapter = ReservaAdapter(getReservas as ArrayList<Reserva>, this)
                                recyclerView.adapter = viewAdapter
                            })
                        }
                    }
                }
            }
        })


        //listeners(zoneId)
        //getReservas()

        syncDBLocalToDBServerBookingsChecked()
        //syncDBServerToDBLocalBookingsNoChecked()

        selects()
    }

    private fun syncDBLocalToDBServerBookingsChecked(){
        var listaReserva = emptyList<Reserva>()
        reserva = ArrayList<Reserva>()
        val database = AppDatabase.getDatabase(this)
        val serviceImpl = ServiceImpl()
        database.reservas().getByCheck1().observe(this, Observer {
            listaReserva = it as ArrayList<Reserva>

            for (i in 0 until listaReserva.size) {
                Log.v("Update", "Entro: " + reserva)
                serviceImpl.updateReserve(this, listaReserva[i]) { ->
                    run {
                        Log.v("UPDATED", "Updated: " + listaReserva[i].id)
                    }
                }
            }
        });
    }/*

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
    }*/

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
            var getReservas = emptyList<Reserva>()
            val database = AppDatabase.getDatabase(this)

            if (spinnerResult2.text == "1 dia") {
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
            }
            else if (spinnerResult2.text == "3 dias"){
                database.reservas().getByDatePer3Days(
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
                })
            }
            else if (spinnerResult2.text == "5 dias"){
                database.reservas().getByDatePer5Days(
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
        val date = calendar.time
        val sdf: SimpleDateFormat
        sdf = SimpleDateFormat(formato)
        sdf.setTimeZone(TimeZone.getTimeZone(zonaHoraria))
        return sdf.format(date)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun obtenerFechaActual(zonaHoraria: String?): String? {
        val formato = "yyyy-MM-dd"
        return obtenerFechaConFormato(formato, zonaHoraria)
    }
}


/*
private fun getBookingsDate(zoneId: Int, prueba: String) {
    val roomServiceImpl = ServiceImpl()
    roomServiceImpl.getBookingByDate(this, zoneId, prueba) { response ->
        run {
            if (response != null) {
                viewAdapter.notifyDataSetChanged()
                viewAdapter.reservaList = response
            }
            viewAdapter.notifyDataSetChanged()
        }
    }
}

private fun getBookingLocalizador(localizador_id: String) {
    val roomServiceImpl = ServiceImpl()
    roomServiceImpl.getBookingByLocalizador(this, localizador_id) { response ->
        run {
            if (response != null) {
                viewAdapter.notifyDataSetChanged()
                viewAdapter.reservaList = response
            }
            viewAdapter.notifyDataSetChanged()
        }
    }
}

private fun getZone(zoneId: Int) {
    val bicycleServiceImpl = ServiceImpl()
    bicycleServiceImpl.getZoneById(this, zoneId) { response ->
        run {
            val url = "http://192.168.56.1:8000/img/"
            val imageUrl = url + response?.url_img + ".jpg"
            Picasso.with(this).load(imageUrl).into(bg_lists);
        }
    }
}*/

