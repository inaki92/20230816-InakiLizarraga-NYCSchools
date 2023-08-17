package com.inaki.a20230816_inakilizarraga_nycschools.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.inaki.a20230816_inakilizarraga_nycschools.model.schools.School
import com.inaki.a20230816_inakilizarraga_nycschools.utils.NavDir
import com.inaki.a20230816_inakilizarraga_nycschools.utils.State
import com.inaki.a20230816_inakilizarraga_nycschools.viewmodel.MainViewModel

@Composable
fun SchoolList(
    viewModel: MainViewModel,
    navController: NavController
) {
    val state by viewModel.schools.collectAsState()
    val error by viewModel.showDialog.collectAsState()

    when(val payload = state) {
        is State.LOADING -> {
            LoadingScreen()
        }
        is State.SUCCESS -> {
            Schools(schools = payload.result) {
                viewModel.selectedSchool = it
                viewModel.schoolScore()
                navController.navigate(NavDir.Details().route)
            }
        }
        is State.ERROR -> {
            viewModel.onOpenErrorDialog()
            ErrorDialog(
                errorMessage = payload.error.message,
                show = error,
                onDismiss = { viewModel.onDialogDismiss() },
                onConfirm = { viewModel.onDialogRetry() }
            )
        }
    }


}

@Composable
fun Schools(
    schools: List<School>,
    itemCLicked: (School) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .padding(top = 48.dp),
        verticalArrangement = Arrangement.spacedBy(9.dp)
    ) {
        items(items = schools) {
            SchoolItem(it, false, itemCLicked)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SchoolItem(
    school: School,
    isDetails: Boolean = false,
    itemCLicked: (School) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(10.dp),
        onClick = { itemCLicked(school) }
    ) {
        Text(
            text = school.schoolName ?: "",
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(textAlign = TextAlign.Center, text = school.phoneNumber ?: "")
        Spacer(modifier = Modifier.height(10.dp))
        Text(textAlign = TextAlign.Center, text = school.website ?: "")
        Spacer(modifier = Modifier.height(10.dp))
        Text(textAlign = TextAlign.Center, text = school.location ?: "")

        if (isDetails) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(textAlign = TextAlign.Justify, text = school.overviewParagraph ?: "")
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(androidx.compose.ui.graphics.Color.White)
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorDialog(
    errorMessage: String?,
    show: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if (show) {
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = onConfirm)
                { Text(text = "Retry") }
            },
            dismissButton = {
                TextButton(onClick = onDismiss)
                { Text(text = "Cancel") }
            },
            title = { Text(text = "Error has occurred") },
            text = { Text(text = errorMessage ?: "Unknown error") }
        )
    }
}