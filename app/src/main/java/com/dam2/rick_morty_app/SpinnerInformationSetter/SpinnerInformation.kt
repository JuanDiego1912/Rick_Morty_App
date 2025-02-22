package com.dam2.rick_morty_app.SpinnerInformationSetter

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.dam2.rick_morty_app.Model.Episodes.Episode
import com.dam2.rick_morty_app.R
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class SpinnerInformation(val chipGroup : ChipGroup,
                         val spinner : Spinner,
                         val episodesResponse : MutableList<Episode>,
                         val context : Context
) {

    fun setListenerOnChips() {
        for (i in 0..< chipGroup.childCount) {
            val chip = chipGroup.getChildAt(i) as Chip
            chip.setOnClickListener {

                val seasonEnum = Seasons.entries.find {
                    it.getSeason(context) == chip.text.toString()
                }

                seasonEnum?.let {
                    updateSpinnerContent(getItemsForSpinner(it))
                }
            }
        }
    }

    private fun getItemsForSpinner(season : Seasons) : List<Episode> {
        return episodesResponse.filter { it.episodio.contains(season.toString(), ignoreCase = true) }
    }

    private fun updateSpinnerContent(nuevosItems : List<Episode>) {

        val adapter = ArrayAdapter(
            context,
            R.layout.spinner_selected_item,
            nuevosItems.map {
                it.id.toString() + ". " + it.episodio + " - " + it.nombreEpisodio
            }
        )
        adapter.setDropDownViewResource(R.layout.spinner_list_items)

        spinner.adapter = adapter
    }
}