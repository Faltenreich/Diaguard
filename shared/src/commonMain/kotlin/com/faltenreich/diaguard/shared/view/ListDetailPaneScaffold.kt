import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun ListDetailPaneScaffold(
    listPane: @Composable () -> Unit,
    detailPane: @Composable () -> Unit,
    modifier: Modifier = Modifier,
)