package com.example.memorydam_numeros

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

//numero en estado normal
var fondo = ""

//Lista con los numeros
var numeros:MutableList<Int> = arrayListOf()

//Se guardan duplicados
var botonera:MutableList<Button> = arrayListOf()

//Imagenes barajadas
var arrayBarajado:MutableList<Int> = arrayListOf()

//Los botones que se han pulsado y se comparan
var primero: Button? = null

//Posiciones de los numeros a comparar en la lista
var numeroPrimero: Int = 0
var numeroSegundo: Int = 0

//Bloqueador del juego para evitar pulsar mientras se realiza la comprobacion
var bloqueo: Boolean = false

//Controlador de las pausas del juego
val handler: Handler = Handler()

//Control del número de aciertos y la puntuación
var aciertos = 0
var intentos = 0

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        cargarNumeros()
        botonesMenu()
        iniciar()
    }

    fun cargarNumeros() {
        numeros.add(0, 1)
        numeros.add(1, 2)
        numeros.add(2, 3)
        numeros.add(3, 4)
        numeros.add(4, 5)
        numeros.add(5, 6)
    }

    fun barajar(longitud: Int): ArrayList<Int> {
        var resultadoA = ArrayList<Int>()
        for (i in 0 until longitud) {
            resultadoA.add(i % longitud / 2)
        }
        Collections.shuffle(resultadoA)
        return resultadoA
    }

    fun cargarBotones() {
        botonera.add(BTN0)
        botonera.add(BTN1)
        botonera.add(BTN2)
        botonera.add(BTN3)
        botonera.add(BTN4)
        botonera.add(BTN5)
        botonera.add(BTN6)
        botonera.add(BTN7)
        botonera.add(BTN8)
        botonera.add(BTN9)
        botonera.add(BTN10)
        botonera.add(BTN11)

        TVresultado.setText("Puntuación: $aciertos")
        TVintentos.setText("Intentos: $intentos")
    }

    fun botonesMenu() {
        BTNreset.setOnClickListener {
            iniciar()
        }

        BTNsalir.setOnClickListener {
            val alerta = AlertDialog.Builder (this).create()
            alerta.setTitle("SALIR")
            alerta.setMessage("¿Esta seguro de que deseas salir?")
            alerta.setButton(AlertDialog.BUTTON_POSITIVE, "SI", {
                    dialog, which -> finish()
            })
            alerta.setButton(AlertDialog.BUTTON_NEGATIVE, "NO", {
                    dialog, which ->Toast.makeText(this, "Siga jugando", Toast.LENGTH_SHORT).show()
            })
            alerta.show()
        }
    }

    fun comprobar(i: Int, btn: Button) {
        //Si ningun boton ha sido pulsado
        if (primero == null) {
            //El primer boton sera el que acabamos de pulsar
            primero = btn
            //Le asignamos el numero situado en arrayBarajado[i]
            primero!!.text = "${arrayBarajado[i]}"
            //Bloqueamos el boton para que no se pierda el numero
            primero!!.isEnabled = false
            //Almacenamos el valor obtenido de arrayBarajado
            numeroPrimero = arrayBarajado[i]

            //Si ya hay un boton pulsado
        } else {
            //bloqueamos todos los demás
            bloqueo = true
            //El segundo boton sera el proximo en pulsar
            //Le asignamos el numero situado en arrayBarajado[i]
            btn.text = "${arrayBarajado[i]}"
            //bloqueamos el botón
            btn.isEnabled = false
            //almacenamos el valor de arrayBarajado.get(i)
            numeroSegundo = arrayBarajado[i]

            //Si los numeros coinciden, los dejamos destapados
            if (numeroPrimero == numeroSegundo) {
                primero = null
                bloqueo = false
                //Aumentamos los aciertos y la puntuación
                aciertos++
                intentos++
                TVresultado.text = "Puntuación: $aciertos"
                TVintentos.text = "Intentos: $intentos"
                //Cuando se hayan acertado todos los numeros se ha ganado el juego
                if (aciertos == numeros.size) {
                    val mensaje = Toast.makeText(applicationContext, "¡Has ganado!", Toast.LENGTH_LONG)
                    mensaje.show()
                }

                //Si los numeros NO coinciden, volvemos a tapar al cabo de un tiempo (1 segundo)
            } else {
                handler.postDelayed({
                    //Dejamos asignadas la imagen de fondo
                    primero!!.text = ""
                    btn.text = ""
                    //Los volvemos a habilitar
                    primero!!.isEnabled = true
                    btn.isEnabled = true
                    //Reiniciamos las variables auxiliares
                    primero = null
                    bloqueo = false
                    //Restamos uno a la puntuación
                    intentos++
                    TVintentos.text = "Intentos: $intentos"

                    //Al cabo de un segundo
                }, 1000)
            }
        }
    }

    fun iniciar() {
        arrayBarajado = barajar(numeros.size * 2)
        cargarBotones()

        //Ocultamos los numeros
        for (i in 0 until botonera.size) {
            botonera[i].text = fondo
        }

        //Añadimos los eventos a los botones
        for (i in arrayBarajado.indices) {
            botonera[i].isEnabled = true
            botonera[i].setOnClickListener {
                if (!bloqueo) {
                    comprobar(i, botonera[i])
                }
            }
        }

        aciertos = 0
        intentos = 0
        TVresultado.text = "Puntuación: $aciertos"
        TVintentos.text = "Intentos: $intentos"
    }
}
