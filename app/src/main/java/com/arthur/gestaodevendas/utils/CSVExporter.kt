package com.arthur.gestaodevendas.utils
import android.content.Context
import android.os.Environment
import android.widget.Toast
import java.io.File
import java.io.IOException

class CSVExporter(private val context: Context) {

    fun exportarCSV(nomeArquivo: String, cabecalho: String, linhas: List<String>) {
        val csvContent = StringBuilder()
        csvContent.append("$cabecalho\n")

        for (linha in linhas) {
            csvContent.append("$linha\n")
        }

        val csvFile = File(getDiretorioArquivos(), nomeArquivo)

        try {
            csvFile.writeText(csvContent.toString())
            exibirMensagem("Lista exportada para $nomeArquivo")
        } catch (e: IOException) {
            exibirMensagem("Erro ao exportar lista")
            e.printStackTrace()
        }
    }

    private fun getDiretorioArquivos(): File {
        val diretorio = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "CSV")
        if (!diretorio.exists()) {
            diretorio.mkdirs()
        }
        return diretorio
    }

    private fun exibirMensagem(mensagem: String) {
        Toast.makeText(context, mensagem, Toast.LENGTH_SHORT).show()
    }
}
