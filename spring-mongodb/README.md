
Consideraciones realizadas en el trabajo
Se utilizó el set de datos de accidentes y se importó a la herramienta Mongo Compass
Desde la consola se realizó los siguientes cambios
-Se creó un campo del tipo Point utilizando los atributos de latitud y longitud.
-Se creó un índice 2D para las consultas GeoEspaciales.
-Como el campo Distance(mi) era del tipo String se copió los valores en un nuevo atributo Double llamado testDouble.


