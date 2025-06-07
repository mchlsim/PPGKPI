package com.attendance.app.ui.user

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.attendance.app.data.model.User
import com.attendance.app.databinding.DialogUserBinding
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder
import java.util.*

class UserDialog : DialogFragment() {
    private var _binding: DialogUserBinding? = null
    private val binding get() = _binding!!
    private val viewModel: UserViewModel by viewModels()
    
    private var existingUser: User? = null

    companion object {
        fun newInstance(user: User? = null): UserDialog {
            return UserDialog().apply {
                existingUser = user
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogUserBinding.inflate(layoutInflater)
        
        if (existingUser != null) {
            binding.etName.setText(existingUser?.name)
            binding.etPosition.setText(existingUser?.position)
        }
        
        return AlertDialog.Builder(requireContext())
            .setTitle(if (existingUser == null) "Tambah Pengguna Baru" else "Edit Pengguna")
            .setView(binding.root)
            .setPositiveButton(if (existingUser == null) "Tambah" else "Simpan") { _, _ ->
                saveUser()
            }
            .setNegativeButton("Batal", null)
            .create()
    }

    private fun saveUser() {
        val name = binding.etName.text.toString()
        val position = binding.etPosition.text.toString()

        if (name.isBlank()) {
            binding.tilName.error = "Nama harus diisi"
            return
        }

        if (position.isBlank()) {
            binding.tilPosition.error = "Jabatan harus diisi"
            return
        }

        val qrCode = existingUser?.qrCode ?: generateQRCode()
        val user = User(
            id = existingUser?.id ?: UUID.randomUUID().toString(),
            name = name,
            position = position,
            qrCode = qrCode
        )

        if (existingUser == null) {
            viewModel.createUser(user)
        } else {
            viewModel.updateUser(user)
        }
    }

    private fun generateQRCode(): String {
        return UUID.randomUUID().toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 