package com.uts.remedy.models

import android.provider.Settings
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Data classes for the app
data class User(
    var id: String,
    val name: String,
    val email: String,
)
data class Medicine(
    var id: String,
    val name: String,
    val dosage: String,
    val perDay: Int,
    val timingInstruction: String,
    val dateTimes: Long,
    val completion: String,
    val image: String,
    val description: String,
    val type: String,
    val userId: String? = null
)
data class DosageRecord(
    var id: String,
    val medicineId: String,
    val dosageTaken: String,
    val timeTaken: Long,
)
data class Reminder(
    var id: String,
    val medicineId: String,
    val reminderTime: Long,
    val frequency: String,
)

class MedicineViewModel() : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    // StateFlow to hold data
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> get() = _user

    private val _medicines = MutableStateFlow<List<Medicine>>(emptyList())
    val medicines: StateFlow<List<Medicine>> get() = _medicines

    private val _dosageRecords = MutableStateFlow<List<DosageRecord>>(emptyList())
    val dosageRecords: StateFlow<List<DosageRecord>> get() = _dosageRecords

    private val _reminders = MutableStateFlow<List<Reminder>>(emptyList())
    val reminders: StateFlow<List<Reminder>> get() = _reminders

    init {
        fetchUserData()
        fetchMedicines()
        fetchDosageRecords()
        fetchReminders()
    }

    private fun fetchUserData() {
        val currentUser = auth.currentUser
        currentUser?.let {
            _user.value = User(it.uid, it.displayName ?: "", it.email ?: "")
        }
    }

    fun getMedicineById(medicineId: String): Medicine? {
        return medicines.value.find { it.id == medicineId }
    }

    private fun fetchMedicines() {
        val userId = auth.currentUser?.uid ?: return
        Log.i("MedicineViewModel", "Fetching medicines for user ID: $userId")
        firestore.collection("medicines")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { documents ->
                val medicineList = documents.map { document ->
                    val dateTimesList = document.get("dateTimes") as? List<*>
                    val dateTime = dateTimesList?.firstOrNull() as? Long ?: System.currentTimeMillis()
                    
                    Medicine(
                        id = document.id,
                        name = document.getString("name") ?: "",
                        dosage = document.getString("dosage") ?: "",
                        perDay = document.getLong("perDay")?.toInt() ?: 0,
                        timingInstruction = document.getString("timingInstruction") ?: "",
                        dateTimes = dateTime,
                        completion = document.getString("completion") ?: "",
                        image = document.getString("image") ?: "",
                        description = document.getString("description") ?: "",
                        type = document.getString("type") ?: "",
                        userId = document.getString("userId") ?: ""
                    )
                }
                _medicines.value = medicineList
            }
    }

    private fun fetchDosageRecords() {
        val userId = auth.currentUser?.uid ?: return
        firestore.collection("dosageRecords")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { documents ->
                val dosageList = documents.map { document ->
                    DosageRecord(
                        id = document.id,
                        medicineId = document.getString("medicineId") ?: "",
                        dosageTaken = document.getString("dosageTaken") ?: "",
                        timeTaken = document.getDate("timeTaken")?.time ?: 0
                    )
                }
                _dosageRecords.value = dosageList
            }
    }

    private fun fetchReminders() {
        val userId = auth.currentUser?.uid ?: return
        firestore.collection("reminders")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { documents ->
                val reminderList = documents.map { document ->
                    Reminder(
                        id = document.id,
                        medicineId = document.getString("medicineId") ?: "",
                        reminderTime = document.getDate("reminderTime")?.time ?: 0,
                        frequency = document.getString("frequency") ?: ""
                    )
                }
                _reminders.value = reminderList
            }
    }

    // Add methods to add/update/delete medicines, dosage records, and reminders
    fun addMedicine(medicine: Medicine) {
        viewModelScope.launch {
            val userId = auth.currentUser?.uid ?: return@launch
            val medicineData = hashMapOf(
                "name" to medicine.name,
                "dosage" to medicine.dosage,
                "perDay" to medicine.perDay,
                "timingInstruction" to medicine.timingInstruction,
                "dateTimes" to listOf(medicine.dateTimes), // Wrap in a list
                "completion" to medicine.completion,
                "image" to medicine.image,
                "description" to medicine.description,
                "type" to medicine.type,
                "userId" to userId
            )
            firestore.collection("medicines")
                .add(medicineData)
                .addOnSuccessListener { documentReference ->
                    medicine.id = documentReference.id

                }
                .addOnFailureListener { e ->
                    Log.e("MedicineViewModel", "Error adding medicine: $e")
                }

        }
    }



    fun fetchUsersFromFirestore() {
        viewModelScope.launch {
            val user = auth.currentUser
            val userId = user?.uid ?: return@launch
            val email = user?.email ?: "Email not available"
            val firestore = FirebaseFirestore.getInstance()
            firestore.collection("medicines")
                .get()
                .addOnSuccessListener { documents ->
                    val users = documents.map { it.data }
                    var combine=""
                    var list_obat=""
                    //Log.e("Firestore", "data789"+ users.get(1)["name"])
                    val userList = documents.map { document ->
                        document.id to document.data
                    }
                    userList.forEach { (id, data) ->
                        if(data["name"]!=null && data["userId"]==userId)
                        {
                            var ti="C"
                            if(data["timingInstruction"]=="After Meals")
                            {
                                ti="A"
                            }
                            if(data["timingInstruction"]=="Before Meals")
                            {
                                ti="B"
                            }

                            var perday=0
                            if(data["perDay"]==0)
                            {
                                perday=1
                            }
                            if(data["perDay"]==1)
                            {
                                perday=2
                            }
                            if(data["perDay"]==2)
                            {
                                perday=3
                            }
                             combine= data["name"].toString() + "-"  + data["perDay"] + "-" + ti + "-" + data["dosage"] + "-0"
                            if(list_obat=="")
                            {
                                list_obat=combine
                            }
                            else
                            {
                                list_obat=list_obat + "#" +  combine
                            }
                            Log.d("data789", "$email # $userId = ID: $id, format: $combine")
                            check_userid(email)

                        }

                    }
                    //update_obat(email,data["name"].toString())
                    update_firebase(email,"daftar_obat",list_obat)
                }
                .addOnFailureListener { exception ->
                    Log.e("Firestore", "Error fetching users", exception)
                }
        }
    }




    fun check_userid(androidId:String)
    {
        val db = FirebaseFirestore.getInstance()

        Log.d("Firestore", "test-cfg:  berhasil diupdate")

        val documentRef = db.collection("user").document(androidId)

        documentRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    //==================ada===========
                    //update_firebase(androidId,"email",email)
                    Log.d("Firestore", "test-cfg:  berhasil diupdate")
                } else {

                    val db = FirebaseFirestore.getInstance()
                    db.collection("user")
                        .document(androidId)
                        .set(emptyMap<String, Any>())
                        .addOnSuccessListener {
                            println("Dokumen 'book' berhasil dibuat!")
                           // update_firebase(androidId,"email",email)
                        }
                        .addOnFailureListener { e ->
                            println("Gagal membuat dokumen: ${e.message}")
                        }
                }
            }
            .addOnFailureListener { e ->
                println("Gagal membuat dokumen: ${e.message}")
            }
    }


    fun update_firebase(document_name:String,file_name:String,file_content:String)
    {
        val dbx = FirebaseFirestore.getInstance()
        val userDocRef = dbx.collection("user").document(document_name)
        val updateData: Map<String, Any>  = hashMapOf(file_name to file_content) // Pastikan format benar

        userDocRef.update(updateData)
            .addOnSuccessListener {
                Log.d("Firestore", "test-cfg: $file_name berhasil diupdate $file_content")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "test-cfg: Gagal mengupdate field $file_name: ${e.message}")
            }
    }




    fun baca_data(androidId:String, field_name:String,callback: (String) -> Unit)
    {

        // Inisialisasi Firestore
        val db = FirebaseFirestore.getInstance()

// Referensi ke dokumen
        val userDocRef = db.collection("user").document(androidId)

// Membaca field TANGGAL
        userDocRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists() && field_name!="" && field_name!=null) {
                    val field_value = document.getString(field_name)// Membaca nilai field TANGGAL

                    if(field_value!=null)
                    {
                        Log.d("Firestore", "Ditemukan $field_value")
                        callback(field_value)
                    }
                    else
                    {
                        Log.d("Firestore", "Ditemukan kosong $field_value")
                        callback("")
                    }

                } else {
                    Log.d("Firestore", "Tidak di temukan $field_name")
                    callback("")
                }
            }
            .addOnFailureListener { e ->
                Log.d("Firestore", "proses gagal ${e.message}")
            }
        Log.d("Firestore", "Status $callback")
    }



    @Composable
    fun ReadFirestoreField(username:String, para_name:String): String {
        var nama by remember { mutableStateOf("") }

        // Referensi ke Firestore
        val db = FirebaseFirestore.getInstance()

        // Membaca data
        LaunchedEffect(Unit) {
            db.collection("user")
                .document(username)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        nama = document.getString(para_name).toString() ?: "Tidak ada nama"
                    } else {
                        nama = "Dokumen tidak ditemukan"
                    }
                }
                .addOnFailureListener {
                    nama = "Gagal membaca data: ${it.message}"
                }
        }

        // Tampilkan data di UI
        // androidx.compose.material.Text(text = "Nama: $nama")
        return nama
    }



    fun simpan_data(obat_id:String,androidId:String,Field_name:String,Field_value:String)
    {
        // Inisialisasi Firestore
        val dbx = FirebaseFirestore.getInstance()
        val fvalue=Field_value+"@"+obat_id
// Referensi ke dokumen
        val userDocRef = dbx.collection("user").document(androidId)
        Log.d("Firestore", "test-cfg: $androidId to $obat_id")
        update_obat(androidId,obat_id)


        baca_data(androidId,fvalue )
        { hasil ->
            Log.d("Firestore", "test-cfg: $androidId hasil: $hasil?")
            if(hasil.trim() != "")
            {
                val jumlah =(hasil.toInt() + 1).toString()

                Log.d("Firestore", "test-cfg: ktk $fvalue = $jumlah")

                val updateData: Map<String, Any>  = hashMapOf(fvalue to jumlah) // Pastikan format benar

                userDocRef.update(updateData)
                    .addOnSuccessListener {
                        Log.d("Firestore", "Field $fvalue berhasil diupdate menjadi $jumlah")
                    }
                    .addOnFailureListener { e ->
                        Log.e("Firestore", "Gagal mengupdate field $fvalue: ${e.message}")
                    }
            }
            else
            {
                val updateData = hashMapOf(fvalue to "1")
                userDocRef.update(updateData as Map<String, Any>)
                    .addOnSuccessListener {
                        Log.d("Firestore", "Proses: $hasil")
                    }
                    .addOnFailureListener { e ->
                        Log.d("Firestore", "Proses: ${e.message}")
                    }
            }

        }


    }



    fun update_obat(androidId:String,code_obat:String)
    {
        var nama: String? = "OBAT1-3-A#OBAT2-2-B"
        var daftar_obat = nama!!.split("#").toList()


        baca_data(androidId,"daftar_obat" )
        { nama ->
            daftar_obat = nama!!.split("#").toList()

            var aturan_obat = "A-1-2-B"!!.split("-").toList()
            var obat = ""
            var rpt = ""
            var mkn = ""
            var max_minum = 0
            var tot_minum = 0
            var a = 0;
            var b = daftar_obat.size;
            var obat_cfg = ""
            var obat_cfg_all = ""
            for (a in 0 until b) {
                // Log.d("Firestore", "test89: " + a +"*" + daftar_obat[a])

                aturan_obat = daftar_obat[a]!!.split("-").toList()
                obat_cfg = daftar_obat[a];
                obat = aturan_obat[0];
                if (obat == code_obat) {
                    rpt = aturan_obat[1];
                    mkn = aturan_obat[2];
                    max_minum = aturan_obat[3].toInt();
                    tot_minum = aturan_obat[4].toInt() + 1;
                    obat_cfg =aturan_obat[0] + "-" + aturan_obat[1] + "-" + aturan_obat[2] + "-" + aturan_obat[3] + "-" + tot_minum
                }
                if (obat_cfg_all == "") {
                    obat_cfg_all = obat_cfg
                } else {
                    obat_cfg_all = obat_cfg_all + "#" + obat_cfg
                }
            }
            Log.d("Firestore", "test-cfg: " + obat_cfg_all)
            val dbx = FirebaseFirestore.getInstance()
            val fvalue="daftar_obat"
            val userDocRef = dbx.collection("user").document(androidId)
            val updateData: Map<String, Any>  = hashMapOf(fvalue to obat_cfg_all) // Pastikan format benar

            userDocRef.update(updateData)
                .addOnSuccessListener {
                    Log.d("Firestore", "test-cfg: $fvalue berhasil diupdate menjadi $obat_cfg_all")
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "test-cfg: Gagal mengupdate field $fvalue: ${e.message}")
                }
        }
    }



    fun updateMedicine(medicine: Medicine) {
        viewModelScope.launch {
            val userId = auth.currentUser?.uid ?: return@launch
            val medicineData = hashMapOf(
                "name" to medicine.name,
                "dosage" to medicine.dosage,
                "perDay" to medicine.perDay,
                "timingInstruction" to medicine.timingInstruction,
                "dateTimes" to medicine.dateTimes,
                "completion" to medicine.completion,
                "image" to medicine.image,
                "description" to medicine.description,
                "type" to medicine.type,
                "userId" to userId
            )
            firestore.collection("medicines").document(medicine.id)
                .set(medicineData)
                .addOnSuccessListener {
                    fetchMedicines()
                }
                .addOnFailureListener { e ->
                    // Handle the error
                }
        }
    }

    fun deleteMedicine(medicineId: String) {
        viewModelScope.launch {
            firestore.collection("medicines").document(medicineId)
                .delete()
                .addOnSuccessListener {
                    fetchMedicines()
                }
                .addOnFailureListener { e ->
                    // Handle the error
                }
        }
    }

    fun addDosageRecord(dosageRecord: DosageRecord) {
        viewModelScope.launch {
            val userId = auth.currentUser?.uid ?: return@launch
            val dosageRecordData = hashMapOf(
                "medicineId" to dosageRecord.medicineId,
                "dosageTaken" to dosageRecord.dosageTaken,
                "timeTaken" to dosageRecord.timeTaken,
                "userId" to userId
            )
            firestore.collection("dosageRecords")
                .add(dosageRecordData)
                .addOnSuccessListener { documentReference ->
                    dosageRecord.id = documentReference.id
                    fetchDosageRecords()
                }
                .addOnFailureListener { e ->
                    // Handle the error
                }
        }
    }

    fun updateDosageRecord(dosageRecord: DosageRecord) {
        viewModelScope.launch {
            val userId = auth.currentUser?.uid ?: return@launch
            val dosageRecordData = hashMapOf(
                "medicineId" to dosageRecord.medicineId,
                "dosageTaken" to dosageRecord.dosageTaken,
                "timeTaken" to dosageRecord.timeTaken,
                "userId" to userId
            )
            firestore.collection("dosageRecords").document(dosageRecord.id)
                .set(dosageRecordData)
                .addOnSuccessListener {
                    fetchDosageRecords()
                }
                .addOnFailureListener { e ->
                    // Handle the error
                }
        }
    }

    fun deleteDosageRecord(dosageRecordId: String) {
        viewModelScope.launch {
            firestore.collection("dosageRecords").document(dosageRecordId)
                .delete()
                .addOnSuccessListener {
                    fetchDosageRecords()
                }
                .addOnFailureListener { e ->
                    // Handle the error
                }
        }
    }

    fun addReminder(reminder: Reminder) {
        viewModelScope.launch {
            val userId = auth.currentUser?.uid ?: return@launch
            val reminderData = hashMapOf(
                "medicineId" to reminder.medicineId,
                "reminderTime" to reminder.reminderTime,
                "frequency" to reminder.frequency,
                "userId" to userId
            )
            firestore.collection("reminders")
                .add(reminderData)
                .addOnSuccessListener { documentReference ->
                    reminder.id = documentReference.id
                    fetchReminders()
                }
                .addOnFailureListener { e ->
                    // Handle the error
                }
        }
    }

    fun updateReminder(reminder: Reminder) {
        viewModelScope.launch {
            val userId = auth.currentUser?.uid ?: return@launch
            val reminderData = hashMapOf(
                "medicineId" to reminder.medicineId,
                "reminderTime" to reminder.reminderTime,
                "frequency" to reminder.frequency,
                "userId" to userId
            )
            firestore.collection("reminders").document(reminder.id)
                .set(reminderData)
                .addOnSuccessListener {
                    fetchReminders()
                }
                .addOnFailureListener { e ->
                    // Handle the error
                }
        }
    }

    fun deleteReminder(reminderId: String) {
        viewModelScope.launch {
            firestore.collection("reminders").document(reminderId)
                .delete()
                .addOnSuccessListener {
                    fetchReminders()
                }
                .addOnFailureListener { e ->
                    // Handle the error
                }
        }
    }
}
