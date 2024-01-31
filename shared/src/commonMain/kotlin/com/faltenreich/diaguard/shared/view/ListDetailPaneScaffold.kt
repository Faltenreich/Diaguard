import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun ListDetailPaneScaffold(
    listPane: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    scaffoldState: ThreePaneScaffoldState,
    extraPane: (@Composable () -> Unit)? = null,
    detailPane: @Composable () -> Unit,
)

expect interface ThreePaneScaffoldState