package com.uts.remedy.screens

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.uts.remedy.models.Medicine
import com.uts.remedy.models.MedicineViewModel


import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.uts.remedy.MainActivity
import com.uts.remedy.R
import com.uts.remedy.auth
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun Daily_update_Screen() {
    val viewModel: MedicineViewModel = viewModel()
    var nama: String? = "OBAT1-3-A#OBAT2-2-B"
    var daftar_obat = nama!!.split("#").toList()
    var aturan_obat = daftar_obat[0]!!.split("-").toList()
    val aturan = Array(10) { Array(10) { "" } }
    val obat = Array(10){""}
    val rpt = Array(10){""}
    val mkn = Array(10){""}
    var a=0;
    var b=0;


    val user = auth.currentUser
    val userId = user?.uid ?: "User not available"
    var email_id = user?.email ?: "Email not available"

    var periode="2025-01-01"
    var login="no"

    periode=getCurrentDate()



        val androidId =email_id
        val dft=viewModel.ReadFirestoreField(androidId,"daftar_obat")
    Log.d("dsp99",androidId + "*" + email_id)
        if(email_id=="null") {

            //====================================

        }
        else
        {

            nama = viewModel.ReadFirestoreField(androidId, "daftar_obat")
            if(nama=="null")
            {
                viewModel.update_firebase(androidId,"daftar_obat","FLU-3-A-10-0")
                nama="FLU-3-A-10-0"
            }

            if (nama != "" && nama!="null") {

                daftar_obat = nama!!.split("#").toList()
                TabelObat(email_id,nama.toString(), androidId, periode, daftar_obat)
                AppContent()
            }
        }

        //=========================

        //=========================




}





@Composable
fun RestartButton() {
    val context = LocalContext.current

    Button(onClick = {
        restartApp(context)
    },
        modifier = Modifier
            .padding(8.dp)
            .width(150.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor  = Color.Black)
    ) {
        androidx.compose.material3.Text("Restart App", color = Color.White)
    }
}




@Composable
fun AppContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .alpha(0f),
        horizontalAlignment = Alignment.CenterHorizontally,
        // verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(50.dp))
        androidx.compose.material3.Text("Hello, Jetpack Compose!")
        Spacer(modifier = Modifier.height(6.dp))
        RestartButton()
    }
}




fun getCurrentDate(): String {
    val currentDate = LocalDate.now() // Mendapatkan tanggal hari ini
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd") // Format tanggal
    return currentDate.format(formatter) // Format dan kembalikan sebagai String
}


@Composable
fun TabelObat(email_id :String,daftarku:String,androidId:String,periode:String,daftar_obat: List<String>) {

    val context = LocalContext.current
    var isFormVisible by remember { mutableStateOf(false) }
    var aturan_obat = "A-1-2-B"!!.split("-").toList()
    val obat = Array(10){""}
    val rpt = Array(10){""}
    val mkn = Array(10){""}
    val max_minum = Array(10){""}
    val tot_minum = Array(10){""}
    val sisa = IntArray(10)
    val total_daily = IntArray(10)
    var a=0;
    var b=daftar_obat.size;
    var fvalue="";
    var total="";
    val title = email_id.split("@")[0]

    val viewModel: MedicineViewModel = viewModel()


    for (a in 0 until b)
    {
        // Log.d("Firestore", "test89: " + a +"*" + daftar_obat[a])

        aturan_obat = daftar_obat[a]!!.split("-").toList()
        obat[a]=aturan_obat[0];
        rpt[a]=aturan_obat[1];
        mkn[a]=aturan_obat[2];
        max_minum[a]=aturan_obat[3];
        tot_minum[a]=aturan_obat[4];
        sisa[a]=max_minum[a].toInt()-tot_minum[a].toInt();
        fvalue=periode+"@"+obat[a].toString()
        //fvalue="2024-12-25@FLU"

        // Log.d("Firestore", "test89:$fvalue")

        total=viewModel.ReadFirestoreField(androidId,fvalue)
        total_daily[a]=total.toIntOrNull() ?: 0
        if(total_daily[a]<=2)
        {
            //Log.d("Firestore", "test89:$fvalue = " + total_daily[a])
        }


    }

    Log.d("Firestore", "test89:============ ")

    for (a in 0 until b)
    {
        Log.d("Firestore", "test89: " + a +"=" + total_daily[a])

    }


    val scrollState2 = rememberScrollState()



    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(scrollState2),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp)) // "Margin" atas
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd // Menyelaraskan konten ke kanan
        ) {

        }

        Spacer(modifier = Modifier.height(10.dp)) // "Margin" atas
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HeaderCell(" ",Color.White)
            HeaderCell("Pagi",Color.Blue)
            HeaderCell("Siang",Color.White)
            HeaderCell("Malam",Color.Red)

        }

        // Baris Pagi
        var c=a
        for (c in 0 until b) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                DataCell(obat[c], Color.LightGray)
                if (rpt[c].toInt() >= 1 && sisa[c] > 0 && total_daily[c] == 0) {
                    ButtonCell(mkn[c], androidId,obat[c], Color.Green)
                } else {
                    ButtonCell("", androidId,obat[c], Color.White)
                }

                if (rpt[c].toInt() >= 3 && sisa[c] > 1 && total_daily[c] <=1) {
                    ButtonCell(mkn[c], androidId,obat[c], Color.Green)
                } else {
                    ButtonCell("", androidId,obat[c], Color.White)
                }

                if ((rpt[c].toInt() >= 3 && sisa[c] > 0 && total_daily[c] <= 2) || (rpt[c].toInt() == 2 && sisa[c] > 0 && total_daily[c] <= 1)) {
                    ButtonCell(mkn[c],androidId, obat[c], Color.Green)
                } else {
                    ButtonCell("",androidId, obat[c], Color.White)
                }

            }
        }


        //=========================

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Tombol SAVE
            Button(
                onClick = {

                    Log.d("Firestore-cfg", "$androidId====")
                    restartApp(context)
                    //isFormVisible = true
                },
                modifier = Modifier
                    .padding(8.dp)
                    .width(150.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black),
                shape = RoundedCornerShape(20.dp)

            ) {
                androidx.compose.material3.Text("Refresh", color = Color.White)
            }

            Button(
                onClick = {

                    Log.d("Firestore-cfg", "$androidId====")
                    //restartApp(context)
                    isFormVisible = true
                },
                modifier = Modifier
                    .padding(0.dp)
                    .width(150.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black),
                shape = RoundedCornerShape(20.dp)

            ) {
                androidx.compose.material3.Text("Setting", color = Color.White)
            }
        }



        if (isFormVisible) {
            // Log.d("Firestore", "master885: " +  daftarku)
            FormApp(daftarku, androidId)
        }



    }
}

@Composable
fun HeaderCell(text: String,kolor:Color) {
    var warna by remember { mutableStateOf(kolor) }
    var gbr by remember { mutableStateOf(R.drawable.trans) }
    if(text=="")
    {
        warna=Color.White
        gbr=R.drawable.trans
    }
    else
    {
        warna=Color.White
        if(text=="Pagi")
        {
            gbr=R.drawable.pagi
        }
        if(text=="Siang")
        {
            gbr=R.drawable.siang
        }
        if(text=="Malam")
        {
            gbr=R.drawable.malam
        }

    }

    //warna=Color.White
    Box(
        modifier = Modifier
            .width(80.dp)
            .background(warna)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        if(text!="") {
            Image(
                painter = painterResource(id = gbr),
                contentDescription = "Icon PNG",
                modifier = Modifier.size(52.dp) // Ukuran ikon 32x32 dp
            )
        }
        androidx.compose.material3.Text(
            text = "",
            color = Color.White,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun DataCell(text: String, kolor: Color) {
    Box(
        modifier = Modifier
            .width(70.dp)
            .height(45.dp)
            .clip(RoundedCornerShape(46.dp))
            .background(kolor)
            .padding(8.dp),
        contentAlignment = Alignment.Center,


        ) {
        androidx.compose.material3.Text(text = text, fontSize = 14.sp, textAlign = TextAlign.Center)
    }
}

@Composable
fun ButtonCell(text: String,androidId:String,obat_id:String,kolor: Color) {
    var warna by remember { mutableStateOf(kolor) }
    var gbr by remember { mutableStateOf(R.drawable.trans) }
    val viewModel: MedicineViewModel = viewModel()
    if(text=="")
    {
        //warna=Color.White
        gbr=R.drawable.trans
    }
    else
    {

        if(text=="A")
        {
            gbr=R.drawable.makan
        }
        if(text=="B")
        {
            gbr= R.drawable.no_makan
        }
        if(text=="C")
        {
            gbr=R.drawable.trans
        }

    }
    Button(
        onClick = {
            if(kolor==Color.Green)
            {
                warna=Color.White
                val hari_ini=getCurrentDate();
                viewModel.simpan_data(obat_id,androidId,"days_status",hari_ini)
            }
            Log.d("Firestore", "firesk:============ ")

        },
        modifier = Modifier
            .padding(8.dp)
            .width(70.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor  = warna)
    ) {
        //Text(text = "\uD83C\uDF57", fontSize = 34.sp, color=Color.Red,textAlign = TextAlign.Center)
        Image(
            painter = painterResource(id =gbr),
            contentDescription = "Icon PNG",
            modifier = Modifier
                // .size(42.dp) // Ukuran ikon 32x32 dp
                .fillMaxSize()
        )

    }
}



//============================================================

@Composable
fun FormApp(obat_cfg:String,androidId:String) {
    val context = LocalContext.current
    var daft_obat:String=obat_cfg
    if(daft_obat=="")
    {
        daft_obat="FLU-3-B-10-0#BIO-2-A-10-0#BTK-3-B-10-0"
    }

    var cfg_obat = remember { mutableStateListOf<String>(daft_obat) }



    // Log.d("Firestore", "master886:*" + daft_obat)

    var obat_arry=daft_obat.split("#")

    var obb=cfg_obat.toString()
    var aturan_obat = obb!!.split("#").toList()
    var obat= daft_obat.split("#")
    //var obat = Array(10){""}
    var a=0;
    var b=obat_arry.size



    // var cod = remember { MutableList(10) { mutableStateOf("") } }
    // var cfg = remember { MutableList(10) { mutableStateOf("") } }



    var kode_obat = Array(10){""}
    var aturan_minum = Array(10){""}

    for (a in 0 until b) {
        obat= obat_arry[a].split("-")
        kode_obat[a]=obat[0]
        aturan_minum[a]=obat[1] + "-" + obat[2] + "-" + obat[3]+"-"+ obat[4]
        //  Log.d("Firestore-cfg", "master881 : " + cfg[a].value)
    }


    var kodeObat = remember { mutableStateListOf(*kode_obat) }
    var aturanminum = remember { mutableStateListOf(*aturan_minum) }

    var newkodeObat = remember { mutableStateListOf("") }
    var newaturanminum = remember { mutableStateListOf("") }

    var data_obat =""
    //val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            //    .fillMaxSize()
            //  .width(50.dp)
            // .verticalScroll(scrollState)
            .padding(6.dp),
        //  .alpha(0f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header Tabel
        Spacer(modifier = Modifier.height(20.dp)) // "Margin" atas
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Blue),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .width(35.dp)
                    .background(Color.Blue)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.material3.Text(
                    text = "",
                    color = Color.White,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }


            Box(
                modifier = Modifier
                    .width(140.dp)
                    .background(Color.Blue)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.material3.Text(
                    text = "Code Obat",
                    color = Color.White,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }


            Box(
                modifier = Modifier
                    .width(225.dp)
                    .background(Color.Blue)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.material3.Text(
                    text = "Aturan Makan",
                    color = Color.White,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))


        for (a in 0 until b) {
            RowInput(
                no = (a+1).toString(),
                codeObat = kodeObat[a],
                total = aturanminum[a],
                onCodeChange = { newValue ->
                    kodeObat[a] = newValue // Perbarui nilai
                    Log.d("Firestore", "master889:*" + newValue)

                },

                onTotalChange = { aturanminum[a] = it }


            )
        }

        a=a+1;



        // Baris 3
        RowInput(
            no = "",
            codeObat = newkodeObat[0],
            total = newaturanminum[0],
            onCodeChange = {
                Log.d("Firestore", "master889:*" + it)
                newkodeObat[0] = it
            },
            onTotalChange = {newaturanminum[0] = it }

        )

        Spacer(modifier = Modifier.height(16.dp))

        // Tombol SAVE
        Button(
            onClick = {

                Log.d("Firestore-cfg", "Start")

                for (a in 0 until b)
                {
                    Log.d("Firestore-cfg", "Start : " +kodeObat[a])
                    if(kodeObat[a]!="" && aturanminum[a]!="")
                    {
                        if(data_obat=="")
                        {
                            data_obat=kodeObat[a] + "-" + aturanminum[a]
                        }
                        else
                        {
                            data_obat=data_obat + "#" + kodeObat[a] + "-" + aturanminum[a]
                        }
                    }

                }

                if(newkodeObat[0]!="" && newaturanminum[0]!="")
                {
                    if(data_obat=="")
                    {
                        data_obat=newkodeObat[0] + "-" + newaturanminum[0]
                    }
                    else
                    {
                        data_obat=data_obat + "#" + newkodeObat[0] + "-" + newaturanminum[0]
                    }
                }

                Log.d("Firestore-cfg", "daftar_obat : " +data_obat)


                if(data_obat!="")
                {

                    val dbx = FirebaseFirestore.getInstance()
                    val fvalue="daftar_obat"
                    val userDocRef = dbx.collection("user").document(androidId)
                    val updateData: Map<String, Any>  = hashMapOf(fvalue to data_obat) // Pastikan format benar

                    userDocRef.update(updateData)
                        .addOnSuccessListener {
                            Log.d("Firestore-cfg", "Firestore-cfg: $fvalue berhasil diupdate menjadi $data_obat")
                        }
                        .addOnFailureListener { e ->
                            Log.e("Firestore-cfg", "Firestore-cfg: Gagal mengupdate field $fvalue: ${e.message}")
                        }

                    //restartApp(context)
                }
                Log.d("Firestore-cfg", "$androidId====$data_obat")


            },
            modifier = Modifier
                .padding(8.dp)
                .width(100.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor  = Color.Black),
            shape = RoundedCornerShape(20.dp)

        ) {
            androidx.compose.material3.Text("SAVE", color = Color.White)
        }
    }

}

@Composable
fun RowInput(
    no: String,
    codeObat: String,
    total: String,
    onCodeChange: (String) -> Unit,
    onTotalChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        androidx.compose.material3.Text(no, fontSize = 16.sp, modifier = Modifier.weight(0.2f))
        androidx.compose.material.TextField(
            value = codeObat,
            onValueChange = onCodeChange,
            modifier = Modifier
                .weight(1f)
                .height(50.dp)
                .width(110.dp)
                .padding(horizontal = 4.dp),
            //  placeholder = { Text("FLU") }
        )
        androidx.compose.material.TextField(
            value = total,
            onValueChange = onTotalChange,
            modifier = Modifier
                .weight(1f)
                .height(50.dp)
                .width(200.dp)
                .padding(horizontal = 4.dp),
            // placeholder = { Text("3-B-10") }
        )

    }
}


//=================hapus=======================
fun deleteFieldIfDocumentExists(collectionName: String, documentId: String, fieldName: String) {
    val db = FirebaseFirestore.getInstance()

    db.collection(collectionName).document(documentId)
        .get()
        .addOnSuccessListener { document ->
            if (document.exists()) {
                db.collection(collectionName).document(documentId)
                    .update(fieldName, FieldValue.delete())
                    .addOnSuccessListener {
                        println("Field '$fieldName' berhasil dihapus!")
                    }
                    .addOnFailureListener { e ->
                        println("Gagal menghapus field '$fieldName': ${e.message}")
                    }
            } else {
                println("Dokumen tidak ditemukan.")
            }
        }
        .addOnFailureListener { e ->
            println("Gagal memeriksa dokumen: ${e.message}")
        }
}

//===============refresh========================
fun restartApp(context: Context) {
    Log.d("Firestore-cfg", "restart-99")
    val intent = Intent(context, MainActivity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(intent)
    if (context is Activity) {
        context.finish() // Menutup aktivitas saat ini
    }
}