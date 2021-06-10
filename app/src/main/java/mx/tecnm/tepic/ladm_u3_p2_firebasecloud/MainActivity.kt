package mx.tecnm.tepic.ladm_u3_p2_firebasecloud

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    var baseFirebase =FirebaseFirestore.getInstance()
    var pedido= arrayListOf<Any>()
    var mostrarItems= arrayListOf<String>()
    var costoTotal=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        agregarItem.setOnClickListener {
            agregarItem()
        }

        Pedido.setOnClickListener {
            confirmarPedido()
            val intent = Intent(this, item::class.java)
            startActivity(intent)
        }
    }

    private fun agregarItem(){
        var item = hashMapOf(
            "Producto" to Producto.text.toString(),
            "Cantidad" to Cantidad.text.toString(),
            "Descripcion" to Descripcion.text.toString(),
            "Precio" to Precio.text.toString()
        )

        pedido.add(item)
        var itemInfo=Producto.text.toString()+"\n"+Descripcion.text.toString()
        mostrarItems.add(itemInfo)

        limpiarCampos()
        actualizarLista()
        listaItems.setOnItemClickListener { parent, view, posicion, i ->
            eliminarItem(posicion)
        }

    }

    private fun actualizarLista(){
        listaItems.adapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_expandable_list_item_1,
            mostrarItems
        )
        listaItems.setOnItemClickListener { parent, view, posicion, i ->
            eliminarItem(posicion)
        }
    }
    private fun confirmarPedido(){
            var insertar = hashMapOf(
                "Nombre" to intent.getStringExtra("Nombre")!!,
                "Telefono" to intent.getStringExtra("Telefono")!!,
                "Domicilio" to intent.getStringExtra("Domicilio")!!,
                "Pedido" to pedido,
                "Fecha" to fecha(),
                "Entregado" to false
            )
            baseFirebase.collection("pedidos")
                .add(insertar)
                .addOnSuccessListener { documentReference ->
                }
            pedido.clear()

        }

    private fun limpiarCampos(){
        Producto.text.clear()
        Producto.text.clear()
        Descripcion.text.clear()
        Precio.text.clear()
    }

    private fun eliminarItem(pos: Int){
        AlertDialog.Builder(this).setTitle("ATENCION")
            .setMessage("Selecciona la opcion a escoger")
            .setPositiveButton("Eliminar"){ d, i->
                pedido.removeAt(pos)
                mostrarItems.removeAt(pos)
                actualizarLista()
            }
            .setNegativeButton("Cancelar"){ d, i->}
            .show()

    }
    fun fecha(): String? {
        val c: Calendar = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy-MM-dd HH:mms")
        var fecha = df.format(c.getTime())
        return fecha
    }
    private fun toast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}


