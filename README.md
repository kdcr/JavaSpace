# JavaSpace

## Juego multijugador escrito en java y usando el motor FXGL



### Puntos clave

* Multijugador estilo battle royale
* Servidor embebido en el juego
* Sistema de físicas realista



## Manual de uso

### Presentación

​	Al abrir el juego la pantalla muestra el launcher, en el que podremos configurar los aspectos del funcionamiento del juego así como elegir con qué nave jugar, el nombre a usar...

### Cómo jugar

​	Para poder unirse a una partida es necesario introducir una dirección ip en la casilla correspondiente en la configuracion, así como el puerto. Para crear una sala de juego, solo es necesario el numero de puerto. La ip de la máqina que crea la sala debe ser compartida con el resto de jugadores, así como el puerto para que estos puedan configurar su cliente en consecuencia.

### Controles

​	Debido a que el juego presenta un movimiento semirealista, no hay pérdida de energía cinética en las naves, es decir, si se quiere frenar hay que propulsar la nave (W) en dirección contraria a su movimiento. Para orientar la nave se usan las teclas A y D, la velocidad angular también se conserva, pero se peude frenar presionando la tecla Q. Tanto la velocidad lineal como angular han sido limitadas para que el juego sea realmente jugable. Para usar el arma de la nave se pulsa la tecla de ESPACIO, hay que tener en consideración que el projectil es un láser, es decir, nos e ve afectado por la rotación de la nave.

### PvP

​	El juego sigue el famoso formato "Battle royale", en el que un número de jugadores se enfrentan entre todos y sólo el último vivo es el ganador. El campo de juego está limitado por unas franjas rojas, que al salirse de estas se recibe daño por segundo.