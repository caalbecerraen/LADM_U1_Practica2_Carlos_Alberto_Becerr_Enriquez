package mx.tecnm.tepic.ladm_u1_practica2

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        abrir.setOnClickListener {
            if(rdint.isChecked){
            if (AbrirEnMemoriaInterna() == true) {
                AlertDialog.Builder(this).setTitle("Atención").setMessage("Se Abrio con extito")
                    .setPositiveButton(
                        "ok"
                    ) { d, i -> d.dismiss() }.show()
            } else {
                AlertDialog.Builder(this).setTitle("Atención")
                    .setMessage("No se pudo abrir la data").setPositiveButton(
                    "ok"
                ) { d, i -> d.dismiss() }.show()
            }}
            if (rdext.isChecked){
                if (abrirDesdeMemoriaExterna() == true) {
                    AlertDialog.Builder(this).setTitle("Atención").setMessage("Se Abrio con extito")
                        .setPositiveButton(
                            "ok"
                        ) { d, i -> d.dismiss() }.show()
                } else {
                    AlertDialog.Builder(this).setTitle("Atención")
                        .setMessage("No se pudo abrir la data").setPositiveButton(
                            "ok"
                        ) { d, i -> d.dismiss() }.show()
                }
            }
        }
        guardar.setOnClickListener {
            if(rdint.isChecked){
            if (guardarEnMemoriaInterna() == true) {
                AlertDialog.Builder(this).setTitle("Atención").setMessage("Se guardo data")
                    .setPositiveButton(
                        "ok"
                    ) { d, i -> d.dismiss() }.show()
                texto.setText("")
            } else {
                AlertDialog.Builder(this).setTitle("Atención")
                    .setMessage("No se pudo guardor la data").setPositiveButton(
                    "ok"
                ) { d, i -> d.dismiss() }.show()
            }
            }
            if (rdext.isChecked){
                if(ActivityCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        0
                    )}
                if (guardarDesdeMemoriaExterna() == true) {
                    AlertDialog.Builder(this).setTitle("Atención").setMessage("Se guardo data")
                        .setPositiveButton(
                            "ok"
                        ) { d, i -> d.dismiss() }.show()
                    texto.setText("")
                } else {
                    AlertDialog.Builder(this).setTitle("Atención")
                        .setMessage("No se pudo guardor la data").setPositiveButton(
                            "ok"
                        ) { d, i -> d.dismiss() }.show()
                }
            }
        }


    }
    private fun abrirDesdeMemoriaExterna():Boolean{
        try {
            if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
                AlertDialog.Builder(this).setTitle("Error")
                    .setMessage("No existe memoria secundaria insertada")
                    .setPositiveButton("OK") { d, i -> d.dismiss() }.show()
                return false;}
            var rutaSD = Environment.getExternalStorageDirectory()
            var archivoEnSD= File(rutaSD.absolutePath, arcnom.text.toString())
            if (archivoEnSD.exists()){
                val archivo = InputStreamReader(FileInputStream(archivoEnSD))
                val br = BufferedReader(archivo)
                var linea = br.readLine()
                val todo = StringBuilder()
                while (linea != null) {
                    todo.append(linea + "\n")
                    linea = br.readLine()
                }
                br.close()
                archivo.close()
                texto.setText(todo)
            }
            else
            {
                AlertDialog.Builder(this).setTitle("Error")
                    .setMessage("No existe el archivo")
                    .setPositiveButton("OK") { d, i -> d.dismiss() }.show()
            }

        } catch (IO: Exception) {
            return false
        }
        return true;
    }
    private fun guardarDesdeMemoriaExterna():Boolean{
        var flujo: FileOutputStream? = null
        var escritor: OutputStreamWriter? = null
        try {
            if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
                AlertDialog.Builder(this).setTitle("Error")
                    .setMessage("No existe memoria secundaria insertada")
                    .setPositiveButton("OK") { d, i -> d.dismiss() }.show()
                return false;}
            val ruta = Environment.getExternalStorageDirectory()
            val archivoEnSD = File(ruta.absolutePath, arcnom.text.toString())
            if (!archivoEnSD.exists()){
                flujo = FileOutputStream(archivoEnSD)
                escritor = OutputStreamWriter(flujo)
                escritor.write(texto.getText().toString())
                escritor?.close()
            }else{
                AlertDialog.Builder(this).setTitle("Error")
                    .setMessage("Ya existe un archivo con ese nombre")
                    .setPositiveButton("OK") { d, i -> d.dismiss() }.show()
                    return false
            }
        } catch (io:Exception) {
            return false
        }
        return true
    }
    private fun guardarEnMemoriaInterna():Boolean{
        if (fileList().contains(arcnom.text.toString()))
        {
            AlertDialog.Builder(this).setTitle("Error")
                .setMessage("Ya existe un archivo con ese nombre")
                .setPositiveButton("OK") { d, i -> d.dismiss() }.show()
            return false
        }
        try {
            var flujoSalida = OutputStreamWriter(openFileOutput(arcnom.text.toString(), MODE_PRIVATE))
            var data = texto.text.toString()
            flujoSalida.write(data)
            flujoSalida.flush()
            flujoSalida.close()
        }
        catch (io: IOException){
            return false
        }
        return true;
    }
    private fun AbrirEnMemoriaInterna():Boolean {
        if (fileList().contains(arcnom.text.toString())) {
            try {
                val archivo = InputStreamReader(openFileInput(arcnom.text.toString()))
                val br = BufferedReader(archivo)
                var linea = br.readLine()
                val todo = StringBuilder()
                while (linea != null) {
                    todo.append(linea + "\n")
                    linea = br.readLine()
                }
                br.close()
                archivo.close()
                texto.setText(todo)
            } catch (e: IOException) {
                return false;
            }

        }
        else{AlertDialog.Builder(this).setTitle("Error")
            .setMessage("No existe archivo con ese nombre")
            .setPositiveButton("OK") { d, i -> d.dismiss() }.show()
            return false;}
        return true;
    }
    }