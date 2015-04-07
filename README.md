# jmeter-utilities

Plugin JMeter que permite reemplazar el valor de los parámetros de una Petición HTTP.
<p>
Una de las utilidades de este plugin es poder identificar el parámetro "javax.faces.ViewState" de todas las peticiones HTTP creadas previamente y poder reemplazar el valor por el nombre de referencia que recupera dinamicamente el viewState por medio de un extractor de expresiones regulares.
</p>
<p>
Este plugin agrega una nueva opción (Utilidades) en el menu principal, y posee una sola opción 
("Reemplazar valores de Parametros"). Cuando se selecciona dicha opción, JMeter mostrara una ventana de dialogo donde el usuario puede digitar el nombre del parametro a buscar y el nuevo valor.
</p>
