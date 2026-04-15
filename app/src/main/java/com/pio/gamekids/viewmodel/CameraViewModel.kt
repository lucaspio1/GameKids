// Local: /app/src/main/java/com/pio/gamekids/viewmodel/CameraViewModel.kt
package com.pio.gamekids.viewmodel

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.concurrent.Executor

class CameraViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    // TODO: Usar o ID real da família que virá do login (Auth) no futuro
    private val familiaId = "id_familia_exemplo"

    fun capturarFoto(
        context: Context,
        imageCapture: ImageCapture,
        executor: Executor,
        criancaId: String, // <-- ID Dinâmico recebido da tela
        taskId: String,
        onSuccess: () -> Unit
    ) {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "Tarefa_${System.currentTimeMillis()}")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        }

        val outputOptions = ImageCapture.OutputFileOptions.Builder(
            context.contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        ).build()

        imageCapture.takePicture(outputOptions, executor, object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                val savedUri = output.savedUri
                if (savedUri != null) {
                    enviarParaFirebase(context, savedUri, criancaId, taskId, onSuccess)
                } else {
                    Toast.makeText(context, "Erro ao obter a imagem", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onError(exc: ImageCaptureException) {
                Toast.makeText(context, "Erro na Câmera: ${exc.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun enviarParaFirebase(context: Context, uri: Uri, criancaId: String, taskId: String, onSuccess: () -> Unit) {
        Toast.makeText(context, "Enviando prova para aprovação...", Toast.LENGTH_SHORT).show()

        // Caminho organizado no Storage: provas/familia/crianca/tarefa.jpg
        val fileName = "provas/${familiaId}/${criancaId}/${taskId}.jpg"
        val storageRef = storage.reference.child(fileName)

        storageRef.putFile(uri)
            .addOnSuccessListener {
                // Se a foto subiu, pegamos a URL pública dela e salvamos no Firestore
                storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                    atualizarStatusTarefa(context, criancaId, taskId, downloadUrl.toString(), onSuccess)
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Erro no upload: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun atualizarStatusTarefa(context: Context, criancaId: String, taskId: String, imageUrl: String, onSuccess: () -> Unit) {
        val tarefaRef = db.collection("usuarios").document(familiaId)
            .collection("criancas").document(criancaId)
            .collection("atividades").document(taskId)

        tarefaRef.update(
            mapOf(
                "status" to "aguardando_aprovacao",
                "foto_prova_url" to imageUrl
            )
        ).addOnSuccessListener {
            Toast.makeText(context, "Missão enviada com sucesso! 🎉", Toast.LENGTH_LONG).show()
            onSuccess() // Dispara a ação de voltar para a tela inicial
        }.addOnFailureListener {
            Toast.makeText(context, "Erro ao atualizar status: ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }
}