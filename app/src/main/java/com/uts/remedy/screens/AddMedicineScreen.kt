package com.uts.remedy.screens

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun AddMedicineScreen(
    viewModel: MedicineViewModel = viewModel(),
    medicineId: String? = null,
    navController: NavController? = null
) {
    var name by remember { mutableStateOf("") }
    var dosage by remember { mutableStateOf("") }
    var perDay by remember { mutableIntStateOf(1) }
    var timingInstruction by remember { mutableStateOf("Before Meals") }
    var completion by remember { mutableStateOf("No Need To Finish") }
    var dateTimes by remember { mutableLongStateOf(0) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var description by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var frequency by remember { mutableStateOf("Twice") }

    var frequencyDropdownExpanded by remember { mutableStateOf(false) }
    var timingInstructionDropdownExpanded by remember { mutableStateOf(false) }
    var completionDropdownExpanded by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                imageUri = uri
            }
        }
    }

    LaunchedEffect(medicineId) {
        medicineId?.let {
            val medicine = viewModel.getMedicineById(it)
            if (medicine != null) {
                name = medicine.name
                dosage = medicine.dosage
                perDay = medicine.perDay
                timingInstruction = medicine.timingInstruction
                completion = medicine.completion
                dateTimes = medicine.dateTimes
                imageUri = medicine.image.toUri()
                description = medicine.description
                type = medicine.type
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(bottom = 200.dp, top = 24.dp) // Adjust for TabView height
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Name
            Column {
                BasicTextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color.Gray)
                        .padding(8.dp)
                )
                Text("Medicine name", color = Color.Gray)
            }

            // Dose Amount
            Column {
                BasicTextField(
                    value = dosage,
                    onValueChange = { dosage = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color.Gray)
                        .padding(8.dp)
                )
                Text("Dose Amount", color = Color.Gray)
            }

            // Frequency
            Column {
                Text("Frequency", color = Color.Gray)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Amount")
                    Spacer(modifier = Modifier.weight(1f))
                    Spacer(modifier = Modifier.width(8.dp))
                    Box {
                        Text(
                            text = frequency,
                            modifier = Modifier
                                .clickable { frequencyDropdownExpanded = true }
                                .border(1.dp, Color.Gray)
                                .padding(8.dp)
                        )
                        DropdownMenu(
                            expanded = frequencyDropdownExpanded,
                            onDismissRequest = { frequencyDropdownExpanded = false },
                            content = {
                                DropdownMenuItem(
                                    onClick = {
                                        frequency = "Once"
                                        frequencyDropdownExpanded = false
                                    },
                                    text = { Text("Once") }
                                )
                                DropdownMenuItem(
                                    onClick = {
                                        frequency = "Twice"
                                        frequencyDropdownExpanded = false
                                    },
                                    text = { Text("Twice") }
                                )
                                DropdownMenuItem(
                                    onClick = {
                                        frequency = "Three Times"
                                        frequencyDropdownExpanded = false
                                    },
                                    text = { Text("Three Times") }
                                )
                            }
                        )
                    }
                }
            }

            // Timing Instructions
            Column {
                Text("Timing Instructions", color = Color.Gray)
                Box {
                    Text(
                        text = timingInstruction,
                        modifier = Modifier
                            .clickable { timingInstructionDropdownExpanded = true }
                            .border(1.dp, Color.Gray)
                            .padding(8.dp)
                    )
                    DropdownMenu(
                        expanded = timingInstructionDropdownExpanded,
                        onDismissRequest = { timingInstructionDropdownExpanded = false },
                        content = {
                            DropdownMenuItem(
                                text = { Text("Before Meals") },
                                onClick = {
                                    timingInstruction = "Before Meals"
                                    timingInstructionDropdownExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("After Meals") },
                                onClick = {
                                    timingInstruction = "After Meals"
                                    timingInstructionDropdownExpanded = false
                                }
                            )
                        }
                    )
                }
            }

            // Completion
            Column {
                Text("Completion", color = Color.Gray)
                Box {
                    Text(
                        text = completion,
                        modifier = Modifier
                            .clickable { completionDropdownExpanded = true }
                            .border(1.dp, Color.Gray)
                            .padding(8.dp)
                    )
                    DropdownMenu(
                        expanded = completionDropdownExpanded,
                        onDismissRequest = { completionDropdownExpanded = false },
                        content = {
                            DropdownMenuItem(
                                text = { Text("No Need To Finish") },
                                onClick = {
                                    completion = "No Need To Finish"
                                    completionDropdownExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Finish By...") },
                                onClick = {
                                    completion = "Finish By..."
                                    completionDropdownExpanded = false
                                }
                            )
                        }
                    )
                }
            }

            // Time Selection
            Text("Select Time", color = Color.Gray)
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {


                val times = listOf("06", "12", "18")
                val buttonColors = remember { mutableStateListOf(*Array(times.size) { Color.Gray }) }

                times.chunked(3).forEach { rowTimes ->
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        rowTimes.forEach { time ->
                            val index = times.indexOf(time)  // Ambil indeks tombol
                           Button(onClick = {
                               // Toggle warna tombol menjadi hijau atau kembali ke abu-abu
                               buttonColors[index] = if (buttonColors[index] == Color.Green) {
                                   Color.Gray
                               } else {
                                   Color.Green
                               }

                            },
                               colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                               containerColor = buttonColors[index]
                            ),
                                modifier = Modifier
                                    .padding(8.dp)
                                    .width(70.dp),
                            )
                              {
                                Text(time)
                               }
                        }
                    }
                }
            }

            Text("*Please enter the time before proceeding", color = Color.Red)

            // Image Selection
            Column {
                Text("Image", color = Color.Gray)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                            launcher.launch(intent)
                        }
                        .border(1.dp, Color.Gray)
                        .padding(8.dp)
                ) {
                    imageUri?.let {
                        Image(
                            painter = rememberImagePainter(it),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentScale = ContentScale.Crop
                        )
                    } ?: Text("Select Image", color = Color.Gray)
                }
            }

            // Description
            Column {
                BasicTextField(
                    value = description,
                    onValueChange = { description = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color.Gray)
                        .padding(8.dp)
                )
                Text("Description", color = Color.Gray)
            }

            // Type
            Column {
                BasicTextField(
                    value = type,
                    onValueChange = { type = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color.Gray)
                        .padding(8.dp)
                )
                Text("Type", color = Color.Gray)
            }

            // Add Medicine Track Button
            Button(
                onClick = {
                    if (name.isBlank()) {
                        Toast.makeText(context, "Please enter medicine name", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    if(frequency=="Once")
                    {
                        perDay=1
                    }
                    if(frequency=="Twice")
                    {
                        perDay=2
                    }
                    if(frequency=="Three Times")
                    {
                        perDay=3
                    }
                    val newMedicine = Medicine(
                        id = medicineId ?: "", // ID will be set by Firestore
                        name = name,
                        dosage = dosage,
                        perDay = perDay,
                        timingInstruction = timingInstruction,
                        dateTimes = System.currentTimeMillis(), // Add current timestamp
                        completion = completion,
                        image = imageUri.toString(),
                        description = description,
                        type = type
                    )

                    viewModel.addMedicine(newMedicine)
                    Toast.makeText(context, "Medicine added successfully", Toast.LENGTH_SHORT).show()
                    viewModel.fetchUsersFromFirestore()
                    navController?.navigate("home")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Medicine Track")
            }
        }
    }
}

