package com.ndemi.garden.gym.ui.screens.base

import com.ndemi.garden.gym.ui.screens.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BaseViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    sealed interface TestState : BaseState {
        data object Initial : TestState

        data object Updated : TestState
    }

    sealed interface TestAction : BaseAction<TestState> {
        data object Update : TestAction {
            override fun reduce(state: TestState): TestState = TestState.Updated
        }
    }

    class TestViewModel : BaseViewModel<TestState, TestAction>(TestState.Initial)

    @Test
    fun `initial state should be set correctly`() {
        val viewModel = TestViewModel()
        assertEquals(TestState.Initial, viewModel.uiStateFlow.value)
    }

    @Test
    fun `sendAction should update state`() =
        runTest {
            val viewModel = TestViewModel()
            viewModel.sendAction(TestAction.Update)

            // State update happens in viewModelScope.launch, so it might need a small wait or unconfined dispatcher
            // MainDispatcherRule uses UnconfinedTestDispatcher by default, so it should be immediate.
            assertEquals(TestState.Updated, viewModel.uiStateFlow.value)
        }
}
