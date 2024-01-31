import androidx.compose.material3.adaptive.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun ListDetailPaneScaffold(
    listPane: @Composable () -> Unit,
    detailPane: @Composable () -> Unit,
    modifier: Modifier,
) {
    val navigator = rememberListDetailPaneScaffoldNavigator()
    androidx.compose.material3.adaptive.ListDetailPaneScaffold(
        listPane = { listPane() },
        modifier = modifier,
        scaffoldState = navigator.scaffoldState,
        detailPane = { detailPane() },
    )
}