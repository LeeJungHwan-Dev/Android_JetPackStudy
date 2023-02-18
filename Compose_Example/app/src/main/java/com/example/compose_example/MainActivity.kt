package com.example.compose_example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose_example.ui.theme.Compose_ExampleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Compose_ExampleTheme { // Theme.kt 파일을 일괄 적용하기 위해 사용
                    MyApp(modifier = Modifier.fillMaxSize()) // 상하좌우 maxSize로 설정
                }
            }
        }
}

@Composable
fun MyApp(modifier: Modifier = Modifier){ // 인자값으로 Modifier값을 받는다.

    var shouldShowOnboarding by rememberSaveable {  //rememberSaveable은 LazyColumn이 재사용될 때 state 상태를 잃어버리는 걸 방지하는 함수이다.
        mutableStateOf(true)
    }

    Surface(modifier,color = MaterialTheme.colorScheme.background) {
        if(shouldShowOnboarding){
            OnboardingScreen(onContinueClicked = {shouldShowOnboarding = false}) //OnboardingScreen에 onContinueClicked 람다식에 shouldShowOnboarding = false 전달
        }else{
            Greetings() // 최초 shouldShowOnboarding이 작동하고 false로 바뀌면 이후 Greetings() composable이 호출된다.
        }
    }
}

@Composable
fun Greeting(name : String){
    var expanded by rememberSaveable { mutableStateOf(false) } // LazyColumn의 재사용에 의한 상태 기억
    val extraPadding by animateDpAsState( //생성된 Greeting에 버튼 클릭 애니메이션을 적용하는 함수
        if (expanded) 48.dp else 0.dp,
        animationSpec = spring( //spring 기반의 애니메이션 사용
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    Surface(
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Row(modifier = Modifier.padding(24.dp)) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = extraPadding.coerceAtLeast(0.dp))
            ) {
                Text(text = "Android,")
                Text(
                    "$name", style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold
                    )
                )
            }
            IconButton(
                onClick = { expanded = !expanded }
            ) {
                Icon(
                    imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = if (expanded) {
                        stringResource(id = R.string.show_less)
                    } else {
                        stringResource(id = R.string.show_more)
                    }
                )
            }
        }
    }
}

@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    onContinueClicked: () -> Unit,
){
    Column(
        modifier = modifier.fillMaxSize(), // 기본적으로 가로 세로를 전부 채운다.
        verticalArrangement = Arrangement.Center, //수직 에서 가운데 정렬
        horizontalAlignment = Alignment.CenterHorizontally // 수평에서 가운데 정렬
    ) {
        Text(text = "Welocome to the Basics Codelab!")
        Button(
            modifier = Modifier.padding(vertical = 24.dp),
            onClick =  onContinueClicked
        ) {
            Text(text = "Continue")
        }
    }
}

@Composable
private fun Greetings(
    modifier: Modifier = Modifier,
    names: List<String> = List(1000){"$it"} // it을 사용해 단일 매개변수로 list 생성
) {
    LazyColumn(modifier = modifier.padding(vertical = 4.dp)) {
        items(items = names) {name ->
            Greeting(name = name)
        }
    }
}