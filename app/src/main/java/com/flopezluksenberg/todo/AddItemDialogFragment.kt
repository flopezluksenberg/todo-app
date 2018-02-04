package com.flopezluksenberg.todo

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import kotlinx.android.synthetic.main.fragment_addtodoitem.*


class AddItemDialogFragment : DialogFragment() {

    companion object {
        const val TAG = "AddItemDialogFragment"

        fun newInstance(): AddItemDialogFragment{
            return AddItemDialogFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_addtodoitem, container)

        dialog.window.requestFeature(Window.FEATURE_NO_TITLE)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnCancel.setOnClickListener { dismiss() }
        btnOk.setOnClickListener {
            (activity as Listener).onAddItem(edtInput.text.toString())
            dismiss()
        }
    }

    interface Listener {
        fun onAddItem(itemText: String)
    }
}