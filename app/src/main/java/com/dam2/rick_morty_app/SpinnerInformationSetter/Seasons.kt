package com.dam2.rick_morty_app.SpinnerInformationSetter

import android.content.Context
import com.dam2.rick_morty_app.R

enum class Seasons(val seasonResId : Int) {

    S01(R.string.season_1),
    S02(R.string.season_2),
    S03(R.string.season_3),
    S04(R.string.season_4),
    S05(R.string.season_5);

    fun getSeason(context : Context) : String {
        return context.getString(seasonResId)
    }
}