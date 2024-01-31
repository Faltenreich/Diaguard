import androidx.compose.material3.adaptive.ListDetailPaneScaffoldDefaults
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
    androidx.compose.material3.adaptive.ListDetailPaneScaffold(
        listPane = { listPane() },
        modifier = modifier,
        scaffoldState = scaffoldState,
        windowInsets = ListDetailPaneScaffoldDefaults.windowInsets,
        extraPane = { extraPane?.invoke() },
        detailPane = { detailPane() },
    )
}

actual typealias ThreePaneScaffoldState = androidx.compose.material3.adaptive.ThreePaneScaffoldState