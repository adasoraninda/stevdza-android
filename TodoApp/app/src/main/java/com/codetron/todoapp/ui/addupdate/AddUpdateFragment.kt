package com.codetron.todoapp.ui.addupdate

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.codetron.todoapp.R
import com.codetron.todoapp.ToDoApplication
import com.codetron.todoapp.data.model.Mode
import com.codetron.todoapp.data.model.Priority
import com.codetron.todoapp.data.model.ToDoData
import com.codetron.todoapp.databinding.FragmentAddUpdateBinding
import com.codetron.todoapp.ui.ToDoViewModel
import com.codetron.todoapp.ui.ViewModelFactory
import com.codetron.todoapp.util.hideKeyboard
import javax.inject.Inject

class AddUpdateFragment : Fragment() {

    private var _binding: FragmentAddUpdateBinding? = null
    private val binding get() = _binding

    private val args: AddUpdateFragmentArgs by navArgs()

    @Inject
    lateinit var factory: ViewModelFactory

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
        _binding = FragmentAddUpdateBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding?.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_update_menu, menu)
        val menuSave = menu.findItem(R.id.menu_save)
        val menuDelete = menu.findItem(R.id.menu_delete)

        if (args.mode.toString() == Mode.ADD.toString()) {
            menuSave.setIcon(R.drawable.ic_check)
            menuDelete.isVisible = false
        } else {
            menuSave.setIcon(R.drawable.ic_save)
            menuDelete.isVisible = true
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_save -> {
                insertDataToDb()
                hideKeyboard(requireActivity())
            }
            R.id.menu_delete -> showAlertDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        spinnerListener()
        initToDoData()
    }

    private fun initToDoData() {
        if (args.toDo != null) {
            binding?.toDo = args.toDo
        }
    }

    private fun insertDataToDb() {
        with(binding) {
            val id = args.toDo?.id ?: 0
            val title = this?.editTextTitle?.text?.toString()?.trim()
            val priority = this?.spinner?.selectedItem?.toString()
            val description = this?.editTextDescription?.text?.toString()?.trim()

            lateinit var message: String

            val validation = ToDoData.verifyDataFromUser(title, description)
            if (validation) {
                val toDoData = ToDoData(
                    id,
                    title ?: getString(R.string.default_value),
                    Priority.parsePriority(priority),
                    description ?: getString(R.string.default_value)
                )
                viewModel.insertData(toDoData)
                message =
                    if (args.toDo == null) getString(R.string.success_add_todo) else getString(R.string.success_update_todo)
                findNavController().popBackStack()
            } else {
                message = getString(R.string.warning_input)
            }
            showToastMessage(message)
        }
    }

    private fun showAlertDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(String.format(getString(R.string.alert_delete_title), args.toDo?.title))
        builder.setMessage(
            String.format(
                getString(R.string.alert_delete_message),
                args.toDo?.title
            )
        )
        builder.setPositiveButton(R.string.yes) { dialog, _ ->
            args.toDo?.let { viewModel.deleteData(it) }
            dialog.dismiss()
            showToastMessage(getString(R.string.alert_delete_success))
            findNavController().popBackStack()
        }
        builder.setNegativeButton(R.string.no) { dialog, _ -> dialog.dismiss() }
        builder.create().show()

    }

    private fun showToastMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun spinnerListener() {
        binding?.spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> (parent?.getChildAt(0) as TextView).setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.red
                        )
                    )
                    1 -> (parent?.getChildAt(0) as TextView).setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.yellow
                        )
                    )
                    2 -> (parent?.getChildAt(0) as TextView).setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.green
                        )
                    )
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }



}