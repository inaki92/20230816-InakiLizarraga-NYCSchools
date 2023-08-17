package com.inaki.a20230816_inakilizarraga_nycschools.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.inaki.a20230816_inakilizarraga_nycschools.model.schools.School
import com.inaki.a20230816_inakilizarraga_nycschools.model.scores.Score
import com.inaki.a20230816_inakilizarraga_nycschools.utils.State
import com.inaki.a20230816_inakilizarraga_nycschools.viewmodel.MainViewModel

@Composable
fun DetailsScreen(
    viewModel: MainViewModel
) {
    val score by viewModel.score.collectAsState()
    val school = viewModel.selectedSchool

    when(val current = score) {
        State.LOADING -> LoadingScreen()
        is State.SUCCESS -> {
            SchoolScoreDetails(current.result.firstOrNull(), school)
        }
        is State.ERROR ->  SchoolScoreDetails(score = null, school)
    }
}

@Composable
fun SchoolScoreDetails(score: Score?, school: School?) {
    Column {
        Box(modifier = Modifier
            .fillMaxHeight()
        ) {
            school?.let {
                SchoolItem(
                    school = school,
                    isDetails = true,
                    itemCLicked = {}
                )
            }
        }

        Box(
            modifier = Modifier
                .weight(0.4f)
                .aspectRatio(1f)
        ) {

            Card(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(10.dp)
            ){
                Text(text = "SAT Scores", textAlign = TextAlign.Center)

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    modifier = Modifier.padding(top = 8.dp, start = 48.dp, bottom = 4.dp),
                    text = "Math Score: ${score?.satMathAvgScore ?: "---"}",
                    textAlign = TextAlign.Center
                )
                Text(
                    modifier = Modifier.padding(top = 8.dp, start = 48.dp, bottom = 4.dp),
                    text = "Reading Score: ${score?.satCriticalReadingAvgScore ?: "---"}"
                )
                Text(
                    modifier = Modifier.padding(top = 8.dp, start = 48.dp, bottom = 4.dp),
                    text = "Writing Score: ${score?.satWritingAvgScore ?: "---"}"
                )
            }
        }
    }
}