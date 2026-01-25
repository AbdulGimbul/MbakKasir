package dev.mbakasir.com.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Minimal animation utilities for cashier-focused UI. These animations are intentionally subtle to
 * not distract from the primary task.
 */

/** Animation duration constants (kept short for cashier workflow efficiency) */
object AnimationDuration {
    const val FAST = 150
    const val NORMAL = 200
    const val SLOW = 300
}

/**
 * Animated visibility wrapper with fade and slide animation. Used for list items and content that
 * appears/disappears.
 */
@Composable
fun AnimatedListItem(
    visible: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter =
            fadeIn(animationSpec = tween(AnimationDuration.FAST)) +
                    slideInVertically(
                        animationSpec = tween(AnimationDuration.FAST),
                        initialOffsetY = { it / 4 } // Subtle slide from 25% below
                    ),
        exit =
            fadeOut(animationSpec = tween(AnimationDuration.FAST)) +
                    slideOutVertically(
                        animationSpec = tween(AnimationDuration.FAST),
                        targetOffsetY = { -it / 4 } // Subtle slide up
                    ),
        modifier = modifier
    ) { content() }
}

/** Fade-only animated visibility (even more minimal). */
@Composable
fun FadeAnimatedContent(
    visible: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(AnimationDuration.NORMAL)),
        exit = fadeOut(animationSpec = tween(AnimationDuration.NORMAL)),
        modifier = modifier
    ) { content() }
}
