package com.example.android.todoapp.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.*
import com.example.android.todoapp.R
import com.example.android.todoapp.data.model.ToDoData
import com.example.android.todoapp.databinding.FragmentListBinding
import com.example.android.todoapp.fragments.list.adapter.ListAdapter
import com.example.android.todoapp.util.hideKeyboard
import com.example.android.todoapp.viewmodel.SharedViewModel
import com.example.android.todoapp.viewmodel.ToDoViewModel
import com.google.android.material.snackbar.Snackbar
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator

class ListFragment : Fragment(), SearchView.OnQueryTextListener {

    private val mToDoViewModel: ToDoViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private val adapter: ListAdapter by lazy { ListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Data binding
        _binding = FragmentListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mSharedViewModel = mSharedViewModel

        //setup recyclerview
        setupRecyclerView()

        //observe live data from view model
        observeLiveData()

        //setup options menu
        setHasOptionsMenu(true)

        //Hide soft keyboard
        hideKeyboard(requireActivity())

        return binding.root
    }

    private fun observeLiveData() {
        mToDoViewModel.getAllData.observe(viewLifecycleOwner, { items ->
            //update data base is empty or not
            mSharedViewModel.checkIfDatabaseEmpty(items)
            //notify changes data list every times update data base
            adapter.setData(items)
        })
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter

        //Linear Layout manager
        //recyclerView.layoutManager = LinearLayoutManager(requireActivity())

        //Grid Layout Manager
        //recyclerView.layoutManager = GridLayoutManager(requireActivity(), 2)

        //Staggered Grid Layout Manager
        recyclerView.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        //link with animation library
        recyclerView.itemAnimator = SlideInUpAnimator().apply { addDuration = 300 }

        swipeToDelete(recyclerView)
    }

    private fun swipeToDelete(recyclerView: RecyclerView) {
        val swipeToDeleteCallBack = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedItem = adapter.getDataList()[viewHolder.adapterPosition]
                //Delete Item
                mToDoViewModel.deleteData(deletedItem)
                //already linked recyclerview with animation
                adapter.notifyItemRemoved(viewHolder.adapterPosition)
                Toast.makeText(
                    requireContext(),
                    R.string.msg_successfully_removed,
                    Toast.LENGTH_SHORT
                ).show()
                //Restored deleted data function
                restoreDeletedData(viewHolder.itemView, deletedItem, viewHolder.adapterPosition)
            }
        }
        // link call back with item touch helper
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallBack)
        //attach item touch helper to recycler view
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    //Note : Snack bar
    private fun restoreDeletedData(view: View, deletedItem: ToDoData, position: Int) {
        val snackBar = Snackbar.make(
            view,
            getString(R.string.msg_snack_bar_delete, deletedItem.title),
            Snackbar.LENGTH_LONG
        )
        snackBar.setAction(R.string.label_undo) {
            //restore data so insert data to db again
            mToDoViewModel.insertData(deletedItem)
            //Note: to remove this for StaggeredGridLayoutManager
            //added this for animation in linear layout manager. but animation is still work even remove this line
            //adapter.notifyItemChanged(position)
        }
        //don't forget to add show
        snackBar.show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)

        //set up search view in menu
        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete -> confirmRemoval()
            R.id.menu_priority_high -> observeHighPriority()
            R.id.menu_priority_low -> observeLowPriority()
        }
        return super.onOptionsItemSelected(item)
    }

    //Note: this one call high priority by calling live data
    private fun observeHighPriority() {
        mToDoViewModel.sortByHighPriority.observe(this, { items ->
            adapter.setData(items)
        })
    }

    //Note: this one call low priority by calling live data
    private fun observeLowPriority() {
        mToDoViewModel.sortByLowPriority.observe(this, { items ->
            adapter.setData(items)
        })
    }

    private fun confirmRemoval() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage(R.string.dialog_remove_all_message)
        builder.setPositiveButton(R.string.label_yes) { _, _ ->
            mToDoViewModel.deleteAll()
            Toast.makeText(
                requireContext(),
                R.string.msg_successfully_removed,
                Toast.LENGTH_SHORT
            ).show()
        }
        builder.setNegativeButton(R.string.label_no) { dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchThroughDatabase(query)
        }
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if (query != null) {
            searchThroughDatabase(query)
        }
        return true
    }

    private fun searchThroughDatabase(query: String) {
        val searchQuery = "%$query%"
        mToDoViewModel.searchDatabase(searchQuery).observe(this, { list ->
            list?.run { adapter.setData(this) }
        })
    }
}