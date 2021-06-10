package mx.tecnm.tepic.ladm_u3_p2_firebasecloud

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item.*


class item : AppCompatActivity() {
    var baseFirebase=FirebaseFirestore.getInstance()
    var mostrarCompletadas= arrayListOf<String>()
    var mostrarCurso= arrayListOf<String>()
    var idsCurso= arrayListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item)

        pedidos.setOnClickListener {
            mandarContacto()
            finish()
        }

        mostrarPedidos.setOnClickListener {
            obtenerCompletadas()
            obtenerCurso()
        }
    }

    private fun mandarContacto(){
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("Nombre", nombre.text.toString())
        intent.putExtra("Telefono", celular.text.toString())
        intent.putExtra("Domicilio", domicilio.text.toString())
        startActivity(intent)

    }

    private fun obtenerCompletadas(){
        mostrarCompletadas.clear()
        baseFirebase.collection("pedidos")
            .whereEqualTo("Entregado", true)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    var itemInfo="${document.getString("Nombre")} \n ${document.getString("Telefono")}"
                    mostrarCompletadas.add(itemInfo)
                }
                pedidosCompletados.adapter = ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, mostrarCompletadas)

            }
            .addOnFailureListener { exception ->
            }

    }
    private fun obtenerCurso(){
        mostrarCurso.clear()
        idsCurso.clear()
        baseFirebase.collection("pedidos")
            .whereEqualTo("Entregado", false)
            .get()
            .addOnSuccessListener { documents ->

                for (document in documents) {
                    var itemInfo="${document.getString("Nombre")}\n${document.getString("Telefono")}"
                    mostrarCurso.add(itemInfo)
                    idsCurso.add(document.id.toString())
                }
                pedidosCurso.adapter = ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, mostrarCurso)
                pedidosCurso.setOnItemClickListener { parent, view, posicion, i ->
                    editarPedido(posicion)
                }
            }
            .addOnFailureListener { exception ->
            }



    }

    private fun editarPedido(pos:Int){
        AlertDialog.Builder(this).setTitle("ATENCION")
            .setMessage("El pedido ha sido entregado")
            .setPositiveButton("Entregado"){d, i->
                baseFirebase.collection("pedidos")
                    .document(idsCurso.get(pos))
                    .update("Entregado",   true)
                    .addOnSuccessListener {
                        pedidosCurso.adapter = ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, mostrarCurso)
                        pedidosCurso.adapter = ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, mostrarCompletadas)
                    }
                    .addOnFailureListener {
                    }
            }
            .setNegativeButton("Cancelar"){d,i->}
            .show()

    }
}