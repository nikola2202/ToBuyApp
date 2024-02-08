package com.example.tobuy

import com.airbnb.epoxy.EpoxyController
import com.example.tobuy.ui.epoxy.models.HeaderEpoxyModel

fun EpoxyController.addHeaderModel(headerText: String) {
    HeaderEpoxyModel(headerText).id(headerText).addTo(this)
}