package com.example.simkader_236.uicontroller.route
import com.example.simkader_236.R

object DestinasiEdit : DestinasiNavigasi {
    override val route = "item_edit"
    override val titleRes = R.string.edit_kader_title
    const val kaderIdArg = "itemId"
    val routeWithArgs = "$route/{$kaderIdArg}"
}