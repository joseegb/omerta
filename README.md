# OMERTÀ · MDAI · Entrega 1

## Estudiante

| Nombre | Usuario Git | Partes principales en las que ha trabajado |
|--------|-------------|--------------------------------------------|
| José Giménez Baselga | joseegb | Todo el proyecto: modelo (`NegocioLegal`), servicio (`NegocioLegalService`), API REST (`NegocioLegalAPIController`), controlador web (`NegocioLegalController`), plantillas Thymeleaf(`omerta.html`, `nuevo-negocio.html`, `index.html`), diseño de la hoja de estilos (`omerta.css`) y documentación. |

## Temática

OMERTÀ es una aplicación web de gestión para organizaciones privadas y criminales centrada en el control de sus negocios fachada o "tapaderas" (`NegocioLegal`). Su objetivo es monitorizar el volumen de ingresos legales y el dinero blanqueado, así como evaluar el nivel de escrutinio e investigación del FBI sobre cada establecimiento. En esta primera entrega se presenta un prototipo en memoria que permite consultar, filtrar y registrar negocios tanto desde una API REST como desde la interfaz web Thymeleaf.

Entidad principal: `NegocioLegal`.

| Atributo | Tipo | Descripción |
|----------|------|-------------|
| `id` | `Long` | Identificador único, asignado automáticamente por el servicio |
| `nombre` | `String` | Nombre comercial del establecimiento tapadera |
| `tipo` | `String` | Tipo o sector del negocio (`LAVANDERIA`, `RESTAURANTE`, `CONSTRUCCION`, `NIGHTCLUB`, etc.) |
| `encargado` | `String` | Nombre del responsable o encargado del negocio |
| `ingresosLegales` | `double` | Ingresos procedentes de la actividad comercial legítima (€) |
| `dineroBlanqueado` | `double` | Volumen de dinero de origen ilícito blanqueado (€) |
| `lvlRiesgoFBI` | `int` | Nivel de riesgo o investigación del FBI (escala de 1 a 10) |
| `activo` | `boolean` | Estado operativo del negocio (`true` = Operativo, `false` = Clausurado) |

Valores calculados:
- `getIngresoTotalReal()`: Devuelve la suma de `ingresosLegales + dineroBlanqueado`. Aparece en el JSON como `ingresoTotalReal`.
- `getPorcentajeBlanqueo()`: Devuelve el porcentaje de dinero blanqueado sobre el ingreso total real (`(dineroBlanqueado / ingresoTotalReal) * 100`).

Este repositorio contiene la aplicación completa de OMERTÀ desarrollada con Spring Boot y Thymeleaf.

## Vídeo de presentación

https://youtu.be/ZJ2FTOQyB80

## Cómo arrancar

Requisitos: JDK 21.

./mvnw spring-boot:run

Página de inicio: http://localhost:8080/

Para compilar y empaquetar el ejecutable:

./mvnw clean package

Para ejecutar el ejecutable `.jar` (utilizado en la demostración de la configuración externa):

java -jar target/omerta-0.0.1-SNAPSHOT.jar

Los datos viven en memoria: al reiniciar la aplicación se vuelven a cargar los 12 negocios de ejemplo definidos en el servicio.

## API REST

| Método | Ruta | Parámetros | Respuesta |
|--------|------|------------|-----------|
| GET | `/api/negocios` | `tipo` (opcional: e.g. `LAVANDERIA`, `RESTAURANTE`) | 200 y lista JSON. Sin parámetro, todos los negocios. |
| GET | `/api/negocios/{id}` | `id` numérico | 200 y objeto JSON. 404 si no existe el id. 400 si el id no es un número. |
| GET | `/api/negocios/tipo/{tipo}` | `tipo` en la ruta (e.g. `RESTAURANTE`) | 200 y lista JSON de negocios pertenecientes a ese tipo. |
| GET | `/api/negocios/riesgo` | `min` (opcional, por defecto 5) | 200 y lista de negocios con `lvlRiesgoFBI` mayor o igual a `min`. |

### Ejemplos (salidas reales)

Detalle de un negocio:

PS C:\Users\Usuario\Desktop\omerta> curl.exe -i localhost:8080/api/negocios/1
HTTP/1.1 200
Content-Type: application/json
Content-Length: 225
Date: Sun, 25 Oct 2026 18:35:31 GMT

{"id":1,"nombre":"Lavandería La Pulcra","tipo":"LAVANDERIA","encargado":"Tony Soprano","ingresosLegales":12000.0,"dineroBlanqueado":48000.0,"lvlRiesgoFBI":3,"activo":true,"ingresoTotalReal":60000.0,"porcentajeBlanqueo":80.0}

Negocio que no existe (404):

PS C:\Users\Usuario\Desktop\omerta> curl.exe -i localhost:8080/api/negocios/9999
HTTP/1.1 404
Content-Type: application/json
Transfer-Encoding: chunked
Date: Sun, 25 Oct 2026 18:35:53 GMT

{"timestamp":"2026-10-25T18:35:53.054Z","status":404,"error":"Not Found","path":"/api/negocios/9999"}

Id que no es un número (400):

PS C:\Users\Usuario\Desktop\omerta> curl.exe -i localhost:8080/api/negocios/abc
HTTP/1.1 400
Content-Type: application/json
Transfer-Encoding: chunked
Date: Sun, 25 Oct 2026 18:36:31 GMT
Connection: close

{"timestamp":"2026-10-25T18:36:31.975Z","status":400,"error":"Bad Request","path":"/api/negocios/abc"}

Búsqueda por tipo en la ruta (GET /api/negocios/tipo/RESTAURANTE):

PS C:\Users\Usuario\Desktop\omerta> curl.exe -i localhost:8080/api/negocios/tipo/RESTAURANTE
HTTP/1.1 200
Content-Type: application/json
Content-Length: 474
Date: Sun, 25 Oct 2026 18:36:58 GMT

[{"id":2,"nombre":"Pizzería Don Corleone","tipo":"RESTAURANTE","encargado":"Vito C.","ingresosLegales":25000.0,"dineroBlanqueado":95000.0,"lvlRiesgoFBI":9,"activo":true,"ingresoTotalReal":120000.0,"porcentajeBlanqueo":79.16666666666666},{"id":10,"nombre":"Trattoria Vesuvio","tipo":"RESTAURANTE","encargado":"Artie Bucco","ingresosLegales":30000.0,"dineroBlanqueado":15000.0,"lvlRiesgoFBI":4,"activo":true,"ingresoTotalReal":45000.0,"porcentajeBlanqueo":33.33333333333333}]

Consulta de negocios por nivel de riesgo mínimo (GET /api/negocios/riesgo?min=8):

PS C:\Users\Usuario\Desktop\omerta> curl.exe -i "localhost:8080/api/negocios/riesgo?min=8"
HTTP/1.1 200
Content-Type: application/json
Content-Length: 1203
Date: Sun, 25 Oct 2026 18:37:27 GMT

[{"id":2,"nombre":"Pizzería Don Corleone","tipo":"RESTAURANTE","encargado":"Vito C.","ingresosLegales":25000.0,"dineroBlanqueado":95000.0,"lvlRiesgoFBI":9,"activo":true,"ingresoTotalReal":120000.0,"porcentajeBlanqueo":79.16666666666666},{"id":3,"nombre":"Construcciones El Cimiento","tipo":"CONSTRUCCION","encargado":"Frankie Carbone","ingresosLegales":180000.0,"dineroBlanqueado":420000.0,"lvlRiesgoFBI":8,"activo":true,"ingresoTotalReal":600000.0,"porcentajeBlanqueo":70.0},{"id":4,"nombre":"Club Nocturno El Terciopelo","tipo":"NIGHTCLUB","encargado":"Silvio Dante","ingresosLegales":35000.0,"dineroBlanqueado":150000.0,"lvlRiesgoFBI":9,"activo":true,"ingresoTotalReal":185000.0,"porcentajeBlanqueo":81.08108108108108},{"id":11,"nombre":"Casino El Dorado","tipo":"CASINO","encargado":"Silvio Dante","ingresosLegales":120000.0,"dineroBlanqueado":500000.0,"lvlRiesgoFBI":8,"activo":true,"ingresoTotalReal":620000.0,"porcentajeBlanqueo":80.64516129032258},{"id":12,"nombre":"Lavandería SúperLimpio","tipo":"LAVANDERIA","encargado":"Paulie Gualtieri","ingresosLegales":5000.0,"dineroBlanqueado":60000.0,"lvlRiesgoFBI":9,"activo":false,"ingresoTotalReal":65000.0,"porcentajeBlanqueo":92.3076923076923}]

## Vistas

| Ruta | Qué muestra |
|------|-------------|
| `/` | Menú principal estático con acceso directo al panel visual y a la API REST. |
| `/negocios` | Listado general de negocios tapadera con barra de filtros por categoría. Muestra la alerta FBI activa, las tarjetas de cada local y el desglose de finanzas. |
| `/negocios?tipo=RESTAURANTE` | Listado filtrado por la categoría seleccionada. |
| `/negocios/nuevo` | Formulario HTML de alta para registrar un nuevo negocio tapadera con validación en servidor. |

## Configuración

Las propiedades propias están en `src/main/resources/application.properties`, bajo el prefijo `omerta`.

| Propiedad | Valor por defecto | Efecto |
|-----------|-------------------|--------|
| `omerta.nombre` | `OMERTÁ App` | Nombre de la organización mostrado en las cabeceras y títulos de las vistas. |
| `omerta.fbi.tipo-objetivo` | `RESTAURANTE` | Sector de negocio sobre el que la alerta especial del FBI centra su foco de investigación. |
| `omerta.fbi.incremento-riesgo` | `2` | Puntos de incremento automático aplicados al `lvlRiesgoFBI` cuando se crea un negocio del tipo investigado. |

Demostración: por defecto, la investigación del FBI se centra en los locales de tipo `RESTAURANTE` incrementando su riesgo en 2 puntos. Al arrancar el archivo `.jar` modificando los parámetros a sector `CASINO` con incremento 3:

java -jar target/omerta-0.0.1-SNAPSHOT.jar --omerta.fbi.tipo-objetivo=CASINO --omerta.fbi.incremento-riesgo=3

Al registrar un nuevo negocio de tipo `CASINO` con riesgo inicial de 8, el servicio aplicará automáticamente el incremento (+3) elevando el riesgo a 11, lo que superará el umbral máximo de 10 y clausurará automáticamente el establecimiento (`activo = false`).

## Decisiones de diseño

- **Por qué esta entidad y estos atributos.** El negocio tapadera (`NegocioLegal`) es la entidad central para el control de la red de blanqueo de capitales. Cuenta con 8 atributos de 5 tipos distintos (`Long`, `String`, `double`, `int`, `boolean`) y 2 valores calculados (`ingresoTotalReal` y `porcentajeBlanqueo`) que aportan métricas clave para las vistas Thymeleaf y los datos JSON.
- **Inyección de dependencias estricta por constructor.** Todos los controladores (`NegocioLegalController`, `NegocioLegalAPIController`) y el servicio (`NegocioLegalService`) utilizan inyección explícita por constructor con atributos declarados como `final`, evitando el uso de anotaciones `@Autowired` en campos.
- **Lógica de negocio desacoplada en el servicio.** La regla de aplicación del incremento del riesgo FBI y la desactivación automática del negocio cuando supera un nivel de riesgo de 10 están encapsuladas en `NegocioLegalService`.
- **Manejo seguro de concurrencia en memoria.** Se hace uso de la colección thread-safe `CopyOnWriteArrayList` en `NegocioLegalService` para garantizar la lecto-escritura concurrente sin bloqueos innecesarios en memoria, asegurando además la generación sincrónicamente segura de IDs correlativos mediante el bloque `synchronized` en el método `añadir`.
- **Un solo servicio para dos controladores.** `NegocioLegalController` (vistas Thymeleaf) y `NegocioLegalAPIController` (API REST) comparten el mismo bean de servicio registrado en el contenedor Spring Boot.
- **Validación en servidor y patrón PRG.** `NegocioLegalController` valida en servidor que los ingresos y montos blanqueados sean no negativos y que el nivel de riesgo FBI esté acotado entre 1 y 10. Tras guardar el negocio con éxito, aplica redirección (`redirect:/negocios`) con `RedirectAttributes` (patrón Post-Redirect-Get) para prevenir el envío duplicado del formulario al recargar.
- **Diseño visual con CSS propio (`omerta.css`).** Se implementó una hoja de estilos temática sobria con fondo oscuro (`#121212`), acentos dorados (`#c5a059`) y tonos rojo sangre (`#8b0000`), sin depender de frameworks externos. Incluye layout adaptable con CSS Grid/Flexbox, resaltado visual diferenciado para negocios de alto riesgo (borde rojo e insignia FBI cuando `lvlRiesgoFBI >= 7`) y negocios clausurados (`opacity: 0.55`).
- **Qué cambiaría al pasar a base de datos en la entrega 2.** Únicamente `NegocioLegalService`: la colección `CopyOnWriteArrayList` se sustituiría por una interfaz `NegocioLegalRepository` de Spring Data JPA, y la clase `NegocioLegal` se anotaría con `@Entity` y `@Id`. Los controladores, las plantillas HTML y la API REST permanecerían intactos.