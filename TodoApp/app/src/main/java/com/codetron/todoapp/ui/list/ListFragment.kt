package com.codetron.todoapp.ui.list

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.codetron.todoapp.R
import com.codetron.todoapp.ToDoApplication
import com.codetron.todoapp.adapter.ToDoListAdapter
import com.codetron.todoapp.data.model.Mode
import com.codetron.todoapp.data.model.ToDoData
import com.codetron.todoapp.databinding.FragmentListBinding
import com.codetron.todoapp.ui.ToDoViewModel
import com.codetron.todoapp.ui.ViewModelFactory
import com.codetron.todoapp.util.hideKeyboard
import com.google.android.material.snackbar.Snackbar
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import javax.inject.Inject

class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding

    @Inject
    lateinit var factory: ViewModelFactory

    @Inject
    lateinit var toDoListAdapter: ToDoListAdapter

    private val viewModel: ToDoViewModel by viewModels { factory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as ToDoApplication).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        hideKeyboard(requireActivity())
        return binding?.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_menu, menu)

        val searchMenu = menu.findItem(R.id.menu_search)
        val searchView = searchMenu.actionView as? SearchView

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    viewModel.searchData("%$query%")
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    viewModel.searchData("%$newText%")
                }
                return true
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_priority_high -> viewModel.getListDataByPriority(true)
            R.id.menu_priority_low -> viewModel.getListDataByPriority(false)
            R.id.menu_delete_all -> showAlertDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniListToDo()
        observeListToDo()
        addButtonListener()
    }

    private fun addButtonListener() {
        binding?.fabAdd?.setOnClickListener {
            val action = ListFragmentDirections.actionListToAddUpdateFragment(Mode.ADD)
            findNavController().navigate(action)
        }
    }

    private fun iniListToDo() {
        with(binding) {
            this?.listTodo?.layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            this?.listTodo?.itemAnimator = SlideInUpAnimator().apply { addDuration = 300 }
            this?.listTodo?.adapter = toDoListAdapter.apply {
                setOnClickListener {
                    val action =
                        ListFragmentDirections.actionListToAddUpdateFragment(Mode.UPDATE, it)
                    findNavController().navigate(action)
                }
            }
            swipeToDelete(this?.listTodo)
        }
    }

    private fun swipeToDelete(recyclerView: RecyclerView?) {
        val itemTouchHelperCallback =
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean = false

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val itemToDelete = toDoListAdapter.currentList[viewHolder.adapterPosition]
                    viewModel.deleteData(itemToDelete)
                    restoreDeleteData(viewHolder.itemView, itemToDelete)
                }
            }
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView)
    }

    private fun restoreDeleteData(view: View, toDoData: ToDoData) {
        Snackbar.make(
            view,
            String.format(getString(R.string.snackbar_delete_to_do), toDoData.title),
            Snackbar.LENGTH_LONG
        )
            .setAction(getString(R.string.undo)) { viewModel.insertData(toDoData) }
            .show()
    }

    private fun observeListToDo() {
        viewModel.allData.observe(viewLifecycleOwner, {
            if (it.isNullOrEmpty()) {
                toDoListAdapter.submitList(emptyList())
                binding?.groupNoData?.visibility = View.VISIBLE
            } else {
                toDoListAdapter.submitList(it)
                binding?.groupNoData?.visibility = View.GONE
            }
        })
    }

    private fun showAlertDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(R.string.alert_delete_all_title)
        builder.setMessage(R.string.alert_delete_all_message)
        builder.setPositiveButton(R.string.yes) { dialog, _ ->
            viewModel.deleteAllData()
            dialog.dismiss()
            showToastMessage(getString(R.string.alert_delete_all_success))
        }
        builder.setNegativeButton(R.string.no) { dialog, _ -> dialog.dismiss() }
        builder.create().show()

    }

    private fun showToastMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

}