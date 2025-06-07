package com.attendance.app.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.attendance.app.databinding.FragmentUserManagementBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class UserManagementFragment : Fragment() {
    private var _binding: FragmentUserManagementBinding? = null
    private val binding get() = _binding!!
    private val viewModel: UserViewModel by viewModels()
    private lateinit var adapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserManagementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupFab()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        adapter = UserAdapter(
            onEditClick = { user ->
                showEditUserDialog(user)
            },
            onDeleteClick = { user ->
                showDeleteConfirmationDialog(user)
            }
        )
        binding.rvUsers.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@UserManagementFragment.adapter
        }
    }

    private fun setupFab() {
        binding.fabAddUser.setOnClickListener {
            showAddUserDialog()
        }
    }

    private fun observeViewModel() {
        viewModel.users.observe(viewLifecycleOwner) { users ->
            adapter.submitList(users)
        }
    }

    private fun showAddUserDialog() {
        UserDialog.newInstance().show(
            childFragmentManager,
            "add_user"
        )
    }

    private fun showEditUserDialog(user: User) {
        UserDialog.newInstance(user).show(
            childFragmentManager,
            "edit_user"
        )
    }

    private fun showDeleteConfirmationDialog(user: User) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Hapus Pengguna")
            .setMessage("Apakah Anda yakin ingin menghapus ${user.name}?")
            .setPositiveButton("Hapus") { _, _ ->
                viewModel.deleteUser(user)
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 