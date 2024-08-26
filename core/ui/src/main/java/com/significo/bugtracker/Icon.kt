package com.significo.bugtracker

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.significo.bugtracker.core.ui.R

object AppIcons {
    val Home = R.drawable.ic_home_24
    val About = R.drawable.ic_info_24
}

sealed class Icon {
    data class ImageVectorIcon(val imageVector: ImageVector) : Icon()
    data class DrawableResourceIcon(@DrawableRes val id: Int) : Icon()
}
