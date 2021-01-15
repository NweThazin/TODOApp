package com.example.android.todoapp.fragments.add

import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.android.todoapp.R
import com.example.android.todoapp.data.model.APIKey
import com.example.android.todoapp.data.model.Priority
import com.example.android.todoapp.data.model.ToDoData
import com.example.android.todoapp.viewmodel.SharedViewModel
import com.example.android.todoapp.viewmodel.ToDoViewModel
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_add.view.*

class AddFragment : Fragment() {

    private val mToDoViewModel: ToDoViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_add, container, false)
        //set Menu
        setHasOptionsMenu(true)

        //set custom listener of spinner priority
        view.spinner_priorities.onItemSelectedListener = mSharedViewModel.listener

        observeLiveData()

        // Inflate the layout for this fragment
        return view
    }

    private fun observeLiveData() {
        mToDoViewModel.insertData.observe(viewLifecycleOwner, Observer {
            when (it) {
                APIKey.SUCCESS -> {
                    Toast.makeText(
                        requireContext(),
                        R.string.msg_successfully_added,
                        Toast.LENGTH_SHORT
                    ).show()

                    //back to list fragment
                    findNavController().navigate(R.id.action_addFragment_to_listFragment)
                }
                APIKey.FAIL -> {
                    Toast.makeText(
                        requireContext(),
                        R.string.error_msg_failed_to_add_a_task,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {

                }
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_add) {
            insertDataToDb()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun insertDataToDb() {
        val mTitle = et_title.text.toString()
        val mPriority = spinner_priorities.selectedItem.toString()
        val mDescription = et_description.text.toString()

        val validation = mSharedViewModel.verifyDataFromUser(mTitle, mDescription)
        if (validation) {
            //insert data to database
            val newData = ToDoData(
                id = 0,
                title = mTitle,
                priority = mSharedViewModel.parsePriority(mPriority),
                description = mDescription
            )

            mToDoViewModel.insertData(newData)
        } else {
            Toast.makeText(requireContext(), R.string.msg_fill_all_fields, Toast.LENGTH_SHORT)
                .show()
        }
    }


}