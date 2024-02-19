package com.daasuu.exoplayerfilter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

/**
 * Created by sudamasayuki on 2017/05/18.
 */
class FilterAdapter(
    private val context: Context,
    resource: Int,
    private val values: List<FilterType>
) : ArrayAdapter<FilterType?>(
    context, resource, values
) {
    internal class ViewHolder {
        var text: TextView? = null
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var rowView = convertView
        // reuse views
        if (rowView == null) {
            val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            rowView = inflater.inflate(R.layout.row_text, null)
            // configure view holder
            val viewHolder = ViewHolder()
            viewHolder.text = rowView.findViewById<View>(R.id.label) as TextView
            rowView.tag = viewHolder
        }
        val holder = rowView!!.tag as ViewHolder
        val s = values[position].name
        holder.text!!.text = s
        return rowView
    }
}
