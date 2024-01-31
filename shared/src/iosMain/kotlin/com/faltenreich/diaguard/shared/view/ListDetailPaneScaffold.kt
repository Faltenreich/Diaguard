import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun ListDetailPaneScaffold(
    listPane: @Composable () -> Unit,
    modifier: Modifier,
    scaffoldState: ThreePaneScaffoldState,
    extraPane: (@Composable () -> Unit)?,
    detailPane: @Composable () -> Unit,
) {
    TODO("Not yet implemented")
}

actual interface ThreePaneScaffoldState