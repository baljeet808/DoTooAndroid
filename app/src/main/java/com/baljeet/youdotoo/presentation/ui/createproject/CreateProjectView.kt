package com.baljeet.youdotoo.presentation.ui.createproject

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.baljeet.youdotoo.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.theme.DotooBlue
import com.baljeet.youdotoo.presentation.ui.theme.DotooPink
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun createProjectView(
    modifier: Modifier,
    sheetState : SheetState
) {

    val viewModel = viewModel<CreateProjectViewModel>()

    LaunchedEffect(key1 = true){
        viewModel.projectState.collectLatest {state->
            if(state.success){
                sheetState.hide()
            }else{
                //report bug
            }
        }
    }


    var projectName by remember {
        mutableStateOf("")
    }
    var description by remember {
        mutableStateOf("")
    }
    val scope = rememberCoroutineScope()
    Box(modifier = modifier){

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {

            Text(
                text = "Create new list",
                fontFamily = FontFamily(Nunito.Bold.font),
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 22.sp,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = projectName,
                onValueChange = { projectName  = it} ,
                label = {
                    Text(
                        text = "Give it a name",
                        color = MaterialTheme.colorScheme.secondary
                        )
                },
                maxLines = 1,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = description,
                onValueChange = { description  = it} ,
                label = {
                    Text(
                        text = "What this list will be about? (Optional)",
                        color = MaterialTheme.colorScheme.secondary,
                        )
                },
                maxLines = 5,
                singleLine = false,
                minLines = 3,
                modifier = Modifier.fillMaxWidth()
            )


            Button(
                onClick = {
                    //do something here
                    scope.launch {
                        viewModel.createProject(projectName, description)
                    }
                },
                colors = ButtonDefaults
                    .buttonColors(containerColor = if(isSystemInDarkTheme()){
                        DotooPink
                    } else
                            DotooBlue
                    ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, start = 10.dp, end = 10.dp),
                shape = RoundedCornerShape(4.dp)

            ) {
                Text(
                    text="Create",
                    color = MaterialTheme.colorScheme.background,
                    fontFamily = FontFamily(Nunito.Bold.font),
                    fontSize = 18.sp
                )
            }


        }

    }


}