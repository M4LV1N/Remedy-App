package com.uts.remedy.screens
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.google.firebase.firestore.FirebaseFirestore
import com.uts.remedy.auth
import kotlinx.coroutines.launch

import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

@Composable
fun ImageScreen() {

    Log.d("5559a","5559a: Field")
    filterData()
    //CalendarScreen()
}

@Composable
fun filterData() {
    val user = auth.currentUser
    val userId = user?.uid ?: "User not available"
    var email_id = user?.email ?: "Email not available"
    val db = FirebaseFirestore.getInstance()
    Log.d("5559a",email_id)
// Akses koleksi "user" dan dokumen "obat"
    val obatDocument = db.collection("user").document(email_id)

// Lakukan query terhadap field tertentu
    var dataList by remember { mutableStateOf<List<Triple<String, String, String>>>(emptyList()) }

    obatDocument.get().addOnSuccessListener { document ->
        var newDataList = mutableListOf<Triple<String, String, String>>()
        if (document != null && document.exists()) {
            // Dapatkan semua field dari dokumen "obat"
            val fields = document.data


            // Filter field yang mengandung '@'
            fields?.forEach { (key, value) ->
                //  Log.d("5559a","Field aaa $key mengandung @: $value")
                if(key.contains("@"))
                {
                    val dat=key.split("@")
                    newDataList.add(Triple(dat[0],dat[1],value.toString()))
                    // Log.d("5559a","Field aaa $key mengandung @: $valuku")
                    Log.d("5559a","Field aaa $key mengandung @: $newDataList")
                }
            }

        }
        dataList=newDataList
        Log.d("5559a","Field akhir @: $dataList")
    }.addOnFailureListener { exception ->
        println("Error mendapatkan data: ${exception.message}")
    }
    Log.d("5559a","Field all @: $dataList")
    TabelData(dataList)

}


@Composable
fun TabelData(dataList: List<Triple<String, String, String>>) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .padding(top = 50.dp)
    ) {
        // Header tabel
        Row(modifier = Modifier.padding(bottom = 8.dp)) {
            Text(text = "Date", modifier = Modifier.weight(1f))
            Text(text = "Medicine", modifier = Modifier.weight(1f))
            Text(text = "Total", modifier = Modifier.weight(1f))
        }

        // Data tabel
        dataList.forEach { (kue0, kue1, extra) ->
            Row(modifier = Modifier.padding(bottom = 8.dp)) {
                Text(text = kue0, modifier = Modifier.weight(1f))
                Text(text = kue1, modifier = Modifier.weight(1f))
                Text(text = extra, modifier = Modifier.weight(1f))
            }
        }
    }
}




//=============================kalender===========================




@Composable
fun CalendarScreen() {
    val today = LocalDate.now()
    val currentMonth = YearMonth.of(today.year, today.month)
    val daysInMonth = currentMonth.lengthOfMonth()
    val firstDayOfWeek = currentMonth.atDay(1).dayOfWeek.value % 7 // 0 untuk Minggu

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top=50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Text(
            text = today.month.getDisplayName(TextStyle.FULL, Locale("id", "ID")),
            fontSize = 24.sp
        )
        Text(
            text = today.year.toString(),
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Hari dalam seminggu
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            listOf("Mo", "Tu", "We", "Th", "Fr", "Sa", "Su").forEach { day ->
                Text(text = day, fontSize = 16.sp)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Tanggal dalam bulan
        Column(modifier = Modifier.fillMaxWidth()) {
            var currentDay = 1
            for (week in 0..5) {
                if (currentDay > daysInMonth) break

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    for (dayOfWeek in 0..6) {
                        if (week == 0 && dayOfWeek < firstDayOfWeek || currentDay > daysInMonth) {
                            // Kosongkan sel
                            Spacer(modifier = Modifier.width(32.dp))
                        } else {
                            Text(
                                text = currentDay.toString(),
                                fontSize = 16.sp,
                                modifier = Modifier.width(32.dp)
                            )
                            currentDay++
                        }
                    }
                }
            }
        }
    }
}
