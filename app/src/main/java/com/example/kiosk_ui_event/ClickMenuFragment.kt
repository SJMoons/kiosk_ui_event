package com.example.kiosk_ui_event

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment

class ClickMenuFragment(clickMenuImage:Int,clickMenuName:String,clickMenuCost:Int): Fragment() {
    lateinit var dataInterface: DataInterface
    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataInterface = context as DataInterface
    }
    var menuImage = clickMenuImage
    var menuName = clickMenuName
    var menuCost = clickMenuCost
    var count: Int = 1
    var toppingCountString: String? = ""
    var toppingCountInt: Int = 0
    var appendTopping = mutableListOf<String>()
    var totalToppingPrice = mutableListOf<Int>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var view: View = inflater.inflate(R.layout.clickmenu_fragment, container, false)
        var cancelBtn = view.findViewById<Button>(R.id.cancel_btn)
        var selectBtn = view.findViewById<Button>(R.id.select_btn)
        var addBtn = view.findViewById<Button>(R.id.add_btn)
        var minusBtn = view.findViewById<Button>(R.id.cups_minus_btn)
        var plusBtn = view.findViewById<Button>(R.id.cups_plus_btn)
        var menu = view.findViewById<TextView>(R.id.menu_name)
        menu.setText(menuName)
        var cost = view.findViewById<TextView>(R.id.cost)
        cost.setText("₩ ${menuCost.toString()}")
        var image = view.findViewById<ImageView>(R.id.image)
        image.setBackgroundResource(menuImage)

        minusBtn!!.setOnClickListener {
            count = count - 1
            if (count < 1) {
                count = 1
            }
            cups()
        }
        plusBtn!!.setOnClickListener {
            count = count + 1
            cups()
        }
        cancelBtn!!.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.fragmentArea, MenuListFragment())
                .commit()
        }
        selectBtn!!.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.fragmentArea, MenuListFragment())
                .commit()
        }
        addBtn!!.setOnClickListener {
            topping()
        }
        return view
    }

    fun topping() {
        var popupView = getLayoutInflater().inflate(R.layout.topping_fragment, null)
        var alertdialog = AlertDialog.Builder(context).create()
        alertdialog.setView(popupView)
        alertdialog.show()
        alertdialog.window!!.setLayout(1100, 1700)

        var gobackBtn = popupView.findViewById<Button>(R.id.topping_goback_btn)
        gobackBtn!!.setOnClickListener {
            alertdialog.hide()
        }

        for (i in 0..8) {
            var toppingPlusBtn = popupView.findViewById<Button>(R.id.topping_plus_btn1 + i)
            var toppingCountView = popupView.findViewById<TextView>(R.id.topping_count_view1 + i)
            toppingPlusBtn.setOnClickListener {
                toppingCountString = toppingCountView.text.toString()
                toppingCountInt = toppingCountString!!.toInt()
                toppingCountInt = toppingCountInt + 1
                toppingCountView.setText(toppingCountInt.toString())
            }
            var toppingMinusBtn = popupView.findViewById<Button>(R.id.topping_minus_btn1 + i)
            toppingMinusBtn.setOnClickListener {
                toppingCountString = toppingCountView.text.toString()
                toppingCountInt = toppingCountString!!.toInt()
                toppingCountInt = toppingCountInt - 1
                if (toppingCountInt < 0) {
                    toppingCountInt = 0
                }
                toppingCountView.setText(toppingCountInt.toString())
            }

            var completeBtn = popupView.findViewById<Button>(R.id.compelete_btn)
            completeBtn!!.setOnClickListener {
                for (i in 0..8) {
                    appendTopping.add(popupView.findViewById<TextView>(R.id.topping_count_view1 + i).getText().toString())
                }
                totalPrice()
                alertdialog.hide()
            }
        }
    }

    fun cups() {
        var cupCount = view?.findViewById<TextView>(R.id.cups_count)
        var cost = view?.findViewById<TextView>(R.id.cost)
        cupCount!!.setText(count.toString())
        cost!!.setText("₩ ${((menuCost+totalToppingPrice.sum())*count).toString()}")
    }

    fun totalPrice() {
        var cost = view?.findViewById<TextView>(R.id.cost)
        var toppingCosts = resources.getIntArray(R.array.topping_cost)
        for (i in 0 until appendTopping.count()) {
            totalToppingPrice.add((appendTopping[i].toInt()*toppingCosts[i]))
        }
        Log.d("message","${(menuCost+totalToppingPrice.sum())*count}")
        cost!!.setText("₩ ${((menuCost+totalToppingPrice.sum())*count).toString()}")
        dataInterface.dataPass(menuName,count,"${(menuCost+totalToppingPrice.sum())*count}")
    }
}