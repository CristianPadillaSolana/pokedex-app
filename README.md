# 📱 Pokédex App – Android (Kotlin)

Aplicación Android que consume la **PokéAPI** para mostrar una lista paginada de Pokémon, detalle individual y manejo de estados (loading, error y reintento).

---

## 🚀 Funcionalidades

✅ Consumo de API REST (PokéAPI)  
✅ Paginación infinita (20 Pokémon por página)  
✅ Detalle completo del Pokémon  
✅ Manejo de estados (Loading / Error / Retry)  
✅ Timeout configurado para simulación de error  
✅ Arquitectura limpia por capas (data / ui)

---

## 🧠 Cómo funciona la paginación

La app utiliza los parámetros:

- `limit = 20`
- `offset` incremental

Cada vez que el usuario hace scroll cerca del final:

```kotlin
if (!isLoading && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount - 5) {
    loadPokemon()
}
```text

Se incrementa el `offset` y se cargan los siguientes 20 Pokémon desde la API.

---

## 🔌 API utilizada

https://pokeapi.co/

Endpoint principal:
GET https://pokeapi.co/api/v2/pokemon?limit=20&offset=0


---

## 🛠 Tecnologías usadas

- Kotlin
- Retrofit
- OkHttp (timeout configurado)
- Coroutines
- Glide
- RecyclerView
- ViewBinding

---

## 📂 Estructura del proyecto
data/ PokeApiService.kt Pokemon models

ui/ MainActivity.kt DetailActivity.kt Adapter

res/ layouts drawables


---

## ⚠️ Manejo de errores

- Timeout configurado en 3 segundos
- Captura de excepciones en llamadas de red
- Visualización de botón "Reintentar"
- Toast informativo

---

## 🎬 Video demo

https://www.loom.com/share/478b045d84e04add87df1a7828b1c52c

---

## 🏷 Tag de entrega
v1.0-entrega

---

## 👨‍💻 Autor

Cristian Padilla Solana
