package com.faltenreich.diaguard.food.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.food.detail.eaten.FoodEatenList
import com.faltenreich.diaguard.food.detail.info.FoodInfo
import com.faltenreich.diaguard.food.detail.nutrient.FoodNutrientList
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf

@Composable
fun FoodDetail(
    modifier: Modifier = Modifier,
    viewModel: FoodDetailViewModel = inject(),
) {
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        initialPage = FoodDetailTab.INFO.ordinal,
        pageCount = { FoodDetailTab.entries.size },
    )
    Column(modifier = modifier) {
        TabRow(selectedTabIndex = pagerState.currentPage) {
            FoodDetailTab.entries.forEachIndexed { index, tab ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = { coroutineScope.launch { pagerState.scrollToPage(index) } },
                    text = { Text(getString(tab.label)) }
                )
            }
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
        ) { page ->
            when (FoodDetailTab.entries[page]) {
                FoodDetailTab.INFO -> FoodInfo(viewModel.food, modifier = Modifier.fillMaxSize())
                FoodDetailTab.NUTRIENT_LIST -> FoodNutrientList(
                    modifier = Modifier.fillMaxSize(),
                    viewModel = inject { parametersOf(viewModel.food) },
                )
                FoodDetailTab.EATEN_LIST -> FoodEatenList(viewModel.food, modifier = Modifier.fillMaxSize())
            }
        }
    }
}