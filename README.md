# robworld-client-java
Cliente Java para [robworld-server](https://github.com/titos-carrasco/robworld-server)


Para generar la librería

```
$ javac -cp "dist/javax.json-1.1.4.jar:build/" -d "build/" src/*.java

$ jar -cf "dist/robworld.jar" -C build/ rcr/
```


Nota:

1. En windows reemplazar el caracter ':' por ';'
2. Utiliza librería json obtenida desde https://github.com/stleary/JSON-java
3. Ambas librerías deben estar en el classplath al momentoi de ejecutar y compilar
4. Este repositorio viene de un directorio de **robot-world** por lo que su versión se ha reiniciado en v1.1.0

