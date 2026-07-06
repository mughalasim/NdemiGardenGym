package com.ndemi.garden.gym.ui.appSnackbar

import androidx.compose.material3.SnackbarHostState
import com.ndemi.garden.gym.ui.screens.MainDispatcherRule
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AppSnackbarViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val hostState: SnackbarHostState = mockk()
    private val appSnackbar: AppSnackbar = mockk()
    private lateinit var viewModel: AppSnackbarViewModel

    @Before
    fun setUp() {
        viewModel = AppSnackbarViewModel(hostState, appSnackbar)
    }

    @Test
    fun `initial state should be Gone`() {
        assertEquals(AppSnackbarViewModel.AppSnackbarState.Gone, viewModel.appSnackbarState.value)
    }

    @Test
    fun `showSnackbar should set state to Visible and then Gone after delay`() =
        runTest {
            // Given
            val data = AppSnackbarData(type = AppSnackbarType.INFO, message = "Test Message")

            // When
            viewModel.showSnackbar(data)

            // Then
            assertTrue(viewModel.appSnackbarState.value is AppSnackbarViewModel.AppSnackbarState.Visible)
            assertEquals(data, (viewModel.appSnackbarState.value as AppSnackbarViewModel.AppSnackbarState.Visible).data)

            // Advance time
            advanceTimeBy(5001)

            // Then
            assertEquals(AppSnackbarViewModel.AppSnackbarState.Gone, viewModel.appSnackbarState.value)
        }
}
